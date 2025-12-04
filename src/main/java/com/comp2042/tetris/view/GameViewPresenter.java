package com.comp2042.tetris.view;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.utils.AudioManager;
import com.comp2042.tetris.utils.ScoreThresholdDetector;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.geometry.Pos;

import java.util.List;
import java.util.Objects;

import com.comp2042.tetris.view.UIConstants;

public class GameViewPresenter {
    private final BoardRenderer boardRenderer;
    private final Label scoreLabel;
    private final Group notificationGroup;
    private final OverlayPanel gameOverPanel;
    private final NotificationAnimator notificationAnimator;
    private final RowClearAnimator rowClearAnimator;

    // new fields to support fireworks/popups on score thresholds
    private int lastScore = 0;
    private final FireworkAnimator fireworkAnimator = new FireworkAnimator();

    public GameViewPresenter(BoardRenderer boardRenderer, Label scoreLabel, Group notificationGroup,
                             OverlayPanel gameOverPanel, NotificationAnimator notificationAnimator) {
        this.boardRenderer = Objects.requireNonNull(boardRenderer, "boardRenderer");
        this.scoreLabel = scoreLabel;
        this.notificationGroup = notificationGroup;
        this.gameOverPanel = gameOverPanel;
        this.notificationAnimator = Objects.requireNonNull(notificationAnimator, "notificationAnimator");
        this.rowClearAnimator = new RowClearAnimator();
    }

    public void initializeGame(GameStateSnapshot snapshot) {
        boardRenderer.initialize(snapshot.boardMatrix(), snapshot.viewData());
    }

    public void updateScore(ScoreChangeEvent event) {
        if (scoreLabel == null) {
            throw new IllegalStateException("scoreLabel is null.");
        }
        // Update the label and, if we've crossed 500-point thresholds, show popups and fireworks
        Platform.runLater(() -> {
            int newScore = event.newScore();
            scoreLabel.setText(String.valueOf(newScore));

            // detect crossed thresholds (500, 1000, 1500, ...)
            List<Integer> crossed = ScoreThresholdDetector.determineCrossedThresholds(lastScore, newScore, 500);
            if (notificationGroup != null && !crossed.isEmpty()) {
                for (Integer threshold : crossed) {
                    // Show a simple +500 popup using existing NotificationPanel and animator
                    NotificationPanel notificationPanel = new NotificationPanel("+500");

                    // Prefer adding to the parent StackPane to align CENTER_LEFT
                    Parent parent = notificationGroup.getParent();
                    if (parent instanceof StackPane stack) {
                        stack.getChildren().add(notificationPanel);
                        StackPane.setAlignment(notificationPanel, Pos.CENTER_LEFT);
                        notificationPanel.setTranslateX(40);
                        notificationAnimator.playShowScore(notificationPanel, stack.getChildren());
                    } else {
                        double leftX = - (UIConstants.WINDOW_WIDTH / 2.0) + 40;
                        notificationPanel.setLayoutX((int) leftX);
                        notificationPanel.setLayoutY(0);
                        notificationGroup.getChildren().add(notificationPanel);
                        notificationAnimator.playShowScore(notificationPanel, notificationGroup.getChildren());
                    }

                    // Play a small firework burst
                    fireworkAnimator.playFirework(notificationGroup);
                    // Play confetti SFX for threshold
                    AudioManager.getInstance().playConfetti();
                }
            }

            lastScore = newScore;
        });
    }

    public void refreshBrick(ViewData viewData, boolean paused) {
        boardRenderer.refreshBrick(viewData, paused);
    }

    public void refreshBoard(int[][] boardMatrix) {
        boardRenderer.refreshGameBackground(boardMatrix);
    }

    public void handleLinesCleared(ClearRow clearRow) {

        if (clearRow.linesRemoved() > 0) {
            // Animate row clearing first
            rowClearAnimator.animateClearRowsWithFlash(
                boardRenderer.getGamePanel(),
                clearRow.clearedRows(),
                () -> {
                    // After animation completes, show notification
                    if (notificationGroup != null) {
                        NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.scoreBonus());

                        Parent parent = notificationGroup.getParent();
                        if (parent instanceof StackPane stack) {
                            stack.getChildren().add(notificationPanel);
                            StackPane.setAlignment(notificationPanel, Pos.CENTER_LEFT);
                            notificationPanel.setTranslateX(40);
                            notificationAnimator.playShowScore(notificationPanel, stack.getChildren());
                        } else {
                            double leftX = - (UIConstants.WINDOW_WIDTH / 2.0) + 40;
                            notificationPanel.setLayoutX((int) leftX);
                            notificationPanel.setLayoutY(0);
                            notificationGroup.getChildren().add(notificationPanel);
                            notificationAnimator.playShowScore(notificationPanel, notificationGroup.getChildren());
                        }
                        // Play confetti SFX when a row clear bonus notification is displayed
                        AudioManager.getInstance().playConfetti();
                    }
                }
            );
        }
    }

    public void showGameOver() {
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(true);
        }
    }

    public void hideGameOver() {
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
    }
}
