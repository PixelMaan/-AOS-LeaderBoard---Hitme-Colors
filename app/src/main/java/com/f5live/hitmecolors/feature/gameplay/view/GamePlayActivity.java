package com.f5live.hitmecolors.feature.gameplay.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
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
    private int mScore = 0;
    private AActivityGamePlayBinding mRootView;
    private CountDownTimer mTimer;
    private DialogOverGame mOverGame;
    boolean isSoundOff;

    private SoundPool soundPool;
    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;
    private int correctSound;
    private int wrongSound;
    private int startSound;
    private float volume;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_game_play);
        this.isSoundOff = PreUtil.getBoolean(Constant.SOUND_OFF, false);

        this.initViews();
        this.initSoundPool();
        this.loadGame();
    }

    private void initViews() {
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
        if (mTimer != null) {
            mTimer.start();
            return;
        }

        mTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                mRootView.gamePlayTvTime.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mRootView.gamePlayTvTime.setText("0");
                gameOver();
            }
        };
        mTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        this.stopBackgroundSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        this.destroyBackgroundSound();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        this.stopBackgroundSound();
    }


    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {
    }

    private void onHitColor(int position) {
        this.countTime();
        if (position == mSelectAt) {
            mScore++;
            this.mRootView.gamePlayTvScore.setText(String.valueOf(mScore));
            this.playSoundEffect(this.correctSound);
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
        this.playSoundEffect(this.startSound);
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
        this.playSoundEffect(this.wrongSound);

        if (this.mOverGame != null && this.mOverGame.isShowing()) {
            return;
        }
        this.mOverGame = new DialogOverGame(this);
        this.mOverGame.setOnCancel(this::loadGame);
        this.mOverGame.setOnDiscard(() -> this.goHome(this.mOverGame));
        if (!isFinishing()) {
            this.mOverGame.show();
        }
        this.stopBackgroundSound();

        // get current achievements
        //this.checkAchievement();

        // post current score to leader board
        //this.postCurrentScores();
    }

    private void goHome(DialogOverGame overGame) {
        if (overGame != null && overGame.isShowing()) {
            overGame.dismiss();
        }
        onBackPressed();
    }


    private void checkAchievement() {
        int score = PreUtil.getInt(Constant.SCORE);
        if (score < PreUtil.getBest()) {
            return;
        }
        if (score >= 50) {
            Games.Achievements.unlock(getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level5));
            return;
        }

        if (score >= 20) {
            Games.Achievements.unlock(getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level4));
            return;
        }

        if (score >= 15) {
            Games.Achievements.unlock(getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level3));
            return;
        }

        if (score >= 10) {
            Games.Achievements.unlock(getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level2));
            return;
        }

        if (score >= 5) {
            Games.Achievements.unlock(getApiClient()
                    , String.valueOf(R.string.achievement_hit_me_colors_level1));
        }
    }

    private void postCurrentScores() {
        Games.Leaderboards.submitScore(getApiClient()
                , String.valueOf(R.string.leaderboard_hit_me__colors), PreUtil.getInt(Constant.SCORE));
    }


    @Override
    protected void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                100);
    }


    private void initSoundPool() {
        // AudioManager audio settings for adjusting the volume
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex = (float) audioManager.getStreamMaxVolume(streamType);
        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;
        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            loaded = true;
            playBackgroundSound();
        });

        this.correctSound = this.soundPool.load(this, R.raw.point, 1);
        this.wrongSound = this.soundPool.load(this, R.raw.failed, 1);
        this.startSound = this.soundPool.load(this, R.raw.pop, 1);
    }

    private void playBackgroundSound() {
        mp = MediaPlayer.create(getApplicationContext(), R.raw.game_play);
        mp.start();
        mp.setLooping(true);
    }

    private void stopBackgroundSound() {
        if (mp != null) {
            mp.stop();
        }
    }

    private void destroyBackgroundSound() {
        if (mp != null) {
            mp.release();
        }
    }

    private void playSoundEffect(int soundId) {
        if (loaded) {
            float leftVolumn = volume;
            float rightVolumn = volume;
            this.soundPool.play(soundId, leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
}