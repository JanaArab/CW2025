package com.comp2042.tetris.model.event;

import java.util.Objects;

public class GameEventBusProvider {

    private static GameEventPublisher eventPublisher;

    private GameEventBusProvider() {

    }
    public static synchronized void initialize(GameEventPublisher publisher) {
        Objects.requireNonNull(publisher, "publisher");
        if (eventPublisher != null) {
            throw new IllegalStateException("GameEventBusProvider has already been initialized");
        }
        eventPublisher = publisher;
    }
    public static GameEventPublisher getEventBus() {
        GameEventPublisher current = eventPublisher;
        if (current == null) {
            throw new IllegalStateException("GameEventBusProvider has not been initialized");
        }
        return current;
    }
}
