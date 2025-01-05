package com.example.foody.utils;

import com.example.foody.model.user.User;
import com.example.foody.utils.enums.Role;

public class UserRoleUtils {

    public static boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }

    public static boolean isModerator(User user) {
        return user.getRole().equals(Role.MODERATOR);
    }

    public static boolean isRestaurateur(User user) {
        return user.getRole().equals(Role.RESTAURATEUR);
    }

    public static boolean isCook(User user) {
        return user.getRole().equals(Role.COOK);
    }

    public static boolean isWaiter(User user) {
        return user.getRole().equals(Role.WAITER);
    }

    public static boolean isCustomer(User user) {
        return user.getRole().equals(Role.CUSTOMER);
    }

    public static boolean isEmployee(User user) {
        return isCook(user) || isWaiter(user);
    }

    public static boolean isBuyer(User user) {
        return isCustomer(user) || isWaiter(user);
    }
}