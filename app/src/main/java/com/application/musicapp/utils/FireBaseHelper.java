package com.application.musicapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseHelper {
    private FirebaseAuth mAuth;

    public FireBaseHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Method to sign in a user
    public void signIn(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Method to check if the user is already signed in
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Callback interface for authentication results
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}
