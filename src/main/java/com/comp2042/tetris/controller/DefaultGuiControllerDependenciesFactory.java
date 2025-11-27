package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.command.CommandRegistry;
import com.comp2042.tetris.controller.command.DefaultCommandRegistry;
import com.comp2042.tetris.controller.command.DefaultInputCommandFactory;
import com.comp2042.tetris.controller.command.InputCommandFactory;
import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.game.GameStateManager;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameViewPresenter;
import com.comp2042.tetris.view.NotificationAnimator;
import com.comp2042.tetris.view.OverlayPanel;
import com.comp2042.tetris.view.UIConstants;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class DefaultGuiControllerDependenciesFactory {
    public GuiControllerDependencies create(GuiController controller) {
        Objects.requireNonNull(controller, "controller");
        GridPane gamePanel = controller.getGamePanel();
        GridPane brickPanel = controller.getBrickPanel();
        GridPane ghostBrickPanel = controller.getGhostBrickPanel();
        GridPane nextBrickPanel = controller.getNextBrickPanel();
        Label scoreLabel = controller.getScoreLabel();
        Group groupNotification = controller.getGroupNotification();
        OverlayPanel gameOverPanel = controller.getGameOverPanel();

        GameStateManager gameStateManager = new GameStateManager();
        AnimationHandler animationHandler = new AnimationHandler(
                gameStateManager,
                UIConstants.DEFAULT_DROP_INTERVAL,
                controller::handleTick
        );

        CommandRegistry commandRegistry = new DefaultCommandRegistry();
        InputCommandFactory inputCommandFactory = new DefaultInputCommandFactory();
        InputHandler inputHandler = new InputHandler(
                animationHandler::isPaused,
                animationHandler::isGameOver,
                commandRegistry,
                inputCommandFactory);

        BoardRenderer boardRenderer = new BoardRenderer(gamePanel, brickPanel, ghostBrickPanel, nextBrickPanel, UIConstants.BRICK_SIZE);
        NotificationAnimator notificationAnimator = new NotificationAnimator();
        GameViewPresenter gameViewPresenter = new GameViewPresenter(
                boardRenderer,
                scoreLabel,
                groupNotification,
                gameOverPanel,
                notificationAnimator);

        return new GuiControllerDependencies(animationHandler, inputHandler, commandRegistry, boardRenderer, gameViewPresenter);
    }

}
