package com.reclaim.util;

import com.reclaim.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Helpers shared by all controllers: session access, view forwarding,
 * flash messages and the CSRF token.
 */
public final class WebUtil {

    /** Session attribute holding the logged-in {@link User}. */
    public static final String SESSION_USER = "authUser";
    /** Session attribute holding the per-session CSRF token. */
    public static final String SESSION_CSRF = "csrfToken";
    /** Session attribute holding a one-time message shown on the next page. */
    public static final String SESSION_FLASH = "flash";
    /** Session attribute remembering the page a guest tried to open before logging in. */
    public static final String SESSION_REDIRECT_AFTER_LOGIN = "redirectAfterLogin";

    private static final String VIEW_PREFIX = "/WEB-INF/views/";
    private static final String VIEW_SUFFIX = ".jsp";
    private static final SecureRandom RANDOM = new SecureRandom();

    private WebUtil() {
    }

    /** The logged-in user, or null for guests. Never creates a session. */
    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute(SESSION_USER);
    }

    /**
     * Forwards to a JSP inside WEB-INF/views (e.g. "auth/login").
     * JSPs live under WEB-INF so browsers can only reach them through a controller.
     */
    public static void render(HttpServletRequest request, HttpServletResponse response, String view)
            throws ServletException, IOException {
        request.getRequestDispatcher(VIEW_PREFIX + view + VIEW_SUFFIX).forward(request, response);
    }

    /** Redirects to a path inside this web application (e.g. "/dashboard"). */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    /** Stores a message that is displayed once on the next rendered page (Post/Redirect/Get pattern). */
    public static void flash(HttpServletRequest request, String type, String message) {
        Map<String, String> flash = new HashMap<>();
        flash.put("type", type);       // success | error | info
        flash.put("message", message);
        request.getSession().setAttribute(SESSION_FLASH, flash);
    }

    /** Returns the session's CSRF token, generating one on first use. */
    public static String csrfToken(HttpSession session) {
        String token = (String) session.getAttribute(SESSION_CSRF);
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(SESSION_CSRF, token);
        }
        return token;
    }

    /** Parses a positive int request parameter; returns -1 when missing or invalid. */
    public static int intParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null) return -1;
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** Copies the submitted parameters into a map so a form can be re-filled after a validation error. */
    public static Map<String, String> formValues(HttpServletRequest request, String... names) {
        Map<String, String> values = new HashMap<>();
        for (String name : names) {
            String value = request.getParameter(name);
            if (value != null) values.put(name, value);
        }
        return values;
    }
}
