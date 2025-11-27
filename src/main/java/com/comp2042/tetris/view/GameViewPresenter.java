package com.comp2042.tetris.view;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.util.Objects;

public class GameViewPresenter {
    private final BoardRenderer boardRenderer;
    private final Label scoreLabel;
    private final Group notificationGroup;
    private final OverlayPanel gameOverPanel;
    private final NotificationAnimator notificationAnimator;
    private final RowClearAnimator rowClearAnimator;

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
        Platform.runLater(() -> scoreLabel.setText(String.valueOf(event.newScore())));
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
