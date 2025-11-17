package com.comp2042.tetris.model.bricks;

import java.util.List;
import java.util.Collections;

final class OBrick extends AbstractBrick {
    private static final List<int[][]> SHAPES = Collections.singletonList(
            new int[][]{
                    {0, 0, 0, 0},
                    {0, 4, 4, 0},
                    {0, 4, 4, 0},
                    {0, 0, 0, 0}
            }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
        return new OBrick();
    }

}
