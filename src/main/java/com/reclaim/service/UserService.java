package com.reclaim.service;

import com.reclaim.dao.UserDAO;
import com.reclaim.exception.DataAccessException;
import com.reclaim.exception.ValidationException;
import com.reclaim.model.User;
import com.reclaim.model.UserType;
import com.reclaim.util.PasswordUtil;
import com.reclaim.util.ValidationUtil;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Business logic for Module 1: registration, login and profile management.
 * Controllers call this class; this class calls the DAO. It never touches
 * HTTP objects, which keeps it easy to test and explain.
 */
public class UserService {

    /** Used to keep login timing similar whether or not the email exists. */
    private static final String DUMMY_HASH = PasswordUtil.hash("timing-equaliser-1");

    private final UserDAO userDAO;

    public UserService() {
        this(new UserDAO());
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Validates the registration form and creates the account.
     * @return the newly created user (without password hash)
     */
    public User register(String fullName, String email, String password, String confirmPassword,
                         String userType, String universityId, String department, String phone)
            throws ValidationException {

        fullName = ValidationUtil.clean(fullName);
        email = normaliseEmail(email);
        universityId = ValidationUtil.clean(universityId);
        department = ValidationUtil.clean(department);
        phone = ValidationUtil.clean(phone);
        UserType type = UserType.fromString(userType);

        Map<String, String> errors = new LinkedHashMap<>();
        validateProfileFields(errors, fullName, type, universityId, department, phone);

        if (!ValidationUtil.isValidEmail(email)) {
            errors.put("email", "Enter a valid email address.");
        }
        String passwordProblem = ValidationUtil.checkPasswordStrength(password);
        if (passwordProblem != null) {
            errors.put("password", passwordProblem);
        } else if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match.");
        }
        if (!errors.containsKey("email") && userDAO.emailExists(email)) {
            errors.put("email", "An account with this email already exists. Try logging in.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setUserType(type);
        user.setUniversityId(universityId);
        user.setDepartment(department);
        user.setPhone(phone);
        try {
            userDAO.create(user);
        } catch (DataAccessException e) {
            // Two people registering the same email at the same instant: the UNIQUE key catches it.
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new ValidationException("email", "An account with this email already exists.");
            }
            throw e;
        }
        user.setPasswordHash(null);
        return user;
    }

    /**
     * Checks the credentials. Uses one generic error message so attackers
     * cannot discover which emails are registered.
     */
    public User authenticate(String email, String password) throws ValidationException {
        email = normaliseEmail(email);
        if (email == null || ValidationUtil.isBlank(password)) {
            throw new ValidationException("login", "Please enter your email and password.");
        }
        User user = userDAO.findByEmail(email);
        if (user == null) {
            PasswordUtil.verify(password, DUMMY_HASH);
            throw new ValidationException("login", "Invalid email or password.");
        }
        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            throw new ValidationException("login", "Invalid email or password.");
        }
        userDAO.updateLastLogin(user.getId());
        user.setPasswordHash(null); // the hash never goes into the session
        return user;
    }

    public User findById(int id) {
        User user = userDAO.findById(id);
        if (user != null) user.setPasswordHash(null);
        return user;
    }

    /** Updates name, type, university id, department and phone. Email stays fixed. */
    public User updateProfile(int userId, String fullName, String userType, String universityId,
                              String department, String phone) throws ValidationException {
        fullName = ValidationUtil.clean(fullName);
        universityId = ValidationUtil.clean(universityId);
        department = ValidationUtil.clean(department);
        phone = ValidationUtil.clean(phone);
        UserType type = UserType.fromString(userType);

        Map<String, String> errors = new LinkedHashMap<>();
        validateProfileFields(errors, fullName, type, universityId, department, phone);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            throw new ValidationException("profile", "Your account could not be found.");
        }
        user.setFullName(fullName);
        user.setUserType(type);
        user.setUniversityId(universityId);
        user.setDepartment(department);
        user.setPhone(phone);
        userDAO.updateProfile(user);
        user.setPasswordHash(null);
        return user;
    }

    public void changePassword(int userId, String currentPassword, String newPassword, String confirmPassword)
            throws ValidationException {
        User user = userDAO.findById(userId);
        if (user == null || !PasswordUtil.verify(currentPassword, user.getPasswordHash())) {
            throw new ValidationException("currentPassword", "Your current password is incorrect.");
        }
        String problem = ValidationUtil.checkPasswordStrength(newPassword);
        if (problem != null) {
            throw new ValidationException("newPassword", problem);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new ValidationException("confirmNewPassword", "Passwords do not match.");
        }
        if (newPassword.equals(currentPassword)) {
            throw new ValidationException("newPassword", "New password must be different from the current one.");
        }
        userDAO.updatePassword(userId, PasswordUtil.hash(newPassword));
    }

    private void validateProfileFields(Map<String, String> errors, String fullName, UserType type,
                                       String universityId, String department, String phone) {
        if (!ValidationUtil.isValidName(fullName)) {
            errors.put("fullName", "Enter your full name (2-100 letters).");
        }
        if (type == null) {
            errors.put("userType", "Select whether you are a student, faculty or staff member.");
        }
        if (universityId != null && !ValidationUtil.isValidUniversityId(universityId)) {
            errors.put("universityId", "University ID may contain only letters, digits, '/' or '-' (3-20 characters).");
        }
        if (department != null && department.length() > 100) {
            errors.put("department", "Department must be at most 100 characters.");
        }
        if (phone != null && !ValidationUtil.isValidPhone(phone)) {
            errors.put("phone", "Enter a valid 10-digit mobile number.");
        }
    }

    private static String normaliseEmail(String email) {
        String cleaned = ValidationUtil.clean(email);
        return cleaned == null ? null : cleaned.toLowerCase(Locale.ROOT);
    }
}
