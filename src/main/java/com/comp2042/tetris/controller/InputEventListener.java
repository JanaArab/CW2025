/**
 * connection point between user and programme especially when it
 * comes to keys
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.DownData;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.data.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
