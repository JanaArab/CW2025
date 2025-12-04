package com.comp2042.tetris.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Interpolator;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * NebulaCloudAnimator creates and animates colorful nebula gas clouds.
 * These clouds drift slowly across the screen with pulsing glow effects,
 * creating a cosmic atmosphere.
 */
public class NebulaCloudAnimator {

    private static final double CLOUD_MIN_SIZE = 80;
    private static final double CLOUD_MAX_SIZE = 200;
    private static final double DRIFT_DURATION_MIN = 30.0;
    private static final double DRIFT_DURATION_MAX = 60.0;
    private static final double PULSE_DURATION = 4.0;
    private static final double BLUR_RADIUS = 40;

    private final Random random;
    private final double screenWidth;
    private final double screenHeight;

    /**
     * Creates a new NebulaCloudAnimator with default screen dimensions.
     */
    public NebulaCloudAnimator() {
        this(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
    }

    /**
     * Creates a new NebulaCloudAnimator with custom screen dimensions.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public NebulaCloudAnimator(double screenWidth, double screenHeight) {
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Creates a nebula cloud with random properties.
     *
     * @return a Circle representing a nebula cloud
     */
    public Circle createNebulaCloud() {
        // Random size
        double radius = CLOUD_MIN_SIZE + random.nextDouble() * (CLOUD_MAX_SIZE - CLOUD_MIN_SIZE);

        Circle cloud = new Circle(radius);

        // Random nebula color - cosmic colors
        Color[] nebulaColors = {
            Color.rgb(148, 0, 211, 0.3),    // Deep Purple
            Color.rgb(75, 0, 130, 0.3),     // Indigo
            Color.rgb(138, 43, 226, 0.3),   // Blue-Violet
            Color.rgb(255, 20, 147, 0.3),   // Deep Pink
            Color.rgb(0, 191, 255, 0.3),    // Deep Sky Blue
            Color.rgb(218, 112, 214, 0.3),  // Orchid
            Color.rgb(72, 209, 204, 0.3),   // Turquoise
            Color.rgb(255, 105, 180, 0.3)   // Hot Pink
        };

        Color baseColor = nebulaColors[random.nextInt(nebulaColors.length)];

        // Create radial gradient from center (bright) to edge (transparent)
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(
                (int)(baseColor.getRed() * 255),
                (int)(baseColor.getGreen() * 255),
                (int)(baseColor.getBlue() * 255),
                0.6  // Brighter in center
            )),
            new Stop(0.5, baseColor),
            new Stop(1, Color.rgb(
                (int)(baseColor.getRed() * 255),
                (int)(baseColor.getGreen() * 255),
                (int)(baseColor.getBlue() * 255),
                0.0  // Transparent at edges
            ))
        );

        cloud.setFill(gradient);

        // Add heavy blur for nebula effect
        GaussianBlur blur = new GaussianBlur(BLUR_RADIUS);
        cloud.setEffect(blur);

        // Random starting position
        double startX = -radius + random.nextDouble() * (screenWidth + 2 * radius);
        double startY = random.nextDouble() * screenHeight;
        cloud.setLayoutX(startX);
        cloud.setLayoutY(startY);

        // Initial opacity
        cloud.setOpacity(0.5 + random.nextDouble() * 0.3);

        return cloud;
    }

    /**
     * Animates a nebula cloud with slow drifting and pulsing effect.
     *
     * @param cloud      the cloud to animate
     * @param parentPane the pane containing the cloud
     */
    public void animateCloud(Circle cloud, Pane parentPane) {
        if (cloud == null || parentPane == null) {
            return;
        }

        // Random drift duration (very slow)
        double duration = DRIFT_DURATION_MIN +
                         random.nextDouble() * (DRIFT_DURATION_MAX - DRIFT_DURATION_MIN);

        // Slow horizontal drift
        TranslateTransition drift = new TranslateTransition(Duration.seconds(duration), cloud);
        drift.setByX(random.nextDouble() < 0.5 ? -100 - random.nextDouble() * 100 : 100 + random.nextDouble() * 100);
        drift.setByY(-50 + random.nextDouble() * 100); // Slight vertical movement
        drift.setInterpolator(Interpolator.LINEAR);

        // Pulsing opacity effect
        FadeTransition pulseOut = new FadeTransition(Duration.seconds(PULSE_DURATION), cloud);
        pulseOut.setFromValue(cloud.getOpacity());
        pulseOut.setToValue(0.2);

        FadeTransition pulseIn = new FadeTransition(Duration.seconds(PULSE_DURATION), cloud);
        pulseIn.setFromValue(0.2);
        pulseIn.setToValue(cloud.getOpacity());

        // Subtle scale pulsing (breathing effect)
        ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(PULSE_DURATION * 1.5), cloud);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(PULSE_DURATION * 1.5), cloud);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Combine animations
        ParallelTransition animation = new ParallelTransition();
        animation.getChildren().addAll(drift);

        // Make pulsing cycle continuously
        pulseOut.setOnFinished(e -> pulseIn.play());
        pulseIn.setOnFinished(e -> pulseOut.play());
        pulseOut.play();

        scaleUp.setOnFinished(e -> scaleDown.play());
        scaleDown.setOnFinished(e -> scaleUp.play());
        scaleUp.play();

        // Remove cloud when drift animation completes
        animation.setOnFinished(e -> {
            parentPane.getChildren().remove(cloud);
            // Create a new cloud to replace it
            createAndAnimateCloud(parentPane);
        });

        animation.play();
    }

    /**
     * Creates and animates a new nebula cloud.
     *
     * @param parentPane the pane to add the cloud to
     * @return the created and animated cloud
     */
    public Circle createAndAnimateCloud(Pane parentPane) {
        Circle cloud = createNebulaCloud();
        parentPane.getChildren().add(cloud);
        // Ensure clouds are sent to back (behind other elements)
        cloud.toBack();
        animateCloud(cloud, parentPane);
        return cloud;
    }

    /**
     * Fills the screen with multiple nebula clouds.
     *
     * @param parentPane the pane to add clouds to
     * @param cloudCount the number of clouds to create
     */
    public void fillWithClouds(Pane parentPane, int cloudCount) {
        for (int i = 0; i < cloudCount; i++) {
            // Add delay to stagger cloud creation
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(
                Duration.seconds(random.nextDouble() * 2)
            );
            delay.setOnFinished(e -> createAndAnimateCloud(parentPane));
            delay.play();
        }
    }
}

