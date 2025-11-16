package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;
import javafx.scene.input.KeyCode;

public class DefaultCommandRegistry implements CommandRegistry {
    @Override
    public void registerCommands(InputHandler inputHandler) {
        GameCommand leftCommand = new MoveLeftCommand(inputHandler);
        GameCommand rightCommand = new MoveRightCommand(inputHandler);
        GameCommand downCommand = new MoveDownCommand(inputHandler);
        GameCommand rotateCommand = new RotateCommand(inputHandler);

        inputHandler.registerCommand(leftCommand, true, KeyCode.LEFT, KeyCode.A);
        inputHandler.registerCommand(rightCommand, true, KeyCode.RIGHT, KeyCode.D);
        inputHandler.registerCommand(downCommand, true, KeyCode.DOWN, KeyCode.S);
        inputHandler.registerCommand(rotateCommand, true, KeyCode.UP, KeyCode.W);
    }
}
