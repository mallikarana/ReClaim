package com.reclaim.listener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.reclaim.dao.DBConnection;
import com.reclaim.model.CampusLocation;
import com.reclaim.model.Category;
import com.reclaim.model.Colour;
import com.reclaim.model.ItemStatus;
import com.reclaim.model.UserType;
import com.reclaim.service.ImageStorageService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs once when Tomcat starts / stops the application.
 *  - start: verifies the database connection, prepares the image folder and
 *           publishes the dropdown lists (categories, colours, ...) to all JSPs;
 *  - stop : unregisters the JDBC driver so Tomcat can unload the app cleanly.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();

        // Shared, read-only lists used by the forms and filters (applicationScope in JSP).
        ctx.setAttribute("categories", Collections.unmodifiableList(java.util.List.of(Category.values())));
        ctx.setAttribute("colours", Collections.unmodifiableList(java.util.List.of(Colour.values())));
        ctx.setAttribute("locations", Collections.unmodifiableList(java.util.List.of(CampusLocation.values())));
        ctx.setAttribute("statuses", Collections.unmodifiableList(java.util.List.of(ItemStatus.values())));
        ctx.setAttribute("userTypes", Collections.unmodifiableList(java.util.List.of(UserType.values())));

        ImageStorageService.initialise(ctx);

        try (Connection ignored = DBConnection.getConnection()) {
            LOG.info("ReClaim started - database connection OK (" + DBConnection.describe() + ")");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ReClaim started but the DATABASE IS NOT REACHABLE (" + DBConnection.describe()
                    + "). Is MySQL running and is db.properties correct? " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ClassLoader appLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == appLoader) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, "Could not deregister JDBC driver", e);
                }
            }
        }
        AbandonedConnectionCleanupThread.checkedShutdown();
        LOG.info("ReClaim stopped");
    }
}
