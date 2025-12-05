package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;
public class MoveLeftCommand implements GameCommand{
    private final GameActionInvoker gameActionInvoker;

    public MoveLeftCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    @Override
    public void execute() {
        gameActionInvoker.moveLeft();
    }
}
