package com.comp2042.tetris.view;

import javafx.beans.NamedArg;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class OverlayPanel extends BorderPane{
    public OverlayPanel(@NamedArg("message") String message) {
        final Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("gameOverStyle");
        setCenter(messageLabel);
    }
}
