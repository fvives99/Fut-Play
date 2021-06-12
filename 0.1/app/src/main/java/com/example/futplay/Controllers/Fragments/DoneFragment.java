package com.example.futplay.Controllers.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.futplay.Controllers.Activities.MenuActivity;
import com.example.futplay.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private ImageView imgVwDoneCheck;

    private TextView txtVwDoneMessage;
    private TextView txtVwDoneOk;

    private String message;
    private String activity_fragment;

    AnimatedVectorDrawableCompat avdc;
    AnimatedVectorDrawable avd;

    public DoneFragment() {
        // Required empty public constructor
    }

    public DoneFragment(String message) {
        this.message = message;
    }

    public DoneFragment(String message, String activity_fragment) {
        this.message = message;
        this.activity_fragment = activity_fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoneFragment newInstance(String param1, String param2) {
        DoneFragment fragment = new DoneFragment();
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

        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_done, container, false);

        // Additional Settings
        viewsMatching(view);
        messageSetUp();
        txtVwOkOnClickList();
        initAnims();

        return view;
    }

    private void viewsMatching(View view) {
        imgVwDoneCheck = view.findViewById(R.id.imgVwDoneCheck);

        txtVwDoneMessage = view.findViewById(R.id.txtVwDoneMessage);
        txtVwDoneOk = view.findViewById(R.id.txtVwDoneOk);
    }

    private void messageSetUp() {
        txtVwDoneMessage.setText(message);
    }

    @SuppressLint("RestrictedApi")
    private void txtVwOkOnClickList() {
        txtVwDoneOk.setOnClickListener(view -> {
            if (activity_fragment != null) {
                if ("menu".equals(activity_fragment)) {
                    Intent menuActivity = new Intent(getActivity(), MenuActivity.class);
                    startActivity(menuActivity);
                    requireActivity().finish();
                }
            } else {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }

    private void initAnims() {
        int delay = 500;
        imgVwDoneCheck.setVisibility(View.GONE);
        view.postDelayed(() -> {
            imgVwDoneCheck.setVisibility(View.VISIBLE);
            Drawable drawable = imgVwDoneCheck.getDrawable();
            if (drawable instanceof AnimatedVectorDrawableCompat) {
                avdc = (AnimatedVectorDrawableCompat) drawable;
                avdc.start();
            } else if (drawable instanceof AnimatedVectorDrawable) {
                avd = (AnimatedVectorDrawable) drawable;
                avd.start();
            }
        }, delay);
    }
}