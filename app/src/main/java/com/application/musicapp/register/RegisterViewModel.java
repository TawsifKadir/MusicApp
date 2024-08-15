package com.application.musicapp.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Date> dateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isAccountCreated = new MutableLiveData<>(false);
    private final MutableLiveData<String> countryCode = new MutableLiveData<>();

    public LiveData<String> getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        Log.d("RegisterViewModel", "Setting full name: " + fullName);
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

    public LiveData<Date> getDate() {
        return dateLiveData;
    }

    public void setDate(Date date) {
        dateLiveData.setValue(date);
    }

    public LiveData<Boolean> getIsAccountCreated(){
        return isAccountCreated;
    }

    public void setIsAccountCreated(Boolean isAccountCreated){
        this.isAccountCreated.setValue(isAccountCreated);
    }

    public void setCountryCode(String countryCode){
        this.countryCode.setValue(countryCode);
    }

    public LiveData<String> getCountryCode(){
        return countryCode;
    }
}
