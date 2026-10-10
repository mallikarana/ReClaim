package com.reclaim.service;

import com.reclaim.model.CampusLocation;
import com.reclaim.model.Category;
import com.reclaim.model.Colour;
import com.reclaim.model.Item;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.ItemType;
import com.reclaim.model.Match;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchScorerTest {

    private final MatchScorer scorer = new MatchScorer();
    private static final LocalDate DAY = LocalDate.of(2026, 10, 1);

    private static Item item(ItemType type, Category category, Colour colour, CampusLocation location,
                             LocalDate date, String title, String description) {
        Item item = new Item();
        item.setType(type);
        item.setCategory(category);
        item.setColour(colour);
        item.setLocation(location);
        item.setIncidentDate(date);
        item.setTitle(title);
        item.setDescription(description);
        item.setStatus(ItemStatus.OPEN);
        return item;
    }

    @Test
    void weightsAddUpTo100() {
        assertEquals(100, MatchScorer.WEIGHT_CATEGORY + MatchScorer.WEIGHT_COLOUR + MatchScorer.WEIGHT_LOCATION
                + MatchScorer.WEIGHT_DATE + MatchScorer.WEIGHT_DESCRIPTION);
    }

    @Test
    void identicalReportsScore100() {
        Item lost = item(ItemType.LOST, Category.MOBILE_PHONE, Colour.BLACK, CampusLocation.LIBRARY, DAY,
                "Samsung Galaxy phone", "Black Samsung Galaxy with cracked screen guard");
        Item found = item(ItemType.FOUND, Category.MOBILE_PHONE, Colour.BLACK, CampusLocation.LIBRARY, DAY,
                "Samsung Galaxy phone", "Black Samsung Galaxy with cracked screen guard");
        Match m = scorer.score(lost, found);
        assertEquals(100, m.getScore());
        assertEquals("High", m.getConfidence());
        assertTrue(scorer.isPotentialMatch(m));
    }

    @Test
    void argumentOrderDoesNotMatter() {
        Item lost = item(ItemType.LOST, Category.KEYS, Colour.SILVER, CampusLocation.CAFETERIA, DAY,
                "Bike keys", "Two keys on a red keychain");
        Item found = item(ItemType.FOUND, Category.KEYS, Colour.GREY, CampusLocation.CAFETERIA, DAY.plusDays(2),
                "Keys", "Found keys with red keychain");
        Match a = scorer.score(lost, found);
        Match b = scorer.score(found, lost);
        assertEquals(a.getScore(), b.getScore());
        assertSame(lost, b.getLostItem());
        assertSame(found, b.getFoundItem());
    }

    @Test
    void unrelatedReportsAreNotMatched() {
        Item lost = item(ItemType.LOST, Category.WALLET, Colour.BROWN, CampusLocation.HOSTEL, DAY,
                "Leather wallet", "Brown leather wallet with college ID");
        Item found = item(ItemType.FOUND, Category.WATER_BOTTLE, Colour.BLUE, CampusLocation.SPORTS_COMPLEX,
                DAY.plusDays(20), "Bottle", "Steel water bottle with stickers");
        Match m = scorer.score(lost, found);
        assertFalse(scorer.isPotentialMatch(m));
        assertEquals(0, m.getCategoryScore());
        assertEquals(0, m.getColourScore());
        assertEquals(0, m.getLocationScore());
    }

    @Test
    void colourRules() {
        assertEquals(20, scorer.colourScore(Colour.BLACK, Colour.BLACK));
        assertEquals(20, scorer.colourScore(Colour.MULTICOLOUR, Colour.MULTICOLOUR));
        assertEquals(10, scorer.colourScore(Colour.GREY, Colour.SILVER));
        assertEquals(10, scorer.colourScore(Colour.GOLD, Colour.YELLOW));
        assertEquals(5, scorer.colourScore(Colour.BLUE, Colour.MULTICOLOUR));
        assertEquals(5, scorer.colourScore(Colour.OTHER, Colour.OTHER));
        assertEquals(0, scorer.colourScore(Colour.BLACK, Colour.WHITE));
    }

    @Test
    void dateRules() {
        Item lost = item(ItemType.LOST, Category.BAG, Colour.BLACK, CampusLocation.LIBRARY, DAY, "Bag", "Black bag");
        assertEquals(15, scorer.dateScore(lost, withDate(DAY)));
        assertEquals(15, scorer.dateScore(lost, withDate(DAY.minusDays(1)))); // 1 day tolerance
        assertEquals(15, scorer.dateScore(lost, withDate(DAY.plusDays(1))));
        assertEquals(12, scorer.dateScore(lost, withDate(DAY.plusDays(3))));
        assertEquals(9, scorer.dateScore(lost, withDate(DAY.plusDays(7))));
        assertEquals(6, scorer.dateScore(lost, withDate(DAY.plusDays(14))));
        assertEquals(3, scorer.dateScore(lost, withDate(DAY.plusDays(30))));
        assertEquals(0, scorer.dateScore(lost, withDate(DAY.plusDays(31))));
        assertEquals(0, scorer.dateScore(lost, withDate(DAY.minusDays(5)))); // found before it was lost
    }

    @Test
    void locationDetailsGivePartialCredit() {
        Item lost = item(ItemType.LOST, Category.BAG, Colour.BLACK, CampusLocation.ACADEMIC_BLOCK, DAY, "Bag", "Black bag");
        Item found = item(ItemType.FOUND, Category.BAG, Colour.BLACK, CampusLocation.COMPUTER_LAB, DAY, "Bag", "Black bag");
        assertEquals(0, scorer.locationScore(lost, found));
        lost.setLocationDetails("Room 304, third floor");
        found.setLocationDetails("Third floor lab corridor");
        assertEquals(10, scorer.locationScore(lost, found));
        found.setLocation(CampusLocation.ACADEMIC_BLOCK);
        assertEquals(25, scorer.locationScore(lost, found));
    }

    @Test
    void realisticPairIsAStrongMatch() {
        Item lost = item(ItemType.LOST, Category.EARPHONES, Colour.WHITE, CampusLocation.LIBRARY, DAY,
                "boAt Airdopes earbuds", "White boAt Airdopes 141 in a charging case, small scratch on the lid");
        Item found = item(ItemType.FOUND, Category.EARPHONES, Colour.WHITE, CampusLocation.LIBRARY, DAY.plusDays(1),
                "White earbuds case", "Found boAt earbuds case on reading table");
        Match m = scorer.score(lost, found);
        assertTrue(m.getScore() >= 75, "expected a high score but was " + m.getScore());
    }

    private static Item withDate(LocalDate date) {
        return item(ItemType.FOUND, Category.BAG, Colour.BLACK, CampusLocation.LIBRARY, date, "Bag", "Black bag");
    }
}
