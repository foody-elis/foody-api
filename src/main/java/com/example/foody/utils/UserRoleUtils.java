package com.example.foody.utils;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;

/**
 * Utility class for user role-related operations.
 */
public class UserRoleUtils {

    /**
     * Checks if the user has the ADMIN role.
     *
     * @param user the user to check
     * @return true if the user is an admin, false otherwise
     */
    public static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    /**
     * Checks if the user has the MODERATOR role.
     *
     * @param user the user to check
     * @return true if the user is a moderator, false otherwise
     */
    public static boolean isModerator(User user) {
        return user.getRole().equals(Role.MODERATOR);
    }

    /**
     * Checks if the user has the RESTAURATEUR role.
     *
     * @param user the user to check
     * @return true if the user is a restaurateur, false otherwise
     */
    public static boolean isRestaurateur(User user) {
        return user.getRole().equals(Role.RESTAURATEUR);
    }

    /**
     * Checks if the user has the COOK role.
     *
     * @param user the user to check
     * @return true if the user is a cook, false otherwise
     */
    public static boolean isCook(User user) {
        return user.getRole().equals(Role.COOK);
    }

    /**
     * Checks if the user has the WAITER role.
     *
     * @param user the user to check
     * @return true if the user is a waiter, false otherwise
     */
    public static boolean isWaiter(User user) {
        return user.getRole().equals(Role.WAITER);
    }

    /**
     * Checks if the user has the CUSTOMER role.
     *
     * @param user the user to check
     * @return true if the user is a customer, false otherwise
     */
    public static boolean isCustomer(User user) {
        return user.getRole().equals(Role.CUSTOMER);
    }

    /**
     * Checks if the user is an employee (either a cook or a waiter).
     *
     * @param user the user to check
     * @return true if the user is an employee, false otherwise
     */
    public static boolean isEmployee(User user) {
        return isCook(user) || isWaiter(user);
    }

    /**
     * Checks if the user is a buyer (either a customer or a waiter).
     *
     * @param user the user to check
     * @return true if the user is a buyer, false otherwise
     */
    public static boolean isBuyer(User user) {
        return isCustomer(user) || isWaiter(user);
    }
}