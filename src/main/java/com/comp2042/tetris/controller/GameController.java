/**
 * connects the player , GUI and game logic
 */

package com.comp2042.tetris.controller;


import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.score.ScoreManager;

import java.util.Objects;
import java.util.function.Supplier;

public class GameController implements IGameController {

    private static final int DROP_SCORE = 1;

    private final Board board;
    private final GameEventPublisher eventPublisher;
    private final ScoreManager scoreManager;

    public GameController(Supplier<Board> boardSupplier, GameEventPublisher eventPublisher) {
        this(requireBoard(boardSupplier), eventPublisher);
    }

    public GameController(Board board, GameEventPublisher eventPublisher) {
        this.board = Objects.requireNonNull(board, "board");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scoreManager = new ScoreManager(board.getScore(), eventPublisher, DROP_SCORE);
        board.createNewBrick();
        publishInitialState();
    }

    private static Board requireBoard(Supplier<Board> boardSupplier) {
        Objects.requireNonNull(boardSupplier, "boardSupplier");
        Board suppliedBoard = boardSupplier.get();
        return Objects.requireNonNull(suppliedBoard, "board");
    }

    private void publishInitialState() {
        eventPublisher.publishGameInitialized(new GameStateSnapshot(board.getBoardMatrix(), board.getViewData()));
        scoreManager.publishCurrentScore();
    }

    @Override
    public void onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();

        if (!canMove) {
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();
            eventPublisher.publishBoardUpdated(board.getBoardMatrix());

            scoreManager.handleLinesCleared(clearRow);

            // If createNewBrick returns true -> game over
            if (board.createNewBrick()) {
                eventPublisher.publishGameOver();
                return;
            }

            eventPublisher.publishBrickUpdated(board.getViewData());
        } else {
            // user-triggered automatic drop adds drop score
            if (event.getEventSource() == EventSource.USER) {
                scoreManager.handleUserDrop();
            }
            eventPublisher.publishBrickUpdated(board.getViewData());
        }
    }

    @Override
    public void onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        eventPublisher.publishBrickUpdated(board.getViewData());
    }

    @Override
    public void onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        eventPublisher.publishBrickUpdated(board.getViewData());
    }

    @Override
    public void onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        eventPublisher.publishBrickUpdated(board.getViewData());
    }

    @Override
    public void createNewGame() {
        board.newGame();
        eventPublisher.publishBoardUpdated(board.getBoardMatrix());
        eventPublisher.publishBrickUpdated(board.getViewData());
        scoreManager.publishCurrentScore();
    }
}
