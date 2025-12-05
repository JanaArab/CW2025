package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.ui.handlers.LevelSelectionEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelSelectionController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelSelectionController.class);

    @FXML
    private StackPane levelSelectOverlay;

    @FXML
    private Button levelClassicButton;

    @FXML
    private Button levelL1Button;

    @FXML
    private Button levelL2Button;

    @FXML
    private Button levelL3Button;

    private LevelSelectionEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("LevelSelectionController initialized");
    }

    public void setEventHandler(LevelSelectionEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void handleLevelClassic(ActionEvent actionEvent) {
        LOGGER.info("Classic level selected");
        if (eventHandler != null) {
            eventHandler.onLevelSelected("CLASSIC");
        }
    }

    @FXML
    protected void handleLevelL1(ActionEvent actionEvent) {
        LOGGER.info("Level 1 selected");
        if (eventHandler != null) {
            eventHandler.onLevelSelected("LEVEL1");
        }
    }

    @FXML
    protected void handleLevelL2(ActionEvent actionEvent) {
        LOGGER.info("Level 2 selected");
        if (eventHandler != null) {
            eventHandler.onLevelSelected("LEVEL2");
        }
    }

    @FXML
    protected void handleLevelL3(ActionEvent actionEvent) {
        LOGGER.info("Level 3 selected");
        if (eventHandler != null) {
            eventHandler.onLevelSelected("LEVEL3");
        }
    }

    @FXML
    protected void cancelLevelSelection(ActionEvent actionEvent) {
        LOGGER.info("Level selection cancelled");
        if (eventHandler != null) {
            eventHandler.onLevelSelectionCancelled();
        }
    }

    public StackPane getLevelSelectOverlay() {
        return levelSelectOverlay;
    }

    public void show() {
        if (levelSelectOverlay != null) {
            levelSelectOverlay.setVisible(true);
            levelSelectOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (levelSelectOverlay != null) {
            levelSelectOverlay.setVisible(false);
            levelSelectOverlay.setManaged(false);
        }
    }
}

