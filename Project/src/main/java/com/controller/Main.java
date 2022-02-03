package com.controller;

import com.model.engine.GameModel;
import com.model.features.Music;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Launches program and calls Menu FXML
 *
 * @author Zain Rashid-modified
 */
public class Main extends Application {

    private static Scene m_menuScene;

    /**
     * Getter for the Menu scene
     *
     * @return current Menu scene
     */
    public static Scene getMenuScene() {
        return m_menuScene;
    }

    /**
     * Launch program
     *
     * @param args array of Strings passed to Main when program is executed
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Call Menu FXML and pass control over to the MenuController
     *
     * @param primaryStage current view for FXML
     * @throws IOException if FXML fails to load
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Music media = Music.getInstance();
        media.createPlayer();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().
                getResource("Menu.fxml"));
        Parent root = loader.load();
        m_menuScene = new Scene(root);
        MenuController MenuController = loader.getController();
        MenuController.setStage(primaryStage);
        primaryStage.setTitle(GameModel.getGameName());
        primaryStage.setScene(m_menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}