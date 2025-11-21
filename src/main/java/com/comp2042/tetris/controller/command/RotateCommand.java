package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;
public class RotateCommand implements GameCommand{
    private final  GameActionInvoker gameActionInvoker;

    public RotateCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    @Override
    public void execute() {
        gameActionInvoker.rotate();
    }



}
