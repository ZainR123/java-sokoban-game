package com.model.engine;

import com.view.SettingsView;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Holds all functions which are used to populate the game grid
 * Converts game objects to a rectangle
 *
 * @author Zain Rashid-modified
 */
public class GraphicObject extends Rectangle {

    private static String m_imageWall, m_imageCrate, m_imageDiamond,
            m_imageKeeper, m_imageFloor, m_imageCrateOnDiamond;

    private static Image m_wall, m_crate, m_diamond, m_keeper, m_floor,
            m_crateOnDiamond;

    /**
     * Empty constructor used when parameters are not needed
     */
    public GraphicObject() {
    }

    /**
     * Sets look for grid object elements
     *
     * @param obj game object element
     */
    public GraphicObject(GameObject obj) {

        switch (obj) {
            case EMPTY -> this.setFill(Color.TAN);
            case WALL -> setWall();
            case CRATE -> setCrate();
            case DIAMOND -> setDiamond();
            case KEEPER -> this.setFill(new ImagePattern(m_keeper));
            case FLOOR -> setFloor();
            case CRATE_ON_DIAMOND -> setCrateOnDiamond();
            default -> {
                String message =
                        "Error in Level constructor. Object not recognized.";
                LoadGame.getLogger().severe(message);
                throw new AssertionError(message);
            }
        }
        this.setHeight(30);
        this.setWidth(30);

        if (obj == GameObject.DIAMOND || obj == GameObject.KEEPER) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }
        if (GameModel.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

    /**
     * Setter for the wall image
     *
     * @param imageWall overwrite current wall image
     */
    public static void setImageWall(String imageWall) {
        GraphicObject.m_imageWall = imageWall;
    }

    /**
     * Setter for the crate image
     *
     * @param imageCrate overwrite current crate image
     */
    public static void setImageCrate(String imageCrate) {
        GraphicObject.m_imageCrate = imageCrate;
    }

    /**
     * Setter for the diamond image
     *
     * @param imageDiamond overwrite current diamond image
     */
    public static void setImageDiamond(String imageDiamond) {
        GraphicObject.m_imageDiamond = imageDiamond;
    }

    /**
     * Setter for the crate on diamond image
     *
     * @param imageCrateOnDiamond overwrite current crate on diamond image
     */
    public static void setImageCrateOnDiamond(String imageCrateOnDiamond) {
        GraphicObject.m_imageCrateOnDiamond = imageCrateOnDiamond;
    }

    /**
     * Getter for the floor image
     *
     * @return current floor image
     */
    public static String getImageFloor() {
        return m_imageFloor;
    }

    /**
     * Setter for the floor image
     *
     * @param imageFloor overwrite current floor image
     */
    public static void setImageFloor(String imageFloor) {
        GraphicObject.m_imageFloor = imageFloor;
    }

    /**
     * Setter for the keeper image
     *
     * @param imageKeeper overwrite current keeper image
     */
    public static void setImageKeeper(String imageKeeper) {
        GraphicObject.m_imageKeeper = imageKeeper;
    }

    /**
     * Sets image for each object depending on what the current object images are
     */
    public static void createImage() {
        m_wall = new Image(Objects.requireNonNullElse
                (m_imageWall, "Black Wall.png"));
        m_crate = new Image(Objects.requireNonNullElse
                (m_imageCrate, "Black Crate.png"));
        m_diamond = new Image(Objects.requireNonNullElse
                (m_imageDiamond, "Black Diamond.png"));
        m_keeper = new Image(Objects.requireNonNullElse
                (m_imageKeeper, "stoppedBackward.png"));
        m_floor = new Image(Objects.requireNonNullElse
                (m_imageFloor, "Grey Floor.png"));
        m_crateOnDiamond = new Image(Objects.requireNonNullElse
                (m_imageCrateOnDiamond, "blackCrateDiamond.png"));
    }

    /**
     * Sets stopped animation image depending on the last key pressed by the user
     */
    public void setStoppedAnimation() {
        if (Movement.getM_counter() == 1) {
            m_imageKeeper = ("stoppedForward.png");
        }
        if (Movement.getM_counter() == 2) {
            m_imageKeeper = ("stoppedRight.png");
        }
        if (Movement.getM_counter() == 3) {
            m_imageKeeper = ("stoppedBackward.png");
        }
        if (Movement.getM_counter() == 4) {
            m_imageKeeper = ("stoppedLeft.png");
        }
        createImage();
    }

    /**
     * Sets moving animation image for moving forwards
     */
    public void setMovingAnimationUp() {
        if (!m_imageKeeper.equals("movingForward2.png")) {
            m_imageKeeper = "movingForward2.png";
        } else {
            m_imageKeeper = "movingForward.png";
        }
        createImage();
    }

    /**
     * Sets moving animation image for moving right
     */
    public void setMovingAnimationRight() {
        if (!m_imageKeeper.equals("movingRight.png")) {

            m_imageKeeper = "movingRight.png";
        } else {
            m_imageKeeper = "stoppedRight.png";
        }
        createImage();
    }

    /**
     * Sets moving animation image for moving backwards
     */
    public void setMovingAnimationDown() {
        if (!m_imageKeeper.equals
                ("movingBackward2.png")) {

            m_imageKeeper = "movingBackward2.png";
        } else {
            m_imageKeeper = "movingBackward.png";
        }
        createImage();
    }

    /**
     * Sets moving animation image for moving left
     */
    public void setMovingAnimationLeft() {
        if (!m_imageKeeper.equals("movingLeft.png")) {

            m_imageKeeper = "movingLeft.png";
        } else {
            m_imageKeeper = "stoppedLeft.png";
        }
        createImage();
    }

    /**
     * Sets wall to an image, colour or default color depending on what is last selected
     */
    public void setWall() {
        if (SettingsView.getGlobalWallCounter() == 0) {
            this.setFill(Color.BLACK);
        }
        if (SettingsView.getGlobalWallCounter() == 1) {
            this.setFill(SettingsView.getUserWallColor());
        }
        if (SettingsView.getGlobalWallCounter() == 2) {
            this.setFill(new ImagePattern(m_wall));
        }
    }

    /**
     * Sets crate to an image, colour or default color depending on what is last selected
     */
    public void setCrate() {
        if (SettingsView.getGlobalCrateCounter() == 0) {
            this.setFill(Color.ORANGE);
        }
        if (SettingsView.getGlobalCrateCounter() == 1) {
            this.setFill(SettingsView.getUserCrateColor());
        }
        if (SettingsView.getGlobalCrateCounter() == 2) {
            this.setFill(new ImagePattern(m_crate));
        }
    }

    /**
     * Sets diamond to an image, colour or default color depending on what is last selected
     */
    public void setDiamond() {
        if (SettingsView.getGlobalDiamondCounter() == 0) {
            this.setFill(Color.DEEPSKYBLUE);
        }
        if (SettingsView.getGlobalDiamondCounter() == 1) {
            this.setFill(SettingsView.getUserDiamondColor());
        }
        if (SettingsView.getGlobalDiamondCounter() == 2) {
            this.setFill(new ImagePattern(m_diamond));
        }
        if (GameModel.isDebugActive()) {
            fadeTransition();
        }
    }

    /**
     * Sets floor to an image, colour or default color depending on what is last selected
     */
    public void setFloor() {
        if (SettingsView.getGlobalFloorCounter() == 0) {
            this.setFill(Color.WHITE);
        }
        if (SettingsView.getGlobalFloorCounter() == 1) {
            this.setFill(SettingsView.getUserFloorColor());
        }
        if (SettingsView.getGlobalFloorCounter() == 2) {
            this.setFill(new ImagePattern(m_floor));
        }
    }

    /**
     * Sets crate on diamond to an image, colour or default color depending on what is last selected
     */
    public void setCrateOnDiamond() {
        if (SettingsView.getGlobalCrateDiamondCounter() == 0) {
            this.setFill(Color.DARKCYAN);
        }
        if (SettingsView.getGlobalCrateDiamondCounter() == 1) {
            this.setFill(SettingsView.getUserCrateDiamondColor());
        }
        if (SettingsView.getGlobalCrateDiamondCounter() == 2) {
            this.setFill(new ImagePattern(m_crateOnDiamond));
        }
    }

    /**
     * Sets fade transition when debug is active
     */
    public void fadeTransition() {
        FadeTransition ft = new
                FadeTransition(Duration.millis(1000), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.2);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }
}