package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Command to rotate the current brick counterclockwise.
 * Implements the Command pattern for decoupled input handling.
 *
 * @see GameCommand
 * @see GameActionInvoker#rotate()
 */
public class RotateCommand implements GameCommand{
    private final  GameActionInvoker gameActionInvoker;

    /**
     * Constructs a RotateCommand with the specified action invoker.
     *
     * @param gameActionInvoker the invoker to execute the rotation action
     */
    public RotateCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        gameActionInvoker.rotate();
    }



}
