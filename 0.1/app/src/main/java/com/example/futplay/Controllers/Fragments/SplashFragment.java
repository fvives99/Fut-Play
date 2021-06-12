package com.example.futplay.Controllers.Fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.futplay.Controllers.Activities.LoginActivity;
import com.example.futplay.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private ImageView imgVwSplashScrn1;
    private ImageView imgVwSplashScrn2;
    private ImageView imgVwSplashScrn3;
    private ImageView imgVwSplashScrn4;

    private MediaPlayer mdPlyrIntro;

    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SplashFragment newInstance(String param1, String param2) {
        SplashFragment fragment = new SplashFragment();
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
        view = inflater.inflate(R.layout.fragment_splash, container, false);

        // Additional Settings
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewsMatching(view);
        initAnims();
        moveToLogin();

        return view;
    }

    private void viewsMatching(View view) {
        imgVwSplashScrn1 = view.findViewById(R.id.imgVwSplashScrn1);
        imgVwSplashScrn2 = view.findViewById(R.id.imgVwSplashScrn2);
        imgVwSplashScrn3 = view.findViewById(R.id.imgVwSplashScrn3);
        imgVwSplashScrn4 = view.findViewById(R.id.imgVwSplashScrn4);
        mdPlyrIntro = MediaPlayer.create(requireContext(), R.raw.fut_play_intro_music);
    }

    private void initAnims() {
        View[] views = new View[]{imgVwSplashScrn1, imgVwSplashScrn2, imgVwSplashScrn3, imgVwSplashScrn4};
        long delayBetweenAnimations = 700L;
        for (int i = 0; i < views.length; i++) {
            int finalI = i;
            final View view = views[i];
            view.setVisibility(View.GONE);
            int delay = (int) (i * delayBetweenAnimations + 1000);
            view.postDelayed(() -> {
                Animation animation;
                view.setVisibility(View.VISIBLE);
                if (finalI == views.length - 1) {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation);
                } else {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.top_right_animation);
                }
                view.startAnimation(animation);
            }, delay);
        }
        new Handler().postDelayed(() -> mdPlyrIntro.start(), 1000);
    }

    private void moveToLogin() {
        new Handler().postDelayed(() -> {
            Intent login = new Intent(requireActivity(), LoginActivity.class);
            startActivity(login);
            requireActivity().finish();
        }, 5000);
    }
}