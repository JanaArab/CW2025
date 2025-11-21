package com.comp2042.tetris.model.bricks;

import java.util.List;

final class TBrick extends AbstractBrick {

    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                    {0, 0, 0, 0},
                    {6, 6, 6, 0},
                    {0, 6, 0, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 6, 0, 0},
                    {0, 6, 6, 0},
                    {0, 6, 0, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 6, 0, 0},
                    {6, 6, 6, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 6, 0, 0},
                    {6, 6, 0, 0},
                    {0, 6, 0, 0},
                    {0, 0, 0, 0}
            }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
        return new TBrick();
    }
}
