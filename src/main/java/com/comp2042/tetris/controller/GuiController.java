/**
 * updates the screen, notifications and game over
 * it is basically what the player sees
 */

package com.comp2042.tetris.controller;

import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.view.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import com.comp2042.tetris.game.GameTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.Objects;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import javafx.scene.Parent;


public class GuiController implements Initializable, IGuiController, GameEventListener {


    @FXML
    private ImageView staticScreen;

    @FXML
    private Pane background1;

    @FXML
    private Pane background2;

    @FXML
    private Pane pixelStarLayer;

    @FXML
    private HBox mainContent;

    @FXML//add button for pausing
    private Button pauseButton;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private StackPane pauseMenuOverlay;

    @FXML
    private VBox pauseMenuCard;

    @FXML
    private StackPane confirmationOverlay;

    @FXML
    private Label confirmationMessage;

    @FXML
    private StackPane musicControlOverlay;

    @FXML
    private StackPane gameOverOverlay;

    @FXML
    private BorderPane gameBoard;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private Pane activeBrickLayer;

    @FXML
    private GridPane brickPanel;

    @FXML
    private Pane ghostBrickLayer;

    @FXML
    private GridPane ghostBrickPanel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private OverlayPanel gameOverPanel;

    @FXML
    private StackPane mainMenuOverlay;

    @FXML
    private VBox mainMenuCard;

    // Level selection overlay (added for level choice after Start Game)
    @FXML
    private StackPane levelSelectOverlay;

    // Simple enum to track the selected level; currently only CLASSIC is wired into gameplay
    private enum Level { CLASSIC, L1, L2 }

    private Level selectedLevel = Level.CLASSIC;


    @FXML
    private Pane curtainLeft;

    @FXML
    private Pane curtainRight;

    private IGameController gameController;

    private AnimationHandler animationHandler;

    private InputHandler inputHandler;

    private BoardRenderer boardRenderer;

    private GameViewPresenter gameViewPresenter;

    private GuiControllerDependencies dependencies;

    private boolean gameActive = false;

    private Runnable pendingConfirmationAction;

    private Runnable pendingCancelAction;

    private ShootingStarAnimator shootingStarAnimator;
    private PixelStarAnimator pixelStarAnimator;
    private NebulaCloudAnimator nebulaCloudAnimator;
    private GameTimer gameTimer;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFont();
        gameOverPanel.setVisible(false);
        setupGamePanelKeyListener();
        // Background scrolling animation removed - static background now
        setupNebulaClouds();
        setupPixelStars();
        setupShootingStars();
        gameTimer = new GameTimer(timerLabel);
        showMainMenu();

