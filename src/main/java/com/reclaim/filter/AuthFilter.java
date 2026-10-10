package com.reclaim.filter;

import com.reclaim.util.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

/**
 * Session-based access control. Every request passes through this filter;
 * only the public pages below are reachable without logging in. Guests who
 * open a protected page are sent to the login page and brought back to that
 * page after a successful login.
 */
public class AuthFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    private static final Set<String> PUBLIC_PATHS = Set.of("/", "/login", "/register", "/favicon.ico");
    private static final String PUBLIC_PREFIX = "/assets/";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.isEmpty()) path = "/";

        boolean isPublic = PUBLIC_PATHS.contains(path) || path.startsWith(PUBLIC_PREFIX);
        if (isPublic || WebUtil.currentUser(request) != null) {
            // Logged-in pages must not be served from the browser cache after logout.
            if (!isPublic) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }
            chain.doFilter(request, response);
            return;
        }

        // Remember where the guest wanted to go (GET only), then ask them to log in.
        if ("GET".equals(request.getMethod())) {
            String target = path + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            request.getSession().setAttribute(WebUtil.SESSION_REDIRECT_AFTER_LOGIN, target);
        }
        WebUtil.flash(request, "info", "Please log in to continue.");
        WebUtil.redirect(request, response, "/login");
    }
}
