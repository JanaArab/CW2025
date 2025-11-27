package com.comp2042.tetris.view;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;

import java.util.Random;

/**
 * Creates a "bad signal" distortion effect using a DisplacementMap.
 * Simulates horizontal tearing/glitching that stabilizes over time.
 */
public class DistortionAnimator {

    private final Random random;
    private AnimationTimer timer;
    private Node targetNode;

    // Map resolution: Width 1 stretches noise across the screen (horizontal lines)
    // Height 64 gives us "chunky" retro scanline blocks
    private static final int MAP_WIDTH = 1;
    private static final int MAP_HEIGHT = 64;

    public DistortionAnimator() {
        this.random = new Random();
    }

    /**
     * Applies a distortion effect that fades out over the specified duration.
     * @param target The node to distort (e.g., the Main Menu pane)
     * @param durationSeconds How long the effect lasts
     */
    public void applyGlitchTransition(Node target, double durationSeconds) {
        this.targetNode = target;

        // Create the map data
        FloatMap floatMap = new FloatMap(MAP_WIDTH, MAP_HEIGHT);
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);
        displacementMap.setWrap(false); // Don't wrap pixels, just smear edges

        // Apply effect to target
        target.setEffect(displacementMap);

        // Max horizontal shift amount (0.1 means 10% of node width)
        double maxIntensity = 0.15;

        timer = new AnimationTimer() {
            private long startTime = -1;

            @Override
            public void handle(long now) {
                if (startTime == -1) startTime = now;
                double elapsed = (now - startTime) / 1_000_000_000.0;

                if (elapsed >= durationSeconds) {
                    stop();
                    return;
                }

                // Calculate intensity: starts high, linearly reduces to 0
                double progress = elapsed / durationSeconds;
                double currentIntensity = maxIntensity * (1.0 - progress);

                updateNoise(floatMap, currentIntensity);
            }
        };
        timer.start();
    }

    private void updateNoise(FloatMap map, double intensity) {
        // We only update the X channel (0) to create horizontal tearing
        for (int y = 0; y < MAP_HEIGHT; y++) {
            // Random shift between -intensity and +intensity
            float shift = (random.nextFloat() - 0.5f) * 2 * (float) intensity;

            // Apply to the single column (which stretches across width)
            map.setSamples(0, y, shift, 0.0f);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        if (targetNode != null) {
            targetNode.setEffect(null); // Clean up effect
            targetNode = null;
        }
    }
}