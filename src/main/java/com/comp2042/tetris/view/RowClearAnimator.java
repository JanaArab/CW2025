package com.comp2042.tetris.view;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles row clearing animations for the Tetris game.
 * Animates cleared rows with a fade-out and scale effect before they are removed.
 */
public class RowClearAnimator {

    private static final Duration ANIMATION_DURATION = Duration.millis(400);
    private static final int HIDDEN_TOP_ROWS = 2;
    private final PixelStarExplosion pixelStarExplosion;

    /**
     * Creates a new RowClearAnimator with pixel star explosion effects.
     */
    public RowClearAnimator() {
        this.pixelStarExplosion = new PixelStarExplosion();
    }

    /**
     * Animates the clearing of rows on the game board.
     *
     * @param gamePanel The GridPane containing the game board rectangles
     * @param clearedRowIndices List of row indices that were cleared (in board coordinates)
     * @param onComplete Callback to execute after the animation completes
     */
    public void animateClearRows(GridPane gamePanel, List<Integer> clearedRowIndices, Runnable onComplete) {
        if (gamePanel == null || clearedRowIndices == null || clearedRowIndices.isEmpty()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        List<Rectangle> rectanglesToAnimate = collectRectanglesFromRows(gamePanel, clearedRowIndices);

        if (rectanglesToAnimate.isEmpty()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        ParallelTransition parallelTransition = new ParallelTransition();

        for (Rectangle rect : rectanglesToAnimate) {
            // Fade out animation
            FadeTransition fadeOut = new FadeTransition(ANIMATION_DURATION, rect);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            // Scale animation (shrink to center)
            ScaleTransition scaleDown = new ScaleTransition(ANIMATION_DURATION, rect);
            scaleDown.setFromX(1.0);
            scaleDown.setFromY(1.0);
            scaleDown.setToX(0.8);
            scaleDown.setToY(0.8);

            parallelTransition.getChildren().addAll(fadeOut, scaleDown);
        }

        parallelTransition.setOnFinished(event -> {
            // Restore opacity and scale for reuse
            for (Rectangle rect : rectanglesToAnimate) {
                rect.setOpacity(1.0);
                rect.setScaleX(1.0);
                rect.setScaleY(1.0);
            }

            if (onComplete != null) {
                onComplete.run();
            }
        });

        parallelTransition.play();
    }

    /**
     * Collects all Rectangle nodes from the specified rows in the GridPane.
     *
     * @param gamePanel The GridPane containing the rectangles
     * @param clearedRowIndices List of row indices (in board coordinates, accounting for hidden rows)
     * @return List of Rectangle nodes to animate
     */
    private List<Rectangle> collectRectanglesFromRows(GridPane gamePanel, List<Integer> clearedRowIndices) {
        List<Rectangle> rectangles = new ArrayList<>();

        for (Integer rowIndex : clearedRowIndices) {
            // Convert board row index to display row index (subtract hidden rows)
            int displayRow = rowIndex - HIDDEN_TOP_ROWS;

            // Only process rows that are visible in the display
            if (displayRow < 0) {
                continue;
            }

            // Collect all rectangles in this row
            for (var node : gamePanel.getChildren()) {
                if (node instanceof Rectangle) {
                    Integer nodeRow = GridPane.getRowIndex(node);
                    if (nodeRow != null && nodeRow == displayRow) {
                        rectangles.add((Rectangle) node);
                    }
                }
            }
        }

        return rectangles;
    }

    /**
     * Creates a more elaborate animation with a flash effect before fade out.
     *
     * @param gamePanel The GridPane containing the game board rectangles
     * @param clearedRowIndices List of row indices that were cleared
     * @param onComplete Callback to execute after the animation completes
     */
    public void animateClearRowsWithFlash(GridPane gamePanel, List<Integer> clearedRowIndices, Runnable onComplete) {
        if (gamePanel == null || clearedRowIndices == null || clearedRowIndices.isEmpty()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        List<Rectangle> rectanglesToAnimate = collectRectanglesFromRows(gamePanel, clearedRowIndices);

        if (rectanglesToAnimate.isEmpty()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        // Trigger star explosions immediately
        triggerRowExplosions(gamePanel, clearedRowIndices);

        SequentialTransition sequentialTransition = new SequentialTransition();

        // Phase 1: Flash (quick brightness increase)
        ParallelTransition flashPhase = new ParallelTransition();
        Duration flashDuration = Duration.millis(100);

        for (Rectangle rect : rectanglesToAnimate) {
            FadeTransition flashOut = new FadeTransition(flashDuration, rect);
            flashOut.setFromValue(1.0);
            flashOut.setToValue(0.3);
            flashOut.setAutoReverse(true);
            flashOut.setCycleCount(2);
            flashPhase.getChildren().add(flashOut);
        }

        // Phase 2: Fade out and shrink
        ParallelTransition fadePhase = new ParallelTransition();
        Duration fadeDuration = Duration.millis(300);

        for (Rectangle rect : rectanglesToAnimate) {
            FadeTransition fadeOut = new FadeTransition(fadeDuration, rect);
            fadeOut.setToValue(0.0);

            ScaleTransition scaleDown = new ScaleTransition(fadeDuration, rect);
            scaleDown.setToX(0.5);
            scaleDown.setToY(0.5);

            fadePhase.getChildren().addAll(fadeOut, scaleDown);
        }

        sequentialTransition.getChildren().addAll(flashPhase, fadePhase);

        sequentialTransition.setOnFinished(event -> {
            // Restore properties
            for (Rectangle rect : rectanglesToAnimate) {
                rect.setOpacity(1.0);
                rect.setScaleX(1.0);
                rect.setScaleY(1.0);
            }

            if (onComplete != null) {
                onComplete.run();
            }
        });

        sequentialTransition.play();
    }

    /**
     * Triggers pixel star explosion effects along the cleared rows.
     *
     * @param gamePanel The GridPane containing the game board
     * @param clearedRowIndices List of row indices that were cleared
     */
    private void triggerRowExplosions(GridPane gamePanel, List<Integer> clearedRowIndices) {
        // Get the parent pane to add explosion effects
        Pane parentPane = (Pane) gamePanel.getParent();
        if (parentPane == null) {
            return;
        }

        // Get the bounds of the game panel in scene coordinates
        Bounds gamePanelBounds = gamePanel.localToScene(gamePanel.getBoundsInLocal());
        Bounds parentBounds = parentPane.localToScene(parentPane.getBoundsInLocal());

        // Calculate the offset to convert from scene to parent coordinates
        double offsetX = gamePanelBounds.getMinX() - parentBounds.getMinX();
        double offsetY = gamePanelBounds.getMinY() - parentBounds.getMinY();

        // Get dimensions of a single cell
        double cellHeight = gamePanel.getRowConstraints().isEmpty() ?
                           UIConstants.BRICK_SIZE : UIConstants.BRICK_SIZE;
        double boardWidth = gamePanelBounds.getWidth();

        // Create explosions for each cleared row
        for (Integer rowIndex : clearedRowIndices) {
            int displayRow = rowIndex - HIDDEN_TOP_ROWS;
            if (displayRow < 0) {
                continue;
            }

            // Calculate the Y position for this row in parent coordinates
            double rowY = offsetY + (displayRow * cellHeight) + (cellHeight / 2);
            double startX = offsetX;
            double endX = offsetX + boardWidth;

            // Create 8 explosions along the row with 15 stars each
            pixelStarExplosion.createRowExplosions(parentPane, startX, endX, rowY, 8, 15, null);
        }
    }
}

