package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

class SimpleGameEventBusTest {

    private SimpleGameEventBus bus;
    private RecordingListener listener;

    @BeforeEach
    void setUp() {
        bus = new SimpleGameEventBus();
        listener = new RecordingListener();
        bus.registerListener(listener);
    }

    @Test
    void registerAndUnregisterListener() {
        SimpleGameEventBus localBus = new SimpleGameEventBus();
        RecordingListener localListener = new RecordingListener();

        localBus.registerListener(localListener);
        localBus.unregisterListener(localListener);

        localBus.publishGameOver();
        assertEquals(0, localListener.gameOverCount);
    }

    @Test
    void publishGameInitializedNotifiesListeners() {
        GameStateSnapshot snapshot = new GameStateSnapshot(new int[][]{{1}}, new ViewData(new int[][]{{2}}, 0, 0, new int[][]{{3}}));

        bus.publishGameInitialized(snapshot);

        assertSame(snapshot, listener.initializedSnapshots.peekLast());
    }

    @Test
    void publishScoreChangedNotifiesListeners() {
        ScoreChangeEvent event = new ScoreChangeEvent(123);

        bus.publishScoreChanged(event);

        assertSame(event, listener.scoreEvents.peekLast());
    }

    @Test
    void publishBrickUpdatedNotifiesListeners() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 2, 3, new int[][]{{4}});

        bus.publishBrickUpdated(viewData);

        assertSame(viewData, listener.brickUpdates.peekLast());
    }

    @Test
    void publishBoardUpdatedNotifiesListeners() {
        int[][] board = new int[][]{{5}};

        bus.publishBoardUpdated(board);

        assertSame(board, listener.boardUpdates.peekLast());
    }

    @Test
    void publishLinesClearedNotifiesListeners() {
        ClearRow clearRow = new ClearRow(1, new int[][]{{0}}, 50, java.util.List.of(5));

        bus.publishLinesCleared(clearRow);

        assertSame(clearRow, listener.linesCleared.peekLast());
    }

    @Test
    void publishGameOverNotifiesListeners() {
        bus.publishGameOver();

        assertEquals(1, listener.gameOverCount);
    }

    private static final class RecordingListener implements GameEventListener {
        private final Deque<GameStateSnapshot> initializedSnapshots = new ArrayDeque<>();
        private final Deque<ScoreChangeEvent> scoreEvents = new ArrayDeque<>();
        private final Deque<ViewData> brickUpdates = new ArrayDeque<>();
        private final Deque<int[][]> boardUpdates = new ArrayDeque<>();
        private final Deque<ClearRow> linesCleared = new ArrayDeque<>();
        private int gameOverCount;

        @Override
        public void onGameInitialized(GameStateSnapshot snapshot) {
            initializedSnapshots.add(snapshot);
        }

        @Override
        public void onScoreChanged(ScoreChangeEvent event) {
            scoreEvents.add(event);
        }

        @Override
        public void onBrickUpdated(ViewData viewData) {
            brickUpdates.add(viewData);
        }

        @Override
        public void onBoardUpdated(int[][] boardMatrix) {
            boardUpdates.add(boardMatrix);
        }

        @Override
        public void onLinesCleared(ClearRow clearRow) {
            linesCleared.add(clearRow);
        }

        @Override
        public void onGameOver() {
            gameOverCount++;
        }

        @Override
        public void onBrickPlaced(BrickPlacedEvent event) {}
    }
}
