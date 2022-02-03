package com.model.features;

import com.model.engine.GameModel;
import com.model.engine.GraphicObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds all Timer related functions
 *
 * @author Zain Rashid
 */
public class Timer {

    private static int m_timeCount, m_imageTimeCount;
    private static Timeline m_timeline, m_timelineMove;
    private static String m_formattedTime;
    private final GameModel m_gameModel;

    /**
     * Timer constructor gets current instances of certain objects
     *
     * @param m_gameModel current instance of GameModel
     */
    public Timer(GameModel m_gameModel) {
        this.m_gameModel = m_gameModel;
    }

    /**
     * Getter for current time in milliseconds
     *
     * @return time as Integer
     */
    public static int getTimeCount() {
        return m_timeCount;
    }

    /**
     * Setter for time
     *
     * @param m_timeCount overwrite current time count
     */
    public static void setTimeCount(int m_timeCount) {
        Timer.m_timeCount = m_timeCount;
    }

    /**
     * Getter for formatted time
     *
     * @return current formatted time
     */
    public static String getFormattedTime() {
        return m_formattedTime;
    }

    /**
     * Creates timer for when to reset sprite animation image
     */
    public void imageTimer() {
        GraphicObject graphicObject = new GraphicObject();
        m_timelineMove = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
            m_imageTimeCount++;
            if (m_imageTimeCount >= 1 && !m_gameModel.isGameComplete()) {
                graphicObject.setStoppedAnimation();
                try {
                    m_gameModel.reloadGrid();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        m_timelineMove.setCycleCount(Animation.INDEFINITE);
        m_timelineMove.play();
    }

    /**
     * Create game timer for playing through a level
     */
    public void createTimer() {
        m_timeline = new Timeline(new KeyFrame(Duration.millis(1), evt -> {
            m_timeCount++;
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");
            m_formattedTime = sdf.format(new Date(m_timeCount));
            m_gameModel.setTimer("Timer: " + m_formattedTime);
        }));
        m_timeline.setCycleCount(Animation.INDEFINITE);
        playTimer();
    }

    /**
     * Pause game timer
     */
    public void pauseTimer() {
        m_timeline.pause();
    }

    /**
     * Stop and reset game timer
     */
    public void stopTimer() {
        m_timeline.stop();
        m_timeline.getKeyFrames().clear();
        m_timeline = null;
        m_timeCount = 0;
    }

    /**
     * Stop and reset sprite animation timer
     */
    public void stopImageTimer() {
        m_timelineMove.stop();
        m_timelineMove.getKeyFrames().clear();
        m_timelineMove = null;
        m_imageTimeCount = 0;
    }

    /**
     * Play game timer
     */
    public void playTimer() {
        m_timeline.play();
    }

    /**
     * Reset game timer
     */
    public void resetTimer() {
        m_timeCount = 0;
    }

    /**
     * Reset image timer
     */
    public void resetImageTimer() {
        m_imageTimeCount = 0;
    }
}