/**
 * handles all functions in the game
 */

package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.Brick;
import com.comp2042.tetris.model.bricks.BrickGenerator;
import com.comp2042.tetris.model.bricks.BrickRotator;
import com.comp2042.tetris.model.bricks.RandomBrickGenerator;
import com.comp2042.tetris.model.level.ClassicLevel;
import com.comp2042.tetris.model.level.GameLevel;
import com.comp2042.tetris.utils.MatrixOperations;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.score.Score;
import java.awt.Point;
import java.util.List;


public class SimpleBoard implements Board {

    //changed width -> rows, height -> cols
    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    private GameLevel currentLevel;
    private int currentBrickRotationCount = 0;

    public SimpleBoard(int rows, int cols) {
        this(rows, cols, new RandomBrickGenerator(), new BrickRotator(), new Score());
    }
    public SimpleBoard(int rows, int cols, BrickGenerator brickGenerator, BrickRotator brickRotator, Score score) {
        this.rows = rows;
        this.cols = cols;
        this.brickGenerator = brickGenerator;
        this.brickRotator = brickRotator;
        this.score = score;
        this.currentGameMatrix = new int[rows][cols];
        this.currentLevel = new ClassicLevel();
    }

    @Override
    public void setLevel(GameLevel level) {
        this.currentLevel = level;
    }

    @Override
    public boolean moveBrickDown() {
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }
    @Override
    public void addRows(int[][] rowsToAdd) {
        if (rowsToAdd == null || rowsToAdd.length == 0) {
            return;
        }

        for (int[] newRow : rowsToAdd) {
            // Shift everything up by 1
            // Note: matrix[0] is top, matrix[rows-1] is bottom
            for (int row = 0; row < rows - 1; row++) {
                currentGameMatrix[row] = currentGameMatrix[row + 1];
            }

            // Add the new row at the bottom
            currentGameMatrix[rows - 1] = newRow;

            // Push the current falling brick up to prevent immediate clipping if possible
            if (currentOffset.getY() > 0) {
                currentOffset.translate(0, -1);
            }
        }
    }

    @Override
    public boolean moveBrickLeft() {
        Point p = new Point(currentOffset);
        p.translate(-1, 0);

        // MatrixOperations.intersect handles all boundary checking including walls
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        Point p = new Point(currentOffset);
        p.translate(1, 0);

        // MatrixOperations.intersect handles all boundary checking including walls
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int limit = currentLevel.getRotationLimit();

        // 2. If limit exists and we reached it, "Freeze" rotation (return false)
        if (limit != -1 && currentBrickRotationCount >= limit) {
            return false;
        }

        Point adjustedOffset = brickRotator.tryRotateLeft(currentGameMatrix, currentOffset);
        if (adjustedOffset != null) {
            currentOffset = adjustedOffset;
            currentBrickRotationCount++;
            return true;
        }

        return false;
    }

    @Override
    public boolean createNewBrick() {
        currentBrickRotationCount = 0;
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        // Compute horizontal spawn so the piece is centered for any board width.
        int[][] shape = brickRotator.getCurrentShape();
        int shapeWidth = (shape != null && shape.length > 0) ? shape[0].length : 0;
        int spawnX = (cols - shapeWidth) / 2;
        int maxSpawn = Math.max(0, cols - shapeWidth);
        if (spawnX > maxSpawn) {
            spawnX = maxSpawn;
        }


       currentOffset = new Point(spawnX, 1);
        // return whether there is an immediate collision (true => collided)
        return MatrixOperations.intersect(currentGameMatrix, shape, (int) currentOffset.getX(), (int) currentOffset.getY());

    }

    @Override
    public int[][] getBoardMatrix() {
        //return a deep copy of current game matrix
        return MatrixOperations.copy(currentGameMatrix);
    }

    @Override
    public ViewData getViewData() {
        int ghostY = calculateGhostPosition();
        List<int[][]> nextBricksShapes = new java.util.ArrayList<>();
        for (Brick brick : brickGenerator.getNextBricks(3)) {
            nextBricksShapes.add(brick.getShapeMatrix().get(0));
        }
        int currentIndex = brickRotator.getCurrentShapeIndex();
        int stateCount = brickRotator.getRotationStateCount();
        int rotationsUsed = currentBrickRotationCount;
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), nextBricksShapes, ghostY, currentIndex, stateCount, rotationsUsed);
    }

    /**
     * Calculate the Y position where the current brick would land if dropped straight down
     */
    private int calculateGhostPosition() {
        if (brickRotator.getCurrentShape() == null) {
            return (int) currentOffset.getY();
        }

        Point testPos = new Point(currentOffset);
        // Keep moving down until we hit something
        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) testPos.getX(), (int) testPos.getY() + 1)) {
            testPos.translate(0, 1);
        }
        return (int) testPos.getY();
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.newMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[rows][cols];
        score.reset();
        createNewBrick();
    }
}

