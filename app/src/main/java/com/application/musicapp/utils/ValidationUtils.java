package com.application.musicapp.utils;

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
}
