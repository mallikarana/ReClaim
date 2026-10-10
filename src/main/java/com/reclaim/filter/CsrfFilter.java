package com.reclaim.filter;

import com.reclaim.util.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * Protection against Cross-Site Request Forgery (CSRF).
 *
 * Every form includes a hidden "csrfToken" field whose value is a random
 * token stored in the user's session. A POST is accepted only when the two
 * match, so another website cannot submit forms on the user's behalf.
 */
public class CsrfFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;
    public static final String PARAM = "csrfToken";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request.getRequestURI().startsWith(request.getContextPath() + "/assets/")) {
            chain.doFilter(request, response); // static files: no session needed
            return;
        }

        HttpSession session = request.getSession(); // ensures every visitor has a token for their forms
        String expected = WebUtil.csrfToken(session);

        if (!"POST".equals(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String submitted;
        try {
            submitted = request.getParameter(PARAM);
        } catch (IllegalStateException e) {
            // Multipart request exceeded the size limits, so its fields could not be read.
            submitted = null;
            request.setAttribute("uploadTooLarge", Boolean.TRUE);
        }
        if (Boolean.TRUE.equals(request.getAttribute("uploadTooLarge")) || isMultipartFailed(request)) {
            WebUtil.flash(request, "error", "The uploaded file is too large. Please choose an image under 5 MB.");
            response.sendRedirect(request.getRequestURI()
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
            return;
        }

        if (submitted == null || !MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8), submitted.getBytes(StandardCharsets.UTF_8))) {
            WebUtil.flash(request, "error", "Your session expired or the form was out of date. Please try again.");
            response.sendRedirect(request.getRequestURI()
                    + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * Tomcat does not throw for oversized multipart bodies when using getParameter();
     * it silently returns no parameters. Detect that case by calling getParts().
     */
    private boolean isMultipartFailed(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("multipart/")) return false;
        try {
            request.getParts();
            return false;
        } catch (IllegalStateException | IOException | ServletException e) {
            return true;
        }
    }
}
