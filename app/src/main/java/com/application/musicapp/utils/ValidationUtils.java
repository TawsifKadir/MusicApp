package com.application.musicapp.utils;

import android.util.Patterns;

public class ValidationUtils {
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Regular expression for validating an Email
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if (!email.matches(emailPattern)) {
            return false;
        }
        return true; // No error
    }

    // Validates password
    public static boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (password.length() < 6) { // Example length requirement
            return false;
        }
        return true; // No error
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        // Basic validation: Check if it matches a phone number pattern
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
}
