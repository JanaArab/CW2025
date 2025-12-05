package com.comp2042.tetris.model.bricks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration of all standard Tetris brick types.
 * Each type wraps a prototype brick instance and provides
 * factory methods for creating brick instances.
 *
 * <p>The seven standard Tetris pieces are:</p>
 * <ul>
 *   <li>{@link #I} - Cyan long straight piece (4 cells in a line)</li>
 *   <li>{@link #J} - Blue J-shaped piece</li>
 *   <li>{@link #L} - Orange L-shaped piece</li>
 *   <li>{@link #O} - Yellow square piece (2x2)</li>
 *   <li>{@link #S} - Green S-shaped piece</li>
 *   <li>{@link #T} - Purple T-shaped piece</li>
 *   <li>{@link #Z} - Red Z-shaped piece</li>
 * </ul>
 *
 * @see Brick
 * @see BrickGenerator
 */
public enum BrickType implements Brick {

    /** The I-piece (long straight piece) */
    I(new IBrick()),
    /** The J-piece */
    J(new JBrick()),
    /** The L-piece */
    L(new LBrick()),
    /** The O-piece (square) */
    O(new OBrick()),
    /** The S-piece */
    S(new SBrick()),
    /** The T-piece */
    T(new TBrick()),
    /** The Z-piece */
    Z(new ZBrick());

    private final Brick prototype;

    /**
     * Constructs a BrickType with the given prototype brick.
     *
     * @param prototype the brick prototype to wrap
     */
    BrickType(Brick prototype) {
        this.prototype = prototype;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return prototype.getShapeMatrix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<int[][]> cloneShape() {
        return prototype.cloneShape();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brick cloneBrick() {
        return prototype.cloneBrick();
    }

    /**
     * Returns an unmodifiable list of all brick type prototypes.
     *
     * @return list of all brick types as Brick instances
     */
    public static List<Brick> prototypes() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }
}
