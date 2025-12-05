package com.comp2042.tetris.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
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
import javafx.util.Duration;


public class BoardRenderer {

    private static final int HIDDEN_TOP_ROWS = 2;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private final GridPane ghostBrickPanel;
    private final GridPane nextBrickPanel;
    private final int brickSize;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] activeBrickMatrix;
    private Rectangle[][] ghostBrickMatrix;
    private List<Rectangle[][]> nextBrickMatrices;

    // Cache the origin offset to prevent jitter from fluctuating scene bounds
    private Double cachedOriginX = null;
    private Double cachedOriginY = null;

    public BoardRenderer(GridPane gamePanel, GridPane brickPanel, GridPane ghostBrickPanel, GridPane nextBrickPanel, int brickSize) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
        this.ghostBrickPanel = ghostBrickPanel;
        this.nextBrickPanel = nextBrickPanel;
        this.brickSize = brickSize;
    }

    public void initialize(int[][] boardMatrix, ViewData brick) {
        initializeBoard(boardMatrix);
        initializeActiveBrick(brick.getBrickData());
        initializeGhostBrick(brick.getBrickData());
        initializeNextBrickPreview(brick.getNextBricksData());
        updateBrickPosition(brick);
        updateGhostBrickPosition(brick);
    }

    public void refreshBrick(ViewData brick, boolean isPaused) {
        if (isPaused || activeBrickMatrix == null) {
            return;
        }

        updateBrickPosition(brick);
        updateGhostBrickPosition(brick);
        refreshActiveBrick(brick.getBrickData());
        refreshGhostBrick(brick.getBrickData());
        refreshNextBrick(brick.getNextBricksData());
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

    private void initializeGhostBrick(int[][] brickData) {
        if (ghostBrickPanel == null) {
            return;
        }
        ghostBrickPanel.getChildren().clear();
        ghostBrickMatrix = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = createTile(getFillColor(brickData[i][j]));
                rectangle.getStyleClass().add("ghostBrick");
                ghostBrickMatrix[i][j] = rectangle;
                ghostBrickPanel.add(rectangle, j, i);
            }
        }
    }

    private void initializeNextBrickPreview(List<int[][]> nextBricksData) {
        if (nextBrickPanel == null) {
            return;
        }

        nextBrickPanel.getChildren().clear();
        nextBrickMatrices = new ArrayList<>();

        // Use smaller tile size for preview (80% of normal size)
        int previewTileSize = 20;

        int verticalOffset = 0;
        for (int brickIndex = 0; brickIndex < nextBricksData.size(); brickIndex++) {
            int[][] brickData = nextBricksData.get(brickIndex);
            Rectangle[][] brickMatrix = new Rectangle[brickData.length][brickData[0].length];

            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    Rectangle rectangle = createPreviewTile(getFillColor(brickData[i][j]), previewTileSize);
                    brickMatrix[i][j] = rectangle;
                    nextBrickPanel.add(rectangle, j, verticalOffset + i);
                }
            }

            nextBrickMatrices.add(brickMatrix);
            // Add spacing between bricks (brick height + 1 row gap)
            verticalOffset += brickData.length + 1;
        }
    }

    private Rectangle createPreviewTile(Color color, int size) {
        Rectangle rectangle = new Rectangle(size, size);
        rectangle.setArcHeight(7);
        rectangle.setArcWidth(7);
        rectangle.setSmooth(true);
        applyBrickStyling(rectangle, color);
        return rectangle;
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

    private void refreshGhostBrick(int[][] brickData) {
        if (ghostBrickPanel == null) {
            return;
        }

        if (!dimensionsMatch(ghostBrickMatrix, brickData)) {
            initializeGhostBrick(brickData);
            return;
        }

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                setGhostRectangleData(brickData[i][j], ghostBrickMatrix[i][j]);
            }
        }
    }

    private void refreshNextBrick(List<int[][]> nextBricksData) {
        if (nextBrickPanel == null) {
            return;
        }

        // Check if we need to reinitialize (different number of bricks or dimension changes)
        if (nextBrickMatrices == null || nextBrickMatrices.size() != nextBricksData.size()) {
            initializeNextBrickPreview(nextBricksData);
            return;
        }

        // Check if any brick dimensions changed
        for (int brickIndex = 0; brickIndex < nextBricksData.size(); brickIndex++) {
            if (!dimensionsMatch(nextBrickMatrices.get(brickIndex), nextBricksData.get(brickIndex))) {
                initializeNextBrickPreview(nextBricksData);
                return;
            }
        }

        // Update each brick's colors
        for (int brickIndex = 0; brickIndex < nextBricksData.size(); brickIndex++) {
            int[][] brickData = nextBricksData.get(brickIndex);
            Rectangle[][] brickMatrix = nextBrickMatrices.get(brickIndex);

            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    setRectangleData(brickData[i][j], brickMatrix[i][j]);
                }
            }
        }
    }


    private void updateBrickPosition(ViewData brick) {
        if (!isLayerReady()) {
            Platform.runLater(() -> updateBrickPosition(brick));
            return;
        }

        if (cachedOriginX == null || cachedOriginY == null) {
            Bounds boardBounds = gamePanel.localToScene(gamePanel.getBoundsInLocal());
            Bounds layerBounds = brickPanel.getParent().localToScene(brickPanel.getParent().getBoundsInLocal());
            cachedOriginX = boardBounds.getMinX() - layerBounds.getMinX();
            cachedOriginY = boardBounds.getMinY() - layerBounds.getMinY();
        }

        double xOffset = cachedOriginX + brick.getXPosition() * horizontalStep();
        double yOffset = cachedOriginY + hiddenRowsOffset() + brick.getYPosition() * verticalStep();

        long targetX = Math.round(xOffset);
        long targetY = Math.round(yOffset);

        if (brickPanel.getLayoutX() != targetX || brickPanel.getLayoutY() != targetY) {
            brickPanel.setLayoutX(targetX);
            brickPanel.setLayoutY(targetY);
        }
    }

    private void updateGhostBrickPosition(ViewData brick) {
        if (ghostBrickPanel == null) {
            return;
        }

        if (!isLayerReady()) {
            Platform.runLater(() -> updateGhostBrickPosition(brick));
            return;
        }

        // Calculate and cache the origin offset on first update
        if (cachedOriginX == null || cachedOriginY == null) {
            Bounds boardBounds = gamePanel.localToScene(gamePanel.getBoundsInLocal());
            Bounds layerBounds = ghostBrickPanel.getParent().localToScene(ghostBrickPanel.getParent().getBoundsInLocal());
            cachedOriginX = boardBounds.getMinX() - layerBounds.getMinX();
            cachedOriginY = boardBounds.getMinY() - layerBounds.getMinY();
        }

        // Use the ghost Y position instead of the actual brick Y position
        double xOffset = cachedOriginX + brick.getXPosition() * horizontalStep();
        double yOffset = cachedOriginY + hiddenRowsOffset() + brick.getGhostYPosition() * verticalStep();

        // Round to whole pixels for pixel-perfect alignment
        long targetX = Math.round(xOffset);
        long targetY = Math.round(yOffset);

        // Only update if position actually changed to reduce layout thrashing
        if (ghostBrickPanel.getLayoutX() != targetX || ghostBrickPanel.getLayoutY() != targetY) {
            ghostBrickPanel.setLayoutX(targetX);
            ghostBrickPanel.setLayoutY(targetY);
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

    private Rectangle createTile(Color color) {
        Rectangle rectangle = new Rectangle(brickSize, brickSize);

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

    private void setGhostRectangleData(int color, Rectangle rectangle) {
        if (rectangle != null) {
            applyGhostBrickStyling(rectangle, getFillColor(color));
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

    private void applyGhostBrickStyling(Rectangle rectangle, Color baseColor) {
        if (baseColor == null || baseColor == Color.TRANSPARENT) {
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setEffect(null);
            rectangle.setStroke(null);
            return;
        }

        rectangle.setFill(baseColor);
        rectangle.setStroke(Color.rgb(255, 255, 255, 0.8));
        rectangle.setStrokeWidth(2.0);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle.setEffect(null);
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
            case 1 -> Color.web("#00FEFF"); // I-brick, Neon Cyan
            case 2 -> Color.web("#589BFF"); // J-brick, Lighter Neon Blue
            case 3 -> Color.web("#FF7F00"); // L-brick, Neon Orange
            case 4 -> Color.web("#FAFA5A"); // O-brick, Lighter Neon Yellow
            case 5 -> Color.web("#7CFF7C"); // S-brick, Lighter Neon Green
            case 6 -> Color.web("#FF00FF"); // T-brick, Neon Magenta
            case 7 -> Color.web("#FF69B4"); // Z-brick, Lighter Neon Pink
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


    public GridPane getGamePanel() {
        return gamePanel;
    }
}
