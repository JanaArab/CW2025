package com.comp2042.tetris.controller.command;

import com.comp2042.tetris.controller.InputHandler;

public interface CommandRegistry {
    void registerCommands(InputHandler inputHandler);
}
