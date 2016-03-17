package com.megglos.service;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * @author meggle
 */
public class EncryptionServiceTest {

    private EncryptionService cut = new EncryptionService();

    /**
     * Just check whether the encrypted result is not the plain text.
     */
    @Test
    public void testEnc() {
        String encSecret = cut.encrypt("secret");
        assertThat(encSecret, is(not("secret")));
        assertTrue(cut.matches("secret", encSecret));
    }

    /**
     * Checks whether one could determine whether a cipher text was derived from a particular plaintext.
     */
    @Test
    public void testCheckOfSecretWithPlaintext() {
        String encSecret = cut.encrypt("secret");
        assertTrue(cut.matches("secret", encSecret));
    }
}
