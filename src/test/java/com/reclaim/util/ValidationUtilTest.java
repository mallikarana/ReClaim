package com.reclaim.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    @Test
    void email() {
        assertTrue(ValidationUtil.isValidEmail("student@geu.ac.in"));
        assertFalse(ValidationUtil.isValidEmail("student@"));
        assertFalse(ValidationUtil.isValidEmail("no spaces@geu.ac.in"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    void phone() {
        assertTrue(ValidationUtil.isValidPhone("9876543210"));
        assertFalse(ValidationUtil.isValidPhone("1234567890"));
        assertFalse(ValidationUtil.isValidPhone("98765"));
    }

    @Test
    void name() {
        assertTrue(ValidationUtil.isValidName("Kartik Maithani"));
        assertTrue(ValidationUtil.isValidName("D'Souza"));
        assertFalse(ValidationUtil.isValidName("A"));
        assertFalse(ValidationUtil.isValidName("<script>"));
    }

    @Test
    void passwordStrength() {
        assertNull(ValidationUtil.checkPasswordStrength("campus2026"));
        assertNotNull(ValidationUtil.checkPasswordStrength("short1"));
        assertNotNull(ValidationUtil.checkPasswordStrength("onlyletters"));
        assertNotNull(ValidationUtil.checkPasswordStrength("12345678"));
    }

    @Test
    void cleanTrimsAndNullsBlank() {
        assertEquals("abc", ValidationUtil.clean("  abc "));
        assertNull(ValidationUtil.clean("   "));
        assertNull(ValidationUtil.clean(null));
    }
}
