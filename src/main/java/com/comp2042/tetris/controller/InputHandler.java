package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.command.GameCommand;
import com.comp2042.tetris.controller.command.InputCommandFactory;
import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;
import java.util.Objects;
import java.util.Map;
import java.util.function.BooleanSupplier;
public class InputHandler implements GameActionInvoker, InputCommandRegistrar {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputHandler.class);
    private final Map<KeyCode, GameCommand> gameplayCommands = new EnumMap<>(KeyCode.class);
    private final Map<KeyCode, GameCommand> globalCommands = new EnumMap<>(KeyCode.class);
    private final BooleanSupplier isPauseSupplier;
    private final BooleanSupplier isGameOverSupplier;
    private final CommandRegistry commandRegistry;
    private final InputCommandFactory inputCommandFactory;
    private  IGameController gameController;
    // When true, left/right inputs are inverted (left -> right, right -> left).
    private volatile boolean invertHorizontal = false;

    public InputHandler(BooleanSupplier isPauseSupplier,
                        BooleanSupplier isGameOverSupplier,
                        CommandRegistry commandRegistry,
                        InputCommandFactory inputCommandFactory) {
        this.commandRegistry = Objects.requireNonNull(commandRegistry, "commandRegistry");
        this.inputCommandFactory = Objects.requireNonNull(inputCommandFactory, "inputCommandFactory");
        this.isPauseSupplier = isPauseSupplier;
        this.isGameOverSupplier = isGameOverSupplier;
    }

    /**
     * Enable or disable horizontal inversion. When enabled, moveLeft() will trigger a right event and
     * moveRight() will trigger a left event. This is used by special levels (e.g. Level 3) to invert controls.
     */
    public void setInvertHorizontal(boolean invert) {
        this.invertHorizontal = invert;
    }

    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
        gameplayCommands.clear();
        commandRegistry.registerCommands(this, this, inputCommandFactory);
    }

    public boolean handle(KeyEvent keyEvent) {
        if (keyEvent == null)  {
            LOGGER.warn("Key event was null; ignoring input.");
            return false;
        }

        GameCommand command = globalCommands.get(keyEvent.getCode());
        if (command != null) {
            command.execute();
            keyEvent.consume();
            LOGGER.debug("Executed global command for key {}", keyEvent.getCode());
            return true;
        }

        if (isInteractionDisabled()) {
            LOGGER.debug("Interaction disabled; ignoring input for key {}", keyEvent.getCode());
            return false;
        }

        command = gameplayCommands.get(keyEvent.getCode());
        if (command == null) {
            LOGGER.debug("No command registered for key {}", keyEvent.getCode());
            return false;
        }

        command.execute();
        keyEvent.consume();
        LOGGER.debug("Executed gameplay command for key {}", keyEvent.getCode());
        return true;
    }

    @Override
    public void moveDown(EventSource source) {
        if (isInteractionDisabled()) {
            return;
        }
        if (gameController != null) {
            gameController.onDownEvent(new MoveEvent(EventType.DOWN, source));
        }
    }

    @Override
    public void moveDown() {
        moveDown(EventSource.USER);
    }

    @Override
    public void moveLeft() {
        if (gameController != null) {
            // If horizontal inversion is active, call the right event instead of left.
            if (invertHorizontal) {
                gameController.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            } else {
                gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            }
        }
    }

    @Override
    public void moveRight() {
        if (gameController != null) {
            // If horizontal inversion is active, call the left event instead of right.
            if (invertHorizontal) {
                gameController.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            } else {
                gameController.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            }
        }
    }

    @Override
    public void rotate() {
        if (gameController != null) {
            gameController.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        }
    }

    @Override
    public void instantDrop() {
        if (gameController != null) {
            gameController.onInstantDropEvent(new MoveEvent(EventType.DOWN, EventSource.USER)); // Using DOWN type, but it's instant
        }
    }

    private boolean isInteractionDisabled() {
        return (isPauseSupplier != null && isPauseSupplier.getAsBoolean())
                || (isGameOverSupplier != null && isGameOverSupplier.getAsBoolean());
    }



    @Override
    public void registerCommand(GameCommand command, boolean requiresActiveGame, KeyCode... keyCodes) {
        Objects.requireNonNull(command, "command cannot be null");
        Objects.requireNonNull(keyCodes, "keyCodes cannot be null");
        Map<KeyCode, GameCommand> targetMap = requiresActiveGame ? gameplayCommands : globalCommands;
        for (KeyCode keyCode : keyCodes) {
            targetMap.put(keyCode, command);
        }
    }
}
