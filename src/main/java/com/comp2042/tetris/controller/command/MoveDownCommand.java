package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;
public class MoveDownCommand implements GameCommand{

    private final InputHandler inputHandler;

    public MoveDownCommand(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        inputHandler.handleDown();
    }


}
