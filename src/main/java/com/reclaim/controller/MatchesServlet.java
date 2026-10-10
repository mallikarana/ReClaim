package com.reclaim.controller;

import com.reclaim.model.User;
import com.reclaim.service.MatchScorer;
import com.reclaim.service.MatchingService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Module 3 - all potential matches for the user's open reports, with the score breakdown. */
@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MatchingService matchingService = new MatchingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(request);
        request.setAttribute("matches", matchingService.findMatchesForUser(user.getId()));
        request.setAttribute("minScore", MatchScorer.MIN_SCORE);
        WebUtil.render(request, response, "items/matches");
    }
}
