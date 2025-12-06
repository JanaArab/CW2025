package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.core.GameActionInvoker;
import com.comp2042.tetris.controller.core.InputCommandRegistrar;

/**
 * Interface for registering game commands with the input system.
 * Implementations define the mapping between keyboard inputs and game actions.
 *
 * @see DefaultCommandRegistry
 * @see GameCommand
 */
public interface CommandRegistry {
    void registerCommands(GameActionInvoker gameActionInvoker,
                          InputCommandRegistrar commandRegistrar,
                          InputCommandFactory inputCommandFactory);
}
