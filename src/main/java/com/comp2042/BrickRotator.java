package com.comp2042;
 /*
 * this class handles brick rotation as well as keeping
 * track of rotation index
 * */
import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;


    public NextShapeInfo peekNextRotation () {
        /*Changed method name so it would explain the purpose of method more clearly
        * and will add an if condition to check existence of brick, if not it displays error message*/
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {

        /*added an if statement for the same purpose  */
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
