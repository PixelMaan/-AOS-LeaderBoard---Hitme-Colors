package com.f5live.hitmecolors.feature.home.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.common.util.MediaUtil;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.common.view.ShakeDetector;
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

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private SoundPool soundPool;
    private AudioManager audioManager;
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;
    private int soundBackground;
    private int soundShake;
    private float volume;
    private int backgroudSoundId;
    private boolean mBackGroupPlayed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_home);
        this.initViews();
        this.initSensor();
        this.initSoundPool();
        beginUserInitiatedSignIn();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
        this.stopSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.playBackground();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopSound();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.stopSound();
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
            this.stopSound();
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
            this.playBackground();
        } else {
            this.stopSound();
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


    private void initSensor() {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        Animation animShake = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.shake_anim);
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                mRootView.homeBtnStart.startAnimation(animShake);
                playShakeSound();
            }

            @Override
            public void onStopShake() {
                animShake.cancel();
            }
        });

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

    private void playShakeSound() {
        MediaPlayer shakeSound = MediaUtil.create(this, R.raw.shake_sound);
        if (shakeSound == null) return;
        if (shakeSound.isPlaying()) return;
        shakeSound.start();
    }


    private void initSoundPool() {
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
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

        // Load sound file (destroy.wav) into SoundPool.
        this.soundBackground = this.soundPool.load(this, R.raw.background, 1);

        // Load sound file (gun.wav) into SoundPool.
        this.soundShake = this.soundPool.load(this, R.raw.shake_sound, 1);

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            loaded = true;
            playBackground();
        });

    }


    public void playBackground() {
        if (loaded && !mBackGroupPlayed) {
            float leftVolumn = volume;
            float rightVolumn = volume;
            // Play sound of gunfire. Returns the ID of the new stream.
            backgroudSoundId = this.soundPool.play(this.soundBackground, leftVolumn, rightVolumn, 1, -1, 1f);
            mBackGroupPlayed = true;
        }
    }

    public void stopSound() {
        if (this.soundPool == null) return;
        this.soundPool.stop(backgroudSoundId);
    }
}
