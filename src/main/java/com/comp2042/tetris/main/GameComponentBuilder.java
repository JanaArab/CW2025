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


public class GameComponentBuilder {
    private Supplier<GameEventPublisher> eventBusSupplier = SimpleGameEventBus::new;
    private Function<GameEventPublisher, IGameController> controllerFactory = eventBus -> new GameController(() -> new SimpleBoard(23, 13), eventBus);
    public static GameComponentBuilder createDefault() {
        return new GameComponentBuilder();
    }
    public GameComponentBuilder withEventBusSupplier(Supplier<GameEventPublisher> supplier) {
        this.eventBusSupplier = Objects.requireNonNull(supplier, "supplier");
        return this;
    }
    public GameComponentBuilder withControllerFactory(Function<GameEventPublisher, IGameController> factory) {
        this.controllerFactory = Objects.requireNonNull(factory, "factory");
        return this;
    }
    public GameComponents build() {
        return build(null);
    }


    public GameComponents build(Consumer<GameEventPublisher> eventBusConfigurer) {
        GameEventPublisher eventBus = Objects.requireNonNull(eventBusSupplier.get(), "eventBus");
        if (eventBusConfigurer != null) {
            eventBusConfigurer.accept(eventBus);
        }
        IGameController gameController = Objects.requireNonNull(controllerFactory.apply(eventBus), "gameController");
        return new GameComponents(eventBus, gameController);
    }
    public record GameComponents(GameEventPublisher eventBus, IGameController gameController) {
    }
}



