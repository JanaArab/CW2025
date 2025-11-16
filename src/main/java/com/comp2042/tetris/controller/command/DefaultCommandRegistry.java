package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;
import com.comp2042.tetris.controller.InputCommandRegistrar;
import javafx.scene.input.KeyCode;

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
    }
}
