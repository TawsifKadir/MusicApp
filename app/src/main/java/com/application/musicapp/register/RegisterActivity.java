package com.application.musicapp.register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.register.forms.DOBFragment;
import com.application.musicapp.register.forms.EmailFragment;
import com.application.musicapp.register.forms.EmailVerificationFragment;
import com.application.musicapp.register.forms.FullNameFragment;
import com.application.musicapp.register.forms.PasswordFragment;
import com.application.musicapp.register.forms.PhoneNumberFragment;
import com.application.musicapp.register.forms.PhoneNumberVerificationFragment;
import com.application.musicapp.register.forms.UsernameFragment;
import com.application.musicapp.utils.DialogUtils;
import com.application.musicapp.utils.FireBaseHelper;
import com.application.musicapp.utils.GenericDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Date;

public class RegisterActivity extends BaseActivity implements RegistrationFragmentChangeListener{

    private static final String TAG = "RegisterActivity";

    private static final int NO_OF_PAGES = 6;

    private RegisterViewModel viewModel;
    private FireBaseHelper fireBaseHelper;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String fullName;
    private Date DOB;
    private DialogUtils dialogUtils;
    private String verifyId;
    private PhoneAuthProvider.ForceResendingToken rsToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        fireBaseHelper = new FireBaseHelper();
        dialogUtils = new DialogUtils(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (savedInstanceState == null) {
            navigateToFullNameFragment();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbarTitle(int page_no) {
        TextView toolbarSubtitle = findViewById(R.id.toolbarSubtitle);
        @SuppressLint("DefaultLocale") String title = String.format("Step %d of %d",page_no,NO_OF_PAGES);
        toolbarSubtitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment instanceof EmailVerificationFragment){
            dialogUtils.showDialog(getString(R.string.dialog_cancel_message), getString(R.string.dialog_message), new GenericDialog.DialogCallback() {
                @Override
                public void onOkClicked() {
                    deleteUser();
                    finish();
                }
            });
        }else if (fragment instanceof PhoneNumberVerificationFragment){
            dialogUtils.showDialog(getString(R.string.dialog_cancel_message), getString(R.string.dialog_phone_message), new GenericDialog.DialogCallback() {
                @Override
                public void onOkClicked() {
                    finish();
                }
            });
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
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
    public void registerUser() {

        if (viewModel == null) {
            throw new IllegalStateException("ViewModel is not initialized");
        }
        viewModel.getFullName().observe(this, name -> {
            Log.d("RegisterActivity", "Observed full name: " + name);
            fullName = name;
        });
        viewModel.getPassword().observe(this, pass -> {
            password = pass;
        });
        viewModel.getUsername().observe(this, uname -> {
            username = uname;
        });
        viewModel.getEmail().observe(this, mail -> {
            email = mail;
        });
        viewModel.getDate().observe(this, date -> {
            DOB = date;
        });
        showLoadingDialog();
        fireBaseHelper.registerUser(fullName, username, email, password, DOB, new FireBaseHelper.RegistrationCallback() {
            @Override
            public void onSuccess() {
                hideLoadingDialog();
                navigateToEmailVerificationFragment();
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoadingDialog();
                Toast.makeText(RegisterActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                deleteUser();
            }
        });
    }

    @Override
    public void resendEmailVerification() {
        fireBaseHelper.resendVerificationEmail(new FireBaseHelper.ResendCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this,"Verification link sent",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void verifyEmailAndCreateUser() {
        fireBaseHelper.checkEmailVerification(new FireBaseHelper.RegistrationCallback() {
            @Override
            public void onSuccess() {
                viewModel.setIsAccountCreated(true);
                navigateToPhoneNumberFragment();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void verifyPhoneNumber(String phoneNumber) {
        Log.d(TAG, "verifyPhoneNumber() called with: phoneNumber = [" + phoneNumber + "]");

        showLoadingDialog();

        fireBaseHelper.verifyPhoneNumber(phoneNumber, RegisterActivity.this, new FireBaseHelper.PhoneAuthCallback() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                updatePhoneNumber(credential);
                hideLoadingDialog();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideLoadingDialog();
                showToast(e.getMessage());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent() called with: verificationId = [" + verificationId + "], token = [" + token + "]");
                phone = phoneNumber;
                verifyId = verificationId;
                rsToken = token;
                hideLoadingDialog();
                navigateToPhoneNumberVerificationFragment();
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoadingDialog();
                showToast(errorMessage);
            }
        });
    }

    @Override
    public void resendOTP() {

        showLoadingDialog();

        fireBaseHelper.resendOTP(phone, rsToken, RegisterActivity.this, new FireBaseHelper.PhoneAuthCallback() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                updatePhoneNumber(credential);
                hideLoadingDialog();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideLoadingDialog();
                showToast(e.getMessage());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent() called with: verificationId = [" + verificationId + "], token = [" + token + "]");
                verifyId = verificationId;
                rsToken = token;
                hideLoadingDialog();
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoadingDialog();
                showToast(errorMessage);
            }
        });
    }

    @Override
    public void updatePhoneNumber(PhoneAuthCredential credential) {
        fireBaseHelper.updatePhoneNumber(credential, new FireBaseHelper.VerificationCallback() {
            @Override
            public void onSuccess() {
                showToast("Phone number has been added");
                finish();
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void deleteUser() {
        fireBaseHelper.deleteUser(new FireBaseHelper.DeleteUserCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(RegisterActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
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
    public void navigateToEmailVerificationFragment() {
        loadFragment(new EmailVerificationFragment(), true);
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
        loadFragment(new PhoneNumberFragment(),false);
    }

    @Override
    public void navigateToPhoneNumberVerificationFragment() {
        loadFragment(new PhoneNumberVerificationFragment(), true);
    }

    @Override
    public String getVerificationId() {
        return verifyId;
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