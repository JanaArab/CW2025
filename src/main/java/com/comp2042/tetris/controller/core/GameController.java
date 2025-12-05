/**
 * Main game controller that connects the game model, user input, and GUI.
 * Implements the {@link IGameController} interface to handle all game actions
 * and coordinate between the board logic, score management, and event publishing.
 *
 * <p>Responsibilities include:</p>
 * <ul>
 *   <li>Processing player input events (move, rotate, drop)</li>
 *   <li>Managing game flow (new game, level changes)</li>
 *   <li>Publishing game state changes to listeners</li>
 *   <li>Coordinating score updates</li>
 * </ul>
 *
 * @see IGameController
 * @see Board
 * @see GameEventPublisher
 */

package com.comp2042.tetris.controller.core;


import com.comp2042.tetris.model.board.Board;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.score.DefaultScorePolicy;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.MoveEvent;
import com.comp2042.tetris.model.event.BrickPlacedEvent;
import com.comp2042.tetris.model.score.ScoreManager;
import com.comp2042.tetris.model.level.GameLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.comp2042.tetris.utils.AudioManager;

import java.util.Objects;
import java.util.function.Supplier;

public class GameController implements IGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private final Board board;
    private final GameEventPublisher eventPublisher;
    private final ScoreManager scoreManager;
    private final GameFlowManager gameFlowManager;

    /**
     * Constructs a GameController with a board supplier.
     *
     * @param boardSupplier supplier that creates the game board
     * @param eventPublisher the event publisher for game events
     */
    public GameController(Supplier<Board> boardSupplier, GameEventPublisher eventPublisher) {
        this(requireBoard(boardSupplier), eventPublisher);
    }

    /**
     * Constructs a GameController with a pre-created board.
     *
     * @param board the game board
     * @param eventPublisher the event publisher for game events
     */
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
    public void setLevel(GameLevel level) {
        // Pass the level down to the board logic
        board.setLevel(level);

        // Enable or disable garbage based on the level
        if (level.isGarbageEnabled()) {
            gameFlowManager.enableGarbage();
        } else {
            gameFlowManager.disableGarbage();
        }
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
        // Play rotation SFX immediately on rotate input for instant feedback
        AudioManager.getInstance().playRotation();
        eventPublisher.publishBrickUpdated(board.getViewData());
    }

    @Override
    public void onInstantDropEvent(MoveEvent event) {
        System.out.println("Instant drop triggered");
        // Instantly drop the brick to the bottom
        while (board.moveBrickDown()) {
            scoreManager.handleDrop(event);

        }
        // Now merge and handle as in handleDownEvent
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
    }

    @Override
    public void createNewGame() {
        board.newGame();
        gameFlowManager.resetGarbage();
        eventPublisher.publishBoardUpdated(board.getBoardMatrix());
        eventPublisher.publishBrickUpdated(board.getViewData());
        scoreManager.publishCurrentScore();
        LOGGER.info("New game started.");
    }
}
