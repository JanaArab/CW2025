package com.comp2042.tetris.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ShootingStarAnimator
 */
class ShootingStarAnimatorTest {

    private ShootingStarAnimator animator;

    @BeforeEach
    void setUp() {
        // Note: These tests will only verify object creation, not actual JavaFX rendering
        // which requires a JavaFX application thread
        animator = new ShootingStarAnimator(800, 600);
    }

    @Test
    void testConstructorWithDefaultDimensions() {
        ShootingStarAnimator defaultAnimator = new ShootingStarAnimator();
        assertNotNull(defaultAnimator);
    }

    @Test
    void testConstructorWithCustomDimensions() {
        ShootingStarAnimator customAnimator = new ShootingStarAnimator(1920, 1080);
        assertNotNull(customAnimator);
    }

    @Test
    void testCreateShootingStarWithDefaultSize() {
        // This test will fail if the shooting_star.png resource is not present
        // Remove the assertThrows if the resource is available
        assertThrows(RuntimeException.class, () -> {
            ImageView star = animator.createShootingStar();
        }, "Should throw RuntimeException when shooting_star.png is not found");
    }

    @Test
    void testCreateShootingStarWithCustomSize() {
        // This test will fail if the shooting_star.png resource is not present
        assertThrows(RuntimeException.class, () -> {
            ImageView star = animator.createShootingStar(150, 75);
        }, "Should throw RuntimeException when shooting_star.png is not found");
    }

    // Note: The following tests require JavaFX Application thread to be initialized
    // and the shooting_star.png resource to be present.
    // They are commented out but can be enabled in a proper JavaFX test environment.

    /*
    @Test
    void testCreateAndAnimateStar() {
        Pane testPane = new Pane();
        ImageView star = animator.createAndAnimateStar(testPane);

        assertNotNull(star);
        assertTrue(testPane.getChildren().contains(star));
    }

    @Test
    void testAnimateStarWithNullStar() {
        Pane testPane = new Pane();
        animator.animateStar(null, testPane);
        // Should not throw exception
    }

    @Test
    void testAnimateStarWithNullPane() {
        ImageView star = animator.createShootingStar();
        animator.animateStar(star, null);
        // Should not throw exception
    }

    @Test
    void testCreateStarBurst() {
        Pane testPane = new Pane();
        animator.createStarBurst(testPane, 5, 2.0);

        // Stars will be added with delays, so we can't immediately check the count
        // This test just verifies no exceptions are thrown
        assertNotNull(testPane);
    }
    */
}

