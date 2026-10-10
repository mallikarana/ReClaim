package com.reclaim.controller;

import com.reclaim.service.ImageStorageService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Streams an item photo from the upload folder (Java I/O).
 * URL: /uploads/{file-name}. Only logged-in users get here (AuthFilter).
 */
@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ImageStorageService imageStorage = new ImageStorageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        Path file = pathInfo == null ? null : imageStorage.resolve(pathInfo.substring(1));
        if (file == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(ImageStorageService.contentTypeFor(file.getFileName().toString()));
        response.setContentLengthLong(Files.size(file));
        response.setHeader("Cache-Control", "private, max-age=86400");
        Files.copy(file, response.getOutputStream());
    }
}
