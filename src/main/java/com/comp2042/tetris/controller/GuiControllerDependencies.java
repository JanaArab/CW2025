package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameViewPresenter;

public record GuiControllerDependencies(
        AnimationHandler animationHandler,
        InputHandler inputHandler,
        CommandRegistry commandRegistry,
        BoardRenderer boardRenderer,
        GameViewPresenter gameViewPresenter
) {

}
