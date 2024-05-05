package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpisodeTest {
    private Episode episode1;

    @BeforeEach
    void initialize() {
        episode1 = new Episode("Episode 1", 0);
    }

    @Test
    void testNameFunctions() {
        assertEquals(episode1.toString(), "Episode 1");
        assertEquals(episode1.getEpisodeName(), "Episode 1");
        episode1.setEpisodeName("New Name");
        assertEquals(episode1.getEpisodeName(), "New Name");
        assertEquals(episode1.toString(), "New Name");
    }

    @Test
    void testWatchStatus() {
        assertFalse(episode1.getWatched());
        assertEquals(episode1.getTimesWatched(), 0);
        episode1.setTimesWatched(1);
        assertTrue(episode1.getWatched());
        assertEquals(episode1.getTimesWatched(), 1);
    }

}