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

public class PauseMenuController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PauseMenuController.class);

    @FXML
    private StackPane pauseMenuOverlay;

    @FXML
    private VBox pauseMenuCard;

    private MenuEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("PauseMenuController initialized");
    }

    public void setEventHandler(MenuEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void resumeGame(ActionEvent actionEvent) {
        LOGGER.info("Resume game button clicked");
        if (eventHandler != null) {
            eventHandler.onResumeGame();
        }
    }

    @FXML
    protected void openMusicControl(ActionEvent actionEvent) {
        LOGGER.info("Music control button clicked from pause menu");
        if (eventHandler != null) {
            eventHandler.onOpenMusicControl();
        }
    }

    @FXML
    protected void startNewGameFromPause(ActionEvent actionEvent) {
        LOGGER.info("New game button clicked from pause menu");
        if (eventHandler != null) {
            eventHandler.onStartNewGameFromPause();
        }
    }

    @FXML
    protected void goToMainMenuFromPause(ActionEvent actionEvent) {
        LOGGER.info("Main menu button clicked from pause menu");
        if (eventHandler != null) {
            eventHandler.onGoToMainMenuFromPause();
        }
    }

    @FXML
    protected void endGameFromPause(ActionEvent actionEvent) {
        LOGGER.info("Exit button clicked from pause menu");
        if (eventHandler != null) {
            eventHandler.onEndGameFromPause();
        }
    }

    public StackPane getPauseMenuOverlay() {
        return pauseMenuOverlay;
    }

    public void show() {
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(true);
            pauseMenuOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(false);
            pauseMenuOverlay.setManaged(false);
        }
    }
}

