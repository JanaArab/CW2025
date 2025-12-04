package com.comp2042.tetris.controller;

public interface LevelSelectionEventHandler {
    void onLevelSelected(String levelType);
    void onLevelSelectionCancelled();
}

