/**
 * connection point between user and programme especially when it
 * comes to keys
 */

package com.comp2042.tetris.controller;


import com.comp2042.tetris.model.event.MoveEvent;


public interface InputEventListener {

    void onDownEvent(MoveEvent event);

    void onLeftEvent(MoveEvent event);

    void onRightEvent(MoveEvent event);

    void  onRotateEvent(MoveEvent event);

    void onInstantDropEvent(MoveEvent event);

    void createNewGame();
}
