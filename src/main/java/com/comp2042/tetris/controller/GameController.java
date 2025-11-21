/**
 * connects the player , GUI and game logic
 */

package com.comp2042.tetris.controller;


import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.score.DefaultScorePolicy;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.score.ScoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;

public class GameController implements IGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private final Board board;
    private final GameEventPublisher eventPublisher;
    private final ScoreManager scoreManager;
    private final GameFlowManager gameFlowManager;

    public GameController(Supplier<Board> boardSupplier, GameEventPublisher eventPublisher) {
        this(requireBoard(boardSupplier), eventPublisher);
    }

    public GameController(Board board, GameEventPublisher eventPublisher) {
        this.board = Objects.requireNonNull(board, "board");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scoreManager = new ScoreManager(board.getScore(), eventPublisher, new DefaultScorePolicy());
        this.gameFlowManager = new GameFlowManager(board, eventPublisher, scoreManager);
        board.createNewBrick();
        publishInitialState();
        LOGGER.info("GameController initialized and initial game state published.");
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
        gameFlowManager.handleDownEvent(event);
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
        LOGGER.info("New game started.");
    }
}
