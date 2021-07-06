package com.example.futplay.Controllers.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
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

import com.example.futplay.Controllers.Activities.CropImageActivity;
import com.example.futplay.Controllers.Adapters.PlayersAdapter;
import com.example.futplay.Controllers.Items.PlayersItem;
import com.example.futplay.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;


    private ImageView imgVwTeamProfileImg;
    private ImageView imgVwTeamSettings;

    private ImageView imgVwStreak1;
    private ImageView imgVwStreak2;
    private ImageView imgVwStreak3;
    private ImageView imgVwStreak4;
    private ImageView imgVwStreak5;

    private TextView txtVwTeamName;
    private TextView txtVwTeamRegion;
    private TextView txtVwTeamCode;
    private TextView txtVwTeamHistory;
    private TextView txtVwTeamStreak;
    private TextView txtVwTeamPlayers;
    private TextView txtVwTeamWDL;

    private RecyclerView recycVwTeamPlayers;
    private RecyclerView.Adapter recycVwTeamPlayersAdapter;
    private RecyclerView.LayoutManager recycVwTeamPlayersLytMngr;
    private ArrayList<PlayersItem> playersList = new ArrayList<>();

    private ProgressBar progressBarTeam;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;

    private String userID;
    private String currentPhotoPath;

    public static Bitmap teamProfileImage = null;

    private Dialog popupTeamSettings;

    private EditText edTxtPopupTeamSettingsFullName;
    private EditText edTxtPopupTeamSettingsAbbreviation;
    private EditText edTxtPopupTeamSettingsRegion;

    private String teamID;

    private ImageView imgVwPopupTeamSettingsSave;
    private ImageView imgVwPopupTeamSettingsClose;

    private ProgressBar progressBarPopupTeamSettings;

    public TeamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamsFragment newInstance(String param1, String param2) {
        TeamsFragment fragment = new TeamsFragment();
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
        view = inflater.inflate(R.layout.fragment_teams, container, false);

        // Additional Settings
        fillPlayersList();
        viewsMatching(view);
        hideViews();
        retrieveData();
        permissions();
        listeners();
        recycVwTeamPlayersConfig();
        initConfig();

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            PlayersItem deletedPlayer = playersList.get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    View profileMenuOption = getActivity().findViewById(R.id.imgVwMenuProfile);
                    profileMenuOption.performClick();
                    break;
                case ItemTouchHelper.RIGHT:
                    AlertDialog.Builder deletePlyr = new AlertDialog.Builder(getContext());
                    deletePlyr.setTitle("Eliminar Jugador");
                    deletePlyr.setMessage("¿Está seguro de que desea eliminar a " + playersList.get(position).getName() + "?");
                    deletePlyr.setPositiveButton("Sí", (dialog, which) -> {
                        playersList.remove(position);
                        recycVwTeamPlayersAdapter.notifyItemRemoved(position);
                        Snackbar.make(recycVwTeamPlayers, "Jugador Eliminado", Snackbar.LENGTH_LONG).setAction("Deshacer", view -> {
                            view.setEnabled(false);
                            playersList.add(position, deletedPlayer);
                            recycVwTeamPlayersAdapter.notifyItemInserted(position);
                        }).show();
                    });
                    deletePlyr.setNegativeButton("No", (dialog, which) -> {
                        recycVwTeamPlayersAdapter.notifyItemChanged(position);
                        dialog.cancel();
                    });
                    deletePlyr.create().show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Ver Perfil")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), R.color.white))
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.green))
                    .addSwipeLeftActionIcon(R.drawable.view_profile_icon)
                    .addSwipeRightLabel("Eliminar")
                    .setSwipeRightLabelColor(ContextCompat.getColor(getContext(), R.color.white))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeRightActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void fillPlayersList() {
        playersList.add(new PlayersItem(R.drawable.profile_image_icon, "Jean Pierre Araya Meléndez"));
        playersList.add(new PlayersItem(R.drawable.profile_image_icon, "Jean Paul Araya Meléndez"));
        playersList.add(new PlayersItem(R.drawable.profile_image_icon, "Jean Xavi Araya Meléndez"));
    }

    private void viewsMatching(View view) {
        //popupTeamInfo = new Dialog(this.getContext());
        //regionPicker = new Dialog(this.getContext());
        popupTeamSettings = new Dialog(this.getContext());
        //  popupDone = new Dialog(this.getContext());

        imgVwTeamProfileImg = view.findViewById(R.id.imgVwTeamProfileImg);
        imgVwTeamSettings = view.findViewById(R.id.imgVwTeamSettings);
        imgVwStreak1 = view.findViewById(R.id.imgVwStreak1);
        imgVwStreak2 = view.findViewById(R.id.imgVwStreak2);
        imgVwStreak3 = view.findViewById(R.id.imgVwStreak3);
        imgVwStreak4 = view.findViewById(R.id.imgVwStreak4);
        imgVwStreak5 = view.findViewById(R.id.imgVwStreak5);

        txtVwTeamName = view.findViewById(R.id.txtVwTeamName);
        txtVwTeamRegion = view.findViewById(R.id.txtVwTeamRegion);
        txtVwTeamCode = view.findViewById(R.id.txtVwTeamCode);
        txtVwTeamHistory = view.findViewById(R.id.txtVwTeamHistory);
        txtVwTeamStreak = view.findViewById(R.id.txtVwTeamStreak);
        txtVwTeamPlayers = view.findViewById(R.id.txtVwTeamPlayers);
        txtVwTeamWDL = view.findViewById(R.id.txtVwTeamWDL);

        recycVwTeamPlayers = view.findViewById(R.id.recycVwTeamPlayers);
        recycVwTeamPlayersLytMngr = new LinearLayoutManager(view.getContext());
        recycVwTeamPlayersAdapter = new PlayersAdapter(playersList);

        progressBarTeam = view.findViewById(R.id.progressBarTeam);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    private void recycVwTeamPlayersConfig() {
        recycVwTeamPlayers.setHasFixedSize(true);
        recycVwTeamPlayers.setLayoutManager(recycVwTeamPlayersLytMngr);
        recycVwTeamPlayers.setAdapter(recycVwTeamPlayersAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycVwTeamPlayers);
    }

    private void hideViews() {
        imgVwTeamProfileImg.setVisibility(View.GONE);
        imgVwTeamSettings.setVisibility(View.GONE);
        imgVwStreak1.setVisibility(View.GONE);
        imgVwStreak2.setVisibility(View.GONE);
        imgVwStreak3.setVisibility(View.GONE);
        imgVwStreak4.setVisibility(View.GONE);
        imgVwStreak5.setVisibility(View.GONE);

        txtVwTeamName.setVisibility(View.GONE);
        txtVwTeamRegion.setVisibility(View.GONE);
        txtVwTeamCode.setVisibility(View.GONE);
        txtVwTeamHistory.setVisibility(View.GONE);
        txtVwTeamStreak.setVisibility(View.GONE);
        txtVwTeamPlayers.setVisibility(View.GONE);
        txtVwTeamWDL.setVisibility(View.GONE);

        recycVwTeamPlayers.setVisibility(View.GONE);
    }

    private void showViews() {
        imgVwTeamProfileImg.setVisibility(View.VISIBLE);
        imgVwTeamSettings.setVisibility(View.VISIBLE);
        imgVwStreak1.setVisibility(View.VISIBLE);
        imgVwStreak2.setVisibility(View.VISIBLE);
        imgVwStreak3.setVisibility(View.VISIBLE);
        imgVwStreak4.setVisibility(View.VISIBLE);
        imgVwStreak5.setVisibility(View.VISIBLE);

        txtVwTeamName.setVisibility(View.VISIBLE);
        txtVwTeamRegion.setVisibility(View.VISIBLE);
        txtVwTeamCode.setVisibility(View.VISIBLE);
        txtVwTeamHistory.setVisibility(View.VISIBLE);
        txtVwTeamStreak.setVisibility(View.VISIBLE);
        txtVwTeamPlayers.setVisibility(View.VISIBLE);
        txtVwTeamWDL.setVisibility(View.VISIBLE);

        recycVwTeamPlayers.setVisibility(View.VISIBLE);
    }

    private void retrieveData() {
        retrieveTeamProfileImage();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            progressBarTeam.setVisibility(View.GONE);
            showViews();
            if (documentSnapshot.exists()) {
                //txtVwProfileAge.setText(calculateAge((String) Objects.requireNonNull(documentSnapshot.get("birthDate"))));
            }
        });
    }

    private void retrieveTeamProfileImage() {
        checkCroppedImage();
        StorageReference profileRef = storageReference.child("users/" + userID + "/team_profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(imgVwTeamProfileImg));
    }

    private void checkCroppedImage() {
        if (teamProfileImage != null) {
            uploadImageToFirebase(parseBitmapToUri(requireContext(), teamProfileImage));
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
        imgVwTeamSettingsOnClickListener();
    }

    private void imgVwProfileImgOnClickListener() {
        imgVwTeamProfileImg.setOnClickListener(view -> {
            AlertDialog.Builder changeProfileImg = new AlertDialog.Builder(requireContext());
            changeProfileImg.setTitle("Cambiar Foto de Perfil del Equipo");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            teamProfileImage = BitmapFactory.decodeFile(currentPhotoPath);
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            teamProfileImage = parseUriToBitmap(data.getData());
        }

        if (teamProfileImage != null) {
            lightenImage();
            Intent cropImage = new Intent(requireActivity(), CropImageActivity.class);
            cropImage.putExtra("type", "teams");
            startActivity(cropImage);
            requireActivity().finish();
        }
    }

    private void lightenImage() {
        int currentWidth = teamProfileImage.getWidth();
        int currentHeight = teamProfileImage.getHeight();
        int newWidth = 500;
        int newHeight = (int) (currentHeight * ((float) 500 / currentWidth));
        teamProfileImage = Bitmap.createScaledBitmap(teamProfileImage, newWidth, newHeight, true);
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
        StorageReference fileRef = storageReference.child("users/" + userID + "/team_profile.jpg");
        fileRef.putFile(image)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(imgVwTeamProfileImg)))
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error al actualizar\nla foto de perfil", Toast.LENGTH_SHORT).show());
    }

    private void initConfig() {
        imgVwTeamSettings.bringToFront();
    }
/*
*
* Acá se muestra la configuación del equipo y los datos que se pueden modificar
* Nombre, Abreviación, Región
*
 */

    private void imgVwTeamSettingsOnClickListener() {
        imgVwTeamSettings.setOnClickListener(v -> {
            displayPopupTeamSettings();
        });
    }

    private void setupPopupLayoutParams(Dialog popup) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        popup.getWindow().setAttributes(lp);
    }

    private void displayPopupTeamSettings() {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        System.out.println(documentReference.toString());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            initPopupTeamSettings();
            popupTeamSettingsListeners();
            setupPopupLayoutParams(popupTeamSettings);
            hidePopupTeamSettingsViews();
            retrievePopupTeamSettingsData();
            popupTeamSettings.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupTeamSettings.show();
            imgVwPopupTeamSettingsSave.bringToFront();
        });
    }

    private void completeTeamInfo() {
        //getTeamID();
        DocumentReference documentReference = firebaseFirestore.collection("users").document("O6GNayOlR6zMBRiinjbF");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String teamName = (String) Objects.requireNonNull(documentSnapshot.get("teamName"));
            String teamAbb = (String) Objects.requireNonNull(documentSnapshot.get("teamAbbreviations"));
            String teamRegion = ((String) Objects.requireNonNull(documentSnapshot.get("region"))).split("/")[0];
            txtVwTeamName.setText(teamName);
            txtVwTeamCode.setText(teamAbb);
            txtVwTeamRegion.setText(teamRegion);
        });
    }

    private void initPopupTeamSettings() {
        popupTeamSettings.setContentView(R.layout.popup_teams_settings);

        edTxtPopupTeamSettingsFullName = popupTeamSettings.findViewById(R.id.edTxtPopupTeamSettingsFullName);
        edTxtPopupTeamSettingsAbbreviation = popupTeamSettings.findViewById(R.id.edTxtPopupTeamSettingsNickname);
        edTxtPopupTeamSettingsRegion = popupTeamSettings.findViewById(R.id.edTxtPopupTeamSettingsRegion);

        imgVwPopupTeamSettingsSave = popupTeamSettings.findViewById(R.id.imgVwPopupTeamSettingsSave);
        imgVwPopupTeamSettingsClose = popupTeamSettings.findViewById(R.id.imgVwPopupTeamSettingsClose);

        progressBarPopupTeamSettings = popupTeamSettings.findViewById(R.id.progressBarPopupTeamSettings);
    }

    private void popupTeamSettingsListeners() {
        imgVwPopupTeamSettingsSaveOnClickListener();
        setImgVwPopupTeamSettingsCloseOnClickListener();
        imgVwPopupTeamSettingsCloseOnClickListener();
    }

    /*private void getTeamID() {
        System.out.println("hola1: "+teamID);
        System.out.println("id user: "+userID);
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID.trim());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String fullName = (String) Objects.requireNonNull(documentSnapshot.get("teamName"));
            String Abbreviation = (String) Objects.requireNonNull(documentSnapshot.get("teamAbbreviations"));
            String region = ((String) Objects.requireNonNull(documentSnapshot.get("region"))).split("/")[0];
            edTxtPopupTeamSettingsFullName.setText(fullName);
            edTxtPopupTeamSettingsAbbreviation.setText(Abbreviation);
            edTxtPopupTeamSettingsRegion.setText(region);

            progressBarPopupTeamSettings.setVisibility(View.GONE);
            showPopupTeamSettingsViews();
        });
    }*/

    private void retrievePopupTeamSettingsData() {
        //getTeamID();
        //System.out.println("hola: "+teamID);
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot ->{
            String fullName = (String) Objects.requireNonNull(documentSnapshot.get("fullName"));
            String Abbreviation = (String) Objects.requireNonNull(documentSnapshot.get("nickname"));
            String region = ((String) Objects.requireNonNull(documentSnapshot.get("region"))).split("/")[0];
            edTxtPopupTeamSettingsFullName.setText(fullName);
            edTxtPopupTeamSettingsAbbreviation.setText(Abbreviation);
            edTxtPopupTeamSettingsRegion.setText(region);

            progressBarPopupTeamSettings.setVisibility(View.GONE);
            showPopupTeamSettingsViews();
        });
    }

    private void imgVwPopupTeamSettingsSaveOnClickListener() {
        imgVwPopupTeamSettingsSave.setOnClickListener(v -> {
            //if (!invalidFields()) {
            String teamName = edTxtPopupTeamSettingsFullName.getText().toString();
            String abb = edTxtPopupTeamSettingsAbbreviation.getText().toString();
            String region = edTxtPopupTeamSettingsRegion.getText().toString();
            DocumentReference documentReference = firebaseFirestore.collection("teams").document("O6GNayOlR6zMBRiinjbF");
            Map<String, Object> team = new HashMap<>();
            team.put("teamName", teamName);
            team.put("teamAbbreviations", abb);
            team.put("region", region);
            documentReference.update(team).addOnSuccessListener(command -> {
                //displayPopupDone("¡Perfil Completado!");
                completeTeamInfo();
            });
            popupTeamSettings.dismiss();
            //}
        });
    }

    //Salir del popup de Team Settings
    private void setImgVwPopupTeamSettingsCloseOnClickListener() {
        if(imgVwPopupTeamSettingsClose != null){
            imgVwPopupTeamSettingsClose.setOnClickListener(v -> {
                popupTeamSettings.dismiss();
            });
        }
    }

    /*
    agarrar el idTeam del useracatual, bhuscar el team, rellenar info
    UNa vez que tengo la info, ver si la puedo modificar
     */
    private void imgVwPopupTeamSettingsCloseOnClickListener() {
        imgVwPopupTeamSettingsSave.setOnClickListener(v -> {
            popupTeamSettings.dismiss();
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

    private void hidePopupTeamSettingsViews() {
        edTxtPopupTeamSettingsFullName.setVisibility(View.GONE);
        edTxtPopupTeamSettingsAbbreviation.setVisibility(View.GONE);
        edTxtPopupTeamSettingsRegion.setVisibility(View.GONE);

        imgVwPopupTeamSettingsSave.setVisibility(View.GONE);
    }

    private void showPopupTeamSettingsViews() {
        edTxtPopupTeamSettingsFullName.setVisibility(View.VISIBLE);
        edTxtPopupTeamSettingsAbbreviation.setVisibility(View.VISIBLE);
        edTxtPopupTeamSettingsRegion.setVisibility(View.VISIBLE);

        imgVwPopupTeamSettingsSave.setVisibility(View.VISIBLE);
    }

}