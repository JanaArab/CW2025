package com.comp2042.tetris.model.event;

import java.util.Objects;

/**
 * Singleton provider for the game event bus.
 * Provides global access to the event publisher for components
 * that cannot receive it through dependency injection.
 *
 * <p>Must be initialized once at application startup before use.</p>
 *
 * @see GameEventPublisher
 */
public class GameEventBusProvider {

    private static GameEventPublisher eventPublisher;

    private GameEventBusProvider() {
        // Utility class - prevent instantiation
    }

    /**
     * Initializes the provider with an event publisher.
     * Can only be called once during application startup.
     *
     * @param publisher the event publisher instance to provide
     * @throws NullPointerException if publisher is null
     * @throws IllegalStateException if already initialized
     */
    public static synchronized void initialize(GameEventPublisher publisher) {
        Objects.requireNonNull(publisher, "publisher");
        if (eventPublisher != null) {
            throw new IllegalStateException("GameEventBusProvider has already been initialized");
        }
        eventPublisher = publisher;
    }

    /**
     * Gets the event bus instance.
     *
     * @return the game event publisher
     * @throws IllegalStateException if not yet initialized
     */
    public static GameEventPublisher getEventBus() {
        GameEventPublisher current = eventPublisher;
        if (current == null) {
            throw new IllegalStateException("GameEventBusProvider has not been initialized");
        }
        return current;
    }
}
