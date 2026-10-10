package com.reclaim.model;

import java.io.Serializable;

/**
 * JavaBean describing a potential match between one LOST and one FOUND report.
 *
 * The total score (0-100) is the sum of five transparent component scores,
 * each capped at its weight - see {@link com.reclaim.service.MatchScorer}.
 * Matches are computed on demand, so they always reflect the current reports.
 */
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    private Item lostItem;
    private Item foundItem;

    private int categoryScore;
    private int colourScore;
    private int locationScore;
    private int dateScore;
    private int descriptionScore;

    public Match() {
    }

    public Match(Item lostItem, Item foundItem) {
        this.lostItem = lostItem;
        this.foundItem = foundItem;
    }

    public Item getLostItem() {
        return lostItem;
    }

    public void setLostItem(Item lostItem) {
        this.lostItem = lostItem;
    }

    public Item getFoundItem() {
        return foundItem;
    }

    public void setFoundItem(Item foundItem) {
        this.foundItem = foundItem;
    }

    /** Given one side of the match, returns the report on the other side. */
    public Item getCounterpart(Item mine) {
        return mine != null && lostItem != null && mine.getId() == lostItem.getId() ? foundItem : lostItem;
    }

    public int getCategoryScore() {
        return categoryScore;
    }

    public void setCategoryScore(int categoryScore) {
        this.categoryScore = categoryScore;
    }

    public int getColourScore() {
        return colourScore;
    }

    public void setColourScore(int colourScore) {
        this.colourScore = colourScore;
    }

    public int getLocationScore() {
        return locationScore;
    }

    public void setLocationScore(int locationScore) {
        this.locationScore = locationScore;
    }

    public int getDateScore() {
        return dateScore;
    }

    public void setDateScore(int dateScore) {
        this.dateScore = dateScore;
    }

    public int getDescriptionScore() {
        return descriptionScore;
    }

    public void setDescriptionScore(int descriptionScore) {
        this.descriptionScore = descriptionScore;
    }

    /** Total match score out of 100. */
    public int getScore() {
        return categoryScore + colourScore + locationScore + dateScore + descriptionScore;
    }

    /** "High" / "Medium" / "Low" confidence label used for the coloured badge. */
    public String getConfidence() {
        int score = getScore();
        if (score >= 75) return "High";
        if (score >= 55) return "Medium";
        return "Low";
    }
}
