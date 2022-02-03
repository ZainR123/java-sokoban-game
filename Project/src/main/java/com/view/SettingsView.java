package com.view;

import com.model.engine.GameModel;
import com.controller.MenuController;
import com.model.engine.GraphicObject;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * View for Settings FXML
 * Sets scene when either imagePicker or colourPicker is requested
 *
 * @author Zain Rashid
 */
public class SettingsView {

    private static final Text m_wallStatus = new Text(), m_crateStatus =
            new Text(), m_diamondStatus = new Text(),
            m_floorStatus = new Text();
    private static int m_globalWallCounter = 0, m_globalCrateCounter = 0,
            m_globalDiamondCounter = 0, m_globalFloorCounter = 0,
            m_globalCrateDiamondCounter = 0;
    private static Color m_userWallColor = Color.BLACK, m_userCrateColor =
            Color.ORANGE, m_userFloorColor = Color.WHITE, m_userDiamondColor
            = Color.DARKCYAN, m_userCrateDiamondColor = Color.DEEPSKYBLUE;
    private final Stage m_primaryStage;

    /**
     * SettingsView constructor to get current instance of certain objects
     *
     * @param m_primaryStage current view for FXML
     */
    public SettingsView(Stage m_primaryStage) {
        this.m_primaryStage = m_primaryStage;
    }

    /**
     * Getter for user wall colour
     *
     * @return user wall colour
     */
    public static Color getUserWallColor() {
        return m_userWallColor;
    }

    /**
     * Getter for user crate colour
     *
     * @return user crate colour
     */
    public static Color getUserCrateColor() {
        return m_userCrateColor;
    }

    /**
     * Getter for user floor colour
     *
     * @return user floor colour
     */
    public static Color getUserFloorColor() {
        return m_userFloorColor;
    }

    /**
     * Getter for user diamond colour
     *
     * @return user diamond colour
     */
    public static Color getUserDiamondColor() {
        return m_userDiamondColor;
    }

    /**
     * Getter for user crate diamond colour
     *
     * @return user crate diamond colour
     */
    public static Color getUserCrateDiamondColor() {
        return m_userCrateDiamondColor;
    }

    /**
     * Getter for global wall counter
     *
     * @return global wall counter as an integer
     */
    public static int getGlobalWallCounter() {
        return m_globalWallCounter;
    }

    /**
     * Getter for global crate counter
     *
     * @return global crate counter as an integer
     */
    public static int getGlobalCrateCounter() {
        return m_globalCrateCounter;
    }

    /**
     * Getter for global diamond counter
     *
     * @return global diamond counter as an integer
     */
    public static int getGlobalDiamondCounter() {
        return m_globalDiamondCounter;
    }

    /**
     * Getter for global floor counter
     *
     * @return global floor counter as an integer
     */
    public static int getGlobalFloorCounter() {
        return m_globalFloorCounter;
    }

    /**
     * Getter for global crate diamond counter
     *
     * @return global crate diamond counter as an integer
     */
    public static int getGlobalCrateDiamondCounter() {
        return m_globalCrateDiamondCounter;
    }

    /**
     * Sets colour for game elements based on user input
     *
     * @param colorPicker colorPicker instance
     * @param count       counter decides whether image, colour or nothing has been selected
     */
    public void setCounter(ColorPicker colorPicker, int count) {

        switch (count) {
            case 0 -> {
                m_userWallColor = colorPicker.getValue();
                m_globalWallCounter = 1;
            }
            case 1 -> {
                m_userCrateColor = colorPicker.getValue();
                m_globalCrateCounter = 1;
            }
            case 2 -> {
                m_userFloorColor = colorPicker.getValue();
                m_globalFloorCounter = 1;
            }
            case 3 -> {
                m_userDiamondColor = colorPicker.getValue();
                m_globalDiamondCounter = 1;
            }
            case 4 -> {
                m_userCrateDiamondColor = colorPicker.getValue();
                m_globalCrateDiamondCounter = 1;
            }
        }
    }

    /**
     * Create colourPicker scene and output to user
     */
    public void colourPicker() {

        String[] colourTitles = {
                "Select Wall Colour", "Select Crate Colour",
                "Select Floor Colour", "Select Diamond Colour",
                "Select Crate On Diamond Colour"};

        Color[] userColours = {
                m_userWallColor, m_userCrateColor, m_userFloorColor,
                m_userDiamondColor, m_userCrateDiamondColor};

        Button exitButton = new Button("Go Back");
        exitButton.setOnAction(e -> m_primaryStage.setScene
                (MenuController.getSettingsScene()));

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(2);
        root.getStylesheets().add("style.css");
        root.getStyleClass().add("background");
        root.getStyleClass().add("subTitle");
        exitButton.getStyleClass().add("exitButton");

        for (int i = 0; i < userColours.length; i++) {
            int count = i;
            Text text = new Text(colourTitles[i]);
            ColorPicker colorPicker = new ColorPicker(userColours[i]);
            colorPicker.getStyleClass().add("colorPicker");
            colorPicker.setOnAction(e -> setCounter(colorPicker, count));
            root.getChildren().addAll(text, colorPicker);
        }

        root.getChildren().add(exitButton);

        Scene colourScene = new Scene(root, 500, 650);
        m_primaryStage.setTitle(GameModel.getGameName());
        m_primaryStage.setScene(colourScene);
        m_primaryStage.setResizable(false);
        m_primaryStage.show();
    }

