package com.model.features;

import com.model.engine.GameModel;
import com.model.engine.LoadGame;
import com.model.engine.Movement;
import com.view.DialogView;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Model for Leaderboard view
 * Stores user results and calculates scoreboard when called
 *
 * @author Zain Rashid
 */
public class LeaderboardModel {

    private static final List<String> m_completeMessageArray =
            new ArrayList<>();
    private static int m_totalGameMoveCount;
    private final GameModel m_gameModel;
    private String m_compareArray, m_writeArray;
    private boolean m_topTen, m_highScore;

    /**
     * Leaderboard constructor to get current instances of certain objects
     *
     * @param m_gameModel current GameModel instance
     */
    public LeaderboardModel(GameModel m_gameModel) {
        this.m_gameModel = m_gameModel;
    }

    /**
     * Getter for total moves in the game so far
     *
     * @return current total move count in Integer form
     */
    public static int getTotalGameMoveCount() {
        return m_totalGameMoveCount;
    }

    /**
     * Setter for total moves in the game so far
     *
     * @param totalGameMoveCount overwrite current total move count
     */
    public static void setTotalGameMoveCount(int totalGameMoveCount) {
        LeaderboardModel.m_totalGameMoveCount = totalGameMoveCount;
    }

    /**
     * Calculate total game moves
     *
     * @param integer current level move count
     */
    public static void calcTotalGameMoveCount(Integer integer) {
        m_totalGameMoveCount = m_totalGameMoveCount + integer;
    }

    /**
     * Reset total game moves integer to zero
     */
    public static void clearTotalGameMoveCount() {
        m_totalGameMoveCount = 0;
    }

    /**
     * Getter for whether user is in the top ten scores
     *
     * @return true when user is in the top ten else false
     */
    public boolean isTopTen() {
        return m_topTen;
    }

    /**
     * Getter for whether user gets the high score
     *
     * @return true when user has the high score else false
     */
    public boolean isHighScore() {
        return m_highScore;
    }

    /**
     * Read scoreboard file and iterate through sorting values and adding to array list
     */
    public void readFileTimer() {

        m_completeMessageArray.clear();

        TreeMap<String, List<String>> highestScores =
                new TreeMap<>(Collections.reverseOrder());

        try (BufferedReader reader = new BufferedReader(new FileReader
                (System.getProperty("user.dir") +
                        "/src/main/resources/leaderboard/" +
                        LoadGame.getMapSetName() +
                        m_gameModel.getPreviousLevelName() + ".txt/"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] playerTime = line.split("-");
                String time = String.valueOf(playerTime[1]);
                List<String> playerList;
                if ((playerList = highestScores.get(time)) == null) {
                    playerList = new ArrayList<>(1);
                    playerList.add(playerTime[0]);
                    highestScores.put(time, playerList);
                } else {
                    playerList.add(playerTime[0]);
                }
            }
            for (String time : highestScores.descendingKeySet()) {
                for (String player : highestScores.get(time)) {
                    m_writeArray = player + "     " + time + "\n";
                    fillArray();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert scoreboard array list to single string
     *
     * @return scoreboard array list as string
     */
    public static String printArray() {
        return String.join("", m_completeMessageArray);
    }

    /**
     * Fills array list with scoreboard sorted values
     */
    public void fillArray() {
        if (m_completeMessageArray.size() == 0) {
            m_completeMessageArray.add("USERNAME                 " +
                    "TIME          LEVEL MOVES\n");
        }
        if (m_completeMessageArray.size() == 1 &&
                m_compareArray.equals(m_writeArray)) {
            m_highScore = true;
        }
        if (m_completeMessageArray.size() < 11) {
            m_completeMessageArray.add(m_writeArray);
            if (m_compareArray.equals(m_writeArray)) {
                m_topTen = true;
            }
        }
    }

    /**
     * Write user scores to file automatically
     *
     * @throws IOException if scoreboard file can't be written to
     */
    public void writeFile() throws IOException {
        File file = new File(System.getProperty("user.dir") +
                "/src/main/resources/leaderboard/" + LoadGame.getMapSetName() +
                m_gameModel.getPreviousLevelName() + ".txt/");
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        m_compareArray = String.format("%-20s", DialogView.getSaveUsername()) +
                "     " + Timer.getFormattedTime() + "     " + String.format
                ("%-3s", Movement.getMovesCount()) + "\n";

        bw.write(String.format("%-20s", DialogView.getSaveUsername()) + "-" +
                Timer.getFormattedTime() + "     " + String.format
                ("%-3s", Movement.getMovesCount()));
        bw.newLine();
        bw.close();
    }
}