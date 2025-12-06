/*
* created this Interface following the Dependency Inversion Principle (DIP)
* to decouple high-level game logic from low-level GUI details
* which will increase testability
* in GuiController we override the methods mentioned in the interface   */

package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.core.IGameController;
import com.comp2042.tetris.model.data.ViewData ;

/**
 * Interface for GUI controllers that manage the game view.
 * Follows the Dependency Inversion Principle (DIP) to decouple
 * high-level game logic from low-level GUI details.
 *
 * <p>Implementations handle:</p>
 * <ul>
 *   <li>Game controller integration</li>
 *   <li>View initialization</li>
 *   <li>Board refresh</li>
 *   <li>Game over display</li>
 * </ul>
 *
 * @see GuiController
 * @see IGameController
 */
public interface IGuiController {

    /**
     * Sets the game controller for handling game logic.
     *
     * @param gameController the game controller to use
     */
    void setGameController(IGameController gameController) ;

    /**
     * Initializes the game view with the board state and brick data.
     *
     * @param boardMatrix the initial board state
     * @param viewData the initial brick and preview data
     */
    void initGameView(int[][] boardMatrix, ViewData viewData) ;

    /**
     * Refreshes the game board background display.
     *
     * @param boardMatrix the current board state
     */
    void refreshGameBackground(int[][] boardMatrix) ;

    /**
     * Displays the game over screen.
     */
    void gameOver() ;


}
