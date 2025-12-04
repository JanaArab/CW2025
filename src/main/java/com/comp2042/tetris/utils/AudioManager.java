package com.comp2042.tetris.utils;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.application.Platform;

public final class AudioManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioManager.class);
    private static final AudioManager INSTANCE = new AudioManager();

    private SafeMediaPlayer rotationPlayer;
    private SafeMediaPlayer bricksTouchPlayer;
    private SafeMediaPlayer hoverPlayer;
    private SafeMediaPlayer clickPlayer;
    private SafeMediaPlayer gameOverPlayer;
    private SafeMediaPlayer confettiPlayer;
    private SafeMediaPlayer flickeringPlayer;

    // When true, other SFX should be suppressed (set by GUI on game over)
    private volatile boolean suppressSfx = false;

    // Global SFX volume (0.0 - 1.0). Default to full volume.
    private volatile double sfxVolume = 1.0;

    /**
     * Set global SFX volume (0.0-1.0). Applies to already-loaded SFX players.
     */
    public void setSfxVolume(double vol) {
        if (vol < 0.0) vol = 0.0; if (vol > 1.0) vol = 1.0;
        this.sfxVolume = vol;
        // Apply to loaded players
        try { if (rotationPlayer != null) rotationPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (bricksTouchPlayer != null) bricksTouchPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (hoverPlayer != null) hoverPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (clickPlayer != null) clickPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (gameOverPlayer != null) gameOverPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (confettiPlayer != null) confettiPlayer.setVolume(vol); } catch (Throwable ignored) {}
        try { if (flickeringPlayer != null) flickeringPlayer.setVolume(vol); } catch (Throwable ignored) {}
    }

    public double getSfxVolume() { return sfxVolume; }

    // Guard to ensure we only attempt to load media once
    private volatile boolean loaded = false;

    private AudioManager() {
        // Do not attempt to create JavaFX media objects here; load lazily in ensureLoaded()
    }

    private synchronized void ensureLoaded() {
        if (loaded) return;
        loaded = true;
        try {
            URL rot = getClass().getResource("/sounds/rotation.mp3");
            if (rot != null) rotationPlayer = new SafeMediaPlayer(rot);
            if (rotationPlayer != null && rotationPlayer.isAvailable()) rotationPlayer.setVolume(sfxVolume);
            LOGGER.info("rotationPlayer available={}", rotationPlayer != null && rotationPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("rotation player init failed", t);
            rotationPlayer = null;
        }
        try {
            URL bricks = getClass().getResource("/sounds/bricks_touch.mp3");
            if (bricks != null) bricksTouchPlayer = new SafeMediaPlayer(bricks);
            if (bricksTouchPlayer != null && bricksTouchPlayer.isAvailable()) bricksTouchPlayer.setVolume(sfxVolume);
            LOGGER.info("bricksTouchPlayer available={}", bricksTouchPlayer != null && bricksTouchPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("bricks player init failed", t);
            bricksTouchPlayer = null;
        }
        try {
            URL hover = getClass().getResource("/sounds/hover-button.mp3");
            if (hover != null) hoverPlayer = new SafeMediaPlayer(hover);
            if (hoverPlayer != null && hoverPlayer.isAvailable()) hoverPlayer.setVolume(sfxVolume);
            LOGGER.info("hoverPlayer available={}", hoverPlayer != null && hoverPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("hover player init failed", t);
            hoverPlayer = null;
        }
        try {
            URL click = getClass().getResource("/sounds/Button1.mp3");
            if (click != null) clickPlayer = new SafeMediaPlayer(click);
            if (clickPlayer != null && clickPlayer.isAvailable()) clickPlayer.setVolume(sfxVolume);
            LOGGER.info("clickPlayer available={}", clickPlayer != null && clickPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("click player init failed", t);
            clickPlayer = null;
        }
        try {
            URL over = getClass().getResource("/sounds/Game Over sound effect.mp3");
            if (over != null) {
                gameOverPlayer = new SafeMediaPlayer(over);
                if (gameOverPlayer != null && gameOverPlayer.isAvailable()) gameOverPlayer.setVolume(sfxVolume);
            }
            LOGGER.info("gameOverPlayer available={}", gameOverPlayer != null && gameOverPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("gameOver player init failed", t);
            gameOverPlayer = null;
        }
        try {
            URL conf = getClass().getResource("/sounds/Confetti.mp3");
            if (conf != null) {
                confettiPlayer = new SafeMediaPlayer(conf);
                if (confettiPlayer != null && confettiPlayer.isAvailable()) confettiPlayer.setVolume(sfxVolume);
            }
            LOGGER.info("confettiPlayer available={}", confettiPlayer != null && confettiPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("confetti player init failed", t);
            confettiPlayer = null;
        }
        try {
            URL flick = getClass().getResource("/sounds/Flickering.mp3");
            if (flick != null) {
                flickeringPlayer = new SafeMediaPlayer(flick);
                if (flickeringPlayer != null && flickeringPlayer.isAvailable()) flickeringPlayer.setVolume(sfxVolume);
            }
            LOGGER.info("flickeringPlayer available={}", flickeringPlayer != null && flickeringPlayer.isAvailable());
        } catch (Throwable t) {
            LOGGER.debug("flickering player init failed", t);
            flickeringPlayer = null;
        }
    }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public void setSuppressSfx(boolean suppress) {
        this.suppressSfx = suppress;
        if (suppress) {
            stopAll();
        }
    }

    public void playRotation() {
        if (suppressSfx) {
            LOGGER.debug("playRotation suppressed");
            return;
        }
        try {
            ensureLoaded();
            boolean available = rotationPlayer != null && rotationPlayer.isAvailable();
            LOGGER.debug("playRotation called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        rotationPlayer.stop();
                        rotationPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("rotation playback failed on FX thread", t);
                    }
                });
            } else {
                LOGGER.debug("rotationPlayer not available or null");
            }
        } catch (Throwable t) {
            LOGGER.debug("playRotation failed", t);
        }
    }

    public void playBricksTouch() {
        if (suppressSfx) return;
        try {
            ensureLoaded();
            boolean available = bricksTouchPlayer != null && bricksTouchPlayer.isAvailable();
            LOGGER.debug("playBricksTouch called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        // Directly use SafeMediaPlayer.playFromStart() to restart reliably
                        try {
                            LOGGER.debug("invoking playFromStart for bricksTouchPlayer");
                            bricksTouchPlayer.playFromStart();
                        } catch (Throwable t) {
                            // fallback to stop/play if something goes wrong
                            try { bricksTouchPlayer.stop(); } catch (Throwable ignored) {}
                            try { bricksTouchPlayer.play(); } catch (Throwable ignored) {}
                        }
                    } catch (Throwable t) {
                        LOGGER.debug("bricks touch playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playBricksTouch failed", t);
        }
    }

    public void playHover() {
        if (suppressSfx) return;
        try {
            ensureLoaded();
            boolean available = hoverPlayer != null && hoverPlayer.isAvailable();
            LOGGER.debug("playHover called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        hoverPlayer.stop();
                        hoverPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("hover playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playHover failed", t);
        }
    }

    public void playClick() {
        if (suppressSfx) return;
        try {
            ensureLoaded();
            boolean available = clickPlayer != null && clickPlayer.isAvailable();
            LOGGER.debug("playClick called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        clickPlayer.stop();
                        clickPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("click playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playClick failed", t);
        }
    }

    public void playGameOver() {
        try {
            ensureLoaded();
            boolean available = gameOverPlayer != null && gameOverPlayer.isAvailable();
            LOGGER.debug("playGameOver called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        gameOverPlayer.stop();
                        gameOverPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("gameOver playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playGameOver failed", t);
        }
    }

    public void playConfetti() {
        if (suppressSfx) return;
        try {
            ensureLoaded();
            boolean available = confettiPlayer != null && confettiPlayer.isAvailable();
            LOGGER.debug("playConfetti called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        // stop then play to restart effect
                        confettiPlayer.stop();
                        confettiPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("confetti playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playConfetti failed", t);
        }
    }

    public void playFlickering() {
        if (suppressSfx) return;
        try {
            ensureLoaded();
            boolean available = flickeringPlayer != null && flickeringPlayer.isAvailable();
            LOGGER.debug("playFlickering called, available={}", available);
            if (available) {
                Platform.runLater(() -> {
                    try {
                        // restart so multiple flickers retrigger sound
                        flickeringPlayer.stop();
                        flickeringPlayer.play();
                    } catch (Throwable t) {
                        LOGGER.debug("flickering playback failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("playFlickering failed", t);
        }
    }

    /**
     * Returns the total duration in seconds of the flickering audio, or 0 if unavailable.
     */
    public double getFlickeringDurationSeconds() {
        try {
            ensureLoaded();
            if (flickeringPlayer != null && flickeringPlayer.isAvailable()) {
                return flickeringPlayer.getTotalDurationSeconds();
            }
        } catch (Throwable ignored) {}
        return 0.0;
    }

    /**
     * Stop the flickering SFX if it is playing.
     */
    public void stopFlickering() {
        try {
            ensureLoaded();
            if (flickeringPlayer != null && flickeringPlayer.isAvailable()) {
                Platform.runLater(() -> {
                    try {
                        flickeringPlayer.stop();
                    } catch (Throwable t) {
                        LOGGER.debug("stopFlickering failed on FX thread", t);
                    }
                });
            }
        } catch (Throwable t) {
            LOGGER.debug("stopFlickering failed", t);
        }
    }

    public void stopAll() {
        try { if (rotationPlayer != null) rotationPlayer.stop(); } catch (Throwable ignored) {}
        try { if (bricksTouchPlayer != null) bricksTouchPlayer.stop(); } catch (Throwable ignored) {}
        try { if (hoverPlayer != null) hoverPlayer.stop(); } catch (Throwable ignored) {}
        try { if (clickPlayer != null) clickPlayer.stop(); } catch (Throwable ignored) {}
        try { if (gameOverPlayer != null) gameOverPlayer.stop(); } catch (Throwable ignored) {}
        try { if (confettiPlayer != null) confettiPlayer.stop(); } catch (Throwable ignored) {}
        try { if (flickeringPlayer != null) flickeringPlayer.stop(); } catch (Throwable ignored) {}
    }
}
