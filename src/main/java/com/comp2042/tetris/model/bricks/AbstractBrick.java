package com.comp2042.tetris.model.bricks;
import com.comp2042.tetris.utils.MatrixOperations;
import java.util.List;

abstract class AbstractBrick implements Brick {
    protected abstract List<int[][]> getPrototypes();

    @Override
    public List<int[][]> cloneShape() {
        return MatrixOperations.deepCopyList(getPrototypes());
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return cloneShape();
    }
}
