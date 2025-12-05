package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple implementation of {@link GameEventPublisher} using the Observer pattern.
 * Maintains a thread-safe list of listeners and notifies them of game events.
 *
 * <p>Uses {@link CopyOnWriteArrayList} for thread-safe listener management.</p>
 *
 * @see GameEventPublisher
 * @see GameEventListener
 */
public class SimpleGameEventBus implements GameEventPublisher {
    private final List<GameEventListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerListener(GameEventListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishGameInitialized(GameStateSnapshot snapshot) {
        for (GameEventListener listener : listeners) {
            listener.onGameInitialized(snapshot);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishScoreChanged(ScoreChangeEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onScoreChanged(event);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void publishBrickUpdated(ViewData viewData) {
        for (GameEventListener listener : listeners) {
            listener.onBrickUpdated(viewData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishBoardUpdated(int[][] boardMatrix) {
        for (GameEventListener listener : listeners) {
            listener.onBoardUpdated(boardMatrix);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishLinesCleared(ClearRow clearRow) {
        for (GameEventListener listener : listeners) {
            listener.onLinesCleared(clearRow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishGameOver() {
        for (GameEventListener listener : listeners) {
            listener.onGameOver();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishBrickPlaced(BrickPlacedEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onBrickPlaced(event);
        }
    }
}
