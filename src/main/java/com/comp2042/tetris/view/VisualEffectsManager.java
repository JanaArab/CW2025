package com.comp2042.tetris.view;

import com.comp2042.tetris.utils.AudioManager;
import javafx.animation.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

/**
 * VisualEffectsManager manages all visual effects in the game including:
 * - Nebula clouds (background cosmic effects)
 * - Pixel stars (twinkling starfield)
 * - Shooting stars (animated streaks across screen)
 * - Flicker effects (screen flickering for Level 3)
 * - Intro sequence animations
 *
 * This class centralizes visual effect logic that was previously scattered
 * across GuiController, making the controller more focused on coordination.
 */
public class VisualEffectsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisualEffectsManager.class);

    private final Pane pixelStarLayer;
    private final Pane mainContent;
    private final Pane gamePanel;

    private final ShootingStarAnimator shootingStarAnimator;
    private final PixelStarAnimator pixelStarAnimator;
    private final NebulaCloudAnimator nebulaCloudAnimator;

    private Timeline flickerScheduler;
    private Timeline shootingStarTimeline;
    private final Random random;

    /**
     * Creates a new VisualEffectsManager.
     *
     * @param pixelStarLayer the layer where stars and nebula effects are rendered
     * @param mainContent the main content pane (used for flicker effects)
     * @param gamePanel the game panel (used for flicker effects)
     */
    public VisualEffectsManager(Pane pixelStarLayer, Pane mainContent, Pane gamePanel) {
        this.pixelStarLayer = pixelStarLayer;
        this.mainContent = mainContent;
        this.gamePanel = gamePanel;

        this.shootingStarAnimator = new ShootingStarAnimator();
        this.pixelStarAnimator = new PixelStarAnimator();
        this.nebulaCloudAnimator = new NebulaCloudAnimator();
        this.random = new Random();
    }

    /**
     * Plays the complete intro sequence including fade-in effect.
     *
     * @param onComplete callback to execute after intro completes
     */
    public void playIntroSequence(Runnable onComplete) {
        if (pixelStarLayer != null) {
            LOGGER.info("Starting animation layer fade-in. Current opacity: {}", pixelStarLayer.getOpacity());
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2.0), pixelStarLayer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setOnFinished(e -> {
                LOGGER.info("Animation layer fade-in complete. Opacity: {}", pixelStarLayer.getOpacity());
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            fadeIn.play();
        } else {
            LOGGER.warn("pixelStarLayer is null - animations will not be visible");
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    /**
     * Sets up and starts all background visual effects (nebula clouds, pixel stars, shooting stars).
     */
    public void setupBackgroundEffects() {
        setupNebulaClouds();
        setupPixelStars();
        setupShootingStars();
    }

    /**
     * Sets up nebula cloud effects in the background.
     */
    private void setupNebulaClouds() {
        if (pixelStarLayer != null) {
            nebulaCloudAnimator.fillWithClouds(pixelStarLayer, 5);
            LOGGER.info("Nebula clouds created: {} children in pixelStarLayer", pixelStarLayer.getChildren().size());
        } else {
            LOGGER.warn("Cannot setup nebula clouds - pixelStarLayer is null");
        }
    }

    /**
     * Sets up pixel star effects (twinkling starfield).
     */
    private void setupPixelStars() {
        if (pixelStarLayer != null) {
            pixelStarAnimator.fillScreenWithStars(pixelStarLayer, 150);
            LOGGER.info("Pixel stars created: {} children in pixelStarLayer", pixelStarLayer.getChildren().size());
        } else {
            LOGGER.warn("Cannot setup pixel stars - pixelStarLayer is null");
        }
    }

    /**
     * Sets up shooting star effects with random timing.
     */
    private void setupShootingStars() {
        if (pixelStarLayer != null) {
            shootingStarTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (Math.random() < 0.8) {
                    shootingStarAnimator.createAndAnimateStar(pixelStarLayer);
                }
            }));
            shootingStarTimeline.setCycleCount(Animation.INDEFINITE);
            shootingStarTimeline.play();
            LOGGER.info("Shooting star timeline started");
        } else {
            LOGGER.warn("Cannot setup shooting stars - pixelStarLayer is null");
        }
    }

    /**
     * Starts the flicker effect for Level 3.
     * The screen will periodically flicker every 30 seconds.
     */
    public void startFlickerEffect() {
        if (flickerScheduler != null) {
            flickerScheduler.stop();
        }

        flickerScheduler = new Timeline(new KeyFrame(Duration.seconds(30), e -> performFlicker()));
        flickerScheduler.setCycleCount(Timeline.INDEFINITE);
        flickerScheduler.play();
        LOGGER.info("Flicker effect started");
    }

    /**
     * Stops the flicker effect and resets opacity to normal.
     */
    public void stopFlickerEffect() {
        if (flickerScheduler != null) {
            flickerScheduler.stop();
            flickerScheduler = null;
            LOGGER.info("Flicker effect stopped");
        }

        if (gamePanel != null) {
            gamePanel.setOpacity(1.0);
        }

        if (mainContent != null) {
            mainContent.setOpacity(1.0);
        }

        try {
            AudioManager.getInstance().stopFlickering();
        } catch (Throwable ignored) {
            // Audio manager may not be available
        }
    }

    /**
     * Performs a single flicker animation with synchronized audio.
     */
    private void performFlicker() {
        if (mainContent == null) {
            return;
        }

        try {
            AudioManager.getInstance().playFlickering();
        } catch (Throwable ignored) {
            // Audio manager may not be available
        }

        Timeline flickerAnim = new Timeline();
        int flickerCount = 10 + random.nextInt(10);
        double stepInterval = 0.05;

        // Synchronize visual duration with audio duration
        try {
            double audioDur = AudioManager.getInstance().getFlickeringDurationSeconds();
            double visualDur = flickerCount * stepInterval;
            if (audioDur > visualDur + 0.05) {
                stepInterval = audioDur / flickerCount;
            }
        } catch (Throwable ignored) {
            // Use default timing if audio duration unavailable
        }

        // Create random opacity keyframes for flicker effect
        for (int i = 0; i < flickerCount; i++) {
            double timeOffset = i * stepInterval;
            double randomOpacity = 0.3 + random.nextDouble() * 0.7;
            flickerAnim.getKeyFrames().add(
                new KeyFrame(Duration.seconds(timeOffset), evt -> mainContent.setOpacity(randomOpacity))
            );
        }

        // Restore full opacity at the end
        flickerAnim.getKeyFrames().add(
            new KeyFrame(Duration.seconds(flickerCount * stepInterval), evt -> mainContent.setOpacity(1.0))
        );

        flickerAnim.play();

        // Stop flickering sound after animation completes
        double flickerDurationSeconds = flickerCount * stepInterval;
        try {
            PauseTransition stopSound = new PauseTransition(Duration.seconds(flickerDurationSeconds));
            stopSound.setOnFinished(e -> {
                try {
                    AudioManager.getInstance().stopFlickering();
                } catch (Throwable ignored) {
                    // Audio manager may not be available
                }
            });
            stopSound.play();
        } catch (Throwable ignored) {
            // Continue even if sound timing fails
        }
    }

    /**
     * Stops all visual effects and cleans up resources.
     */
    public void cleanup() {
        stopFlickerEffect();

        if (shootingStarTimeline != null) {
            shootingStarTimeline.stop();
            shootingStarTimeline = null;
        }

        LOGGER.info("Visual effects cleaned up");
    }
}

