package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.utils.AudioManager;
import com.comp2042.tetris.utils.SafeMediaPlayer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Manages all sound-related operations including:
 * - Start sound playback
 * - Local sound effect player references
 * - Volume slider initialization and binding
 * - Music fade-to-main transition scheduling
 *
 * This class extracts sound setup logic from GuiController without changing any behavior.
 */
public class SoundManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

    private SafeMediaPlayer bricksTouchPlayer;
    private SafeMediaPlayer hoverButtonPlayer;
    private SafeMediaPlayer buttonClickPlayer;
    private SafeMediaPlayer gameOverPlayer;

    private final Object resourceLoader;

    /**
     * Creates a SoundManager with access to resources.
     * @param resourceLoader The object to use for loading sound resources (typically 'this' from the controller)
     */
    public SoundManager(Object resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Plays the start sound. This is called once during initialization.
     * Original location: GuiController.initialize()
     */
    public SafeMediaPlayer playStartSound() {
        try {
            URL startUrl = resourceLoader.getClass().getResource("/sounds/start.MP3");
            if (startUrl != null) {
                SafeMediaPlayer startPlayer = new SafeMediaPlayer(startUrl);
                startPlayer.setVolume(1.0);
                startPlayer.play();
                return startPlayer;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to load start sound", e);
        }
        return null;
    }

    /**
     * Initializes music and SFX volume sliders with their bindings.
     * Original location: GuiController.initialize() Platform.runLater block
     *
     * @param musicVolumeSlider The slider controlling music volume (0-100)
     * @param sfxVolumeSlider The slider controlling SFX volume (0-100)
     * @param mainPlayerGetter Function to get the current main player (for volume updates)
     */
    public void initializeVolumeSliders(Slider musicVolumeSlider, Slider sfxVolumeSlider,
                                       java.util.function.Supplier<SafeMediaPlayer> mainPlayerGetter) {
        Platform.runLater(() -> {
            try {
                if (musicVolumeSlider != null) {
                    // default to 50%
                    musicVolumeSlider.setValue(50);
                    musicVolumeSlider.valueProperty().addListener((obs, oldV, newV) -> {
                        double uiVol = Math.max(0.0, Math.min(1.0, newV.doubleValue() / 100.0));
                        try {
                            // scale to existing target (0.5 max in codebase)
                            SafeMediaPlayer mainPlayer = mainPlayerGetter.get();
                            if (mainPlayer != null) mainPlayer.setVolume(uiVol * 0.5);
                        } catch (Throwable ignored) {}
                    });
                }
                if (sfxVolumeSlider != null) {
                    sfxVolumeSlider.setValue(100);
                    double initSfx = Math.max(0.0, Math.min(1.0, sfxVolumeSlider.getValue() / 100.0));
                    try { AudioManager.getInstance().setSfxVolume(initSfx); } catch (Throwable ignored) {}
                    sfxVolumeSlider.valueProperty().addListener((obs, oldV, newV) -> {
                        double v = Math.max(0.0, Math.min(1.0, newV.doubleValue() / 100.0));
                        try { AudioManager.getInstance().setSfxVolume(v); } catch (Throwable ignored) {}
                    });
                }
            } catch (Throwable ignored) {}
        });
    }

    /**
     * Loads all small SFX players. These are kept as local references.
     * Original location: GuiController.initialize() - "Load small SFX" section
     */
    public void loadSmallSFX() {
        // Load small SFX (controller keeps local references but playback is through AudioManager)
        try {
            URL bricksUrl = resourceLoader.getClass().getResource("/sounds/bricks_touch.mp3");
            if (bricksUrl != null) {
                bricksTouchPlayer = new SafeMediaPlayer(bricksUrl);
                bricksTouchPlayer.setVolume(1.0);
            }
        } catch (Exception ignored) {}

        try {
            URL hoverUrl = resourceLoader.getClass().getResource("/sounds/hover-button.mp3");
            if (hoverUrl != null) {
                hoverButtonPlayer = new SafeMediaPlayer(hoverUrl);
                hoverButtonPlayer.setVolume(1.0);
            }
        } catch (Exception ignored) {}

        try {
            URL clickUrl = resourceLoader.getClass().getResource("/sounds/Button1.mp3");
            if (clickUrl != null) {
                buttonClickPlayer = new SafeMediaPlayer(clickUrl);
                buttonClickPlayer.setVolume(1.0);
            }
        } catch (Exception ignored) {}

        try {
            URL gameOverUrl = resourceLoader.getClass().getResource("/sounds/Game Over sound effect.mp3");
            if (gameOverUrl != null) {
                gameOverPlayer = new SafeMediaPlayer(gameOverUrl);
                gameOverPlayer.setVolume(1.0);
                gameOverPlayer.setCycleCount(1);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Schedules the fade to main music after a delay.
     * Original location: GuiController.initialize() - "Delay the music fade" section
     *
     * @param fadeToMainMusicCallback The callback to invoke when fade should start
     */
    public void scheduleFadeToMainMusic(Runnable fadeToMainMusicCallback) {
        // Delay the music fade until after the static animation completes
        PauseTransition musicDelay = new PauseTransition(Duration.seconds(5.5));
        musicDelay.setOnFinished(e -> fadeToMainMusicCallback.run());
        musicDelay.play();
    }

    // Getters for the loaded SFX players (if needed elsewhere)
    public SafeMediaPlayer getBricksTouchPlayer() {
        return bricksTouchPlayer;
    }

    public SafeMediaPlayer getHoverButtonPlayer() {
        return hoverButtonPlayer;
    }

    public SafeMediaPlayer getButtonClickPlayer() {
        return buttonClickPlayer;
    }

    public SafeMediaPlayer getGameOverPlayer() {
        return gameOverPlayer;
    }
}

