package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;
public interface InputCommandFactory {

    GameCommand createMoveLeftCommand(GameActionInvoker gameActionInvoker);

    GameCommand createMoveRightCommand(GameActionInvoker gameActionInvoker);

    GameCommand createMoveDownCommand(GameActionInvoker gameActionInvoker);

    GameCommand createRotateCommand(GameActionInvoker gameActionInvoker);

    GameCommand createInstantDropCommand(GameActionInvoker gameActionInvoker);
}
