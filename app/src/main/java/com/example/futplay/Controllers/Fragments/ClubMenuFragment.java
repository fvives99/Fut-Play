package com.example.futplay.Controllers.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futplay.Controllers.Items.Club;
import com.example.futplay.Controllers.Items.Players;
import com.example.futplay.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClubMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClubMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //this variables are related to clubs creation
    private String userID;
    private String clubID;

    private EditText edTxtClubName;
    private EditText edTxtClubTag;
    private EditText edTxtClubRegion;

    //Club Fragment view Images
    private ImageView imgVwCreateClub;
    private ImageView imgVwJoinClub;

    //Club Fragment
    private Spinner spinnerClubJoined;

    //Club PopUps
    private Dialog popUpCreateClub;
    private Dialog popupSendJoinClubRequest;

    //TeamsFragment
    private TeamsFragment teamsFragment;

    //PopUps Progress Bars
    private ProgressBar progressBarCreateClub;
    private ProgressBar progressBarSendRequest;

    //Create Club ImageView
    private ImageView imgVwPopupCreateClubExit;
    private ImageView imgVwPopupCreateClubSave;

    //Request Join Club ImageView
    private ImageView imgVwRequestJoinCLubExit;
    private ImageView imgVwRequestJoinClubSend;
    private ImageView imgVwRequestJoinClubPaste;

    //Done popUp Variables
    private Dialog popupDone;

    private ImageView imgVwPopupDoneCheck;
    AnimatedVectorDrawableCompat avdc;
    AnimatedVectorDrawable avd;

    private TextView txtVwPopupDoneMessage;
    private TextView txtVwPopupDoneOk;

    public ClubMenuFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClubMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClubMenuFragment newInstance(String param1, String param2) {
        ClubMenuFragment fragment = new ClubMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_club_menu, container, false);
        viewsMatching(view);
        initPopupMain();
        mainListeners();
        return view;
    }

    private void viewsMatching(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void initPopupMain(){
        popUpCreateClub = new Dialog(this.getContext());
        popupDone = new Dialog(this.getContext());
        //progressBarSendRequest= view.findViewById(R.id.progressBarJoinClubRequest);
        //popupSendJoinClubRequest.setContentView(R.layout.popup_send_join_club_request);
        imgVwCreateClub = view.findViewById(R.id.imgVwCreateClub);
        imgVwJoinClub = view.findViewById(R.id.imgVwJoinClub);
    }

    private void fillUpSpinnerClubsJoined() {
    }

    private void initPopupCreateClub() {
        popUpCreateClub.setContentView(R.layout.popup_create_club);
        imgVwPopupCreateClubExit = popUpCreateClub.findViewById(R.id.imgVwPopupCreateClubCloseButton);
        imgVwPopupCreateClubSave = popUpCreateClub.findViewById(R.id.imgVwPopupCreateClubSave);
        edTxtClubName = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubName);
        edTxtClubTag = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubTag);
        edTxtClubRegion = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubRegion);
        progressBarCreateClub = popUpCreateClub.findViewById(R.id.progressBarCreateClub);
        createClubListeners();
    }

    private void mainListeners() {
        //imgVwPopupSendClubRequestClickListener();
        imgVwPopupCreateTeamOnClickListener();
    }

    private void createClubListeners(){
        imgVwPopupCreateClubSaveClickListener();
        imgVwPopupCreateClubExitClickListener();
        edTxtFieldsOnFocusChangeListeners();
    }

    private boolean invalidFields() {
        boolean invalid = false;
        if (edTxtClubName.getText().toString().trim().equals("")) {
            edTxtClubName.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtClubTag.getText().toString().trim().equals("")) {
            edTxtClubTag.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtClubRegion.getText().toString().equals("")) {
            edTxtClubName.setError("Campo Obligatorio");
            invalid = true;
        }

        return invalid;
    }

    private void edTxtFieldsOnFocusChangeListeners() {
        edTxtClubName.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !edTxtClubName.getText().toString().equals("") && edTxtClubName.getText().toString().length() < 3 || edTxtClubName.getText().toString().length() > 30) {
                edTxtClubName.setError("El nombre del club debe tener de 3 a 30 caracteres");
            }
        });

        edTxtClubTag.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !edTxtClubTag.getText().toString().equals("") && edTxtClubTag.getText().toString().length() > 3 || edTxtClubTag.getText().toString().length() < 3) {
                edTxtClubTag.setError("El TAG del club tiene que tener 3 letras mayúsculas");
            }
        });
    }

    private void clearFields() {
        edTxtClubName.setText("");
        edTxtClubTag.setText("");
        edTxtClubRegion.setText("");
    }

    private void clearErrors() {
        edTxtClubName.setError(null);
        edTxtClubTag.setError(null);
        edTxtClubRegion.setError(null);
    }

    private String insertClub() {
        if (!invalidFields()) {
            progressBarCreateClub.setVisibility(View.VISIBLE);
            Club newClub = new Club();
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            newClub.setClubName(camelCase(edTxtClubName.getText().toString().trim().replaceAll(" +", " ")));
            newClub.setClubTag(edTxtClubTag.getText().toString().trim());
            newClub.setClubRegion(edTxtClubRegion.getText().toString().trim());
            newClub.setMatchesWon(0);newClub.setMatchesLost(0);newClub.setMatchesTied(0);
            Players newMember = new Players(userID);
            newMember.setPrivileges("C");
            newClub.addMember(newMember);
            clubID = newClub.setClubID();
            DocumentReference documentReference = firebaseFirestore.collection("clubs").document(clubID);
            documentReference.set(newClub);
            clearFields();
            clearErrors();
            return newClub.getClubName();
        }else{
            return "";
        }

    }

    public static String camelCase(String str) {
        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int length = builder.length();

        for (int i = 0; i < length; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }

    private void imgVwPopupCreateTeamOnClickListener() {
        if(imgVwCreateClub != null){
            imgVwCreateClub.setOnClickListener(v -> {
                initPopupCreateClub();
                setupPopupLayoutParams(popUpCreateClub);
                popUpCreateClub.setCancelable(false);
                popUpCreateClub.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpCreateClub.show();
                progressBarCreateClub.setVisibility(View.GONE);
            });
        }
    }

    private void imgVwPopupSendClubRequestClickListener() {
        if(imgVwJoinClub != null){
            imgVwJoinClub.setOnClickListener(v -> {
                popupSendJoinClubRequest.dismiss();
            });
        }
    }
    private void imgVwPopupCreateClubExitClickListener() {
        if(imgVwPopupCreateClubExit != null){
            imgVwPopupCreateClubExit.setOnClickListener(v -> {
                popUpCreateClub.dismiss();
            });
        }
    }
    private void imgVwPopupCreateClubSaveClickListener() {
        if(imgVwPopupCreateClubSave != null){
            imgVwPopupCreateClubSave.setOnClickListener(v -> {
                String clubName = insertClub();
                if(clubName != ""){
                    progressBarCreateClub.setVisibility(View.GONE);
                    displayPopupDone("Club "+clubName+" Creado Exitósamente!!");
                    popUpCreateClub.dismiss();
                }
            });
        }
    }

    // Done PopUp functions

    private void displayPopupDone(String message) {
        initPopupDone(message);
        popupDoneListeners();
        setupPopupLayoutParams(popupDone);

        popupDone.setCancelable(false);
        popupDone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDone.show();
        initPopupDoneAnims();
    }

    private void initPopupDoneAnims() {
        int delay = 500;
        imgVwPopupDoneCheck.setVisibility(View.GONE);
        view.postDelayed(() -> {
            imgVwPopupDoneCheck.setVisibility(View.VISIBLE);
            Drawable drawable = imgVwPopupDoneCheck.getDrawable();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                avdc = (AnimatedVectorDrawableCompat) drawable;
                avdc.start();
            } else if (drawable instanceof AnimatedVectorDrawable) {
                avd = (AnimatedVectorDrawable) drawable;
                avd.start();
            }
        }, delay);
    }

    private void initPopupDone(String message) {
        popupDone.setContentView(R.layout.popup_done);

        imgVwPopupDoneCheck = popupDone.findViewById(R.id.imgVwPopupDoneCheck);

        txtVwPopupDoneMessage = popupDone.findViewById(R.id.txtVwPopupDoneMessage);
        txtVwPopupDoneOk = popupDone.findViewById(R.id.txtVwPopupDoneOk);

        txtVwPopupDoneMessage.setText(message);
    }

    private void popupDoneListeners() {
        txtVwPopupDoneOkOnClickListener();
    }
    private void txtVwPopupDoneOkOnClickListener() {
        txtVwPopupDoneOk.setOnClickListener(v -> popupDone.dismiss());
    }

    // End of Done PopUp functions

    private void setupPopupLayoutParams(Dialog popup) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        popup.getWindow().setAttributes(lp);
    }

}