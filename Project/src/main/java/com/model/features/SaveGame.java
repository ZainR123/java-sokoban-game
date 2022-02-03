package com.model.features;

import com.model.engine.GameModel;
import com.model.engine.Level;
import com.model.engine.LoadGame;
import com.model.engine.Movement;
import com.view.DialogView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

/**
 * Contains functions which save current game state
 *
 * @author Zain Rashid
 */
public class SaveGame {

    private final GameModel m_gameModel;
    private final Stage m_primaryStage;
    private final Level m_currentLevel;
    private StringBuilder m_savedGrid;

    /**
     * Save Game constructor gets instances for certain objects
     *
     * @param m_gameModel current GameModel instance
     * @param m_primaryStage   current stage view for FXML
     * @param m_currentLevel   current GameModel instance
     */
    public SaveGame(GameModel m_gameModel, Stage m_primaryStage,
                    Level m_currentLevel) {
        this.m_gameModel = m_gameModel;
        this.m_primaryStage = m_primaryStage;
        this.m_currentLevel = m_currentLevel;
    }

    /**
     * Save Current Game State and future levels to a file
     */
    public void saveGame() {
        try {
            storeGrid();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().add(new FileChooser.
                    ExtensionFilter
                    ("Sokoban save file", "*.skb"));
            fileChooser.setInitialDirectory(new File(System.
                    getProperty("user.dir") + "/src/main/resources/maps" +
                    "/mapSaves/"));
            fileChooser.setInitialFileName("save" + LoadGame.getMapSetName() +
                    m_gameModel.getLevelCounter() +
                    DialogView.getSaveUsername());
            File saveFile = fileChooser.showSaveDialog(m_primaryStage);

            if (saveFile != null) {
                FileWriter fw = new FileWriter(saveFile);
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write("SaveGame");
                bw.newLine();
                bw.write("Username: " + DialogView.getSaveUsername());
                bw.newLine();
                bw.write("Moves: " + Movement.getMovesCount());
                bw.newLine();
                bw.write("TotalM: " + LeaderboardModel.
                        getTotalGameMoveCount());
                bw.newLine();
                bw.write("Timer: " + Timer.getTimeCount());
                bw.newLine();
                bw.write("LevelIndex: " + m_gameModel.
                        getLevelCounter());
                bw.newLine();
                bw.write("MapSetName: " + LoadGame.getMapSetName());
                bw.newLine();
                bw.write("LevelName: " + m_currentLevel.getName());
                bw.newLine();
                bw.write(String.valueOf(m_savedGrid));
                bw.write(calculateRemainingMapSet());
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates remaining levels in game
     *
     * @return String of remaining levels
     */
    public String calculateRemainingMapSet() {
        final int removeLevelConstant = 43;
        int factor =
                (LoadGame.getTotalLevelCount() -
                        m_gameModel.getLevelCounter())
                        * removeLevelConstant;

        Collections.reverse(LoadGame.getGetGrid());
        LoadGame.getGetGrid().subList(factor,
                LoadGame.getGetGrid().size()).clear();
        Collections.reverse(LoadGame.getGetGrid());
        return String.join("", LoadGame.getGetGrid());
    }

    /**
     * Combines current object and diamonds grid to get current game state of level
     */
    public void storeGrid() {

        m_savedGrid = new StringBuilder();
        StringBuilder objectsGrid =
                new StringBuilder(m_currentLevel.toString());
        StringBuilder diamondsGrid =
                new StringBuilder(m_currentLevel.getDiamondsGrid().toString());

        for (int i = 0; i < objectsGrid.length(); i++) {
            if (diamondsGrid.charAt(i) == 'D' && objectsGrid.charAt(i) == 'C') {
                m_savedGrid.append('O');
            } else if (diamondsGrid.charAt(i) == '=') {
                m_savedGrid.append(objectsGrid.charAt(i));
            } else {
                m_savedGrid.append(diamondsGrid.charAt(i));
            }
        }
    }
}