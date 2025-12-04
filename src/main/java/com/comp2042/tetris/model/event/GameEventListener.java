package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import javafx.beans.property.IntegerProperty;

public interface GameEventListener {
    void onGameInitialized(GameStateSnapshot snapshot);

    void onScoreChanged(ScoreChangeEvent event);


    void onBrickUpdated(ViewData viewData);

    void onBoardUpdated(int[][] boardMatrix);

    void onLinesCleared(ClearRow clearRow);

    void onGameOver();

    void onBrickPlaced(BrickPlacedEvent event);
}
