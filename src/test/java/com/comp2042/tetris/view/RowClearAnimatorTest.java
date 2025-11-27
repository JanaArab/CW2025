package com.comp2042.tetris.view;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the RowClearAnimator class.
 * Verifies row clearing animations work correctly.
 */
class RowClearAnimatorTest {

    private RowClearAnimator animator;
    private GridPane gamePanel;

    @BeforeEach
    void setUp() {
        animator = new RowClearAnimator();
        gamePanel = new GridPane();
    }

    @Test
    void testAnimatorNotNull() {
        assertNotNull(animator, "Animator should be instantiated");
    }

    @Test
    void testAnimateClearRowsWithNullGamePanel() {
        final boolean[] callbackCalled = {false};

        animator.animateClearRows(null, Arrays.asList(2, 3), () -> {
            callbackCalled[0] = true;
        });

        // Should complete immediately with null panel
        assertTrue(callbackCalled[0], "Callback should be called with null panel");
    }

    @Test
    void testAnimateClearRowsWithEmptyList() {
        final boolean[] callbackCalled = {false};

        animator.animateClearRows(gamePanel, Arrays.asList(), () -> {
            callbackCalled[0] = true;
        });

        // Should complete immediately with empty list
        assertTrue(callbackCalled[0], "Callback should be called with empty list");
    }

    @Test
    void testAnimateClearRowsWithNullList() {
        final boolean[] callbackCalled = {false};

        animator.animateClearRows(gamePanel, null, () -> {
            callbackCalled[0] = true;
        });

        // Should complete immediately with null list
        assertTrue(callbackCalled[0], "Callback should be called with null list");
    }

    @Test
    void testAnimateClearRowsWithFlashNullSafety() {
        final boolean[] callbackCalled = {false};

        animator.animateClearRowsWithFlash(null, Arrays.asList(2), () -> {
            callbackCalled[0] = true;
        });

        assertTrue(callbackCalled[0], "Should handle null panel gracefully");
    }

    @Test
    void testCallbackWithNullCallbackDoesNotCrash() {
        assertDoesNotThrow(() -> {
            animator.animateClearRows(gamePanel, Arrays.asList(2), null);
        }, "Should handle null callback without crashing");
    }

    @Test
    void testCallbackWithNullCallbackFlashDoesNotCrash() {
        assertDoesNotThrow(() -> {
            animator.animateClearRowsWithFlash(gamePanel, Arrays.asList(2), null);
        }, "Should handle null callback without crashing in flash animation");
    }
}

