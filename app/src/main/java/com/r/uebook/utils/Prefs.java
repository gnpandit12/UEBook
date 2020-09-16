package com.r.uebook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.r.uebook.BaseApplication;

import static com.r.uebook.utils.ApplicationConstants.APP_NAME;


public class Prefs {


    public static void setPreferences(String key, String value) {
        Context context = BaseApplication.getInstance();
        SharedPreferences.Editor editor = context.getSharedPreferences(
                APP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreferences(String key) {
        Context context = BaseApplication.getInstance();
        SharedPreferences prefs = context.getSharedPreferences(APP_NAME,
                Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static boolean getBoolean(String key, boolean default_value) {
        Context context = BaseApplication.getInstance();
        SharedPreferences prefs = context.getSharedPreferences(APP_NAME,
                Context.MODE_PRIVATE);
        return prefs.getBoolean(key, default_value);

    }

    public static void setBoolean(String key, boolean value) {
        Context context = BaseApplication.getInstance();
        SharedPreferences.Editor editor = context.getSharedPreferences(
                APP_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static void clearPreferences() {
        Context context = BaseApplication.getInstance();
        SharedPreferences.Editor editor = context.getSharedPreferences(
                APP_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
