package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.data.ViewData;

public record GameStateSnapshot(int[][] boardMatrix, ViewData viewData) {
}
