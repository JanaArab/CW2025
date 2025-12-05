package com.comp2042.tetris.model.level;

/**
 * Level 1 with progressive speed increase based on score.
 * Features dynamic difficulty where the game speeds up as
 * the player earns more points.
 *
 * <p>Speed configuration:</p>
 * <ul>
 *   <li>Base speed: 5 drops per second</li>
 *   <li>Speed increment: +1.5 per threshold</li>
 *   <li>Score threshold: every 100 points</li>
 *   <li>Max speed cap at: 500 points</li>
 * </ul>
 *
 * @see DynamicLevel
 * @see GameLevel
 */
public class Level1 extends DynamicLevel {
    private static final double BASE_SPEED = 5.0;

    /**
     * Constructs Level 1 with its specific speed configuration.
     */
    public Level1() {
        super(BASE_SPEED, 1.5, 100, 500);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Level 1";
    }
}
