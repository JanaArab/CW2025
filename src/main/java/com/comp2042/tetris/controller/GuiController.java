/**
 * updates the screen, notifications and game over
 * it is basically what the player sees
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.EventSource;

import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameOverPanel;
import com.comp2042.tetris.view.NotificationPanel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, IGuiController, GameEventListener {

    //replaced the numbers in code with actual constants to amke editing easier
    private static final int BRICK_SIZE = 20; //size of each tile
    private static final int BOARD_TOP_OFFSET = -42; //offset to align the board in gui

    @FXML//add button for pausing
    private Button pauseButton ;

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



    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private final InputHandler inputHandler = new InputHandler(isPause::get, isGameOver::get);

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

    private void setupFont(){
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
    }


    private void setupGamePanelKeyListener(){

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);

    }



    private void handleKeyInput(KeyEvent keyEvent) {
        //changed the name of method to make it clearer F
        boolean  handled = inputHandler.handle(keyEvent);
        if(! handled) {
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
        if(boardRenderer != null){
            boardRenderer.initialize(boardMatrix, brick);
        }

        startTimeline();
    }


   @Override
   public void refreshGameBackground(int[][] board) {
        if(boardRenderer != null){
            boardRenderer.refreshGameBackground(board);
            }
        }



    private void startTimeline(){
        //created a startTimeline method to make the code cleaner
        if(timeLine != null){
            timeLine.stop();
        }
        timeLine = new Timeline(new KeyFrame(
                 Duration.millis(400),
                actionEvent -> {
                     inputHandler.handleDown(EventSource.THREAD);
                     gamePanel.requestFocus();}
        ));

        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }


    @Override
    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
        inputHandler.setGameController(gameController);
    }

    @Override
    public void bindScore(IntegerProperty integerProperty) {
        if(scoreLabel == null){ //throw null exception in case  scoreLabel is not initialized
            throw new IllegalStateException("scoreLabel is null. ");
        }
        scoreLabel.textProperty().unbind();//prevent double binding when restarting the game
        scoreLabel.textProperty().bind(integerProperty.asString());
        //it exists to bind the score updates with gui(view for player) and added a scoreLabel to keep the changes
    }

    @Override
    public void gameOver() {
        if(timeLine!= null){
            timeLine.stop();
        }

        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }


    public void newGame(ActionEvent actionEvent) {
        if(timeLine!= null){
            timeLine.stop();
        }
        gameOverPanel.setVisible(false);
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        if(gameController != null){
            gameController.createNewGame();
        }
        startTimeline();
        gamePanel.requestFocus();

    }



    public void pauseGame(ActionEvent actionEvent) {
        //this is implied to be able to pause with a pause button
        if (timeLine == null) return;
        if (!isPause.getValue()) {
            timeLine.pause();
            isPause.setValue(true);
            if (pauseLabel != null) pauseLabel.setVisible(true);
            if (pauseButton != null) pauseButton.setText("Resume");
        } else {
            timeLine.play();
            isPause.setValue(false);
            if (pauseLabel != null) pauseLabel.setVisible(false);
            if (pauseButton != null) pauseButton.setText("Pause");
        }
        gamePanel.requestFocus();
    }

    @Override
    public void onGameInitialized(GameStateSnapshot snapshot, IntegerProperty scoreProperty) {
        initGameView(snapshot.boardMatrix(), snapshot.viewData());
        bindScore(scoreProperty);
    }

    @Override
    public void onBrickUpdated(ViewData viewData) {
       if (boardRenderer != null){
           boardRenderer.refreshBrick(viewData, isPause.getValue());
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
