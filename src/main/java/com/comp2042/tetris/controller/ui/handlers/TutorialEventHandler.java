package com.comp2042.tetris.controller.ui.handlers;

/**
 * Event handler interface for tutorial screen actions.
 * Provides callback for closing the tutorial panel.
 *
 * @see com.comp2042.tetris.controller.ui.TutorialController
 */
public interface TutorialEventHandler {

    /**
     * Called when the close tutorial button is clicked.
     */
    void onCloseTutorial();
}
