package com.comp2042.tetris.controller.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Random;

public class GameOverController {

    @FXML
    private Label gameLabel;

    @FXML
    private Label overLabel;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            double opacity = 0.5 + random.nextDouble() * 0.5; // 0.5 to 1.0
            double shakeX = (random.nextDouble() - 0.5) * 4; // -2 to 2
            double shakeY = (random.nextDouble() - 0.5) * 4;

            gameLabel.setOpacity(opacity);
            gameLabel.setTranslateX(shakeX);
            gameLabel.setTranslateY(shakeY);

            overLabel.setOpacity(opacity);
            overLabel.setTranslateX(shakeX);
            overLabel.setTranslateY(shakeY);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
