package com.comp2042.tetris.model.bricks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BrickType implements Brick{

    I(new IBrick()),
    J(new JBrick()),
    L(new LBrick()),
    O(new OBrick()),
    S(new SBrick()),
    T(new TBrick()),
    Z(new ZBrick());

    private final Brick prototype;

    BrickType(Brick prototype) {
        this.prototype = prototype;
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return prototype.getShapeMatrix();
    }

    @Override
    public List<int[][]> cloneShape() {
        return prototype.cloneShape();
    }

    @Override
    public Brick cloneBrick() {
        return prototype.cloneBrick();
    }

    public static List<Brick> prototypes() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }
}
