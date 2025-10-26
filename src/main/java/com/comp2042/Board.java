/**
 * includes ;logic of brick movement in the game to be later done in simpleboard
 */

package com.comp2042;


public interface Board {

    /*If true returns correct movement of block down
    * if there is no space then returns false */
    boolean moveBrickDown();

    /*If true returns correct movement of block left
    * if there is no space then returns false */
    boolean moveBrickLeft();

    /*If true returns correct movement of block right
    * if there is no space then returns false */
    boolean moveBrickRight();

    /*If true returns correct if rotation succeeded
    * if there is no space then returns false */
    boolean rotateLeftBrick();

    /*in createNewBrick this method returns true(game over) if there is no space
    * for new bricks, and false if there is enough space to add them */
    boolean createNewBrick();

    /*copy the current logical matrix*/
    int[][] getBoardMatrix();

    /*returns the view of bricks (current and upcoming)*/
    ViewData getViewData();

    /*makes sure the placed brick is locked in place */
    void mergeBrickToBackground();

    /*clear full rows*/
    ClearRow clearRows();

    /*returns score*/
    Score getScore();

    /*restart new game*/
    void newGame();
}
