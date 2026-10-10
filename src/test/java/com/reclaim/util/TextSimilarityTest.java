package com.reclaim.util;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextSimilarityTest {

    @Test
    void keywordsDropStopWordsAndPunctuationAndPlurals() {
        Set<String> words = TextSimilarity.keywords("I lost my KEYS near the Library, with a red keychain!");
        assertEquals(Set.of("key", "library", "red", "keychain"), words);
    }

    @Test
    void keywordsHandleNullAndEmpty() {
        assertTrue(TextSimilarity.keywords((String) null).isEmpty());
        assertTrue(TextSimilarity.keywords("", "   ").isEmpty());
    }

    @Test
    void overlapUsesTheShorterSet() {
        Set<String> longer = Set.of("black", "samsung", "phone", "cracked", "screen", "cover");
        Set<String> shorter = Set.of("samsung", "phone");
        assertEquals(1.0, TextSimilarity.overlap(longer, shorter), 1e-9);
        assertEquals(0.5, TextSimilarity.overlap(Set.of("blue", "bottle"), Set.of("blue", "bag", "pen")), 1e-9);
    }

    @Test
    void overlapIsZeroForEmptyOrDisjointSets() {
        assertEquals(0.0, TextSimilarity.overlap(Set.of(), Set.of("a")), 1e-9);
        assertEquals(0.0, TextSimilarity.overlap(Set.of("wallet"), Set.of("bottle")), 1e-9);
        assertFalse(TextSimilarity.common(Set.of("x"), Set.of("y")).contains("x"));
    }
}
