package com.comp2042.tetris.model.bricks;


import java.util.List;//to store rotations in a list

final class IBrick extends  AbstractBrick {

    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        },
            new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
            return new IBrick();
    }
}
