package com.view;

import com.model.engine.GameModel;
import com.controller.Main;
import com.controller.MenuController;
import com.model.engine.LoadGame;
import com.model.engine.Movement;
import com.model.features.LeaderboardModel;
import com.model.features.Timer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Forms all dialog boxes for game and shows them when requested
 *
 * @author Zain Rashid
 */
public class DialogView {

    private static String m_saveUsername;
    private final Stage m_primaryStage;
    private final GameModel m_gameModel;
    private final Timer m_timer;
    private final LeaderboardModel m_leaderboardModel;
    private boolean m_gameComplete, m_levelComplete, m_gameHelp, m_menuHelp,
            m_username;
    private VBox m_dialogVBox;
    private Stage m_dialogStage;

    /**
     * DialogView constructor to get current instances of certain objects
     *
     * @param m_primaryStage   current view of FXML
     * @param m_gameModel instance of GameModel object
     * @param m_timer          instance of Timer object
     */
    public DialogView(Stage m_primaryStage, GameModel m_gameModel,
                      Timer m_timer) {
        this.m_primaryStage = m_primaryStage;
        this.m_gameModel = m_gameModel;
        this.m_timer = m_timer;
        m_leaderboardModel = new LeaderboardModel(m_gameModel);
    }

    /**
     * Getter for username
     *
     * @return current username in String form
     */
    public static String getSaveUsername() {
        return m_saveUsername;
    }

    /**
     * Setter for username
     *
     * @param m_saveUsername overwrite current username
     */
    public static void setSaveUsername(String m_saveUsername) {
        DialogView.m_saveUsername = m_saveUsername;
    }

