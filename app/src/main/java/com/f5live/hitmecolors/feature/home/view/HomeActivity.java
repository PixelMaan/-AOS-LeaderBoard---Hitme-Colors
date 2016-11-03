package com.f5live.hitmecolors.feature.home.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.api.RestAPI;
import com.f5live.hitmecolors.common.util.Constant;
import com.f5live.hitmecolors.common.util.FontUtil;
import com.f5live.hitmecolors.common.util.MediaUtil;
import com.f5live.hitmecolors.common.util.PermissionUtil;
import com.f5live.hitmecolors.common.util.PreUtil;
import com.f5live.hitmecolors.common.view.ShakeDetector;
import com.f5live.hitmecolors.databinding.AActivityHomeBinding;
import com.f5live.hitmecolors.feature.gameplay.view.GamePlayActivity;
import com.f5live.hitmecolors.gamehelper.BaseGameActivity;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;

import org.json.JSONObject;

public class HomeActivity extends BaseGameActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private static final int REQUEST_ACHIEVEMENTS = 9000;
    private static final int REQUEST_LEADER_BOARD = 9001;
    private AActivityHomeBinding mRootView;
    // Maximum sound stream.
    private static final int MAX_STREAMS = 5;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private ShakeDetector mShakeDetector;
    private boolean mBackGroupPlayed;
    private int backgroundSoundId;
    private Sensor mAccelerometer;
    private SoundPool soundPool;
    private int soundBackground;
    private boolean loaded;
    private float volume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Init main content views
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_home);
        this.initViews();
        this.initSensor();
        this.initSoundPool();
        this.requestPermissions();

        // calling the login from google play game
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

        this.mRootView.homeBtnMoreGame.setOnClickListener(view
                -> this.postScoreToLeaderBoard());

        this.mRootView.homeBtnStore.setOnClickListener(view
                -> this.unlockAchievement());

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


    /**
     * Initializing the sensor and detecting the shake listener
     */
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


    /**
     * Get all achievements from this game has create on Google Console
     */
    private void onShowAchievements() {
        // it has been connected when user has logged in
        if (getApiClient().isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    REQUEST_ACHIEVEMENTS);
        } else {
            // re-call login
            beginUserInitiatedSignIn();
        }
    }


    /**
     * Load the leader board game.
     * List all gamer are ranking on the Board
     */
    private void onShowLeaderBoard() {
        // it has been connected when user has logged in
        if (getApiClient().isConnected() && isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leaderboard_hit_me__colors)),
                    REQUEST_LEADER_BOARD);
        } else {
            // re-call login
            beginUserInitiatedSignIn();
        }
    }


    /**
     * Initializing media player to play the sound
     */
    private void playShakeSound() {
        MediaPlayer shakeSound = MediaUtil.create(this, R.raw.shake_sound);
        if (shakeSound == null) return;
        if (shakeSound.isPlaying()) return;
        shakeSound.start();
    }


    /**
     * Initializing Sound Pool
     */
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

        // Load sound file (destroy.wav) into SoundPool.
        this.soundBackground = this.soundPool.load(this, R.raw.background_music, 1);

        // Load sound file (gun.wav) into SoundPool.
        int soundShake = this.soundPool.load(this, R.raw.shake_sound, 1);

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            loaded = true;
            playBackground();
        });

    }


    /**
     * Loading and playing the background music for game.
     */
    public void playBackground() {
        if (loaded && !mBackGroupPlayed) {
            float leftVolumn = volume;
            float rightVolumn = volume;
            // Play sound of gunfire. Returns the ID of the new stream.
            backgroundSoundId = this.soundPool.play(this.soundBackground, leftVolumn, rightVolumn, 1, -1, 1f);
            mBackGroupPlayed = true;
        }
    }


    /**
     * Stopping background sound when user pause/stop/deytroy this game.
     */
    public void stopSound() {
        if (this.soundPool == null) return;
        this.soundPool.stop(backgroundSoundId);
    }


    /**
     * The method using to unlock the new achievement.
     * If the new achievement has been not unlocked, will be unlock and show popup to notice unlock successful.
     * Else the achievement has been unlocked, will not unlock again and will not show the notice popup.
     */
    private void unlockAchievement() {
        if (!getApiClient().isConnected() || !isSignedIn()) {
            return;
        }
        // unlock the target achievement
        Games.Achievements.unlock(getApiClient(), "CgkI2KW2374LEAIQAQ");

        // Display popup notice has been unlocked successful the achievement.
        //Games.setViewForPopups(getApiClient(), mRootView.getRoot());
    }


    /**
     * The method using to post user score to Leader Board
     */
    private void postScoreToLeaderBoard() {
        if (!getApiClient().isConnected() || !isSignedIn()) {
            return;
        }
        // Post score
//        Games.Leaderboards.submitScoreImmediate(getApiClient()
//                , String.valueOf(R.string.leaderboard_hit_me__colors), 1000);
        Games.Leaderboards.submitScoreImmediate(getApiClient()
                , "CgkI2KW2374LEAIQAw", 1000);
    }


    /**
     * Handling the reset all achievements status for this user.
     */
    public void resetAchievements() {
        if (isSignedIn()) {
            String accountName = "";//getGameHelper().getCurrentAccountName();
            String scopes = "";//getScopes();

            new ResetTask(this, accountName, scopes).execute((Void) null);
        }
    }


    /**
     * Using the AsyncTask to call the API service.
     * Handle the expect result and do the business logic.
     */
    private class ResetTask extends AsyncTask<Void, Void, Void>
            implements RestAPI.RequestFail, RestAPI.ResponseSuccess {
        String mAccountName;
        String mScope;
        Context mContext;

        ResetTask(Context con, String name, String sc) {
            mContext = con;
            mAccountName = name;
            mScope = sc;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String accesstoken = GoogleAuthUtil.getToken(mContext, mAccountName, mScope);
                String API_RESET = "https://www.googleapis.com" +
                        "/games/v1management" +
                        "/achievements" +
                        "/reset?access_token=" + accesstoken;
                RestAPI.Post(null, API_RESET, this, this);
            } catch (Exception ignored) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO
        }

        @Override
        public void onRequestFail(String error) {
            // TODO
        }

        @Override
        public void onResponseSucess(JSONObject json) {
            // TODO
        }
    }


    private void requestPermissions() {
        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                PermissionUtil.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (!PermissionUtil.isPermissionGranted(this, Manifest.permission.WRITE_CONTACTS)) {
            PermissionUtil.requestPermission(this, Manifest.permission.WRITE_CONTACTS);
        }
    }

}
