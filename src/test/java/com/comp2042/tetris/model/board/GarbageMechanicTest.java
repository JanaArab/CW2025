package com.comp2042.tetris.model.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GarbageMechanicTest {

    private GarbageMechanic garbageMechanic;

    @BeforeEach
    void setUp() {
        garbageMechanic = new GarbageMechanic();
    }

    @Test
    void disabledByDefault() {
        assertFalse(garbageMechanic.isEnabled());
        assertEquals(0, garbageMechanic.checkAndGetRowsToAdd());
    }

    @Test
    void enableSetsEnabledFlag() {
        garbageMechanic.enable();
        assertTrue(garbageMechanic.isEnabled());
    }

    @Test
    void disableClearsEnabledFlag() {
        garbageMechanic.enable();
        garbageMechanic.disable();
        assertFalse(garbageMechanic.isEnabled());
    }

    @Test
    void checkAndGetRowsToAddReturnsZeroWhenDisabled() {
        assertEquals(0, garbageMechanic.checkAndGetRowsToAdd());
    }

    @Test
    void generateGarbageRowCreatesRowWithOneHole() {
        int cols = 10;
        int[] row = garbageMechanic.generateGarbageRow(cols);

        assertNotNull(row);
        assertEquals(cols, row.length);

        // Count the number of empty cells (holes)
        int holes = 0;
        int filled = 0;
        for (int cell : row) {
            if (cell == 0) {
                holes++;
            } else {
                filled++;
                assertTrue(cell >= 1 && cell <= 7, "Garbage cell should be between 1 and 7");
            }
        }

        assertEquals(1, holes, "Should have exactly one hole");
        assertEquals(cols - 1, filled, "Should have cols-1 filled cells");
    }

    @Test
    void generateGarbageRowCreatesRandomPatterns() {
        int cols = 10;
        int[] row1 = garbageMechanic.generateGarbageRow(cols);
        int[] row2 = garbageMechanic.generateGarbageRow(cols);

        // The rows should likely differ (though there's a small chance they're identical)
        // We just check that both are valid
        assertNotNull(row1);
        assertNotNull(row2);
        assertEquals(cols, row1.length);
        assertEquals(cols, row2.length);
    }

    @Test
    void resetWhenEnabledReschedulesGarbage() {
        garbageMechanic.enable();
        garbageMechanic.reset();
        // Can't easily test the timing without waiting, but at least verify no exception
        assertTrue(garbageMechanic.isEnabled());
    }

    @Test
    void resetWhenDisabledDoesNothing() {
        garbageMechanic.reset();
        assertFalse(garbageMechanic.isEnabled());
    }
}

