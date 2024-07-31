package com.application.musicapp.register;

public interface RegistrationFragmentChangeListener {
    void registerUser();
    void navigateToFullNameFragment();
    void navigateToDOBFragment();
    void navigateToEmailFragment();
    void navigateToUserNameFragment();
    void navigateToPasswordFragment();
    void navigateToPhoneNumberFragment();
}
