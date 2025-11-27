package com.comp2042.tetris.model.bricks;

import java.util.List;

public interface BrickGenerator {

    Brick getBrick();

    Brick getNextBrick();

    List<Brick> getNextBricks(int count);
}
