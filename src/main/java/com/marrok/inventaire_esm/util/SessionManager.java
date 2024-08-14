package com.marrok.inventaire_esm.util;

public class SessionManager {
    private static int activeUserId;

    public static int getActiveUserId() {
        return activeUserId;
    }

    public static void setActiveUserId(int userId) {
        activeUserId = userId;
    }
}
