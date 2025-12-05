package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.ui.handlers.MenuEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuController.class);

    @FXML
    private StackPane mainMenuOverlay;

    @FXML
    private VBox mainMenuCard;

    private MenuEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("MainMenuController initialized");
    }

    public void setEventHandler(MenuEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void handleStartGame(ActionEvent actionEvent) {
        LOGGER.info("Start game button clicked");
        if (eventHandler != null) {
            eventHandler.onStartGame();
        }
    }

    @FXML
    protected void handleExitGame(ActionEvent actionEvent) {
        LOGGER.info("Exit game button clicked");
        if (eventHandler != null) {
            eventHandler.onExitGame();
        }
    }

    @FXML
    protected void openMusicControl(ActionEvent actionEvent) {
        LOGGER.info("Music control button clicked");
        if (eventHandler != null) {
            eventHandler.onOpenMusicControl();
        }
    }

    @FXML
    protected void openTutorial(ActionEvent actionEvent) {
        LOGGER.info("Tutorial button clicked");
        if (eventHandler != null) {
            eventHandler.onOpenTutorial();
        }
    }

    public StackPane getMainMenuOverlay() {
        return mainMenuOverlay;
    }

    public void show() {
        if (mainMenuOverlay != null) {
            mainMenuOverlay.setVisible(true);
            mainMenuOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (mainMenuOverlay != null) {
            mainMenuOverlay.setVisible(false);
            mainMenuOverlay.setManaged(false);
        }
    }
}

