package com.comp2042.tetris.controller.core;


import com.comp2042.tetris.controller.command.GameCommand;
import javafx.scene.input.KeyCode;

/**
 * Interface for registering game commands with key bindings.
 * Allows mapping keyboard keys to specific game commands.
 *
 * @see InputHandler
 * @see com.comp2042.tetris.controller.command.GameCommand
 */
public interface InputCommandRegistrar {

    /**
     * Registers a command with the specified key bindings.
     *
     * @param command the game command to register
     * @param requiresActiveGame true if the command should only work during active gameplay
     * @param keyCodes the keyboard keys that trigger this command
     */
    void registerCommand(GameCommand command, boolean requiresActiveGame, KeyCode... keyCodes);
}
