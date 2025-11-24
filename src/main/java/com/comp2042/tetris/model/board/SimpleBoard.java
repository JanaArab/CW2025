/**
 * handles all functions in the game
 */

package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.Brick;
import com.comp2042.tetris.model.bricks.BrickGenerator;
import com.comp2042.tetris.model.bricks.BrickRotator;
import com.comp2042.tetris.model.bricks.RandomBrickGenerator;
import com.comp2042.tetris.utils.MatrixOperations;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.score.Score;
import java.awt.Point;



public class SimpleBoard implements Board {

    //changed width -> rows, height -> cols
    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;


    public SimpleBoard(int rows, int cols) {
        this(rows, cols, new RandomBrickGenerator(), new BrickRotator(), new Score());
    }
    public SimpleBoard(int rows, int cols, BrickGenerator brickGenerator, BrickRotator brickRotator, Score score) {
        this.rows = rows;
        this.cols = cols;
        this.brickGenerator = brickGenerator;
        this.brickRotator = brickRotator;
        this.score = score;
        currentGameMatrix = new int[rows][cols];
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
        Point adjustedOffset = brickRotator.tryRotateLeft(currentGameMatrix, currentOffset);
        if (adjustedOffset != null) {
            currentOffset = adjustedOffset;
            return true;
        }

        return false;
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        // Compute horizontal spawn so the piece is centered for any board width.
        int[][] shape = brickRotator.getCurrentShape();
        int shapeWidth = (shape != null && shape.length > 0) ? shape[0].length : 0;

        int spawnX = (cols - shapeWidth) / 2;
        // Clamp to valid range
        if (spawnX < 0) {
            spawnX = 0;
        }
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
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
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