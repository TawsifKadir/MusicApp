package com.application.musicapp.register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.register.forms.DOBFragment;
import com.application.musicapp.register.forms.EmailFragment;
import com.application.musicapp.register.forms.FullNameFragment;
import com.application.musicapp.register.forms.PasswordFragment;
import com.application.musicapp.register.forms.PhoneNumberFragment;
import com.application.musicapp.register.forms.UsernameFragment;
import com.application.musicapp.utils.FireBaseHelper;

public class RegisterActivity extends BaseActivity implements RegistrationFragmentChangeListener{

    private RegisterViewModel viewModel;
    private FireBaseHelper fireBaseHelper;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String fullName;
    private String DOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            navigateToFullNameFragment();
            return;
        }

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        fireBaseHelper = new FireBaseHelper();
    }

    private void loadFragment(Fragment fragment,Boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }


    @Override
    public void registerUser() {
        viewModel.getUsername().observe(this, uname -> username = uname);
        viewModel.getPassword().observe(this, pass -> password = pass);
        viewModel.getFullName().observe(this, fName -> fullName = fName);
        viewModel.getEmail().observe(this, email_temp -> email = email_temp);
        viewModel.getPhoneNumber().observe(this, phone_temp -> phoneNumber = phone_temp);

        showLoadingDialog();
        // Register user with Firebase
        fireBaseHelper.registerUser(fullName, username, email, password,  phoneNumber, new FireBaseHelper.RegistrationCallback() {
            @Override
            public void onSuccess() {
                hideLoadingDialog();
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                // Navigate to next screen or update UI
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoadingDialog();
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(int page_no) {
        TextView toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
        @SuppressLint("DefaultLocale") String title = String.format("Step %d of 5",page_no);
        toolbarSubtitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the back stack entry
            fragmentManager.popBackStack();

            // Check if the back stack is now empty
            if (fragmentManager.getBackStackEntryCount() == 0) {
                // If it's empty, finish the activity
                finish();
            }
        } else {
            // No fragments in the back stack, finish the activity directly
            super.onBackPressed();
        }
    }

    @Override
    public void navigateToFullNameFragment() {
        loadFragment(new FullNameFragment(),false);
    }

    @Override
    public void navigateToDOBFragment() {
        loadFragment(new DOBFragment(),true);
    }

    @Override
    public void navigateToEmailFragment() {
        loadFragment(new EmailFragment(),true);
    }

    @Override
    public void navigateToUserNameFragment() {
        loadFragment(new UsernameFragment(),true);
    }

    @Override
    public void navigateToPasswordFragment() {
        loadFragment(new PasswordFragment(),true);
    }

    @Override
    public void navigateToPhoneNumberFragment() {
        loadFragment(new PhoneNumberFragment(),true);
    }

    @Override
    public void initInitial() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initObservers() {

    }

    @Override
    public void onNext() {

    }
}