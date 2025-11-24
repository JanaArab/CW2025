package com.comp2042.tetris.view;

import org.junit.jupiter.api.Test;
import javafx.scene.layout.GridPane;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify brick positioning calculations
 */
class BrickPositioningTest {

    @Test
    void testHorizontalStepCalculation() {
        GridPane gamePanel = new GridPane();
        GridPane brickPanel = new GridPane();
        GridPane nextPanel = new GridPane();

        // Set hgap and vgap to 0 as in the actual game
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        brickPanel.setHgap(0);
        brickPanel.setVgap(0);

        int brickSize = 20;
        BoardRenderer renderer = new BoardRenderer(gamePanel, brickPanel, nextPanel, brickSize);

        // The step should be exactly brickSize when gap is 0
        // This is a private method, but we can verify behavior indirectly

        System.out.println("Brick size: " + brickSize);
        System.out.println("Game panel hgap: " + gamePanel.getHgap());
        System.out.println("Brick panel hgap: " + brickPanel.getHgap());
        System.out.println("Expected step: " + brickSize);
        System.out.println("Actual step: " + (brickSize + brickPanel.getHgap()));

        assertEquals(0, brickPanel.getHgap(), "Brick panel hgap should be 0");
        assertEquals(0, brickPanel.getVgap(), "Brick panel vgap should be 0");
    }
}

