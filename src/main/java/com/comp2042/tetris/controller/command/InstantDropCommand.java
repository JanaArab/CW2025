package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;

public class InstantDropCommand implements GameCommand {

    private final GameActionInvoker gameActionInvoker;

    public InstantDropCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    @Override
    public void execute() {
        gameActionInvoker.instantDrop();
    }
}
