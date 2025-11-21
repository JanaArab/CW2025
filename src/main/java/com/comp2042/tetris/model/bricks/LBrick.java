package com.comp2042.tetris.model.bricks;

import java.util.List;

final class LBrick extends AbstractBrick {

    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 3},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
            },
            new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 3, 0},
                {0, 0, 3, 0}
            },
            new int[][]{
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0}
            },
            new int[][]{
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
            }
        );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    public Brick cloneBrick() {
        return new LBrick();
    }
}
