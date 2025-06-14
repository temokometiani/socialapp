package com.example.social.Service;

import com.example.social.Repository.UserRepository;
import com.example.social.exception.ApiException;
import com.example.social.exception.UserNotFoundException;
import com.example.social.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;
    private final UserRepository userRepository;

    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final long OTP_EXPIRATION_MINUTES = 5;
    private static final long LOCKOUT_DURATION_MINUTES = 30;

    private static final String OTP_KEY_PREFIX = "2fa:otp:";
    private static final String ATTEMPTS_KEY_PREFIX = "2fa:attempts:";
    private static final String LOCKOUT_KEY_PREFIX = "2fa:lockout:";


    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user registered with this email address."));

        if (isUserLocked(user.getEmail())) {
            throw new ApiException("Account is temporarily locked due to too many failed attempts. Please try again later.", HttpStatus.FORBIDDEN);
        }

        String otp = generateNumericOtp();
        String otpKey = OTP_KEY_PREFIX + user.getEmail();
        redisTemplate.opsForValue().set(otpKey, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        LOGGER.info("Generated 2FA OTP for user {}", user.getEmail());

        String subject = "Your Social Platform Verification Code";
        String body = String.format("Hello %s,\n\nYour two-factor authentication code is: %s\n\nThis code will expire in %d minutes.",
                user.getName(), otp, OTP_EXPIRATION_MINUTES);
        emailService.sendEmail(user.getEmail(), subject, body);
    }


    public boolean verifyOtp(String email, String providedOtp) {
        if (isUserLocked(email)) {
            throw new ApiException("Account is temporarily locked due to too many failed attempts.", HttpStatus.FORBIDDEN);
        }

        String otpKey = OTP_KEY_PREFIX + email;
        String storedOtp = redisTemplate.opsForValue().get(otpKey);

        if (providedOtp != null && providedOtp.equals(storedOtp)) {
            clearOtpData(email);
            LOGGER.info("2FA OTP verification successful for user {}", email);
            return true;
        } else {
            handleFailedAttempt(email);
            return false;
        }
    }

    private void handleFailedAttempt(String email) {
        String attemptsKey = ATTEMPTS_KEY_PREFIX + email;
        long attempts = redisTemplate.opsForValue().increment(attemptsKey);
        LOGGER.warn("Failed 2FA OTP attempt #{} for user {}", attempts, email);

        if (attempts >= MAX_OTP_ATTEMPTS) {
            String lockoutKey = LOCKOUT_KEY_PREFIX + email;
            redisTemplate.opsForValue().set(lockoutKey, "locked", LOCKOUT_DURATION_MINUTES, TimeUnit.MINUTES);
            clearOtpData(email);
            LOGGER.error("User {} locked for {} minutes due to max failed 2FA attempts.", email, LOCKOUT_DURATION_MINUTES);
            throw new ApiException(String.format("Invalid OTP. Your account is now locked for %d minutes.", LOCKOUT_DURATION_MINUTES), HttpStatus.FORBIDDEN);
        } else {
            throw new ApiException("Invalid OTP. Please try again.", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isUserLocked(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOCKOUT_KEY_PREFIX + email));
    }

    private void clearOtpData(String email) {
        redisTemplate.delete(OTP_KEY_PREFIX + email);
        redisTemplate.delete(ATTEMPTS_KEY_PREFIX + email);
    }

    private String generateNumericOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
