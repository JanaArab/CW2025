package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.Brick;
import com.comp2042.tetris.model.bricks.BrickGenerator;
import com.comp2042.tetris.model.bricks.BrickRotator;
import com.comp2042.tetris.model.bricks.RotationStrategy;
import com.comp2042.tetris.model.bricks.StandardRotationStrategy;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.score.Score;
import com.comp2042.tetris.utils.MatrixOperations;
import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private static final int[][] SINGLE_CELL = {{1}};
    private static final List<int[][]> SINGLE_SHAPE = List.<int[][]>of(MatrixOperations.copy(SINGLE_CELL));
    private static final List<int[][]> LINE_SHAPES = List.<int[][]>of(
            MatrixOperations.copy(new int[][]{{1, 1}}),
            MatrixOperations.copy(new int[][]{{1}, {1}})
    );

    @Test
    void moveBrickDownMovesWhenPathClear() {
        SimpleBoard board = createBoard(6, 4, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());

        assertTrue(board.moveBrickDown());
        assertEquals(2, board.getViewData().getYPosition());
    }

    @Test
    void moveBrickDownStopsWhenCollisionDetected() {
        SimpleBoard board = createBoard(6, 4, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());
        ViewData view = board.getViewData();
        int[][] matrix = accessMatrix(board);
        matrix[view.getYPosition() + 1][view.getXPosition()] = 8;

        assertFalse(board.moveBrickDown());
        assertEquals(view.getYPosition(), board.getViewData().getYPosition());
    }

    @Test
    void moveBrickLeftMovesWhenSpaceAvailable() {
        SimpleBoard board = createBoard(6, 6, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());

        assertTrue(board.moveBrickLeft());
        assertEquals(1, board.getViewData().getXPosition());
    }

    @Test
    void moveBrickRightStopsWhenBlocked() {
        SimpleBoard board = createBoard(6, 6, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());
        ViewData view = board.getViewData();
        int[][] matrix = accessMatrix(board);
        matrix[view.getYPosition()][view.getXPosition() + 1] = 3;

        assertFalse(board.moveBrickRight());
        assertEquals(view.getXPosition(), board.getViewData().getXPosition());
    }

    @Test
    void rotateLeftBrickChangesShapeWhenRotationPossible() {
        SimpleBoard board = createBoard(6, 6, LINE_SHAPES, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());

        assertTrue(board.rotateLeftBrick());
        int[][] expected = LINE_SHAPES.get(1);
        assertArrayEquals(expected, board.getViewData().getBrickData());
    }

    @Test
    void rotateLeftBrickReturnsFalseWhenStrategyRejectsRotation() {
        SimpleBoard board = createBoard(6, 6, LINE_SHAPES, new RejectingRotationStrategy());
        assertFalse(board.createNewBrick());

        assertFalse(board.rotateLeftBrick());
        assertArrayEquals(LINE_SHAPES.get(0), board.getViewData().getBrickData());
    }

    @Test
    void mergeBrickToBackgroundCopiesCurrentShapeIntoMatrix() {
        SimpleBoard board = createBoard(6, 6, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());
        ViewData view = board.getViewData();

        board.mergeBrickToBackground();
        int[][] matrix = board.getBoardMatrix();
        assertEquals(1, matrix[view.getYPosition()][view.getXPosition()]);
    }

    @Test
    void clearRowsRemovesFilledLinesAndUpdatesMatrix() {
        SimpleBoard board = createBoard(6, 4, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());
        int[][] matrix = accessMatrix(board);
        int bottomRow = matrix.length - 1;
        for (int col = 0; col < matrix[bottomRow].length; col++) {
            matrix[bottomRow][col] = 4;
        }

        ClearRow clearRow = board.clearRows();
        assertEquals(1, clearRow.linesRemoved());
        int[][] updatedMatrix = board.getBoardMatrix();
        for (int value : updatedMatrix[bottomRow]) {
            assertEquals(0, value);
        }
    }

    @Test
    void createNewBrickCentersPieceAndReportsNoCollision() {
        SimpleBoard board = createBoard(6, 6, SINGLE_SHAPE, new StandardRotationStrategy());
        assertFalse(board.createNewBrick());

        assertEquals(2, board.getViewData().getXPosition());
        assertEquals(1, board.getViewData().getYPosition());
    }

    @Test
    void createNewBrickReturnsTrueWhenSpawnLocationOccupied() {
        SimpleBoard board = createBoard(6, 4, SINGLE_SHAPE, new StandardRotationStrategy());
        int[][] matrix = accessMatrix(board);
        int spawnX = (4 - SINGLE_CELL[0].length) / 2;
        matrix[1][spawnX] = 5;

        assertTrue(board.createNewBrick());
    }

    private static SimpleBoard createBoard(int rows, int cols, List<int[][]> shapes, RotationStrategy rotationStrategy) {
        StubBrick brickOne = new StubBrick(shapes);
        StubBrick brickTwo = new StubBrick(shapes);
        StubBrickGenerator generator = new StubBrickGenerator(List.of(brickOne, brickTwo));
        BrickRotator rotator = new BrickRotator(rotationStrategy);
        return new SimpleBoard(rows, cols, generator, rotator, new Score());
    }

    private static int[][] accessMatrix(SimpleBoard board) {
        try {
            Field field = SimpleBoard.class.getDeclaredField("currentGameMatrix");
            field.setAccessible(true);
            return (int[][]) field.get(board);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class StubBrick implements Brick {
        private final List<int[][]> shapes;

        private StubBrick(List<int[][]> shapes) {
            this.shapes = MatrixOperations.deepCopyList(shapes);
        }

        @Override
        public List<int[][]> getShapeMatrix() {
            return MatrixOperations.deepCopyList(shapes);
        }

        @Override
        public List<int[][]> cloneShape() {
            return MatrixOperations.deepCopyList(shapes);
        }

        @Override
        public Brick cloneBrick() {
            return new StubBrick(shapes);
        }
    }

    private static final class StubBrickGenerator implements BrickGenerator {
        private final Deque<Brick> bricks = new ArrayDeque<>();
        private Brick lastReturned;

        private StubBrickGenerator(List<Brick> initialBricks) {
            this.bricks.addAll(initialBricks);
            this.lastReturned = initialBricks.getLast();
        }

        @Override
        public Brick getBrick() {
            if (bricks.isEmpty()) {
                return lastReturned;
            }
            lastReturned = bricks.pollFirst();
            return lastReturned;
        }

        @Override
        public Brick getNextBrick() {
            Brick next = bricks.peekFirst();
            return next != null ? next : lastReturned;
        }

        @Override
        public java.util.List<Brick> getNextBricks(int count) {
            java.util.List<Brick> result = new java.util.ArrayList<>();
            java.util.Iterator<Brick> iterator = bricks.iterator();
            for (int i = 0; i < count; i++) {
                if (iterator.hasNext()) {
                    result.add(iterator.next());
                } else {
                    result.add(lastReturned);
                }
            }
            return result;
        }
    }

    private static final class RejectingRotationStrategy implements RotationStrategy {
        @Override
        public Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset) {
            return null;
        }
    }
}