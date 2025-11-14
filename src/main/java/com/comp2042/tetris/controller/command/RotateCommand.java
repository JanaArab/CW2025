package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;
public class RotateCommand implements GameCommand{
    private final InputHandler inputHandler;

    public RotateCommand(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        inputHandler.handleRotate();
    }



}
