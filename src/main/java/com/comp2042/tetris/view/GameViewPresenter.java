package com.comp2042.tetris.view;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.utils.ScoreThresholdDetector;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Objects;

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

                    // Center the notification panel in the screen (same offsets used elsewhere)
                    notificationPanel.setLayoutX(-110);
                    notificationPanel.setLayoutY(-100);

                    notificationGroup.getChildren().add(notificationPanel);
                    notificationAnimator.playShowScore(notificationPanel, notificationGroup.getChildren());

                    // Play a small firework burst
                    fireworkAnimator.playFirework(notificationGroup);
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

                        // Center the notification panel in the screen
                        // NotificationPanel has minWidth=220 and minHeight=200
                        // Center it by offsetting by half its dimensions
                        notificationPanel.setLayoutX(-110); // Half of minWidth (220/2)
                        notificationPanel.setLayoutY(-100); // Half of minHeight (200/2)

                        notificationGroup.getChildren().add(notificationPanel);
                        notificationAnimator.playShowScore(notificationPanel, notificationGroup.getChildren());
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
