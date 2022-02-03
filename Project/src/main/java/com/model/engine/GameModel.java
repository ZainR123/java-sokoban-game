package com.model.engine;

import com.controller.Main;
import com.model.features.LeaderboardModel;
import com.model.features.Music;
import com.model.features.SaveGame;
import com.model.features.Timer;
import com.view.DialogView;
import com.view.SettingsView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * Controller for the Game FXML
 * Calls methods when user presses the corresponding buttons
 *
 * @author Zain Rashid
 */
public class GameModel {

    private static final String GAME_NAME = "Sokoban";
    private static boolean m_debug = false;
    private final Timer m_timer = new Timer(this);
    private Movement movement = new Movement(this);
    private Stage m_primaryStage;
    private int m_levelCounter = 1;
    private LoadGame m_loadGame;
    private Level m_currentLevel;
    private String m_previousLevelName;
    private final Stack<Point> m_undoStack = new Stack<>();
    private boolean m_reset = false, m_gameComplete = false, m_levelComplete =
            false;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Label moveCounter;
    @FXML
    private Label timer;
    @FXML
    private Label level;

    /**
     * Getter for the game name
     *
     * @return game name in String form
     */
    public static String getGameName() {
        return GAME_NAME;
    }

    /**
     * Getter for debug boolean
     *
     * @return true if debug active else false
     */
    public static boolean isDebugActive() {
        return m_debug;
    }

    /**
     * Setter for debug boolean
     *
     * @param m_debug overwrite current debug value
     */
    public static void setDebug(boolean m_debug) {
        GameModel.m_debug = m_debug;
    }

    public Stack<Point> getUndoStack() {
        return m_undoStack;
    }

    /**
     * Getter for reset level boolean
     *
     * @return true if reset level called else false
     */
    public boolean isReset() {
        return m_reset;
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
     * Setter for move counter label
     *
     * @param moveCounter overwrite current move count output to FXML
     */
    public void setMoveCounter(String moveCounter) {
        this.moveCounter.setText(moveCounter);
    }

    /**
     * Setter for timer label
     *
     * @param timer overwrite current timer output to FXML
     */
    public void setTimer(String timer) {
        this.timer.setText(timer);
    }

    /**
     * Getter for previous level name
     *
     * @return current previous level name in String form
     */
    public String getPreviousLevelName() {
        return m_previousLevelName;
    }

    /**
     * Getter for the current level
     *
     * @return current value for current level
     */
    public Level getCurrentLevel() {
        return m_currentLevel;
    }

    /**
     * Setter for the current level
     *
     * @param currentLevel overwrite current value for current level
     */
    public void setCurrentLevel(Level currentLevel) {
        this.m_currentLevel = currentLevel;
    }

    /**
     * Getter for level counter
     *
     * @return current level counter in Integer form
     */
    public int getLevelCounter() {
        return m_levelCounter;
    }

    /**
     * Setter for level counter
     *
     * @param m_levelCounter overwrite current value for level counter
     */
    public void setLevelCounter(int m_levelCounter) {
        this.m_levelCounter = m_levelCounter;
    }

    /**
     * Getter for whether game is complete
     *
     * @return true if game complete else false
     */
    public boolean isGameComplete() {
        return m_gameComplete;
    }

    /**
     * Getter for whether level is complete
     *
     * @return true if level complete else false
     */
    public boolean isLevelComplete() {
        return m_levelComplete;
    }

    /**
     * Setter for whether level is complete
     *
     * @param levelComplete overwrite current value for whether level is complete
     */
    public void setLevelComplete(boolean levelComplete) {
        this.m_levelComplete = levelComplete;
    }

    /**
     * Getter for load game function
     *
     * @param primaryStage current view for FXML
     * @return true if user successfully selects a map else returns false
     * @throws IOException when map is unable to be read
     */
    public boolean getLoadGame(Stage primaryStage) throws IOException {
        movement = new Movement(this);
        DialogView m_dialogView = new DialogView
                (m_primaryStage, this, m_timer);
        m_loadGame = new LoadGame(m_primaryStage, m_dialogView, m_timer,
                movement, this);
        return m_loadGame.loadGameFile(primaryStage);
    }

    /**
     * Getter for load default game save function
     *
     * @param primaryStage current view for FXML
     * @throws IOException when map is unable to be read
     */
    public void getLoadDefaultGame(Stage primaryStage) throws IOException {
        DialogView m_dialogView = new DialogView
                (m_primaryStage, this, m_timer);
        m_loadGame = new LoadGame(m_primaryStage, m_dialogView, m_timer,
                movement, this);
        m_loadGame.loadDefaultSaveFile(primaryStage);
    }


    /**
     * Gets next level if current level complete
     *
     * @return the next level loaded from level array
     */
    public Level getNextLevel() {
        if (m_currentLevel == null) {
            return LoadGame.getLevels().get(0);
        }

        int currentLevelIndex = m_currentLevel.getIndex();

        m_previousLevelName =
                LoadGame.getLevelNameList().get(currentLevelIndex - 1);
        if (currentLevelIndex < LoadGame.getLevels().size()) {
            return LoadGame.getLevels().get(currentLevelIndex);

        }
        m_gameComplete = true;
        return null;
    }

    /**
     * Toggles debug mode based on user input
     *
     * @throws IOException for reload grid function
     */
    public void toggleDebug() throws IOException {
        m_debug = !m_debug;
        reloadGrid();
    }

    /**
     * Setter for the floor colour behind objects on the grid
     */
    public void floorColor() {
        gameGrid.setStyle("-fx-background-color: White");

        if (SettingsView.getGlobalFloorCounter() == 1) {
            String webFormat = String.format("#%02x%02x%02x",
                    (int) (255 *
                            SettingsView.getUserFloorColor().getRed()),
                    (int) (255 *
                            SettingsView.getUserFloorColor().getGreen()),
                    (int) (255 *
                            SettingsView.getUserFloorColor().getBlue()));
            gameGrid.setStyle("-fx-background-color: " + webFormat + ";");
        }

        if (SettingsView.getGlobalFloorCounter() == 2) {
            switch (GraphicObject.getImageFloor()) {
                case "Green Floor.png" -> gameGrid.setStyle
                        ("-fx-background-color: #659F3E");
                case "Brown Floor.png" -> gameGrid.setStyle
                        ("-fx-background-color: #B7916A");
                case "Cream Floor.png" -> gameGrid.setStyle
                        ("-fx-background-color: #ECE3CE");
                case "Grey Floor.png" -> gameGrid.setStyle
                        ("-fx-background-color: #989AA7");
            }
        }
    }

    /**
     * Setter for level counter label which outputs to Game FXML
     */
    public void runLevelCounter() {
        level.setText("Level: " + m_levelCounter);
    }

    /**
     * Reload grid with use of the current level iterator
     *
     * @throws IOException when incorrect scoreboard file called
     */
    public void reloadGrid() throws IOException {
        DialogView m_dialogView = new DialogView
                (m_primaryStage, this, m_timer);

        if (Movement.isMoveComplete()) {
            movement.setMoveLabel();
        }

        if (isLevelComplete() && !isGameComplete()) {
            LeaderboardModel.calcTotalGameMoveCount(Movement.getMovesCount());
            m_dialogView.showLevelCompleteMessage();
            movement.resetMoveCounter();
            movement.setMoveLabel();
            m_levelComplete = false;
            m_levelCounter++;
        }

        if (isGameComplete()) {
            LeaderboardModel.calcTotalGameMoveCount(Movement.getMovesCount());
            m_dialogView.showVictoryMessage();
            m_timer.stopImageTimer();
            return;
        }

        Level currentLevel = getCurrentLevel();
        Level.LevelIterator levelGridIterator =
                (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        runLevelCounter();

        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(),
                    levelGridIterator.getCurrentPosition());
        }
        gameGrid.autosize();
        m_primaryStage.sizeToScene();
    }

