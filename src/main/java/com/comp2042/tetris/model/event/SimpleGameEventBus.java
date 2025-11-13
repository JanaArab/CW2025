package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import javafx.beans.property.IntegerProperty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleGameEventBus implements GameEventPublisher {
    private final List<GameEventListener> listeners = new CopyOnWriteArrayList<>();

    public void registerListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void publishGameInitialized(GameStateSnapshot snapshot, IntegerProperty scoreProperty) {
        for (GameEventListener listener : listeners) {
            listener.onGameInitialized(snapshot, scoreProperty);
        }
    }

    @Override
    public void publishBrickUpdated(ViewData viewData) {
        for (GameEventListener listener : listeners) {
            listener.onBrickUpdated(viewData);
        }
    }

    @Override
    public void publishBoardUpdated(int[][] boardMatrix) {
        for (GameEventListener listener : listeners) {
            listener.onBoardUpdated(boardMatrix);
        }
    }

    @Override
    public void publishLinesCleared(ClearRow clearRow) {
        for (GameEventListener listener : listeners) {
            listener.onLinesCleared(clearRow);
        }
    }

    @Override
    public void publishGameOver() {
        for (GameEventListener listener : listeners) {
            listener.onGameOver();
        }
    }

}
