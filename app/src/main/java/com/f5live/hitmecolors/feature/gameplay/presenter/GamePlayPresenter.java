package com.f5live.hitmecolors.feature.gameplay.presenter;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public interface GamePlayPresenter {
    void onPlayGame();

    void onReplayGame();

    void onPauseGame();

    void onExitGame();

    void onResumeGame();
}
