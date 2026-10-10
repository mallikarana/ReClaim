package com.reclaim.service;

import com.reclaim.model.Colour;
import com.reclaim.model.Item;
import com.reclaim.model.ItemType;
import com.reclaim.model.Match;
import com.reclaim.util.TextSimilarity;

import java.time.temporal.ChronoUnit;
import java.util.Set;

/**
 * Deterministic, rule-based scoring of one LOST report against one FOUND report.
 *
 * The score is out of 100 and made of five independent parts, exactly the
 * attributes named in the project proposal:
 *
 *   Attribute     Weight  Rule
 *   -----------   ------  -----------------------------------------------------
 *   Category        25    same category = 25, otherwise 0
 *   Colour          20    same colour = 20, similar shade (grey/silver,
 *                         gold/yellow, red/pink, brown/beige) = 10, otherwise
 *                         if either is "multicolour"/"not sure" = 5, else 0
 *   Location        25    same campus zone = 25; different zones but the
 *                         free-text location details share a word = 10
 *   Date            15    found within 0-1 day of loss = 15, <= 3 days = 12,
 *                         <= 7 = 9, <= 14 = 6, <= 30 = 3; found more than a
 *                         day BEFORE it was lost = 0 (impossible)
 *   Description     15    15 x overlap of keywords from title + description
 *
 * Pairs scoring at least {@link #MIN_SCORE} are shown as potential matches.
 * The class has no database or web code, so it is fully unit-tested.
 */
public class MatchScorer {

    public static final int WEIGHT_CATEGORY = 25;
    public static final int WEIGHT_COLOUR = 20;
    public static final int WEIGHT_LOCATION = 25;
    public static final int WEIGHT_DATE = 15;
    public static final int WEIGHT_DESCRIPTION = 15;

    /** Minimum total score for a pair to be reported as a potential match. */
    public static final int MIN_SCORE = 40;

    /**
     * Scores a pair of reports. The two arguments may be given in either order;
     * the result always has the LOST report as lostItem and the FOUND report as foundItem.
     */
    public Match score(Item a, Item b) {
        Item lost = a.getType() == ItemType.LOST ? a : b;
        Item found = lost == a ? b : a;

        Match match = new Match(lost, found);
        match.setCategoryScore(categoryScore(lost, found));
        match.setColourScore(colourScore(lost.getColour(), found.getColour()));
        match.setLocationScore(locationScore(lost, found));
        match.setDateScore(dateScore(lost, found));
        match.setDescriptionScore(descriptionScore(lost, found));
        return match;
    }

    public boolean isPotentialMatch(Match match) {
        return match.getScore() >= MIN_SCORE;
    }

    int categoryScore(Item lost, Item found) {
        return lost.getCategory() != null && lost.getCategory() == found.getCategory() ? WEIGHT_CATEGORY : 0;
    }

    int colourScore(Colour lost, Colour found) {
        if (lost == null || found == null) return 0;
        if (lost == found && lost != Colour.OTHER) return WEIGHT_COLOUR;        // exact colour
        if (lost.isUncertain() || found.isUncertain()) return 5;                // can't be sure
        if (lost.getFamily().equals(found.getFamily())) return WEIGHT_COLOUR / 2; // similar shade
        return 0;
    }

    int locationScore(Item lost, Item found) {
        if (lost.getLocation() != null && lost.getLocation() == found.getLocation()) {
            return WEIGHT_LOCATION;
        }
        Set<String> lostWords = TextSimilarity.keywords(lost.getLocationDetails());
        Set<String> foundWords = TextSimilarity.keywords(found.getLocationDetails());
        return TextSimilarity.common(lostWords, foundWords).isEmpty() ? 0 : 10;
    }

    int dateScore(Item lost, Item found) {
        if (lost.getIncidentDate() == null || found.getIncidentDate() == null) return 0;
        long days = ChronoUnit.DAYS.between(lost.getIncidentDate(), found.getIncidentDate());
        if (days < -1) return 0;   // found well before it was lost: cannot be the same item
        if (days <= 1) return WEIGHT_DATE;
        if (days <= 3) return 12;
        if (days <= 7) return 9;
        if (days <= 14) return 6;
        if (days <= 30) return 3;
        return 0;
    }

    int descriptionScore(Item lost, Item found) {
        Set<String> lostWords = TextSimilarity.keywords(lost.getTitle(), lost.getDescription());
        Set<String> foundWords = TextSimilarity.keywords(found.getTitle(), found.getDescription());
        return (int) Math.round(WEIGHT_DESCRIPTION * TextSimilarity.overlap(lostWords, foundWords));
    }
}
