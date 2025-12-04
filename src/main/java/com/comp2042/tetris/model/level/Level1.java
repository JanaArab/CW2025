package com.comp2042.tetris.model.level;

public class Level1 extends DynamicLevel {
    private static final double BASE_SPEED = 5.0;

    public Level1() {
        // Speed +0.5 every 1000 points, stops at 3000
        super(BASE_SPEED, 1.5, 100, 500);
    }

    @Override
    public String getName() {
        return "Level 1";
    }
}
