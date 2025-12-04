package com.comp2042.tetris.view;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PixelStarAnimator creates and animates small twinkling pixel stars across the screen.
 * These stars stay in place and twinkle (fade in/out) to create a starfield effect.
 */
public class PixelStarAnimator {

    private static final int DEFAULT_STAR_COUNT = 100;
    private static final double STAR_MIN_SIZE = 1.0;
    private static final double STAR_MAX_SIZE = 3.0;
    private static final double TWINKLE_DURATION_MIN = 1.0;
    private static final double TWINKLE_DURATION_MAX = 3.0;

    private final Random random;
    private final double screenWidth;
    private final double screenHeight;
    private final List<Circle> pixelStars;

    /**
     * Creates a new PixelStarAnimator with default screen dimensions.
     */
    public PixelStarAnimator() {
        this(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
    }

    /**
     * Creates a new PixelStarAnimator with custom screen dimensions.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public PixelStarAnimator(double screenWidth, double screenHeight) {
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pixelStars = new ArrayList<>();
    }

    /**
     * Creates a single pixel star with random properties.
     *
     * @return a Circle representing a pixel star
     */
    public Circle createPixelStar() {
        // Random size between min and max
        double size = STAR_MIN_SIZE + random.nextDouble() * (STAR_MAX_SIZE - STAR_MIN_SIZE);

        // Create small circle
        Circle star = new Circle(size);

        // Random position
        double x = random.nextDouble() * screenWidth;
        double y = random.nextDouble() * screenHeight;
        star.setLayoutX(x);
        star.setLayoutY(y);

        // Random color (white, light blue, light yellow, or light pink for variety)
        Color[] colors = {
            Color.WHITE,
            Color.rgb(200, 220, 255),  // Light blue
            Color.rgb(255, 255, 220),  // Light yellow
            Color.rgb(255, 220, 255)   // Light pink
        };
        star.setFill(colors[random.nextInt(colors.length)]);

        // Random initial opacity
        star.setOpacity(0.3 + random.nextDouble() * 0.7);

        return star;
    }

    /**
     * Creates a twinkling animation for a pixel star.
     *
     * @param star the star to animate
     */
    public void animateTwinkle(Circle star) {
        // Random twinkle duration
        double duration = TWINKLE_DURATION_MIN +
                         random.nextDouble() * (TWINKLE_DURATION_MAX - TWINKLE_DURATION_MIN);

        // Random delay before starting
        double delay = random.nextDouble() * 2.0;

        // Fade out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(duration), star);
        fadeOut.setFromValue(star.getOpacity());
        fadeOut.setToValue(0.1 + random.nextDouble() * 0.3);
        fadeOut.setDelay(Duration.seconds(delay));

        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(duration), star);
        fadeIn.setFromValue(fadeOut.getToValue());
        fadeIn.setToValue(0.5 + random.nextDouble() * 0.5);

        // Create sequential transition (fade out then fade in)
        SequentialTransition twinkle = new SequentialTransition(fadeOut, fadeIn);
        twinkle.setCycleCount(SequentialTransition.INDEFINITE);
        twinkle.play();
    }

    /**
     * Fills the screen with twinkling pixel stars.
     *
     * @param parentPane the pane to add stars to
     * @param starCount  the number of stars to create
     */
    public void fillScreenWithStars(Pane parentPane, int starCount) {
        pixelStars.clear();

        for (int i = 0; i < starCount; i++) {
            Circle star = createPixelStar();
            pixelStars.add(star);
            parentPane.getChildren().add(star);
            animateTwinkle(star);
        }
    }

    /**
     * Fills the screen with the default number of twinkling pixel stars.
     *
     * @param parentPane the pane to add stars to
     */
    public void fillScreenWithStars(Pane parentPane) {
        fillScreenWithStars(parentPane, DEFAULT_STAR_COUNT);
    }

    /**
     * Removes all pixel stars from the parent pane.
     *
     * @param parentPane the pane to remove stars from
     */
    public void clearStars(Pane parentPane) {
        for (Circle star : pixelStars) {
            parentPane.getChildren().remove(star);
        }
        pixelStars.clear();
    }

    /**
     * Gets the list of created pixel stars.
     *
     * @return the list of pixel star circles
     */
    public List<Circle> getPixelStars() {
        return new ArrayList<>(pixelStars);
    }

    /**
     * Gets the number of pixel stars currently managed.
     *
     * @return the star count
     */
    public int getStarCount() {
        return pixelStars.size();
    }
}

