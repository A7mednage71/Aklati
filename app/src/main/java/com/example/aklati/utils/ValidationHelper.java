package com.example.aklati.utils;

import android.util.Patterns;

public class ValidationHelper {

    // Login Validation
    public static ValidationResult validateLogin(String email, String password) {
        if (email == null || email.trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ValidationResult(false, "Please enter a valid email address!");
        }
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Password is required!");
        }
        return new ValidationResult(true, null);
    }

    // Register Validation
    public static ValidationResult validateRegister(String name, String email, String password, String confirmPassword) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Name is required!");
        }
        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ValidationResult(false, "Invalid email format!");
        }
        if (password == null || password.length() < 6) {
            return new ValidationResult(false, "Password must be at least 6 characters!");
        }
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "Passwords do not match!");
        }
        return new ValidationResult(true, null);
    }

    // Helper class to wrap the result
    public static class ValidationResult {
        public final boolean isValid;
        public final String message;

        public ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }
    }
}

