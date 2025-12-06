package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.core.InputHandler;
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
import com.comp2042.tetris.view.VisualEffectsManager;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Factory class for creating GuiController dependencies.
 * Initializes and wires together all the components needed by GuiController.
 *
 * <p>Creates and configures:</p>
 * <ul>
 *   <li>AnimationHandler for game loop</li>
 *   <li>InputHandler and InputController for keyboard input</li>
 *   <li>BoardRenderer for visual display</li>
 *   <li>GameViewPresenter for UI updates</li>
 *   <li>VisualEffectsManager for special effects</li>
 * </ul>
 *
 * @see GuiControllerDependencies
 * @see GuiController
 */
public class DefaultGuiControllerDependenciesFactory {

    /**
     * Creates all dependencies for the GuiController.
     *
     * @param controller the GuiController to create dependencies for
     * @return a GuiControllerDependencies containing all required components
     * @throws NullPointerException if controller is null
     */
    public GuiControllerDependencies create(GuiController controller) {
        Objects.requireNonNull(controller, "controller");
        GridPane gamePanel = controller.getGamePanel();
        GridPane brickPanel = controller.getBrickPanel();
        GridPane ghostBrickPanel = controller.getGhostBrickPanel();
        GridPane nextBrickPanel = controller.getNextBrickPanel();
        Label scoreLabel = controller.getScoreLabel();
        Group groupNotification = controller.getGroupNotification();
        OverlayPanel gameOverPanel = controller.getGameOverPanel();
        BorderPane gameBoard = controller.getGameBoard();
        Pane pixelStarLayer = controller.getPixelStarLayer();
        Pane mainContent = controller.getMainContent();

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

        InputController inputController = new InputController(inputHandler);

        BoardRenderer boardRenderer = new BoardRenderer(gamePanel, brickPanel, ghostBrickPanel, nextBrickPanel, UIConstants.BRICK_SIZE);
        NotificationAnimator notificationAnimator = new NotificationAnimator();
        GameViewPresenter gameViewPresenter = new GameViewPresenter(
                boardRenderer,
                scoreLabel,
                groupNotification,
                gameOverPanel,
                notificationAnimator);

        VisualEffectsManager visualEffectsManager = new VisualEffectsManager(
                pixelStarLayer,
                mainContent,
                gamePanel
        );

        return new GuiControllerDependencies(
                animationHandler,
                inputHandler,
                inputController,
                commandRegistry,
                boardRenderer,
                gameViewPresenter,
                visualEffectsManager
        );
    }

}
