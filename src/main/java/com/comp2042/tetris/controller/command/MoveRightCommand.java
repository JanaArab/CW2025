package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;

public class MoveRightCommand implements GameCommand{
    private final InputHandler inputHandler;

    public MoveRightCommand(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }
    @Override
    public void execute() {
        inputHandler.handleRightMove();
    }
}
