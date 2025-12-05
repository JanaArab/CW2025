package com.comp2042.tetris.controller.core;


import com.comp2042.tetris.controller.command.GameCommand;
import javafx.scene.input.KeyCode;

public interface InputCommandRegistrar {
    void registerCommand(GameCommand command, boolean requiresActiveGame, KeyCode... keyCodes);
}
