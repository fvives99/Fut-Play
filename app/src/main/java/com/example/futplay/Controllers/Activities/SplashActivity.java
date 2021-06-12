package com.example.futplay.Controllers.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.futplay.Controllers.Fragments.SplashFragment;
import com.example.futplay.R;

public class SplashActivity extends AppCompatActivity {

    private SplashFragment splashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewsMatching();
    }

    private void viewsMatching() {
        splashFragment = new SplashFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragContainerSplash, splashFragment).addToBackStack("login").commit();
    }
}