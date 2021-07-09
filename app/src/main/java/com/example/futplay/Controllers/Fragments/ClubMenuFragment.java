package com.example.futplay.Controllers.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futplay.Controllers.Items.Club;
import com.example.futplay.Controllers.Items.Players;
import com.example.futplay.Controllers.Items.Requests;
import com.example.futplay.Controllers.Items.UserClubs;
import com.example.futplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

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
    private EditText edTxtPopupProfileInfoRegion;

    //Region Picker variables
    private Dialog regionPicker;

    private NumberPicker regionPickerProvinces;
    private NumberPicker regionPickerCantons;
    private NumberPicker regionPickerDistricts;

    private ArrayList<Integer> regionChosenValues;

    private TextView txtVwRegionPickerAccept;

    //Club Fragment view Images
    private TextView txtVwCreateClub;
    private TextView txtVwJoinClub;

    //Club PopUps
    private Dialog popUpCreateClub;
    private Dialog popupSendJoinClubRequest;

    //PopUps Progress Bars
    private ProgressBar progressBarCreateClub;
    private ProgressBar progressBarSendRequest;

    //Create Club ImageView
    private ImageView imgVwPopupCreateClubExit;
    private ImageView imgVwPopupCreateClubSave;

    //Request Join Club ImageView
    private ImageView imgVwRequestJoinCLubExit;
    private ImageView imgVwRequestJoinClubSend;

    //Request Join Club EditText
    private EditText edTxtRequestClubTag;
    private EditText edTxtRequestClubID;

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
        popupSendJoinClubRequest = new Dialog(this.getContext());
        regionPicker = new Dialog(this.getContext());
        txtVwCreateClub = view.findViewById(R.id.txtVwCreateClub);
        txtVwJoinClub = view.findViewById(R.id.txtVwRequestJoinClub);
    }

    private void initRegionPicker() {
        regionPicker.setContentView(R.layout.region_picker);

        txtVwRegionPickerAccept = regionPicker.findViewById(R.id.txtVwRegionPickerAccept);

        regionPickerProvinces = regionPicker.findViewById(R.id.regionPickerProvinces);
        regionPickerCantons = regionPicker.findViewById(R.id.regionPickerCantons);
        regionPickerDistricts = regionPicker.findViewById(R.id.regionPickerDistricts);

        regionChosenValues = new ArrayList<>();
    }


    private void initPopupCreateClub() {
        popUpCreateClub.setContentView(R.layout.popup_create_club);
        initRegionPicker();
        imgVwPopupCreateClubExit = popUpCreateClub.findViewById(R.id.imgVwPopupCreateClubCloseButton);
        imgVwPopupCreateClubSave = popUpCreateClub.findViewById(R.id.imgVwPopupCreateClubSave);
        edTxtClubName = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubName);
        edTxtClubTag = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubTag);
        edTxtPopupProfileInfoRegion = popUpCreateClub.findViewById(R.id.edTxtPopupCreateClubRegion);
        progressBarCreateClub = popUpCreateClub.findViewById(R.id.progressBarCreateClub);
        createClubListeners();
    }

    private void initPopupJoinRequest() {
        popupSendJoinClubRequest.setContentView(R.layout.popup_send_join_club_request);
        imgVwRequestJoinCLubExit = popupSendJoinClubRequest.findViewById(R.id.imgVwPopupJoinClubRequestCloseButton);
        imgVwRequestJoinClubSend = popupSendJoinClubRequest.findViewById(R.id.imgVwPopupSendJoinClubRequest);
        edTxtRequestClubID = popupSendJoinClubRequest.findViewById(R.id.txtVwPopupJoinClubRequestID);
        edTxtRequestClubTag = popupSendJoinClubRequest.findViewById(R.id.txtVwPopupJoinClubRequestTag);
        progressBarSendRequest = popupSendJoinClubRequest.findViewById(R.id.progressBarJoinClubRequest);
        joinRequestListeners();
    }

    private void mainListeners() {
        imgVwPopupClubRequestClickListener();
        imgVwPopupCreateTeamOnClickListener();
    }

    private void createClubListeners(){
        edTxtPopupProfileInfoRegionAddTextChangedListener();
        edTxtPopupProfileInfoRegionOnClickListener();
        imgVwPopupCreateClubSaveClickListener();
        imgVwPopupCreateClubExitClickListener();
        createClubEdTxtFieldsOnFocusChangeListeners();
    }

    private void joinRequestListeners(){
        imgVwPopupJoinRequestExitClickListener();
        imgVwCreateClubRequestSendClickListener();
        sendRequestEdTxtFieldsOnFocusChangeListeners();
    }
 //CREATE TEAM LISTENERS
    private void imgVwPopupCreateTeamOnClickListener() {
        if(txtVwCreateClub != null){
            txtVwCreateClub.setOnClickListener(v -> {
                initPopupCreateClub();
                setupPopupLayoutParams(popUpCreateClub);
                popUpCreateClub.setCancelable(false);
                popUpCreateClub.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpCreateClub.show();
                progressBarCreateClub.setVisibility(View.GONE);
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
               insertClub();
            });
        }
    }
    //CLUB JOIN REQUEST LISTENERS
    private void imgVwPopupClubRequestClickListener() {
        if(txtVwJoinClub != null){
            txtVwJoinClub.setOnClickListener(v -> {
                initPopupJoinRequest();
                setupPopupLayoutParams(popupSendJoinClubRequest);
                popupSendJoinClubRequest.setCancelable(false);
                popupSendJoinClubRequest.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupSendJoinClubRequest.show();
                progressBarSendRequest.setVisibility(View.GONE);
            });
        }
    }

    private void imgVwPopupJoinRequestExitClickListener() {
        if(imgVwRequestJoinCLubExit != null){
            imgVwRequestJoinCLubExit.setOnClickListener(v -> {
                popupSendJoinClubRequest.dismiss();
            });
        }
    }

    private void imgVwCreateClubRequestSendClickListener() {
        if(imgVwRequestJoinClubSend != null){
            imgVwRequestJoinClubSend.setOnClickListener(v -> {
                createRequest();
            });
        }
    }
    //INPUT VALIDATION METHODS
    private boolean createClubInvalidFields() {
        boolean invalid = false;
        if (edTxtClubName.getText().toString().trim().equals("")) {
            edTxtClubName.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtClubTag.getText().toString().trim().equals("")) {
            edTxtClubTag.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtPopupProfileInfoRegion.getText().toString().equals("")) {
            edTxtClubName.setError("Campo Obligatorio");
            invalid = true;
        }

        return invalid;
    }

    private boolean joinRequestInvalidFields() {
        boolean invalid = false;
        if (edTxtRequestClubTag.getText().toString().trim().equals("")) {
            edTxtClubName.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtRequestClubID.getText().toString().trim().equals("")) {
            edTxtClubTag.setError("Campo Obligatorio");
            invalid = true;
        }

        return invalid;
    }

    private void createClubEdTxtFieldsOnFocusChangeListeners() {
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

    private void sendRequestEdTxtFieldsOnFocusChangeListeners() {
        edTxtRequestClubTag.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !edTxtRequestClubTag.getText().toString().equals("") && edTxtRequestClubTag.getText().toString().length() > 3 || edTxtRequestClubTag.getText().toString().length() < 3) {
                edTxtRequestClubTag.setError("El TAG del club tiene que tener 3 letras mayúsculas");
            }
        });
    }

    private void createClubClearFields() {
        edTxtClubName.setText("");
        edTxtClubTag.setText("");
        edTxtPopupProfileInfoRegion.setText("");
    }

    private void createClubClearErrors() {
        edTxtClubName.setError(null);
        edTxtClubTag.setError(null);
        edTxtPopupProfileInfoRegion.setError(null);
    }

    private void sendRequestClearFields() {
        edTxtRequestClubID.setText("");
        edTxtRequestClubTag.setText("");
    }

    private void sendRequestClearErrors() {
        edTxtRequestClubID.setError(null);
        edTxtRequestClubTag.setError(null);
    }

    private void insertClub() {
        if (!createClubInvalidFields()) {
            progressBarCreateClub.setVisibility(View.VISIBLE);
            Club newClub = new Club();
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            newClub.setClubName(edTxtClubName.getText().toString().trim().replaceAll(" +", " "));
            newClub.setClubTag(edTxtClubTag.getText().toString().trim());
            newClub.setClubRegion(edTxtPopupProfileInfoRegion.getText().toString().trim());
            newClub.setMatchesWon(0);newClub.setMatchesLost(0);newClub.setMatchesTied(0);
            //Players newMember = new Players(userID);
            //newMember.setPrivileges("C");
            newClub.addMember(userID);
            clubID = newClub.setClubID();

            DocumentReference docRef = firebaseFirestore.collection("clubs").document(clubID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("firebase", "DocumentSnapshot data: " + document.getData());
                            Toast toast = Toast.makeText(getActivity(),
                                    "Este club ya existe en nuestra base de datos, prueba con otro nombre!",
                                    Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            Log.d("firebase", "No such document");
                            docRef.set(newClub)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("firebase", "DocumentSnapshot successfully written!");
                                            DocumentReference verifyUserRef = firebaseFirestore.collection("users").document(userID);
                                            verifyUserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                                    if (userTask.isSuccessful()) {
                                                        DocumentSnapshot userDocument = userTask.getResult();
                                                        if (userDocument.exists()) {
                                                            Map<String, Object> user = userDocument.getData();
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(user.get("userClubs"));
                                                            UserClubs temp = new Gson().fromJson(json, UserClubs.class);
                                                            temp.addClub(newClub.getClubID());
                                                            user.put("userClubs",temp);
                                                            verifyUserRef.set(user,SetOptions.merge());
                                                            displayPopupDone("Club "+newClub.getClubName()+" Creado Exitósamente!!");
                                                            View profileMenuOption = getActivity().findViewById(R.id.imgVwMenuProfile);
                                                            profileMenuOption.performClick();


                                                        } else {
                                                            Log.d("firebase", "get failed with ", task.getException());
                                                            Toast toast = Toast.makeText(getActivity(),
                                                                    "Se ha producido un error, intenta de nuevo más tarde!",
                                                                    Toast.LENGTH_SHORT);
                                                            toast.show();
                                                            createClubClearFields();
                                                            createClubClearErrors();
                                                            progressBarCreateClub.setVisibility(View.GONE);
                                                        }
                                                    } else {
                                                        Log.d("firebase", "get failed with ", task.getException());
                                                        Toast toast = Toast.makeText(getActivity(),
                                                                "Se ha producido un error, intenta de nuevo más tarde!",
                                                                Toast.LENGTH_SHORT);
                                                        toast.show();
                                                        createClubClearFields();
                                                        createClubClearErrors();
                                                        progressBarCreateClub.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                            createClubClearFields();
                                            createClubClearErrors();
                                            popUpCreateClub.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("firebase", "Error writing document", e);
                                            Log.d("firebase", "get failed with ", task.getException());
                                            Toast toast = Toast.makeText(getActivity(),
                                                    "No se ha podido añadir el club a nuestra base de datos, intenta de nuevo más tarde!",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                            createClubClearFields();
                                            createClubClearErrors();
                                            progressBarCreateClub.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    } else {
                        Log.d("firebase", "get failed with ", task.getException());
                        Toast toast = Toast.makeText(getActivity(),
                                "No se ha podido acceder a la base de datos, intenta de nuevo más tarde!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        createClubClearFields();
                        createClubClearErrors();
                        progressBarCreateClub.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    private void createRequest() {
        if (!joinRequestInvalidFields()) {
            progressBarSendRequest.setVisibility(View.VISIBLE);
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            String clubRequestID = edTxtRequestClubTag.getText().toString().trim()+"#"+edTxtRequestClubID.getText().toString().trim();
            String requestID = clubRequestID + "-" + userID;
            DocumentReference docRef = firebaseFirestore.collection("clubs").document(clubRequestID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("firebase", "DocumentSnapshot data: " + document.getData());
                            Club  clubFound = document.toObject(Club.class);
                            String clubName = clubFound.getClubName();
                            DocumentReference verifyRequestRef = firebaseFirestore.collection("requests").document(requestID);
                            verifyRequestRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> reqTask) {
                                    if (reqTask.isSuccessful()) {
                                        DocumentSnapshot requestDocument = reqTask.getResult();
                                        if (requestDocument.exists()) {
                                            Log.d("firebase", "DocumentSnapshot data: " + requestDocument.getData());
                                            Toast toast = Toast.makeText(getActivity(),
                                                    "Ya tienes una solicitud pendiente con "+clubName+"!",
                                                    Toast.LENGTH_SHORT);
                                            toast.show();
                                            sendRequestClearFields();
                                            sendRequestClearErrors();
                                            progressBarSendRequest.setVisibility(View.GONE);
                                        } else {
                                            Log.d("firebase", "No such document");
                                            Requests newRequest = new Requests(requestID,"Pending");
                                            DocumentReference requestReference= firebaseFirestore.collection("requests").document(requestID);
                                            requestReference.set(newRequest);
                                            displayPopupDone("Solicitud a "+clubName+" Enviada Exitósamente!!");
                                            View profileMenuOption = getActivity().findViewById(R.id.imgVwMenuProfile);
                                            profileMenuOption.performClick();
                                            progressBarSendRequest.setVisibility(View.GONE);
                                            popupSendJoinClubRequest.dismiss();
                                        }
                                    } else {
                                        Log.d("firebase", "get failed with ", task.getException());
                                        Toast toast = Toast.makeText(getActivity(),
                                                "No se ha podido acceder a la base de datos, intenta de nuevo más tarde!",
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                        sendRequestClearFields();
                                        sendRequestClearErrors();
                                        progressBarSendRequest.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Log.d("firebase", "No such document");
                            Toast toast = Toast.makeText(getActivity(),
                                    "No existe el club que ingresaste, revisa la info y vuelve a intentarlo",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            sendRequestClearFields();
                            sendRequestClearErrors();
                            progressBarSendRequest.setVisibility(View.GONE);
                        }
                    } else {
                        Log.d("firebase", "get failed with ", task.getException());
                        Toast toast = Toast.makeText(getActivity(),
                                "No se ha podido acceder a la base de datos, intenta de nuevo más tarde!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendRequestClearFields();
                        sendRequestClearErrors();
                        progressBarSendRequest.setVisibility(View.GONE);
                    }
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


    //-------------------------------------------------RegionPicker Methods-----------------------------------------------------------------------

    private void displayRegionPicker() {
        regionPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        regionPicker.show();
    }

    private void edTxtPopupProfileInfoRegionAddTextChangedListener() {
        edTxtPopupProfileInfoRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edTxtPopupProfileInfoRegion.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void edTxtPopupProfileInfoRegionOnClickListener() {
        edTxtPopupProfileInfoRegion.setOnClickListener(v -> {
            if (edTxtPopupProfileInfoRegion.getText().toString().equals("")) {
                initRegionPicker();
                regionPickerListeners();
                populateAndDisplayRegionPickers();
            } else {
                regionPickerProvinces.setValue(regionChosenValues.get(0));
                regionPickerCantons.setValue(regionChosenValues.get(1));
                regionPickerDistricts.setValue(regionChosenValues.get(2));
                displayRegionPicker();
            }
        });
    }

    private void regionPickerListeners() {
        regionPickerProvincesOnValueChangedListener();
        regionPickerCantonsOnValueChangedListener();
        txtVwRegionPickerAcceptOnClickListener();
    }

    private void regionPickerProvincesOnValueChangedListener() {
        regionPickerProvinces.setOnValueChangedListener((picker, oldVal, newVal) -> {
            picker.setOnScrollListener((regionPicker, scrollState) -> {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    String province = String.valueOf(regionPicker.getValue() + 1);
                    String canton = String.valueOf(regionPickerCantons.getValue() + 1);
                    String districtsList = province + "_" + canton;
                    regionPickerProvinceChange(province, districtsList);
                }
            });
            String province = String.valueOf(newVal + 1);
            String canton = String.valueOf(regionPickerCantons.getValue() + 1);
            String districtsList = province + "_" + canton;
            regionPickerProvinceChange(province, districtsList);
        });
    }

    private void regionPickerCantonsOnValueChangedListener() {
        regionPickerCantons.setOnValueChangedListener((picker, oldVal, newVal) -> {
            picker.setOnScrollListener((regionPicker, scrollState) -> {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    String province = String.valueOf(regionPickerProvinces.getValue() + 1);
                    String canton = String.valueOf(regionPicker.getValue() + 1);
                    String districtsList = province + "_" + canton;
                    regionPickerCantonChange(districtsList);
                }
            });
            String province = String.valueOf(regionPickerProvinces.getValue() + 1);
            String canton = String.valueOf(newVal + 1);
            String districtsList = province + "_" + canton;
            regionPickerCantonChange(districtsList);
        });
    }

    private void regionPickerProvinceChange(String province, String districtsList) {
        DocumentReference documentReference = firebaseFirestore.collection("countries_addresses").document("cri_addresses");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            populateRegionPicker((String) documentSnapshot.get(province), province, regionPickerCantons);
            populateRegionPicker((String) documentSnapshot.get(districtsList), districtsList, regionPickerDistricts);
        });
    }

    private void regionPickerCantonChange(String districtsList) {
        DocumentReference documentReference = firebaseFirestore.collection("countries_addresses").document("cri_addresses");
        documentReference.get().addOnSuccessListener(documentSnapshot ->
                populateRegionPicker((String) documentSnapshot.get(districtsList), districtsList, regionPickerDistricts));
    }

    private void txtVwRegionPickerAcceptOnClickListener() {
        txtVwRegionPickerAccept.setOnClickListener(v1 -> {
            String region = regionPickerProvinces.getDisplayedValues()[regionPickerProvinces.getValue()] + "/"
                    + regionPickerCantons.getDisplayedValues()[regionPickerCantons.getValue()] + "/"
                    + regionPickerDistricts.getDisplayedValues()[regionPickerDistricts.getValue()];
            edTxtPopupProfileInfoRegion.setText(region);
            fillUpRegionChosenValues();
            regionPicker.dismiss();
        });
    }

    private void fillUpRegionChosenValues() {
        regionChosenValues.clear();
        regionChosenValues.add(regionPickerProvinces.getValue());
        regionChosenValues.add(regionPickerCantons.getValue());
        regionChosenValues.add(regionPickerDistricts.getValue());
    }

    private void populateRegionPicker(String json, String arrayName, NumberPicker picker) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray(arrayName);
                String[] values = new String[jsonArray.length()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = jsonArray.getString(i);
                }
                picker.setValue(0);
                picker.setDisplayedValues(null);
                picker.setMinValue(0);
                picker.setMaxValue(values.length - 1);
                picker.setDisplayedValues(values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateAndDisplayRegionPickers() {
        DocumentReference documentReference = firebaseFirestore.collection("countries_addresses").document("cri_addresses");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                populateRegionPicker((String) documentSnapshot.get("0"), "0", regionPickerProvinces);
                populateRegionPicker((String) documentSnapshot.get("1"), "1", regionPickerCantons);
                populateRegionPicker((String) documentSnapshot.get("1_1"), "1_1", regionPickerDistricts);
            }
            displayRegionPicker();
        });
    }

}


