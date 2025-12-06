package com.comp2042.tetris.controller.ui.handlers;

/**
 * Event handler interface for confirmation dialog actions.
 * Provides callbacks for confirm and cancel button clicks.
 *
 * @see com.comp2042.tetris.controller.ui.ConfirmationDialogController
 */
public interface ConfirmationEventHandler {

    /**
     * Called when the user confirms the action.
     */
    void onConfirm();

    /**
     * Called when the user cancels the action.
     */
    void onCancel();
}
