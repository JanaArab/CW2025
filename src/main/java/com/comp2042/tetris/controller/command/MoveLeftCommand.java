package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;

public class MoveLeftCommand implements GameCommand{
    private final InputHandler inputHandler;

    public MoveLeftCommand(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        inputHandler.handleLeftMove();
    }
}
