package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Factory interface for creating game command instances.
 * Provides factory methods for all movement and action commands.
 *
 * @see DefaultInputCommandFactory
 * @see GameCommand
 */
public interface InputCommandFactory {

    /**
     * Creates a command to move the brick left.
     *
     * @param gameActionInvoker the invoker to execute the action
     * @return a new MoveLeftCommand instance
     */
    GameCommand createMoveLeftCommand(GameActionInvoker gameActionInvoker);

    /**
     * Creates a command to move the brick right.
     *
     * @param gameActionInvoker the invoker to execute the action
     * @return a new MoveRightCommand instance
     */
    GameCommand createMoveRightCommand(GameActionInvoker gameActionInvoker);

    /**
     * Creates a command to move the brick down (soft drop).
     *
     * @param gameActionInvoker the invoker to execute the action
     * @return a new MoveDownCommand instance
     */
    GameCommand createMoveDownCommand(GameActionInvoker gameActionInvoker);

    /**
     * Creates a command to rotate the brick.
     *
     * @param gameActionInvoker the invoker to execute the action
     * @return a new RotateCommand instance
     */
    GameCommand createRotateCommand(GameActionInvoker gameActionInvoker);

    /**
     * Creates a command to instantly drop the brick (hard drop).
     *
     * @param gameActionInvoker the invoker to execute the action
     * @return a new InstantDropCommand instance
     */
    GameCommand createInstantDropCommand(GameActionInvoker gameActionInvoker);
}
