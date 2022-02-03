package com.model.features;

import com.model.engine.GameModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    void resetTimer() {
        GameModel gameModel = new GameModel();
        Timer timer = new Timer(gameModel);
        Timer.setTimeCount(10);
        timer.resetTimer();
        assertEquals(0,Timer.getTimeCount());
    }
}