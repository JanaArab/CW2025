package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.core.InputHandler;
import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameViewPresenter;
import com.comp2042.tetris.view.VisualEffectsManager;

/**
 * Record containing all dependencies required by the GuiController.
 * Encapsulates the various handlers, controllers, and renderers needed for the game UI.
 *
 * @param animationHandler handles game loop and animation timing
 * @param inputHandler processes keyboard input
 * @param inputController centralizes input handling logic
 * @param commandRegistry registers command-key bindings
 * @param boardRenderer renders the game board and bricks
 * @param gameViewPresenter manages view updates and notifications
 * @param visualEffectsManager handles visual effects like stars and explosions
 *
 * @see GuiController
 * @see DefaultGuiControllerDependenciesFactory
 */
public record GuiControllerDependencies(
        AnimationHandler animationHandler,
        InputHandler inputHandler,
        InputController inputController,
        CommandRegistry commandRegistry,
        BoardRenderer boardRenderer,
        GameViewPresenter gameViewPresenter,
        VisualEffectsManager visualEffectsManager
) {

}
