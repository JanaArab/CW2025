package com.comp2042.tetris.model.bricks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates bricks using a bag randomization system.
 * This ensures fair distribution of all brick types by shuffling
 * a complete set of all seven pieces before dispensing them.
 *
 * <p>The bag system prevents long droughts of specific pieces
 * while maintaining unpredictability in the sequence.</p>
 *
 * @see BrickGenerator
 * @see BrickBagPolicy
 * @see ShuffleBagPolicy
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private final BrickBagPolicy bagPolicy;

    private final int previewSize;

    /**
     * Constructs a RandomBrickGenerator with default bag policy and preview size.
     */
    public RandomBrickGenerator() {
        this(new ShuffleBagPolicy(), 3);
    }

    /**
     * Constructs a RandomBrickGenerator with custom bag policy and preview size.
     *
     * @param bagPolicy the policy for creating shuffled bags of bricks
     * @param previewSize the number of bricks to keep in the preview queue
     */
    public RandomBrickGenerator(BrickBagPolicy bagPolicy, int previewSize) {
        this.bagPolicy = bagPolicy;
        this.previewSize = Math.max(1, previewSize);
        this.brickList = BrickType.prototypes();
        refillIfNeeded();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brick getBrick() {
        refillIfNeeded();
        return nextBricks.poll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brick getNextBrick() {
        refillIfNeeded();
        return nextBricks.peek();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brick> getNextBricks(int count) {
        refillIfNeeded();
        return nextBricks.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Refills the queue with new shuffled bags when running low.
     */
    private void refillIfNeeded() {
        while (nextBricks.size() < previewSize) {
            nextBricks.addAll(bagPolicy.createBag(brickList));
        }
    }
}
