package com.comp2042.tetris.model.bricks;

import java.awt.Point;

public interface RotationStrategy {
    Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset);
}
