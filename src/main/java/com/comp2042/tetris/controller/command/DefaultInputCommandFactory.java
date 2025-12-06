package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;

/**
 * Default implementation of {@link InputCommandFactory}.
 * Creates concrete command instances for all game actions.
 *
 * @see InputCommandFactory
 * @see GameCommand
 */
public class DefaultInputCommandFactory implements InputCommandFactory  {
    @Override
    public GameCommand createMoveLeftCommand(GameActionInvoker gameActionInvoker) {
        return new MoveLeftCommand(gameActionInvoker);
    }

    @Override
    public GameCommand createMoveRightCommand(GameActionInvoker gameActionInvoker) {
        return new MoveRightCommand(gameActionInvoker);
    }

    @Override
    public GameCommand createMoveDownCommand(GameActionInvoker gameActionInvoker) {
        return new MoveDownCommand(gameActionInvoker);
    }

    @Override
    public GameCommand createRotateCommand(GameActionInvoker gameActionInvoker) {
        return new RotateCommand(gameActionInvoker);
    }

    @Override
    public GameCommand createInstantDropCommand(GameActionInvoker gameActionInvoker) {
        return new InstantDropCommand(gameActionInvoker);
    }
}
