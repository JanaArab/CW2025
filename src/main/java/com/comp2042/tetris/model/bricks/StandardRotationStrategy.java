package com.comp2042.tetris.model.bricks;
import com.comp2042.tetris.utils.MatrixOperations;
import java.awt.Point;

public class StandardRotationStrategy implements RotationStrategy {
    private static final int[] KICKS = {-1, 1, -2, 2};

    @Override
    public Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset) {
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();

        if (!MatrixOperations.intersect(boardMatrix, rotatedShape, currentX, currentY)) {
            return currentOffset;
        }

        for (int dx : KICKS) {
            if (!MatrixOperations.intersect(boardMatrix, rotatedShape, currentX + dx, currentY)) {
                return new Point(currentX + dx, currentY);
            }
        }
        return null;
    }
}
