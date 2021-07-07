package com.example.futplay.Controllers.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.futplay.Controllers.Activities.CropImageActivity;
import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private Dialog popupProfileInfo;

    private ImageView imgVwPopupProfileInfoSave;

    private EditText edTxtPopupProfileInfoNickname;
    private EditText edTxtPopupProfileInfoRegion;

    private Spinner spinnerPopupProfileInfoPosition;

    private Dialog regionPicker;

    private TextView txtVwRegionPickerAccept;

    private NumberPicker regionPickerProvinces;
    private NumberPicker regionPickerCantons;
    private NumberPicker regionPickerDistricts;

    private Dialog popupProfileSettings;

    private EditText edTxtPopupProfileSettingsFullName;
    private EditText edTxtPopupProfileSettingsNickname;
    private EditText edTxtPopupProfileSettingsRegion;
    private EditText edTxtPopupProfileSettingsAge;
    private EditText edTxtPopupProfileSettingsEmail;
    private EditText edTxtPopupProfileSettingsNewPassword;
    private EditText edTxtPopupProfileSettingsConfirmNewPassword;
    private EditText edTxtPopupProfileSettingsPhoneNumber;

    private Spinner spinnerPopupProfileSettingsPosition;

    private ImageView imgVwPopupProfileSettingsSave;
    private ImageView imgVwPopupProfileSettingsClose;

    private CountryCodePicker countryCodePickerPopupProfileSettings;

    private ProgressBar progressBarPopupProfileSettings;

    private Dialog popupDone;

    private ImageView imgVwPopupDoneCheck;

    private TextView txtVwPopupDoneMessage;
    private TextView txtVwPopupDoneOk;

    AnimatedVectorDrawableCompat avdc;
    AnimatedVectorDrawable avd;

    private TextView txtVwProfileNickname;
    private TextView txtVwProfileRegion;
    private TextView txtVwProfilePosition;
    private TextView txtVwProfileAge;
    private TextView txtVwProfileStatistics;
    private TextView txtVwProfileMatchesPlayed;
    private TextView txtVwProfileMVP;
    private TextView txtVwProfileFutPlayPlayer;
    private TextView txtVwProfileMatchesPlayedNumber;
    private TextView txtVwProfileMVPNumber;
    private TextView txtVwProfileFutPlayPlayerNumber;

    private ImageView imgVwProfileImg;
    private ImageView imgVwProfileSettings;

    private ArrayList<Integer> regionChosenValues;

    private ProgressBar progressBarProfile;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private String userID;
    private String currentPhotoPath;

    public static Bitmap profileImage = null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Additional Settings
        viewsMatching(view);
        hideViews();
        displayPopupProfileInfo();
        retrieveData();
        permissions();
        listeners();
        initConfig();

        return view;
    }

    private void viewsMatching(View view) {
        popupProfileInfo = new Dialog(this.getContext());
        regionPicker = new Dialog(this.getContext());
        popupProfileSettings = new Dialog(this.getContext());
        popupDone = new Dialog(this.getContext());

        txtVwProfileNickname = view.findViewById(R.id.txtVwProfileNickname);
        txtVwProfileRegion = view.findViewById(R.id.txtVwProfileRegion);
        txtVwProfilePosition = view.findViewById(R.id.txtVwProfilePosition);
        txtVwProfileAge = view.findViewById(R.id.txtVwProfileAge);
        txtVwProfileStatistics = view.findViewById(R.id.txtVwProfileStatistics);
        txtVwProfileMatchesPlayed = view.findViewById(R.id.txtVwProfileMatchesPlayed);
        txtVwProfileMVP = view.findViewById(R.id.txtVwProfileMVP);
        txtVwProfileFutPlayPlayer = view.findViewById(R.id.txtVwProfileFutPlayPlayer);
        txtVwProfileMatchesPlayedNumber = view.findViewById(R.id.txtVwProfileMatchesPlayedNumber);
        txtVwProfileMVPNumber = view.findViewById(R.id.txtVwProfileMVPNumber);
        txtVwProfileFutPlayPlayerNumber = view.findViewById(R.id.txtVwProfileFutPlayPlayerNumber);

        imgVwProfileImg = view.findViewById(R.id.imgVwProfileImg);
        imgVwProfileSettings = view.findViewById(R.id.imgVwProfileSettings);

        progressBarProfile = view.findViewById(R.id.progressBarProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    private void hideViews() {
        txtVwProfileNickname.setVisibility(View.GONE);
        txtVwProfileRegion.setVisibility(View.GONE);
        txtVwProfilePosition.setVisibility(View.GONE);
        txtVwProfileAge.setVisibility(View.GONE);
        txtVwProfileStatistics.setVisibility(View.GONE);
        txtVwProfileMatchesPlayed.setVisibility(View.GONE);
        txtVwProfileMVP.setVisibility(View.GONE);
        txtVwProfileFutPlayPlayer.setVisibility(View.GONE);
        txtVwProfileMatchesPlayedNumber.setVisibility(View.GONE);
        txtVwProfileMVPNumber.setVisibility(View.GONE);
        txtVwProfileFutPlayPlayerNumber.setVisibility(View.GONE);

        imgVwProfileImg.setVisibility(View.GONE);
        imgVwProfileSettings.setVisibility(View.GONE);
    }

    private void showViews() {
        txtVwProfileNickname.setVisibility(View.VISIBLE);
        txtVwProfileRegion.setVisibility(View.VISIBLE);
        txtVwProfilePosition.setVisibility(View.VISIBLE);
        txtVwProfileAge.setVisibility(View.VISIBLE);
        txtVwProfileStatistics.setVisibility(View.VISIBLE);
        txtVwProfileMatchesPlayed.setVisibility(View.VISIBLE);
        txtVwProfileMVP.setVisibility(View.VISIBLE);
        txtVwProfileFutPlayPlayer.setVisibility(View.VISIBLE);
        txtVwProfileMatchesPlayedNumber.setVisibility(View.VISIBLE);
        txtVwProfileMVPNumber.setVisibility(View.VISIBLE);
        txtVwProfileFutPlayPlayerNumber.setVisibility(View.VISIBLE);

        imgVwProfileImg.setVisibility(View.VISIBLE);
        imgVwProfileSettings.setVisibility(View.VISIBLE);
    }

    private void initPopupProfileInfo() {
        popupProfileInfo.setContentView(R.layout.popup_profile_info);

        imgVwPopupProfileInfoSave = popupProfileInfo.findViewById(R.id.imgVwPopupProfileInfoSave);

        edTxtPopupProfileInfoNickname = popupProfileInfo.findViewById(R.id.edTxtPopupProfileInfoNickname);
        edTxtPopupProfileInfoRegion = popupProfileInfo.findViewById(R.id.edTxtPopupProfileInfoRegion);

        spinnerPopupProfileInfoPosition = popupProfileInfo.findViewById(R.id.spinnerPopupProfileInfoPosition);

        fillUpSpinnerPopupProfileInfoPosition();
    }

    private void initRegionPicker() {
        regionPicker.setContentView(R.layout.region_picker);

        txtVwRegionPickerAccept = regionPicker.findViewById(R.id.txtVwRegionPickerAccept);

        regionPickerProvinces = regionPicker.findViewById(R.id.regionPickerProvinces);
        regionPickerCantons = regionPicker.findViewById(R.id.regionPickerCantons);
        regionPickerDistricts = regionPicker.findViewById(R.id.regionPickerDistricts);

        regionChosenValues = new ArrayList<>();
    }

    private void popupProfileInfoListeners() {
        edTxtPopupProfileInfoNicknameOnEditorActionListener();
        edTxtPopupProfileInfoNicknameAddTextChangedListener();
        edTxtPopupProfileInfoRegionAddTextChangedListener();
        edTxtPopupProfileInfoRegionOnClickListener();
        spinnerPopupProfileInfoPositionOnItemSelectedListener();
        imgVwPopupProfileInfoSaveOnClickListener();
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

    public void regionPickerProvinceChange(String province, String districtsList) {
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

    private void edTxtPopupProfileInfoNicknameOnEditorActionListener() {
        edTxtPopupProfileInfoNickname.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                edTxtPopupProfileInfoRegion.performClick();
                edTxtPopupProfileInfoNickname.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void edTxtPopupProfileInfoNicknameAddTextChangedListener() {
        edTxtPopupProfileInfoNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edTxtPopupProfileInfoNickname.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void fillUpSpinnerPopupProfileInfoPosition() {
        DocumentReference documentReference = firebaseFirestore.collection("spinners_data").document("positions");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String json = (String) documentSnapshot.get("spanish");
                try {
                    if (json != null) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("spanish");
                        int jsonArrayLength = jsonArray.length();
                        ArrayList<String> positionsValues = new ArrayList<>();
                        positionsValues.add("Posición:");
                        for (int i = 0; i < jsonArrayLength; i++) {
                            positionsValues.add(jsonArray.getString(i));
                        }
                        ArrayAdapter<String> positionsArrayAdapter = new ArrayAdapter<>(popupProfileInfo.getContext(), R.layout.custom_spinner_dialog_item, R.id.txtVwCustomSpinnerDialogItemText, positionsValues);
                        spinnerPopupProfileInfoPosition.setAdapter(positionsArrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void spinnerPopupProfileInfoPositionOnItemSelectedListener() {
        spinnerPopupProfileInfoPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray));
                } else {
                    ((TextView) ((LinearLayout) parent.getChildAt(0)).getChildAt(0)).setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                    ((TextView) ((LinearLayout) spinnerPopupProfileInfoPosition.getSelectedView()).getChildAt(0)).setError(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void imgVwPopupProfileInfoSaveOnClickListener() {
        imgVwPopupProfileInfoSave.setOnClickListener(v -> {
            if (!invalidFields()) {
                String nickname = edTxtPopupProfileInfoNickname.getText().toString();
                String region = edTxtPopupProfileInfoRegion.getText().toString();
                String position = spinnerPopupProfileInfoPosition.getSelectedItem().toString();
                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("nickname", nickname);
                user.put("region", region);
                user.put("position", position);
                documentReference.update(user).addOnSuccessListener(command -> {
                    displayPopupDone("¡Perfil Completado!");
                    completeProfileInfo();
                });
                popupProfileInfo.dismiss();
            }
        });
    }

    private void displayPopupDone(String message) {
        initPopupDone(message);
        popupDoneListeners();
        setupPopupLayoutParams(popupDone);

        popupDone.setCancelable(false);
        popupDone.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDone.show();
        initPopupDoneAnims();
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

    private boolean invalidFields() {
        boolean invalid = false;
        if (edTxtPopupProfileInfoNickname.getText().toString().trim().equals("")) {
            edTxtPopupProfileInfoNickname.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtPopupProfileInfoRegion.getText().toString().equals("")) {
            edTxtPopupProfileInfoRegion.setError("Campo Obligatorio");
            invalid = true;
        }
        if (spinnerPopupProfileInfoPosition.getSelectedItemPosition() == 0) {
            ((TextView) ((LinearLayout) spinnerPopupProfileInfoPosition.getSelectedView()).getChildAt(0)).setError("Campo Obligatorio");
            invalid = true;
        }
        return invalid;
    }

    private void displayPopupProfileInfo() {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (Objects.equals(documentSnapshot.get("nickname"), "")) {
                initPopupProfileInfo();
                popupProfileInfoListeners();
                setupPopupLayoutParams(popupProfileInfo);

                popupProfileInfo.setCancelable(false);
                popupProfileInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupProfileInfo.show();
            } else {
                completeProfileInfo();
            }
        });
    }

    private void completeProfileInfo() {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String nickname = (String) Objects.requireNonNull(documentSnapshot.get("nickname"));
            String region = ((String) Objects.requireNonNull(documentSnapshot.get("region"))).split("/")[0];
            String position = (String) Objects.requireNonNull(documentSnapshot.get("position"));
            txtVwProfileNickname.setText(nickname);
            txtVwProfileRegion.setText(region);
            txtVwProfilePosition.setText(position);
        });
    }

    private void setupPopupLayoutParams(Dialog popup) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        popup.getWindow().setAttributes(lp);
    }

    private void displayRegionPicker() {
        regionPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        regionPicker.show();
    }

    private void retrieveData() {
        retrieveProfileImage();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String age = calculateAge((String) Objects.requireNonNull(documentSnapshot.get("birthDate")));
            txtVwProfileAge.setText(age);

            progressBarProfile.setVisibility(View.GONE);
            showViews();
        });
    }

    private void retrieveProfileImage() {
        checkCroppedImage();
        StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(imgVwProfileImg));
    }

    private void checkCroppedImage() {
        if (profileImage != null) {
            uploadImageToFirebase(parseBitmapToUri(requireContext(), profileImage));
        }
    }

    private String calculateAge(String date) {
        String[] dateArray = date.split("/");
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentMonth < Integer.parseInt(dateArray[1]) || currentMonth == Integer.parseInt(dateArray[1]) &&
                currentDay < Integer.parseInt(dateArray[0])) {
            return (currentYear - Integer.parseInt(dateArray[2]) - 1) + " años";
        } else {
            return (currentYear - Integer.parseInt(dateArray[2])) + " años";
        }
    }

    private void permissions() {
        cameraPermission();
        readExternalStoragePermission();
        writeExternalStoragePermission();
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
    }

    private void readExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    private void writeExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void listeners() {
        imgVwProfileImgOnClickListener();
        imgVwProfileSettingsOnClickListener();
    }

    private void initConfig() {
         imgVwProfileSettings.bringToFront();
    }

    private void imgVwProfileImgOnClickListener() {
        imgVwProfileImg.setOnClickListener(view -> {
            AlertDialog.Builder changeProfileImg = new AlertDialog.Builder(requireContext());
            changeProfileImg.setTitle("Cambiar Foto de Perfil");
            changeProfileImg.setMessage("Fuente de la Imagen:");
            changeProfileImg.setPositiveButtonIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.camera_icon));
            changeProfileImg.setPositiveButton("Cámara", (dialog, which) -> {
                String fileName = "photo";
                File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(requireActivity(), "com.example.futplay.fileprovider", imageFile);

                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(camera, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            changeProfileImg.setNegativeButton("Galería", (dialog, which) -> {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 2);
            });
            changeProfileImg.setNegativeButtonIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.gallery_icon));
            changeProfileImg.create().show();
        });
    }

    private void imgVwProfileSettingsOnClickListener() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa entré a: imgVwProfileSettingsOnClickListener ");
        imgVwProfileSettings.setOnClickListener(v -> {
            displayPopupProfileSettings();
        });
    }

    private void displayPopupProfileSettings() {
        System.out.println("bbbbbbbbbbbbbbbbbbb entré a: displayPopupProfileSettings ");
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            initPopupProfileSettings();
            popupProfileSettingsListeners();
            setupPopupLayoutParams(popupProfileSettings);
            hidePopupProfileSettingsViews();
            retrievePopupProfileSettingsData();

            popupProfileSettings.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupProfileSettings.show();
        });
    }

    private void initPopupProfileSettings() {
        popupProfileSettings.setContentView(R.layout.popup_profile_settings);

        edTxtPopupProfileSettingsFullName = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsFullName);
        edTxtPopupProfileSettingsNickname = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsNickname);
        edTxtPopupProfileSettingsRegion = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsRegion);
        edTxtPopupProfileSettingsAge = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsAge);
        edTxtPopupProfileSettingsEmail = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsEmail);
        edTxtPopupProfileSettingsNewPassword = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsNewPassword);
        edTxtPopupProfileSettingsConfirmNewPassword = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsConfirmNewPassword);
        edTxtPopupProfileSettingsPhoneNumber = popupProfileSettings.findViewById(R.id.edTxtPopupProfileSettingsPhoneNumber);

        spinnerPopupProfileSettingsPosition = popupProfileSettings.findViewById(R.id.spinnerPopupProfileSettingsPosition);

        imgVwPopupProfileSettingsSave = popupProfileSettings.findViewById(R.id.imgVwPopupProfileSettingsSave);
        imgVwPopupProfileSettingsClose = popupProfileSettings.findViewById(R.id.imgVwPopupProfileSettingsClose);

        countryCodePickerPopupProfileSettings = popupProfileSettings.findViewById(R.id.countryCodePickerPopupProfileSettings);

        progressBarPopupProfileSettings = popupProfileSettings.findViewById(R.id.progressBarPopupProfileSettings);

        fillUpSpinnerPopupProfileSettingsPosition();
    }

    private void fillUpSpinnerPopupProfileSettingsPosition() {
        DocumentReference documentReference = firebaseFirestore.collection("spinners_data").document("positions");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String json = (String) documentSnapshot.get("spanish");
                try {
                    if (json != null) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("spanish");
                        int jsonArrayLength = jsonArray.length();
                        ArrayList<String> positionsValues = new ArrayList<>();
                        positionsValues.add("Posición:");
                        for (int i = 0; i < jsonArrayLength; i++) {
                            positionsValues.add(jsonArray.getString(i));
                        }
                        ArrayAdapter<String> positionsArrayAdapter = new ArrayAdapter<>(popupProfileSettings.getContext(), R.layout.custom_spinner_dialog_item, R.id.txtVwCustomSpinnerDialogItemText, positionsValues);
                        spinnerPopupProfileSettingsPosition.setAdapter(positionsArrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void popupProfileSettingsListeners() {

    }

    private void retrievePopupProfileSettingsData() {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String fullName = (String) Objects.requireNonNull(documentSnapshot.get("fullName"));
            String nickname = (String) Objects.requireNonNull(documentSnapshot.get("nickname"));
            String region = ((String) Objects.requireNonNull(documentSnapshot.get("region"))).split("/")[0];
            String position = (String) Objects.requireNonNull(documentSnapshot.get("position"));
            String age = calculateAge((String) Objects.requireNonNull(documentSnapshot.get("birthDate")));
            String email = (String) Objects.requireNonNull(documentSnapshot.get("email"));
            String phoneNumber = (String) Objects.requireNonNull(documentSnapshot.get("phoneNumber"));
            edTxtPopupProfileSettingsFullName.setText(fullName);
            edTxtPopupProfileSettingsNickname.setText(nickname);
            edTxtPopupProfileSettingsRegion.setText(region);
            spinnerPopupProfileSettingsPosition.setSelection(getIndexOfStringInSpinner(position, spinnerPopupProfileSettingsPosition));
            edTxtPopupProfileSettingsAge.setText(age);
            edTxtPopupProfileSettingsEmail.setText(email);
            edTxtPopupProfileSettingsPhoneNumber.setText(phoneNumber);

            progressBarPopupProfileSettings.setVisibility(View.GONE);
            showPopupProfileSettingsViews();
        });
    }

    private int getIndexOfStringInSpinner(String text, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(text)) {
                return i;
            }
        }
        return 0;
    }

    private void hidePopupProfileSettingsViews() {
        edTxtPopupProfileSettingsFullName.setVisibility(View.GONE);
        edTxtPopupProfileSettingsNickname.setVisibility(View.GONE);
        edTxtPopupProfileSettingsRegion.setVisibility(View.GONE);
        edTxtPopupProfileSettingsAge.setVisibility(View.GONE);
        edTxtPopupProfileSettingsEmail.setVisibility(View.GONE);
        edTxtPopupProfileSettingsNewPassword.setVisibility(View.GONE);
        edTxtPopupProfileSettingsConfirmNewPassword.setVisibility(View.GONE);
        edTxtPopupProfileSettingsPhoneNumber.setVisibility(View.GONE);

        spinnerPopupProfileSettingsPosition.setVisibility(View.GONE);

        imgVwPopupProfileSettingsSave.setVisibility(View.GONE);

        countryCodePickerPopupProfileSettings.setVisibility(View.GONE);
    }

    private void showPopupProfileSettingsViews() {
        edTxtPopupProfileSettingsFullName.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsNickname.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsRegion.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsAge.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsEmail.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsNewPassword.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsConfirmNewPassword.setVisibility(View.VISIBLE);
        edTxtPopupProfileSettingsPhoneNumber.setVisibility(View.VISIBLE);

        spinnerPopupProfileSettingsPosition.setVisibility(View.VISIBLE);

        imgVwPopupProfileSettingsSave.setVisibility(View.VISIBLE);

        countryCodePickerPopupProfileSettings.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            profileImage = BitmapFactory.decodeFile(currentPhotoPath);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            profileImage = parseUriToBitmap(data.getData());
        }

        if (profileImage != null) {
            lightenImage();
            Intent cropImage = new Intent(requireActivity(), CropImageActivity.class);
            cropImage.putExtra("type", "profile");
            startActivity(cropImage);
            requireActivity().finish();
        }
    }

    private void lightenImage() {
        int currentWidth = profileImage.getWidth();
        int currentHeight = profileImage.getHeight();
        int newWidth = 500;
        int newHeight = (int) (currentHeight * ((float) 500 / currentWidth));
        profileImage = Bitmap.createScaledBitmap(profileImage, newWidth, newHeight, true);
    }

    private Bitmap parseUriToBitmap(Uri imageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Uri parseBitmapToUri(Context inContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, "profile", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebase(Uri image) {
        StorageReference fileRef = storageReference.child("users/" + userID + "/profile.jpg");
        fileRef.putFile(image)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(imgVwProfileImg)))
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al actualizar\nla foto de perfil", Toast.LENGTH_SHORT).show());
    }
}