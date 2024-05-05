package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class SeasonTest {
    private Season season1;
    private Episode episode1;
    private Episode episode2;
    private Episode episode3;

    @BeforeEach
    void initialize() {
        season1 = new Season(1, 0);
        episode1 = new Episode("Episode 1", 0);
        episode2 = new Episode("Episode 2", 0);
        episode3 = new Episode("Episode 3", 0);
    }

    @Test
    void testCheckEntireSeasonWatched() {
        assertEquals(season1.toString(), "Season 1");
        season1.addEpisode(episode1);
        season1.addEpisode(episode2);
        season1.addEpisode(episode3);
        assertFalse(season1.checkEntireSeasonWatched());
        episode1.setTimesWatched(1);
        episode2.setTimesWatched(1);
        episode3.setTimesWatched(1);
        assertTrue(season1.checkEntireSeasonWatched());
        assertEquals(season1.getTimesWatched(), 0);
        season1.setTimesWatched(1);
        assertEquals(season1.getTimesWatched(), 1);
        assertEquals(season1.toString(), "Season 1");
        season1.removeEpisode(episode1);
        assertEquals(season1.getAllEpisodes().size(), 2);
    }

    @Test
    void nextUnwatchedSeason() {
        assertNull(season1.findNextUnwatchedEpisode());
        season1.addEpisode(episode1);
        assertEquals(season1.findNextUnwatchedEpisode(), episode1);
        episode1.setTimesWatched(1);
        assertNull(season1.findNextUnwatchedEpisode());
    }

    @Test
    void getAllEpisodes() {
        assertEquals(season1.getSeasonNumber(), 1);
        assertEquals(season1.getAllEpisodes().size(), 0);
        season1.addEpisode(episode1);
        assertEquals(season1.getAllEpisodes().size(), 1);
        season1.setSeasonNumber(2);
        assertEquals(season1.getSeasonNumber(), 2);
    }

}