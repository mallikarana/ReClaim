package com.reclaim.controller;

import com.reclaim.exception.ValidationException;
import com.reclaim.model.User;
import com.reclaim.service.UserService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Module 1 - shows the registration form (GET) and creates the account (POST). */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (WebUtil.currentUser(request) != null) {
            WebUtil.redirect(request, response, "/dashboard");
            return;
        }
        WebUtil.render(request, response, "auth/register");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = userService.register(
                    request.getParameter("fullName"),
                    request.getParameter("email"),
                    request.getParameter("password"),
                    request.getParameter("confirmPassword"),
                    request.getParameter("userType"),
                    request.getParameter("universityId"),
                    request.getParameter("department"),
                    request.getParameter("phone"));

            // Post/Redirect/Get: redirecting prevents a duplicate account on browser refresh.
            request.getSession().setAttribute("registeredEmail", user.getEmail());
            WebUtil.flash(request, "success",
                    "Account created for " + user.getFirstName() + ". Please log in to continue.");
            WebUtil.redirect(request, response, "/login");
        } catch (ValidationException e) {
            request.setAttribute("errors", e.getFieldErrors());
            request.setAttribute("form", WebUtil.formValues(request,
                    "fullName", "email", "userType", "universityId", "department", "phone"));
            WebUtil.render(request, response, "auth/register");
        }
    }
}
