package com.comp2042.tetris.main;

import com.comp2042.tetris.controller.core.GameController;
import com.comp2042.tetris.controller.core.IGameController;
import com.comp2042.tetris.model.board.SimpleBoard;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.SimpleGameEventBus;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;

/**
 * Builder class for constructing game components.
 * Uses the Builder pattern to create and configure the game's
 * event bus and controller with customizable dependencies.
 *
 * <p>Example usage:</p>
 * <pre>
 * GameComponents components = GameComponentBuilder.createDefault()
 *     .build(eventBus -> eventBus.registerListener(myListener));
 * </pre>
 *
 * @see GameComponents
 * @see GameEventPublisher
 * @see IGameController
 */
public class GameComponentBuilder {
    private Supplier<GameEventPublisher> eventBusSupplier = SimpleGameEventBus::new;
    private Function<GameEventPublisher, IGameController> controllerFactory = eventBus -> new GameController(() -> new SimpleBoard(23, 13), eventBus);

    /**
     * Creates a new builder with default configuration.
     *
     * @return a new GameComponentBuilder instance
     */
    public static GameComponentBuilder createDefault() {
        return new GameComponentBuilder();
    }

    /**
     * Sets a custom event bus supplier.
     *
     * @param supplier the supplier for creating the event bus
     * @return this builder for method chaining
     */
    public GameComponentBuilder withEventBusSupplier(Supplier<GameEventPublisher> supplier) {
        this.eventBusSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }

    /**
     * Sets a custom controller factory.
     *
     * @param factory the factory function for creating the game controller
     * @return this builder for method chaining
     */
    public GameComponentBuilder withControllerFactory(Function<GameEventPublisher, IGameController> factory) {
        this.controllerFactory = Objects.requireNonNull(factory, "factory");
        return this;
    }

    /**
     * Builds the game components with default configuration.
     *
     * @return the constructed GameComponents
     */
    public GameComponents build() {
        return build(null);
    }

    /**
     * Builds the game components with optional event bus configuration.
     *
     * @param eventBusConfigurer optional consumer to configure the event bus (e.g., register listeners)
     * @return the constructed GameComponents
     */
    public GameComponents build(Consumer<GameEventPublisher> eventBusConfigurer) {
        GameEventPublisher eventBus = Objects.requireNonNull(eventBusSupplier.get(), "eventBus");
        if (eventBusConfigurer != null) {
            eventBusConfigurer.accept(eventBus);
        }
        IGameController gameController = Objects.requireNonNull(controllerFactory.apply(eventBus), "gameController");
        return new GameComponents(eventBus, gameController);
    }

    /**
     * Container record for the game's core components.
     *
     * @param eventBus the game event publisher
     * @param gameController the main game controller
     */
    public record GameComponents(GameEventPublisher eventBus, IGameController gameController) {
    }
}
