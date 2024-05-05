package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// This class represents a season with an identification number, a counter of the number of times it has
// been watched, and a list of all the episodes that are a part of the season.

public class Season implements Writable {
    private int timesWatched; // the number of times the show has been watched
    private int seasonNumber; // identifier for the season number
    private ArrayList<Episode> episodes; // list of episodes in the season

    // EFFECTS: instantiates a season with no episodes
    //          and that is not fully watched
    public Season(int number, int timesWatched) {
        this.timesWatched = timesWatched;
        this.seasonNumber = number;
        this.episodes = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an episode to the season
    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
        EventLog.getInstance().logEvent(new Event("Episode added"));
    }

    // MODIFIES: this
    // EFFECTS: removes a episode from the season
    public void removeEpisode(Episode episode) {
        this.episodes.remove(episode);
        EventLog.getInstance().logEvent(new Event("Episode removed"));
    }

    // EFFECTS: returns the first episode in the season that is not watched
    //          if no episodes in the season or not watched, returns null
    public Episode findNextUnwatchedEpisode() {
        for (Episode episode : this.episodes) {
            if (!episode.getWatched()) {
                return episode;
            }
        }
        return null;
    }

    // EFFECTS: returns if every episode in the season is watched
    public boolean checkEntireSeasonWatched() {
        for (Episode episode: this.episodes) {
            if (!episode.getWatched()) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns a list of all the episodes in the season
    public ArrayList<Episode> getAllEpisodes() {
        return this.episodes;
    }

    public int getTimesWatched() {
        return this.timesWatched;
    }

    public void setTimesWatched(int times) {
        this.timesWatched = times;
    }

    // EFFECTS: returns if the season has been watched
    public boolean getWatched() {
        return this.timesWatched >= 1;
    }

    public int getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(int num) {
        this.seasonNumber = num;
    }

    // EFFECTS: returns a string representation of the season
    public String toString() {
        return "Season " + seasonNumber;
        //return "Season " + seasonNumber + ", Times Watched = " + timesWatched;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("number", seasonNumber);
        json.put("times", timesWatched);
        json.put("episodes", episodesToJson());
        return json;
    }

    // EFFECTS: returns episodes in this season as a JSON array
    private JSONArray episodesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Episode e : episodes) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }
}
