package com.reclaim.util;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Simple, explainable keyword-based text similarity used by the matcher.
 *
 * Step 1 - keywords(): lower-case the text, split it into words, drop very
 *          common words ("the", "near", "lost", ...) and strip a plural "s".
 * Step 2 - overlap(): what fraction of the SHORTER keyword set also appears
 *          in the other set (the "overlap coefficient").
 *
 * The overlap coefficient suits lost & found reports: the owner usually
 * writes a long description while the finder writes a short one, and we
 * don't want the length difference alone to lower the similarity.
 */
public final class TextSimilarity {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "but", "of", "in", "on", "at", "to", "for", "with", "without",
            "from", "by", "near", "into", "onto", "over", "under", "about", "around", "inside", "outside",
            "is", "was", "are", "were", "be", "been", "it", "its", "this", "that", "these", "those",
            "my", "mine", "me", "i", "we", "our", "you", "your", "he", "she", "his", "her", "they", "their",
            "has", "have", "had", "some", "any", "very", "also", "just", "please", "found", "lost",
            "item", "thing", "someone", "somewhere", "left", "there", "here", "which", "who", "when",
            "while", "after", "before", "today", "yesterday", "not", "no", "can", "could", "would", "will");

    private TextSimilarity() {
    }

    /** Extracts the set of meaningful keywords from one or more pieces of text. */
    public static Set<String> keywords(String... texts) {
        Set<String> words = new HashSet<>();
        for (String text : texts) {
            if (text == null) continue;
            for (String raw : text.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+")) {
                if (raw.length() < 2 || STOP_WORDS.contains(raw)) continue;
                words.add(stem(raw));
            }
        }
        return words;
    }

    /**
     * Overlap coefficient = |A ∩ B| / min(|A|, |B|), a value from 0.0 to 1.0.
     * Returns 0 when either set is empty.
     */
    public static double overlap(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        Set<String> smaller = a.size() <= b.size() ? a : b;
        Set<String> larger = smaller == a ? b : a;
        int common = 0;
        for (String word : smaller) {
            if (larger.contains(word)) common++;
        }
        return (double) common / smaller.size();
    }

    /** Words the two sets have in common (shown in the UI to explain a score). */
    public static Set<String> common(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    /** Very light stemming: "keys" -> "key", "earphones" -> "earphone" (but "glass" stays). */
    private static String stem(String word) {
        if (word.length() > 3 && word.endsWith("s") && !word.endsWith("ss")) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }
}
