package com.comp2042.tetris.controller.core;

import com.comp2042.tetris.model.level.GameLevel;

/**
 * Interface for the main game controller.
 * Extends {@link InputEventListener} to handle player inputs
 * and adds level management capabilities.
 *
 * @see GameController
 * @see InputEventListener
 */
public interface IGameController extends InputEventListener {

    /**
     * Sets the current game level which affects game mechanics.
     *
     * @param level the GameLevel to apply
     */
    void setLevel(GameLevel level);
}
