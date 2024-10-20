package com.example.foody.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(Constants.ROLE_ADMIN),
    MODERATOR(Constants.ROLE_MODERATOR),
    RESTAURATEUR(Constants.ROLE_RESTAURATEUR),
    COOK(Constants.ROLE_COOK),
    WAITER(Constants.ROLE_WAITER),
    CUSTOMER(Constants.ROLE_CUSTOMER);

    private final String role;

    public static class Constants {
        public static final String ROLE_PREFIX = "ROLE_";

        public static final String ADMIN_VALUE = "ADMIN";
        public static final String MODERATOR_VALUE = "MODERATOR";
        public static final String RESTAURATEUR_VALUE = "RESTAURATEUR";
        public static final String COOK_VALUE = "COOK";
        public static final String WAITER_VALUE = "WAITER";
        public static final String CUSTOMER_VALUE = "CUSTOMER";

        public static final String ROLE_ADMIN = ROLE_PREFIX + ADMIN_VALUE;
        public static final String ROLE_MODERATOR = ROLE_PREFIX + MODERATOR_VALUE;
        public static final String ROLE_RESTAURATEUR = ROLE_PREFIX + RESTAURATEUR_VALUE;
        public static final String ROLE_COOK = ROLE_PREFIX + COOK_VALUE;
        public static final String ROLE_WAITER = ROLE_PREFIX + WAITER_VALUE;
        public static final String ROLE_CUSTOMER = ROLE_PREFIX + CUSTOMER_VALUE;
    }
}
