package persistence;

import model.Episode;
import model.Library;
import model.Season;
import model.Show;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidName() {
        try {
            Library lib = new Library();
            JsonWriter writer = new JsonWriter("./data/my\0invalidName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLibrary() {
        try {
            Library lib = new Library();
            Show masked = new Show("Masked Singer", 0);
            Season season = new Season(1, 0);
            Episode episode = new Episode("Episode 1", 0);
            lib.addShow(masked);
            masked.addSeason(season);
            season.addEpisode(episode);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyLibrary.json");
            lib = reader.readLibrary();
            assertEquals(1, lib.getLibrarySize());
            checkLibrary(lib);
            checkShow(masked, "Masked Singer", 0);
            checkSeason(season, 1, 0);
            checkEpisode(episode, "Episode 1", 0);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterNewLibrary() {
        try {
            Library lib = new Library();
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralLibrary.json");
            writer.open();
            writer.write(lib);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralLibrary.json");
            lib = reader.readLibrary();
            assertEquals(0, lib.getLibrarySize());
            assertEquals(null, lib.findNextUnwatchedShow());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
