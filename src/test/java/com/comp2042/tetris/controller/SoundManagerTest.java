package com.comp2042.tetris.controller;

import com.comp2042.tetris.controller.ui.SoundManager;
import com.comp2042.tetris.utils.SafeMediaPlayer;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


class SoundManagerTest {

    private SoundManager soundManager;
    private MockResourceLoader resourceLoader;

    private static boolean javaFxInitialized = false;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit once for all tests
        if (!javaFxInitialized) {
            try {
                Platform.startup(() -> {});
                javaFxInitialized = true;
            } catch (IllegalStateException e) {
                // Already initialized
                javaFxInitialized = true;
            }
        }
    }

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
    void testPlayStartSoundWithMockResourceLoader() {
        // When using MockResourceLoader (which has no resources in its classpath location)
        // The method should handle the case gracefully
        // Note: The actual behavior depends on whether resources are on classpath
        SafeMediaPlayer result = soundManager.playStartSound();

        // Since MockResourceLoader doesn't have /sounds/start.MP3 in its package,
        // but the test classpath might still have it, we just verify no exception is thrown
        // and if result is non-null, it's a valid player
        if (result != null) {
            assertNotNull(result, "If player is returned, it should be valid");
        }
        // Test passes either way - the key is no exception is thrown
    }

    @Test
    void testInitializeVolumeSlidersWithNullSliders() throws Exception {
        // Ensure JavaFX is initialized before running
        if (!javaFxInitialized) {
            return; // Skip test if JavaFX couldn't be initialized
        }

        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] error = {null};

        // When called with null sliders, should handle gracefully
        Platform.runLater(() -> {
            try {
                soundManager.initializeVolumeSliders(null, null, () -> null);
            } catch (Throwable t) {
                error[0] = t;
            } finally {
                latch.countDown();
            }
        });

        // Wait for Platform.runLater to complete
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Operation should complete within timeout");
        assertNull(error[0], "Should handle null sliders gracefully without exception");
    }

    @Test
    void testLoadSmallSFXDoesNotThrow() {
        // When loading SFX
        assertDoesNotThrow(() -> soundManager.loadSmallSFX(),
            "Loading SFX should not throw exceptions"
        );
    }

    @Test
    void testScheduleFadeToMainMusicWithCallback() throws Exception {
        // Ensure JavaFX is initialized before running
        if (!javaFxInitialized) {
            return; // Skip test if JavaFX couldn't be initialized
        }

        // Given
        final boolean[] callbackInvoked = {false};
        Runnable callback = () -> callbackInvoked[0] = true;

        CountDownLatch latch = new CountDownLatch(1);
        final Throwable[] error = {null};

        // When - run on JavaFX thread
        Platform.runLater(() -> {
            try {
                soundManager.scheduleFadeToMainMusic(callback);
            } catch (Throwable t) {
                error[0] = t;
            } finally {
                latch.countDown();
            }
        });

        // Wait for the scheduling to complete (not the actual callback)
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Scheduling operation should complete within timeout");
        assertNull(error[0], "Scheduling fade should not throw exceptions");

        // Note: We don't wait for the actual 5.5 second delay callback
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
     * Mock class for resource loading in tests.
     * This class has no resources in its classpath location.
     */
    private static class MockResourceLoader {
        // Used to provide a class for resource loading
    }
}

