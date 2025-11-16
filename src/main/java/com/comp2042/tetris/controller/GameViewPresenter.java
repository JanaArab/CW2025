package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.view.BoardRenderer;
import com.comp2042.tetris.view.GameOverPanel;
import com.comp2042.tetris.view.NotificationPanel;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;

import java.util.Objects;

public class GameViewPresenter {
    private final BoardRenderer boardRenderer;
    private final Label scoreLabel;
    private final Group notificationGroup;
    private final GameOverPanel gameOverPanel;

    public GameViewPresenter(BoardRenderer boardRenderer, Label scoreLabel, Group notificationGroup, GameOverPanel gameOverPanel) {
        this.boardRenderer = Objects.requireNonNull(boardRenderer, "boardRenderer");
        this.scoreLabel = scoreLabel;
        this.notificationGroup = notificationGroup;
        this.gameOverPanel = gameOverPanel;
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
        if (clearRow.linesRemoved() > 0 && notificationGroup != null) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.scoreBonus());
            notificationGroup.getChildren().add(notificationPanel);
            notificationPanel.showScore(notificationGroup.getChildren());
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
