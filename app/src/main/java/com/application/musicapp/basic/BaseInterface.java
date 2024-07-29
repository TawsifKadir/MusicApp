package com.application.musicapp.basic;

public interface BaseInterface {
    void initInitial();
    void initViews();
    void initObservers();
    void onNext();
    void onBack();
    void showToast(String message);
    // Method to navigate to another activity
    void navigateToActivity(Class<?> targetActivity);
}
