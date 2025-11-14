/**
 * connects the player , GUI and game logic
 */

package com.comp2042.tetris.controller;


import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.board.SimpleBoard;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.ScoreChangeEvent;


public class GameController implements IGameController {

    private static final int DROP_SCORE = 1;


    private final Board board;

    private final GameEventPublisher eventPublisher;


    public GameController(GameEventPublisher eventPublisher) {
        this(new SimpleBoard(25, 10), eventPublisher);
    }

    public GameController(Board board, GameEventPublisher eventPublisher) {
        this.board = board;
        this.eventPublisher = eventPublisher;
        board.createNewBrick();
        publishInitialState();
    }

    private void publishInitialState() {
        eventPublisher.publishGameInitialized(new GameStateSnapshot(board.getBoardMatrix(), board.getViewData()));
                publishScoreChanged();
    }

    private void publishScoreChanged() {
        eventPublisher.publishScoreChanged(new ScoreChangeEvent(board.getScore().getValue()));
    }


    @Override
    public void onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();
            eventPublisher.publishBoardUpdated(board.getBoardMatrix());
            if (clearRow.linesRemoved() > 0) {
                board.getScore().add(clearRow.scoreBonus());
                publishScoreChanged();
                eventPublisher.publishLinesCleared(clearRow);
            }

            /* as mentioned previously in Board, if returns true when game is over
             * and false when there is space for a new brick */
            if (board.createNewBrick()) {
                eventPublisher.publishGameOver();
                return;

            }

            eventPublisher.publishBrickUpdated(board.getViewData());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(DROP_SCORE);/*replaced the 1 with a constant
                that way changing it in the future will be easier*/
                publishScoreChanged();
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
        public void createNewGame () {
            board.newGame();
            eventPublisher.publishBoardUpdated(board.getBoardMatrix());
            eventPublisher.publishBrickUpdated(board.getViewData());
            publishScoreChanged();
        }
    }

