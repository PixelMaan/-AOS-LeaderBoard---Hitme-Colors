package com.f5live.hitmecolors;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f5live.hitmecolors.api.HttpsTrustManager;
import com.f5live.hitmecolors.api.RestAPI;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class HitMeColorsApplication extends Application{

    private static HitMeColorsApplication mApp;

    SharedPreferences sharedPreferences;

    public static HitMeColorsApplication getAppIntance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        HttpsTrustManager.allowAllSSL();
        RestAPI.init(getApplicationContext());
    }

    public void setSharedPreferences(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
