package com.f5live.hitmecolors.feature.gameplay.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.feature.gameplay.presenter.PositionListener;

import java.util.Random;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private final int SIZE_LV_1 = 4;
    private final int SIZE_LV_2 = 8;
    private final int SIZE_LV_3 = 16;
    private int mPosDifferent, mOriginColor, mDifferentColor;
    private PositionListener mListener;

    public ImageAdapter(Context c, PositionListener listener) {
        mContext = c;
        this.mListener = listener;
        if (PreUtil.getInt(Constant.SCORE) <= 5) {
            level1();
        } else if (PreUtil.getInt(Constant.SCORE) <= 10) {
            level2();
        } else {
            level3();
        }
    }

    public int getCount() {
        if (PreUtil.getInt(Constant.SCORE) <= 5) {
            return SIZE_LV_1;
        } else if (PreUtil.getInt(Constant.SCORE) <= 10) {
            return SIZE_LV_2;
        } else {
            return SIZE_LV_3;
        }
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = inflater.inflate(R.layout.v_item, null);

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.itemIv);

            if (position == mPosDifferent) {
                imageView.setBackgroundColor(mDifferentColor);
            } else {
                imageView.setBackgroundColor(mOriginColor);
            }

        } else {
            gridView = convertView;
        }

        return gridView;
    }

    public int getRandom(int size) {
        Random r = new Random();
        return r.nextInt(size - 1);
    }

    public void level1() {
        Random rnd = new Random();
        mOriginColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(200),
                rnd.nextInt(256));
        int alpha = 255, red = rnd.nextInt(256), green = rnd.nextInt(50), blue = rnd
                .nextInt(256);
        int dGreen = green - 10, dBlue = blue - 20;

        mOriginColor = Color.argb(alpha, red, green, blue);
        mDifferentColor = Color.argb(alpha, red, dGreen, dBlue);
        mPosDifferent = getRandom(SIZE_LV_1);
        mListener.onPosition(mPosDifferent);
    }

    public void level2() {
        Random rnd = new Random();
        mOriginColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(200),
                rnd.nextInt(256));
        int alpha = 255, red = rnd.nextInt(256), green = rnd.nextInt(200), blue = rnd
                .nextInt(200);
        int dGreen = green + 5, dBlue = blue + 10;

        mOriginColor = Color.argb(alpha, red, green, blue);
        mDifferentColor = Color.argb(alpha, red, dGreen, dBlue);
        mPosDifferent = getRandom(SIZE_LV_2);
        mListener.onPosition(mPosDifferent);
    }

    public void level3() {
        Random rnd = new Random();
        mOriginColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(200),
                rnd.nextInt(256));
        int alpha = 255, red = rnd.nextInt(256), green = rnd.nextInt(200), blue = rnd
                .nextInt(200);
        int dGreen = green + 5, dBlue = blue + 20;

        mOriginColor = Color.argb(alpha, red, green, blue);
        mDifferentColor = Color.argb(alpha, red, dGreen, dBlue);
        mPosDifferent = getRandom(SIZE_LV_3);
        mListener.onPosition(mPosDifferent);
    }

}