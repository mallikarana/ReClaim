package com.reclaim.controller;

import com.reclaim.exception.ValidationException;
import com.reclaim.model.User;
import com.reclaim.service.UserService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Module 1 - login.
 *
 * On success a NEW session is created (the old one is invalidated, which
 * prevents "session fixation" attacks) and the User bean is stored in it.
 * The optional "Remember my email" box stores only the email in a cookie,
 * never the password.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String EMAIL_COOKIE = "reclaim_email";
    private static final int COOKIE_DAYS = 30;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (WebUtil.currentUser(request) != null) {
            WebUtil.redirect(request, response, "/dashboard");
            return;
        }
        // Pre-fill the email: just registered > remembered cookie.
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("registeredEmail");
        session.removeAttribute("registeredEmail");
        boolean remembered = false;
        if (email == null) {
            email = readEmailCookie(request);
            remembered = email != null;
        }
        request.setAttribute("email", email);
        request.setAttribute("remembered", remembered);
        WebUtil.render(request, response, "auth/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        try {
            User user = userService.authenticate(email, request.getParameter("password"));

            // Keep the page the user originally wanted, then start a fresh session.
            HttpSession oldSession = request.getSession(false);
            String target = null;
            if (oldSession != null) {
                target = (String) oldSession.getAttribute(WebUtil.SESSION_REDIRECT_AFTER_LOGIN);
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute(WebUtil.SESSION_USER, user);

            rememberEmail(request, response, user.getEmail(), request.getParameter("rememberEmail") != null);

            WebUtil.flash(request, "success", "Welcome back, " + user.getFirstName() + "!");
            WebUtil.redirect(request, response, isSafeTarget(target) ? target : "/dashboard");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getFieldErrors());
            request.setAttribute("email", email);
            request.setAttribute("remembered", request.getParameter("rememberEmail") != null);
            WebUtil.render(request, response, "auth/login");
        }
    }

    /** Only allow redirects to paths inside this application (no open redirect to other sites). */
    private static boolean isSafeTarget(String target) {
        return target != null && target.startsWith("/") && !target.startsWith("//") && !target.contains("\\")
                && !target.startsWith("/login") && !target.startsWith("/logout");
    }

    private void rememberEmail(HttpServletRequest request, HttpServletResponse response, String email, boolean remember) {
        Cookie cookie = new Cookie(EMAIL_COOKIE, remember ? URLEncoder.encode(email, StandardCharsets.UTF_8) : "");
        cookie.setHttpOnly(true);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setMaxAge(remember ? COOKIE_DAYS * 24 * 60 * 60 : 0); // 0 deletes the cookie
        response.addCookie(cookie);
    }

    private String readEmailCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (EMAIL_COOKIE.equals(cookie.getName()) && !cookie.getValue().isBlank()) {
                return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
