package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.controller.command.GameCommand;
import com.comp2042.tetris.controller.command.InputCommandFactory;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    private RecordingCommand gameplayCommand;
    private RecordingCommand globalCommand;
    private RecordingRegistry registry;
    private InputHandler handler;
    private RecordingController controller;

    @BeforeEach
    void setUp() {
        gameplayCommand = new RecordingCommand();
        globalCommand = new RecordingCommand();
        registry = new RecordingRegistry(globalCommand);
        handler = new InputHandler(() -> false, () -> false, registry, new RecordingFactory(gameplayCommand));
        controller = new RecordingController();
        handler.setGameController(controller);
    }

    @Test
    void handleExecutesGlobalCommandEvenWhenPaused() {
        handler.handle(mockKeyEvent(KeyCode.P));

        assertEquals(1, globalCommand.executions);
        assertEquals(0, gameplayCommand.executions);
    }

    @Test
    void handleIgnoresGameplayCommandWhenInteractionDisabled() {
        InputHandler pausedHandler = new InputHandler(() -> true, () -> false, registry, new RecordingFactory(gameplayCommand));
        pausedHandler.setGameController(controller);

        boolean handled = pausedHandler.handle(mockKeyEvent(KeyCode.LEFT));

        assertFalse(handled);
        assertEquals(0, gameplayCommand.executions);
    }

    @Test
    void handleExecutesGameplayCommandWhenActive() {
        boolean handled = handler.handle(mockKeyEvent(KeyCode.LEFT));

        assertTrue(handled);
        assertEquals(1, gameplayCommand.executions);
    }

    @Test
    void moveDownDelegatesToControllerWithSource() {
        handler.moveDown(EventSource.THREAD);

        assertEquals(1, controller.downCalls);
        assertEquals(EventSource.THREAD, controller.lastDownEvent.getEventSource());
    }

    @Test
    void moveLeftRightRotateInvokeControllerActions() {
        handler.moveLeft();
        handler.moveRight();
        handler.rotate();

        assertEquals(1, controller.leftCalls);
        assertEquals(1, controller.rightCalls);
        assertEquals(1, controller.rotateCalls);
    }

    private static KeyEvent mockKeyEvent(KeyCode code) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code, false, false, false, false);
    }

    private static final class RecordingCommand implements GameCommand {
        private int executions;

        @Override
        public void execute() {
            executions++;
        }
    }

    private static final class RecordingRegistry implements CommandRegistry {
        private final RecordingCommand globalCommand;

        private RecordingRegistry(RecordingCommand globalCommand) {
            this.globalCommand = globalCommand;
        }

        @Override
        public void registerCommands(GameActionInvoker invoker, InputCommandRegistrar registrar, InputCommandFactory factory) {
            registrar.registerCommand(factory.createMoveLeftCommand(invoker), true, KeyCode.LEFT);
            registrar.registerCommand(globalCommand, false, KeyCode.P);
        }
    }

    private static final class RecordingFactory implements InputCommandFactory {
        private final RecordingCommand gameplayCommand;

        private RecordingFactory(RecordingCommand gameplayCommand) {
            this.gameplayCommand = gameplayCommand;
        }

        @Override
        public GameCommand createMoveDownCommand(GameActionInvoker invoker) {
            return gameplayCommand;
        }

        @Override
        public GameCommand createMoveLeftCommand(GameActionInvoker invoker) {
            return gameplayCommand;
        }

        @Override
        public GameCommand createMoveRightCommand(GameActionInvoker invoker) {
            return gameplayCommand;
        }

        @Override
        public GameCommand createRotateCommand(GameActionInvoker invoker) {
            return gameplayCommand;
        }
    }

    private static final class RecordingController implements IGameController {
        private int downCalls;
        private int leftCalls;
        private int rightCalls;
        private int rotateCalls;
        private MoveEvent lastDownEvent;

        @Override
        public void onDownEvent(MoveEvent event) {
            downCalls++;
            lastDownEvent = event;
        }

        @Override
        public void onLeftEvent(MoveEvent event) {
            leftCalls++;
        }

        @Override
        public void onRightEvent(MoveEvent event) {
            rightCalls++;
        }

        @Override
        public void onRotateEvent(MoveEvent event) {
            rotateCalls++;
        }

        @Override
        public void createNewGame() {}

        @Override
        public void setLevel(com.comp2042.tetris.model.level.GameLevel level) {
            // no-op for tests
        }
    }
}
