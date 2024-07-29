package com.application.musicapp.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.MainActivity;
import com.application.musicapp.R;
import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.utils.FireBaseHelper;
import com.application.musicapp.utils.PrefHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {
    private final static String TAG = "LoginActivity";
    private EditText tvUserName;
    private EditText tvPassword;
    private Button btSubmit;
    private Button btRegister;
    private LoginViewModel viewModel;
    private PrefHelper prefHelper;
    private FireBaseHelper fireBaseHelper;

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
    }

    @Override
    public void initViews() {
        btSubmit = findViewById(R.id.btSubmit);
        btRegister = findViewById(R.id.btRegister);
        tvUserName = findViewById(R.id.userName);
        tvPassword = findViewById(R.id.password);
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
}