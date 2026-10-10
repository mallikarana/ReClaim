package com.reclaim.controller;

import com.reclaim.model.Item;
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

/**
 * Shows one report. The owner additionally sees the potential matches for it
 * and can change its status; other users only see the public details.
 */
@WebServlet("/item")
public class ItemDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ItemService itemService = new ItemService();
    private final MatchingService matchingService = new MatchingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebUtil.intParam(request, "id");
        Item item = id > 0 ? itemService.findById(id) : null;
        if (item == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        User user = WebUtil.currentUser(request);
        boolean owner = item.getUserId() == user.getId();
        request.setAttribute("item", item);
        request.setAttribute("owner", owner);
        if (owner) {
            request.setAttribute("matches", matchingService.findMatchesFor(item));
        }
        WebUtil.render(request, response, "items/detail");
    }
}
