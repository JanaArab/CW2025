package com.comp2042.tetris.controller.ui;

import com.comp2042.tetris.controller.core.InputHandler;
import com.comp2042.tetris.utils.AudioManager;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * InputController is a dedicated wrapper that centralizes all input handling logic,
 * including keyboard input, mouse hover, and click events.
 * This separates input concerns from the GuiController, making the code more maintainable
 * and testable.
 */
public class InputController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InputController.class);

    private final InputHandler inputHandler;
    private Button lastHoveredButton = null;

    /**
     * Creates a new InputController with the specified InputHandler.
     *
     * @param inputHandler the InputHandler to delegate key input to
     */
    public InputController(InputHandler inputHandler) {
        this.inputHandler = Objects.requireNonNull(inputHandler, "inputHandler cannot be null");
    }

    /**
     * Attaches key event listeners to the provided focusNode.
     * This method should be called once the scene is available.
     *
     * @param focusNode the node to attach keyboard listeners to
     */
    public void setupKeyboardInput(Node focusNode) {
        if (focusNode == null) {
            LOGGER.warn("Cannot setup keyboard input: focusNode is null");
            return;
        }

        focusNode.setFocusTraversable(true);
        focusNode.requestFocus();

        focusNode.setOnKeyPressed(this::handleKeyPressed);

        LOGGER.debug("Keyboard input setup completed for node: {}", focusNode.getClass().getSimpleName());
    }

    /**
     * Handles keyboard input events by delegating to InputHandler.
     * Special handling for rotation sound (KeyCode.R).
     *
     * @param keyEvent the keyboard event to handle
     */
    private void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent == null) {
            LOGGER.warn("Received null KeyEvent");
            return;
        }

        // Special case: play rotation sound for R key
        try {
            if (keyEvent.getCode() == KeyCode.R) {
                AudioManager.getInstance().playRotation();
                keyEvent.consume();
                return;
            }
        } catch (Throwable e) {
            LOGGER.warn("Failed to play rotation sound", e);
        }

        // Delegate to InputHandler
        if (inputHandler != null) {
            inputHandler.handle(keyEvent);
        }
    }

    /**
     * Sets up hover sound effects for buttons in the scene.
     * This method attaches mouse move event filters to play sounds when hovering over buttons.
     *
     * @param scene the scene to attach hover listeners to
     */
    public void setupHoverSounds(Scene scene) {
        if (scene == null) {
            LOGGER.warn("Cannot setup hover sounds: scene is null");
            return;
        }

        attachHoverHandler(scene);

        // Also directly attach hover handlers to all existing buttons
        if (scene.getRoot() != null) {
            scene.getRoot().lookupAll(".button").forEach(node -> {
                if (node instanceof Button button) {
                    button.setOnMouseEntered(e -> playHoverSound());
                }
            });
        }

        LOGGER.debug("Hover sounds setup completed");
    }

    /**
     * Attaches a scene-wide hover handler that detects button hovers.
     *
     * @param scene the scene to attach the handler to
     */
    private void attachHoverHandler(Scene scene) {
        Objects.requireNonNull(scene, "scene");

        scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_MOVED, e -> {
            Node picked = e.getPickResult().getIntersectedNode();
            Button btn = findButtonInHierarchy(picked);

            if (btn != null) {
                if (btn != lastHoveredButton) {
                    lastHoveredButton = btn;
                    playHoverSound();
                }
            } else {
                lastHoveredButton = null;
            }
        });
    }

    /**
     * Sets up click sound effects for buttons in the scene.
     * This method attaches mouse pressed event filters to play sounds when clicking buttons.
     *
     * @param scene the scene to attach click listeners to
     */
    public void setupClickSounds(Scene scene) {
        if (scene == null) {
            LOGGER.warn("Cannot setup click sounds: scene is null");
            return;
        }

        attachClickHandler(scene);

        // Also directly attach click handlers to all existing buttons
        if (scene.getRoot() != null) {
            scene.getRoot().lookupAll(".button").forEach(node -> {
                if (node instanceof Button button) {
                    button.setOnMousePressed(ev -> playButtonClickSound());
                }
            });
        }

        LOGGER.debug("Click sounds setup completed");
    }

    /**
     * Attaches a scene-wide click handler that detects button clicks.
     *
     * @param scene the scene to attach the handler to
     */
    private void attachClickHandler(Scene scene) {
        Objects.requireNonNull(scene, "scene");

        scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, e -> {
            Node picked = e.getPickResult().getIntersectedNode();
            Button btn = findButtonInHierarchy(picked);

            if (btn != null) {
                playButtonClickSound();
            }
        });
    }

    /**
     * Traverses the node hierarchy to find a Button ancestor.
     *
     * @param node the starting node
     * @return the Button if found, null otherwise
     */
    private Button findButtonInHierarchy(Node node) {
        Node current = node;
        while (current != null) {
            if (current instanceof Button) {
                return (Button) current;
            }
            current = current.getParent();
        }
        return null;
    }

    /**
     * Plays the button click sound effect.
     */
    private void playButtonClickSound() {
        try {
            AudioManager.getInstance().playClick();
        } catch (Throwable e) {
            LOGGER.debug("Failed to play click sound", e);
        }
    }

    /**
     * Plays the button hover sound effect.
     */
    private void playHoverSound() {
        try {
            AudioManager.getInstance().playHover();
        } catch (Throwable e) {
            LOGGER.debug("Failed to play hover sound", e);
        }
    }

    /**
     * Requests focus on a specific node.
     * Useful for ensuring keyboard input is captured.
     *
     * @param focusNode the node to focus
     */
    public void requestFocus(Node focusNode) {
        if (focusNode != null) {
            focusNode.requestFocus();
        }
    }

    /**
     * Gets the underlying InputHandler.
     *
     * @return the InputHandler instance
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }
}

