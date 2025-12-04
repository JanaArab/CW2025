package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.board.GarbageMechanic;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.BrickPlacedEvent;
import com.comp2042.tetris.model.score.ScoreManager;

import java.util.Objects;

public class GameFlowManager {
    private final Board board;
    private final GameEventPublisher eventPublisher;
    private final ScoreManager scoreManager;
    private final GarbageMechanic garbageMechanic;

    public GameFlowManager(Board board, GameEventPublisher eventPublisher, ScoreManager scoreManager) {
        this(board, eventPublisher, scoreManager, new GarbageMechanic());
    }

    public GameFlowManager(Board board, GameEventPublisher eventPublisher, ScoreManager scoreManager, GarbageMechanic garbageMechanic) {
        this.board = Objects.requireNonNull(board, "board");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scoreManager = Objects.requireNonNull(scoreManager, "scoreManager");
        this.garbageMechanic = Objects.requireNonNull(garbageMechanic, "garbageMechanic");
    }

    public void handleDownEvent(MoveEvent event) {
        MoveEvent dropEvent = Objects.requireNonNull(event, "event");

        // Check if it's time to add garbage rows
        checkAndAddGarbage();

        boolean canMove = board.moveBrickDown();

        if (!canMove) {
            board.mergeBrickToBackground();
            eventPublisher.publishBrickPlaced(new BrickPlacedEvent());
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

    private void checkAndAddGarbage() {
        int rowsToAdd = garbageMechanic.checkAndGetRowsToAdd();
        if (rowsToAdd > 0) {
            int[][] garbageRows = new int[rowsToAdd][];
            for (int i = 0; i < rowsToAdd; i++) {
                garbageRows[i] = garbageMechanic.generateGarbageRow(board.getBoardMatrix()[0].length);
            }
            board.addRows(garbageRows);
            eventPublisher.publishBoardUpdated(board.getBoardMatrix());
        }
    }

    public void enableGarbage() {
        garbageMechanic.enable();
    }

    public void disableGarbage() {
        garbageMechanic.disable();
    }

    public void resetGarbage() {
        garbageMechanic.reset();
    }
}
