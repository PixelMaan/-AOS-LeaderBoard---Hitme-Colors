package com.f5live.hitmecolors.common.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.f5live.hitmecolors.HitMeColorsApplication;
import com.google.gson.Gson;

import java.util.List;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class PreUtil {
    private static final String TAG = PreUtil.class.getCanonicalName();


    /**
     * Type Int
     */
    public static void putInt(String key, int value) {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        prefs.edit().putInt(key, value).apply();
        if (key.equalsIgnoreCase(Constant.SCORE) && value > getBest()) {
            setBest(value);
        }
    }

    public static int getBest() {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        return prefs.getInt(Constant.BEST_SCORE, 0);
    }

    private static void setBest(int score) {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constant.BEST_SCORE, score);
        editor.apply();
    }


    /**
     * Type Boolean
     */
    public static void putBoolean(String key, boolean value) {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        prefs.edit().putBoolean(key, value).apply();
    }

    /**
     * Type String
     */
    public static void putString(String key, String value) {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        prefs.edit().putString(key, value).apply();
    }

    /**
     * Type Object
     */
    public static void putObject(String key, Object value) {
        Log.d(TAG, "putObject: " + key);
        Log.d(TAG, "putObject: " + new Gson().toJson(value));
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        String strObject = new Gson().toJson(value);
        prefs.edit().putString(key, strObject).apply();
    }

    /**
     * Type List Object
     */
    public static void putListObject(String key, List<Object> value) {
        SharedPreferences prefs = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        String strObject = new Gson().toJson(value);
        prefs.edit().putString(key, strObject).apply();
    }

    /**
     * get .......
     */
    public static int getInt(String key) {
        return getInt(key, -1);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences pref = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        return pref.getInt(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences pref = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        return pref.getBoolean(key, defaultValue);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences pref = HitMeColorsApplication.getAppIntance().getSharedPreferences();
        return pref.getString(key, defaultValue);
    }
}
