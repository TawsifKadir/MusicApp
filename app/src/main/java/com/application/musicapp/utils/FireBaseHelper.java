package com.application.musicapp.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FireBaseHelper {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore;

    public FireBaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firestore = FirebaseFirestore.getInstance();
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

    // Method to register a new user
    public void registerUser(String fullName, String username, String email, String password, String confirmPassword, String phoneNumber, RegistrationCallback callback) {
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
            callback.onFailure("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            callback.onFailure("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            saveUserData(user.getUid(), fullName, username, email, phoneNumber, callback);
                                        } else {
                                            callback.onFailure("Failed to update profile: " + profileTask.getException().getMessage());
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    // Method to save user data to Firebase
    private void saveUserData(String userId, String fullName, String username, String email, String phoneNumber, RegistrationCallback callback) {
        User userInfo = new User(fullName, username, email, phoneNumber);

        // Choose between Realtime Database or Firestore
        // Uncomment the one you need:

        // Save to Firebase Realtime Database
//        databaseReference.child(userId).setValue(userInfo)
//                .addOnCompleteListener(dbTask -> {
//                    if (dbTask.isSuccessful()) {
//                        callback.onSuccess();
//                    } else {
//                        callback.onFailure("Failed to save user data: " + dbTask.getException().getMessage());
//                    }
//                });

        // Save to Firestore
         firestore.collection("users").document(userId).set(userInfo)
             .addOnCompleteListener(dbTask -> {
                 if (dbTask.isSuccessful()) {
                     callback.onSuccess();
                 } else {
                     callback.onFailure("Failed to save user data: " + Objects.requireNonNull(dbTask.getException()).getMessage());
                 }
             });
    }

    // Method to check if the user is already signed in
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Inner class for user data
    private static class User {
        public String fullName;
        public String username;
        public String email;
        public String phoneNumber;

        public User() { }

        public User(String fullName, String username, String email, String phoneNumber) {
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
    }

    // Callback interface for authentication results
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }

    // Callback interface for registration results
    public interface RegistrationCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
