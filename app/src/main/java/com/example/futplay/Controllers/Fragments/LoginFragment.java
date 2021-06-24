package com.example.futplay.Controllers.Fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.futplay.Controllers.Activities.LoginActivity;
import com.example.futplay.Controllers.Activities.MenuActivity;
import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private EditText edTxtLoginEmail;
    private EditText edTxtLoginPassword;

    private ImageView imgVwLogin;
    private ImageView imgVwLoginScrn1;
    private ImageView imgVwLoginScrn2;

    private TextView txtVwLoginForgotPass;
    private TextView txtVwLoginRegister;

    private FirebaseAuth firebaseAuth;

    private LoginActivity loginActivity;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        // Additional Settings
        viewsMatching(view);
        setUpListeners();
        initAnims();

        return view;
    }

    private void viewsMatching(View view) {
        edTxtLoginEmail = view.findViewById(R.id.edTxtLoginEmail);
        edTxtLoginPassword = view.findViewById(R.id.edTxtLoginPassword);

        imgVwLogin = view.findViewById(R.id.imgVwLogin);
        imgVwLoginScrn1 = view.findViewById(R.id.imgVwLoginScrn1);
        imgVwLoginScrn2 = view.findViewById(R.id.imgVwLoginScrn2);

        txtVwLoginRegister = view.findViewById(R.id.txtVwLoginRegister);
        txtVwLoginForgotPass = view.findViewById(R.id.txtVwLoginForgotPass);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setUpListeners() {
        edTxtPasswordOnKeyListener();
        imgVwLoginOnClickListener();
        txtVwForgotPassOnClickListener();
        txtVwRegisterOnClickListener();
    }

    private void initAnims() {
        Animation botLeftAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_left_animation);
        Animation topRightAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_right_animation);
        imgVwLoginScrn1.setAnimation(botLeftAnim);
        imgVwLoginScrn2.setAnimation(topRightAnim);
    }

    private void edTxtPasswordOnKeyListener() {
        edTxtLoginPassword.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                UIUtil.hideKeyboard(requireActivity());
                imgVwLogin.setFocusable(true);
                imgVwLogin.setFocusableInTouchMode(true);
                imgVwLogin.requestFocus();
            }
            return false;
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void imgVwLoginOnClickListener() {
        imgVwLogin.setOnClickListener(v -> {
            login();
        });
    }

    private void txtVwForgotPassOnClickListener() {
        txtVwLoginForgotPass.setOnClickListener(view -> {
            EditText edTxtResetEmail = new EditText(view.getContext());
            AlertDialog.Builder passwordReset = new AlertDialog.Builder(view.getContext());
            passwordReset.setTitle("Reiniciar Contraseña");
            passwordReset.setMessage("Ingrese el correo con el que se registró para recibir el enlace de reinicio de contraseña:");
            passwordReset.setView(edTxtResetEmail);
            passwordReset.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                String email = edTxtResetEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(aVoid -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerLogin, new DoneFragment("Si el correo electrónico proporcionado se encuentra registrado, encontrará un mensaje con el enlace de reinicio de contraseña")).commit())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "¡Ha habido un error!.\nIntente más tarde", Toast.LENGTH_SHORT).show());
            });
            passwordReset.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            passwordReset.create().show();
        });
    }

    private void txtVwRegisterOnClickListener() {
        txtVwLoginRegister.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerLogin, loginActivity.getSignUpFragment()).addToBackStack("sign_up").commit();
            defaultSetUp();
        });
    }

    private void defaultSetUp() {
        clearFields();
        clearErrors();
    }

    private void clearFields() {
        edTxtLoginEmail.setText("");
        edTxtLoginPassword.setText("");
    }

    private void clearErrors() {
        edTxtLoginEmail.setError(null);
        edTxtLoginPassword.setError(null);
    }

    private void login() {
        if (!emptyfields()) {
            String email = edTxtLoginEmail.getText().toString().trim();
            String password = edTxtLoginPassword.getText().toString();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UIUtil.hideKeyboard(requireActivity());
                    Intent menuActivity = new Intent(getActivity(), MenuActivity.class);
                    imgVwLogin.setEnabled(false);
                    startActivity(menuActivity);
                    requireActivity().finish();
                    defaultSetUp();
                } else {
                    Toast.makeText(getContext(), "Correo electrónico y/o\ncontraseña incorrecta(os)", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean emptyfields() {
        boolean empty = false;
        if (edTxtLoginEmail.getText().toString().trim().toLowerCase().equals("")) {
            edTxtLoginEmail.setError("Ingrese un correo electrónico");
            empty = true;
        }
        if (edTxtLoginPassword.getText().toString().equals("")) {
            edTxtLoginPassword.setError("Ingrese una contraseña");
            empty = true;
        }
        return empty;
    }
}