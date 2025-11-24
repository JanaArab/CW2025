package com.comp2042.tetris.view;

import javafx.util.Duration;

public class UIConstants {

    public static final int BRICK_SIZE = 20;
    public static final int BOARD_TOP_OFFSET = -42;
    public static final Duration DEFAULT_DROP_INTERVAL = Duration.millis(400);

    public static final int WINDOW_WIDTH = 850;
    public static final int WINDOW_HEIGHT = 480;

    public static final int LOGICAL_BOARD_MIN_COL = 4; // Treat columns 0 and 1 as invalid
    public static final int LOGICAL_BOARD_MAX_COL = 70;

    private UIConstants() {
    }
}
