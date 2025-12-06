package com.comp2042.tetris.controller.ui.handlers;

/**
 * Event handler interface for settings/music control screen actions.
 * Provides callback for closing the settings panel.
 *
 * @see com.comp2042.tetris.controller.ui.SettingsController
 */
public interface SettingsEventHandler {

    /**
     * Called when the close button is clicked.
     */
    void onCloseMusicControl();
}
