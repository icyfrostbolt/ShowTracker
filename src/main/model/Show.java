package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// This class represents a show with a name, a counter of the number of times it has
// been watched, and a list of all the seasons that are a part of the show.

public class Show implements Writable {
    private String showName; // the name of the show
    private int timesWatched; // the number of times the show has been watched
    private ArrayList<Season> seasons; // list of seasons in the show

    // EFFECTS: instantiates a show with a name,
    //          no times watched, and no seasons
    public Show(String name, int timesWatched) {
        this.showName = name;
        this.timesWatched = timesWatched;
        this.seasons = new ArrayList<>();
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String newName) {
        this.showName = newName;
    }

    // MODIFIES: this
    // EFFECTS: adds a season to the show
    public void addSeason(Season season) {
        this.seasons.add(season);
        EventLog.getInstance().logEvent(new Event("Season added"));
    }

    // MODIFIES: this
    // EFFECTS: removes a season from the show
    public void removeSeason(Season season) {
        this.seasons.remove(season);
        EventLog.getInstance().logEvent(new Event("Season removed"));
    }

    // EFFECTS: returns the next season that is not watched
    public Season findNextUnwatchedSeason() {
        for (Season season : this.seasons) {
            if (!season.getWatched()) {
                return season;
            }
        }
        return null;
    }

    // EFFECTS: returns a list of all the seasons in the show
    public ArrayList<Season> getAllSeasons() {
        return this.seasons;
    }

    public int getTimesWatched() {
        return this.timesWatched;
    }

    public void setTimesWatched(int times) {
        this.timesWatched = times;
    }

    // EFFECTS: returns if the show has been watched
    public boolean getWatched() {
        return this.timesWatched >= 1;
    }

    @Override
    // EFFECTS: returns a string representation of the show
    public String toString() {
        return showName;
        //return "Show Name = " + showName + ", Times Watched = " + timesWatched;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", showName);
        json.put("times", timesWatched);
        json.put("seasons", seasonsToJson());
        return json;
    }

    // EFFECTS: returns seasons in this show as a JSON array
    private JSONArray seasonsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Season s : seasons) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
