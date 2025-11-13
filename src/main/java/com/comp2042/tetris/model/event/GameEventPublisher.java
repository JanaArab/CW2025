package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import javafx.beans.property.IntegerProperty;

public interface GameEventPublisher {
    void publishGameInitialized(GameStateSnapshot snapshot, IntegerProperty scoreProperty);

    void publishBrickUpdated(ViewData viewData);

    void publishBoardUpdated(int[][] boardMatrix);

    void publishLinesCleared(ClearRow clearRow);

    void publishGameOver();

}