        // Load the game over screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameOverScreen.fxml"));
            Parent gameOverRoot = loader.load();
            gameOverOverlay.getChildren().add(gameOverRoot);
        } catch (Exception e) {
            System.out.println("Failed to load game over screen: " + e.getMessage());
        }
        playIntroSequence();
    }

    private void playIntroSequence() {
        if (staticScreen != null) {
            StaticScreenAnimator staticAnimator = new StaticScreenAnimator();
            DistortionAnimator distortionAnimator = new DistortionAnimator();

            // The main menu is inside mainMenuOverlay
            // We want to distort this pane as the static fades out

            staticAnimator.play(staticScreen, () -> {
                // This runs when static starts fading (opacity < 1.0)
                // Apply distortion to the menu overlay for 3.5 seconds (matching fade duration)
                if (mainMenuOverlay != null) {
                    distortionAnimator.applyGlitchTransition(mainMenuOverlay, 3.5);
                }
            });
        }
    }

    private void setupNebulaClouds() {
        nebulaCloudAnimator = new NebulaCloudAnimator();
        // Add 5 colorful nebula clouds to the background
        nebulaCloudAnimator.fillWithClouds(pixelStarLayer, 5);
    }

    private void setupPixelStars() {
        pixelStarAnimator = new PixelStarAnimator();
        // Fill the static star layer with 150 twinkling pixel stars
        pixelStarAnimator.fillScreenWithStars(pixelStarLayer, 150);
    }




    private void setupShootingStars() {
        shootingStarAnimator = new ShootingStarAnimator();
        // Create periodic shooting stars on the static star layer
        javafx.animation.Timeline starTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(1),
                e -> {
                    // 80% chance each second to create a shooting star (even more frequent!)
                    if (Math.random() < 0.8) {
                        shootingStarAnimator.createAndAnimateStar(pixelStarLayer);
                    }
                }
            )
        );
        starTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        starTimeline.play();
    }

    public void setDependencies(GuiControllerDependencies dependencies) {
        this.dependencies = Objects.requireNonNull(dependencies, "dependencies");
        this.animationHandler = dependencies.animationHandler();
        this.inputHandler = dependencies.inputHandler();
        this.boardRenderer = dependencies.boardRenderer();
        this.gameViewPresenter = dependencies.gameViewPresenter();
        configureInputCommands();
        updatePauseUi(false);

    }

    private void setupFont() {
        try {
            // Note the path: /fonts/PressStart2P.ttf
            Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 20);
            Font.loadFont(getClass().getClassLoader().getResource("fonts/digital.ttf").toExternalForm(), 38);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Subspace.otf"), 52);
        } catch (Exception e) {
            System.out.println("Failed to load font: " + e.getMessage());
        }

    }

    private void setupGamePanelKeyListener() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyInput);
    }
    private void configureInputCommands() {
        if (inputHandler == null) {
            return;
        }
        inputHandler.registerCommand(this::startNewGame, false, KeyCode.N);
        inputHandler.registerCommand(this::togglePauseState, false, KeyCode.P);
    }

    void handleTick() {
        if (!gameActive) {
            return;
        }
        if (inputHandler != null) {
            inputHandler.moveDown(EventSource.THREAD);
        }
        gamePanel.requestFocus();
    }

    private void handleKeyInput(KeyEvent keyEvent) {
        if (inputHandler != null) {
            inputHandler.handle(keyEvent);
        }
        gamePanel.requestFocus();
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.initializeGame(new GameStateSnapshot(boardMatrix, brick));
        }
        // DO NOT auto-start the animation handler here
        // The game loop should only start when startNewGame() is explicitly called
        updatePauseUi(false);
    }

    @Override
    public void refreshGameBackground(int[][] board) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.refreshBoard(board);
        }
    }

    private void updatePauseUi(boolean paused) {
        if (pauseMenuOverlay != null) {
            pauseMenuOverlay.setVisible(paused);
            pauseMenuOverlay.setManaged(paused);
        }
    }

    @Override
    public void setGameController(IGameController gameController) {
        this.gameController = gameController;
        if (inputHandler != null) {
            inputHandler.setGameController(gameController);
        }
    }

    @Override
    public void gameOver() {
        ensureConfigured();
        gameActive = false;
        if (animationHandler != null) {
            animationHandler.onGameOver();
        }
        updatePauseUi(false);

        // Stop the timer when game is over
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Keep pause button visible but disable it when game is over
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }

        // Show the new game over screen
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.setManaged(true);
        }

        // Wait 3 seconds, then close curtains and show play again prompt
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
        pause.setOnFinished(e -> {
            // Hide game over overlay
            if (gameOverOverlay != null) {
                gameOverOverlay.setVisible(false);
                gameOverOverlay.setManaged(false);
            }
            // Reset curtains to open position for animation
            if (curtainLeft != null) {
                curtainLeft.setTranslateX(-900);
            }
            if (curtainRight != null) {
                curtainRight.setTranslateX(900);
            }
            // Close curtains
            closeCurtains(() -> {
                // Show play again confirmation
                if (confirmationMessage != null) {
                    confirmationMessage.setText("Play again?");
                }
                pendingConfirmationAction = () -> {
                    setNodeVisibility(confirmationOverlay, false);
                    // Hide curtains
                    if (curtainLeft != null) {
                        curtainLeft.setVisible(false);
                        curtainLeft.setManaged(false);
                    }
                    if (curtainRight != null) {
                        curtainRight.setVisible(false);
                        curtainRight.setManaged(false);
                    }
                    // Show mainContent
                    if (mainContent != null) {
                        mainContent.setVisible(true);
                        mainContent.setManaged(true);
                        mainContent.setOpacity(1.0);
                        mainContent.setMouseTransparent(false);
                    }
                    // Start new game directly
                    startNewGameDirect();
                };
                pendingCancelAction = () -> {
                    setNodeVisibility(confirmationOverlay, false);
                    showMainMenu();
                };
                setNodeVisibility(confirmationOverlay, true);
            });
        });
        pause.play();
    }
    public void newGame(ActionEvent actionEvent) {
        startNewGame();
    }
    public void pauseGame(ActionEvent actionEvent) {
        togglePauseState();
    }

    @FXML
    private void handleStartGame(ActionEvent actionEvent) {
        // Show the level selection overlay instead of immediately starting.
        // This preserves the existing curtain/start flow which will be triggered by the level handlers.
        if (levelSelectOverlay != null) {
            setNodeVisibility(levelSelectOverlay, true);
            // Hide the main menu when the level selection is shown
            setNodeVisibility(mainMenuOverlay, false);
        } else {
            // Fallback: if overlay missing, proceed with existing start flow
            // CRITICAL: Ensure mainContent is completely invisible (opacity 0) to prevent flash
            if (mainContent != null) {
                mainContent.setOpacity(0.0);
                mainContent.setVisible(true);
            }

            // IMPORTANT: Show curtains FIRST (covering everything) to prevent flash of old game state
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

    // Level selection handlers
    @FXML
    private void handleLevelClassic(ActionEvent actionEvent) {
        selectedLevel = Level.CLASSIC;
        // Hide overlay and start existing curtain animation -> will refresh/create new game behind curtains
        setNodeVisibility(levelSelectOverlay, false);
        // Show curtains and play animation sequence
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
    private void handleLevelL1(ActionEvent actionEvent) {
        selectedLevel = Level.L1;
        // For now L1 behaves the same as classic but selection is recorded for future use
        setNodeVisibility(levelSelectOverlay, false);
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
    private void handleLevelL2(ActionEvent actionEvent) {
        selectedLevel = Level.L2;
        // For now L2 behaves the same as classic but selection is recorded for future use
        setNodeVisibility(levelSelectOverlay, false);
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
    private void cancelLevelSelect(ActionEvent actionEvent) {
        // Hide overlay and return to main menu
        setNodeVisibility(levelSelectOverlay, false);
        setNodeVisibility(mainMenuOverlay, true);
    }

    @FXML
    private void exitGame(ActionEvent actionEvent) {
        Platform.exit();
    }

    private void startNewGame() {
        ensureConfigured();
        if (animationHandler == null) {
            return;
        }
        if (gameViewPresenter != null) {
            gameViewPresenter.hideGameOver();
        }
        updatePauseUi(false);

        // Hide the game over screen
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }

        // Show pause button when game starts
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }

        gameActive = true;
        animationHandler.ensureInitialized();
        if (gameController != null) {
            gameController.createNewGame();
        }
        animationHandler.start();

        // Reset and start the timer when game starts
        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }

        gamePanel.requestFocus();
    }

    /**
     * Refreshes the game board (creates new game) behind the closed curtains.
     * This is called BEFORE the curtain opens, so the fresh board is ready to be revealed.
     * Does NOT start the game loop or timer yet.
     */
    private void refreshGameBehindCurtains() {
        ensureConfigured();
        if (animationHandler == null) {
            return;
        }
        if (gameViewPresenter != null) {
            gameViewPresenter.hideGameOver();
        }
        updatePauseUi(false);

        // Hide the game over screen
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }

        gameActive = true;
        animationHandler.ensureInitialized();

        // Create the new game board (this renders fresh blocks behind the curtains)
        if (gameController != null) {
            gameController.createNewGame();
        }

        // Make mainContent visible but with opacity 0, ready for fade-in after curtain opens
        if (mainContent != null) {
            mainContent.setVisible(true);
            mainContent.setManaged(true);
            mainContent.setMouseTransparent(false);
            mainContent.setOpacity(0.0);  // Ready for fade-in, behind curtains
        }

        // DO NOT start animation handler or timer yet - that happens after curtain opens
    }

    /**
     * Starts the game loop and timer AFTER the curtain has opened and fade-in completes.
     * The game board has already been created by refreshGameBehindCurtains().
     */
    private void startGameLoopAndTimer() {
        if (animationHandler != null) {
            animationHandler.start();
        }

        // Reset and start the timer
        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }

        // Show pause button after curtain opens
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }

        gamePanel.requestFocus();
    }

    private void showMainMenu() {
        gameActive = false;
        // Pause the game if it's running (not already paused and not game over)
        if (animationHandler != null && !animationHandler.isGameOver() && !animationHandler.isPaused()) {
            animationHandler.togglePause();
        }
        // Hide curtains
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
       // setNodeVisibility(settingsPanel, false);
        setMainContentVisible(false);
        if (pauseButton != null) {
            pauseButton.setVisible(false);
            pauseButton.setManaged(false);
        }
    }

    private void hideMainMenu() {
        setNodeVisibility(mainMenuOverlay, false);
       // setNodeVisibility(settingsPanel, false);
        setNodeVisibility(mainMenuCard, true);
        setMainContentVisible(true);
    }

    private void setMainContentVisible(boolean visible) {
        if (mainContent == null) {
            return;
        }

        // Keep layout space so the controller frame never shifts, but hide visuals and input when menu is up
        mainContent.setManaged(true);
        mainContent.setVisible(true);
        mainContent.setMouseTransparent(!visible);
        mainContent.setOpacity(visible ? 1.0 : 0.0);
        mainContent.requestLayout();
    }

    private void setNodeVisibility(Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
            node.setManaged(visible);
        }
    }

    private void playCurtainAnimation() {
        if (curtainLeft == null || curtainRight == null) {
            // If curtains are not available, just start the game
            startNewGame();
            return;
        }

        // Curtains are already positioned and visible from handleStartGame()
        // Just ensure they're in the correct state (defensive programming)
        curtainLeft.setTranslateX(0);
        curtainRight.setTranslateX(0);

        // IMPORTANT: Refresh the game BEFORE curtain opens
        // This ensures fresh board is ready behind the curtains
        refreshGameBehindCurtains();

        // Wait a brief moment with curtains closed
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(500));
        pause.setOnFinished(e -> {
            // Create the opening animation - slower and more dramatic
            // Window is 900px wide, so each curtain needs to move 900px (full width) to clear the window
            javafx.animation.TranslateTransition leftTransition = new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(1200), curtainLeft);
            leftTransition.setFromX(0);
            leftTransition.setToX(-900); // Move left curtain completely off-screen to the left

            javafx.animation.TranslateTransition rightTransition = new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(1200), curtainRight);
            rightTransition.setFromX(0);
            rightTransition.setToX(900); // Move right curtain completely off-screen to the right

            // Use EASE_OUT interpolator for natural, decelerating motion (starts fast, slows down at the end)
            leftTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            rightTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            // Play both transitions together
            javafx.animation.ParallelTransition parallelTransition = new javafx.animation.ParallelTransition(
                leftTransition, rightTransition);

            parallelTransition.setOnFinished(event -> {
                // Hide curtains after animation
                curtainLeft.setVisible(false);
                curtainLeft.setManaged(false);
                curtainRight.setVisible(false);
                curtainRight.setManaged(false);

                // Show pause button after curtain opens
                if (pauseButton != null) {
                    pauseButton.setVisible(true);
                    pauseButton.setManaged(true);
                    pauseButton.setDisable(false);
                }

                // mainContent is already visible with opacity 0 (set in refreshGameBehindCurtains)
                // Fresh game board is already rendered behind the curtains
                // Now just fade it in smoothly
                if (mainContent != null) {
                    // Create smooth fade-in animation
                    javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(
                        javafx.util.Duration.millis(600), mainContent);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.setInterpolator(javafx.animation.Interpolator.EASE_IN);

                    fadeIn.setOnFinished(fadeEvent -> {
                        // Game already created before curtain opened
                        // Just start the game loop and timer now
                        startGameLoopAndTimer();
                    });

                    fadeIn.play();
                } else {
                    // Fallback if mainContent is null
                    setMainContentVisible(true);
                    startGameLoopAndTimer();
                }
            });

            parallelTransition.play();
        });

        pause.play();
    }

    private void togglePauseState() {
        ensureConfigured();
        if (animationHandler == null || animationHandler.isGameOver()) {
            return;
        }

        animationHandler.togglePause();
        boolean isPaused = animationHandler.isPaused();
        updatePauseUi(isPaused);

        // Pause or resume the timer based on game state
        if (gameTimer != null) {
            if (isPaused) {
                gameTimer.pause();
            } else {
                gameTimer.resume();
            }
        }

        gamePanel.requestFocus();
    }

    @Override
    public void onGameInitialized(GameStateSnapshot snapshot) {
        initGameView(snapshot.boardMatrix(), snapshot.viewData());
    }

    @Override
    public void onScoreChanged(ScoreChangeEvent event) {
        if (gameViewPresenter != null) {
            gameViewPresenter.updateScore(event);
        }
    }

    @Override
    public void onBrickUpdated(ViewData viewData) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            boolean paused = animationHandler != null && animationHandler.isPaused();
            gameViewPresenter.refreshBrick(viewData, paused);
        }
    }
    @Override
    public void onBoardUpdated(int[][] boardMatrix) {
        ensureConfigured();
        refreshGameBackground(boardMatrix);
    }
    @Override
    public void onLinesCleared(ClearRow clearRow) {
        ensureConfigured();
        if (gameViewPresenter != null) {
            gameViewPresenter.handleLinesCleared(clearRow);
        }
    }
    @Override
    public void onGameOver() {
        gameOver();
    }
    private void ensureConfigured() {
        if (dependencies == null) {
            throw new IllegalStateException("GuiController dependencies have not been configured. Inject dependencies before use.");
        }
    }

    GridPane getGamePanel() {
        return gamePanel;
    }

    GridPane getNextBrickPanel() {
        return nextBrickPanel;
    }

    GridPane getBrickPanel() {
        return brickPanel;
    }

    GridPane getGhostBrickPanel() {
        return ghostBrickPanel;
    }

    Label getScoreLabel() {
        return scoreLabel;
    }

    Group getGroupNotification() {
        return groupNotification;
    }

    OverlayPanel getGameOverPanel() {
        return gameOverPanel;
    }

    // ==================== Pause Menu Handlers ====================

    @FXML
    private void resumeGame(ActionEvent actionEvent) {
        togglePauseState(); // Resume the game
    }

    @FXML
    private void openMusicControl(ActionEvent actionEvent) {
        // Hide pause menu, show music control
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(musicControlOverlay, true);
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }
    }

    @FXML
    private void closeMusicControl(ActionEvent actionEvent) {
        // Hide music control, show pause menu
        setNodeVisibility(musicControlOverlay, false);
        setNodeVisibility(pauseMenuOverlay, true);
        if (pauseButton != null) {
            pauseButton.setDisable(false);
        }
    }


    @FXML
    private void startNewGameFromPause(ActionEvent actionEvent) {
        // Show confirmation dialog
        if (confirmationMessage != null) {
            confirmationMessage.setText("Start a new game?");
        }
        pendingConfirmationAction = () -> {
            // Hide pause menu
            setNodeVisibility(pauseMenuOverlay, false);
            // Start new game
            if (animationHandler != null && animationHandler.isPaused()) {
                togglePauseState(); // Unpause first
            }
            startNewGame();
        };
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }
    }

    @FXML
    private void goToMainMenuFromPause(ActionEvent actionEvent) {
        // Show confirmation dialog
        if (confirmationMessage != null) {
            confirmationMessage.setText("Return to main menu?");
        }
        pendingConfirmationAction = () -> {
            // Stop game and return to main menu
            gameActive = false;
            if (gameTimer != null) {
                gameTimer.stop();
            }
            // Unpause if paused, then trigger game over to stop everything
            if (animationHandler != null && animationHandler.isPaused()) {
                togglePauseState();
            }
            if (animationHandler != null) {
                animationHandler.onGameOver();
            }
            setNodeVisibility(pauseMenuOverlay, false);
            showMainMenu();
        };
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }
    }

    @FXML
    private void endGameFromPause(ActionEvent actionEvent) {
        // Show confirmation dialog
        if (confirmationMessage != null) {
            confirmationMessage.setText("End game and exit?");
        }
        pendingConfirmationAction = () -> {
            Platform.exit();
        };
        setNodeVisibility(pauseMenuOverlay, false);
        setNodeVisibility(confirmationOverlay, true);
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }
    }

    @FXML
    private void confirmAction(ActionEvent actionEvent) {
        // Execute pending action
        setNodeVisibility(confirmationOverlay, false);
        if (pendingConfirmationAction != null) {
            pendingConfirmationAction.run();
            pendingConfirmationAction = null;
        }
        pendingCancelAction = null;
        if (pauseButton != null) {
            pauseButton.setDisable(false);
        }
    }

    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        // Hide confirmation overlay
        setNodeVisibility(confirmationOverlay, false);

        if (pendingCancelAction != null) {
            // If a specific cancel action was provided (e.g., from game over flow), run it
            pendingCancelAction.run();
        } else {
            // No specific cancel handler -> return to pause menu (keep the game paused)
            setNodeVisibility(pauseMenuOverlay, true);
        }

        // Clear pending actions
        pendingCancelAction = null;
        pendingConfirmationAction = null;

        // Re-enable pause button so user can interact with pause menu
        if (pauseButton != null) {
            pauseButton.setDisable(false);
        }

        // Ensure keyboard focus remains on the game panel while paused
        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }

    private void closeCurtains(Runnable onFinished) {
        if (curtainLeft == null || curtainRight == null) {
            onFinished.run();
            return;
        }

        // Set visible
        curtainLeft.setVisible(true);
        curtainLeft.setManaged(true);
        curtainRight.setVisible(true);
        curtainRight.setManaged(true);

        // Animate from current position to 0,0
        javafx.animation.TranslateTransition leftTransition = new javafx.animation.TranslateTransition(
            javafx.util.Duration.millis(1200), curtainLeft);
        leftTransition.setFromX(curtainLeft.getTranslateX());
        leftTransition.setToX(0);

        javafx.animation.TranslateTransition rightTransition = new javafx.animation.TranslateTransition(
            javafx.util.Duration.millis(1200), curtainRight);
        rightTransition.setFromX(curtainRight.getTranslateX());
        rightTransition.setToX(0);

        leftTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        rightTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        javafx.animation.ParallelTransition parallel = new javafx.animation.ParallelTransition(leftTransition, rightTransition);
        parallel.setOnFinished(e -> onFinished.run());
        parallel.play();
    }

    private void startNewGameDirect() {
        ensureConfigured();
        if (animationHandler == null) {
            return;
        }
        if (gameViewPresenter != null) {
            gameViewPresenter.hideGameOver();
        }
        updatePauseUi(false);

        // Hide the game over screen
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }

        // Show pause button when game starts
        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }

        gameActive = true;
        animationHandler.ensureInitialized();
        if (gameController != null) {
            gameController.createNewGame();
        }
        animationHandler.start();

        // Reset and start the timer when game starts
        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }

        gamePanel.requestFocus();
    }
}
