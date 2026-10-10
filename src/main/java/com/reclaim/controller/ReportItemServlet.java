package com.reclaim.controller;

import com.reclaim.exception.ValidationException;
import com.reclaim.model.Item;
import com.reclaim.model.ItemType;
import com.reclaim.model.User;
import com.reclaim.service.ItemService;
import com.reclaim.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Module 2 - report a LOST or FOUND item.
 * GET /report?type=lost|found shows the form; POST saves the report.
 *
 * {@code @MultipartConfig} enables file uploads (the optional photo). The
 * container limit (10 MB) is a safety net; the real 5 MB rule is checked in
 * ImageStorageService so the user gets a friendly message.
 */
@WebServlet("/report")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10 * 1024 * 1024, maxRequestSize = 12 * 1024 * 1024)
public class ReportItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ItemType type = ItemType.fromString(request.getParameter("type"));
        request.setAttribute("type", type == null ? ItemType.LOST : type);
        request.setAttribute("today", LocalDate.now().toString());
        request.setAttribute("minDate", LocalDate.now().minusDays(ItemService.MAX_AGE_DAYS).toString());
        WebUtil.render(request, response, "items/report");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(request);
        try {
            Part image = request.getPart("image");
            Item item = itemService.createReport(user.getId(),
                    request.getParameter("type"),
                    request.getParameter("title"),
                    request.getParameter("category"),
                    request.getParameter("description"),
                    request.getParameter("colour"),
                    request.getParameter("location"),
                    request.getParameter("locationDetails"),
                    request.getParameter("incidentDate"),
                    image);
            WebUtil.flash(request, "success", "Your " + item.getType().getLabel().toLowerCase()
                    + " item report was submitted. We've checked it for potential matches below.");
            WebUtil.redirect(request, response, "/item?id=" + item.getId());
        } catch (ValidationException e) {
            ItemType type = ItemType.fromString(request.getParameter("type"));
            request.setAttribute("type", type == null ? ItemType.LOST : type);
            request.setAttribute("today", LocalDate.now().toString());
            request.setAttribute("minDate", LocalDate.now().minusDays(ItemService.MAX_AGE_DAYS).toString());
            request.setAttribute("errors", e.getFieldErrors());
            request.setAttribute("form", WebUtil.formValues(request, "title", "category", "description",
                    "colour", "location", "locationDetails", "incidentDate"));
            WebUtil.render(request, response, "items/report");
        }
    }
}
