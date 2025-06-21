package com.example.social.model.DTO.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class OtpVerificationRequest {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP cannot be empty")
    private String otp;


    public OtpVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
}