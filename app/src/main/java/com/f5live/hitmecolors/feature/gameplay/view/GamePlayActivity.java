package com.f5live.hitmecolors.feature.gameplay.view;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.base.BaseActivity;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.MediaUtil;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.common.view.DialogOverGame;
import com.f5live.hitmecolors.databinding.AActivityGamePlayBinding;
import com.f5live.hitmecolors.feature.gameplay.presenter.PositionListener;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class GamePlayActivity extends BaseActivity implements GamePlayView, PositionListener {

    private int mSelectAt;
    private MediaPlayer mPlayer;
    private MediaPlayer mBackgroundPlayer;
    private int mScore = 0;
    private AActivityGamePlayBinding mRootView;
    private CountDownTimer mTimer;
    private DialogOverGame mOverGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_game_play);

        this.loadGame();

        this.mRootView.gamePlayGrv.setOnItemClickListener((parent, v, position, id)
                -> this.onHitColor(position));
    }

    public void fetchAdapter() {
        ImageAdapter adapter = new ImageAdapter(this, this);
        this.mRootView.gamePlayGrv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPosition(int differentPosition) {
        mSelectAt = differentPosition;
    }

    public void countTime() {
        mTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                mRootView.gamePlayTvTime.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                gameOver();
            }
        };
        mTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }

        if (mBackgroundPlayer != null) {
            mBackgroundPlayer.stop();
            mBackgroundPlayer = null;
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }

        if (mBackgroundPlayer != null) {
            mBackgroundPlayer.stop();
            mBackgroundPlayer = null;
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }

        if (mBackgroundPlayer != null) {
            mBackgroundPlayer.stop();
            mBackgroundPlayer = null;
        }

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void onHitColor(int position) {
        if (position == mSelectAt) {
            mScore++;
            this.mRootView.gamePlayTvScore.setText(String.valueOf(mScore));
            mPlayer = MediaUtil.create(this, R.raw.point);
            if (mPlayer != null) {
                mPlayer.start();
            }
            PreUtil.putInt(Constant.SCORE, this.mScore);
            fetchAdapter();
            mTimer.cancel();
            countTime();
        } else {
            mTimer.onFinish();
        }
    }

    private void loadGame() {
        fetchAdapter();
        PreUtil.putInt(Constant.SCORE, 0);
        mScore = 0;
        countTime();
        mPlayer = MediaUtil.create(
                GamePlayActivity.this, R.raw.pop);
        if (mPlayer != null) {
            mPlayer.start();
        }
        mRootView.gamePlayTvScore.setText(String.valueOf(mScore));
        this.loadBackgroundPlayer();
    }

    private void gameOver() {
        this.mTimer.cancel();
        mPlayer = MediaUtil.create(GamePlayActivity.this, R.raw.failed);
        if (mPlayer != null) {
            mPlayer.start();
        }

        if (this.mOverGame != null && this.mOverGame.isShowing()) {
            return;
        }
        this.mOverGame = new DialogOverGame(this);
        this.mOverGame.setOnCancel(this::loadGame);
        this.mOverGame.setOnDiscard(() -> this.goHome(this.mOverGame));
        if (!isFinishing()) {
            this.mOverGame.show();
        }
        this.mBackgroundPlayer.stop();
    }

    private void goHome(DialogOverGame overGame) {
        if (overGame != null && overGame.isShowing()) {
            overGame.dismiss();
        }
        onBackPressed();
    }

    private void loadBackgroundPlayer() {
        this.mBackgroundPlayer = MediaUtil.create(
                GamePlayActivity.this, R.raw.background);
        if (mBackgroundPlayer != null) {
            mBackgroundPlayer.start();
        }
    }
}