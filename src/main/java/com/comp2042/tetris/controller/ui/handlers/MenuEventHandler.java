package com.comp2042.tetris.controller.ui.handlers;

/**
 * Event handler interface for menu actions.
 * Provides callbacks for main menu and pause menu button clicks.
 *
 * @see com.comp2042.tetris.controller.ui.MainMenuController
 * @see com.comp2042.tetris.controller.ui.PauseMenuController
 */
public interface MenuEventHandler {

    /**
     * Called when the start game button is clicked.
     */
    void onStartGame();

    /**
     * Called when the exit game button is clicked.
     */
    void onExitGame();

    /**
     * Called when the music control button is clicked.
     */
    void onOpenMusicControl();

    /**
     * Called when the tutorial button is clicked.
     */
    void onOpenTutorial();

    /**
     * Called when the resume game button is clicked from pause menu.
     */
    void onResumeGame();

    /**
     * Called when starting a new game from the pause menu.
     */
    void onStartNewGameFromPause();

    /**
     * Called when returning to main menu from pause menu.
     */
    void onGoToMainMenuFromPause();

    /**
     * Called when ending the game from pause menu.
     */
    void onEndGameFromPause();
}
