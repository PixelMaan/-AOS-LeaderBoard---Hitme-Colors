package com.f5live.hitmecolors.feature.home.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.common.util.MediaUtil;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.databinding.AActivityHomeBinding;
import com.f5live.hitmecolors.feature.gameplay.view.GamePlayActivity;
import com.f5live.hitmecolors.gamehelper.BaseGameActivity;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;

public class HomeActivity extends BaseGameActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private AActivityHomeBinding mRootView;
    private static final int REQUEST_ACHIEVEMENTS = 9000;
    private static final int REQUEST_LEADER_BOARD = 9001;
    private MediaPlayer mBackgroundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_home);
        this.initViews();
        beginUserInitiatedSignIn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stopBackgroundSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopBackgroundSound();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.stopBackgroundSound();
    }

    private void initViews() {
        this.mRootView.homeTitle.setTypeface(FontUtil.getFontType(this));

        this.mRootView.homeBtnSound.setOnClickListener(view
                -> this.checkSoundOnOff(false));

        this.mRootView.homeBtnLeaderBoard.setOnClickListener(view
                -> this.onShowLeaderBoard());

        this.mRootView.homeBtnStart.setOnClickListener(view
                -> {
            this.startActivity(new Intent(this, GamePlayActivity.class));
            stopBackgroundSound();
        });

        this.mRootView.homeBtnAchievement.setOnClickListener(view
                -> this.onShowAchievements());

        this.checkSoundOnOff(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if (responseCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
            getApiClient().disconnect();
        }
    }


    private void handleSound(boolean isOn) {
        if (isOn) {
            this.playBackgroundSound();
        } else {
            this.stopBackgroundSound();
        }
    }

    private void playBackgroundSound() {
        this.mBackgroundPlayer = MediaUtil.create(this, R.raw.background);
        if (mBackgroundPlayer != null) {
            mBackgroundPlayer.start();
        }

    }

    private void stopBackgroundSound() {
        if (mBackgroundPlayer != null) {
            this.mBackgroundPlayer.release();
            this.mBackgroundPlayer = null;
        }
    }

    private void checkSoundOnOff(boolean isLoadingGame) {
        boolean isSoundOff = PreUtil.getBoolean(Constant.SOUND_OFF, false);
        if (isSoundOff) {
            this.mRootView.homeBtnSound.setBackgroundResource(R.drawable.btn_sound_on);
        } else {
            this.mRootView.homeBtnSound.setBackgroundResource(R.drawable.btn_sound_off);
        }
        PreUtil.putBoolean(Constant.SOUND_OFF, !isSoundOff);
        if (isLoadingGame) {
            this.handleSound(isSoundOff);
            return;
        }
        this.handleSound(!isSoundOff);
    }


    @Override
    public void onSignInFailed() {
        Log.d(TAG, "onSignInFailed() called.");
    }

    @Override
    public void onSignInSucceeded() {
        Log.d(TAG, "onSignInSucceeded() called.");
    }

    private void onShowAchievements() {
        if (getApiClient().isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    REQUEST_ACHIEVEMENTS);
        } else {
            beginUserInitiatedSignIn();
        }
    }

    private void onShowLeaderBoard() {
        if (getApiClient().isConnected() && isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leader_board_id)),
                    REQUEST_LEADER_BOARD);
        } else {
            beginUserInitiatedSignIn();
        }
    }
}
