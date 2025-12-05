package com.comp2042.tetris.controller.core;

import com.comp2042.tetris.game.AnimationHandler;
import com.comp2042.tetris.game.GameTimer;
import com.comp2042.tetris.utils.AudioManager;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Manages all game over logic including overlay visibility, opacity transitions,
 * curtain animations, and post-game-over confirmation dialogs.
 */
public class GameOverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOverManager.class);

    private final StackPane gameOverOverlay;
    private final HBox mainContent;
    private final Pane curtainLeft;
    private final Pane curtainRight;
    private final Button pauseButton;
    private final GameTimer gameTimer;
    private final AnimationHandler animationHandler;

    // Callbacks for post-game-over actions
    private Runnable onShowConfirmationDialog;

    /**
     * Creates a new GameOverManager.
     *
     * @param gameOverOverlay the overlay containing the game over screen
     * @param mainContent the main content pane to adjust opacity
     * @param curtainLeft the left curtain for transitions
     * @param curtainRight the right curtain for transitions
     * @param pauseButton the pause button to disable
     * @param gameTimer the game timer to stop
     * @param animationHandler the animation handler to notify
     */
    public GameOverManager(StackPane gameOverOverlay, HBox mainContent, Pane curtainLeft,
                          Pane curtainRight, Button pauseButton, GameTimer gameTimer,
                          AnimationHandler animationHandler) {
        this.gameOverOverlay = gameOverOverlay;
        this.mainContent = mainContent;
        this.curtainLeft = curtainLeft;
        this.curtainRight = curtainRight;
        this.pauseButton = pauseButton;
        this.gameTimer = gameTimer;
        this.animationHandler = Objects.requireNonNull(animationHandler, "animationHandler");
    }

    /**
     * Sets the callback to invoke when the confirmation dialog should be shown
     * after the game over overlay has been displayed.
     *
     * @param onShowConfirmationDialog callback to show the confirmation dialog
     */
    public void setOnShowConfirmationDialog(Runnable onShowConfirmationDialog) {
        this.onShowConfirmationDialog = onShowConfirmationDialog;
    }

    /**
     * Initiates the game over sequence:
     * 1. Notifies the animation handler
     * 2. Stops music and plays game over sound
     * 3. Stops the game timer
     * 4. Disables the pause button
     * 5. Shows the game over overlay
     * 6. Reduces main content opacity
     * 7. After a delay, hides the overlay and shows confirmation dialog
     */
    public void showGameOver() {
        LOGGER.info("Initiating game over sequence");

        // Notify animation handler
        animationHandler.onGameOver();

        // Stop music and play game over sound
        AudioManager audio = AudioManager.getInstance();
        audio.setSuppressSfx(true);
        audio.playGameOver();

        // Stop game timer
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Disable pause button
        if (pauseButton != null) {
            pauseButton.setDisable(true);
        }

        // Show game over overlay
        showOverlay();

        // Dim main content
        setMainContentOpacity(0.5);

        // Schedule hiding of overlay and showing confirmation dialog
        scheduleConfirmationDialog();
    }

    /**
     * Shows the game over overlay.
     */
    private void showOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(true);
            gameOverOverlay.setManaged(true);
        }
    }

    /**
     * Hides the game over overlay.
     */
    public void hideOverlay() {
        if (gameOverOverlay != null) {
            gameOverOverlay.setVisible(false);
            gameOverOverlay.setManaged(false);
        }
    }

    /**
     * Sets the opacity of the main content.
     *
     * @param opacity the opacity value (0.0 to 1.0)
     */
    private void setMainContentOpacity(double opacity) {
        if (mainContent != null) {
            mainContent.setOpacity(opacity);
        }
    }

    /**
     * Resets the main content opacity to fully opaque.
     */
    public void resetMainContentOpacity() {
        setMainContentOpacity(1.0);
    }

    /**
     * Resets the curtain positions to their off-screen positions.
     */
    public void resetCurtainPositions() {
        if (curtainLeft != null) {
            curtainLeft.setTranslateX(-900);
        }
        if (curtainRight != null) {
            curtainRight.setTranslateX(900);
        }
    }

    /**
     * Schedules the confirmation dialog to appear after a 3-second delay.
     * This also hides the game over overlay and resets the main content opacity.
     */
    private void scheduleConfirmationDialog() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(_ -> {
            hideOverlay();
            resetMainContentOpacity();
            resetCurtainPositions();

            // Invoke the confirmation dialog callback
            if (onShowConfirmationDialog != null) {
                onShowConfirmationDialog.run();
            }
        });
        pause.play();
    }

    /**
     * Checks if the game over overlay is currently visible.
     *
     * @return true if the overlay is visible, false otherwise
     */
    public boolean isGameOverVisible() {
        return gameOverOverlay != null && gameOverOverlay.isVisible();
    }
}

