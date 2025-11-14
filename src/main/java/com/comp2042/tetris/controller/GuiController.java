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
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, IGuiController, GameEventListener {

    //replaced the numbers in code with actual constants to amke editing easier
    private static final int BRICK_SIZE = 20; //size of each tile
    private static final int BOARD_TOP_OFFSET = -42; //offset to align the board in gui

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


    private GameLoop gameLoop;

    private final GameStateManager gameStateManager = new GameStateManager();

    private final InputHandler inputHandler = new InputHandler(gameStateManager::isPaused, gameStateManager::isGameOver);

    private BoardRenderer boardRenderer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //I refactored this block of code to make it easier to read
        //split initialize into smaller helper methods to make it clearer
        setupFont();
        setupGamePanelKeyListener();
        gameOverPanel.setVisible(false);
        boardRenderer = new BoardRenderer(gamePanel, brickPanel, BRICK_SIZE, BOARD_TOP_OFFSET);
    }

    private void setupFont() {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
    }


    private void setupGamePanelKeyListener() {

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);

    }


    private void handleKeyInput(KeyEvent keyEvent) {
        //changed the name of method to make it clearer F
        boolean handled = inputHandler.handle(keyEvent);
        if (!handled) {
            switch (keyEvent.getCode()) {
                case N -> {
                    newGame(null);
                    keyEvent.consume();
                }
                case P -> {
                    pauseGame(null);
                    keyEvent.consume();
                    // Do nothing for other keys
                }
                default -> {
                }
            }
        }

        gamePanel.requestFocus();
    }


    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        if (boardRenderer != null) {
            boardRenderer.initialize(boardMatrix, brick);
        }

        startGameLoop();
    }


    @Override
    public void refreshGameBackground(int[][] board) {
        if (boardRenderer != null) {
            boardRenderer.refreshGameBackground(board);
        }
    }


    private void startGameLoop() {
        //created a startTimeline method to make the code cleaner
        ensureGameLoopInitialized();
        gameStateManager.onGameStarted(gameLoop);
        updatePauseUi(false);
    }

    private void ensureGameLoopInitialized() {
        if (gameLoop == null) {
            gameLoop = new GameLoop(Duration.millis(400), () -> {
                inputHandler.handleDown(EventSource.THREAD);
                gamePanel.requestFocus();
            });
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
        public void setGameController (IGameController gameController){
            this.gameController = gameController;
            inputHandler.setGameController(gameController);
        }


        @Override
        public void gameOver () {
            gameStateManager.onGameOver(gameLoop);
            gameOverPanel.setVisible(true);
            updatePauseUi(false);
        }


        public void newGame (ActionEvent actionEvent){

            gameOverPanel.setVisible(false);
            updatePauseUi(false);
            ensureGameLoopInitialized();
            if (gameController != null) {
                gameController.createNewGame();
            }
            gameStateManager.onGameStarted(gameLoop);
            gamePanel.requestFocus();

        }

        public void pauseGame (ActionEvent actionEvent){
            if (gameLoop == null || gameStateManager.isGameOver()) return;
            gameStateManager.togglePause(gameLoop);
            updatePauseUi(gameStateManager.isPaused());
            gamePanel.requestFocus();
        }

        @Override
        public void onGameInitialized (GameStateSnapshot snapshot ){
            initGameView(snapshot.boardMatrix(), snapshot.viewData());
        }
        @Override
        public void onScoreChanged (ScoreChangeEvent event){
            if (scoreLabel == null) {
                throw new IllegalStateException("scoreLabel is null.");
            }
            Platform.runLater(() -> scoreLabel.setText(String.valueOf(event.newScore())));
        }

        @Override
        public void onBrickUpdated(ViewData viewData){
            if (boardRenderer != null) {
                boardRenderer.refreshBrick(viewData, gameStateManager.isPaused());
            }
        }

        @Override
        public void onBoardUpdated(int[][] boardMatrix){
            refreshGameBackground(boardMatrix);
        }

        @Override
        public void onLinesCleared(ClearRow clearRow){
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

