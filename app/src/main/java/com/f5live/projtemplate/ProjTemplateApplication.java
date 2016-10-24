package com.f5live.projtemplate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f5live.projtemplate.api.HttpsTrustManager;
import com.f5live.projtemplate.api.RestAPI;
import com.f5live.projtemplate.feature.login.view.LoginActivity;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class ProjTemplateApplication extends Application{

    private static ProjTemplateApplication mApp;

    SharedPreferences sharedPreferences;

    public static ProjTemplateApplication getAppIntance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        HttpsTrustManager.allowAllSSL();
        RestAPI.init(getApplicationContext());
        RestAPI.setClazzInvalidToken(LoginActivity.class);
    }

    public void setSharedPreferences(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
}
