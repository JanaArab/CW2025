package com.comp2042.tetris.model.bricks;

import java.util.List;

public interface Brick extends Cloneable {

    List<int[][]> getShapeMatrix();

    List<int[][]> cloneShape();

    Brick cloneBrick();
}
