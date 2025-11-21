package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ScoreManagerTest {

    private Score score;
    private StubPublisher publisher;
    private StubPolicy policy;
    private ScoreManager manager;

    @BeforeEach
    void setUp() {
        score = new Score();
        publisher = new StubPublisher();
        policy = new StubPolicy();
        manager = new ScoreManager(score, publisher, policy);
    }

    @Test
    void publishCurrentScoreEmitsEventWithCurrentValue() {
        score.add(250);

        manager.publishCurrentScore();

        assertEquals(1, publisher.scoreEvents.size());
        assertEquals(250, publisher.scoreEvents.peekLast().newScore());
    }

    @Test
    void handleDropAddsScoreWhenPolicyReturnsPositiveValue() {
        policy.nextScore = 3;
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        manager.handleDrop(event);

        assertEquals(3, score.getValue());
        assertEquals(1, publisher.scoreEvents.size());
        assertSame(event, policy.lastEvent);
    }

    @Test
    void handleDropIgnoresZeroOrNegativeScores() {
        policy.nextScore = 0;
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

        manager.handleDrop(event);

        assertEquals(0, score.getValue());
        assertTrue(publisher.scoreEvents.isEmpty());
    }

    @Test
    void handleLinesClearedAddsScorePublishesAndBroadcastsClearRow() {
        ClearRow clearRow = new ClearRow(2, new int[][]{{0}}, 300);

        manager.handleLinesCleared(clearRow);

        assertEquals(300, score.getValue());
        assertEquals(1, publisher.scoreEvents.size());
        assertSame(clearRow, publisher.lastLinesCleared);
    }

    @Test
    void handleLinesClearedSkipsWhenNoLinesRemoved() {
        ClearRow clearRow = new ClearRow(0, new int[][]{{0}}, 0);

        manager.handleLinesCleared(clearRow);

        assertEquals(0, score.getValue());
        assertTrue(publisher.scoreEvents.isEmpty());
        assertNull(publisher.lastLinesCleared);
    }

    private static final class StubPolicy implements ScorePolicy {
        private int nextScore;
        private MoveEvent lastEvent;

        @Override
        public int calculateDropScore(MoveEvent event) {
            this.lastEvent = event;
            return nextScore;
        }
    }

    private static final class StubPublisher implements GameEventPublisher {
        private final Deque<ScoreChangeEvent> scoreEvents = new ArrayDeque<>();
        private ClearRow lastLinesCleared;

        @Override
        public void registerListener(com.comp2042.tetris.model.event.GameEventListener listener) {
        }

        @Override
        public void unregisterListener(com.comp2042.tetris.model.event.GameEventListener listener) {
        }

        @Override
        public void publishGameInitialized(com.comp2042.tetris.model.event.GameStateSnapshot snapshot) {
        }

        @Override
        public void publishScoreChanged(ScoreChangeEvent event) {
            scoreEvents.add(event);
        }

        @Override
        public void publishBrickUpdated(com.comp2042.tetris.model.data.ViewData viewData) {
        }

        @Override
        public void publishBoardUpdated(int[][] boardMatrix) {
        }

        @Override
        public void publishLinesCleared(ClearRow clearRow) {
            this.lastLinesCleared = clearRow;
        }

        @Override
        public void publishGameOver() {
        }
    }
}

