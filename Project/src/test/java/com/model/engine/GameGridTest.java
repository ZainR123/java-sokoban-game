package com.model.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GameGridTest {

    private final int m_rows = 20;
    private final int m_columns = 20;
    private GameGrid m_gameGrid;
    private GameObject[][] m_gameObject;
    private Point source;
    private Point delta;

    @BeforeEach
    void setUp() {
        m_gameGrid = new GameGrid(m_columns, m_rows);
        m_gameObject = new GameObject[m_columns][m_rows];
    }

    @Test
    void translatePoint() {
        final int a = 1;
        final int b = 2;
        final int c = 3;
        source = new Point(a,b);
        delta = new Point(b,c);
        Point destination = new Point(a+b, b+c);
        assertEquals(destination, GameGrid.translatePoint(source, delta));
    }

    @Test
    void getTargetFromSource() {
        final int a = 1;
        final int b = 2;
        final int c = 3;
        source = new Point(a,b);
        delta = new Point(b,c);
        assertEquals(m_gameObject[a+b][a+c], m_gameGrid.getTargetFromSource
                (source, delta));
    }

    @Test
    void getGameObjectAt() {
        final int a = 19;
        final int b = 18;
        assertEquals(m_gameObject[a][b], m_gameGrid.getGameObjectAt(a, b));
    }

    @Test
    void testGetGameObjectAt() {
        final int a = 1;
        source = new Point(a,a);
        assertEquals(m_gameObject[a][a], m_gameGrid.getGameObjectAt(source));
    }

    @Test
    void testPutGameObjectAt() {
        final int rowNum = m_rows + 1;
        final int colNum = m_columns + 1;
        assertFalse(m_gameGrid.putGameObjectAt(GameObject.WALL, rowNum, colNum));
    }

    @Test
    void testPutGameObjectAt2() {
        final int rowNum = m_rows - 1;
        final int colNum = m_columns - 1;
        assertTrue(m_gameGrid.putGameObjectAt(GameObject.WALL, rowNum, colNum));
    }

    @Test
    public void testPutObjectAt3() {
        assertFalse(m_gameGrid.putGameObjectAt(GameObject.CRATE, m_rows,
                m_columns));
    }

    @Test
    public void testPutObjectAtGameObjectPoint() {
        final int a = 0;
        m_gameGrid.putGameObjectAt(GameObject.CRATE, a,a);
        assertSame(m_gameGrid.getGameObjectAt(a, a), GameObject.CRATE);
    }
}