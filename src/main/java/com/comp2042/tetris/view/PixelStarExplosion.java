package com.comp2042.tetris.view;

import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates and animates a pixel star explosion effect.
 * Stars burst out from a central point and fade away.
 */
public class PixelStarExplosion {

    private static final double STAR_MIN_SIZE = 2.0;
    private static final double STAR_MAX_SIZE = 4.0;
    private static final Duration EXPLOSION_DURATION = Duration.millis(400);
    private static final double MIN_VELOCITY = 80.0;
    private static final double MAX_VELOCITY = 200.0;

    private final Random random;

    public PixelStarExplosion() {
        this.random = new Random();
    }

    /**
     * Creates a star explosion effect at the specified position.
     *
     * @param parentPane The pane to add the explosion effect to
     * @param centerX The x-coordinate of the explosion center
     * @param centerY The y-coordinate of the explosion center
     * @param starCount The number of stars in the explosion
     * @param onComplete Callback to execute after the explosion completes
     */
    public void createExplosion(Pane parentPane, double centerX, double centerY,
                                int starCount, Runnable onComplete) {
        List<Circle> stars = new ArrayList<>();
        List<Timeline> timelines = new ArrayList<>();

        for (int i = 0; i < starCount; i++) {
            Circle star = createExplosionStar(centerX, centerY);
            stars.add(star);
            parentPane.getChildren().add(star);

            // Create animation for this star
            Timeline timeline = animateExplosionStar(star, centerX, centerY);
            timelines.add(timeline);
        }

        // Start all animations
        ParallelTransition allAnimations = new ParallelTransition();
        for (Timeline timeline : timelines) {
            allAnimations.getChildren().add(timeline);
        }

        allAnimations.setOnFinished(_ -> {
            // Remove all stars after animation
            parentPane.getChildren().removeAll(stars);
            if (onComplete != null) {
                onComplete.run();
            }
        });

        allAnimations.play();
    }

    /**
     * Creates multiple explosions along a horizontal line (for row clearing).
     *
     * @param parentPane The pane to add the explosion effects to
     * @param startX The starting x-coordinate
     * @param endX The ending x-coordinate
     * @param y The y-coordinate of the row
     * @param explosionCount The number of explosions to create along the line
     * @param starsPerExplosion The number of stars in each explosion
     * @param onComplete Callback to execute after all explosions complete
     */
    public void createRowExplosions(Pane parentPane, double startX, double endX, double y,
                                   int explosionCount, int starsPerExplosion, Runnable onComplete) {
        if (explosionCount <= 0) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        SequentialTransition sequentialExplosions = new SequentialTransition();
        double spacing = (endX - startX) / (explosionCount - 1);

        for (int i = 0; i < explosionCount; i++) {
            double x = startX + (i * spacing);

            // Create a delayed explosion
            PauseTransition delay = new PauseTransition(Duration.millis(15.0 * i));
            delay.setOnFinished(_ -> createExplosion(parentPane, x, y, starsPerExplosion, null));

            sequentialExplosions.getChildren().add(delay);
        }

        sequentialExplosions.setOnFinished(_ -> {
            // Wait for the last explosion to complete
            PauseTransition finalWait = new PauseTransition(EXPLOSION_DURATION);
            finalWait.setOnFinished(_ -> {
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            finalWait.play();
        });

        sequentialExplosions.play();
    }

    /**
     * Creates a single explosion star at the specified position.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A Circle representing the star
     */
    private Circle createExplosionStar(double x, double y) {
        double size = STAR_MIN_SIZE + random.nextDouble() * (STAR_MAX_SIZE - STAR_MIN_SIZE);
        Circle star = new Circle(size);
        star.setCenterX(x);
        star.setCenterY(y);

        // Random bright colors for explosion stars
        Color[] colors = {
            Color.YELLOW,
            Color.GOLD,
            Color.ORANGE,
            Color.WHITE,
            Color.rgb(255, 200, 100),  // Light orange
            Color.rgb(255, 255, 150)   // Light yellow
        };
        star.setFill(colors[random.nextInt(colors.length)]);
        star.setOpacity(1.0);

        return star;
    }

    /**
     * Animates an explosion star radiating out from center and fading away.
     *
     * @param star The star to animate
     * @param centerX The x-coordinate of the explosion center
     * @param centerY The y-coordinate of the explosion center
     * @return Timeline animation for the star
     */
    private Timeline animateExplosionStar(Circle star, double centerX, double centerY) {
        // Random direction (angle in radians)
        double angle = random.nextDouble() * 2 * Math.PI;

        // Random velocity
        double velocity = MIN_VELOCITY + random.nextDouble() * (MAX_VELOCITY - MIN_VELOCITY);

        // Calculate end position based on angle and velocity
        double distance = velocity * (EXPLOSION_DURATION.toMillis() / 1000.0);
        double endX = centerX + Math.cos(angle) * distance;
        double endY = centerY + Math.sin(angle) * distance;

        // Create timeline for movement
        Timeline timeline = new Timeline();

        // Movement keyframes
        KeyValue kvX = new KeyValue(star.centerXProperty(), endX, Interpolator.EASE_OUT);
        KeyValue kvY = new KeyValue(star.centerYProperty(), endY, Interpolator.EASE_OUT);

        // Fade out keyframe
        KeyValue kvOpacity = new KeyValue(star.opacityProperty(), 0.0, Interpolator.EASE_IN);

        // Scale down keyframe
        KeyValue kvScaleX = new KeyValue(star.scaleXProperty(), 0.3, Interpolator.EASE_IN);
        KeyValue kvScaleY = new KeyValue(star.scaleYProperty(), 0.3, Interpolator.EASE_IN);

        KeyFrame kf = new KeyFrame(EXPLOSION_DURATION, kvX, kvY, kvOpacity, kvScaleX, kvScaleY);
        timeline.getKeyFrames().add(kf);

        return timeline;
    }
}

