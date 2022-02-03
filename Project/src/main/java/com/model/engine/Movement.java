package com.model.engine;

import com.controller.MenuController;
import com.model.features.Timer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.io.IOException;

/**
 * Holds all functions related to movement
 *
 * @author Zain Rashid
 */
public class Movement {

    private static int m_counter = 0, m_moveCount = 0;
    private static boolean m_moveComplete = false;
    private final GameModel m_gameModel;
    private Boolean keeperMoved = false;

    /**
     * Movement constructor gets current instances for certain objects
     *
     * @param m_gameModel current GameModel instance
     */
    public Movement(GameModel m_gameModel) {
        this.m_gameModel = m_gameModel;
    }

    /**
     * Getter for key pressed counter
     *
     * @return key pressed counter in Integer form
     */
    public static int getM_counter() {
        return m_counter;
    }

    /**
     * Getter for level moves
     *
     * @return current level moves in Integer form
     */
    public static int getMovesCount() {
        return m_moveCount;
    }

    /**
     * Setter for level move count
     *
     * @param m_moveCount overwrite current level move count
     */
    public static void setMoveCount(int m_moveCount) {
        Movement.m_moveCount = m_moveCount;
    }

    /**
     * States whether a move has been completed or not
     *
     * @return true if move complete else false
     */
    public static boolean isMoveComplete() {
        return m_moveComplete;
    }

    /**
     * Handles action that should be executed when a certain key is pressed
     *
     * @param code keyboard key code
     */
    public void handleKey(KeyCode code) {
        Timer timer = new Timer(m_gameModel);
        GraphicObject graphicObject = new GraphicObject();

        switch (code) {

            case UP, W -> {
                timer.resetImageTimer();
                m_counter = 1;
                graphicObject.setMovingAnimationUp();

                move(new Point(-1, 0));
            }

            case RIGHT, D -> {
                timer.resetImageTimer();
                m_counter = 2;
                graphicObject.setMovingAnimationRight();

                move(new Point(0, 1));
            }
            case DOWN, S -> {
                timer.resetImageTimer();
                m_counter = 3;
                graphicObject.setMovingAnimationDown();
                GraphicObject.createImage();
                move(new Point(1, 0));
            }
            case LEFT, A -> {
                timer.resetImageTimer();
                m_counter = 4;
                graphicObject.setMovingAnimationLeft();
                move(new Point(0, -1));
            }
        }

        if (GameModel.isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Handles movement of the keeper and objects that interact with it
     *
     * @param delta the movement delta
     */
    public void move(Point delta) {
        if (m_gameModel.isGameComplete()) {
            return;
        }

        Point keeperPosition = m_gameModel.getCurrentLevel().
                getKeeperPosition();
        GameObject keeper = m_gameModel.getCurrentLevel().
                getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition,
                delta);
        GameObject keeperTarget = m_gameModel.getCurrentLevel().
                getObjectAt(targetObjectPoint);

        if (GameModel.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(m_gameModel.getCurrentLevel().toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget,
                    targetObjectPoint);
        }

        keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = m_gameModel.getCurrentLevel().
                        getTargetObject
                                (targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                m_gameModel.getCurrentLevel().moveGameObjectBy
                        (keeperTarget, targetObjectPoint,
                                delta);
                m_gameModel.getCurrentLevel().moveGameObjectBy
                        (keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                m_gameModel.getCurrentLevel().moveGameObjectBy
                        (keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                LoadGame.getLogger().severe("The object to be " +
                        "moved was not a recognised GameObject.");
                throw new AssertionError
                        ("This should not have happened. " +
                                "Report this problem to the developer.");
        }
        ifKeeperMoves(delta, keeperPosition);
    }

    /**
     * When keeper has moved update movement and check if level has been completed
     *
     * @param delta          the movement delta
     * @param keeperPosition current keeper position on game grid
     */
    public void ifKeeperMoves(Point delta, Point keeperPosition) {
        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());

            m_moveComplete = true;
            m_moveCount++;
            if (m_gameModel.getCurrentLevel().isComplete()) {
                m_gameModel.setLevelComplete(true);
                if (GameModel.isDebugActive()) {
                    System.out.println("Level complete!");
                }
                m_gameModel.setCurrentLevel
                        (m_gameModel.getNextLevel());
                m_gameModel.setCurrentLevel
                        (m_gameModel.getCurrentLevel());
            }
        }
    }


    /**
     * Set move Counter for game FXML label
     */
    public void setMoveLabel() {
        m_gameModel.setMoveCounter("Moves: " + String.format("%03d",
                m_moveCount));
    }

    /**
     * Reset move counter
     */
    public void resetMoveCounter() {
        m_moveCount = 0;
    }

    /**
     * Adds event filter to handle key events passing them to my game scene.
     */
    public void setEventFilter() {
        EventHandler<KeyEvent> m_handler = event -> {
            handleKey(event.getCode());
            try {
                m_gameModel.reloadGrid();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        MenuController.getBoardScene().addEventFilter
                (KeyEvent.KEY_PRESSED, m_handler);
    }
}