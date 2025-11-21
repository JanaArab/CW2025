package com.comp2042.tetris.model.bricks;

import com.comp2042.tetris.model.event.NextShapeInfo;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private static final int[][] SHAPE_ONE = {{1, 0}, {0, 1}};
    private static final int[][] SHAPE_TWO = {{2, 2}, {0, 0}};
    private static final int[][] SHAPE_THREE = {{0, 3}, {3, 3}};

    @Test
    void peekNextRotationThrowsWhenBrickMissing() {
        BrickRotator rotator = new BrickRotator();
        assertThrows(IllegalStateException.class, rotator::peekNextRotation);
    }

    @Test
    void getCurrentShapeThrowsWhenBrickMissing() {
        BrickRotator rotator = new BrickRotator();
        assertThrows(IllegalStateException.class, rotator::getCurrentShape);
    }

    @Test
    void setBrickResetsCurrentShapeIndex() {
        BrickRotator rotator = new BrickRotator();
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO)));
        rotator.setCurrentShape(1);

        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO)));

        assertArrayEquals(SHAPE_ONE, rotator.getCurrentShape());
    }

    @Test
    void peekNextRotationWrapsAroundToFirstShape() {
        BrickRotator rotator = new BrickRotator();
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO)));
        rotator.setCurrentShape(1);

        NextShapeInfo info = rotator.peekNextRotation();

        assertArrayEquals(SHAPE_ONE, info.getShape());
        assertEquals(0, info.getPosition());
    }

    @Test
    void peekNextRotationReturnsNextShapeIndex() {
        BrickRotator rotator = new BrickRotator();
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO, SHAPE_THREE)));
        rotator.setCurrentShape(0);

        NextShapeInfo info = rotator.peekNextRotation();

        assertArrayEquals(SHAPE_TWO, info.getShape());
        assertEquals(1, info.getPosition());
    }

    @Test
    void tryRotateLeftAdvancesShapeWhenStrategyAccepts() {
        StubRotationStrategy strategy = new StubRotationStrategy();
        Point kickedPoint = new Point(5, 4);
        strategy.setNextResult(kickedPoint);
        BrickRotator rotator = new BrickRotator(strategy);
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO)));
        rotator.setCurrentShape(0);
        int[][] board = new int[4][4];
        Point offset = new Point(1, 1);

        Point result = rotator.tryRotateLeft(board, offset);

        assertEquals(kickedPoint, result);
        assertArrayEquals(SHAPE_TWO, rotator.getCurrentShape());
        assertSame(board, strategy.capturedBoard);
        assertArrayEquals(SHAPE_TWO, strategy.capturedShape);
        assertEquals(offset, strategy.capturedOffset);
    }

    @Test
    void tryRotateLeftKeepsShapeWhenStrategyRejects() {
        StubRotationStrategy strategy = new StubRotationStrategy();
        strategy.setNextResult(null);
        BrickRotator rotator = new BrickRotator(strategy);
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_THREE)));
        int[][] board = new int[2][2];
        Point offset = new Point(0, 0);

        Point result = rotator.tryRotateLeft(board, offset);

        assertNull(result);
        assertArrayEquals(SHAPE_ONE, rotator.getCurrentShape());
        assertSame(board, strategy.capturedBoard);
        assertArrayEquals(SHAPE_THREE, strategy.capturedShape);
        assertEquals(offset, strategy.capturedOffset);
    }

    @Test
    void tryRotateLeftUsesCloneOfShapeList() {
        StubRotationStrategy strategy = new StubRotationStrategy();
        strategy.setNextResult(new Point(0, 0));
        BrickRotator rotator = new BrickRotator(strategy);
        rotator.setBrick(new StubBrick(copyShapes(SHAPE_ONE, SHAPE_TWO)));

        int[][] originalReference = rotator.getCurrentShape();
        rotator.tryRotateLeft(new int[2][2], new Point());

        assertNotSame(originalReference, rotator.getCurrentShape());
    }

    private static List<int[][]> copyShapes(int[][]... shapes) {
        List<int[][]> copies = new ArrayList<>();
        Arrays.stream(shapes).map(BrickRotatorTest::copyMatrix).forEach(copies::add);
        return copies;
    }

    private static List<int[][]> copyShapes(List<int[][]> source) {
        List<int[][]> copies = new ArrayList<>(source.size());
        for (int[][] matrix : source) {
            copies.add(copyMatrix(matrix));
        }
        return copies;
    }

    private static int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    private static final class StubBrick implements Brick {
        private final List<int[][]> shapes;

        private StubBrick(List<int[][]> shapes) {
            this.shapes = copyShapes(shapes);
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return copyShapes(shapes);
        }

        @Override
        public List<int[][]> cloneShape() {
            return copyShapes(shapes);
        }

        @Override
        public Brick cloneBrick() {
            return new StubBrick(copyShapes(shapes));
        }
    }

    private static final class StubRotationStrategy implements RotationStrategy {
        private Point nextResult;
        private int[][] capturedBoard;
        private int[][] capturedShape;
        private Point capturedOffset;

        private void setNextResult(Point nextResult) {
            this.nextResult = nextResult;
        }

        @Override
        public Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset) {
            this.capturedBoard = boardMatrix;
            this.capturedShape = rotatedShape;
            this.capturedOffset = currentOffset;
            return nextResult;
        }
    }
}
