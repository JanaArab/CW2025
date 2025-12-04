package com.comp2042.tetris.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

public class NotificationAnimator {
    public void playShowScore(Node panel, ObservableList<Node> list) {
        if (panel == null || list == null) {
            return;
        }

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), panel);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2500), panel);
        translateTransition.setToY(panel.getLayoutY() - 40);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);

        ParallelTransition transition = new ParallelTransition(translateTransition, fadeTransition);
        transition.setOnFinished(event -> list.remove(panel));
        transition.play();
    }

    /**
     * Shorter, snappier notification used for rotation messages so they don't linger.
     */
    public void playShowRotation(Node panel, ObservableList<Node> list) {
        if (panel == null || list == null) return;

        // Faster fade + smaller translate to keep the message brief and unobtrusive
        FadeTransition fade = new FadeTransition(Duration.millis(800), panel);
        TranslateTransition translate = new TranslateTransition(Duration.millis(1000), panel);
        translate.setToY(panel.getLayoutY() - 20);
        fade.setFromValue(1);
        fade.setToValue(0);

        ParallelTransition p = new ParallelTransition(translate, fade);
        p.setOnFinished(e -> list.remove(panel));
        p.play();
    }
}
