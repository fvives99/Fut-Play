package com.example.futplay.Controllers.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private ProfileFragment profileFragment;

    private EditText edTxtProfileSettingsNickname;
    private EditText edTxtProfileSettingsRegion;
    private EditText edTxtProfileSettingsPosition;
    private EditText edTxtProfileSettingsEmail;
    private EditText edTxtProfileSettingsPassword;
    private EditText edTxtProfileSettingsPhoneNumber;

    private ImageView imgVwProfileInfo;

    private ProgressBar progressBarProfileSettings;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;


    public ProfileSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSettingsFragment newInstance(String param1, String param2) {
        ProfileSettingsFragment fragment = new ProfileSettingsFragment();
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
        view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        // Additional Settings
        viewsMatching(view);
        listeners();
        initConfig();

        return view;
    }

    private void viewsMatching(View view) {
        profileFragment = new ProfileFragment();

        edTxtProfileSettingsNickname = view.findViewById(R.id.edTxtProfileSettingsNickname);
        edTxtProfileSettingsRegion = view.findViewById(R.id.edTxtProfileSettingsRegion);
        edTxtProfileSettingsPosition = view.findViewById(R.id.edTxtProfileSettingsPosition);
        edTxtProfileSettingsEmail = view.findViewById(R.id.edTxtProfileSettingsEmail);
        edTxtProfileSettingsPassword = view.findViewById(R.id.edTxtProfileSettingsPassword);
        edTxtProfileSettingsPhoneNumber = view.findViewById(R.id.edTxtProfileSettingsPhoneNumber);

        imgVwProfileInfo = view.findViewById(R.id.imgVwProfileInfo);

        progressBarProfileSettings = view.findViewById(R.id.progressBarProfileSettings);
        // FIX THIS:
        progressBarProfileSettings.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void listeners() {
        imgVwProfileInfoOnClickListener();
    }

    private void initConfig() {
        imgVwProfileInfo.bringToFront();
    }

    private void imgVwProfileInfoOnClickListener() {
        imgVwProfileInfo.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerMenu, profileFragment).addToBackStack("profile").commit());
    }
}