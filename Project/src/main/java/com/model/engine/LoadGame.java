package com.model.engine;

import com.model.features.LeaderboardModel;
import com.model.features.Timer;
import com.view.DialogView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Holds all methods which load an initial game
 *
 * @author Zain Rashid
 */
public class LoadGame {

    private static final ArrayList<String> m_levelNameList = new ArrayList<>();
    private static final List<String> m_getGrid = new ArrayList<>();
    private static String m_mapSetName;
    private static boolean m_loadGameRunning;
    private static List<Level> m_levels;
    private static GameLogger m_logger;
    private static boolean m_defaultGameRunning;
    private static int totalLevelCount;
    private final DialogView m_dialogView;
    private final Movement m_movement;
    private final Timer m_timer;
    private final GameModel m_gameModel;
    private File m_saveFile;
    private Stage m_primaryStage;

    /**
     * LoadGame constructor gets current instances of certain objects
     *
     * @param m_primaryStage   current view stage of FXML
     * @param m_dialogView     current instance of dialogView object
     * @param m_timer          current instance of Timer object
     * @param m_movement       current instance of Movement object
     * @param m_gameModel current instance of GameModel object
     */
    public LoadGame(Stage m_primaryStage, DialogView m_dialogView, Timer m_timer
            , Movement m_movement, GameModel m_gameModel) {
        this.m_primaryStage = m_primaryStage;
        this.m_dialogView = m_dialogView;
        this.m_timer = m_timer;
        this.m_movement = m_movement;
        this.m_gameModel = m_gameModel;
    }

    /**
     * Getter for the total number of levels
     *
     * @return current total number of levels in Integer form
     */
    public static int getTotalLevelCount() {
        return totalLevelCount;
    }

    /**
     * Getter for the map set name
     *
     * @return current map set name in String form
     */
    public static String getMapSetName() {
        return m_mapSetName;
    }

    /**
     * Getter for level name String array list
     *
     * @return current level name String array list
     */
    public static ArrayList<String> getLevelNameList() {
        return m_levelNameList;
    }

    /**
     * Getter for whether load game function is running or not
     *
     * @return true when load game is running else false
     */
    public static boolean isLoadGameRunning() {
        return m_loadGameRunning;
    }

    /**
     * Getter for whether load default game function is running or not
     *
     * @return true when load default game is running else false
     */
    public static boolean isDefaultGameRunning() {
        return m_defaultGameRunning;
    }

    /**
     * Getter for level objects grid array list
     *
     * @return current level objects grid list
     */
    public static List<Level> getLevels() {
        return m_levels;
    }

    /**
     * Getter for logger object
     *
     * @return current GameLogger instance
     */
    public static GameLogger getLogger() {
        return m_logger;
    }

    /**
     * Getter for level diamond + object grid array list
     *
     * @return current grid array list as a List of Strings
     */
    public static List<String> getGetGrid() {
        return m_getGrid;
    }

    /**
     * Getter for save file
     *
     * @return current saved file
     */
    public File getSaveFile() {
        return m_saveFile;
    }

