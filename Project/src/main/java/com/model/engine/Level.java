package com.model.engine;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static com.model.engine.GameGrid.translatePoint;

/**
 * Handles creation of game level taking in level name, level index and level grid
 * and putting it in a 2D array
 *
 * @author Zain Rashid-modified
 */
public final class Level implements Iterable<GameObject> {

    private final String m_name;
    private final GameGrid m_objectsGrid;
    private final GameGrid m_diamondsGrid;
    private final int m_index;
    private int m_numberOfDiamonds = 0;
    private Point m_keeperPosition = new Point(0, 0);

    /**
     * Create level using parameters
     *
     * @param levelName  the name of the level
     * @param levelIndex the number used as index for the levels
     * @param raw_level  the raw data of the level
     */
    public Level(String levelName, int levelIndex, List<String> raw_level) {
        if (GameModel.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n",
                    levelIndex, levelName);
        }

        m_name = levelName;
        m_index = levelIndex;

        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        m_objectsGrid = new GameGrid(rows, columns);
        m_diamondsGrid = new GameGrid(rows, columns);

        for (int row = 0; row < raw_level.size(); row++) {

            for (int col = 0; col < raw_level.get(row).length(); col++) {

                GameObject curTile = GameObject.fromChar
                        (raw_level.get(row).charAt(col));

                if (curTile == GameObject.DIAMOND) {
                    m_numberOfDiamonds++;
                    m_diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    m_keeperPosition = new Point(row, col);
                } else if (curTile == GameObject.CRATE_ON_DIAMOND) {
                    m_numberOfDiamonds++;
                    curTile = GameObject.DIAMOND;
                    m_diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.CRATE;
                }

                m_objectsGrid.putGameObjectAt(curTile, row, col);
            }
        }
        for (int row = 0; row < raw_level.size(); row++) {
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                if (countSurrounding(row, col)) {
                    m_objectsGrid.putGameObjectAt(GameObject.EMPTY, row, col);
                }
            }
        }
    }

    /**
     * Getter for objects grid
     *
     * @return current objects Grid in String form
     */
    @Override
    public String toString() {
        return m_objectsGrid.toString();
    }

    /**
     * Getter for diamonds grid
     *
     * @return current diamonds grid
     */
    public GameGrid getDiamondsGrid() {
        return m_diamondsGrid;
    }

    /**
     * Getter for current level name
     *
     * @return current level name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Getter for level index
     *
     * @return current level index
     */
    public int getIndex() {
        return m_index;
    }

    /**
     * Getter for keeper position
     *
     * @return current keeper position
     */
    public Point getKeeperPosition() {
        return m_keeperPosition;
    }

    /**
     * Getter for target object
     *
     * @param source the source point
     * @param delta  the distance from the source point
     * @return the object at distance delta from source
     */
    public GameObject getTargetObject(Point source, Point delta) {
        return m_objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Getter for object at point p of the objects grid.
     *
     * @param p the point where is object is located
     * @return GameObject the objected located at point p.
     */
    public GameObject getObjectAt(Point p) {
        return m_objectsGrid.getGameObjectAt(p);
    }

    /**
     * Getter for iterator over elements of.
     *
     * @return level iterator.
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * Returns whether iterated position is valid or not
     *
     * @param a rows
     * @param b columns
     * @return true if valid else false
     */
    boolean isValid(int a, int b) {
        return a >= 0 && a < m_objectsGrid.getROWS() && b >= 0 && b <
                m_objectsGrid.getCOLUMNS();
    }

    /**
     * Iterate through grid and set excess walls to blank spaces
     *
     * @param row x point on grid
     * @param col y point on grid
     * @return true if excess wall else false
     */
    public boolean countSurrounding(int row, int col) {
        boolean isEmpty = true;

        for (int a = row - 1; a < row + 2; a++) {

            for (int b = col - 1; b < col + 2; b++) {
                if (isValid(a, b) && (m_objectsGrid.getGameObjectAt(a, b) !=
                        GameObject.WALL
                        && m_objectsGrid.getGameObjectAt(a, b) !=
                        GameObject.EMPTY)) {
                    isEmpty = false;
                }

            }
        }
        return isEmpty;
    }

    /**
     * Iterates to see whether game complete
     *
     * @return true if all crates on diamonds else false
     */
    public boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < m_objectsGrid.getROWS(); row++) {
            for (int col = 0; col < m_objectsGrid.getCOLUMNS(); col++) {
                if (m_objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE
                        && m_diamondsGrid.getGameObjectAt(col, row) ==
                        GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= m_numberOfDiamonds;
    }


    public void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * Moves game object to target destination
     * Removes object from original position and places it into the new one
     *
     * @param object      - the game object to be moved
     * @param source      - the position of the object to be moved
     * @param destination - the destination of the game object
     */
    public void moveGameObjectTo(GameObject object, Point source,
                                 Point destination) {
        m_objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        m_objectsGrid.putGameObjectAt(object, destination);
    }

    /**
     * LevelIterator iterates through the game grid
     *
     * @see Iterator
     */
    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        /**
         * Getter for game grid position
         *
         * @return current game grid position
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }

        /**
         * Whether to go to next grid line
         *
         * @return true if line iterated through else false
         */
        @Override
        public boolean hasNext() {
            return !(row == m_objectsGrid.getROWS() - 1 && column ==
                    m_objectsGrid.getCOLUMNS());
        }

        /**
         * Go to next row in game grid
         *
         * @return next row
         */
        @Override
        public GameObject next() {
            if (column >= m_objectsGrid.getCOLUMNS()) {
                column = 0;
                row++;
            }

            GameObject object = m_objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = m_diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                }
            }
            return retObj;
        }
    }
}