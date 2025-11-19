package com.comp2042.tetris.view;


import com.comp2042.tetris.model.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class BoardRenderer {

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane nextBrickPanel;
    private final int brickSize;
    private final int boardTopOffset;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] activeBrickMatrix;
    private Rectangle[][] nextBrickMatrix;

    public BoardRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextBrickPanel, int brickSize, int boardTopOffset) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.nextBrickPanel = nextBrickPanel;
        this.brickSize = brickSize;
        this.boardTopOffset = boardTopOffset;
    }

    public void initialize(int[][] boardMatrix, ViewData brick) {
        initializeBoard(boardMatrix);
        initializeActiveBrick(brick.getBrickData());
        initializeNextBrickPreview(brick.getNextBrickData());
        updateBrickPosition(brick);
    }

    public void refreshBrick(ViewData brick, boolean isPaused) {
        if (isPaused || activeBrickMatrix == null) {
            return;
        }

        updateBrickPosition(brick);
        refreshActiveBrick(brick.getBrickData());
        refreshNextBrick(brick.getNextBrickData());
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

    private void initializeActiveBrick(int[][] brickData) {
        brickPanel.getChildren().clear();
        activeBrickMatrix = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = createTile(getFillColor(brickData[i][j]));
                activeBrickMatrix[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    private void initializeNextBrickPreview(int[][] nextBrickData) {
        if (nextBrickPanel == null) {
            return;
        }

        nextBrickPanel.getChildren().clear();
        nextBrickMatrix = new Rectangle[nextBrickData.length][nextBrickData[0].length];
        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = createTile(getFillColor(nextBrickData[i][j]));
                nextBrickMatrix[i][j] = rectangle;
                nextBrickPanel.add(rectangle, j, i);
            }
        }
    }

    private void refreshActiveBrick(int[][] brickData) {
        if (!dimensionsMatch(activeBrickMatrix, brickData)) {
            initializeActiveBrick(brickData);
            return;
        }

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                setRectangleData(brickData[i][j], activeBrickMatrix[i][j]);
            }
        }
    }

    private void refreshNextBrick(int[][] nextBrickData) {
        if (nextBrickPanel == null) {
            return;
        }

        if (!dimensionsMatch(nextBrickMatrix, nextBrickData)) {
            initializeNextBrickPreview(nextBrickData);
            return;
        }

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                setRectangleData(nextBrickData[i][j], nextBrickMatrix[i][j]);
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

    private boolean dimensionsMatch(Rectangle[][] matrix, int[][] data) {
        return matrix != null && matrix.length == data.length && matrix[0].length == data[0].length;
    }

    private Color getFillColor(int value) {
        return switch (value) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.CYAN;
            case 2 -> Color.DODGERBLUE;
            case 3 -> Color.ORANGE;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.LIMEGREEN;
            case 7 -> Color.MAGENTA;
            default -> Color.WHITE;
        };
    }
}


