package com.model.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    private GameObject[][] m_gameObject;
    private Level level;
    private Point source;
    private final int a = 1;
    private final int b = 2;

    @BeforeEach
    void setUp() {
        List<String> raw_level = new ArrayList<>();

        raw_level.add("WWWWWWWWWWWWWWWWWWWW");
        raw_level.add("W    W             W");
        raw_level.add("W C  W D           W");
        raw_level.add("W    W      WWWWWWWW");
        raw_level.add("W    WWWW  WWWWWWWWW");
        raw_level.add("W            WWWWWWW");
        raw_level.add("W    WWWWW   WWWWWWW");
        raw_level.add("W    WWWWWWWWWWWWWWW");
        raw_level.add("W    WWWWWWWWWWWWWWW");
        raw_level.add("W    WWWWWWWWWWWWWWW");
        raw_level.add("W    WWWWWWWWWWWWWWW");
        raw_level.add("W           WWWWWWWW");
        raw_level.add("W       W WWWWWWWWWW");
        raw_level.add("WWWWWWW W WWWWWWWWWW");
        raw_level.add("WWWWWWW W WWWWWWWWWW");
        raw_level.add("WWWWWWW W WWWWWWWWWW");
        raw_level.add("WWWWWWW W WWWWWWWWWW");
        raw_level.add("WWWWWWW W WWWWWWWWWW");
        raw_level.add("WWWWWWW   SWWWWWWWWW");
        raw_level.add("WWWWWWWWWWWWWWWWWWWW");

        int m_columns = 20;
        int m_rows = 20;
        int levelIndex = 1;
        String levelName = "levelName";
        level = new Level(levelName, levelIndex, raw_level);
        m_gameObject = new GameObject[m_columns][m_rows];
    }

    @Test
    void getTargetObject() {
        source = new Point(a,b);
        int c = 3;
        Point delta = new Point(b,c);
        assertNotNull(level.getTargetObject(source,delta));
    }

    @Test
    void getObjectAt() {
        source = new Point(a,b);
        assertNotNull(level.getObjectAt(source));
    }

    @Test
    void isValid() {
        assertTrue(level.isValid(a,b));
    }

    @Test
    void countSurrounding() {
        assertFalse(level.countSurrounding(a,b));
    }

    @Test
    void isComplete() {
        assertFalse(level.isComplete());

    }
}