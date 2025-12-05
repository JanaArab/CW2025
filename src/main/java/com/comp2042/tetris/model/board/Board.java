/**
 * includes ;logic of brick movement in the game to be later done in simple board
 */

package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.level.GameLevel;
import com.comp2042.tetris.model.score.Score;
import com.comp2042.tetris.model.data.ViewData;

/**
 * Represents the game board and defines the core game mechanics for Tetris.
 * This interface provides methods for brick movement, rotation, and board management.
 *
 * <p>Implementations of this interface handle:</p>
 * <ul>
 *   <li>Moving bricks (down, left, right)</li>
 *   <li>Rotating bricks</li>
 *   <li>Creating new bricks when current one is placed</li>
 *   <li>Clearing completed rows</li>
 *   <li>Managing the game score</li>
 * </ul>
 *
 * @see SimpleBoard
 * @see ClearRow
 */
public interface Board {

    /**
     * Attempts to move the current brick down by one row.
     *
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current brick left by one column.
     *
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current brick right by one column.
     *
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current brick counterclockwise.
     *
     * @return true if the rotation was successful, false if blocked
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the top of the board.
     *
     * @return true if game is over (no space for new brick), false otherwise
     */
    boolean createNewBrick();

    /**
     * Returns a copy of the current board matrix.
     *
     * @return a 2D array representing the board state
     */
    int[][] getBoardMatrix();

    /**
     * Returns the view data containing brick positions and shapes for rendering.
     *
     * @return the current ViewData for the GUI
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the board background.
     * Called when a brick is placed and can no longer move.
     */
    void mergeBrickToBackground();

    /**
     * Checks and clears any completed rows from the board.
     *
     * @return a ClearRow containing information about cleared rows and score bonus
     */
    ClearRow clearRows();

    /**
     * Returns the current game score.
     *
     * @return the Score object tracking the player's score
     */
    Score getScore();

    /**
     * Resets the board for a new game.
     */
    void newGame();

    /**
     * Sets the current game level which affects game mechanics.
     *
     * @param level the GameLevel to apply
     */
    void setLevel(GameLevel level);

    /**
     * Adds garbage rows to the bottom of the board.
     * Used in special game modes.
     *
     * @param rows the garbage rows to add
     */
    void addRows(int[][] rows);
}
