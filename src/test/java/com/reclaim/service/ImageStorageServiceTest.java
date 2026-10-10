package com.reclaim.service;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ImageStorageServiceTest {

    @Test
    void detectsImageTypesFromMagicBytes() {
        assertEquals("jpg", ImageStorageService.detectImageType(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00}));
        assertEquals("png", ImageStorageService.detectImageType(
                new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A}));
        assertEquals("gif", ImageStorageService.detectImageType("GIF89a".getBytes(StandardCharsets.US_ASCII)));
        assertEquals("webp", ImageStorageService.detectImageType("RIFF0000WEBPVP8 ".getBytes(StandardCharsets.US_ASCII)));
    }

    @Test
    void rejectsNonImages() {
        assertNull(ImageStorageService.detectImageType("<?php echo 'hi'; ?>".getBytes(StandardCharsets.US_ASCII)));
        assertNull(ImageStorageService.detectImageType(new byte[0]));
    }
}
