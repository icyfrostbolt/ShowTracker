package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// This class represents a library, with a list of all the shows which are in it,
// and the total number of shows.

public class Library implements Writable {
    private ArrayList<Show> shows; // list of shows in the library
    private int showCount; // the number of shows in the library

    // EFFECTS: instantiates a library with 0 shows
    //          and an empty show list
    public Library() {
        this.shows = new ArrayList<>();
        this.showCount = 0;
    }

    // MODIFIES: this
    // EFFECTS: adds a show to the library and increases showCount by 1
    public void addShow(Show show) {
        this.shows.add(show);
        this.showCount++;
        EventLog.getInstance().logEvent(new Event("Show added"));
    }

    // MODIFIES: this
    // EFFECTS: removes a show from the library decreases showCount by 1
    public void removeShow(Show show) {
        this.shows.remove(show);
        this.showCount--;
        EventLog.getInstance().logEvent(new Event("Show removed"));
    }

    // EFFECTS: returns the number of shows in the library
    public int getLibrarySize() {
        return this.showCount;
    }

    // EFFECTS: returns a list of all the shows in the library
    public ArrayList<Show> getAllShows() {
        return this.shows;
    }

    // EFFECTS: returns the next show that is not watched
    public Show findNextUnwatchedShow() {
        for (Show show : this.shows) {
            if (!show.getWatched()) {
                return show;
            }
        }
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("shows", showsToJson());
        json.put("count", showCount);
        return json;
    }

    // EFFECTS: returns shows in this library as a JSON array
    private JSONArray showsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Show s : shows) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
