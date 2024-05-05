package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.Event;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

// ShowTracker application with GUI
public class ShowTrackerAppGUI extends JFrame {
    private static final String JSON_STORE = "./data/library.json";
    private JPanel main;
    private JPanel emptyPanel;
    private JPanel editPanel;
    private JPanel buttonPanel;
    private JButton save;
    private JButton load;
    private JButton addShow;
    private JButton add;
    private JButton remove;
    private DefaultListModel<Show> showModel;
    private JList showList;
    private DefaultListModel<Season> seasonModel;
    private JList seasonList;
    private DefaultListModel<Episode> episodeModel;
    private JList episodeList;
    private JScrollPane showScroll;
    private JScrollPane seasonScroll;
    private JScrollPane episodeScroll;
    private Library library;
    private JPanel sidePane;
    private String currentType;
    private JLabel nameInfo;
    private JLabel timesWatched;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JWindow loadingScreen;

    // EFFECTS: initializes GUI application, and opens it
    public ShowTrackerAppGUI() {
        setTitle("ShowTracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);

        library = new Library();
        currentType = "None";

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        main = new JPanel();
        main.setPreferredSize(new Dimension(200, 200));

        addLists();
        addPanes();
        addInformationPanels();
        addButtons();
        loadingScreen();
        logEvents();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes models and lists used to store shows, seasons, and episodes
    private void addLists() {
        showModel = new DefaultListModel<>();
        showList = new JList(showModel);
        showList.setFixedCellHeight(20);
        showList.setSelectionBackground(Color.BLUE);

        seasonModel = new DefaultListModel<>();
        seasonList = new JList(seasonModel);
        seasonList.setFixedCellHeight(20);
        seasonList.setSelectionBackground(Color.BLUE);

        episodeModel = new DefaultListModel<>();
        episodeList = new JList(episodeModel);
        episodeList.setFixedCellHeight(20);
        episodeList.setSelectionBackground(Color.BLUE);

        showList.addListSelectionListener(this::showSelectionChange);
        seasonList.addListSelectionListener(this::seasonSelectionChange);
        episodeList.addListSelectionListener(this::episodeSelectionChange);
    }

    // MODIFIES: this
    // EFFECTS: initializes save, load, add, and remove buttons
    private void addButtons() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1));
        buttonPanel.setBackground(Color.CYAN);

        save = new JButton("Save");
        save.addActionListener(e -> saveLibrary());
        load = new JButton("Load");
        load.addActionListener(e -> loadLibrary());

        addShow = new JButton("Add Show");
        addShow.addActionListener(e -> addShow());
        add = new JButton("Add");
        add.addActionListener(e -> addItem());
        remove = new JButton("Remove");
        remove.addActionListener(e -> removeButton());

        buttonPanel.add(save);
        buttonPanel.add(load);

        emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.CYAN);
        buttonPanel.add(emptyPanel);

        buttonPanel.add(addShow);
        buttonPanel.add(add);
        buttonPanel.add(remove);

        main.add(buttonPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds side panel with information on the currently selected item
    private void addInformationPanels() {
        editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(200,550));
        editPanel.setLayout(new GridLayout(6, 1));

        nameInfo = new JLabel();
        nameInfo.setOpaque(true);
        nameInfo.setText("");
        nameInfo.setHorizontalAlignment(JLabel.CENTER);

        timesWatched = new JLabel();
        timesWatched.setOpaque(true);
        timesWatched.setText("");
        timesWatched.setHorizontalAlignment(JLabel.CENTER);

        editPanel.add(nameInfo);
        editPanel.add(timesWatched);

        main.add(editPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes save, load, add, and remove buttons
    private void addPanes() {
        sidePane = new JPanel();

        showScroll = new JScrollPane(showList);
        showScroll.setPreferredSize(new Dimension(200, 550));
        showScroll.setVisible(true);

        seasonScroll = new JScrollPane(seasonList);
        seasonScroll.setPreferredSize(new Dimension(200, 550));
        seasonScroll.setVisible(true);

        episodeScroll = new JScrollPane(episodeList);
        episodeScroll.setPreferredSize(new Dimension(200, 550));
        episodeScroll.setVisible(true);

        sidePane.add(showScroll);
        sidePane.add(seasonScroll);
        sidePane.add(episodeScroll);
        main.add(sidePane);
        this.add(main);
        refreshShows();
    }

    // EFFECTS: adds a show, season, or episode to the corresponding list
    private void addItem() {
        switch (currentType) {
            case "None":
                addShow();
                break;
            case "Show": {
                Show show = showModel.get(showList.getSelectedIndex());
                Season season = new Season(show.getAllSeasons().size() + 1, 0);
                show.addSeason(season);
                seasonList.setSelectedIndex(show.getAllSeasons().size() - 1);
                refreshSeasons();
                break;
            }
            case "Season":
            case "Episode": {
                Season season = seasonModel.get(seasonList.getSelectedIndex());
                Episode episode = new Episode("Episode " + (season.getAllEpisodes().size() + 1), 0);
                season.addEpisode(episode);
                refreshEpisodes();
                break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a show to the show list
    private void addShow() {
        Show show = new Show("Show " + (library.getLibrarySize() + 1), 0);
        library.addShow(show);
        showModel.addElement(show);
        showList.setSelectedIndex(library.getLibrarySize() - 1);
    }

    // MODIFIES: this
    // EFFECTS: remove a show, season, or episode from the corresponding list
    private void removeButton() {
        if (episodeList.getSelectedIndex() != -1) {
            Show show = library.getAllShows().get(showList.getSelectedIndex());
            Season season = show.getAllSeasons().get(seasonList.getSelectedIndex());
            Episode episode = season.getAllEpisodes().get(episodeList.getSelectedIndex());
            season.removeEpisode(episode);
            refreshEpisodes();
        } else if (seasonList.getSelectedIndex() != -1) {
            Show show = library.getAllShows().get(showList.getSelectedIndex());
            Season season = show.getAllSeasons().get(seasonList.getSelectedIndex());
            show.removeSeason(season);
            refreshSeasons();
        } else if (showList.getSelectedIndex() != -1) {
            Show show = library.getAllShows().get(showList.getSelectedIndex());
            library.removeShow(show);
            if (library.getLibrarySize() == 0) {
                currentType = "None";
                nameInfo.setText("");
                timesWatched.setText("");
            }
            refreshShows();
        }
    }

    // EFFECTS: saves the library to a JSON file
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
            refreshShows();
            System.out.println("Loaded library from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: shows the amount of times the current selected item has been watched
    private void displayInfo() {
        if (episodeList.getSelectedIndex() != -1) {
            nameInfo.setText(episodeModel.get(episodeList.getSelectedIndex()).getEpisodeName());
            timesWatched.setText("Watched "
                    + episodeModel.get(episodeList.getSelectedIndex()).getTimesWatched()
                    + " Times");
        } else if (seasonList.getSelectedIndex() != -1) {
            nameInfo.setText("Season " + seasonModel.get(seasonList.getSelectedIndex()).getSeasonNumber());
            timesWatched.setText("Watched "
                    + seasonModel.get(seasonList.getSelectedIndex()).getTimesWatched()
                    + " Times");
        } else if (showList.getSelectedIndex() != -1) {
            nameInfo.setText(showModel.get(showList.getSelectedIndex()).getShowName());
            timesWatched.setText("Watched "
                    + showModel.get(showList.getSelectedIndex()).getTimesWatched()
                    + " Times");
        }
        main.repaint();
    }

    // EFFECTS: refreshes the GUI show list and selects the last item in the list
    private void refreshShows() {
        showModel.clear();
        seasonModel.clear();
        episodeModel.clear();

        for (Show s : library.getAllShows()) {
            showModel.addElement(s);
        }

        showList.setSelectedIndex(showModel.getSize() - 1);
    }

    // EFFECTS: refreshes the GUI season list with seasons in the currently selected show
    private void refreshSeasons() {
        seasonModel.clear();
        episodeModel.clear();

        Show show = showModel.get(showList.getSelectedIndex());
        for (Season se : show.getAllSeasons()) {
            seasonModel.addElement(se);
        }
    }

    // EFFECTS: refreshes the GUI episodes list with episodes in the currently selected season
    private void refreshEpisodes() {
        episodeModel.clear();

        Season season = seasonModel.get(seasonList.getSelectedIndex());
        for (Episode e : season.getAllEpisodes()) {
            episodeModel.addElement(e);
        }
    }

    // EFFECTS: listener for when the selected show changes to clear respective seasons and episodes
    private void showSelectionChange(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        currentType = "Show";

        seasonModel.clear();
        episodeModel.clear();

        Show show;
        try {
            show = showModel.get(showList.getSelectedIndex());

            for (Season s : show.getAllSeasons()) {
                seasonModel.addElement(s);
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            ;
        }

        if (seasonList.getSelectedIndex() == -1
                && episodeList.getSelectedIndex() == -1) {
            displayInfo();
        }

        if (showList.getSelectedIndex() == -1) {
            currentType = "None";
        }
    }

    // EFFECTS: listener for when the selected season changes to clear respective episodes
    private void seasonSelectionChange(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || seasonList.getSelectedIndex() == -1) {
            return;
        }

        currentType = "Season";

        episodeModel.clear();

        if (episodeList.getSelectedIndex() == -1) {
            displayInfo();
        }

        Season season = seasonModel.get(seasonList.getSelectedIndex());
        for (Episode ep : season.getAllEpisodes()) {
            episodeModel.addElement(ep);
        }
    }

    // EFFECTS: listener for when the selected episode changes
    private void episodeSelectionChange(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || episodeList.getSelectedIndex() == -1) {
            return;
        }

        currentType = "Episode";

        displayInfo();
    }

    // EFFECTS: displays the loading screen for 2 seconds
    private void loadingScreen() {
        loadingScreen = new JWindow();
        loadingScreen.setSize(550, 300);
        loadingScreen.setLocationRelativeTo(null);
        loadingScreen.add(new JLabel(new ImageIcon("./data/loadingscreen.png")));
        try {
            loadingScreen.setVisible(true);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ;
        }
        loadingScreen.dispose();
    }

    // EFFECTS: listener to display events when application is quit
    private void logEvents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Iterator<model.Event> events = EventLog.getInstance().iterator();

                while (events.hasNext()) {
                    System.out.println(events.next());
                }
            }
        });
    }
}
