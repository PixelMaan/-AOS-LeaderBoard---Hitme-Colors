package com.f5live.projtemplate.api;

import com.f5live.projtemplate.BuildConfig;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class APIUrl {
    private static final String SERVER_NAME = BuildConfig.SERVER_NAME;
    public static final String API_LOGIN = SERVER_NAME + "dashboard/login";
    public static final String API_SIGNUP = SERVER_NAME + "dashboard/signip";
    public static final String API_LOGOUT = SERVER_NAME + "dashboard/logout";
}
