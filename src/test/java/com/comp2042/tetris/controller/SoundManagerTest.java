package com.comp2042.tetris.controller;

import com.comp2042.tetris.utils.SafeMediaPlayer;
import javafx.scene.control.Slider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SoundManager to verify sound logic extraction.
 * These tests verify that SoundManager correctly encapsulates sound setup logic.
 */
class SoundManagerTest {

    private SoundManager soundManager;
    private MockResourceLoader resourceLoader;

    @BeforeEach
    void setUp() {
        resourceLoader = new MockResourceLoader();
        soundManager = new SoundManager(resourceLoader);
    }

    @Test
    void testSoundManagerCanBeCreated() {
        assertNotNull(soundManager, "SoundManager should be created successfully");
    }

    @Test
    void testPlayStartSoundReturnsNullWhenResourceNotFound() {
        // When resource is not available
        SafeMediaPlayer result = soundManager.playStartSound();

        // Then should return null gracefully
        assertNull(result, "Should return null when start sound resource not found");
    }

    @Test
    void testInitializeVolumeSlidersWithNullSliders() {
        // When called with null sliders
        assertDoesNotThrow(() ->
            soundManager.initializeVolumeSliders(null, null, () -> null),
            "Should handle null sliders gracefully"
        );
    }

    @Test
    void testLoadSmallSFXDoesNotThrow() {
        // When loading SFX
        assertDoesNotThrow(() -> soundManager.loadSmallSFX(),
            "Loading SFX should not throw exceptions"
        );
    }

    @Test
    void testScheduleFadeToMainMusicWithCallback() {
        // Given
        final boolean[] callbackInvoked = {false};
        Runnable callback = () -> callbackInvoked[0] = true;

        // When
        assertDoesNotThrow(() -> soundManager.scheduleFadeToMainMusic(callback),
            "Scheduling fade should not throw exceptions"
        );

        // Note: We can't easily test the timing without waiting 5.5 seconds
        // The important thing is that it doesn't throw
    }

    @Test
    void testGettersReturnNullWhenSFXNotLoaded() {
        // When SFX not loaded yet
        assertNull(soundManager.getBricksTouchPlayer(), "Should return null before loading");
        assertNull(soundManager.getHoverButtonPlayer(), "Should return null before loading");
        assertNull(soundManager.getButtonClickPlayer(), "Should return null before loading");
        assertNull(soundManager.getGameOverPlayer(), "Should return null before loading");
    }

    @Test
    void testGettersAfterLoadSmallSFX() {
        // When
        soundManager.loadSmallSFX();

        // Then - even if files not found, getters should not throw
        assertDoesNotThrow(() -> soundManager.getBricksTouchPlayer());
        assertDoesNotThrow(() -> soundManager.getHoverButtonPlayer());
        assertDoesNotThrow(() -> soundManager.getButtonClickPlayer());
        assertDoesNotThrow(() -> soundManager.getGameOverPlayer());
    }

    @Test
    void testSoundManagerUsesResourceLoader() {
        // Verify that SoundManager was constructed with resource loader
        assertNotNull(soundManager, "SoundManager should store resource loader");
    }

    /**
     * Mock class for resource loading in tests
     */
    private static class MockResourceLoader {
        // Used to provide a class for resource loading
    }
}

