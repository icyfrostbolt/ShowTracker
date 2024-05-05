package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// This class represents an episode with a name, and a counter of the number of times it has been watched

public class Episode implements Writable {
    private String episodeName; // the name of the episode
    private int timesWatched; // the number of times the show has been watched

    // EFFECTS: instantiates an episode with a name,
    //          no times watched
    //          and that is not fully watched
    public Episode(String name, int timesWatched) {
        this.episodeName = name;
        this.timesWatched = timesWatched;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String newName) {
        this.episodeName = newName;
    }

    public int getTimesWatched() {
        return this.timesWatched;
    }

    public void setTimesWatched(int times) {
        this.timesWatched = times;
    }

    // EFFECTS: returns if the episode has been watched
    public boolean getWatched() {
        return this.timesWatched >= 1;
    }

    // EFFECTS: returns a string representation of the episode
    public String toString() {
        return episodeName;
        //return "Episode Name = " + episodeName + ", Times Watched = " + timesWatched;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", episodeName);
        json.put("times", timesWatched);
        return json;
    }
}
