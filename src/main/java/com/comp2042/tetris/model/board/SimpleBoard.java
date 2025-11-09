/**
 * handles all functions in the game
 */

package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.Brick;
import com.comp2042.tetris.model.bricks.BrickGenerator;
import com.comp2042.tetris.model.bricks.BrickRotator;
import com.comp2042.tetris.model.bricks.RandomBrickGenerator;
import com.comp2042.tetris.model.data.MatrixOperations;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.NextShapeInfo;
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
        this.rows = rows;
        this.cols = cols;
        currentGameMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        // fixed the rotation next to borders bug
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.peekNextRotation();
        int[][] nextShapeMatrix = nextShape.getShape();

        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();


        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShapeMatrix, currentX, currentY);
        if (!conflict) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        int[] kicks ={-1,1,-2,2};
        for(int dx:kicks){
            if (!MatrixOperations.intersect(currentMatrix, nextShapeMatrix, currentX + dx, currentY)) {
                // Update X offset since we applied a kick
                currentOffset = new Point(currentX + dx, currentY);
                brickRotator.setCurrentShape(nextShape.getPosition());
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        // Use y=2: board rows 0-1 are hidden; row index 2 maps to the top visible row
        currentOffset = new Point(4, 1);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());

    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
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
