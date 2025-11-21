package com.comp2042.tetris.model.bricks;

import java.util.List;

final class SBrick extends AbstractBrick {

    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                    {0, 0, 0, 0},
                    {0, 5, 5, 0},
                    {5, 5, 0, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {5, 0, 0, 0},
                    {5, 5, 0, 0},
                    {0, 5, 0, 0},
                    {0, 0, 0, 0}
            }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
        return new SBrick();
    }
}
