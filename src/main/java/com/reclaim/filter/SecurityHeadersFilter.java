package com.reclaim.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Adds standard browser security headers to every response. */
public class SecurityHeadersFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.setHeader("X-Content-Type-Options", "nosniff");   // don't guess content types
        response.setHeader("X-Frame-Options", "DENY");             // no clickjacking via iframes
        response.setHeader("Referrer-Policy", "same-origin");
        chain.doFilter(request, response);
    }
}
