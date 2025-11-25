package com.comp2042.tetris.view;


import com.comp2042.tetris.model.data.ViewData;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;


public class BoardRenderer {

    private static final int HIDDEN_TOP_ROWS = 2;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane nextBrickPanel;
    private final int brickSize;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] activeBrickMatrix;
    private Rectangle[][] nextBrickMatrix;

    // Cache the origin offset to prevent jitter from fluctuating scene bounds
    private Double cachedOriginX = null;
    private Double cachedOriginY = null;

    public BoardRenderer(GridPane gamePanel, GridPane brickPanel, GridPane nextBrickPanel, int brickSize) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.nextBrickPanel = nextBrickPanel;
        this.brickSize = brickSize;
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
                // Grid lines removed - no stroke on tiles
                rectangle.setStroke(null);
                rectangle.setStrokeWidth(0);
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
        if (!isLayerReady()) {
            Platform.runLater(() -> updateBrickPosition(brick));
            return;
        }

        // Calculate and cache the origin offset on first update
        // This prevents jitter from fluctuating scene bounds on subsequent updates
        if (cachedOriginX == null || cachedOriginY == null) {
            Bounds boardBounds = gamePanel.localToScene(gamePanel.getBoundsInLocal());
            Bounds layerBounds = brickPanel.getParent().localToScene(brickPanel.getParent().getBoundsInLocal());
            cachedOriginX = boardBounds.getMinX() - layerBounds.getMinX();
            cachedOriginY = boardBounds.getMinY() - layerBounds.getMinY();
        }

        // Calculate position using cached origin and logical brick coordinates
        // Since brick position is always an integer and step size is constant,
        // this produces stable, whole-pixel positions
        double xOffset = cachedOriginX + brick.getXPosition() * horizontalStep();
        double yOffset = cachedOriginY + hiddenRowsOffset() + brick.getYPosition() * verticalStep();

        // Round to whole pixels for pixel-perfect alignment
        long targetX = Math.round(xOffset);
        long targetY = Math.round(yOffset);

        // Only update if position actually changed to reduce layout thrashing
        if (brickPanel.getLayoutX() != targetX || brickPanel.getLayoutY() != targetY) {
            brickPanel.setLayoutX(targetX);
            brickPanel.setLayoutY(targetY);
        }
    }

    private boolean isLayerReady() {
        return gamePanel.getScene() != null
                && brickPanel.getParent() != null
                && brickPanel.getParent().getScene() != null;
    }

    private double horizontalStep() {
        return brickSize + brickPanel.getHgap();
    }

    private double verticalStep() {
        return brickSize + brickPanel.getVgap();
    }

    private double hiddenRowsOffset() {
        return -HIDDEN_TOP_ROWS * verticalStep();
    }

   /* private Rectangle createTile(Color color) {
        Rectangle rectangle = new Rectangle(brickSize, brickSize);
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        rectangle.setFill(color);
        // Enable snap-to-pixel for crisp rendering
        rectangle.setSmooth(false);
        return rectangle;
    }*/

    private Rectangle createTile(Color color) {
        Rectangle rectangle = new Rectangle(brickSize, brickSize);

        // Slightly smaller rounding for a cleaner, modern look
        rectangle.setArcHeight(8);
        rectangle.setArcWidth(8);
        rectangle.setSmooth(true);
        applyBrickStyling(rectangle, color);
        return rectangle;
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        if (rectangle != null) {
            applyBrickStyling(rectangle, getFillColor(color));
        }
    }

    private void applyBrickStyling(Rectangle rectangle, Color baseColor) {
        if (baseColor == null || baseColor == Color.TRANSPARENT) {
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setEffect(null);
            rectangle.setStroke(null);
            return;
        }

        rectangle.setFill(createBrickGradient(baseColor));
        rectangle.setStroke(Color.rgb(255, 255, 255, 0.2));
        rectangle.setStrokeWidth(0.8);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45);
        light.setElevation(60);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5);

        rectangle.setEffect(lighting);
    }

    private LinearGradient createBrickGradient(Color baseColor) {
        Color highlight = baseColor.deriveColor(0, 1.05, 1.25, 1);
        Color midTone = baseColor;
        Color shadow = baseColor.deriveColor(0, 1, 0.65, 1);

        return new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, highlight),
                new Stop(0.4, midTone),
                new Stop(0.95, shadow));
    }

    private boolean dimensionsMatch(Rectangle[][] matrix, int[][] data) {
        return matrix != null
                && data != null
                && matrix.length == data.length
                && matrix[0].length == data[0].length;
    }

    private Color getFillColor(int value) {
        return switch (value) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("#A5D8FF"); // pale blue
            case 2 -> Color.web("#74C0FC"); // sky blue
            case 3 -> Color.web("#4DABF7"); // electric blue
            case 4 -> Color.web("#9775FA"); // lavender
            case 5 -> Color.web("#845EF7"); // deep purple
            case 6 -> Color.web("#F783AC"); // pastel pink
            case 7 -> Color.web("#FF6BCB"); // vibrant pink
            default -> Color.web("#DEE2FF"); // soft fallback tint
        };
    }

    private int[][] extractBoardState() {
        int rows = displayMatrix.length;
        int cols = displayMatrix[0].length;
        int[][] board = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle cell = displayMatrix[i][j];
                if (cell == null) {
                    board[i][j] = 0;
                    continue;
                }
                Color fill = (Color) cell.getFill();
                board[i][j] = (fill == null || fill.getOpacity() == 0) ? 0 : 1;
            }
        }
        return board;
    }
}
