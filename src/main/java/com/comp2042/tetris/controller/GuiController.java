package com.comp2042.tetris.controller;
import com.comp2042.tetris.game.GameTimer;
import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameStateSnapshot;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.model.event.BrickPlacedEvent;
import com.comp2042.tetris.model.level.ClassicLevel;
import com.comp2042.tetris.model.level.Level1;
import com.comp2042.tetris.model.level.Level2;
import com.comp2042.tetris.view.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.stage.Popup;

import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import com.comp2042.tetris.utils.AudioManager;

public class GuiController extends MenuController implements Initializable, IGuiController, GameEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiController.class);

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private Label debugNotificationLabel;

    @FXML
    private Pane activeBrickLayer;

    @FXML
    private GridPane brickPanel;

    @FXML
    private Pane ghostBrickLayer;

    @FXML
    private GridPane ghostBrickPanel;

    @FXML
    private OverlayPanel gameOverPanel;

    private BoardRenderer boardRenderer;

    private GuiControllerDependencies dependencies;

    private ShootingStarAnimator shootingStarAnimator;
    private PixelStarAnimator pixelStarAnimator;
    private NebulaCloudAnimator nebulaCloudAnimator;

    private int lastRotationUsed = -1;

    private int rotationPopupTicks = 0;
    private Timeline flickerScheduler;
    private final Random random = new Random();

    private SoundManager soundManager;

    private InputController inputController;

    private GameOverManager gameOverManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFont();
        if (gameOverPanel != null) gameOverPanel.setVisible(false);
        setupNebulaClouds();
        setupPixelStars();
        setupShootingStars();
        gameTimer = new GameTimer(timerLabel);

        soundManager = new SoundManager(this);

        // Play start sound - delegates to SoundManager
        startPlayer = soundManager.playStartSound();

        showMainMenu();

        // Load the game over screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameOverScreen.fxml"));
            Parent gameOverRoot = loader.load();
            if (gameOverOverlay != null) gameOverOverlay.getChildren().add(gameOverRoot);
        } catch (Exception e) {
            LOGGER.warn("Failed to load game over screen", e);
        }

        playIntroSequence();

        // Delay the music fade until after the static animation completes - delegates to SoundManager
        soundManager.scheduleFadeToMainMusic(this::fadeToMainMusic);

        // Initialize music & SFX sliders - delegates to SoundManager
        soundManager.initializeVolumeSliders(musicVolumeSlider, sfxVolumeSlider, () -> mainPlayer);

        // Load small SFX - delegates to SoundManager
        soundManager.loadSmallSFX();

        Platform.runLater(() -> { try { setupInputController(); } catch (Throwable ignored) {} });

        // Attach a tooltip and a Popup-based hover hint to the Classic level button so the full description is always visible
        try {
            if (levelClassicButton != null) {
                // Popup-based hint (more reliable and won't be clipped) that appears immediately on hover
                Platform.runLater(() -> {
                    installLevelHint(levelClassicButton, "This is your normal tetris game with a galxy twist, HAVE FUN");
                });
            }
            // Also attach hints to L1/L2/L3
            Platform.runLater(() -> {
                try { if (levelL1Button != null) installLevelHint(levelL1Button, "Every point pushes the accelerator! The higher your score, the faster the blocks fall"); } catch (Throwable ignored) {}
                try { if (levelL2Button != null) installLevelHint(levelL2Button, "You only get four rotations per piece. Think fast, rotate smarter, and place perfectly"); } catch (Throwable ignored) {}
                try { if (levelL3Button != null) installLevelHint(levelL3Button, "Welcome to pure madness, random garbage lines, flickering screen, reversed controls, and no next-piece preview. Only true Tetris warriors survive here."); } catch (Throwable ignored) {}
            });
        } catch (Throwable ignored) {}

    }

    // Helper: installs a Popup-based hint for a button with consistent styling and behavior
    private void installLevelHint(Button target, String message) {
        if (target == null || message == null) return;
        try {
            Popup hintPopup = new Popup();
            Label hintLabel = new Label(message);
            hintLabel.setStyle("-fx-background-color: rgba(0,0,0,0.95); -fx-text-fill: #ffffff; -fx-background-radius: 6; -fx-padding: 10 14;");
            hintLabel.setWrapText(true);
            hintLabel.setMaxWidth(520);
            hintLabel.setPrefWidth(520);
            hintLabel.setPadding(new Insets(10, 14, 10, 14));
            try { hintLabel.setFont(Font.font("Press Start 2P", 14)); } catch (Throwable ignored) {}
            hintPopup.getContent().add(hintLabel);
            hintPopup.setAutoHide(true);

            target.setOnMouseEntered(e -> {
                try {
                    if (target.getScene() == null || target.getScene().getWindow() == null) return;
                    hintLabel.applyCss(); hintLabel.layout();
                    double hintH = hintLabel.prefHeight(520);
                    double hintW = hintLabel.prefWidth(-1);
                    Point2D btnScreen = target.localToScreen(0, 0);
                    if (btnScreen == null) return;
                    double btnW = target.getWidth();
                    double x = btnScreen.getX() + (btnW - hintW) / 2.0;
                    double y = btnScreen.getY() - hintH - 12;
                    if (x < 0) x = btnScreen.getX();
                    if (y < 0) y = btnScreen.getY() + target.getHeight() + 6;
                    hintPopup.show(target.getScene().getWindow(), x, y);
                } catch (Throwable ignored) {}
            });
            target.setOnMouseExited(e -> { try { if (hintPopup.isShowing()) hintPopup.hide(); } catch (Throwable ignored) {} });
            target.setOnMousePressed(e -> { try { if (hintPopup.isShowing()) hintPopup.hide(); } catch (Throwable ignored) {} });
        } catch (Throwable ignored) {}
    }

    private void playIntroSequence() {
        if (staticScreen != null) {
            StaticScreenAnimator staticAnimator = new StaticScreenAnimator();
            DistortionAnimator distortionAnimator = new DistortionAnimator();
            staticAnimator.play(staticScreen, () -> { if (mainMenuOverlay != null) distortionAnimator.applyGlitchTransition(mainMenuOverlay, 3.5); });
        }
    }

    private void setupNebulaClouds() { nebulaCloudAnimator = new NebulaCloudAnimator(); nebulaCloudAnimator.fillWithClouds(pixelStarLayer, 5); }
    private void setupPixelStars() { pixelStarAnimator = new PixelStarAnimator(); pixelStarAnimator.fillScreenWithStars(pixelStarLayer, 150); }

    private void setupShootingStars() {
        shootingStarAnimator = new ShootingStarAnimator();
        Timeline starTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> { if (Math.random() < 0.8) shootingStarAnimator.createAndAnimateStar(pixelStarLayer); } ));
        starTimeline.setCycleCount(Animation.INDEFINITE);
        starTimeline.play();
    }

    public void setDependencies(GuiControllerDependencies dependencies) {
        this.dependencies = Objects.requireNonNull(dependencies, "dependencies");
        this.animationHandler = dependencies.animationHandler();
        this.inputHandler = dependencies.inputHandler();
        this.inputController = dependencies.inputController();
        this.boardRenderer = dependencies.boardRenderer();
        this.gameViewPresenter = dependencies.gameViewPresenter();
        configureInputCommands();
        updatePauseUi(false);

        // Initialize GameOverManager
        initializeGameOverManager();

        if (inputController != null && gamePanel != null) {
            inputController.setupKeyboardInput(gamePanel);
        }
    }

    private void setupFont() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 20);
            Font.loadFont(getClass().getClassLoader().getResource("fonts/digital.ttf").toExternalForm(), 38);
            Font.loadFont(getClass().getResourceAsStream("/fonts/Subspace.otf"), 52);
        } catch (Exception e) { LOGGER.warn("Failed to load font", e); }
    }

    private void setupInputController() {
        if (inputController == null) return;

        Scene scene = gamePanel.getScene();
        if (scene != null) {
            inputController.setupHoverSounds(scene);
            inputController.setupClickSounds(scene);
        } else {
            // Scene not yet available, listen for when it becomes available
            gamePanel.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    inputController.setupHoverSounds(newScene);
                    inputController.setupClickSounds(newScene);
                }
            });
        }
    }

    private void initializeGameOverManager() {
        if (animationHandler == null) {
            LOGGER.warn("Cannot initialize GameOverManager: animationHandler is null");
            return;
        }

        gameOverManager = new GameOverManager(
            gameOverOverlay,
            mainContent,
            curtainLeft,
            curtainRight,
            pauseButton,
            gameTimer,
            animationHandler
        );

        // Set up the callback for showing the confirmation dialog after game over
        gameOverManager.setOnShowConfirmationDialog(this::showGameOverConfirmationDialog);
    }

    private void configureInputCommands() {
        if (inputHandler == null) return;
        inputHandler.registerCommand(this::startNewGame, false, KeyCode.N);
        inputHandler.registerCommand(this::togglePauseState, false, KeyCode.P);
    }

    void handleTick() {
        if (!gameActive) return;
        if (inputHandler != null) inputHandler.moveDown(EventSource.THREAD);
        if (inputController != null) inputController.requestFocus(gamePanel);
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        ensureConfigured();
        if (gameViewPresenter != null) gameViewPresenter.initializeGame(new GameStateSnapshot(boardMatrix, brick));
        updatePauseUi(false);
    }

    @Override
    public void refreshGameBackground(int[][] board) { ensureConfigured(); if (gameViewPresenter != null) gameViewPresenter.refreshBoard(board); }

    protected void updatePauseUi(boolean paused) { if (pauseMenuOverlay != null) { pauseMenuOverlay.setVisible(paused); pauseMenuOverlay.setManaged(paused); } }

    @Override
    public void setGameController(IGameController gameController) { this.gameController = gameController; if (inputHandler != null) inputHandler.setGameController(gameController); }

    @Override
    public void gameOver() {
        stopFlickerEffect();
        ensureConfigured();
        gameActive = false;
        updatePauseUi(false);

        // Stop music players
        try { if (mainPlayer != null) mainPlayer.stop(); } catch (Exception ignored) {}
        try { if (startPlayer != null) { startPlayer.stop(); startPlayer.dispose(); startPlayer = null; } } catch (Exception ignored) {}

        // Delegate game over sequence to GameOverManager
        if (gameOverManager != null) {
            gameOverManager.showGameOver();
        } else {
            LOGGER.warn("GameOverManager is null, falling back to legacy game over logic");
            fallbackGameOver();
        }
    }

    private void showGameOverConfirmationDialog() {
        closeCurtains(() -> {
            if (confirmationMessage != null) confirmationMessage.setText("Play again?");

            pendingConfirmationAction = () -> {
                setNodeVisibility(confirmationOverlay, false);
                if (curtainLeft != null) { curtainLeft.setVisible(false); curtainLeft.setManaged(false); }
                if (curtainRight != null) { curtainRight.setVisible(false); curtainRight.setManaged(false); }
                if (mainContent != null) { mainContent.setVisible(true); mainContent.setManaged(true); mainContent.setOpacity(1.0); mainContent.setMouseTransparent(false); }
                startNewGameDirect();
            };

            pendingCancelAction = () -> {
                // Hide confirmation UI
                setNodeVisibility(confirmationOverlay, false);
                // Re-enable SFX
                try { AudioManager.getInstance().setSuppressSfx(false); AudioManager.getInstance().stopAll(); } catch (Throwable ignored) {}
                // Show main menu
                showMainMenu();
                // Reattach hover/click handlers via InputController
                Platform.runLater(() -> { try { setupInputController(); } catch (Throwable ignored) {} });
                // Force stop any existing background and switch to main.mp3 immediately
                Platform.runLater(() -> {
                    try {
                        restartMainMenuMusicImmediate();
                    } catch (Throwable ignored) {}
                });
            };

            setNodeVisibility(confirmationOverlay, true);
        });
    }

    private void fallbackGameOver() {
        if (animationHandler != null) animationHandler.onGameOver();

        AudioManager audio = AudioManager.getInstance();
        audio.setSuppressSfx(true);
        audio.playGameOver();

        if (gameTimer != null) gameTimer.stop();
        if (pauseButton != null) pauseButton.setDisable(true);
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.setManaged(true);
        }
        if (mainContent != null) mainContent.setOpacity(0.5);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            if (gameOverOverlay != null) { gameOverOverlay.setVisible(false); gameOverOverlay.setManaged(false); }
            if (mainContent != null) mainContent.setOpacity(1.0);
            if (curtainLeft != null) curtainLeft.setTranslateX(-900);
            if (curtainRight != null) curtainRight.setTranslateX(900);
            showGameOverConfirmationDialog();
        });
        pause.play();
    }
    @Override
    protected void togglePauseState() { ensureConfigured(); if (animationHandler == null || animationHandler.isGameOver()) return; animationHandler.togglePause(); boolean isPaused = animationHandler.isPaused(); updatePauseUi(isPaused); if (gameTimer != null) { if (isPaused) gameTimer.pause(); else gameTimer.resume(); } gamePanel.requestFocus(); }

    @Override
    public void onGameInitialized(GameStateSnapshot snapshot) { initGameView(snapshot.boardMatrix(), snapshot.viewData()); }
    @Override
    public void onScoreChanged(ScoreChangeEvent event) { if (gameViewPresenter != null) gameViewPresenter.updateScore(event); if (animationHandler != null && currentLevel != null) animationHandler.setTickInterval(currentLevel.getTickInterval(event.newScore())); }

    @Override
    public void onBrickUpdated(ViewData viewData) {
        ensureConfigured(); if (gameViewPresenter != null) { boolean paused = animationHandler != null && animationHandler.isPaused(); gameViewPresenter.refreshBrick(viewData, paused); }
        try {
            int rotationsUsed = viewData.getRotationsUsed(); int stateCount = viewData.getRotationStateCount();
            if (!(currentLevel instanceof Level2)) { lastRotationUsed = rotationsUsed; } else { if (stateCount > 1 && rotationsUsed > 0 && rotationsUsed != lastRotationUsed) showRotationNotification(rotationsUsed, stateCount); lastRotationUsed = rotationsUsed; }
        } catch (Exception ignored) {}
    }

    private void showRotationNotification(int rotationsUsed, int rotationStateCount) {
        if (groupNotification == null) return;
        // Reset the popup tick counter when a new brick appears (rotation counter decreased)
        if (rotationsUsed < lastRotationUsed) {
            rotationPopupTicks = 0;
        }
        // Advance tick for this rotation event; use it to compute a 4-step message sequence
        rotationPopupTicks++;
        int stepIndex = (rotationPopupTicks - 1) % 4; // 0..3
        int remaining = 3 - stepIndex; // produces 3,2,1,0
        String text = remaining == 1 ? "1 rotation left" : String.format("%d rotations left", remaining);
        NotificationPanel notificationPanel = new NotificationPanel(text);
        // Apply rotation-specific CSS so the panel is compact and left-center aligned
        try { notificationPanel.getStyleClass().add("rotation-notification"); } catch (Throwable ignored) {}
         double marginLeft = 160.0; Parent parent = groupNotification.getParent(); if (parent instanceof StackPane stack) {
             stack.getChildren().add(notificationPanel); StackPane.setAlignment(notificationPanel, Pos.CENTER_LEFT); notificationPanel.setTranslateX(marginLeft);
             double sceneWidth = stack.getScene() != null ? stack.getScene().getWidth() : UIConstants.WINDOW_WIDTH;
             LOGGER.debug("[RotationNotification] added to StackPane parent, text='{}' remaining={} sceneWidth={} translateX={}", text, remaining, sceneWidth, notificationPanel.getTranslateX());

            NotificationAnimator animator = new NotificationAnimator(); animator.playShowRotation(notificationPanel, stack.getChildren());
         } else {
            double sceneWidth = groupNotification.getScene() != null ? groupNotification.getScene().getWidth() : UIConstants.WINDOW_WIDTH; double leftX = - (sceneWidth / 2.0) + 70; notificationPanel.setLayoutX(leftX); notificationPanel.setLayoutY(0); LOGGER.debug("[RotationNotification] fallback to Group, text='{}' remaining={} sceneWidth={} leftX={}", text, remaining, sceneWidth, leftX);
             try { notificationPanel.getStyleClass().add("rotation-notification"); } catch (Throwable ignored) {}
             groupNotification.getChildren().add(notificationPanel);
             NotificationAnimator animator = new NotificationAnimator(); animator.playShowRotation(notificationPanel, groupNotification.getChildren()); }
    }

    @Override
    public void onBoardUpdated(int[][] boardMatrix) { ensureConfigured(); refreshGameBackground(boardMatrix); }
    @Override
    public void onLinesCleared(ClearRow clearRow) { ensureConfigured(); if (gameViewPresenter != null) gameViewPresenter.handleLinesCleared(clearRow); }
    @Override
    public void onGameOver() { gameOver(); }
    @Override
    public void onBrickPlaced(BrickPlacedEvent event) { LOGGER.debug("Brick placed event received, playing sound"); AudioManager.getInstance().playBricksTouch(); }

    private void ensureConfigured() { if (dependencies == null) throw new IllegalStateException("GuiController dependencies have not been configured. Inject dependencies before use."); }

    GridPane getGamePanel() { return gamePanel; }
    GridPane getNextBrickPanel() { return nextBrickPanel; }
    GridPane getBrickPanel() { return brickPanel; }
    GridPane getGhostBrickPanel() { return ghostBrickPanel; }
    Label getScoreLabel() { return scoreLabel; }
    Group getGroupNotification() { return groupNotification; }
    OverlayPanel getGameOverPanel() { return gameOverPanel; }
    BorderPane getGameBoard() { return gameBoard; }

    @FXML
    private void startNewGameDirect() {
        ensureConfigured();
        if (animationHandler == null) return;

        AudioManager.getInstance().setSuppressSfx(false);
        AudioManager.getInstance().stopAll();

        if (gameViewPresenter != null) gameViewPresenter.hideGameOver();
        updatePauseUi(false);

        // Use GameOverManager to hide overlay if available
        if (gameOverManager != null) {
            gameOverManager.hideOverlay();
        } else if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }

        if (pauseButton != null) {
            pauseButton.setVisible(true);
            pauseButton.setManaged(true);
            pauseButton.setDisable(false);
        }

        gameActive = true;
        animationHandler.ensureInitialized();

        if (gameController != null) gameController.createNewGame();
        animationHandler.start();

        if (gameTimer != null) {
            gameTimer.reset();
            gameTimer.start();
        }

        gamePanel.requestFocus();
        try {
            // Determine background music based on the selected level (primary). This ensures replaying a level plays its track.
            String bgResource = "/sounds/main.mp3";
            if (currentLevel instanceof Level1) {
                bgResource = "/sounds/level1.mp3";
            } else if (currentLevel instanceof Level2) {
                bgResource = "/sounds/level2.mp3";
            } else if (currentLevel.areControlsInverted()) {
                bgResource = "/sounds/level3.mp3";
            } else if (currentLevel instanceof ClassicLevel) {
                bgResource = "/sounds/classic.mp3";
            }

            backgroundChangeId++;
            switchBackgroundTo(bgResource);
        } catch (Throwable ignored) {}
    }
    @Override
    protected void startFlickerEffect() { flickerScheduler = new Timeline(new KeyFrame(Duration.seconds(30), e -> performFlicker())); flickerScheduler.setCycleCount(Timeline.INDEFINITE); flickerScheduler.play(); }

    @Override
    protected void stopFlickerEffect() { if (flickerScheduler != null) { flickerScheduler.stop(); flickerScheduler = null; } if (gamePanel != null) gamePanel.setOpacity(1.0); if (mainContent != null) mainContent.setOpacity(1.0); try { AudioManager.getInstance().stopFlickering(); } catch (Throwable ignored) {} }
    private void performFlicker() {
        if (mainContent == null) return;
        try { AudioManager.getInstance().playFlickering(); } catch (Throwable ignored) {}
        Timeline flickerAnim = new Timeline(); int flickerCount = 10 + random.nextInt(10); double stepInterval = 0.05;
        try { double audioDur = AudioManager.getInstance().getFlickeringDurationSeconds(); double visualDur = flickerCount * stepInterval; if (audioDur > visualDur + 0.05) stepInterval = audioDur / flickerCount; } catch (Throwable ignored) {}
        for (int i = 0; i < flickerCount; i++) { double timeOffset = i * stepInterval; double randomOpacity = 0.3 + random.nextDouble() * 0.7; flickerAnim.getKeyFrames().add(new KeyFrame(Duration.seconds(timeOffset), evt -> mainContent.setOpacity(randomOpacity))); }
        flickerAnim.getKeyFrames().add(new KeyFrame(Duration.seconds(flickerCount * stepInterval), evt -> mainContent.setOpacity(1.0)));
        flickerAnim.play();
        double flickerDurationSeconds = flickerCount * stepInterval;
        try { PauseTransition stopSound = new PauseTransition(Duration.seconds(flickerDurationSeconds)); stopSound.setOnFinished(e -> { try { AudioManager.getInstance().stopFlickering(); } catch (Throwable ignored) {} }); stopSound.play(); } catch (Throwable ignored) {}
    }

    @Override
    protected void setupHoverSounds() {
        setupInputController();
    }

    @Override
    protected void setupClickSounds() {

    }
}
