package com.reclaim.controller;

import com.reclaim.util.WebUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Module 1 - logout. Accepts POST only (with CSRF token) so that a link or
 * image on another site cannot silently log the user out.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // destroys the session and everything stored in it
        }
        WebUtil.flash(request, "success", "You have been logged out safely.");
        WebUtil.redirect(request, response, "/login");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebUtil.redirect(request, response, "/dashboard");
    }
}
