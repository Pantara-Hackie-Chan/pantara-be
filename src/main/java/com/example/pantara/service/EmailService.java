package com.example.pantara.service;

public interface EmailService {
    void sendVerificationEmail(String to, String otp);
    void sendPasswordResetEmail(String to, String otp);
    void sendWelcomeEmail(String to, String username);
}

