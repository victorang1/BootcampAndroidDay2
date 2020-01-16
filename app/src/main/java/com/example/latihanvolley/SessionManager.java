package com.example.latihanvolley;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "CacheBootcamp2";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USERNAME = "username";

    private static final int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        if(checkLogin()) {
            return true;
        }
        return false;
    }

    private boolean checkLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
