package com.comp2042.tetris.controller.ui.handlers;

/**
 * Event handler interface for level selection screen actions.
 * Provides callbacks for level selection and cancellation.
 *
 * @see com.comp2042.tetris.controller.ui.LevelSelectionController
 */
public interface LevelSelectionEventHandler {

    /**
     * Called when a level is selected.
     *
     * @param levelType the type of level selected (e.g., "CLASSIC", "LEVEL1")
     */
    void onLevelSelected(String levelType);

    /**
     * Called when level selection is cancelled.
     */
    void onLevelSelectionCancelled();
}
