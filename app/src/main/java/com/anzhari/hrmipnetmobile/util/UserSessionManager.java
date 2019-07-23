package com.anzhari.hrmipnetmobile.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREFER_NAME = UserSessionManager.class.getSimpleName();
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String TOKEN = "token";

    @SuppressLint("CommitPrefEdits")
    public UserSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setToken(String token){
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public void setIsUserLogin(boolean status){
        editor.putBoolean(IS_USER_LOGIN, status);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, null);
    }

    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

}
