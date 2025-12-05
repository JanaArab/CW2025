package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.ui.handlers.TutorialEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TutorialController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialController.class);

    @FXML
    private StackPane tutorialOverlay;

    private TutorialEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("TutorialController initialized");
    }

    public void setEventHandler(TutorialEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void closeTutorial(ActionEvent actionEvent) {
        LOGGER.info("Close tutorial button clicked");
        if (eventHandler != null) {
            eventHandler.onCloseTutorial();
        }
    }

    public StackPane getTutorialOverlay() {
        return tutorialOverlay;
    }

    public void show() {
        if (tutorialOverlay != null) {
            tutorialOverlay.setVisible(true);
            tutorialOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (tutorialOverlay != null) {
            tutorialOverlay.setVisible(false);
            tutorialOverlay.setManaged(false);
        }
    }
}

