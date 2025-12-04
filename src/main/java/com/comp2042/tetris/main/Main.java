
package com.comp2042.tetris.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.tetris.controller.GuiController;
import com.comp2042.tetris.controller.DefaultGuiControllerDependenciesFactory;
import com.comp2042.tetris.model.event.GameEventBusProvider;
import com.comp2042.tetris.model.event.GameEventListener;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.view.UIConstants;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        DefaultGuiControllerDependenciesFactory dependenciesFactory = new DefaultGuiControllerDependenciesFactory();
        c.setDependencies(dependenciesFactory.create(c));

        GameComponentBuilder builder = GameComponentBuilder.createDefault();
        GameComponentBuilder.GameComponents components;

        if (c instanceof GameEventListener listener) {
            components = builder.build(eventBus -> eventBus.registerListener(listener));
        } else {
            throw new IllegalStateException("GuiController must implement GameEventListener");
        }
        GameEventPublisher eventBus = components.eventBus();
        GameEventBusProvider.initialize(eventBus);
        c.setGameController(components.gameController());

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setResizable(false);

        Scene scene = new Scene(root, UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
