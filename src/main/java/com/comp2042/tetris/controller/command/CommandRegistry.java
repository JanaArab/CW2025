package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.GameActionInvoker;
import com.comp2042.tetris.controller.InputCommandRegistrar;

public interface CommandRegistry {
    void registerCommands(GameActionInvoker gameActionInvoker,
                          InputCommandRegistrar commandRegistrar,
                          InputCommandFactory inputCommandFactory);
}
