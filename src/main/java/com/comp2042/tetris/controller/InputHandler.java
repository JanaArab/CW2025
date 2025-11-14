package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.command.GameCommand;
import com.comp2042.tetris.controller.command.MoveDownCommand;
import com.comp2042.tetris.controller.command.MoveLeftCommand;
import com.comp2042.tetris.controller.command.MoveRightCommand;
import com.comp2042.tetris.controller.command.RotateCommand;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.EnumMap;
import java.util.Objects;
import java.util.Map;
import java.util.function.BooleanSupplier;
public class InputHandler {
    private final Map<KeyCode, GameCommand> gameplayCommands = new EnumMap<>(KeyCode.class);
    private final Map<KeyCode, GameCommand> globalCommands = new EnumMap<>(KeyCode.class);
    private final BooleanSupplier isPauseSupplier;
    private final BooleanSupplier isGameOverSupplier;
    private  IGameController gameController;

    public InputHandler(BooleanSupplier isPauseSupplier, BooleanSupplier isGameOverSupplier ) {
        this.isPauseSupplier = isPauseSupplier;
        this.isGameOverSupplier = isGameOverSupplier;
    }

    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
        registerCommands();
    }

    public boolean handle(KeyEvent keyEvent) {
        if (keyEvent == null)  {
            return false;
        }

        GameCommand command = globalCommands.get(keyEvent.getCode());
        if (command != null) {
            command.execute();
            keyEvent.consume();
            return true;
        }

        if (isInteractionDisabled()) {
            return false;
        }

        command = gameplayCommands.get(keyEvent.getCode());
        if (command == null) {
            return false;
        }

        command.execute();
        keyEvent.consume();
        return true;
    }

    public void handleDown(EventSource source) {
        if (isInteractionDisabled()) {
            return;
        }
        if (gameController != null) {
            gameController.onDownEvent(new MoveEvent(EventType.DOWN, source));
        }
    }

    public void handleDown() {
        handleDown(EventSource.USER);
    }

    public void handleLeftMove() {
        if (gameController != null) {
            gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        }
    }

    public void handleRightMove() {
        if (gameController != null) {
            gameController.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        }
    }

    public void handleRotate() {
        if (gameController != null) {
            gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        }
    }

    private boolean isInteractionDisabled() {
        return (isPauseSupplier != null && isPauseSupplier.getAsBoolean())
                || (isGameOverSupplier != null && isGameOverSupplier.getAsBoolean());
    }

    private void registerCommands() {
        gameplayCommands.clear();
        if (gameController == null) {
            return;
        }

        GameCommand leftCommand = new MoveLeftCommand(this);
        GameCommand rightCommand = new MoveRightCommand(this);
        GameCommand downCommand = new MoveDownCommand(this);
        GameCommand rotateCommand = new RotateCommand(this);

        registerCommand(leftCommand, true, KeyCode.LEFT, KeyCode.A);
        registerCommand(rightCommand, true, KeyCode.RIGHT, KeyCode.D);
        registerCommand(downCommand, true, KeyCode.DOWN, KeyCode.S);
        registerCommand(rotateCommand, true, KeyCode.UP, KeyCode.W);
    }
    public void registerCommand(GameCommand command, boolean requiresActiveGame, KeyCode... keyCodes) {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(keyCodes, "keyCodes cannot be null");
        Map<KeyCode, GameCommand> targetMap = requiresActiveGame ? gameplayCommands : globalCommands;
        for (KeyCode keyCode : keyCodes) {
            targetMap.put(keyCode, command);
        }
    }
}
