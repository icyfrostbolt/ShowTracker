package persistence;

import model.Episode;
import model.Library;
import model.Season;
import model.Show;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads a library from a JSON file
public class JsonReader {
    private String file;

    // EFFECTS: constructs reader to read from JSON file
    public JsonReader(String file) {
        this.file = file;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws an IOException if there is an error from reading data from file
    public Library readLibrary() throws IOException {
        String jsonData = readFile(file);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLibrary(jsonObject);
    }

    // EFFECTS: reads the source file as string and then returns it
    private String readFile(String source) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> stringBuilder.append(s));
        }

        return stringBuilder.toString();
    }

    // EFFECTS: parses a library from the JSON object and then returns it
    private Library parseLibrary(JSONObject jsonObject) {
        Library lib = new Library();
        addShows(lib, jsonObject);
        return lib;
    }

    // MODIFIES: lib
    // EFFECTS: parses shows from JSON object and adds them to library
    private void addShows(Library lib, JSONObject jsonObject) {
        JSONArray showArray = jsonObject.getJSONArray("shows");
        for (Object show : showArray) {
            JSONObject nextShow = (JSONObject) show;
            addShow(lib, nextShow);
        }
    }

    // MODIFIES: lib
    // EFFECTS: parses a show from JSON object and adds it to the library
    private void addShow(Library lib, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        JSONArray seasonArray = jsonObject.getJSONArray("seasons");
        int times = jsonObject.getInt("times");
        Show show = new Show(name, times);
        for (Object season : seasonArray) {
            JSONObject nextSeason = (JSONObject) season;
            addSeason(show, nextSeason);
        }

        lib.addShow(show);
    }

    // MODIFIES: lib
    // EFFECTS: parses a season from JSON object and adds it to the show
    private void addSeason(Show show, JSONObject jsonObject) {
        int number = jsonObject.getInt("number");
        int times = jsonObject.getInt("times");
        Season season = new Season(number, times);
        JSONArray episodeArray = jsonObject.getJSONArray("episodes");
        for (Object episode : episodeArray) {
            JSONObject nextEpisode = (JSONObject) episode;
            addEpisode(season, nextEpisode);
        }
        show.addSeason(season);
    }

    // MODIFIES: lib
    // EFFECTS: parses an episode from JSON object and adds it to the season
    private void addEpisode(Season season, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int times = jsonObject.getInt("times");
        Episode episode = new Episode(name, times);
        season.addEpisode(episode);
    }
}
