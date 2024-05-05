package persistence;

import model.Episode;
import model.Library;
import model.Season;
import model.Show;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkLibrary(Library library) {
        assertEquals(library.getAllShows().size(), library.getLibrarySize());
    }

    protected void checkShow(Show show, String name, int count) {
        assertEquals(name, show.getShowName());
        assertEquals(count, show.getTimesWatched());
    }

    protected void checkSeason(Season season, int number, int count) {
        assertEquals(number, season.getSeasonNumber());
        assertEquals(count, season.getTimesWatched());
    }

    protected void checkEpisode(Episode episode, String name, int count) {
        assertEquals(name, episode.getEpisodeName());
        assertEquals(count, episode.getTimesWatched());
    }
}
