package com.controller;

import com.model.engine.GameModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Controller for Menu FXML
 * Calls functions based on when the user presses a corresponding button
 *
 * @author Zain Rashid
 */
public class MenuController {

    private static Scene m_boardScene;
    private static Scene m_settingsScene;
    private Stage m_primaryStage;

    /**
     * Getter for Settings scene
     *
     * @return current Settings scene
     */
    public static Scene getSettingsScene() {
        return m_settingsScene;
    }

    /**
     * Getter for Board scene
     *
     * @return current Board scene
     */
    public static Scene getBoardScene() {
        return m_boardScene;
    }

    /**
     * Setter for primary stage
     *
     * @param primaryStage overwrite current stage view for FXML
     */
    public void setStage(Stage primaryStage) {
        this.m_primaryStage = primaryStage;
    }

    /**
     * Load Game FXML with default map when user requests the default game
     *
     * @throws Exception when the FXML and/or game file isn't found
     */
    public void newGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().
                getResource("Game.fxml"));
        Parent root = loader.load();
        m_boardScene = new Scene(root);
        GameModel gameModel = loader.getController();
        gameModel.setStage(m_primaryStage);
        gameModel.getLoadDefaultGame(m_primaryStage);
        m_primaryStage.setTitle(GameModel.getGameName());
        m_primaryStage.setScene(m_boardScene);
        m_primaryStage.setResizable(false);
    }

    /**
     * Load Game FXML with map choice when the user requests to load a game
     *
     * @throws Exception when the FXML and/or game file isn't found
     */
    public void loadGame() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().
                getResource("Game.fxml"));
        Parent root = loader.load();
        m_boardScene = new Scene(root);
        GameModel gameModel = loader.getController();
        gameModel.setStage(m_primaryStage);
        if (gameModel.getLoadGame(m_primaryStage)) {
            m_primaryStage.setTitle(GameModel.getGameName());
            m_primaryStage.setScene(m_boardScene);
            m_primaryStage.setResizable(false);
        }
    }

    /**
     * Load Settings FXML when the user requests to view the settings
     *
     * @throws Exception when FXML isn't found
     */
    public void settings() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().
                getResource("Settings.fxml"));
        Parent root = loader.load();
        m_settingsScene = new Scene(root);
        SettingsController settingsController = loader.getController();
        settingsController.setStage(m_primaryStage);
        m_primaryStage.setTitle(GameModel.getGameName());
        m_primaryStage.setScene(m_settingsScene);
        m_primaryStage.setResizable(false);
        m_primaryStage.show();
    }

    /**
     * Close program
     */
    public void closeMenu() {
        System.exit(0);
    }
}