package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;
import com.comp2042.tetris.controller.core.InputCommandRegistrar;
import javafx.scene.input.KeyCode;

/**
 * Default implementation of {@link CommandRegistry} that registers standard Tetris controls.
 * Maps keyboard keys to game commands for movement, rotation, and instant drop.
 *
 * <p>Default key bindings:</p>
 * <ul>
 *   <li>Left/A - Move left</li>
 *   <li>Right/D - Move right</li>
 *   <li>Down/S - Soft drop</li>
 *   <li>Up/W - Rotate</li>
 *   <li>Space - Hard drop (instant)</li>
 * </ul>
 *
 * @see CommandRegistry
 * @see GameCommand
 */
public class DefaultCommandRegistry implements CommandRegistry {
    @Override
    public void registerCommands(GameActionInvoker gameActionInvoker,
                                 InputCommandRegistrar commandRegistrar,
                                 InputCommandFactory inputCommandFactory) {
        GameCommand leftCommand = inputCommandFactory.createMoveLeftCommand(gameActionInvoker);
        GameCommand rightCommand = inputCommandFactory.createMoveRightCommand(gameActionInvoker);
        GameCommand downCommand = inputCommandFactory.createMoveDownCommand(gameActionInvoker);
        GameCommand rotateCommand = inputCommandFactory.createRotateCommand(gameActionInvoker);

        commandRegistrar.registerCommand(leftCommand, true, KeyCode.LEFT, KeyCode.A);
        commandRegistrar.registerCommand(rightCommand, true, KeyCode.RIGHT, KeyCode.D);
        commandRegistrar.registerCommand(downCommand, true, KeyCode.DOWN, KeyCode.S);
        commandRegistrar.registerCommand(rotateCommand, true, KeyCode.UP, KeyCode.W);

        GameCommand instantDropCommand = inputCommandFactory.createInstantDropCommand(gameActionInvoker);
        commandRegistrar.registerCommand(instantDropCommand, true, KeyCode.SPACE);
    }
}
