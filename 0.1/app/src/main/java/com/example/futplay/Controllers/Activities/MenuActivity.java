package com.example.futplay.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.futplay.Controllers.Fragments.FieldsFragment;
import com.example.futplay.Controllers.Fragments.MatchesFragment;
import com.example.futplay.Controllers.Fragments.ProfileFragment;
import com.example.futplay.Controllers.Fragments.TeamsFragment;
import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    private ProfileFragment profileFragment;
    private TeamsFragment teamsFragment;
    private FieldsFragment fieldsFragment;
    private MatchesFragment matchesFragment;

    private ImageView imgVwMenuBack;
    private ImageView imgVwMenuSignOut;
    private ImageView imgVwMenuProfile;
    private ImageView imgVwMenuTeams;
    private ImageView imgVwMenuFields;
    private ImageView imgVwMenuMatches;

    private String type = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_purple));

        viewsMatching();
        initConfig();
        selectToolbarOption(type);
        listeners();
    }

    @Override
    public void onBackPressed() {
        goBackOnMenu();
    }

    private void goBackOnMenu() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            clearToolbarSelection();
            switch (Objects.requireNonNull(this.getSupportFragmentManager().getBackStackEntryAt(this.getSupportFragmentManager().getBackStackEntryCount() - 2).getName())) {
                case "profile":
                case "profileSettings":
                    imgVwMenuProfile.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_icon_colored));
                    break;
                case "teams":
                    imgVwMenuTeams.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.teams_icon_colored));
                    break;
                case "fields":
                    imgVwMenuFields.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fields_icon_colored));
                    break;
                default:
                    imgVwMenuMatches.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.matches_icon_colored));
                    break;
            }
            this.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void viewsMatching() {
        profileFragment = new ProfileFragment();
        teamsFragment = new TeamsFragment();
        fieldsFragment = new FieldsFragment();
        matchesFragment = new MatchesFragment();

        imgVwMenuBack = findViewById(R.id.imgVwMenuBack);
        imgVwMenuSignOut = findViewById(R.id.imgVwMenuSignOut);
        imgVwMenuProfile = findViewById(R.id.imgVwMenuProfile);
        imgVwMenuTeams = findViewById(R.id.imgVwMenuTeams);
        imgVwMenuFields = findViewById(R.id.imgVwMenuFields);
        imgVwMenuMatches = findViewById(R.id.imgVwMenuMatches);

        Intent cropImage = getIntent();
        if (cropImage.hasExtra("type")) {
            type = cropImage.getStringExtra("type");
        }
    }

    private void initConfig() {
        switch (type){
            case "profile":
                getSupportFragmentManager().beginTransaction().add(R.id.fragContainerMenu, profileFragment).addToBackStack(type).commit();
                break;
            case "teams":
                getSupportFragmentManager().beginTransaction().add(R.id.fragContainerMenu, teamsFragment).addToBackStack(type).commit();
                break;
            case "fields":
                getSupportFragmentManager().beginTransaction().add(R.id.fragContainerMenu, fieldsFragment).addToBackStack(type).commit();
                break;
            case "matches":
                getSupportFragmentManager().beginTransaction().add(R.id.fragContainerMenu, matchesFragment).addToBackStack(type).commit();
                break;
        }
    }

    private void listeners() {
        imgVwMenuBackOnClickList();
        imgVwMenuSignOutOnClickList();
        imgVwMenuProfileOnClickList();
        imgVwMenuTeamsOnClickList();
        imgVwMenuFieldsOnClickList();
        imgVwMenuMatchesOnClickList();
    }

    private void imgVwMenuBackOnClickList() {
        imgVwMenuBack.setOnClickListener(view -> goBackOnMenu());
    }

    private void imgVwMenuSignOutOnClickList() {
        imgVwMenuSignOut.setOnClickListener(view -> {
            AlertDialog.Builder logout = new AlertDialog.Builder(this);
            logout.setTitle("Cerrar Sesión");
            logout.setMessage("¿Está seguro de que desea cerrar sesión?");
            logout.setPositiveButton("Sí", (dialog, which) ->
            {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                finish();
            });
            logout.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            logout.create().show();
        });
    }

    private void imgVwMenuProfileOnClickList() {
        imgVwMenuProfile.setOnClickListener(view -> selectToolbarOption("profile"));
    }

    private void imgVwMenuTeamsOnClickList() {
        imgVwMenuTeams.setOnClickListener(view -> selectToolbarOption("teams"));
    }

    private void imgVwMenuFieldsOnClickList() {
        imgVwMenuFields.setOnClickListener(view -> selectToolbarOption("fields"));
    }

    private void imgVwMenuMatchesOnClickList() {
        imgVwMenuMatches.setOnClickListener(view -> selectToolbarOption("matches"));
    }

    private void selectToolbarOption(String tag) {
        Fragment selectedFragment = new Fragment();
        clearToolbarSelection();
        switch (tag) {
            case "profile":
                imgVwMenuProfile.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_icon_colored));
                selectedFragment = profileFragment;
                break;
            case "teams":
                imgVwMenuTeams.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.teams_icon_colored));
                selectedFragment = teamsFragment;
                break;
            case "fields":
                imgVwMenuFields.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fields_icon_colored));
                selectedFragment = fieldsFragment;
                break;
            case "matches":
                imgVwMenuMatches.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.matches_icon_colored));
                selectedFragment = matchesFragment;
                break;
        }
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (Objects.equals(this.getSupportFragmentManager().getBackStackEntryAt(this.getSupportFragmentManager().getBackStackEntryCount() - 1).getName(), tag)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerMenu, selectedFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerMenu, selectedFragment).addToBackStack(tag).commit();
            }
        }
    }

    private void clearToolbarSelection() {
        imgVwMenuProfile.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.profile_icon));
        imgVwMenuTeams.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.teams_icon));
        imgVwMenuFields.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fields_icon));
        imgVwMenuMatches.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.matches_icon));
    }
}