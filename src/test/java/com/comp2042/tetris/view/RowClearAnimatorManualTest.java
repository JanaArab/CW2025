package com.comp2042.tetris.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Manual test application for RowClearAnimator.
 * Run this to visually verify the row clearing animation works.
 */
public class RowClearAnimatorManualTest extends Application {

    private GridPane gamePanel;
    private RowClearAnimator animator;
    private int[][] board;
    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final int BRICK_SIZE = 30;

    @Override
    public void start(Stage primaryStage) {
        animator = new RowClearAnimator();
        gamePanel = new GridPane();
        gamePanel.setHgap(2);
        gamePanel.setVgap(2);

        // Initialize board
        initializeBoard();

        // Create buttons
        Button fillRowButton = new Button("Fill Row 18");
        fillRowButton.setOnAction(e -> fillRow(18));

        Button clearRowButton = new Button("Clear Row 18 (Simple)");
        clearRowButton.setOnAction(e -> animateClearRow(18, false));

        Button clearRowFlashButton = new Button("Clear Row 18 (Flash)");
        clearRowFlashButton.setOnAction(e -> animateClearRow(18, true));

        Button fillMultipleButton = new Button("Fill Rows 15-18");
        fillMultipleButton.setOnAction(e -> {
            fillRow(15);
            fillRow(16);
            fillRow(17);
            fillRow(18);
        });

        Button clearMultipleButton = new Button("Clear Rows 15-18");
        clearMultipleButton.setOnAction(e -> animateClearRows(Arrays.asList(17, 18, 19, 20), true));

        Button resetButton = new Button("Reset Board");
        resetButton.setOnAction(e -> resetBoard());

        VBox controls = new VBox(10, fillRowButton, clearRowButton, clearRowFlashButton,
                                 fillMultipleButton, clearMultipleButton, resetButton);
        controls.setAlignment(Pos.CENTER);
        controls.setStyle("-fx-padding: 20;");

        VBox root = new VBox(20, gamePanel, controls);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: #1a1a2e;");

        Scene scene = new Scene(root, 600, 800);
        primaryStage.setTitle("Row Clear Animation Test");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Row Clear Animation Test Started");
        System.out.println("- Click 'Fill Row 18' to fill a row with blocks");
        System.out.println("- Click 'Clear Row 18' to see the simple animation");
        System.out.println("- Click 'Clear Row 18 (Flash)' to see the flash animation");
    }

    private void initializeBoard() {
        board = new int[ROWS][COLS];
        gamePanel.getChildren().clear();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.GRAY);
                rect.setStrokeWidth(0.5);
                GridPane.setRowIndex(rect, row);
                GridPane.setColumnIndex(rect, col);
                gamePanel.getChildren().add(rect);
                board[row][col] = 0;
            }
        }
    }

    private void fillRow(int row) {
        if (row < 0 || row >= ROWS) return;

        for (int col = 0; col < COLS; col++) {
            board[row][col] = 1;
            updateCell(row, col);
        }
        System.out.println("Filled row: " + row);
    }

    private void updateCell(int row, int col) {
        for (var node : gamePanel.getChildren()) {
            if (node instanceof Rectangle rect) {
                Integer r = GridPane.getRowIndex(rect);
                Integer c = GridPane.getColumnIndex(rect);
                if (r != null && c != null && r == row && c == col) {
                    if (board[row][col] == 0) {
                        rect.setFill(Color.TRANSPARENT);
                        rect.setStroke(Color.GRAY);
                    } else {
                        rect.setFill(Color.CYAN);
                        rect.setStroke(Color.WHITE);
                    }
                }
            }
        }
    }

    private void animateClearRow(int displayRow, boolean useFlash) {
        // In real game, board row index = display row + 2 (for hidden rows)
        int boardRow = displayRow + 2;

        System.out.println("Animating clear for display row: " + displayRow + " (board row: " + boardRow + ")");

        if (useFlash) {
            animator.animateClearRowsWithFlash(gamePanel, Arrays.asList(boardRow), () -> {
                clearRow(displayRow);
                System.out.println("Animation complete - row cleared");
            });
        } else {
            animator.animateClearRows(gamePanel, Arrays.asList(boardRow), () -> {
                clearRow(displayRow);
                System.out.println("Animation complete - row cleared");
            });
        }
    }

    private void animateClearRows(List<Integer> boardRows, boolean useFlash) {
        System.out.println("Animating clear for board rows: " + boardRows);

        if (useFlash) {
            animator.animateClearRowsWithFlash(gamePanel, boardRows, () -> {
                for (Integer boardRow : boardRows) {
                    int displayRow = boardRow - 2;
                    if (displayRow >= 0 && displayRow < ROWS) {
                        clearRow(displayRow);
                    }
                }
                System.out.println("Multiple row animation complete");
            });
        } else {
            animator.animateClearRows(gamePanel, boardRows, () -> {
                for (Integer boardRow : boardRows) {
                    int displayRow = boardRow - 2;
                    if (displayRow >= 0 && displayRow < ROWS) {
                        clearRow(displayRow);
                    }
                }
                System.out.println("Multiple row animation complete");
            });
        }
    }

    private void clearRow(int row) {
        if (row < 0 || row >= ROWS) return;

        for (int col = 0; col < COLS; col++) {
            board[row][col] = 0;
            updateCell(row, col);
        }
    }

    private void resetBoard() {
        initializeBoard();
        System.out.println("Board reset");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

