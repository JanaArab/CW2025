package com.comp2042.tetris.view;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StaticScreenAnimator {

    private static final int FRAME_COUNT = 5;
    private static final int NOISE_WIDTH = 225;
    private static final int NOISE_HEIGHT = 180;

    // Transition Timings
    private static final double STATIC_VISIBLE_DURATION = 2.0;
    private static final double FADE_DURATION = 3.5; // Slow fade to reveal distortion

    private final List<WritableImage> noiseFrames;
    private final Random random;
    private AnimationTimer timer;

    public StaticScreenAnimator() {
        this.noiseFrames = new ArrayList<>();
        this.random = new Random();
        generateNoiseFrames();
    }

    private void generateNoiseFrames() {
        for (int i = 0; i < FRAME_COUNT; i++) {
            WritableImage image = new WritableImage(NOISE_WIDTH, NOISE_HEIGHT);
            PixelWriter pw = image.getPixelWriter();
            for (int y = 0; y < NOISE_HEIGHT; y++) {
                for (int x = 0; x < NOISE_WIDTH; x++) {
                    double gray = random.nextDouble();
                    Color color = gray > 0.5 ? Color.WHITE : Color.BLACK;
                    pw.setColor(x, y, color);
                }
            }
            noiseFrames.add(image);
        }
    }

    /**
     * Plays the static animation sequence.
     * @param staticView The view to display static on.
     * @param onFadeStart Runnable to execute when the static begins to fade out (triggering distortion).
     */
    public void play(ImageView staticView, Runnable onFadeStart) {
        if (staticView == null) return;

        staticView.setVisible(true);
        staticView.setOpacity(1.0);

        // 1. Start Noise Loop
        timer = new AnimationTimer() {
            private int frameIndex = 0;
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 80_000_000) {
                    staticView.setImage(noiseFrames.get(frameIndex));
                    frameIndex = (frameIndex + 1) % FRAME_COUNT;
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        // 2. Wait, then Fade Out
        PauseTransition staticHold = new PauseTransition(Duration.seconds(STATIC_VISIBLE_DURATION));
        staticHold.setOnFinished(e -> {
            // Trigger the distortion effect on the layer BELOW the static
            if (onFadeStart != null) onFadeStart.run();
        });

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_DURATION), staticView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            stop();
            staticView.setVisible(false);
            staticView.setImage(null);
        });

        SequentialTransition sequence = new SequentialTransition(staticHold, fadeOut);
        sequence.play();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }
}