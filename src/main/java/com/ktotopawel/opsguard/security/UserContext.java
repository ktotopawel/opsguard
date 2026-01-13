package com.ktotopawel.opsguard.security;

public class UserContext {
    private static final ThreadLocal<CurrentUser> currentUser = new ThreadLocal<>();

    public static CurrentUser get() {
        return currentUser.get();
    }

    public static void set(Long userId) {
        CurrentUser user = new CurrentUser(userId);
        currentUser.set(user);
    }

    public static void clear() {
        currentUser.remove();
    }
}
