package com.comp2042.tetris.view;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BackgroundAnimator {

    private static final double SCREEN_HEIGHT = 720;
    private static final double ANIMATION_DURATION = 20.0;

    public void animate(Pane background1, Pane background2) {

        TranslateTransition tt1 = new TranslateTransition(Duration.seconds(ANIMATION_DURATION), background1);
        tt1.setFromY(0);
        tt1.setToY(SCREEN_HEIGHT);
        tt1.setInterpolator(Interpolator.LINEAR);
        tt1.setCycleCount(TranslateTransition.INDEFINITE);
        tt1.play();

        TranslateTransition tt2 = new TranslateTransition(Duration.seconds(ANIMATION_DURATION), background2);
        tt2.setFromY(0);
        tt2.setToY(SCREEN_HEIGHT);
        tt2.setInterpolator(Interpolator.LINEAR);
        tt2.setCycleCount(TranslateTransition.INDEFINITE);
        tt2.play();
    }
}