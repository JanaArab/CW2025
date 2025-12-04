package com.comp2042.tetris.controller;

import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.game.GameTimer;
import com.comp2042.tetris.model.level.ClassicLevel;
import com.comp2042.tetris.model.level.GameLevel;
import com.comp2042.tetris.model.level.Level1;
import com.comp2042.tetris.model.level.Level2;
import com.comp2042.tetris.utils.AudioManager;
import com.comp2042.tetris.utils.SafeMediaPlayer;
import com.comp2042.tetris.view.GameViewPresenter;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Handles all menu, pause, and tutorial UI interactions separate from the in-game rendering logic.
 */
public abstract class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @FXML
    protected ImageView staticScreen;

    @FXML
    protected Pane pixelStarLayer;

    @FXML
    protected HBox mainContent;

    @FXML
    protected Button pauseButton;

    @FXML
    protected StackPane pauseMenuOverlay;

    @FXML
    protected VBox pauseMenuCard;

    @FXML
    protected StackPane confirmationOverlay;

    @FXML
    protected Label confirmationMessage;

    @FXML
    protected StackPane musicControlOverlay;

    @FXML
    protected StackPane tutorialOverlay;

    @FXML
    protected Slider musicVolumeSlider;

    @FXML
    protected Slider sfxVolumeSlider;

    @FXML
    protected StackPane gameOverOverlay;

    @FXML
    protected StackPane mainMenuOverlay;

    protected Node previousOverlay;

    @FXML
    protected VBox mainMenuCard;

    @FXML
    protected StackPane levelSelectOverlay;

    @FXML
    protected Pane curtainLeft;

    @FXML
    protected Pane curtainRight;

    @FXML
    protected Button levelClassicButton;

    @FXML
    protected Button levelL1Button;

    @FXML
    protected Button levelL2Button;

    @FXML
    protected Button levelL3Button;

    @FXML
    protected GridPane nextBrickPanel;

    @FXML
    protected Label nextTitleLabel;

    @FXML
    protected BorderPane gameBoard;

    protected GameLevel currentLevel = new ClassicLevel();

    protected IGameController gameController;
    protected AnimationHandler animationHandler;
    protected InputHandler inputHandler;
    protected GameViewPresenter gameViewPresenter;
    protected GameTimer gameTimer;

    protected boolean gameActive = false;
    protected Runnable pendingConfirmationAction;
    protected Runnable pendingCancelAction;
    protected SafeMediaPlayer startPlayer;
    protected SafeMediaPlayer mainPlayer;
    protected String activeBackgroundResourcePath = null;
    protected volatile long backgroundChangeId = 0L;

    @FXML
    protected void handleStartGame(ActionEvent actionEvent) {
        if (levelSelectOverlay != null) {
            setNodeVisibility(levelSelectOverlay, true);
            setNodeVisibility(mainMenuOverlay, false);
        } else {
            if (mainContent != null) {
                mainContent.setOpacity(0.0);
                mainContent.setVisible(true);
            }
            if (curtainLeft != null && curtainRight != null) {
                curtainLeft.setTranslateX(0);
                curtainRight.setTranslateX(0);
                curtainLeft.setVisible(true);
                curtainLeft.setManaged(true);
                curtainRight.setVisible(true);
                curtainRight.setManaged(true);
            }
            Platform.runLater(() -> {
                setNodeVisibility(mainMenuOverlay, false);
                setNodeVisibility(mainMenuCard, true);
                playCurtainAnimation();
            });
        }
    }

    @FXML
    protected void handleLevelClassic(ActionEvent actionEvent) {
        currentLevel = new ClassicLevel();
        switchBackgroundTo("/sounds/classic.mp3");
        initiateGameStart();
    }

    @FXML
    protected void handleLevelL1(ActionEvent actionEvent) {
        currentLevel = new Level1();
        switchBackgroundTo("/sounds/level1.mp3");
        initiateGameStart();
    }

    @FXML
    protected void handleLevelL2(ActionEvent actionEvent) {
        currentLevel = new Level2();
        switchBackgroundTo("/sounds/level2.mp3");
        initiateGameStart();
    }

    @FXML
    protected void handleLevelL3(ActionEvent actionEvent) {
        currentLevel = new com.comp2042.tetris.model.level.Level3();
        switchBackgroundTo("/sounds/level3.mp3");
        try {
            if (inputHandler != null) inputHandler.setInvertHorizontal(true);
        } catch (Throwable ignored) {
        }
        initiateGameStart();
    }

    protected void initiateGameStart() {
        setNodeVisibility(levelSelectOverlay, false);
        try {
            if (inputHandler != null)
                inputHandler.setInvertHorizontal(currentLevel instanceof com.comp2042.tetris.model.level.Level3);
        } catch (Throwable ignored) {
        }
        if (gameController != null) gameController.setLevel(currentLevel);
        if (nextBrickPanel != null) {
            boolean hideNext = currentLevel.isNextBrickHidden();
            Node parent = nextBrickPanel.getParent();
            if (parent != null) parent.setVisible(!hideNext);
            else nextBrickPanel.setVisible(!hideNext);
            if (nextTitleLabel != null) {
                nextTitleLabel.setVisible(!hideNext);
                nextTitleLabel.setManaged(!hideNext);
            }
        }
        stopFlickerEffect();
        if (currentLevel.isFlickerEnabled()) startFlickerEffect();
        if (curtainLeft != null && curtainRight != null) {
            curtainLeft.setTranslateX(0);
            curtainRight.setTranslateX(0);
            curtainLeft.setVisible(true);
            curtainLeft.setManaged(true);
            curtainRight.setVisible(true);
            curtainRight.setManaged(true);
        }
        Platform.runLater(this::playCurtainAnimation);
    }

    @FXML
    protected void cancelLevelSelect(ActionEvent actionEvent) {
        setNodeVisibility(levelSelectOverlay, false);
        setNodeVisibility(mainMenuOverlay, true);
    }

    @FXML
    protected void exitGame(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    protected void resumeGame(ActionEvent actionEvent) {
        togglePauseState();
    }

    @FXML
    protected void pauseGame(ActionEvent actionEvent) {
        togglePauseState();
    }

    @FXML
    protected void openMusicControl(ActionEvent actionEvent) {
        previousOverlay = pauseMenuOverlay.isVisible() ? pauseMenuOverlay : mainMenuOverlay;
        setNodeVisibility(previousOverlay, false);
        setNodeVisibility(musicControlOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(true);
    }

    @FXML
    protected void closeMusicControl(ActionEvent actionEvent) {
        setNodeVisibility(musicControlOverlay, false);
        setNodeVisibility(previousOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(false);
    }

    @FXML
    protected void openTutorial(ActionEvent actionEvent) {
        previousOverlay = pauseMenuOverlay.isVisible() ? pauseMenuOverlay : mainMenuOverlay;
        setNodeVisibility(previousOverlay, false);
        setNodeVisibility(tutorialOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(true);
    }

    @FXML
    protected void closeTutorial(ActionEvent actionEvent) {
        setNodeVisibility(tutorialOverlay, false);
        setNodeVisibility(previousOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(false);
    }

    @FXML
    protected void startNewGameFromPause(ActionEvent actionEvent) {
        if (confirmationMessage != null) confirmationMessage.setText("Start a new game?");
        pendingConfirmationAction = () -> {
            setNodeVisibility(pauseMenuOverlay, false);
            if (animationHandler != null && animationHandler.isPaused()) togglePauseState();
            startNewGame();
        };
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(true);
    }

    @FXML
    protected void goToMainMenuFromPause(ActionEvent actionEvent) {
        if (confirmationMessage != null) confirmationMessage.setText("Return to main menu?");
        pendingConfirmationAction = () -> {
            gameActive = false;
            if (gameTimer != null) gameTimer.stop();
            if (animationHandler != null && animationHandler.isPaused()) togglePauseState();
            if (animationHandler != null) animationHandler.onGameOver();
            setNodeVisibility(pauseMenuOverlay, false);
            showMainMenu();
            Platform.runLater(() -> {
                try {
                    AudioManager.getInstance().setSuppressSfx(false);
                    AudioManager.getInstance().stopAll();
                } catch (Throwable ignored) {
                }
                try {
                    restartMainMenuMusicImmediate();
                } catch (Throwable ignored) {
                }
                try {
                    setupHoverSounds();
                    setupClickSounds();
                } catch (Throwable ignored) {
                }
            });
        };
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(true);
    }

    @FXML
    protected void endGameFromPause(ActionEvent actionEvent) {
        if (confirmationMessage != null) confirmationMessage.setText("End game and exit?");
        pendingConfirmationAction = () -> Platform.exit();
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) pauseButton.setDisable(true);
    }

    @FXML
    protected void confirmAction(ActionEvent actionEvent) {
        setNodeVisibility(confirmationOverlay, false);
        if (pendingConfirmationAction != null) {
            pendingConfirmationAction.run();
            pendingConfirmationAction = null;
        }
        pendingCancelAction = null;
        if (pauseButton != null) pauseButton.setDisable(false);
    }

    @FXML
    protected void cancelAction(ActionEvent actionEvent) {
        setNodeVisibility(confirmationOverlay, false);
        if (pendingCancelAction != null) pendingCancelAction.run();
        else setNodeVisibility(pauseMenuOverlay, true);
        pendingCancelAction = null;
        pendingConfirmationAction = null;
        if (pauseButton != null) pauseButton.setDisable(false);
        if (gameBoard != null) gameBoard.requestFocus();
    }

    protected void closeCurtains(Runnable onFinished) {
        if (curtainLeft == null || curtainRight == null) {
            onFinished.run();
            return;
        }
        curtainLeft.setVisible(true);
        curtainLeft.setManaged(true);
        curtainRight.setVisible(true);
        curtainRight.setManaged(true);
        TranslateTransition leftTransition = new TranslateTransition(Duration.millis(1200), curtainLeft);
        leftTransition.setFromX(curtainLeft.getTranslateX());
        leftTransition.setToX(0);
        TranslateTransition rightTransition = new TranslateTransition(Duration.millis(1200), curtainRight);
        rightTransition.setFromX(curtainRight.getTranslateX());
        rightTransition.setToX(0);
        leftTransition.setInterpolator(Interpolator.EASE_OUT);
        rightTransition.setInterpolator(Interpolator.EASE_OUT);
        ParallelTransition parallel = new ParallelTransition(leftTransition, rightTransition);
        parallel.setOnFinished(e -> onFinished.run());
        parallel.play();
    }

    protected void startNewGame() {
        AudioManager.getInstance().setSuppressSfx(false);
        AudioManager.getInstance().stopAll();
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }
        if (mainContent != null) mainContent.setOpacity(1.0);
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }
        gameActive = true;
        if (animationHandler != null) {
            animationHandler.setTickInterval(currentLevel.getTickInterval(0));
            animationHandler.ensureInitialized();
            animationHandler.start();
        }
        if (gameController != null) gameController.createNewGame();
        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }
        if (gameBoard != null) gameBoard.requestFocus();
        try {
            String bgResource = "/sounds/main.mp3";
            if (currentLevel instanceof Level1) {
                bgResource = "/sounds/level1.mp3";
            } else if (currentLevel instanceof Level2) {
                bgResource = "/sounds/level2.mp3";
            } else if (currentLevel instanceof com.comp2042.tetris.model.level.Level3) {
                bgResource = "/sounds/level3.mp3";
            } else if (currentLevel instanceof ClassicLevel) {
                bgResource = "/sounds/classic.mp3";
            }
            backgroundChangeId++;
            restartBackgroundMusicImmediate(bgResource);
        } catch (Throwable ignored) {
        }
    }

    protected void refreshGameBehindCurtains() {
        if (gameViewPresenter != null) gameViewPresenter.hideGameOver();
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }
        gameActive = true;
        if (animationHandler != null) {
            animationHandler.ensureInitialized();
            if (gameController != null) gameController.createNewGame();
        }
        if (mainContent != null) {
            mainContent.setVisible(true);
            mainContent.setManaged(true);
            mainContent.setMouseTransparent(false);
            mainContent.setOpacity(0.0);
        }
    }

    protected void startGameLoopAndTimer() {
        if (animationHandler != null) animationHandler.start();
        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }
        if (gameBoard != null) gameBoard.requestFocus();
    }

    protected void showMainMenu() {
        gameActive = false;
        try {
            if (inputHandler != null) inputHandler.setInvertHorizontal(false);
        } catch (Throwable ignored) {
        }
        if (animationHandler != null && !animationHandler.isGameOver() && !animationHandler.isPaused())
            animationHandler.togglePause();
        if (curtainLeft != null) {
            curtainLeft.setVisible(false);
            curtainLeft.setManaged(false);
        }
        if (curtainRight != null) {
            curtainRight.setVisible(false);
            curtainRight.setManaged(false);
        }
        setNodeVisibility(mainMenuOverlay, true);
        setNodeVisibility(mainMenuCard, true);
        setMainContentVisible(false);
        if (pauseButton != null) {
            pauseButton.setVisible(false);
            pauseButton.setManaged(false);
        }
    }

    protected void setMainContentVisible(boolean visible) {
        if (mainContent == null) return;
        mainContent.setManaged(true);
        mainContent.setVisible(true);
        mainContent.setMouseTransparent(!visible);
        mainContent.setOpacity(visible ? 1.0 : 0.0);
        mainContent.requestLayout();
    }

    protected void setNodeVisibility(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }

    protected void playCurtainAnimation() {
        if (curtainLeft == null || curtainRight == null) {
            startNewGame();
            return;
        }
        curtainLeft.setTranslateX(0);
        curtainRight.setTranslateX(0);
        refreshGameBehindCurtains();
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            TranslateTransition leftTransition = new TranslateTransition(Duration.millis(1200), curtainLeft);
            leftTransition.setFromX(0);
            leftTransition.setToX(-900);
            leftTransition.setInterpolator(Interpolator.EASE_OUT);
            TranslateTransition rightTransition = new TranslateTransition(Duration.millis(1200), curtainRight);
            rightTransition.setFromX(0);
            rightTransition.setToX(900);
            rightTransition.setInterpolator(Interpolator.EASE_OUT);
            ParallelTransition parallelTransition = new ParallelTransition(leftTransition, rightTransition);
            parallelTransition.setOnFinished(event -> {
                curtainLeft.setVisible(false);
                curtainLeft.setManaged(false);
                curtainRight.setVisible(false);
                curtainRight.setManaged(false);
                if (pauseButton != null) {
                    pauseButton.setVisible(true);
                    pauseButton.setManaged(true);
                    pauseButton.setDisable(false);
                }
                if (mainContent != null) {
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(600), mainContent);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.setInterpolator(Interpolator.EASE_IN);
                    fadeIn.setOnFinished(fadeEvent -> startGameLoopAndTimer());
                    fadeIn.play();
                } else {
                    setMainContentVisible(true);
                    startGameLoopAndTimer();
                }
            });
            parallelTransition.play();
        });
        pause.play();
    }

    protected void togglePauseState() {
        if (animationHandler == null || animationHandler.isGameOver()) return;
        animationHandler.togglePause();
        boolean isPaused = animationHandler.isPaused();
        updatePauseUi(isPaused);
        if (gameTimer != null) {
            if (isPaused) gameTimer.pause();
            else gameTimer.resume();
        }
        if (gameBoard != null) gameBoard.requestFocus();
    }

    protected void updatePauseUi(boolean paused) {
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(paused);
            pauseMenuOverlay.setManaged(paused);
        }
    }

    protected void switchBackgroundTo(String resourcePath) {
        try {
            URL newUrl = getClass().getResource(resourcePath);
            if (newUrl == null) return;

            final long myChangeId = ++backgroundChangeId;

            SafeMediaPlayer oldPlayer = mainPlayer != null ? mainPlayer : startPlayer;
            SafeMediaPlayer newPlayer = new SafeMediaPlayer(newUrl);
            if (!newPlayer.isAvailable()) return;
            newPlayer.setVolume(0.0);
            newPlayer.setCycleCount(Animation.INDEFINITE);
            newPlayer.play();
            double targetVolume = 0.5;
            double durationSeconds = 1.2;
            double initialOldVolume = oldPlayer != null ? oldPlayer.getVolume() : 1.0;
            int steps = 12;
            Timeline fadeTimeline = new Timeline();
            try {
                if (musicVolumeSlider != null) targetVolume = 0.5 * (musicVolumeSlider.getValue() / 100.0);
            } catch (Throwable ignored) {
            }
            for (int i = 0; i <= steps; i++) {
                final double frac = (double) i / steps;
                final double oldVol = initialOldVolume * (1.0 - frac);
                final double newVol = targetVolume * frac;
                double time = frac * durationSeconds;
                fadeTimeline.getKeyFrames().add(new javafx.animation.KeyFrame(Duration.seconds(time), evt -> {
                    try {
                        if (oldPlayer != null) oldPlayer.setVolume(oldVol);
                        if (newPlayer != null) newPlayer.setVolume(newVol);
                    } catch (Throwable ignored) {
                    }
                }));
            }
            fadeTimeline.setOnFinished(e -> {
                if (myChangeId != backgroundChangeId) {
                    try {
                        if (newPlayer != null) {
                            newPlayer.stop();
                            newPlayer.dispose();
                        }
                    } catch (Throwable ignored) {
                    }
                    return;
                }
                try {
                    if (oldPlayer != null && oldPlayer != newPlayer) {
                        oldPlayer.stop();
                        oldPlayer.dispose();
                        if (oldPlayer == startPlayer) startPlayer = null;
                        if (oldPlayer == mainPlayer) mainPlayer = null;
                    }
                } catch (Throwable ignored) {
                }
                mainPlayer = newPlayer;
                activeBackgroundResourcePath = resourcePath;
            });
            fadeTimeline.play();
        } catch (Exception e) {
            LOGGER.warn("Failed to switch background music to {}", resourcePath, e);
        }
    }

    protected void restartMainMenuMusicImmediate() {
        try {
            backgroundChangeId++;

            URL mainUrl = getClass().getResource("/sounds/main.mp3");
            if (mainUrl == null) return;

            try {
                if (mainPlayer != null) {
                    mainPlayer.stop();
                    mainPlayer.dispose();
                    mainPlayer = null;
                }
            } catch (Throwable ignored) {
            }

            try {
                if (startPlayer != null) {
                    startPlayer.stop();
                    startPlayer.dispose();
                    startPlayer = null;
                }
            } catch (Throwable ignored) {
            }

            SafeMediaPlayer newMain = new SafeMediaPlayer(mainUrl);
            try {
                double sliderVolume = musicVolumeSlider != null ? musicVolumeSlider.getValue() / 100.0 : 0.5;
                newMain.setVolume(Math.max(0.0, Math.min(1.0, sliderVolume * 0.5)));
            } catch (Throwable ignored) {
            }
            newMain.setCycleCount(Animation.INDEFINITE);
            newMain.play();

            mainPlayer = newMain;
            activeBackgroundResourcePath = "/sounds/main.mp3";
        } catch (Exception e) {
            LOGGER.warn("Failed to restart main menu music immediately", e);
        }
    }

    protected void restartBackgroundMusicImmediate(String resourcePath) {
        try {
            if (resourcePath == null) return;
            URL url = getClass().getResource(resourcePath);
            if (url == null) return;

            backgroundChangeId++;

            try {
                if (mainPlayer != null) {
                    mainPlayer.stop();
                    mainPlayer.dispose();
                    mainPlayer = null;
                }
            } catch (Throwable ignored) {
            }
            try {
                if (startPlayer != null) {
                    startPlayer.stop();
                    startPlayer.dispose();
                    startPlayer = null;
                }
            } catch (Throwable ignored) {
            }

            SafeMediaPlayer newPlayer = new SafeMediaPlayer(url);
            newPlayer.setCycleCount(Animation.INDEFINITE);
            double uiVol = 0.5;
            try {
                if (musicVolumeSlider != null) uiVol = (musicVolumeSlider.getValue() / 100.0);
            } catch (Throwable ignored) {
            }
            newPlayer.setVolume(Math.max(0.0, Math.min(1.0, uiVol * 0.5)));
            newPlayer.play();

            mainPlayer = newPlayer;
            activeBackgroundResourcePath = resourcePath;
        } catch (Exception e) {
            LOGGER.debug("Failed to restart background music immediately for {}", resourcePath, e);
        }
    }

    protected void fadeToMainMusic() {
        try {
            URL mainUrl = getClass().getResource("/sounds/main.mp3");
            if (mainUrl != null) {
                activeBackgroundResourcePath = "/sounds/main.mp3";
                mainPlayer = new SafeMediaPlayer(mainUrl);
                mainPlayer.setVolume(0.0);
                mainPlayer.setCycleCount(Animation.INDEFINITE);
                mainPlayer.play();
                double targetMainVolume = 0.5;
                double durationSeconds = 3.5;
                double initialStartVolume = startPlayer != null ? startPlayer.getVolume() : 1.0;
                int steps = 35;
                Timeline fadeTimeline = new Timeline();
                try {
                    if (musicVolumeSlider != null) targetMainVolume = 0.5 * (musicVolumeSlider.getValue() / 100.0);
                } catch (Throwable ignored) {
                }
                for (int i = 0; i <= steps; i++) {
                    final double frac = (double) i / steps;
                    final double startVol = initialStartVolume * (1.0 - frac);
                    final double mainVol = targetMainVolume * frac;
                    double time = frac * durationSeconds;
                    fadeTimeline.getKeyFrames().add(new javafx.animation.KeyFrame(Duration.seconds(time), evt -> {
                        if (startPlayer != null) startPlayer.setVolume(startVol);
                        if (mainPlayer != null) mainPlayer.setVolume(mainVol);
                    }));
                }
                fadeTimeline.setOnFinished(e -> {
                    if (startPlayer != null) {
                        startPlayer.stop();
                        startPlayer.dispose();
                        startPlayer = null;
                    }
                });
                fadeTimeline.play();
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to load main sound", e);
        }
    }

    protected abstract void stopFlickerEffect();

    protected abstract void startFlickerEffect();

    protected abstract void setupHoverSounds();

    protected abstract void setupClickSounds();

}