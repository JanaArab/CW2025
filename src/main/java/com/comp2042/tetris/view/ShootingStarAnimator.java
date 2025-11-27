package com.comp2042.tetris.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

/**
 * ShootingStarAnimator creates and animates shooting star effects across the game screen.
 * The shooting stars move diagonally from top-left to bottom-right with a fading effect.
 * Stars are generated programmatically using JavaFX shapes.
 */
public class ShootingStarAnimator {

    private static final double DEFAULT_STAR_LENGTH = 100;
    private static final double DEFAULT_STAR_THICKNESS = 3;
    private static final double ANIMATION_DURATION_MIN = 1.5;  // Back to slower speed
    private static final double ANIMATION_DURATION_MAX = 3.0;  // Back to slower speed
    private static final double FADE_DURATION = 1.0;  // Original fade duration

    private final Random random;
    private final double screenWidth;
    private final double screenHeight;

    /**
     * Creates a new ShootingStarAnimator with default screen dimensions.
     */
    public ShootingStarAnimator() {
        this(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
    }

    /**
     * Creates a new ShootingStarAnimator with custom screen dimensions.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public ShootingStarAnimator(double screenWidth, double screenHeight) {
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Creates a new shooting star Group with default size.
     *
     * @return a new Group configured as a shooting star
     */
    public Group createShootingStar() {
        return createShootingStar(DEFAULT_STAR_LENGTH, DEFAULT_STAR_THICKNESS);
    }

    /**
     * Creates a new shooting star Group with custom size.
     *
     * @param length    the length of the star trail
     * @param thickness the thickness of the star trail
     * @return a new Group configured as a shooting star
     */
    public Group createShootingStar(double length, double thickness) {
        Group star = new Group();

        // Define multiple vibrant colors for variety
        Color[] starColors = {
            Color.rgb(200, 100, 255),   // Purple/Violet
            Color.rgb(100, 200, 255),   // Cyan/Blue
            Color.rgb(255, 100, 200),   // Pink/Magenta
            Color.rgb(255, 200, 100),   // Orange/Gold
            Color.rgb(100, 255, 200),   // Mint/Aqua
            Color.rgb(255, 255, 100)    // Yellow
        };

        // Randomly select a color for this star
        Color selectedColor = starColors[random.nextInt(starColors.length)];

        // Calculate mid-tone color for gradient (darker version)
        Color midColor = Color.rgb(
            (int)(selectedColor.getRed() * 200),
            (int)(selectedColor.getGreen() * 200),
            (int)(selectedColor.getBlue() * 200)
        );

        // Create the bright star head (small circle) - RANDOM COLOR!
        Circle starHead = new Circle(thickness * 1.5);
        starHead.setFill(selectedColor);
        GaussianBlur headBlur = new GaussianBlur(5);
        starHead.setEffect(headBlur);

        // Create the trail (gradient rectangle)
        Rectangle trail = new Rectangle(length, thickness);
        trail.setTranslateX(-length); // Trail extends backwards
        trail.setTranslateY(-thickness / 2);

        // Create gradient from transparent to selected color
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.TRANSPARENT),
            new Stop(0.5, Color.rgb(
                (int)(selectedColor.getRed() * 255),
                (int)(selectedColor.getGreen() * 255),
                (int)(selectedColor.getBlue() * 255),
                0.6
            )),
            new Stop(1, selectedColor)
        );
        trail.setFill(gradient);

        // Add blur effect to trail
        GaussianBlur trailBlur = new GaussianBlur(3);
        trail.setEffect(trailBlur);

        // Rotate to point diagonally
        star.setRotate(45);

        star.getChildren().addAll(trail, starHead);
        star.setOpacity(0.9);
        star.setCache(true);

        return star;
    }

    /**
     * Animates a shooting star across the screen from top-left to bottom-right.
     * The star will fade in and out during the animation and be removed from the parent pane when complete.
     *
     * @param star       the Group to animate
     * @param parentPane the pane containing the star (will be removed from this pane after animation)
     */
    public void animateStar(Group star, Pane parentPane) {
        animateStar(star, parentPane, -100, random.nextDouble() * screenHeight * 0.3);
    }

    /**
     * Animates a shooting star across the screen with custom starting position.
     * The star will move diagonally, fade in and out, and be removed from the parent pane when complete.
     *
     * @param star       the Group to animate
     * @param parentPane the pane containing the star (will be removed from this pane after animation)
     * @param startX     the starting X position
     * @param startY     the starting Y position
     */
    public void animateStar(Group star, Pane parentPane, double startX, double startY) {
        if (star == null || parentPane == null) {
            return;
        }

        // Set initial position
        star.setTranslateX(startX);
        star.setTranslateY(startY);

        // Random animation duration
        double duration = ANIMATION_DURATION_MIN +
                         random.nextDouble() * (ANIMATION_DURATION_MAX - ANIMATION_DURATION_MIN);

        // Calculate diagonal movement (moving right and down)
        double deltaX = screenWidth + 200; // Move past the right edge
        double deltaY = screenHeight * 0.4 + random.nextDouble() * screenHeight * 0.3; // Diagonal movement

        // Create translate transition for movement
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), star);
        translateTransition.setByX(deltaX);
        translateTransition.setByY(deltaY);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        // Create fade transitions
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_DURATION * 0.3), star);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.8);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_DURATION), star);
        fadeOut.setDelay(Duration.seconds(duration - FADE_DURATION));
        fadeOut.setFromValue(0.8);
        fadeOut.setToValue(0.0);

        // Combine all animations
        ParallelTransition parallelTransition = new ParallelTransition(
            translateTransition,
            fadeIn,
            fadeOut
        );

        // Remove star from pane when animation completes
        parallelTransition.setOnFinished(e -> parentPane.getChildren().remove(star));

        parallelTransition.play();
    }

    /**
     * Creates and animates a new shooting star, adding it to the specified pane.
     *
     * @param parentPane the pane to add the shooting star to
     * @return the created and animated Group
     */
    public Group createAndAnimateStar(Pane parentPane) {
        Group star = createShootingStar();
        parentPane.getChildren().add(star);
        animateStar(star, parentPane);
        return star;
    }

    /**
     * Creates and animates a new shooting star with custom size.
     *
     * @param parentPane the pane to add the shooting star to
     * @param length     the length of the star trail
     * @param thickness  the thickness of the star trail
     * @return the created and animated Group
     */
    public Group createAndAnimateStar(Pane parentPane, double length, double thickness) {
        Group star = createShootingStar(length, thickness);
        parentPane.getChildren().add(star);
        animateStar(star, parentPane);
        return star;
    }

    /**
     * Creates and animates multiple shooting stars with random delays.
     *
     * @param parentPane the pane to add the shooting stars to
     * @param count      the number of stars to create
     * @param maxDelay   the maximum delay in seconds before each star starts
     */
    public void createStarBurst(Pane parentPane, int count, double maxDelay) {
        for (int i = 0; i < count; i++) {
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                Duration.seconds(random.nextDouble() * maxDelay)
            );
            delay.setOnFinished(e -> createAndAnimateStar(parentPane));
            delay.play();
        }
    }
}




