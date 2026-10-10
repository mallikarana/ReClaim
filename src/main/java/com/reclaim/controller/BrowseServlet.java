package com.reclaim.controller;

import com.reclaim.dao.ItemDAO;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Category;
import com.reclaim.model.Item;
import com.reclaim.model.ItemFilter;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.ItemType;
import com.reclaim.service.ItemService;
import com.reclaim.util.ValidationUtil;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Module 3 - browse and search all reports.
 * Filters come from the query string (GET), so a search can be bookmarked or shared.
 */
@WebServlet("/items")
public class BrowseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ItemFilter filter = new ItemFilter();
        filter.setType(ItemType.fromString(request.getParameter("type")));
        filter.setCategory(Category.fromString(request.getParameter("category")));
        filter.setLocation(CampusLocation.fromString(request.getParameter("location")));
        String status = request.getParameter("status");
        // Default view = OPEN reports; "ALL" removes the status filter.
        filter.setStatus("ALL".equalsIgnoreCase(status) ? null
                : status == null || status.isBlank() ? ItemStatus.OPEN : ItemStatus.fromString(status));
        String keyword = ValidationUtil.clean(request.getParameter("q"));
        if (keyword != null && keyword.length() > 100) keyword = keyword.substring(0, 100);
        filter.setKeyword(keyword);

        List<Item> items = itemService.browse(filter);
        request.setAttribute("filter", filter);
        request.setAttribute("statusParam", filter.getStatus() == null ? "ALL" : filter.getStatus().name());
        request.setAttribute("items", items);
        request.setAttribute("limitReached", items.size() >= ItemDAO.BROWSE_LIMIT);
        WebUtil.render(request, response, "items/browse");
    }
}
