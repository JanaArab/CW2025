package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.data.ViewData;

public record DownData(ClearRow clearRow, ViewData viewData) {
}

/**public final class DownData {
    private final ClearRow clearRow; // Info about cleared lines (if any)
    private final ViewData viewData; // Info for updating visuals
    // Constructor: stores both logical and visual update data
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }
    // Return the line-clearing information
    public ClearRow getClearRow() {
        return clearRow;
    }
    // Return the updated visual data for redrawing the game
    public ViewData getViewData() {
        return viewData;
    }
}*/
