package com.comp2042.tetris.model.bricks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private final BrickBagPolicy bagPolicy;

    private final int previewSize;

    public RandomBrickGenerator() {
        this(new ShuffleBagPolicy(), 3);
    }

    public RandomBrickGenerator(BrickBagPolicy bagPolicy, int previewSize) {
        this.bagPolicy = bagPolicy;
        this.previewSize = Math.max(1, previewSize);
        this.brickList = BrickType.prototypes();
        refillIfNeeded();

    }

    @Override
    public Brick getBrick() {
        refillIfNeeded();
        return nextBricks.poll();
    }

    @Override
    public Brick getNextBrick() {
        refillIfNeeded();
        return nextBricks.peek();
    }

    @Override
    public List<Brick> getNextBricks(int count) {
        refillIfNeeded();
        return nextBricks.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private void refillIfNeeded() {
        while (nextBricks.size() < previewSize) {
            nextBricks.addAll(bagPolicy.createBag(brickList));
        }
    }
}
