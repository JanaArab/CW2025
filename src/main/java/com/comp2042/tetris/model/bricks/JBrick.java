package com.comp2042.tetris.model.bricks;

import java.util.List;

final class JBrick extends AbstractBrick {


    private static final List<int[][]> SHAPES = List.of(
            new int[][]{
                    {0, 0, 0, 0},
                    {2, 2, 2, 0},
                    {0, 0, 2, 0},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0},
                    {0, 2, 2, 0},
                    {0, 2, 0, 0},
                    {0, 2, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0},
                    {0, 2, 0, 0},
                    {0, 2, 2, 2},
                    {0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 2, 0},
                    {0, 0, 2, 0},
                    {0, 2, 2, 0},
                    {0, 0, 0, 0}
            }
    );
    @Override
    protected List<int[][]> getPrototypes() {
        return SHAPES;
    }

    @Override
    public Brick cloneBrick() {
        return new JBrick();
    }
}
