package com.comp2042.tetris.game;

import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AnimationHandlerTest {

    private GameStateManager stateManager;
    private int tickCount;

    @BeforeEach
    void setUp() {
        stateManager = new GameStateManager();
        tickCount = 0;
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor throws when gameStateManager is null")
        void constructor_nullStateManager_throws() {
            assertThrows(NullPointerException.class,
                () -> new AnimationHandler(null, Duration.millis(100), () -> {}));
        }

        @Test
        @DisplayName("Constructor throws when tickInterval is null")
        void constructor_nullInterval_throws() {
            assertThrows(NullPointerException.class,
                () -> new AnimationHandler(stateManager, null, () -> {}));
        }

        @Test
        @DisplayName("Constructor throws when tickAction is null")
        void constructor_nullTickAction_throws() {
            assertThrows(NullPointerException.class,
                () -> new AnimationHandler(stateManager, Duration.millis(100), null));
        }

        @Test
        @DisplayName("Constructor creates valid instance with valid params")
        void constructor_validParams_createsInstance() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> tickCount++);
            assertNotNull(handler);
        }
    }

    @Nested
    @DisplayName("State Delegation Tests")
    class StateDelegationTests {

        @Test
        @DisplayName("isPaused delegates to GameStateManager - initially false")
        void isPaused_initiallyFalse() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertFalse(handler.isPaused());
        }

        @Test
        @DisplayName("isPaused reflects GameStateManager state")
        void isPaused_reflectsStateManager() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertFalse(handler.isPaused());

            // Directly manipulate state manager (which doesn't require JavaFX)
            stateManager.togglePause(null);
            assertTrue(handler.isPaused());

            stateManager.togglePause(null);
            assertFalse(handler.isPaused());
        }

        @Test
        @DisplayName("isGameOver delegates to GameStateManager - initially false")
        void isGameOver_initiallyFalse() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertFalse(handler.isGameOver());
        }

        @Test
        @DisplayName("isGameOver reflects GameStateManager state")
        void isGameOver_reflectsStateManager() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertFalse(handler.isGameOver());

            stateManager.onGameOver(null);
            assertTrue(handler.isGameOver());
        }
    }

    @Nested
    @DisplayName("SetTickInterval Tests - Before Initialization")
    class SetTickIntervalBeforeInitTests {

        @Test
        @DisplayName("setTickInterval before initialization doesn't throw")
        void setTickInterval_beforeInit_doesNotThrow() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            // This should work because gameLoop is null, so it just updates the field
            assertDoesNotThrow(() -> handler.setTickInterval(Duration.millis(50)));
        }

        @Test
        @DisplayName("setTickInterval accepts various durations")
        void setTickInterval_acceptsVariousDurations() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertDoesNotThrow(() -> handler.setTickInterval(Duration.millis(50)));
            assertDoesNotThrow(() -> handler.setTickInterval(Duration.millis(200)));
            assertDoesNotThrow(() -> handler.setTickInterval(Duration.seconds(1)));
        }
    }

    @Nested
    @DisplayName("TogglePause Before Init Tests")
    class TogglePauseBeforeInitTests {

        @Test
        @DisplayName("togglePause does nothing before initialization (no gameLoop)")
        void togglePause_beforeInit_doesNothing() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            // Before init, gameLoop is null, so togglePause should return early
            handler.togglePause();

            // State should not have changed since gameLoop was null
            assertFalse(handler.isPaused());
        }
    }

    @Nested
    @DisplayName("OnGameOver Tests")
    class OnGameOverTests {

        @Test
        @DisplayName("onGameOver sets game over state via state manager")
        void onGameOver_setsState() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertFalse(handler.isGameOver());

            handler.onGameOver();
            assertTrue(handler.isGameOver());
        }

        @Test
        @DisplayName("onGameOver clears paused state")
        void onGameOver_clearsPausedState() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            // Set paused via state manager directly
            stateManager.togglePause(null);
            assertTrue(handler.isPaused());

            handler.onGameOver();
            assertFalse(handler.isPaused());
            assertTrue(handler.isGameOver());
        }

        @Test
        @DisplayName("Multiple onGameOver calls don't throw")
        void onGameOver_multipleCalls_dontThrow() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});

            assertDoesNotThrow(() -> {
                handler.onGameOver();
                handler.onGameOver();
                handler.onGameOver();
            });

            assertTrue(handler.isGameOver());
        }
    }

    @Nested
    @DisplayName("Initial State Tests")
    class InitialStateTests {

        @Test
        @DisplayName("New handler is not paused")
        void newHandler_isNotPaused() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});
            assertFalse(handler.isPaused());
        }

        @Test
        @DisplayName("New handler is not game over")
        void newHandler_isNotGameOver() {
            AnimationHandler handler = new AnimationHandler(
                stateManager, Duration.millis(100), () -> {});
            assertFalse(handler.isGameOver());
        }
    }
}
