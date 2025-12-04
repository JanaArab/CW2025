package com.comp2042.tetris.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameViewController.class);

    @FXML
    private HBox mainContent;

    @FXML
    private GridPane gamePanel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private GridPane ghostBrickLayer;

    @FXML
    private GridPane activeBrickLayer;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timerLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("GameViewController initialized");
    }

    public HBox getMainContent() {
        return mainContent;
    }

    public GridPane getGamePanel() {
        return gamePanel;
    }

    public GridPane getNextBrickPanel() {
        return nextBrickPanel;
    }

    public GridPane getGhostBrickLayer() {
        return ghostBrickLayer;
    }

    public GridPane getActiveBrickLayer() {
        return activeBrickLayer;
    }

    public void updateScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText(String.valueOf(score));
        }
    }

    public void updateTimer(String time) {
        if (timerLabel != null) {
            timerLabel.setText(time);
        }
    }

    public void show() {
        if (mainContent != null) {
            mainContent.setVisible(true);
            mainContent.setManaged(true);
        }
    }

    public void hide() {
        if (mainContent != null) {
            mainContent.setVisible(false);
            mainContent.setManaged(false);
        }
    }
}

