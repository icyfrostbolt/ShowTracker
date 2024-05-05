package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class ShowTest {
    private Show survivor;
    private Season season1;
    private Episode episode1;
    private Episode episode2;
    private Episode episode3;

    @BeforeEach
    void initialize() {
        survivor = new Show("Survivor", 0);
        season1 = new Season(1, 0);
        episode1 = new Episode("Episode 1", 0);
        episode2 = new Episode("Episode 2", 0);
        episode3 = new Episode("Episode 3", 0);
    }

    @Test
    void testNameFunctions() {
        assertEquals(survivor.toString(), "Survivor");
        assertEquals(survivor.getShowName(), "Survivor");
        survivor.setShowName("New Name");
        assertEquals(survivor.getShowName(), "New Name");
        assertEquals(survivor.toString(), "New Name");
    }

    @Test
    void testFindNextUnwatchedSeason() {
        survivor.addSeason(season1);
        season1.addEpisode(episode1);
        season1.addEpisode(episode2);
        season1.addEpisode(episode3);
        assertEquals(survivor.findNextUnwatchedSeason(), season1);
        assertEquals(survivor.getTimesWatched(), 0);
        assertFalse(survivor.getWatched());
        season1.setTimesWatched(3);
        assertNull(survivor.findNextUnwatchedSeason());
        survivor.setTimesWatched(2);
        assertEquals(survivor.getTimesWatched(), 2);
        assertTrue(survivor.getWatched());
    }

    @Test
    void getAllSeasons() {
        assertEquals(survivor.getAllSeasons().size(), 0);
        survivor.addSeason(season1);
        assertEquals(survivor.getAllSeasons().size(), 1);
        survivor.removeSeason(season1);
        assertEquals(season1.getAllEpisodes().size(), 0);
    }
}