package com.comp2042.tetris.model.bricks;

import java.util.Deque;
import java.util.List;

public interface BrickBagPolicy {
    Deque<Brick> createBag(List<Brick> prototypes);
}