    /**
     * Adds an object to the specified grid position.
     * Converts Game object into a rectangle adding it to the specified location
     *
     * @param gameObject game object to be added into the grid
     * @param location   location where the game object will be added
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        gameGrid.add(graphicObject, location.y, location.x);
    }

    /**
     * Reset current level by initializing game
     *
     * @throws IOException when map file can't be accessed
     */
    public void resetLevel() throws IOException {

        m_timer.stopTimer();
        m_reset = true;

        if (LoadGame.isDefaultGameRunning()) {
            InputStream in = getClass().getClassLoader().
                    getResourceAsStream("SampleGame.skb");
            m_loadGame.initializeGame(in);
        }
        if (LoadGame.isLoadGameRunning()) {
            m_loadGame.initializeGame
                    (new FileInputStream(m_loadGame.getSaveFile()));
        }
    }

    /**
     * Exit game
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Call save game function
     */
    public void callSaveGame() {
        SaveGame saveGame = new SaveGame(this, m_primaryStage,
                m_currentLevel);
        saveGame.saveGame();
    }

    /**
     * Call Menu scene
     */
    public void callStart() {
        m_timer.stopTimer();
        m_primaryStage.setScene(Main.getMenuScene());
    }

    /**
     * Call help dialog box
     */
    public void callShowHelpGame() {

        DialogView m_dialogView = new DialogView
                (m_primaryStage, this, m_timer);
        m_dialogView.showHelpGame();
    }

    /**
     * Call undo function
     */
    public void undo() throws IOException {
        if(m_undoStack.isEmpty()){
            return;
        }
        Point delta = m_undoStack.pop();
        Point reverseDelta = new Point((int)(delta.getX()-1), (int)(delta.getY()-1));

        Point keeperPosition = m_currentLevel.getKeeperPosition();
        GameObject keeper = m_currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = m_currentLevel.getObjectAt(targetObjectPoint);

        switch (keeperTarget) {
            case CRATE -> {
/*                GameObject crateTarget = m_currentLevel.getTargetObject(
                        targetObjectPoint, delta);*/
                m_currentLevel.moveGameObjectBy(keeper, keeperPosition, reverseDelta);
                m_currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint,
                        reverseDelta);
            }
            case WALL, FLOOR -> m_currentLevel.moveGameObjectBy(keeper, keeperPosition, reverseDelta);
            default -> {
                LoadGame.getLogger().severe("The object to be moved was not a recognised " +
                        "com.GameObject.");
                throw new AssertionError("This should not have happened. " +
                        "Report this problem to the developer.");
            }
        }
        keeperPosition.translate((int) reverseDelta.getX(), (int) reverseDelta.getY());
        Movement.setMoveCount(Movement.getMovesCount()-1);
        reloadGrid();
    }

    /**
     * Call Toggle Music function
     */
    public void callToggleMusic() {
        Music media = Music.getInstance();
        media.toggleMusic();
    }
}