package com.example.futplay.Controllers.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.futplay.Controllers.Activities.CropImageActivity;
import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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

    private ProfileSettingsFragment profileSettingsFragment;

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
        //initialInfoPopUp(view);
        retrieveData();
        permissions();
        listeners();
        initConfig();

        return view;
    }

    private void viewsMatching(View view) {
        profileSettingsFragment = new ProfileSettingsFragment();

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

    private void initialInfoPopUp(View view) {
        EditText edTxtInputNickname = new EditText(view.getContext());
        EditText edTxtInputRegion = new EditText(view.getContext());
        EditText edTxtInputPosition = new EditText(view.getContext());
        android.app.AlertDialog.Builder infoPopUp = new android.app.AlertDialog.Builder(view.getContext());
        infoPopUp.setTitle("Datos del Perfil");
        infoPopUp.setMessage("Ingrese la siguiente información para completar su perfil:");
        infoPopUp.setView(view);
        infoPopUp.setPositiveButton("Aceptar", (dialogInterface, i) -> {
            System.out.println("GUARDAR CAMBIOS");
            //firebaseAuth.sendPasswordResetEmail(email)
            //        .addOnSuccessListener(aVoid -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerLogin, new DoneFragment("Si el correo electrónico proporcionado se encuentra registrado, encontrará un mensaje con el enlace de reinicio de contraseña")).addToBackStack("done").commit())
            //        .addOnFailureListener(e -> Toast.makeText(getContext(), "¡Ha habido un error!.\nIntente más tarde", Toast.LENGTH_SHORT).show());
        });
        infoPopUp.create().show();
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

    private void retrieveData() {
        retrieveProfileImage();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            progressBarProfile.setVisibility(View.GONE);
            showViews();
            if (documentSnapshot.exists()) {
                txtVwProfileAge.setText(calculateAge((String) Objects.requireNonNull(documentSnapshot.get("birthDate"))));
            }
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
        imgVwProfileSettings.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerMenu, profileSettingsFragment).addToBackStack("profileSettings").commit());
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