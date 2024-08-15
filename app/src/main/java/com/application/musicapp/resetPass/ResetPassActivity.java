package com.application.musicapp.resetPass;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.login.LoginViewModel;
import com.application.musicapp.utils.FireBaseHelper;
import com.application.musicapp.utils.TestConfig;
import com.application.musicapp.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassActivity extends BaseActivity {

    private ResetPassViewModel viewModel;
    private FireBaseHelper fireBaseHelper;

    private TextInputLayout emailLayout;
    private TextInputEditText etEmail;
    private Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initInitial();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbarSubtitleTitle();
        setToolbarTitleText("Reset Password");
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initViews();
        initObservers();
    }

    public void setToolbarTitleText(String text){
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(text);
    }

    public void setToolbarSubtitleTitle() {
        TextView toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
        @SuppressLint("DefaultLocale") String title = "";
        toolbarSubtitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initInitial() {
        viewModel = new ViewModelProvider(this).get(ResetPassViewModel.class);
        fireBaseHelper = new FireBaseHelper();
    }

    @Override
    public void initViews() {
        emailLayout = findViewById(R.id.emailLayout);
        etEmail = findViewById(R.id.email);
        btNext = findViewById(R.id.btNext);
    }

    @Override
    public void initObservers() {

        viewModel.getEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                etEmail.setText(s);
            }
        });

        btNext.setOnClickListener(v->{

            String mail = etEmail.getText().toString().trim();

            if (mail.isEmpty()){
                emailLayout.setError("Please enter your email");
            }else if (!ValidationUtils.validateEmail(mail)){
                emailLayout.setError("Please enter valid email");
            }else {
                viewModel.setEmail(mail);
                sendResetPasswordMail(mail);
            }

        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String mail = s.toString().trim();
                if (mail.isEmpty()){
                    emailLayout.setError(null);
                }else if (ValidationUtils.validateEmail(mail)){
                    emailLayout.setError(null);
                }else {
                    emailLayout.setError("Please enter valid email");
                }
            }
        });
    }

    @Override
    public void onNext() {

    }

    public void sendResetPasswordMail(String email){

        showLoadingDialog();

        fireBaseHelper.resetPassword(email, new FireBaseHelper.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                hideLoadingDialog();
                showToast("Resend Password Link has been sent to your email");
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                hideLoadingDialog();
                showToast(e.getMessage());
            }
        });
    }
}