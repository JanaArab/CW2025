package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;
public class MoveDownCommand implements GameCommand{

    private final GameActionInvoker gameActionInvoker;

    public MoveDownCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    @Override
    public void execute() {
        gameActionInvoker.moveDown();
    }


}
