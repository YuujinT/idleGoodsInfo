package com.example.idlegoodsinfo.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    private PasswordHasher() {
    }

    public static String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String hashed) {
        return BCrypt.checkpw(rawPassword, hashed);
    }
}

