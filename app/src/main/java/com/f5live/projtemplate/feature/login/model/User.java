package com.f5live.projtemplate.feature.login.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */
public class User implements Parcelable {
    private String mId;

    private String mName;

    private String mEmail;

    // TODO - Add more in the bellow


    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mEmail);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.mId = in.readString();
        this.mName = in.readString();
        this.mEmail = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
