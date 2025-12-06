package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Command to instantly drop the current brick to the bottom (hard drop).
 * Implements the Command pattern for decoupled input handling.
 *
 * @see GameCommand
 * @see GameActionInvoker#instantDrop()
 */
public class InstantDropCommand implements GameCommand {

    private final GameActionInvoker gameActionInvoker;

    /**
     * Constructs an InstantDropCommand with the specified action invoker.
     *
     * @param gameActionInvoker the invoker to execute the instant drop action
     */
    public InstantDropCommand(GameActionInvoker gameActionInvoker) {
        this.gameActionInvoker = gameActionInvoker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        gameActionInvoker.instantDrop();
    }
}
