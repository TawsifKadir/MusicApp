package com.application.musicapp.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> fullName = new MutableLiveData<>();
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    public LiveData<String> getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.setValue(fullName);
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public LiveData<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public LiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword.setValue(confirmPassword);
    }

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }
}
