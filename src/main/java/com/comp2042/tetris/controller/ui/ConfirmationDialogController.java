package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.ui.handlers.ConfirmationEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationDialogController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationDialogController.class);

    @FXML
    private StackPane confirmationOverlay;

    @FXML
    private Label confirmationMessage;

    private ConfirmationEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("ConfirmationDialogController initialized");
    }

    public void setEventHandler(ConfirmationEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void confirmAction(ActionEvent actionEvent) {
        LOGGER.info("Confirm button clicked");
        if (eventHandler != null) {
            eventHandler.onConfirm();
        }
    }

    @FXML
    protected void cancelAction(ActionEvent actionEvent) {
        LOGGER.info("Cancel button clicked");
        if (eventHandler != null) {
            eventHandler.onCancel();
        }
    }

    public StackPane getConfirmationOverlay() {
        return confirmationOverlay;
    }

    public Label getConfirmationMessage() {
        return confirmationMessage;
    }

    public void show(String message) {
        if (confirmationMessage != null) {
            confirmationMessage.setText(message);
        }
        if (confirmationOverlay != null) {
            confirmationOverlay.setVisible(true);
            confirmationOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (confirmationOverlay != null) {
            confirmationOverlay.setVisible(false);
            confirmationOverlay.setManaged(false);
        }
    }
}

