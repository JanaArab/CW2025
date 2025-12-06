package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Command to move the current brick one cell down (soft drop).
 * Implements the Command pattern for decoupled input handling.
 *
 * @see GameCommand
 * @see GameActionInvoker#moveDown()
 */
public class MoveDownCommand implements GameCommand{

    private final GameActionInvoker gameActionInvoker;

    /**
     * Constructs a MoveDownCommand with the specified action invoker.
     *
     * @param gameActionInvoker the invoker to execute the move down action
     */
    public MoveDownCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        gameActionInvoker.moveDown();
    }


}
