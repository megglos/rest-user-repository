package com.megglos.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Provides basic encryption functionality.
 *
 * @author meggle
 */
@Service
public class EncryptionService {

    private final PasswordEncoder enc;

    public EncryptionService() {
        // may provide the impl via DI for more flexibility, enough for demo now
        enc = new BCryptPasswordEncoder();
    }

    /**
     * Encrypt given secret string.
     *
     * @param secret plaintext to encrypt
     * @return encrypted secret
     */
    public String encrypt(String secret) {
        return enc.encode(secret);
    }

    /**
     * Check whether an encoded secret was derived from the given secret.
     *
     * @param secret plaintext to check
     * @param encSecret cipher text to check
     * @return true if the encSecret was derived from the secret
     */
    public boolean matches(String secret, String encSecret) {
        return enc.matches(secret, encSecret);
    }

}
