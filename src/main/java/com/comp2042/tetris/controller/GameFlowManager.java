package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.score.ScoreManager;

import java.util.Objects;

public class GameFlowManager {
    private final Board board;
    private final GameEventPublisher eventPublisher;
    private final ScoreManager scoreManager;

    public GameFlowManager(Board board, GameEventPublisher eventPublisher, ScoreManager scoreManager) {
        this.board = Objects.requireNonNull(board, "board");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scoreManager = Objects.requireNonNull(scoreManager, "scoreManager");
    }

    public void handleDownEvent(MoveEvent event) {
        MoveEvent dropEvent = Objects.requireNonNull(event, "event");
        boolean canMove = board.moveBrickDown();

        if (!canMove) {
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();
            eventPublisher.publishBoardUpdated(board.getBoardMatrix());

            scoreManager.handleLinesCleared(clearRow);

            if (board.createNewBrick()) {
                eventPublisher.publishGameOver();
                return;
            }

            eventPublisher.publishBrickUpdated(board.getViewData());
        } else {
            scoreManager.handleDrop(dropEvent);
            eventPublisher.publishBrickUpdated(board.getViewData());
        }
    }
}
