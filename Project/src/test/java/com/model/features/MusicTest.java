package com.model.features;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicTest {

    private Music music;

    @BeforeEach
    void setUp() {
         music = Music.getInstance();
    }

    @Test
    void getInstance() {
        assertNotNull(music);
    }

    @Test
    void getInstance2() {
        assertSame(music,Music.getInstance());
    }
}