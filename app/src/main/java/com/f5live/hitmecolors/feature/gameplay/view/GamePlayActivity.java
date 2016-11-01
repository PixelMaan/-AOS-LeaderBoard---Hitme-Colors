package com.f5live.hitmecolors.feature.gameplay.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.common.util.MediaUtil;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.databinding.AActivityGamePlayBinding;
import com.f5live.hitmecolors.feature.gameplay.presenter.PositionListener;
import com.f5live.hitmecolors.gamehelper.BaseGameActivity;
import com.google.android.gms.games.Games;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class GamePlayActivity extends BaseGameActivity implements GamePlayView, PositionListener {

    private int mSelectAt;
    private MediaPlayer mPlayer;
    private MediaPlayer mBackgroundPlayer;
    private int mScore = 0;
    private AActivityGamePlayBinding mRootView;
    private CountDownTimer mTimer;
    private DialogOverGame mOverGame;
    boolean isSoundOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_game_play);
        isSoundOff = PreUtil.getBoolean(Constant.SOUND_OFF, false);

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

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    private void onHitColor(int position) {
        if (position == mSelectAt) {
            mScore++;
            this.mRootView.gamePlayTvScore.setText(String.valueOf(mScore));
            mPlayer = MediaUtil.create(this, R.raw.point);
            if (!isSoundOff && mPlayer != null) {
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
        if (!isSoundOff && mPlayer != null) {
            mPlayer.start();
        }
        mRootView.gamePlayTvScore.setText(String.valueOf(mScore));

        // set type face
        mRootView.gamePlayTvScore.setTypeface(FontUtil.getFontType(this));
        mRootView.gamePlayTvTime.setTypeface(FontUtil.getFontType(this));

        this.playBackgroundSound();
    }

    private void gameOver() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        mPlayer = MediaUtil.create(GamePlayActivity.this, R.raw.failed);
        if (!isSoundOff && mPlayer != null) {
            mPlayer.start();
        }

        if (this.mOverGame != null && this.mOverGame.isShowing()) {
            return;
        }
//        this.mOverGame = new DialogOverGame(this);
//        this.mOverGame.setOnCancel(this::loadGame);
//        this.mOverGame.setOnDiscard(() -> this.goHome(this.mOverGame));
//        if (!isFinishing()) {
//            this.mOverGame.show();
//        }
        this.stopBackgroundSound();

        // get current achievements
        this.checkAchievement();

        // post current score to leader board
        this.postCurrentScores();
    }

    private void goHome(DialogOverGame overGame) {
        if (overGame != null && overGame.isShowing()) {
            overGame.dismiss();
        }
        onBackPressed();
    }


    private void playBackgroundSound() {
        this.mBackgroundPlayer = MediaUtil.create(this, R.raw.background);
        if (!isSoundOff && mBackgroundPlayer != null) {
            mBackgroundPlayer.start();
        }
    }

    private void stopBackgroundSound() {
        if (mBackgroundPlayer != null) {
            this.mBackgroundPlayer.stop();
        }
    }

    private void checkAchievement() {
        int score = PreUtil.getInt(Constant.SCORE);
        if (score >= 50) {
            Games.Achievements.unlock(getGameHelper().getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level5));
            Games.setViewForPopups(getGameHelper().getApiClient(), mRootView.getRoot());
            return;
        }

        if (score >= 20) {
            Games.Achievements.unlock(getGameHelper().getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level4));
            Games.setViewForPopups(getGameHelper().getApiClient(), mRootView.getRoot());
            return;
        }

        if (score >= 15) {
            Games.Achievements.unlock(getGameHelper().getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level3));
            Games.setViewForPopups(getGameHelper().getApiClient(), mRootView.getRoot());
            return;
        }

        if (score >= 10) {
            Games.Achievements.unlock(getGameHelper().getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level2));
            Games.setViewForPopups(getGameHelper().getApiClient(), mRootView.getRoot());
            return;
        }

        if (score >= 5) {
            Games.Achievements.unlock(getGameHelper().getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level1));
            Games.setViewForPopups(getGameHelper().getApiClient()
                    , mRootView.getRoot());
        }
    }

    private void postCurrentScores() {
        Games.Leaderboards.submitScore(getGameHelper().getApiClient()
                , String.valueOf(R.string.leader_board_id), PreUtil.getInt(Constant.SCORE));
    }

    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
    }
}