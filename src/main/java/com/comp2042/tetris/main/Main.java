/**
 * Main
 * ----
 * The entry point of the JavaFX Tetris game.
 * Loads the game layout
 * launches the main game window
 */

package com.comp2042.tetris.main;

import com.comp2042.tetris.controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.tetris.controller.IGuiController;
import com.comp2042.tetris.controller.GuiController;
import com.comp2042.tetris.model.event.SimpleGameEventBus;
import com.comp2042.tetris.model.event.GameEventListener;


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

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 300, 510);
        primaryStage.setScene(scene);
        primaryStage.show();
        SimpleGameEventBus eventBus = new SimpleGameEventBus();
        IGuiController guiController = c ;
        if(guiController instanceof GameEventListener listener){
            eventBus.registerListener(listener);
        }
        else{
            throw new IllegalStateException("GuiController must implement GameEventListener");
        }
        GameController gameController = new GameController(eventBus);
        guiController.setGameController(gameController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
