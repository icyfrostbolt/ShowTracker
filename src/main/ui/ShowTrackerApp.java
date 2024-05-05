package ui;

import model.Episode;
import model.Library;
import model.Season;
import model.Show;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// ShowTracker application
public class ShowTrackerApp {
    private static final String JSON_STORE = "./data/library.json";
    private Scanner scanner;
    private Library library;
    private Show currentShow;
    private Season currentSeason;
    private Episode currentEpisode;
    private String currentType;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the show library application
    public ShowTrackerApp() {
        currentType = "Library";
        library = new Library();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runLibrary();
    }

    // MODIFIES: this
    // EFFECTS: gets the users commands
    private void runLibrary() {
        boolean run = true;
        String input = null;

        init();

        while (run) {
            showCommands();
            input = scanner.next().toLowerCase();

            if (input.equals("q")) {
                run = false;
            } else {
                processAction(input);
            }
        }
        System.out.println("\nEnded the program!");
    }

    // MODIFIES: this
    // EFFECTS: initializes the library
    private void init() {
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: processes the users command
    private void processAction(String action) {
        if (action.equals("v")) {
            outputAllChildren();
        } else if (action.equals("r")) {
            recommendUnwatched();
        } else if (action.equals("w")) {
            changeWatched();
        } else if (action.equals("f")) {
            goForwards();
        } else if (action.equals("b")) {
            goBackwards();
        } else if (action.equals("a")) {
            selectItem();
        } else if (action.equals("s")) {
            saveLibrary();
        } else if (action.equals("l")) {
            loadLibrary();
        } else {
            System.out.println("This is an invalid input!");
        }
    }

    // EFFECTS: shows a list of commands for the user
    private void showCommands() {
        System.out.println("\nSelect from:");
        System.out.println("v - view information");
        System.out.println("a - add");
        System.out.println("w - watch");
        System.out.println("f - move forwards");
        System.out.println("b - move backwards");
        System.out.println("r - recommend");
        System.out.println("s - save library");
        System.out.println("l - load library");
        System.out.println("q - quit\n");
    }

    // MODIFIES: this
    // EFFECTS: goes a folder deeper into the program
    private void goForwards() {
        if (currentType.equals("Library")) {
            currentShow = selectShow();
            currentType = "Show";
            if (currentShow != null) {
                System.out.println("You are now in " + currentShow.getShowName() + "!");
            }
        } else if (currentType.equals("Show")) {
            currentSeason = selectSeason();
            currentType = "Season";
            if (currentSeason != null) {
                System.out.println("You're in Season " + currentSeason.getSeasonNumber() + " of "
                        + currentShow.getShowName() + "!");
            }
        } else if (currentType.equals("Season")) {
            currentEpisode = selectEpisode();
            currentType = "Episode";
            if (currentEpisode != null) {
                System.out.println("You're in " + currentEpisode.getEpisodeName() + " of " + currentShow.getShowName());
            }
        } else {
            System.out.println("You cannot go any further!");
        }
    }

    // MODIFIES: this
    // EFFECTS: goes a folder backwards out of the program
    private void goBackwards() {
        if (currentType.equals("Show")) {
            currentType = "Library";
            System.out.println("You are now in the Library!");
        } else if (currentType.equals("Season")) {
            currentType = "Show";
            System.out.println("You are now in " + currentShow.getShowName() + "!");
        } else if (currentType.equals("Episode")) {
            currentType = "Season";
            System.out.println("You are now in Season " + currentSeason.getSeasonNumber()
                    + " of " + currentShow.getShowName() + "!");
        } else {
            System.out.println("You cannot go back any further!");
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the number of times an show, season, or episode has been watched
    private void changeWatched() {
        if (currentType.equals("Show")) {
            currentShow.setTimesWatched(currentShow.getTimesWatched() + 1);
            System.out.println("You have now watched the show " + currentShow.getShowName() + " "
                    + currentShow.getTimesWatched() + " times");
        } else if (currentType.equals("Season")) {
            currentSeason.setTimesWatched(currentSeason.getTimesWatched() + 1);
            System.out.println("You have now watched Season " + currentSeason.getSeasonNumber() + " of "
                    + currentShow.getShowName() + " " + currentSeason.getTimesWatched() + " times");
        } else if (currentType.equals("Episode")) {
            currentEpisode.setTimesWatched(currentEpisode.getTimesWatched() + 1);
            System.out.println("You have now watched the episode " + currentEpisode.getEpisodeName() + " "
                    + currentEpisode.getTimesWatched() + " times.");
        } else {
            System.out.println("You can't watch a library! Move forward in the folder system to "
                    + "change your watch count!");
        }
    }

    // MODIFIES: this
    // EFFECTS: based on the current folder type, runs a command to add a type of that item
    private void selectItem() {
        switch (currentType) {
            case "Library": {
                currentShow = selectShow();
                break;
            }
            case "Show": {
                currentSeason = selectSeason();
                break;
            }
            case "Season": {
                currentEpisode = selectEpisode();
                break;
            }
            default:
                System.out.println("You cannot go any further!");
                break;
        }
    }

    // EFFECTS: prints out the string information of the current child information,
    //          or episode if the currentType is an episode
    private void outputAllChildren() {
        if (currentType.equals("Library")) {
            ArrayList<Show> shows = library.getAllShows();
            for (Show show : shows) {
                System.out.println(show.toString());
            }
        } else if (currentType.equals("Show")) {
            ArrayList<Season> seasons = currentShow.getAllSeasons();
            for (Season season : seasons) {
                System.out.println(season.toString());
            }
        } else if (currentType.equals("Season")) {
            ArrayList<Episode> episodes = currentSeason.getAllEpisodes();
            for (Episode episode : episodes) {
                System.out.println(episode.toString());
            }
        } else {
            System.out.println(currentEpisode.toString());
        }
    }

    // MODIFIES: this
    // EFFECTS: takes a string input
    //          if a show already exists with this name, selects it
    //          if a show does not exist with this name, creates it
    private Show selectShow() {
        boolean done = true;
        String choice = "";
        ArrayList<Show> shows = library.getAllShows();

        System.out.println("Select a show!");
        while (done) {
            choice = scanner.next();
            for (Show show : shows) {
                if (show.getShowName().equals(choice)) {
                    done = false;
                    return show;
                }
            }
            System.out.println("Added new show: " + choice);
            Show newShow = new Show(choice, 0);
            library.addShow(newShow);
            return newShow;
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: takes a string input
    //          if a season already exists with this number in this show, selects it
    //          if a season does not exist with this number in this show, creates it
    private Season selectSeason() {
        boolean done = true;
        int choice = 0;
        ArrayList<Season> seasons = currentShow.getAllSeasons();

        System.out.println("Select a season! (Input must be an integer)");
        while (done) {
            choice = Integer.valueOf(scanner.next());
            for (Season season : seasons) {
                if (season.getSeasonNumber() == choice) {
                    done = false;
                    return season;
                }
            }
            System.out.println("Created Season " + choice + " of " + currentShow.getShowName());
            Season season = new Season(choice, 0);
            currentShow.addSeason(season);
            return season;
        }
        return null;
    }

    // EFFECTS: takes a string input
    //          if an episode already exists in this season with this name, selects it
    //          if an episode does not exist in this season with this name, creates it
    private Episode selectEpisode() {
        boolean done = true;
        String choice = "";
        ArrayList<Episode> episodes = currentSeason.getAllEpisodes();

        System.out.println("Select an episode!");
        while (done) {
            choice = scanner.next();
            for (Episode episode : episodes) {
                if (episode.getEpisodeName().equals(choice)) {
                    done = false;
                    return episode;
                }
            }
            System.out.println("Created Episode " + choice + " of "
                    + currentSeason.getSeasonNumber() + " of " + currentShow.getShowName());
            Episode episode = new Episode(choice, 0);
            currentSeason.addEpisode(episode);
            return episode;
        }
        return null;
    }

    // EFFECTS: depending on the current folder type, filters unwatched versions
    private void recommendUnwatched() {
        if (currentType.equals("Library")) {
            recommendShow();
        } else if (currentType.equals("Show")) {
            recommendSeason();
        } else if (currentType.equals("Season")) {
            recommendEpisode();
        } else {
            System.out.println("You cannot run this here!");
        }
    }

    // EFFECTS: returns the first unwatched show in the library
    private void recommendShow() {
        Show rec = library.findNextUnwatchedShow();
        if (rec != null) {
            System.out.println("You should watch " + rec.getShowName()
                    + ", you haven't watched that before!");
        } else {
            System.out.println("There is no show that you have not watched!");
        }
    }

    // EFFECTS: returns the first unwatched season in the show
    private void recommendSeason() {
        Season rec = currentShow.findNextUnwatchedSeason();
        if (rec != null) {
            System.out.println("You should watch Season" + rec.getSeasonNumber()
                    + ", you haven't watched that before!");
        } else {
            System.out.println("There is no show that you have not watched!");
        }
    }

    // EFFECTS: returns the first unwatched episode in the season
    private void recommendEpisode() {
        Episode rec = currentSeason.findNextUnwatchedEpisode();
        if (rec != null) {
            System.out.println("You should watch " + rec.getEpisodeName()
                    + ", you haven't watched that before!");
        } else {
            System.out.println("There is no show that you have not watched!");
        }
    }

    // EFFECTS: saves the library to a file
    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            System.out.println("Saved library to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the library from a file
    private void loadLibrary() {
        try {
            library = jsonReader.readLibrary();
            System.out.println("Loaded library from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