    /**
     * Set images on buttons
     *
     * @param imageValue image string name
     * @return button with image
     */
    public Button setButtons(String imageValue) {
        Button button = new Button();
        ImageView image = new ImageView(new Image(imageValue));
        button.setGraphic(image);
        button.getStyleClass().add("images");
        return button;
    }

    /**
     * Set image values for game elements
     *
     * @param imageValue image string name
     * @param count      counter decides whether image, colour or nothing has been selected
     */
    public void setImageValue(String imageValue, int count) {

        switch (count) {
            case 1 -> {
                GraphicObject.setImageWall(imageValue);
                m_globalWallCounter = 2;
            }
            case 2 -> {
                String crateDiamondImageValue = null;
                GraphicObject.setImageCrate(imageValue);
                if (imageValue.equals("Black Crate.png")) {
                    crateDiamondImageValue = "blackCrateDiamond.png";
                }
                if (imageValue.equals("Brown Crate.png")) {
                    crateDiamondImageValue = "brownCrateDiamond.png";
                }
                if (imageValue.equals("Cream Crate.png")) {
                    crateDiamondImageValue = "creamCrateDiamond.png";
                }
                if (imageValue.equals("Grey Crate.png")) {
                    crateDiamondImageValue = "greyCrateDiamond.png";
                }
                GraphicObject.setImageCrateOnDiamond(crateDiamondImageValue);
                m_globalCrateCounter = 2;
                m_globalCrateDiamondCounter = 2;
            }
            case 3 -> {
                GraphicObject.setImageDiamond(imageValue);
                m_globalDiamondCounter = 2;
            }
            case 4 -> {
                GraphicObject.setImageFloor(imageValue);
                m_globalFloorCounter = 2;
            }
        }
    }

    /**
     * Forms imagePicker buttons with images and text based on what image has been selected
     *
     * @param array array for image names
     * @param text  set text for what image is currently selected
     * @param count counter decides whether an image, colour or nothing has been selected
     * @return HBox of buttons with images
     */
    public HBox createButtons(String[] array, Text text, int count) {

        HBox pane = new HBox();
        for (String images : array) {
            String imageValue = images + ".png";
            Button button = setButtons(imageValue);
            pane.getChildren().add(button);

            button.setOnAction(e -> {
                text.setText(images + " Selected");
                setImageValue(imageValue, count);
                GraphicObject.createImage();
            });
        }
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(5);
        return pane;
    }

    /**
     * Create imagePicker scene and output to user
     */
    public void imagePicker() {

        String[] namesWall = {
                "Black Wall", "Brown Wall", "Cream Wall", "Grey Wall"},
                namesCrate = {
                        "Black Crate", "Brown Crate", "Cream Crate",
                        "Grey Crate"},
                namesDiamond = {
                        "Black Diamond", "Brown Diamond", "Cream Diamond",
                        "Grey Diamond"},
                namesFloor = {
                        "Green Floor", "Brown Floor", "Cream Floor",
                        "Grey Floor"};

        Text selectWallTitle = new Text("Select Wall");
        Text selectCrateTitle = new Text("Select Crate");
        Text selectDiamondTitle = new Text("Select Diamond");
        Text selectFloorTitle = new Text("Select Floor");
        Button exitButton = new Button("Go Back");

        exitButton.setOnAction
                (e -> m_primaryStage.setScene
                        (MenuController.getSettingsScene()));

        VBox root = new VBox();

        root.getStylesheets().add("style.css");
        root.getStyleClass().add("subTitle");
        root.getStyleClass().add("background");
        root.setAlignment(Pos.CENTER);
        exitButton.getStyleClass().add("exitButton");

        root.getChildren().addAll
                (selectWallTitle, m_wallStatus,
                        createButtons(namesWall, m_wallStatus, 1),
                        selectCrateTitle, m_crateStatus,
                        createButtons(namesCrate, m_crateStatus, 2),
                        selectDiamondTitle, m_diamondStatus,
                        createButtons(namesDiamond, m_diamondStatus, 3),
                        selectFloorTitle, m_floorStatus, createButtons
                                (namesFloor, m_floorStatus, 4),
                        exitButton);

        Scene imageScene = new Scene(root, 500, 650);
        m_primaryStage.setTitle(GameModel.getGameName());
        m_primaryStage.setScene(imageScene);
        m_primaryStage.setResizable(false);
        m_primaryStage.show();
    }
}