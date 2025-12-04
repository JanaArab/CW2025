package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.model.event.BrickPlacedEvent;
import com.comp2042.tetris.model.score.Score;
import com.comp2042.tetris.model.score.ScoreManager;
import com.comp2042.tetris.model.score.ScorePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

class GameFlowManagerTest {

    private StubBoard board;
    private StubPublisher publisher;
    private RecordingScorePolicy scorePolicy;
    private Score score;
    private ScoreManager scoreManager;
    private GameFlowManager manager;

    @BeforeEach
    void setUp() {
        board = new StubBoard();
        publisher = new StubPublisher();
        scorePolicy = new RecordingScorePolicy();
        score = new Score();
        scoreManager = new ScoreManager(score, publisher, scorePolicy);
        manager = new GameFlowManager(board, publisher, scoreManager);
    }

    @Test
    void handleDownEventMovesBrickAndPublishesWhenMoveSucceeds() {
        board.moveDownResult = true;
        scorePolicy.nextDropScore = 4;

        manager.handleDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        assertEquals(4, score.getValue());
        assertEquals(1, publisher.scoreEvents.size());
        assertEquals(4, publisher.scoreEvents.peekLast().newScore());
        assertEquals(1, publisher.brickUpdates.size());
        assertTrue(publisher.boardUpdates.isEmpty());
        assertTrue(publisher.linesClearedEvents.isEmpty());
        assertEquals(0, publisher.gameOverCalls);
    }

    @Test
    void handleDownEventMergesClearsAndSpawnsNewBrickWhenMoveFailsAndNewBrickFits() {
        board.moveDownResult = false;
        board.clearRowResult = new ClearRow(1, new int[][]{{1}}, 100, java.util.List.of(5));
        board.createNewBrickResult = false;

        manager.handleDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        assertEquals(100, score.getValue());
        assertEquals(1, publisher.scoreEvents.size());
        assertEquals(100, publisher.scoreEvents.peekLast().newScore());
        assertEquals(1, publisher.boardUpdates.size());
        assertEquals(1, publisher.brickUpdates.size());
        assertEquals(1, publisher.linesClearedEvents.size());
        assertEquals(0, publisher.gameOverCalls);
        assertTrue(board.mergeCalled);
    }

    @Test
    void handleDownEventPublishesGameOverWhenNewBrickCannotSpawn() {
        board.moveDownResult = false;
        board.clearRowResult = new ClearRow(0, new int[][]{{0}}, 0, java.util.Collections.emptyList());
        board.createNewBrickResult = true;

        manager.handleDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));

        assertEquals(0, score.getValue());
        assertEquals(0, publisher.scoreEvents.size());
        assertEquals(1, publisher.gameOverCalls);
        assertEquals(1, publisher.boardUpdates.size());
        assertTrue(publisher.brickUpdates.isEmpty());
        assertEquals(0, scorePolicy.callCount);
    }

    private static final class StubBoard implements Board {
        private boolean moveDownResult;
        private ClearRow clearRowResult = new ClearRow(0, new int[][]{{0}}, 0, java.util.Collections.emptyList());
        private boolean createNewBrickResult;
        private boolean mergeCalled;

        private final ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{2}});

        private final int[][] boardMatrix = new int[][]{{0}};

        @Override
        public boolean moveBrickDown() {
            return moveDownResult;
        }

        @Override
        public boolean moveBrickLeft() {
            return false;
        }

        @Override
        public boolean moveBrickRight() {
            return false;
        }

        @Override
        public boolean rotateLeftBrick() {
            return false;
        }

        @Override
        public boolean createNewBrick() {
            return createNewBrickResult;
        }

        @Override
        public int[][] getBoardMatrix() {
            return boardMatrix;
        }

        @Override
        public ViewData getViewData() {
            return viewData;
        }

        @Override
        public void mergeBrickToBackground() {
            mergeCalled = true;
        }

        @Override
        public ClearRow clearRows() {
            return clearRowResult;
        }

        @Override
        public com.comp2042.tetris.model.score.Score getScore() {
            return new com.comp2042.tetris.model.score.Score();
        }

        @Override
        public void newGame() {}

        @Override
        public void setLevel(com.comp2042.tetris.model.level.GameLevel level) {
            // no-op for test stub
        }

        @Override
        public void addRows(int[][] rows) {
            // no-op for test stub
        }
    }

    private static final class StubPublisher implements GameEventPublisher {
        private final Deque<int[][]> boardUpdates = new ArrayDeque<>();
        private final Deque<ViewData> brickUpdates = new ArrayDeque<>();
        private final Deque<ClearRow> linesClearedEvents = new ArrayDeque<>();
        private final Deque<ScoreChangeEvent> scoreEvents = new ArrayDeque<>();
        private int gameOverCalls;

        @Override
        public void registerListener(GameEventListener listener) {}

        @Override
        public void unregisterListener(GameEventListener listener) {}

        @Override
        public void publishGameInitialized(GameStateSnapshot snapshot) {}

        @Override
        public void publishScoreChanged(ScoreChangeEvent event) {
            scoreEvents.add(event);
        }

        @Override
        public void publishBrickUpdated(ViewData viewData) {
            brickUpdates.add(viewData);
        }

        @Override
        public void publishBoardUpdated(int[][] boardMatrix) {
            boardUpdates.add(boardMatrix);
        }

        @Override
        public void publishLinesCleared(ClearRow clearRow) {
            linesClearedEvents.add(clearRow);
        }

        @Override
        public void publishGameOver() {
            gameOverCalls++;
        }

        @Override
        public void publishBrickPlaced(BrickPlacedEvent event) {}
    }

    private static final class RecordingScorePolicy implements ScorePolicy {
        private int nextDropScore;
        private int callCount;

        @Override
        public int calculateDropScore(MoveEvent event) {
            callCount++;
            return nextDropScore;
        }
    }
}
