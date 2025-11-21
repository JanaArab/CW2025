package com.comp2042.tetris.model.bricks;

import java.util.List;

final class ZBrick extends AbstractBrick {

    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                    {0, 0, 0, 0},
                    {7, 7, 0, 0},
                    {0, 7, 7, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 7, 0, 0},
                    {7, 7, 0, 0},
                    {7, 0, 0, 0},
                    {0, 0, 0, 0}
            }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
        return new ZBrick();
    }
}
