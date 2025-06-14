package com.example.social.util;

public final class AppConstants {

    // Private constructor to prevent instantiation
    private AppConstants() {}


    public static final class RedisKeys {
        public static final String OTP_KEY_PREFIX = "2fa:otp:";
        public static final String ATTEMPTS_KEY_PREFIX = "2fa:attempts:";
        public static final String LOCKOUT_KEY_PREFIX = "2fa:lockout:";

        private RedisKeys() {}
    }

    public static final class TwoFactorAuth {
        public static final int MAX_OTP_ATTEMPTS = 5;
        public static final long OTP_EXPIRATION_MINUTES = 5;
        public static final long LOCKOUT_DURATION_MINUTES = 30;

        private TwoFactorAuth() {}
    }


    public static final class Roles {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";

        private Roles() {}
    }
}

