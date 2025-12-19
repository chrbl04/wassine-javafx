package util;

import model.User;

public class Session {
    private static User currentUser;

    public static void login(User u) {
        currentUser = u;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        return currentUser == null ? -1 : currentUser.getUserId();
    }
    public static void clear() {
        currentUser = null;
    }

    public static void logout() {
        currentUser = null;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
