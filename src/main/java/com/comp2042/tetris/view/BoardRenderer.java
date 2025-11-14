package com.comp2042.tetris.view;


import com.comp2042.tetris.model.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class BoardRenderer {

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final int brickSize;
    private final int boardTopOffset;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] previewMatrix;

    public BoardRenderer(GridPane gamePanel, GridPane brickPanel, int brickSize, int boardTopOffset) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.brickSize = brickSize;
        this.boardTopOffset = boardTopOffset;
    }

    public void initialize(int[][] boardMatrix, ViewData brick) {
        initializeBoard(boardMatrix);
        initializePreview(brick);
        updateBrickPosition(brick);
    }

    public void refreshBrick(ViewData brick, boolean isPaused) {
        if (isPaused || previewMatrix == null) {
            return;
        }

        updateBrickPosition(brick);
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], previewMatrix[i][j]);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        if (displayMatrix == null) {
            return;
        }
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void initializeBoard(int[][] boardMatrix) {
        gamePanel.getChildren().clear();
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = createTile(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
    }

    private void initializePreview(ViewData brick) {
        brickPanel.getChildren().clear();
        previewMatrix = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = createTile(getFillColor(brick.getBrickData()[i][j]));
                previewMatrix[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    private void updateBrickPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getXPosition() * brickPanel.getVgap() + brick.getXPosition() * brickSize);
        brickPanel.setLayoutY(boardTopOffset + gamePanel.getLayoutY() + brick.getYPosition() * brickPanel.getHgap() + brick.getYPosition() * brickSize);
    }

    private Rectangle createTile(Color color) {
        Rectangle rectangle = new Rectangle(brickSize, brickSize);
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        rectangle.setFill(color);
        return rectangle;
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        if (rectangle != null) {
            rectangle.setFill(getFillColor(color));
        }
    }

    private Color getFillColor(int value) {
        return switch (value) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }
}


