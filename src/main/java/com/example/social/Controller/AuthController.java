package com.example.social.Controller;


import com.example.social.Service.AuthService;
import com.example.social.Service.UserService;
import com.example.social.model.DTO.request.LoginRequest;
import com.example.social.model.DTO.request.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "APIs for user registration and 2FA")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user in the system and in Keycloak, then sends an OTP to their email.")
    @ApiResponse(responseCode = "200", description = "Registration successful, OTP sent.")
    @ApiResponse(responseCode = "400", description = "Invalid input or user with email already exists.")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        authService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok("Registration successful. Please check your email for a verification code.");
    }

    @PostMapping("/otp/request")
    @Operation(summary = "Request a new OTP", description = "Generates and sends a new OTP to the user's email. Typically used after a password check.")
    public ResponseEntity<String> requestOtp(@RequestBody LoginRequest loginRequest) {
        // would validate the password against Keycloak first.
        authService.generateAndSendOtp(loginRequest.getEmail());
        return ResponseEntity.ok("A new verification code has been sent to your email.");
    }

    @PostMapping("/otp/verify")
    @Operation(summary = "Verify the OTP", description = "Verifies the 2FA code. If successful, the client can proceed to get a token from Keycloak.")
    @ApiResponse(responseCode = "200", description = "OTP verification successful.")
    @ApiResponse(responseCode = "401", description = "Invalid OTP or account is locked.")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = authService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("OTP verification successful. You can now log in to get your token.");
        }
        return ResponseEntity.status(401).body("Invalid OTP.");
    }
}