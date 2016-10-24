package com.f5live.hitmecolors.feature.gameplay.presenter;

import android.content.Context;

import com.f5live.hitmecolors.feature.gameplay.view.GamePlayView;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class GamePlayPresenterImpl implements GamePlayPresenter {
    private Context mContext;
    private GamePlayView mView;

    public GamePlayPresenterImpl(Context context, GamePlayView view) {
        this.mContext = context;
        this.mView = view;
    }

    @Override
    public void onPlayGame() {

    }

    @Override
    public void onReplayGame() {

    }

    @Override
    public void onPauseGame() {

    }

    @Override
    public void onExitGame() {

    }

    @Override
    public void onResumeGame() {

    }
}
