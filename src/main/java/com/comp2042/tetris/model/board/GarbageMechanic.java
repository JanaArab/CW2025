package com.comp2042.tetris.model.board;

import java.util.Random;

/**
 * Manages the timing and generation of garbage rows in Tetris.
 * This class is responsible for scheduling when garbage should be added
 * and generating the garbage row patterns.
 */
public class GarbageMechanic {
    private final Random random = new Random();
    private long nextGarbageTime;
    private boolean enabled = false;

    /**
     * Enable garbage mechanic and schedule the first garbage.
     */
    public void enable() {
        this.enabled = true;
        scheduleNextGarbage();
    }

    /**
     * Disable garbage mechanic.
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Reset the garbage mechanic (typically called on new game).
     */
    public void reset() {
        if (enabled) {
            scheduleNextGarbage();
        }
    }

    /**
     * Check if it's time to add garbage and return how many rows to add.
     * Returns 0 if it's not time yet or garbage is disabled.
     *
     * @return number of garbage rows to add (0 if none)
     */
    public int checkAndGetRowsToAdd() {
        if (!enabled) {
            return 0;
        }

        if (System.nanoTime() >= nextGarbageTime) {
            scheduleNextGarbage();
            return 1 + random.nextInt(2); // 1 or 2 rows
        }
        return 0;
    }

    /**
     * Generate a single garbage row with a random hole.
     *
     * @param cols the number of columns in the board
     * @return an array representing a garbage row
     */
    public int[] generateGarbageRow(int cols) {
        int[] newRow = new int[cols];
        int holeCol = random.nextInt(cols);

        // Fill row with garbage blocks (using random brick colors 1-7)
        for (int col = 0; col < cols; col++) {
            if (col != holeCol) {
                newRow[col] = 1 + random.nextInt(7); // Random brick color
            } else {
                newRow[col] = 0; // The hole
            }
        }
        return newRow;
    }

    /**
     * Schedule the next garbage to appear in 30-45 seconds.
     */
    private void scheduleNextGarbage() {
        // Schedule next garbage in 30-45 seconds (in nanoseconds)
        long delaySeconds = 30L + random.nextInt(16); // 30 to 45
        nextGarbageTime = System.nanoTime() + (delaySeconds * 1_000_000_000L);
    }

    /**
     * Check if garbage mechanic is currently enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }
}

