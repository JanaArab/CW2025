/**
 * updates the screen, notifications and game over
 * it is basically what the player sees
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameViewPresenter;
import com.comp2042.tetris.view.OverlayPanel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import com.comp2042.tetris.view.BackgroundAnimator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.Objects;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;

public class GuiController implements Initializable, IGuiController, GameEventListener {

    @FXML
    private Pane background1;

    @FXML
    private Pane background2;

    @FXML
    private HBox mainContent;

    @FXML//add button for pausing
    private Button pauseButton;

    @FXML
    private Label scoreLabel;

    @FXML //add label for pausing
    private Label pauseLabel;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private OverlayPanel gameOverPanel;

    @FXML
    private StackPane mainMenuOverlay;

    @FXML
    private VBox mainMenuCard;

    @FXML
    private VBox settingsPanel;

    private IGameController gameController;

    private AnimationHandler animationHandler;

    private InputHandler inputHandler;

    private BoardRenderer boardRenderer;

    private GameViewPresenter gameViewPresenter;

    private GuiControllerDependencies dependencies;

    private boolean gameActive = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFont();
        gameOverPanel.setVisible(false);
        setupGamePanelKeyListener();
        new BackgroundAnimator().animate(background1, background2);
        showMainMenu();
    }
    public void setDependencies(GuiControllerDependencies dependencies) {
        this.dependencies = Objects.requireNonNull(dependencies, "dependencies");
        this.animationHandler = dependencies.animationHandler();
        this.inputHandler = dependencies.inputHandler();
        this.boardRenderer = dependencies.boardRenderer();
        this.gameViewPresenter = dependencies.gameViewPresenter();
        configureInputCommands();
        updatePauseUi(false);

    }

    private void setupFont() {
        try {
            // Note the path: /fonts/PressStart2P.ttf
            Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 20);
            Font.loadFont(getClass().getClassLoader().getResource("fonts/digital.ttf").toExternalForm(), 38);
        } catch (Exception e) {
            System.out.println("Failed to load font: " + e.getMessage());
        }

    }

    private void setupGamePanelKeyListener() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);
    }
    private void configureInputCommands() {
        if (inputHandler == null) {
            return;
        }
        inputHandler.registerCommand(this::startNewGame, false, KeyCode.N);
        inputHandler.registerCommand(this::togglePauseState, false, KeyCode.P);
    }

    void handleTick() {
        if (!gameActive) {
            return;
        }
        if (inputHandler != null) {
            inputHandler.moveDown(EventSource.THREAD);
        }
        gamePanel.requestFocus();
    }

    private void handleKeyInput(KeyEvent keyEvent) {
        if (inputHandler != null) {
            inputHandler.handle(keyEvent);
        }
        gamePanel.requestFocus();
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.initializeGame(new GameStateSnapshot(boardMatrix, brick));
        }
        if (animationHandler != null) {
            animationHandler.start();
            updatePauseUi(false);
        }
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.refreshBoard(board);
        }
    }

    private void updatePauseUi(boolean paused) {
        if (pauseLabel != null) {
            pauseLabel.setVisible(paused);
        }
        if (pauseButton != null) {
            pauseButton.setText(paused ? "Resume" : "Pause");
        }
    }

    @Override
    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
        if (inputHandler != null) {
            inputHandler.setGameController(gameController);
        }
    }

    @Override
    public void gameOver() {
        ensureConfigured();
        gameActive = false;
        if (animationHandler != null) {
            animationHandler.onGameOver();
        }
        if (gameViewPresenter != null) {
            gameViewPresenter.showGameOver();
        }
        updatePauseUi(false);
        // Hide pause button when game is over
        if (pauseButton != null) {
            pauseButton.setVisible(false);
            pauseButton.setManaged(false);
        }
    }
    public void newGame(ActionEvent actionEvent) {
        startNewGame();
    }
    public void pauseGame(ActionEvent actionEvent) {
        togglePauseState();
    }

    @FXML
    private void handleStartGame(ActionEvent actionEvent) {
        hideMainMenu();
        startNewGame();
    }

    @FXML
    private void openSettings(ActionEvent actionEvent) {
        setNodeVisibility(mainMenuCard, false);
        setNodeVisibility(settingsPanel, true);
    }

    @FXML
    private void closeSettings(ActionEvent actionEvent) {
        setNodeVisibility(settingsPanel, false);
        setNodeVisibility(mainMenuCard, true);
    }

    @FXML
    private void exitGame(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void startNewGame() {
        ensureConfigured();
        if (animationHandler == null) {
            return;
        }
        if (gameViewPresenter != null) {
            gameViewPresenter.hideGameOver();
        }
        updatePauseUi(false);

        // Show pause button when game starts
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
        }

        gameActive = true;
        animationHandler.ensureInitialized();
        if (gameController != null) {
            gameController.createNewGame();
        }
        animationHandler.start();
        gamePanel.requestFocus();
    }

    private void showMainMenu() {
        gameActive = false;
        // Pause the game if it's running (not already paused and not game over)
        if (animationHandler != null && !animationHandler.isGameOver() && !animationHandler.isPaused()) {
            animationHandler.togglePause();
        }
        setNodeVisibility(mainMenuOverlay, true);
        setNodeVisibility(mainMenuCard, true);
        setNodeVisibility(settingsPanel, false);
        setMainContentVisible(false);
        if (pauseButton != null) {
            pauseButton.setVisible(false);
            pauseButton.setManaged(false);
        }
    }

    private void hideMainMenu() {
        setNodeVisibility(mainMenuOverlay, false);
        setNodeVisibility(settingsPanel, false);
        setNodeVisibility(mainMenuCard, true);
        setMainContentVisible(true);
    }

    private void setMainContentVisible(boolean visible) {
        if (mainContent == null) {
            return;
        }

        // Keep layout space so the controller frame never shifts, but hide visuals and input when menu is up
        mainContent.setManaged(true);
        mainContent.setVisible(true);
        mainContent.setMouseTransparent(!visible);
        mainContent.setOpacity(visible ? 1.0 : 0.0);
        mainContent.requestLayout();
    }

    private void setNodeVisibility(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }

    private void togglePauseState() {
        ensureConfigured();
        if (animationHandler == null || animationHandler.isGameOver()) {
            return;
        }

        animationHandler.togglePause();
        updatePauseUi(animationHandler.isPaused());
        gamePanel.requestFocus();
    }

    @Override
    public void onGameInitialized(GameStateSnapshot snapshot) {
        initGameView(snapshot.boardMatrix(), snapshot.viewData());
    }

    @Override
    public void onScoreChanged(ScoreChangeEvent event) {
        if (gameViewPresenter != null) {
            gameViewPresenter.updateScore(event);
        }
    }

    @Override
    public void onBrickUpdated(ViewData viewData) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            boolean paused = animationHandler != null && animationHandler.isPaused();
            gameViewPresenter.refreshBrick(viewData, paused);
        }
    }
    @Override
    public void onBoardUpdated(int[][] boardMatrix) {
        ensureConfigured();
        refreshGameBackground(boardMatrix);
    }
    @Override
    public void onLinesCleared(ClearRow clearRow) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.handleLinesCleared(clearRow);
        }
    }
    @Override
    public void onGameOver() {
        gameOver();
    }
    private void ensureConfigured() {
        if (dependencies == null) {
            throw new IllegalStateException("GuiController dependencies have not been configured. Inject dependencies before use.");
        }
    }

    GridPane getGamePanel() {
        return gamePanel;
    }

    GridPane getNextBrickPanel() {
        return nextBrickPanel;
    }

    GridPane getBrickPanel() {
        return brickPanel;
    }

    Label getScoreLabel() {
        return scoreLabel;
    }

    Group getGroupNotification() {
        return groupNotification;
    }

    OverlayPanel getGameOverPanel() {
        return gameOverPanel;
    }
}
