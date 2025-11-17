package com.comp2042.tetris.model.bricks;
 /*
 * this class handles brick rotation as well as keeping
 * track of rotation index
 * */
import com.comp2042.tetris.model.event.NextShapeInfo;

import java.awt.Point;
import java.util.List;

public class BrickRotator {

    private final RotationStrategy rotationStrategy;
    private Brick brick;
    private int currentShape = 0;

    public BrickRotator() {
        this(new StandardRotationStrategy());
    }
    public BrickRotator( RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }

    public NextShapeInfo peekNextRotation() {
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        int nextShape = (currentShape + 1) % getBrickShapes().size();
        return new NextShapeInfo(getBrickShapes().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {

        /*added an if statement for the same purpose  */
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        return getBrickShapes().get(currentShape);
    }
    public Point tryRotateLeft(int[][] boardMatrix, Point offset) {
        NextShapeInfo nextShapeInfo = peekNextRotation();
        Point kickedOffset = rotationStrategy.findOffsetForRotation(boardMatrix, nextShapeInfo.getShape(), offset);
        if (kickedOffset != null) {
            currentShape = nextShapeInfo.getPosition();
            return kickedOffset;
        }
        return null;
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick.cloneBrick();
        currentShape = 0;
    }
    private List<int[][]> getBrickShapes() {
        return brick.cloneShape();
    }


}
