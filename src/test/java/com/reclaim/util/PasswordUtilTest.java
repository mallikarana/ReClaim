package com.reclaim.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void correctPasswordVerifies() {
        String hash = PasswordUtil.hash("Secret123");
        assertTrue(PasswordUtil.verify("Secret123", hash));
    }

    @Test
    void wrongPasswordFails() {
        String hash = PasswordUtil.hash("Secret123");
        assertFalse(PasswordUtil.verify("secret123", hash));
        assertFalse(PasswordUtil.verify("", hash));
        assertFalse(PasswordUtil.verify(null, hash));
    }

    @Test
    void samePasswordGetsDifferentSalts() {
        assertNotEquals(PasswordUtil.hash("Secret123"), PasswordUtil.hash("Secret123"));
    }

    @Test
    void malformedStoredHashIsRejected() {
        assertFalse(PasswordUtil.verify("Secret123", "not-a-hash"));
        assertFalse(PasswordUtil.verify("Secret123", "1:%%%:%%%"));
        assertFalse(PasswordUtil.verify("Secret123", null));
    }
}
