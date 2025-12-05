package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.core.GameController;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private StubBoard board;
    private StubPublisher publisher;

    @BeforeEach
    void setUp() {
        board = new StubBoard();
        publisher = new StubPublisher();
    }

    @Test
    void constructorUsingSupplierPublishesInitialState() {
        Supplier<Board> supplier = () -> board;

        new GameController(supplier, publisher);

        assertEquals(1, board.createNewBrickCalls);
        assertEquals(1, publisher.initializedSnapshots.size());
        assertEquals(1, publisher.scoreEvents.size());
    }

    @Test
    void onLeftEventMovesBrickLeftAndPublishesViewData() {
        GameController controller = new GameController(board, publisher);
        board.createNewBrickCalls = 0; // reset after constructor call
        publisher.brickUpdates.clear();

        controller.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));

        assertEquals(1, board.moveLeftCalls);
        assertEquals(1, publisher.brickUpdates.size());
        assertSame(board.viewData, publisher.brickUpdates.peekLast());
    }

    @Test
    void onRightEventMovesBrickRightAndPublishesViewData() {
        GameController controller = new GameController(board, publisher);
        publisher.brickUpdates.clear();

        controller.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));

        assertEquals(1, board.moveRightCalls);
        assertEquals(1, publisher.brickUpdates.size());
    }

    @Test
    void onRotateEventRotatesBrickAndPublishesViewData() {
        GameController controller = new GameController(board, publisher);
        publisher.brickUpdates.clear();

        controller.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));

        assertEquals(1, board.rotateCalls);
        assertEquals(1, publisher.brickUpdates.size());
    }

    @Test
    void createNewGameResetsBoardAndPublishesStateAndScore() {
        GameController controller = new GameController(board, publisher);
        publisher.brickUpdates.clear();
        publisher.boardUpdates.clear();
        publisher.scoreEvents.clear();

        controller.createNewGame();

        assertEquals(1, board.newGameCalls);
        assertEquals(1, publisher.boardUpdates.size());
        assertEquals(1, publisher.brickUpdates.size());
        assertEquals(1, publisher.scoreEvents.size());
    }

    private static final class StubBoard implements Board {
        private final com.comp2042.tetris.model.score.Score score = new com.comp2042.tetris.model.score.Score();
        private final ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{2}});
        private final int[][] boardMatrix = new int[][]{{0}};
        private int moveLeftCalls;
        private int moveRightCalls;
        private int rotateCalls;
        private int newGameCalls;
        private int createNewBrickCalls;

        @Override
        public boolean moveBrickDown() { return false; }

        @Override
        public boolean moveBrickLeft() {
            moveLeftCalls++;
            return true;
        }

        @Override
        public boolean moveBrickRight() {
            moveRightCalls++;
            return true;
        }

        @Override
        public boolean rotateLeftBrick() {
            rotateCalls++;
            return true;
        }

        @Override
        public boolean createNewBrick() {
            createNewBrickCalls++;
            return false;
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
        public ClearRow clearRows() { return new ClearRow(0, new int[][]{{0}}, 0, java.util.Collections.emptyList()); }

        @Override
        public com.comp2042.tetris.model.score.Score getScore() { return score; }

        @Override
        public void mergeBrickToBackground() {}

        @Override
        public void newGame() {
            newGameCalls++;
        }

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
        private final Deque<GameStateSnapshot> initializedSnapshots = new ArrayDeque<>();
        private final Deque<ScoreChangeEvent> scoreEvents = new ArrayDeque<>();
        private final Deque<ViewData> brickUpdates = new ArrayDeque<>();
        private final Deque<int[][]> boardUpdates = new ArrayDeque<>();

        @Override
        public void registerListener(GameEventListener listener) {}

        @Override
        public void unregisterListener(GameEventListener listener) {}

        @Override
        public void publishGameInitialized(GameStateSnapshot snapshot) {
            initializedSnapshots.add(snapshot);
        }

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
        public void publishLinesCleared(ClearRow clearRow) {}

        @Override
        public void publishGameOver() {}

        @Override
        public void publishBrickPlaced(BrickPlacedEvent event) {}
    }
}
