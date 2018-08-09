package com.user.etow.data.prefs;

/*
 *  Copyright Ⓒ 2018. All rights reserved
 *  Author DangTin. Create on 2018/05/13
 */

import android.content.Context;

import com.google.gson.Gson;
import com.user.etow.models.User;

public class DataStoreManager {

    public static final String PREF_FIRST_INSTALL_APP = "PREF_FIRST_INSTALL_APP";
    public static final String PREF_TOKEN_USER = "PREF_TOKEN_USER";
    public static final String PREF_IS_LOGIN = "PREF_IS_LOGIN";
    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";
    public static final String PREF_ID_TRIP_PROCESS = "PREF_ID_TRIP_PROCESS";

    private static DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    /**
     * Call when start application
     */
    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    // Check first install app
    public static void setFirstInstallApp(boolean status) {
        DataStoreManager.getInstance().sharedPreferences.putBooleanValue(PREF_FIRST_INSTALL_APP, status);
    }

    public static boolean getFirstInstallApp() {
        return DataStoreManager.getInstance().sharedPreferences.getBooleanValue(PREF_FIRST_INSTALL_APP);
    }

    // save token user
    public static void setUserToken(String accessToken) {
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_TOKEN_USER, accessToken);
    }

    public static String getUserToken() {
        return DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_TOKEN_USER, "");
    }

    // check user login
    public static void setIsLogin(boolean isLogin) {
        DataStoreManager.getInstance().sharedPreferences.putBooleanValue(PREF_IS_LOGIN, isLogin);
    }

    public static boolean getIsLogin() {
        return DataStoreManager.getInstance().sharedPreferences.getBooleanValue(PREF_IS_LOGIN);
    }

    // save user infor
    public static void setUser(User user) {
        if (user != null) {
            String jsonUser = user.toJSon();
            DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
        }
    }

    public static void removeUser() {
        User user = new User();
        String jsonUser = user.toJSon();
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        User user = new Gson().fromJson(jsonUser, User.class);
        return user;
    }

    // check trip process
    public static void setPrefIdTripProcess(int id) {
        DataStoreManager.getInstance().sharedPreferences.putIntValue(PREF_ID_TRIP_PROCESS, id);
    }

    public static int getPrefIdTripProcess() {
        return DataStoreManager.getInstance().sharedPreferences.getIntValue(PREF_ID_TRIP_PROCESS);
    }
}
