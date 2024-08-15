package com.application.musicapp.resetPass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResetPassViewModel extends ViewModel {
    private final MutableLiveData<String> email = new MutableLiveData<>();

    public LiveData<String> getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email.setValue(email);
    }
}
