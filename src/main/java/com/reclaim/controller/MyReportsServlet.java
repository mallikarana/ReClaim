package com.reclaim.controller;

import com.reclaim.model.Item;
import com.reclaim.model.ItemType;
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

/** Module 2 - "My Reports": every report the logged-in user has created, with match counts. */
@WebServlet("/my-reports")
public class MyReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ItemService itemService = new ItemService();
    private final MatchingService matchingService = new MatchingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(request);
        List<Item> items = itemService.findByUser(user.getId());

        ItemType tab = ItemType.fromString(request.getParameter("type"));
        if (tab != null) {
            items.removeIf(item -> item.getType() != tab);
        }
        request.setAttribute("tab", tab == null ? "ALL" : tab.name());
        request.setAttribute("items", items);
        request.setAttribute("matchCounts", matchingService.matchCountsByItem(user.getId()));
        WebUtil.render(request, response, "items/my-reports");
    }
}
