package com.model.engine;

import java.awt.*;
import java.util.Iterator;

/**
 * Used to create a 2D grid and add gameObjects to it
 *
 * @author Zain Rashid-modified
 */
public class GameGrid implements Iterable {

    private final int m_COLUMNS;
    private final int m_ROWS;
    private final GameObject[][] m_gameObjects;

    /**
     * Create grid using columns and rows to set the max size
     *
     * @param columns the number of columns
     * @param rows    the number of rows
     */
    public GameGrid(int columns, int rows) {
        m_COLUMNS = columns;
        m_ROWS = rows;

        m_gameObjects = new GameObject[m_COLUMNS][m_ROWS];
    }

    /**
     * Returns the point located at a distance delta from a starting GameObject
     *
     * @param sourceLocation the source point
     * @param delta          the distance between the reference GameObject and the target point
     * @return the target point at a distance delta from sourceLocation.
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }

    /**
     * Getter for gameGrid columns
     *
     * @return current columns
     */
    public int getCOLUMNS() {
        return m_COLUMNS;
    }

    /**
     * Getter for gameGrid rows
     *
     * @return current rows
     */
    public int getROWS() {
        return m_ROWS;
    }

    /**
     * Returns the size of this grid as a dimension.
     *
     * @return a dimension GameObject that indicates the size of this grid
     * @deprecated
     */
    @Deprecated
    public Dimension getDimension() {
        return new Dimension(m_COLUMNS, m_ROWS);
    }

    /**
     * Gets the GameObject at delta distance from the origin GameObject
     *
     * @param source the source GameObject location
     * @param delta  the distance from source
     * @return the target GameObject
     */
    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Gets the GameObject positioned at (x, y)
     *
     * @param col the row of the GameObject
     * @param row the column of the GameObject
     * @return GameObject
     * @throws ArrayIndexOutOfBoundsException if the coordinates are outside the grid bounds
     */
    public GameObject getGameObjectAt(int col, int row)
            throws ArrayIndexOutOfBoundsException {

        if (isPointOutOfBounds(col, row)) {
            if (GameModel.isDebugActive()) {
                System.out.printf("Trying to get null " +
                        "GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException
                    ("The point [" + col + ":" + row + "] is outside the map.");
        }

        return m_gameObjects[col][row];
    }

    /**
     * Gets a GameObject located at the chosen point
     *
     * @param p the point where the GameObject should be found
     * @return GameObject if the the GameObject is found else null if the point is null
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * Iterates over elements
     *
     * @return an Iterator
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }


    /**
     * Removes a game object from the grid
     *
     * @param position the position putting null in the GameObject position
     * @return boolean true if it was possible to remove the GameObject else false
     * @deprecated
     */
    @Deprecated
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * Puts a game object into the specified location (x, y)
     *
     * @param gameObject the gameObject to be put into the array
     * @param x          the x coordinate
     * @param y          the y coordinate
     * @return true if the operation is successful else false
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        m_gameObjects[x][y] = gameObject;
        return m_gameObjects[x][y] == gameObject;
    }

    /**
     * Puts a game object into the specified point
     *
     * @param gameObject the GameObject to be put into the array
     * @param p          the point where the GameObject will be put
     * @return true if the operation is successful else false
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject,
                (int) p.getX(), (int) p.getY());
    }

    /**
     * Checks if a point is outside the grid
     *
     * @param x the x coordinate point to be checked
     * @param y the y coordinate point to be checked
     * @return true if the x or y point is outside the grid else false
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= m_COLUMNS || y >= m_ROWS);
    }

    /**
     * Checks if a point is outside the grid
     *
     * @param p the point to be checked
     * @return true if the point is outside the grid else false
     * @deprecated
     */
    @Deprecated
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(m_gameObjects.length);

        for (GameObject[] gameObject : m_gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }


    /**
     * GridIterator iterates through the game grid containing game objects
     *
     * @see Iterator
     */
    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        /**
         * Whether to go to next grid line
         *
         * @return true if line iterated through else false
         */
        @Override
        public boolean hasNext() {
            return !(row == m_ROWS && column == m_COLUMNS);
        }

        /**
         * Go to next row in game grid
         *
         * @return next row
         */
        @Override
        public GameObject next() {
            if (column >= m_COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}