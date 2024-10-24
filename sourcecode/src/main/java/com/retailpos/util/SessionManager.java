package com.retailpos.util;
public class SessionManager {

    private static String currentUser;
    private static String currentUserRole;
    private static long sessionStartTime;
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;  // 30 minutes

    public static void login(String username, String role) {
        currentUser = username;
        currentUserRole = role;
        sessionStartTime = System.currentTimeMillis();
    }

//    public static boolean isSessionValid() {
//        long currentTime = System.currentTimeMillis();
//        return currentTime - sessionStartTime < SESSION_TIMEOUT;
//    }
public static boolean isSessionValid() {
    // Implement session validity checks if necessary
    return currentUser != null;
}
    public static void logout() {
        currentUser = null;
        currentUserRole = null;
        sessionStartTime=0;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }
}
