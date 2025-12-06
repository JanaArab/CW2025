package com.comp2042.tetris.view;

import javafx.util.Duration;

/**
 * Constants for UI layout and timing.
 * Contains shared values used across the view layer.
 *
 * @see BoardRenderer
 * @see GameViewPresenter
 */
public class UIConstants {

    /** Size of each brick cell in pixels. */
    public static final int BRICK_SIZE = 20;

    /** Vertical offset for the game board display. */
    public static final int BOARD_TOP_OFFSET = -42;

    /** Default time between automatic brick drops. */
    public static final Duration DEFAULT_DROP_INTERVAL = Duration.millis(400);

    /** Width of the game window in pixels. */
    public static final int WINDOW_WIDTH = 950;

    /** Height of the game window in pixels. */
    public static final int WINDOW_HEIGHT = 530;

    /** Minimum column index for valid game board area. */
    public static final int LOGICAL_BOARD_MIN_COL = 4; // Treat columns 0 and 1 as invalid

    /** Maximum column index for valid game board area. */
    public static final int LOGICAL_BOARD_MAX_COL = 70;

    private UIConstants() {
        // Utility class - prevent instantiation
    }
}