    /**
     * Read through map file and assign values to game variables depending on certain lines
     *
     * @param input map file directory
     * @return array list of all the levels in the game
     */
    public List<Level> loadGameFile(InputStream input) {
        m_levelNameList.clear();
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new
                InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, ++levelIndex,
                                rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (!m_gameModel.isReset() && line.startsWith("W")) {
                    m_getGrid.add(line);
                    m_getGrid.add("\n");
                }

                readSaveGame(line);

                if (line.contains("MapSetName")) {
                    m_mapSetName = line.replace
                            ("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (!m_gameModel.isReset()) {
                        totalLevelCount++;
                        m_getGrid.add("\n");
                        m_getGrid.add(line);
                        m_getGrid.add("\n");
                    }
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(levelName, ++levelIndex,
                                rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }
                    levelName = line.replace("LevelName: ", "");
                    m_levelNameList.add(levelName);
                    continue;
                }
                line = line.trim();
                line = line.toUpperCase();

                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }
        } catch (IOException e) {
            m_logger.severe("Error trying to load the game " +
                    "file: " + e);
        } catch (NullPointerException e) {
            m_logger.severe("Cannot open the requested file:" +
                    " " + e);
        }
        return levels;
    }

    /**
     * Cases for when a save game is read through the file reader
     *
     * @param line current String line being iterated through from map set file
     */
    public void readSaveGame(String line) {
        if (line.contains("Username")) {
            DialogView.setSaveUsername(line.replace("Username: ",
                    ""));
        }

        if (line.contains("Moves")) {
            Movement.setMoveCount(Integer.parseInt(line.replace(
                    "Moves: ", "")));
        }

        if (line.contains("TotalM")) {
            LeaderboardModel.setTotalGameMoveCount(Integer.parseInt(line
                    .replace("TotalM" + ": ", "")));
        }

        if (line.contains("Timer")) {
            Timer.setTimeCount(Integer.parseInt(line.replace
                    ("Timer: ", "")));
        }

        if (line.contains("LevelIndex")) {
            m_gameModel.setLevelCounter(Integer.parseInt
                    (line.replace("LevelIndex: ", "")));
        }
    }

    /**
     * Opens file chooser and allows user to pick a map to play and initialize game
     *
     * @param primaryStage current stage view for FXML
     * @return true if map successfully selected else false
     * @throws IOException if map couldn't be opened successfully
     */
    public boolean loadGameFile(Stage primaryStage) throws IOException {
        ifSave = false;
        this.m_primaryStage = primaryStage;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
                ("Sokoban save file", "*.skb"));
        fileChooser.setInitialDirectory(new File(System.
                getProperty("user.dir") + "/src/main/resources/maps/"));
        m_saveFile = fileChooser.showOpenDialog(m_primaryStage);

        if (m_saveFile != null) {
            if (GameModel.isDebugActive()) {
                m_logger.info("Loading save file: " + m_saveFile.getName());
            }
            if (m_saveFile.getName().contains("save")) {
                ifSave = true;
            }
            initializeGame(new FileInputStream(m_saveFile));
            if (!m_saveFile.getName().contains("save")) {
                m_dialogView.showUsername();
            }
            m_loadGameRunning = true;
            return true;
        } else {
            return false;
        }
    }
    private boolean ifSave = false;

    /**
     * Runs default game with default map set and initializes game
     *
     * @param primaryStage current stage view for FXML
     * @throws IOException if map couldn't be opened
     */
    public void loadDefaultSaveFile(Stage primaryStage) throws IOException {
        this.m_primaryStage = primaryStage;
        InputStream in = getClass().
                getClassLoader().getResourceAsStream("SampleGame.skb");
        initializeGame(in);
        m_dialogView.showUsername();
        m_defaultGameRunning = true;
    }

    /**
     * Starts game and other game functions
     * Sets current level and reloads game grid
     *
     * @param input map set name
     * @throws IOException if map couldn't be opened
     */
    public void initializeGame(InputStream input) throws IOException {
        try {
            m_logger = new GameLogger();
            m_movement.resetMoveCounter();
            m_movement.setMoveLabel();
            m_timer.imageTimer();
            m_timer.createTimer();
            m_gameModel.runLevelCounter();
            m_levels = loadGameFile(input);

            if (!m_gameModel.isReset()) {
                m_movement.setEventFilter();
                m_gameModel.setCurrentLevel
                            (m_gameModel.getNextLevel());

                GameModel.setDebug(false);
            }
            if (!(ifSave || m_gameModel.isReset())) {
                LeaderboardModel.clearTotalGameMoveCount();
            }
            if (ifSave && m_gameModel.isReset()) {
                m_gameModel.setCurrentLevel(m_levels.get
                        (0));
            }
            if(m_gameModel.isReset() && !ifSave) {
                m_gameModel.setCurrentLevel(m_levels.get
                        (m_gameModel.getLevelCounter() - 1));
            }

        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            m_logger.warning("Cannot load the default save file: " +
                    Arrays.toString(e.getStackTrace()));
        }
        GraphicObject.setImageKeeper("stoppedBackward.png");
        GraphicObject.createImage();
        m_gameModel.floorColor();
        m_gameModel.reloadGrid();
    }
}