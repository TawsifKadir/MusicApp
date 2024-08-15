package com.application.musicapp.register;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public interface RegistrationFragmentChangeListener {
    void registerUser();
    void resendEmailVerification();
    void verifyEmailAndCreateUser();
    void verifyPhoneNumber(String phoneNumber);
    void resendOTP();
    void updatePhoneNumber(PhoneAuthCredential credential);
    void deleteUser();
    void navigateToFullNameFragment();
    void navigateToDOBFragment();
    void navigateToEmailFragment();
    void navigateToEmailVerificationFragment();
    void navigateToUserNameFragment();
    void navigateToPasswordFragment();
    void navigateToPhoneNumberFragment();
    void navigateToPhoneNumberVerificationFragment();
    String getVerificationId();
}
