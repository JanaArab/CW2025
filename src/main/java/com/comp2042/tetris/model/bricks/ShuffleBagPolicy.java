package com.comp2042.tetris.model.bricks;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
public class ShuffleBagPolicy implements BrickBagPolicy {
    @Override
    public Deque<Brick> createBag(List<Brick> prototypes) {
        List<Brick> shuffled = new ArrayList<>();
        for (Brick prototype : prototypes) {
            shuffled.add(prototype.cloneBrick());
        }
        Collections.shuffle(shuffled);
        return new ArrayDeque<>(shuffled);
    }
}
