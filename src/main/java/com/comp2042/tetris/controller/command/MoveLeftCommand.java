package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Command to move the current brick one cell to the left.
 * Implements the Command pattern for decoupled input handling.
 *
 * @see GameCommand
 * @see GameActionInvoker#moveLeft()
 */
public class MoveLeftCommand implements GameCommand{
    private final GameActionInvoker gameActionInvoker;

    /**
     * Constructs a MoveLeftCommand with the specified action invoker.
     *
     * @param gameActionInvoker the invoker to execute the move left action
     */
    public MoveLeftCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        gameActionInvoker.moveLeft();
    }
}
