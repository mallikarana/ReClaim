package com.reclaim.controller;

import com.reclaim.exception.ValidationException;
import com.reclaim.model.User;
import com.reclaim.service.ItemService;
import com.reclaim.service.UserService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Module 1 - user profile. GET shows the profile; POST either updates the
 * profile details (action=profile) or changes the password (action=password).
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        show(request, response, WebUtil.currentUser(request));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User current = WebUtil.currentUser(request);
        String action = request.getParameter("action");
        try {
            if ("password".equals(action)) {
                userService.changePassword(current.getId(),
                        request.getParameter("currentPassword"),
                        request.getParameter("newPassword"),
                        request.getParameter("confirmNewPassword"));
                WebUtil.flash(request, "success", "Your password has been changed.");
            } else {
                User updated = userService.updateProfile(current.getId(),
                        request.getParameter("fullName"),
                        request.getParameter("userType"),
                        request.getParameter("universityId"),
                        request.getParameter("department"),
                        request.getParameter("phone"));
                // Refresh the copy kept in the session so the header shows the new name.
                request.getSession().setAttribute(WebUtil.SESSION_USER, updated);
                WebUtil.flash(request, "success", "Your profile has been updated.");
            }
            WebUtil.redirect(request, response, "/profile");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getFieldErrors());
            if (!"password".equals(action)) {
                request.setAttribute("form", WebUtil.formValues(request,
                        "fullName", "userType", "universityId", "department", "phone"));
                request.setAttribute("editOpen", true);
            } else {
                request.setAttribute("passwordOpen", true);
            }
            show(request, response, current);
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response, User sessionUser)
            throws ServletException, IOException {
        User user = userService.findById(sessionUser.getId());
        if (user == null) {
            // The account was removed while logged in: end the session.
            request.getSession().invalidate();
            WebUtil.redirect(request, response, "/login");
            return;
        }
        Map<String, Integer> counts = itemService.countByUser(user.getId());
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        request.setAttribute("profile", user);
        request.setAttribute("reportCount", total);
        request.setAttribute("lostCount", counts.getOrDefault("LOST_OPEN", 0)
                + counts.getOrDefault("LOST_RESOLVED", 0) + counts.getOrDefault("LOST_CLOSED", 0));
        request.setAttribute("foundCount", counts.getOrDefault("FOUND_OPEN", 0)
                + counts.getOrDefault("FOUND_RESOLVED", 0) + counts.getOrDefault("FOUND_CLOSED", 0));
        WebUtil.render(request, response, "profile");
    }
}
