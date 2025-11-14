/**
 * updates the screen, notifications and game over
 * it is basically what the player sees
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameOverPanel;
import com.comp2042.tetris.view.NotificationPanel;
import com.comp2042.tetris.view.UIConstants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, IGuiController, GameEventListener {


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
    private GameOverPanel gameOverPanel;

    private IGameController gameController;

    private final GameStateManager gameStateManager = new GameStateManager();

    private AnimationHandler animationHandler;

    private InputHandler inputHandler;

    private BoardRenderer boardRenderer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFont();
        gameOverPanel.setVisible(false);
        animationHandler = new AnimationHandler(gameStateManager, UIConstants.DEFAULT_DROP_INTERVAL, this::handleTick);
        inputHandler = new InputHandler(animationHandler::isPaused, animationHandler::isGameOver);
        configureInputCommands();
        setupGamePanelKeyListener();

        boardRenderer = new BoardRenderer(gamePanel, brickPanel, UIConstants.BRICK_SIZE, UIConstants.BOARD_TOP_OFFSET);

    }

    private void setupFont() {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
    }

    private void setupGamePanelKeyListener() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);
    }
    private void configureInputCommands() {
        inputHandler.registerCommand(this::startNewGame, false, KeyCode.N);
        inputHandler.registerCommand(this::togglePauseState, false, KeyCode.P);
    }

    private void handleTick() {
        if (inputHandler != null) {
            inputHandler.handleDown(EventSource.THREAD);
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
        if (boardRenderer != null) {
            boardRenderer.initialize(boardMatrix, brick);
        }
        if (animationHandler != null) {
            animationHandler.start();
            updatePauseUi(false);
        }
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        if (boardRenderer != null) {
            boardRenderer.refreshGameBackground(board);
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
        if (animationHandler != null) {
            animationHandler.onGameOver();
        }
        gameOverPanel.setVisible(true);
        updatePauseUi(false);
    }
    public void newGame(ActionEvent actionEvent) {
        startNewGame();
    }
    public void pauseGame(ActionEvent actionEvent) {
        togglePauseState();
    }

    private void startNewGame() {
        if (animationHandler == null) {
            return;
        }
        gameOverPanel.setVisible(false);
        updatePauseUi(false);

        animationHandler.ensureInitialized();
        if (gameController != null) {
            gameController.createNewGame();
        }
        animationHandler.start();
        gamePanel.requestFocus();
    }

    private void togglePauseState() {
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
        if (scoreLabel == null) {
            throw new IllegalStateException("scoreLabel is null.");
        }
        Platform.runLater(() -> scoreLabel.setText(String.valueOf(event.newScore())));
    }

    @Override
    public void onBrickUpdated(ViewData viewData) {
        if (boardRenderer != null) {
            boolean paused = animationHandler != null && animationHandler.isPaused();
            boardRenderer.refreshBrick(viewData, paused);
        }
    }
    @Override
    public void onBoardUpdated(int[][] boardMatrix) {
        refreshGameBackground(boardMatrix);
    }
    @Override
    public void onLinesCleared(ClearRow clearRow) {
        if (clearRow.linesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.scoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }
    @Override
    public void onGameOver() {
        gameOver();
    }
}