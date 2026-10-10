package com.reclaim.controller;

import com.reclaim.exception.ValidationException;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.User;
import com.reclaim.service.ItemService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Module 2 - status tracking: the owner marks a report Resolved / Closed or re-opens it. */
@WebServlet("/item/status")
public class ItemStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemService itemService = new ItemService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = WebUtil.currentUser(request);
        int itemId = WebUtil.intParam(request, "id");
        String status = request.getParameter("status");
        try {
            itemService.updateStatus(itemId, user.getId(), status);
            ItemStatus newStatus = ItemStatus.fromString(status);
            String message = switch (newStatus) {
                case RESOLVED -> "Great news! The report is marked as resolved.";
                case CLOSED -> "The report has been closed and no longer appears in matches.";
                case OPEN -> "The report is open again and will be included in matching.";
            };
            WebUtil.flash(request, "success", message);
        } catch (ValidationException e) {
            WebUtil.flash(request, "error", e.getFieldErrors().values().iterator().next());
        }
        String back = request.getParameter("back");
        WebUtil.redirect(request, response, "my-reports".equals(back) ? "/my-reports" : "/item?id=" + itemId);
    }
}
