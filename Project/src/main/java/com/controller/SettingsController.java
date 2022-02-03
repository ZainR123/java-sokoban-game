package com.controller;

import com.model.features.Music;
import com.view.DialogView;
import com.view.SettingsView;
import javafx.stage.Stage;

/**
 * Controller for Settings FXML
 * Calls functions based on the user pressing corresponding buttons
 *
 * @author Zain Rashid
 */
public class SettingsController {

    private Stage m_primaryStage;

    /**
     * Setter for primary stage
     *
     * @param primaryStage overwrite current stage view for FXML
     */
    public void setStage(Stage primaryStage) {
        this.m_primaryStage = primaryStage;
    }

    /**
     * Call Menu scene
     */
    public void callStart() {
        m_primaryStage.setScene(Main.getMenuScene());
    }

    /**
     * Call Toggle Music function
     */
    public void callToggleMusic() {
        Music media = Music.getInstance();
        media.toggleMusic();
    }

    /**
     * Call Help dialog box
     */
    public void callShowHelp() {
        DialogView help = new DialogView(m_primaryStage, null,
                null);
        help.showHelp();
    }

    /**
     * Call colour picker scene
     */
    public void callColourPicker() {
        SettingsView settingsView = new SettingsView(m_primaryStage);
        settingsView.colourPicker();
    }

    /**
     * Call image picker scene
     */
    public void callImagePicker() {
        SettingsView settingsView = new SettingsView(m_primaryStage);
        settingsView.imagePicker();
    }
}