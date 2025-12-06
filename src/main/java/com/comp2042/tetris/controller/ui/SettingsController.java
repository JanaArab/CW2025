package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.ui.handlers.SettingsEventHandler;
import com.comp2042.tetris.utils.AudioManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the settings/music control screen.
 * Manages volume sliders for music and sound effects.
 *
 * @see SettingsEventHandler
 */
public class SettingsController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

    @FXML
    private StackPane musicControlOverlay;

    @FXML
    private VBox musicCard;

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Slider sfxVolumeSlider;

    private SettingsEventHandler eventHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("SettingsController initialized");

        if (musicVolumeSlider != null) {
            musicVolumeSlider.setValue(50.0);
            musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                LOGGER.debug("Music volume changed to: {}", newVal.doubleValue());
            });
        }

        if (sfxVolumeSlider != null) {
            sfxVolumeSlider.setValue(AudioManager.getInstance().getSfxVolume() * 100);
            sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                AudioManager.getInstance().setSfxVolume(newVal.doubleValue() / 100);
            });
        }
    }

    public void setEventHandler(SettingsEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @FXML
    protected void closeMusicControl(ActionEvent actionEvent) {
        LOGGER.info("Close music control button clicked");
        if (eventHandler != null) {
            eventHandler.onCloseMusicControl();
        }
    }

    public StackPane getMusicControlOverlay() {
        return musicControlOverlay;
    }

    public void show() {
        if (musicControlOverlay != null) {
            musicControlOverlay.setVisible(true);
            musicControlOverlay.setManaged(true);
        }
    }

    public void hide() {
        if (musicControlOverlay != null) {
            musicControlOverlay.setVisible(false);
            musicControlOverlay.setManaged(false);
        }
    }
}
