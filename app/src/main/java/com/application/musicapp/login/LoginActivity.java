package com.application.musicapp.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.MainActivity;
import com.application.musicapp.R;
import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.utils.DialogUtils;
import com.application.musicapp.utils.FireBaseHelper;
import com.application.musicapp.utils.GenericDialog;
import com.application.musicapp.utils.PrefHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {
    private final static String TAG = "LoginActivity";
    private TextInputEditText tvUserName;
    private TextInputEditText tvPassword;
    private TextInputLayout tlUserName;
    private Button btSubmit;
    private TextView btRegister;
    private TextView btCredChange;
    private TextView btResetPass;
    private LoginViewModel viewModel;
    private PrefHelper prefHelper;
    private FireBaseHelper fireBaseHelper;
    private Boolean isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Firebase
        FirebaseApp.initializeApp(this);

        initInitial();

        if (prefHelper.isLoggedIn()){
            navigateToMainActivity();
            return;
        }

        initViews();
        initObservers();
    }

    @Override
    public void initInitial() {
        fireBaseHelper = new FireBaseHelper();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        prefHelper = new PrefHelper(this);
        isEmail = true;
    }

    @Override
    public void initObservers() {
        //Dedicating observers to EditTexts to sustain orientation changes
        viewModel.getUsername().observe(this, username -> tvUserName.setText(username));
        viewModel.getPassword().observe(this, password -> tvPassword.setText(password));

        //Setting on click listener for login
        btSubmit.setOnClickListener(v->{
            String username = tvUserName.getText().toString().trim();
            String password = tvPassword.getText().toString().trim();
            Log.d(TAG, "Email: " + username + "\nPassword: " + password);
            authenticate(username,password);
        });

        btRegister.setOnClickListener(v->{
            navigateToRegisterActivity();
        });

        btCredChange.setOnClickListener(v->{
            if (isEmail){
                isEmail = false;
                clearUi();
                tlUserName.setHint(R.string.phone_number);
                btCredChange.setText(R.string.use_email);
            }else{
                isEmail = true;
                clearUi();
                tlUserName.setHint(R.string.email);
                btCredChange.setText(R.string.use_phone_number);
            }
        });

        btResetPass.setOnClickListener(v->{

        });
    }

    @Override
    public void initViews() {
        btSubmit = findViewById(R.id.btSubmit);
        btRegister = findViewById(R.id.btRegister);
        btCredChange = findViewById(R.id.credTypeChange);
        btResetPass = findViewById(R.id.resetPass);
        tvUserName = findViewById(R.id.userName);
        tvPassword = findViewById(R.id.password);
        tlUserName = findViewById(R.id.userNameLayout);
    }

    @Override
    public void onNext() {
        navigateToMainActivity();
    }

    public void authenticate(String email,String password){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }else{
            showLoadingDialog();
            fireBaseHelper.signIn(email, password, new FireBaseHelper.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    prefHelper.saveUser(user.getDisplayName(), true);
                    hideLoadingDialog();
                    onNext();
                }
                @Override
                public void onFailure(Exception e) {
                    hideLoadingDialog();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToMainActivity(){
        navigateToActivity(MainActivity.class);
    }

    private void navigateToRegisterActivity(){
        navigateToActivity(RegisterActivity.class);
    }

    private void clearUi(){
        tvUserName.setText("");
        tvPassword.setText("");
    }
}