package com.comp2042.tetris.controller;

public interface MenuEventHandler {
    void onStartGame();
    void onExitGame();
    void onOpenMusicControl();
    void onOpenTutorial();
    void onResumeGame();
    void onStartNewGameFromPause();
    void onGoToMainMenuFromPause();
    void onEndGameFromPause();
}

