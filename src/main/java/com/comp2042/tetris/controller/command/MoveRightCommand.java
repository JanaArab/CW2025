package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Command to move the current brick one cell to the right.
 * Implements the Command pattern for decoupled input handling.
 *
 * @see GameCommand
 * @see GameActionInvoker#moveRight()
 */
public class MoveRightCommand implements GameCommand{
    private final GameActionInvoker gameActionInvoker;

    /**
     * Constructs a MoveRightCommand with the specified action invoker.
     *
     * @param gameActionInvoker the invoker to execute the move right action
     */
    public MoveRightCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        gameActionInvoker.moveRight();
    }
}
