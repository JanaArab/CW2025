package com.comp2042.tetris.view;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * More dramatic firework animator: larger particle counts, further spread, longer durations,
 * expanding ring, and a small secondary sparkle burst.
 */
public class FireworkAnimator {
    private final Random random = new Random();

    public void playFirework(Group container) {
        if (container == null) return;

        Platform.runLater(() -> {
            Group particles = new Group();

            double centerX = 0;
            double centerY = 0;

            // Dramatic settings
            int count = 28 + random.nextInt(8); // ~28-35 particles
            double minRadius = 3.0;
            double maxRadius = 9.0;
            double minDistance = 100.0;
            double maxDistance = 220.0;

            // Create an expanding ring to emphasize the blast
            Circle ring = new Circle(centerX, centerY, 6);
            ring.setFill(null);
            ring.setStroke(randomColor());
            ring.setStrokeWidth(3);
            ring.setOpacity(0.9);
            particles.getChildren().add(ring);

            // animate the ring expanding and fading
            double ringFinalRadius = 140 + random.nextDouble() * 60;
            Timeline ringTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(ring.radiusProperty(), 6),
                    new KeyValue(ring.opacityProperty(), 0.95)
                ),
                new KeyFrame(Duration.millis(900d + random.nextInt(500)),
                    new KeyValue(ring.radiusProperty(), ringFinalRadius),
                    new KeyValue(ring.opacityProperty(), 0.0)
                )
            );
            // Timeline doesn't support a global interpolator; key values/transitions use their own interpolators.
             ringTimeline.play();

            for (int i = 0; i < count; i++) {
                // Primary particle
                double radius = minRadius + random.nextDouble() * (maxRadius - minRadius);
                Circle c = new Circle(radius);
                Paint fill = randomColor();
                c.setFill(fill);
                c.setEffect(new DropShadow(12, Color.color(0,0,0,0.6)));
                c.setTranslateX(centerX);
                c.setTranslateY(centerY);
                particles.getChildren().add(c);

                // Direction and travel
                double angle = 2 * Math.PI * i / count + (random.nextDouble() - 0.5) * 0.5;
                double distance = minDistance + random.nextDouble() * (maxDistance - minDistance);
                double toX = Math.cos(angle) * distance;
                double toY = Math.sin(angle) * distance;

                Duration translateDur = Duration.millis(900d + random.nextInt(700));
                TranslateTransition tt = new TranslateTransition(translateDur, c);
                tt.setByX(toX);
                tt.setByY(toY);
                tt.setInterpolator(Interpolator.SPLINE(0.2, 0.8, 0.2, 1.0));

                Duration fadeDur = Duration.millis(1200d + random.nextInt(800));
                FadeTransition ft = new FadeTransition(fadeDur, c);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);

                ScaleTransition st = new ScaleTransition(translateDur, c);
                st.setToX(0.4);
                st.setToY(0.4);

                ParallelTransition pt = new ParallelTransition(tt, ft, st);
                pt.setDelay(Duration.millis(random.nextInt(120)));
                pt.play();

                // when primary finishes, spawn a few tiny sparkles at its end location
                pt.setOnFinished(e -> {
                    // determine end position
                    double endX = c.getTranslateX();
                    double endY = c.getTranslateY();

                    int sparks = 3 + random.nextInt(3);
                    for (int s = 0; s < sparks; s++) {
                        Circle sp = new Circle(1 + random.nextDouble() * 2);
                        sp.setFill(randomColor());
                        sp.setTranslateX(endX);
                        sp.setTranslateY(endY);
                        particles.getChildren().add(sp);

                        double sparAngle = random.nextDouble() * 2 * Math.PI;
                        double sparDist = 10 + random.nextDouble() * 30;
                        TranslateTransition stt = new TranslateTransition(Duration.millis(300d + random.nextInt(300)), sp);
                        stt.setByX(Math.cos(sparAngle) * sparDist);
                        stt.setByY(Math.sin(sparAngle) * sparDist);
                        stt.setInterpolator(Interpolator.EASE_OUT);

                        FadeTransition sft = new FadeTransition(Duration.millis(300d + random.nextInt(300)), sp);
                        sft.setFromValue(1.0);
                        sft.setToValue(0.0);

                        ParallelTransition spt = new ParallelTransition(stt, sft);
                        spt.setOnFinished(ev -> particles.getChildren().remove(sp));
                        spt.play();
                    }

                    particles.getChildren().remove(c);
                });
            }

            container.getChildren().add(particles);

            // cleanup after a bit longer to allow all animations to complete
            PauseTransition cleanup = new PauseTransition(Duration.millis(2200d + random.nextInt(800)));
            cleanup.setOnFinished(e -> container.getChildren().remove(particles));
            cleanup.play();
        });
    }

    private Color randomColor() {
        Color[] palette = new Color[]{Color.ORANGE, Color.YELLOW, Color.RED, Color.CORNFLOWERBLUE, Color.MAGENTA, Color.LIME, Color.GOLD, Color.DEEPPINK};
        return palette[random.nextInt(palette.length)];
    }
}
