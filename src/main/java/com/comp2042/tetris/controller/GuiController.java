/**
 * updates the screen, notifications and game over
 * it is basically what the player sees
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.DownData;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.EventType;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.score.NotificationPanel;
import com.comp2042.tetris.view.GameOverPanel;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

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

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //I refactored this block of code to make it easier to read
        //split initialize into smaller helper methods to make it clearer
        setupFont();
        setupGamePanelKeyListener();
        // setupReflectionEffect();
        gameOverPanel.setVisible(false);
    }

    private void setupFont(){
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
    }

    /*private void setupReflectionEffect(){
    commenting this method for now because it is not used and the effect is not nice
        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        if (scoreLabel != null) {
            scoreLabel.setEffect(reflection);
        }
    }*/
    private void setupGamePanelKeyListener(){

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);

    }



    private void handleKeyInput(KeyEvent keyEvent) {//changed the name of method to make it clearer F
        //replaced the huge nested if statement with a switch to make the code more visible,clean and organized
        if (isPause.get() || isGameOver.get()) return;

        switch (keyEvent.getCode()) {
            case LEFT, A -> handleLeftMove();//used helper methods to handle input logic
            case RIGHT, D -> handleRightMove();
            case UP, W -> handleRotate();
            case DOWN, S -> handleDown();
            case N -> newGame(null);
            case P -> pauseGame(null);
            default -> {}
        }
        keyEvent.consume();
    }

    private void handleLeftMove() {
        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
    }

    private void handleRightMove() {
        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
    }

    private void handleRotate() {
        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
    }

    private void handleDown() {
        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
    }




    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = createTile(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i-2 );
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = createTile((Color) getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(BOARD_TOP_OFFSET + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);

        startTimeline();
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(BOARD_TOP_OFFSET + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));

    }

    /**
     * Helper to create a tile rectangle with consistent size, corner arcs and fill color instead of having repeated lines of
     * code in multiple methods it is in one place
     */
    private Rectangle createTile(Color color) {
        Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        rectangle.setFill(color);
        return rectangle;
    }

    private void startTimeline(){
        //created a startTimeline method to make the code cleaner
        if(timeLine != null){
            timeLine.stop();
        }
        timeLine = new Timeline(new KeyFrame(
                 Duration.millis(400),
                actionEvent -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.clearRow() != null && downData.clearRow().linesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.clearRow().scoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.viewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if(scoreLabel == null){ //throw null exception in case  scoreLabel is not initialized
            throw new IllegalStateException("scoreLabel is null. ");
        }
        scoreLabel.textProperty().unbind();//prevent double binding when restarting the game
        scoreLabel.textProperty().bind(integerProperty.asString());
        //it exists to bind the score updates with gui(view for player) and added a scoreLabel to keep the changes
    }

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
        eventListener.createNewGame();
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


}
