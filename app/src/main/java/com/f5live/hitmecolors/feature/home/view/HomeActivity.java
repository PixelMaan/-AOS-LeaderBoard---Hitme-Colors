package com.f5live.hitmecolors.feature.home.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.f5live.hitmecolors.R;
import com.f5live.hitmecolors.common.base.BaseActivity;
import com.f5live.hitmecolors.databinding.AActivityHomeBinding;
import com.f5live.hitmecolors.feature.home.presenter.HomeViewPersenterImpl;
import com.f5live.hitmecolors.feature.home.presenter.HomeViewPresenter;
import com.f5live.hitmecolors.gamehelper.BaseGameUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class HomeActivity extends BaseActivity implements HomeView
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private AActivityHomeBinding mRootView;
    private HomeViewPresenter mPresenter;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initGoogleApiClient();
        this.mRootView = DataBindingUtil.setContentView(this, R.layout.a_activity_home);
        this.mPresenter = new HomeViewPersenterImpl(this, this);
        this.initViews();
        this.initGoogleApiClient();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initViews() {
        this.mRootView.homeBtnLeaderBoard.setOnClickListener(view -> {
            Toast.makeText(this, "Is Enable: " + (this.mGoogleApiClient != null
                    && this.mGoogleApiClient.isConnected()), Toast.LENGTH_SHORT).show();
            BaseGameUtils.showAlert(this, getString(R.string.you_won));
            if (mGoogleApiClient.isConnected()) {
                // unlock the "Trivial Victory" achievement.
                Games.Achievements.unlock(mGoogleApiClient,
                        getString(R.string.victory));
            }
        });

        this.mRootView.homeBtnSignIn.setOnClickListener(view -> {
            this.onSignIn();
        });
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    public void onShowLoading() {
        super.showLoading();
    }

    @Override
    public void onDismissLoading() {
        super.dismissLoading();
    }

    @Override
    public void onShowMsgError(String msg) {
        super.showDialogError(msg);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Is Enable: " + (this.mGoogleApiClient != null
                && this.mGoogleApiClient.isConnected()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    private void onSignIn() {
        if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id,
                R.string.victory)) {
            Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
        }

        // start the sign-in flow
        Log.d(TAG, "Sign-in button clicked");
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (responseCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.signin_other_error);
            }
        }
    }


}
