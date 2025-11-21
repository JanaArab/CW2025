package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;

public interface GameEventPublisher {

    void registerListener(GameEventListener listener);

    void unregisterListener(GameEventListener listener);

    void publishGameInitialized(GameStateSnapshot snapshot);

    void publishScoreChanged(ScoreChangeEvent event);


    void publishBrickUpdated(ViewData viewData);

    void publishBoardUpdated(int[][] boardMatrix);

    void publishLinesCleared(ClearRow clearRow);

    void publishGameOver();

}
