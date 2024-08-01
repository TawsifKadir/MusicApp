package com.application.musicapp.register;

public interface RegistrationFragmentChangeListener {
    void registerUser();
    void resendEmailVerification();
    void verifyEmailAndCreateUser();
    void addPhoneNumber();
    void deleteUser();
    void navigateToFullNameFragment();
    void navigateToDOBFragment();
    void navigateToEmailFragment();
    void navigateToEmailVerificationFragment();
    void navigateToUserNameFragment();
    void navigateToPasswordFragment();
    void navigateToPhoneNumberFragment();
    void navigateToPhoneNumberVerificationFragment();
}
