package com.comp2042.tetris.model.bricks;
import com.comp2042.tetris.utils.MatrixOperations;
import java.util.List;

/**
 * Abstract base class for all Tetris brick implementations.
 * Provides common functionality for shape cloning and retrieval.
 *
 * <p>Subclasses must implement {@link #getPrototypes()} to define
 * the rotation states for the specific brick type.</p>
 *
 * @see Brick
 * @see BrickType
 */
abstract class AbstractBrick implements Brick {

    /**
     * Returns the prototype shape matrices for this brick type.
     * Each matrix represents one rotation state.
     *
     * @return a list of 2D arrays defining all rotation states
     */
    protected abstract List<int[][]> getPrototypes();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> cloneShape() {
        return MatrixOperations.deepCopyList(getPrototypes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return cloneShape();
    }
}
