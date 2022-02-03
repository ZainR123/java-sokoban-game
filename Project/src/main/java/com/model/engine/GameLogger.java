package com.model.engine;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Handles the game logging
 * The logfile is placed in the directory where the game is executed under a directory named "GAME_NAME"-logfiles
 *
 * @author Zain Rashid-modified
 */
public class GameLogger extends Logger {

    private static final Logger LOGGER = getLogger("GameLogger");
    private final DateFormat m_dateFormat = new SimpleDateFormat
            ("dd/MM/yyyy HH:mm:ss");
    private final Calendar m_calendar = Calendar.getInstance();

    /**
     * Writes log file and saves to log folder
     * @throws IOException if file can't be saved/ written to
     */
    public GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.getProperty
                ("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" +
                GameModel.getGameName() + ".log");
        LOGGER.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        fh.close();
    }

    /**
     * Returns String containing the current date and time, and the message
     *
     * @param message the message that should be appended to the String
     * @return String containing the current date and time, and the message
     */
    private String createFormattedMessage(String message) {
        return m_dateFormat.format(m_calendar.getTime()) + " -- " + message;
    }

    /**
     * {@inheritDoc}
     *
     * @param message the message that should be appended to the String
     */
    public void info(String message) {
        LOGGER.info(createFormattedMessage(message));
    }

    /**
     * {@inheritDoc}
     *
     * @param message the message that should be appended to the String
     */
    public void warning(String message) {
        LOGGER.warning(createFormattedMessage(message));
    }

    /**
     * {@inheritDoc}
     *
     * @param message the message that should be appended to the String
     */
    public void severe(String message) {
        LOGGER.severe(createFormattedMessage(message));
    }
}