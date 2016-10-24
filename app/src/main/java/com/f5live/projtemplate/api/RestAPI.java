package com.f5live.projtemplate.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class RestAPI<T> {

    public interface RequestFail {
        void onRequestFail(String error);
    }

    public interface ResponseSuccess {
        void onResponseSucess(JSONObject json);
    }

    private static final int ERROR_INVALID_TOKEN = 401;
    private static final String RESPONSE_SUCESS = "success";
    private static final int REQUEST_SUCCESS = 200;
    private static RequestQueue requestQueue;
    private static Map<String, String> _sHeader = new HashMap<>();
    private static Class _sClazz;

    @SuppressWarnings("AccessStaticViaInstance")
    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        HttpsTrustManager.intHttpsTrustManager().allowAllSSL();
    }

    public static void setClazzInvalidToken(Class clazz) {
        _sClazz = clazz;
    }

    public static void setHeader(Map<String, String> header) {
        _sHeader = header;
    }

    public static void setHeader(String key, String value) {
        _sHeader.put(key, value);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private static Response.ErrorListener errVolley(RequestFail fail) {
        Response.ErrorListener err = error -> {
            if (fail != null)
                fail.onRequestFail("Cause the problems with your network. Please try again later!");//response message error
        };
        return err;
    }

    private static DefaultRetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(
            (int) TimeUnit.MINUTES.toMillis(2), //timeout 2minutes
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public static void Get(String url, ResponseSuccess success, RequestFail fail) {
        Get(null, url, null, success, fail);
    }

    public static void Get(String url, String parameter, ResponseSuccess success, RequestFail fail) {
        Get(null, url, parameter, success, fail);
    }

    public static synchronized void Get(Context context, String url, ResponseSuccess success, RequestFail fail) {
        Get(context, url, null, success, fail);
    }


    public static synchronized void Get(Context context, String url, String object, ResponseSuccess success, RequestFail fail) {
        Log.d("url", url);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                object,
                json -> {
                    Log.d("response", json.toString());
                    if (json.optInt("code") == ERROR_INVALID_TOKEN) {
                        invalidToken(context);
                    } else if (json.optInt("code") == REQUEST_SUCCESS) {
                        if (success != null) success.onResponseSucess(json);
                    } else {
                        if (fail != null) fail.onRequestFail(json.optString("message"));
                    }
                },
                errVolley(fail)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return _sHeader;
            }
        };

        request.setRetryPolicy(defaultRetryPolicy);

        requestQueue.add(request);
    }

    public static synchronized void Post(String url, String param, ResponseSuccess success, RequestFail fail) {
        Post(null, url, param, success, fail);
    }

    public static synchronized void Post(Context context, String url, String param, ResponseSuccess success, RequestFail fail) {
        Log.d("url", url);
        Log.d("param", param);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                param,
                json -> {
                    Log.d("response", json.toString());
                    if (json.optInt("code") == ERROR_INVALID_TOKEN && context != null) {
                        invalidToken(context);
                    } else if (json.optInt("code") == REQUEST_SUCCESS) {
                        if (success != null) success.onResponseSucess(json);
                    } else {
                        if (fail != null) fail.onRequestFail(json.optString("message"));
                    }
                },
                errVolley(fail)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return _sHeader;
            }
        };
        request.setRetryPolicy(defaultRetryPolicy);
        requestQueue.add(request);
    }


    public static synchronized void Put(Context context, String url, String param, ResponseSuccess success, RequestFail fail) {
        Log.d("url", url);
        Log.d("param", param);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                param,
                json -> {
                    Log.d("response", json.toString());

                    if (json.optInt("code") == ERROR_INVALID_TOKEN) {
                        invalidToken(context);
                    } else if (json.optInt("code") == REQUEST_SUCCESS) {
                        if (success != null) success.onResponseSucess(json);
                    } else {
                        if (fail != null) fail.onRequestFail(json.optString("message"));
                    }
                },
                errVolley(fail)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return _sHeader;
            }
        };
        request.setRetryPolicy(defaultRetryPolicy);
        requestQueue.add(request);
    }

    private static void invalidToken(Context context) {
        Intent intent = new Intent(context, _sClazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
