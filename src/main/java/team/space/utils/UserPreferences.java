package team.space.utils;

import java.util.prefs.Preferences;

public class UserPreferences {
    private static final Preferences userPrefs = Preferences.userRoot().node("account");

    public static String getUsername() {
        return userPrefs.get("username", null);
    }

    public static void putUsername(String username) {
        userPrefs.put("username", username);
    }

    public static void removeUsername() {
        userPrefs.remove("username");
    }
}
