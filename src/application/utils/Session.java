package application.utils;

import application.models.utilisateurs;

public class Session {
    private static utilisateurs currentUser;

    public static void setCurrentUser(utilisateurs user) {
        currentUser = user;
    }

    public static utilisateurs getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }

    public static void clearSession() {
        currentUser = null;
    }
}
