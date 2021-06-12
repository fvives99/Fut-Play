package com.example.futplay.Controllers.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.futplay.Controllers.Fragments.LoginFragment;
import com.example.futplay.Controllers.Fragments.SignUpFragment;
import com.example.futplay.R;

public class LoginActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewsMatching();
    }

    @Override
    public void onBackPressed() {
        Fragment currFrag = getSupportFragmentManager().findFragmentById(R.id.fragContainerLogin);
        if (currFrag instanceof LoginFragment) {
            onBackPressedLogin();
        } else if (currFrag instanceof SignUpFragment) {
            onBackPressedSignUp();
        } else {
            super.onBackPressed();
        }
    }

    public SignUpFragment getSignUpFragment() {
        return signUpFragment;
    }

    private void viewsMatching() {
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragContainerLogin, loginFragment).addToBackStack("login").commit();
    }

    private void onBackPressedLogin() {
        AlertDialog.Builder closeApp = new AlertDialog.Builder(LoginActivity.this);
        closeApp.setTitle("Cerrar Aplicación");
        closeApp.setMessage("¿Está seguro de que desea cerrar la aplicación?");
        closeApp.setPositiveButton("Sí", (dialog, which) -> finish());
        closeApp.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        closeApp.create().show();
    }

    private void onBackPressedSignUp() {
        signUpFragment.defaultSetUp();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerLogin, loginFragment).addToBackStack("login").commit();
    }
}