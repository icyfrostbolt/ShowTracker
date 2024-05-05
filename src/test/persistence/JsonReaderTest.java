package persistence;

import model.Show;
import org.junit.jupiter.api.Test;

import model.Library;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderImaginaryFile() {
        JsonReader reader = new JsonReader("./data/notReal.json");
        try {
            Library lib = reader.readLibrary();
            fail("Did not get IOException");
        } catch (IOException e) {
        }
    }

    @Test
    void testReaderEmptyLibrary() {
        JsonReader reader = new JsonReader("./data/testEmptyLibrary.json");
        try {
            Library lib = reader.readLibrary();
            assertEquals(0, lib.getLibrarySize());
            assertEquals(null, lib.findNextUnwatchedShow());
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    void testReaderGeneralLibrary() {
        JsonReader reader = new JsonReader("./data/testGeneralLibrary.json");
        try {
            Library lib = reader.readLibrary();
            ArrayList<Show> shows = lib.getAllShows();
            assertEquals(2, shows.size());
            assertEquals(2, lib.getLibrarySize());
            assertEquals(lib.findNextUnwatchedShow().getShowName(), "Show 1");
            checkShow(shows.get(0), "Show 1", 0);
            checkShow(shows.get(1), "Show 2", 0);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
