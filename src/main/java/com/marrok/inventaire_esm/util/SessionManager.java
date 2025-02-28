package com.marrok.inventaire_esm.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionManager {
    private static final Logger logger = LogManager.getLogger(SessionManager.class);
    private static int activeUserId;

    public static int getActiveUserId() {
        logger.info("getActiveUserId()");
        return activeUserId;
    }

    public static void setActiveUserId(int userId) {
        logger.info("setActiveUserId() with userId: " + userId);
        activeUserId = userId;
    }
}
