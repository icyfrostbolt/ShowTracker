package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    private Library library;
    private Show survivor;
    private Show masterchef;

    @BeforeEach
    void initialize() {
        library = new Library();
        survivor = new Show("Survivor", 0);
        masterchef = new Show("Masterchef", 0);
    }

    @Test
    void testCheckEntireSeasonWatched() {
        assertEquals(library.getLibrarySize(), 0);
        library.addShow(survivor);
        assertEquals(library.getLibrarySize(), 1);
        library.addShow(masterchef);
        assertEquals(library.getLibrarySize(), 2);
        library.removeShow(survivor);
        assertEquals(library.getLibrarySize(), 1);
    }

    @Test
    void getAllShows() {
        assertEquals(library.getAllShows().size(), 0);
        library.addShow(survivor);
        assertEquals(library.getAllShows().size(), 1);
    }

    @Test
    void testUnwatchedFinder() {
        assertNull(library.findNextUnwatchedShow());
        library.addShow(survivor);
        library.addShow(masterchef);
        assertEquals(library.findNextUnwatchedShow(), survivor);
        survivor.setTimesWatched(1);
        assertEquals(library.findNextUnwatchedShow(), masterchef);
    }
}