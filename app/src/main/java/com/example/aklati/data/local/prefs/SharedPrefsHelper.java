package com.example.aklati.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_NAME = "userName";
    static final String PREF_NAME = "AklatiPrefs";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static SharedPrefsHelper instance;
    private final SharedPreferences sharedPreferences;

    // Singleton Pattern
    private SharedPrefsHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context.getApplicationContext());
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public void saveUserData(String name, String email) {
        sharedPreferences.edit()
                .putString(KEY_USER_NAME, name)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    public String getData(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}