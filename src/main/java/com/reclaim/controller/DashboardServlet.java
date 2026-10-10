package com.reclaim.controller;

import com.reclaim.model.Match;
import com.reclaim.model.User;
import com.reclaim.service.ItemService;
import com.reclaim.service.MatchingService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/** The logged-in home page: personal statistics, best matches and recent campus reports. */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int TOP_MATCHES = 3;
    private static final int RECENT_ITEMS = 6;

    private final ItemService itemService = new ItemService();
    private final MatchingService matchingService = new MatchingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(request);

        Map<String, Integer> mine = itemService.countByUser(user.getId());
        request.setAttribute("myLostOpen", mine.getOrDefault("LOST_OPEN", 0));
        request.setAttribute("myFoundOpen", mine.getOrDefault("FOUND_OPEN", 0));
        request.setAttribute("myResolved",
                mine.getOrDefault("LOST_RESOLVED", 0) + mine.getOrDefault("FOUND_RESOLVED", 0));

        List<Match> matches = matchingService.findMatchesForUser(user.getId());
        request.setAttribute("matchCount", matches.size());
        request.setAttribute("topMatches", matches.subList(0, Math.min(TOP_MATCHES, matches.size())));

        Map<String, Integer> campus = itemService.campusCounts();
        request.setAttribute("campusLost", campus.getOrDefault("LOST", 0));
        request.setAttribute("campusFound", campus.getOrDefault("FOUND", 0));
        request.setAttribute("campusResolved", campus.getOrDefault("RESOLVED", 0));

        request.setAttribute("recentItems", itemService.recentOpen(RECENT_ITEMS));
        WebUtil.render(request, response, "dashboard");
    }
}
