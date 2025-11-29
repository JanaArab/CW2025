package com.comp2042.tetris.controller;
import com.comp2042.tetris.model.level.GameLevel;
public interface IGameController extends InputEventListener {
    void setLevel(GameLevel level);
}
