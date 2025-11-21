/*
* created this Interface following the Dependency Inversion Principle (DIP)
* to decouple high-level game logic from low-level GUI details
* which will increase testability
* in GuiController we override the methods mentioned in the interface   */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.model.data.ViewData ;



public interface IGuiController {
    void setGameController(IGameController gameController) ;

    void initGameView(int[][] boardMatrix, ViewData viewData) ;

    void refreshGameBackground(int[][] boardMatrix) ;

    void gameOver() ;


}
