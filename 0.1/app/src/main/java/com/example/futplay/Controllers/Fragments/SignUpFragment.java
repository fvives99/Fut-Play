package com.example.futplay.Controllers.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.futplay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    private EditText edTxtSignUpFullName;
    private EditText edTxtSignUpEmail;
    private EditText edTxtSignUpPassword;
    private EditText edTxtSignUpConfirmPassword;
    private EditText edTxtSignUpBirthDate;
    private EditText edTxtSignUpPhoneNumber;

    private ImageView imgVwSignUp;
    private ImageView imgVwSignUpScrn1;
    private ImageView imgVwSignUpScrn2;

    private CountryCodePicker cntryCdPckrSignUpCountryCode;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String userID;

    private int month = 0, day = 0, year = 0;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Additional Settings
        viewsMatching(view);
        edTxtSignUpBirthDateSetUp();
        setUpListeners();
        initAnims();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void viewsMatching(View view) {
        edTxtSignUpFullName = view.findViewById(R.id.edTxtSignUpFullName);
        edTxtSignUpEmail = view.findViewById(R.id.edTxtSignUpEmail);
        edTxtSignUpPassword = view.findViewById(R.id.edTxtSignUpPassword);
        edTxtSignUpConfirmPassword = view.findViewById(R.id.edTxtSignUpConfirmPassword);
        edTxtSignUpBirthDate = view.findViewById(R.id.edTxtSignUpBirthDate);
        edTxtSignUpPhoneNumber = view.findViewById(R.id.edTxtSignUpPhoneNumber);

        imgVwSignUp = view.findViewById(R.id.imgVwSignUp);
        imgVwSignUpScrn1 = view.findViewById(R.id.imgVwSignUpScrn1);
        imgVwSignUpScrn2 = view.findViewById(R.id.imgVwSignUpScrn2);

        cntryCdPckrSignUpCountryCode = view.findViewById(R.id.cntryCdPckrSignUpCountryCode);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @SuppressLint("SetTextI18n")
    private void edTxtSignUpBirthDateSetUp() {
        edTxtSignUpBirthDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR) - 18;

            DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Aceptar", dialog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setOnShowListener(dialogInterface -> dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE));
            dialog.setOnDismissListener(dialogInterface -> {
                edTxtSignUpPhoneNumber.requestFocus();
                showKeyboard();
            });
            dialog.setCancelable(false);
            dialog.show();
        });

        dateSetListener = (datePicker, yearDP, monthDP, dayOfMonthDP) -> {
            monthDP++;
            String formattedMonth = "" + monthDP;
            String formattedDayOfMonth = "" + dayOfMonthDP;

            if (monthDP < 10) {
                formattedMonth = "0" + monthDP;
            }
            if (dayOfMonthDP < 10) {
                formattedDayOfMonth = "0" + dayOfMonthDP;
            }

            edTxtSignUpBirthDate.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + yearDP);
            edTxtSignUpBirthDate.setError(null);
        };
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setUpListeners() {
        imgVwSignUpOnClickListener();
        lastFieldOnKeyListener();
        edTxtFieldsOnFocusChangeListeners();
        edTxtSignUpPasswordsTextChangedListeners();
        edTxtSignUpConfirmPasswordOnEditorActionListener();
    }

    private void imgVwSignUpOnClickListener() {
        imgVwSignUp.setOnClickListener(v -> register());
    }

    private void lastFieldOnKeyListener() {
        edTxtSignUpPhoneNumber.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                UIUtil.hideKeyboard(requireActivity());
                imgVwSignUp.setFocusable(true);
                imgVwSignUp.setFocusableInTouchMode(true);
                imgVwSignUp.requestFocus();
            }
            return false;
        });
    }

    private void edTxtFieldsOnFocusChangeListeners() {
        edTxtSignUpEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !edTxtSignUpEmail.getText().toString().equals("") && !Patterns.EMAIL_ADDRESS.matcher(edTxtSignUpEmail.getText().toString().trim()).matches()) {
                edTxtSignUpEmail.setError("Correo electrónico inválido");
            }
        });

        edTxtSignUpPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !edTxtSignUpPassword.getText().toString().equals("") && edTxtSignUpPassword.getText().toString().length() < 6) {
                edTxtSignUpPassword.setError("La contraseña debe tener 6 o más caracteres");
            }
        });

        edTxtSignUpConfirmPassword.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !edTxtSignUpConfirmPassword.getText().toString().equals("") && edTxtSignUpConfirmPassword.getText().toString().length() < 6) {
                edTxtSignUpConfirmPassword.setError("La contraseña debe tener 6 o más caracteres");
            }
        });
    }

    private void edTxtSignUpPasswordsTextChangedListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edTxtSignUpPassword.setError(null);
                edTxtSignUpConfirmPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        edTxtSignUpPassword.addTextChangedListener(textWatcher);
        edTxtSignUpConfirmPassword.addTextChangedListener(textWatcher);
    }

    private void edTxtSignUpConfirmPasswordOnEditorActionListener() {
        edTxtSignUpConfirmPassword.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                edTxtSignUpBirthDate.performClick();
                return true;
            }
            return false;
        });
    }

    private void initAnims() {
        Animation botLeftAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_left_animation);
        Animation topRightAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.top_right_animation);
        imgVwSignUpScrn1.setAnimation(botLeftAnim);
        imgVwSignUpScrn2.setAnimation(topRightAnim);
    }

    public void defaultSetUp() {
        clearFields();
        clearErrors();
    }

    private void clearFields() {
        edTxtSignUpFullName.setText("");
        edTxtSignUpEmail.setText("");
        edTxtSignUpPassword.setText("");
        edTxtSignUpConfirmPassword.setText("");
        edTxtSignUpBirthDate.setText("");
        edTxtSignUpPhoneNumber.setText("");
    }

    private void clearErrors() {
        edTxtSignUpFullName.setError(null);
        edTxtSignUpEmail.setError(null);
        edTxtSignUpPassword.setError(null);
        edTxtSignUpConfirmPassword.setError(null);
        edTxtSignUpBirthDate.setError(null);
        edTxtSignUpPhoneNumber.setError(null);
    }

    private void register() {
        if (!invalidFields()) {
            String email = edTxtSignUpEmail.getText().toString().trim();
            String password = edTxtSignUpPassword.getText().toString();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) {
                        userID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", camelCase(edTxtSignUpFullName.getText().toString().trim().replaceAll(" +", " ")));
                        user.put("email", edTxtSignUpEmail.getText().toString().trim());
                        user.put("birthDate", edTxtSignUpBirthDate.getText().toString());
                        user.put("countryCode", cntryCdPckrSignUpCountryCode.getFullNumber());
                        user.put("phoneNumber", edTxtSignUpPhoneNumber.getText().toString().trim());
                        documentReference.set(user).addOnSuccessListener(aVoid -> {
                            defaultSetUp();
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragContainerLogin, new DoneFragment("¡Registro Exitoso!", "menu")).addToBackStack("done").commit();
                        });
                    } else {
                        throw Objects.requireNonNull(task.getException());
                    }
                } catch (FirebaseAuthUserCollisionException e) {
                    edTxtSignUpEmail.setError("El correo electrónico ingresado ya se encuentra registrado");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Registro Fallido.\nIntente más tarde", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean invalidFields() {
        boolean invalid = false;
        if (edTxtSignUpFullName.getText().toString().trim().equals("")) {
            edTxtSignUpFullName.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtSignUpEmail.getText().toString().trim().equals("")) {
            edTxtSignUpEmail.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtSignUpPassword.getText().toString().equals("")) {
            edTxtSignUpPassword.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtSignUpConfirmPassword.getText().toString().equals("")) {
            edTxtSignUpConfirmPassword.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtSignUpBirthDate.getText().toString().equals("")) {
            edTxtSignUpBirthDate.setError("Campo Obligatorio");
            invalid = true;
        }
        if (edTxtSignUpPhoneNumber.getText().toString().trim().equals("")) {
            edTxtSignUpPhoneNumber.setError("Campo Obligatorio");
            invalid = true;
        }
        if (!edTxtSignUpPassword.getText().toString().equals(edTxtSignUpConfirmPassword.getText().toString())) {
            edTxtSignUpPassword.setError("Las contraseñas no coinciden");
            invalid = true;
        }
        if(erroredFields()){
            invalid = true;
        }
        return invalid;
    }

    private boolean erroredFields(){
        boolean errored = false;
        if(edTxtSignUpEmail.getError()!=null || edTxtSignUpPassword.getError()!= null
                || edTxtSignUpConfirmPassword.getError()!=null){
            errored = true;
        }
        return errored;
    }

    public static String camelCase(String str) {
        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int length = builder.length();

        for (int i = 0; i < length; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }
        return builder.toString();
    }
}