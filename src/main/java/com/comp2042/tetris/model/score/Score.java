package com.comp2042.tetris.model.score;

/**
 * Represents the player's score in the game.
 * Provides methods to get, add to, and reset the score value.
 *
 * <p>This is a simple mutable score container used by the game logic
 * to track the player's points earned from dropping bricks and clearing lines.</p>
 *
 * @see ScoreManager
 * @see ScorePolicy
 */
public final class Score {

    private int value;

    /**
     * Gets the current score value.
     *
     * @return the current score
     */
    public int getValue() {
        return value;
    }

    /**
     * Adds points to the current score.
     *
     * @param delta the points to add (can be negative)
     */
    public void add(int delta) {
        value += delta;
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        value = 0;
    }
}
