package com.comp2042.tetris.controller.core;

import com.comp2042.tetris.model.event.EventSource;

public interface GameActionInvoker {
    void moveDown(EventSource source);

    void moveDown();

    void moveLeft();

    void moveRight();

    void rotate();

    void instantDrop();
}