    /**
     * Set username dialog box
     * Restrict user inputs
     */
    public void usernameScreen() {
        TextField username = new TextField();
        Button startGame = new Button("Start Game");
        startGame.getStyleClass().add("playButton");
        m_dialogVBox.getChildren().addAll(username, startGame);
        createBackButton();

        username.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("^[0-9a-zA-Z\\\\ ]*$")) {
                username.setText(oldValue);
            }
            if (newValue.trim().length() > 20) {
                username.setText(oldValue);
            }
        });

        startGame.disableProperty().bind(
                Bindings.createBooleanBinding(() -> username.getText().trim().
                        isEmpty(), username.textProperty())
        );

        startGame.setOnAction(e -> {
            m_saveUsername = username.getText().trim();
            m_dialogStage.close();
            m_primaryStage.setScene(MenuController.getBoardScene());
            m_timer.stopTimer();
            m_primaryStage.show();
            m_timer.createTimer();
        });
    }

    /**
     * Set help dialog box in game
     */
    public void gameHelp() {
        Button backGame = new Button("Back To Game");
        backGame.getStyleClass().add("playButton");
        m_dialogVBox.getChildren().add(backGame);

        backGame.setOnAction(e -> {
            m_dialogStage.close();
            m_primaryStage.show();
            m_timer.playTimer();
        });
    }

    /**
     * Set help dialog box in menu
     */
    public void menuHelp() {
        Button backSettings = new Button("Back To Settings");
        backSettings.getStyleClass().add("exitButton");
        m_dialogVBox.getChildren().add(backSettings);

        backSettings.setOnAction(e -> {
            m_dialogStage.close();
            m_primaryStage.setScene(MenuController.getSettingsScene());
            m_primaryStage.show();
        });
    }

    /**
     * Set level complete dialog box after a level is finished
     */
    public void levelComplete() {
        Button nextLevel = new Button("Next Level");
        nextLevel.getStyleClass().add("playButton");
        m_dialogVBox.getChildren().add(nextLevel);
        createLeaderboardButton();
        createBackButton();

        nextLevel.setOnAction(e -> {
            m_primaryStage.setScene(MenuController.getBoardScene());
            m_timer.resetTimer();
            m_dialogStage.close();
            m_primaryStage.show();
        });
    }

    /**
     * Set game complete dialog box after a map set is finished
     */
    public void gameComplete() {
        createLeaderboardButton();
        createBackButton();
    }

    /**
     * Create button to go back to the Menu scene
     */
    public void createBackButton() {
        Button m_backMenu = new Button("Back To Menu");
        m_backMenu.getStyleClass().add("exitButton");
        m_dialogVBox.getChildren().add(m_backMenu);
        m_backMenu.setOnAction(e -> {
            m_dialogStage.close();
            m_timer.stopTimer();
            m_primaryStage.setScene(Main.getMenuScene());
            m_primaryStage.show();
        });
    }

    /**
     * Create button to enter the leaderboard screen
     */
    public void createLeaderboardButton() {
        LeaderboardView leaderboardView =
                new LeaderboardView(m_dialogStage, m_gameModel);
        Button m_leaderboardButton = new Button("Level Leaderboard");
        m_leaderboardButton.getStyleClass().add("leaderboardButton");
        m_dialogVBox.getChildren().add(m_leaderboardButton);
        m_leaderboardButton.setOnAction(e -> {
            m_dialogStage.close();
            leaderboardView.leaderboardDialog();
        });
    }

    /**
     * Sets images for help dialog boxes
     *
     * @param imageValue image name
     * @return Label with image
     */
    public Label setImage(String imageValue) {
        Label label = new Label();
        ImageView image = new ImageView(new Image(imageValue));
        label.setGraphic(image);
        label.getStyleClass().add("images");
        return label;
    }

    /**
     * Create and set Labels for help dialog boxes
     *
     * @param array String array containing image names
     * @return HBox with Labels
     */
    public HBox createLabels(String[] array) {
        HBox pane = new HBox();
        for (String images : array) {
            String imageValue = images + ".png";
            Label label = setImage(imageValue);
            pane.getChildren().add(label);
        }
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);
        return pane;
    }

    /**
     * Sets styling for dialog box VBox and text
     *
     * @param vbox  Dialog box
     * @param text  Dialog box title
     * @param text1 Dialog box body text
     */
    public void setDialogBox(VBox vbox, Text text, Text text1) {
        text.getStyleClass().add("subTitle");
        text.setTextAlignment(TextAlignment.CENTER);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStylesheets().add("style.css");
        vbox.getStyleClass().add("background");
        vbox.getStyleClass().add("dialogMessage");
        vbox.getChildren().addAll(text, text1);
    }

    /**
     * Create dialog box shells
     *
     * @param dialogTitle         Dialog box title
     * @param dialogMessage       Dialog box body text
     * @param dialogMessageEffect Dialog box text effects
     */
    public void createDialog(String dialogTitle, String dialogMessage,
                             Effect dialogMessageEffect) {

        String[] imageNames = {"Brown Crate", "stoppedBackward"},
                imageNames2 = {"Brown Wall"},
                imageNames3 = {"Brown Crate", "Brown Diamond"};

        m_primaryStage.hide();
        m_dialogStage = new Stage();
        m_dialogStage.setResizable(false);
        m_dialogStage.setTitle(GameModel.getGameName());

        Text text = new Text(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }
        m_dialogVBox = new VBox();

        setDialogBox(m_dialogVBox, text, text1);

        if (m_menuHelp || m_gameHelp) {

            Text text2 = new Text("-Only one box can be pushed at a time.\n "
                    + "-A box cannot be pulled. ");
            Text text3 = new Text("-The player " +
                    "cannot walk through boxes or walls.");
            Text text4 = new Text("-The puzzle is " +
                    "solved when all boxes are on the goals.");

            text2.setTextAlignment(TextAlignment.CENTER);
            text3.setTextAlignment(TextAlignment.CENTER);
            text4.setTextAlignment(TextAlignment.CENTER);

            m_dialogVBox.getChildren().addAll(createLabels(imageNames), text2,
                    createLabels(imageNames2), text3, createLabels(imageNames3),
                    text4);
        }

        if (m_levelComplete) {
            levelComplete();
        }

        if (m_gameComplete) {
            gameComplete();
        }

        if (m_gameHelp) {
            gameHelp();
        }

        if (m_menuHelp) {
            menuHelp();
        }

        if (m_username) {
            usernameScreen();
        }

        Scene dialogScene = new Scene(m_dialogVBox, 500, 650);
        m_dialogStage.setScene(dialogScene);
        m_dialogStage.show();
    }

    /**
     * Outputs username dialog box
     */
    public void showUsername() {
        m_username = true;
        String string = "Enter Username";
        String message = "-Only letters and numbers may be entered";
        MotionBlur mb = new MotionBlur(2, 1);
        createDialog(string, message, mb);
    }

    /**
     * Outputs game complete dialog box
     *
     * @throws IOException if scoreboard file isn't accessible
     */
    public void showVictoryMessage() throws IOException {
        m_leaderboardModel.writeFile();
        m_leaderboardModel.readFileTimer();
        m_gameComplete = true;
        String string = "Game Complete!";
        MotionBlur mb = new MotionBlur(2, 1);
        createDialog(string, completeMessage(), mb);
    }

    /**
     * Outputs level complete dialog box
     *
     * @throws IOException if scoreboard file isn't accessible
     */
    public void showLevelCompleteMessage() throws IOException {
        m_leaderboardModel.writeFile();
        m_leaderboardModel.readFileTimer();
        m_levelComplete = true;
        String string = "Level " + m_gameModel.getLevelCounter() +
                " Complete!";
        MotionBlur mb = new MotionBlur(2, 1);
        createDialog(string, completeMessage(), mb);
    }

    /**
     * Return dialog box body for when a level or game is complete depending on what score the user got
     *
     * @return dialog body in String form
     */
    public String completeMessage() {
        if (m_leaderboardModel.isHighScore()) {
            if (m_gameModel.getLevelCounter() == 1) {
                return "Well done you got the high score for this level!\n" +
                        outputResults();
            } else {
                return "Well done you got the high score for this level!\n" +
                        outputResults() + "\n Total Game Moves: " +
                        LeaderboardModel.getTotalGameMoveCount();
            }
        }
        if (m_leaderboardModel.isTopTen() && !m_leaderboardModel.isHighScore()) {
            if (m_gameModel.getLevelCounter() == 1) {
                return "Well done you are in the top 10 scores!\n" +
                        outputResults();
            } else {
                return "Well done you are in the top 10 scores!\n" +
                        outputResults() + "\n Total Game Moves: " +
                        LeaderboardModel.getTotalGameMoveCount();
            }
        } else {
            if (m_gameModel.getLevelCounter() == 1) {
                return "Sorry you aren't in the top 10 scores better luck next "
                        + "time!\n" + outputResults();
            } else {
                return "Sorry you aren't in the top 10 scores better luck next "
                        + "time!\n" + outputResults() + "\n Total Game Moves: "
                        + LeaderboardModel.getTotalGameMoveCount();
            }
        }
    }

    /**
     * Return generic body string for game or level complete
     *
     * @return dialog body in String form
     */
    public String outputResults() {
        return "Username: " + m_saveUsername +
                "\n Map Name: " + LoadGame.getMapSetName()
                + "\n " + "Level Name: " +
                m_gameModel.getPreviousLevelName()
                + "\n Time: " + Timer.getFormattedTime() +
                "\n Total Level Moves: " + Movement.getMovesCount();
    }

    /**
     * Output help dialog box in game
     */
    public void showHelpGame() {
        m_gameHelp = true;
        m_timer.pauseTimer();
        createDialog("Help", helpMessage(), null);
    }

    /**
     * Output help dialog box in menu
     */
    public void showHelp() {
        m_menuHelp = true;
        createDialog("Help", helpMessage(), null);
    }

    /**
     * Return help dialog box body
     *
     * @return help body in String form
     */
    public String helpMessage() {
        return "The goal of the game is to push all of the boxes onto " +
                "the goals.\n" + " Listed below are the specific rules " +
                "to solve a puzzle:\n" +
                "\n";
    }
}