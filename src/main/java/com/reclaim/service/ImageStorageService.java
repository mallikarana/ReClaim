package com.reclaim.service;

import com.reclaim.exception.ValidationException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Saves and loads item photos using Java I/O.
 *
 * Photos are stored OUTSIDE the deployed web application folder, so they
 * survive redeployments. Security measures:
 *  - the file type is detected from the file's first bytes ("magic numbers"),
 *    not trusted from the browser-supplied name or content type;
 *  - files are renamed to a random UUID, so users can't overwrite each other's
 *    files or use path tricks like "../../";
 *  - maximum size of 5 MB.
 */
public class ImageStorageService {

    private static final Logger LOG = Logger.getLogger(ImageStorageService.class.getName());

    public static final long MAX_BYTES = 5L * 1024 * 1024;
    private static final Pattern SAFE_NAME = Pattern.compile("^[a-f0-9\\-]{36}\\.(jpg|png|gif|webp)$");

    private static volatile Path uploadDir;

    /** Called once at startup by AppContextListener. */
    public static void initialise(ServletContext ctx) {
        String configured = ctx.getInitParameter("reclaim.uploadDir");
        Path dir;
        if (configured != null && !configured.isBlank()) {
            dir = Paths.get(configured);
        } else {
            String base = System.getProperty("catalina.base", System.getProperty("user.dir"));
            dir = Paths.get(base, "reclaim-uploads");
        }
        try {
            Files.createDirectories(dir);
            uploadDir = dir.toAbsolutePath().normalize();
            LOG.info("Item photos are stored in " + uploadDir);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create upload directory " + dir, e);
        }
    }

    /**
     * Validates and stores an uploaded photo.
     * @return the stored file name, or null when no file was chosen
     */
    public String store(Part part) throws ValidationException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > MAX_BYTES) {
            throw new ValidationException("image", "The photo must be smaller than 5 MB.");
        }
        if (uploadDir == null) {
            throw new ValidationException("image", "Photo upload is currently unavailable. Submit the report without a photo.");
        }
        byte[] data;
        try (InputStream in = part.getInputStream()) {
            data = in.readAllBytes();
        } catch (IOException e) {
            throw new ValidationException("image", "The photo could not be read. Please try again.");
        }
        String extension = detectImageType(data);
        if (extension == null) {
            throw new ValidationException("image", "Only JPG, PNG, GIF or WEBP images are allowed.");
        }
        String fileName = UUID.randomUUID() + "." + extension;
        try {
            Files.write(uploadDir.resolve(fileName), data);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not save uploaded photo", e);
            throw new ValidationException("image", "The photo could not be saved. Please try again.");
        }
        return fileName;
    }

    /** Returns the file for a stored photo, or null if the name is invalid or the file is missing. */
    public Path resolve(String fileName) {
        if (uploadDir == null || fileName == null || !SAFE_NAME.matcher(fileName).matches()) {
            return null;
        }
        Path file = uploadDir.resolve(fileName).normalize();
        return file.startsWith(uploadDir) && Files.isRegularFile(file) ? file : null;
    }

    /** Removes a stored photo (used when saving the report fails afterwards). */
    public void delete(String fileName) {
        Path file = resolve(fileName);
        if (file != null) {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Could not delete photo " + fileName, e);
            }
        }
    }

    public static String contentTypeFor(String fileName) {
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".webp")) return "image/webp";
        return "image/jpeg";
    }

    /** Identifies the image format from its first bytes; null if it is not a supported image. */
    static String detectImageType(byte[] d) {
        if (d.length >= 3 && (d[0] & 0xFF) == 0xFF && (d[1] & 0xFF) == 0xD8 && (d[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        if (d.length >= 8 && (d[0] & 0xFF) == 0x89 && d[1] == 'P' && d[2] == 'N' && d[3] == 'G') {
            return "png";
        }
        if (d.length >= 6 && d[0] == 'G' && d[1] == 'I' && d[2] == 'F' && d[3] == '8') {
            return "gif";
        }
        if (d.length >= 12 && d[0] == 'R' && d[1] == 'I' && d[2] == 'F' && d[3] == 'F'
                && d[8] == 'W' && d[9] == 'E' && d[10] == 'B' && d[11] == 'P') {
            return "webp";
        }
        return null;
    }
}
