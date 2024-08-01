package com.application.musicapp.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FireBaseHelper {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firestore;
    private Date pendingDob;
    private String fullName;

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

    public void registerUser(String fullName, String username, String email, String password, Date DOB,RegistrationCallback callback) {
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            callback.onFailure("All fields are required");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            sendVerificationEmail(user,fullName,DOB, callback);
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

    private void sendVerificationEmail(FirebaseUser user, String fullName, Date DOB, RegistrationCallback callback) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            this.fullName = fullName;
                            this.pendingDob = DOB;
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Failed to send verification email: " + task.getException().getMessage());
                        }
                    });
        }
    }

    public void checkEmailVerification(RegistrationCallback callback) {
        FirebaseUser user = getCurrentUser();

        user.reload().addOnCompleteListener(reloadTask -> {
            if (reloadTask.isSuccessful()) {
                if (user.isEmailVerified()) {
                    // Email is verified, now save user data
                    saveUserData(user.getUid(), this.fullName,user.getDisplayName(), user.getEmail(), this.pendingDob, callback);
                } else {
                    callback.onFailure("Email not verified yet.");
                }
            } else {
                callback.onFailure("Failed to reload user data: " + reloadTask.getException().getMessage());
            }
        });
    }

    public void resendVerificationEmail(final ResendCallback callback) {
        FirebaseUser user = getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Failed to resend verification email: " + task.getException().getMessage());
                    }
                });
    }

    // Method to update user profile and save user data
    private void updateUserProfile(FirebaseUser user, String fullName, String username, Date DOB,RegistrationCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(profileTask -> {
                    if (profileTask.isSuccessful()) {
                        saveUserData(user.getUid(), fullName, username, user.getEmail(), DOB,callback);
                    } else {
                        callback.onFailure("Failed to update profile: " + profileTask.getException().getMessage());
                    }
                });
    }

    // Method to save user data to Firebase Firestore
    private void saveUserData(String userId, String fullName, String username, String email, Date DOB,RegistrationCallback callback) {
        User userInfo = new User(fullName, username, email, DOB);

        firestore.collection("users").document(userId).set(userInfo)
                .addOnCompleteListener(dbTask -> {
                    if (dbTask.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Failed to save user data: " + Objects.requireNonNull(dbTask.getException()).getMessage());
                    }
                });
    }

    // Method to verify phone number
    public void verifyPhoneNumber(String phoneNumber, PhoneAuthCallback callback, Activity activity) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity) // You need to pass the current activity here
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // This is called when verification is completed automatically
                        callback.onVerificationCompleted(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        callback.onVerificationFailed(e);
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        callback.onCodeSent(verificationId, token);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Method to update phone number
    public void updatePhoneNumber(PhoneAuthCredential credential, VerificationCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePhoneNumber(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Failed to update phone number: " + task.getException().getMessage());
                        }
                    });
        } else {
            callback.onFailure("User not signed in");
        }
    }

    // Method to delete the user
    public void deleteUser(DeleteUserCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess("User account deleted.");
                } else {
                    callback.onFailure("Failed to delete user account: " + task.getException().getMessage());
                }
            });
        } else {
            callback.onFailure("User not signed in.");
        }
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
        public Date DOB;

        public User() { }

        public User(String fullName, String username, String email, Date DOB) {
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.DOB = DOB;
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

    // Callback interface for verification results
    public interface VerificationCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    // Callback interface for phone authentication results
    public interface PhoneAuthCallback {
        void onVerificationCompleted(PhoneAuthCredential credential);
        void onVerificationFailed(FirebaseException e);
        void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token);
    }

    // Callback interface for deleting user account
    public interface DeleteUserCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    // Callback interface for deleting user account
    public interface ResendCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
