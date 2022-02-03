package com.model.features;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * Holds all Music related functions
 *
 * @author Zain Rashid
 */
public class Music {

    private static Music m_singleton;
    private MediaPlayer m_player;
    private boolean m_mediaPlaying = false;

    /**
     * Transforms Music class into singleton
     *
     * @return singleton instance
     */
    public static Music getInstance() {
        if (m_singleton == null) {
            m_singleton = new Music();
        }
        return m_singleton;
    }

    /**
     * play Music player
     */
    public void playMusic() {
        m_player.play();
    }

    /**
     * pause Music player
     */
    public void pauseMusic() {
        m_player.pause();
    }

    /**
     * See if music is playing or not
     *
     * @return true if music playing else false;
     */
    public boolean isPlayingMusic() {
        return m_player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Toggles music player, pausing and resuming music
     */
    public void toggleMusic() {
        if (!isPlayingMusic()) {
            playMusic();
        } else {
            pauseMusic();
        }
    }

    /**
     * Create Music player
     */
    public void createPlayer() {
        if (!m_mediaPlaying) {
            m_mediaPlaying = true;
            Media song = new Media(Objects.requireNonNull(getClass().
                    getClassLoader().getResource("puzzle_theme.wav"))
                    .toString());
            m_player = new MediaPlayer(song);
            m_player.setCycleCount(MediaPlayer.INDEFINITE);
            m_player.setVolume(0.25);
            playMusic();
        }
    }
}