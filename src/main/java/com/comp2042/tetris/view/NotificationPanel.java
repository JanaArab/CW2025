/**
 * used to display score labels floating on the screen
 */

package com.comp2042.tetris.view;

import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class NotificationPanel extends BorderPane {

    public NotificationPanel(String text) {
        // Compact defaults to match rotation notification design
        setMinHeight(60);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        // Align label to left-center inside the panel
        BorderPane.setAlignment(score, javafx.geometry.Pos.CENTER_LEFT);
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }
}
