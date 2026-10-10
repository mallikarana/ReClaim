package com.reclaim.service;

import com.reclaim.dao.ItemDAO;
import com.reclaim.model.Item;
import com.reclaim.model.ItemType;
import com.reclaim.model.Match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Module 3: finds potential matches between LOST and FOUND reports.
 *
 * Matches are calculated on demand from the current OPEN reports, so they are
 * always up to date (a resolved or closed report immediately stops matching).
 * Each candidate pair is scored by {@link MatchScorer}; pairs reported by the
 * same user are skipped.
 */
public class MatchingService {

    /** Highest score first; ties broken by the most recent report. */
    private static final Comparator<Match> BEST_FIRST =
            Comparator.comparingInt(Match::getScore).reversed()
                    .thenComparing(m -> Math.max(m.getLostItem().getId(), m.getFoundItem().getId()),
                            Comparator.reverseOrder());

    private final ItemDAO itemDAO;
    private final MatchScorer scorer;

    public MatchingService() {
        this(new ItemDAO(), new MatchScorer());
    }

    public MatchingService(ItemDAO itemDAO, MatchScorer scorer) {
        this.itemDAO = itemDAO;
        this.scorer = scorer;
    }

    /** Potential matches for a single report, best first. Empty if the report is not OPEN. */
    public List<Match> findMatchesFor(Item item) {
        if (item == null || !item.isOpen()) {
            return Collections.emptyList();
        }
        List<Item> candidates = itemDAO.findOpenByType(item.getType().opposite());
        return scoreAgainst(item, candidates);
    }

    /** Potential matches for all of a user's OPEN reports, best first. */
    public List<Match> findMatchesForUser(int userId) {
        List<Item> myOpenItems = new ArrayList<>();
        for (Item item : itemDAO.findByUser(userId)) {
            if (item.isOpen()) myOpenItems.add(item);
        }
        if (myOpenItems.isEmpty()) {
            return Collections.emptyList();
        }
        // Load each candidate pool once instead of once per report.
        Map<ItemType, List<Item>> pools = new HashMap<>();
        List<Match> all = new ArrayList<>();
        for (Item mine : myOpenItems) {
            ItemType wanted = mine.getType().opposite();
            List<Item> pool = pools.computeIfAbsent(wanted, itemDAO::findOpenByType);
            all.addAll(scoreAgainst(mine, pool));
        }
        all.sort(BEST_FIRST);
        return all;
    }

    /** Number of potential matches per report id, for badges on "My Reports". */
    public Map<Integer, Integer> matchCountsByItem(int userId) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Match match : findMatchesForUser(userId)) {
            Item mine = match.getLostItem().getUserId() == userId ? match.getLostItem() : match.getFoundItem();
            counts.merge(mine.getId(), 1, Integer::sum);
        }
        return counts;
    }

    private List<Match> scoreAgainst(Item item, List<Item> candidates) {
        List<Match> matches = new ArrayList<>();
        for (Item candidate : candidates) {
            if (candidate.getUserId() == item.getUserId() || candidate.getType() == item.getType()) {
                continue; // never match a user with themselves, nor lost-with-lost
            }
            Match match = scorer.score(item, candidate);
            if (scorer.isPotentialMatch(match)) {
                matches.add(match);
            }
        }
        matches.sort(BEST_FIRST);
        return matches;
    }
}
