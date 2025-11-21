package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;

public class MoveRightCommand implements GameCommand{
    private final GameActionInvoker gameActionInvoker;

    public MoveRightCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }
    @Override
    public void execute() {
        gameActionInvoker.moveRight();
    }
}
