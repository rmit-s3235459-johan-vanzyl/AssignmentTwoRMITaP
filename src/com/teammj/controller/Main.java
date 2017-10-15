package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.Cyclist;
import com.teammj.model.persons.Swimmer;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Main implements Initializable {

    private static Document document;
    private static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    private static final ObservableList<Official> officials = FXCollections.observableArrayList();
    private static final ObservableList<Person> persons = FXCollections.observableArrayList();
    private static final ObservableList<Game> games = FXCollections.observableArrayList();
    private static final ObservableList<AthleteMap> athleteGameMap = FXCollections.observableArrayList();

    public AnchorPane root;
    public VBox loadVbox;
    public TableView<Person> tblViewPersons;
    public AnchorPane centerPane;
    public TableView<Game> tblViewGames;
    public TableView<AthleteMap> tblViewGame;

    public void generateSaveFile() {
        document = DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVbox.setOnMouseClicked(event -> loadFromFile());
        setupPersonsTable();
        setupGamesTable();
        setupGameTable();
    }

    private void setupGameTable() {
        TableColumn<AthleteMap, String> nameColumn = new TableColumn<>("Name");
        TableColumn<AthleteMap, Integer> timeTaken = new TableColumn<>("Time Taken (s)");
        TableColumn<AthleteMap, Integer> athletePoints = new TableColumn<>("Total Points");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("athleteName"));
        timeTaken.setCellValueFactory(new PropertyValueFactory<>("athleteTime"));
        athletePoints.setCellValueFactory(new PropertyValueFactory<>("athletePoints"));

        tblViewGame.setItems(athleteGameMap);
        tblViewGame.getColumns().addAll(nameColumn, timeTaken, athletePoints);
    }

    private void setupGamesTable() {
        TableColumn<Game, DATA.GAMETYPE> gameType = new TableColumn<>("Game Type");
        TableColumn<Game, Integer> gameCount = new TableColumn<>("Game No");
        gameType.setCellValueFactory(new PropertyValueFactory<>("gametype"));
        gameCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        tblViewGames.setItems(games);
        tblViewGames.getColumns().addAll(gameType, gameCount);
        tblViewGames.setOnMouseClicked(event -> {
            if(tblViewGames.getSelectionModel().getSelectedItem() != null) {
                fillInGameMap(tblViewGames.getSelectionModel().getSelectedItem());
            }

        });
    }

    private void fillInGameMap(Game selectedItem) {
        athleteGameMap.removeAll();
        tblViewGame.getItems().clear();
        HashMap<Athlete, Integer> map = (HashMap<Athlete, Integer>) selectedItem.getAthleteTimes();
        map.forEach((a, i) -> athleteGameMap.add(new AthleteMap(a.getName(), i, a.getPoints())));
    }

    private void setupPersonsTable() {
        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Person, DATA.PERSON_TYPE> typeColumn = new TableColumn<>("Type");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("personType"));

        nameColumn.setMinWidth(140.0);
        typeColumn.setMinWidth(70.0);

        nameColumn.setEditable(true);

        tblViewPersons.setItems(persons);
        tblViewPersons.getColumns().addAll(nameColumn, typeColumn);
    }

    public static void loadFromFile(String... args) {

        if(args.length > 0) {
            File file = new File(args[0]);
            System.out.println(file);
            if(!file.exists()) {
                System.err.println("Specified load file does not exist");
                System.exit(1);
            }
            document = DocumentHandler.loadFromSavedFile(
                    Ozlympic.getCurrentStage(),
                    athletes,
                    officials,
                    games,
                    file
            );
        } else {
            document = DocumentHandler.loadFromSavedFile(
                    Ozlympic.getCurrentStage(),
                    athletes,
                    officials,
                    games
            );
        }

        persons.addAll(athletes);
        persons.addAll(officials);

        officials.forEach(official -> System.out.println(official.getName()));

    }


    public void exit() {
        escape();
    }

    public static void escape() {
        new Thread(() -> {
            if(document != null) {
                DocumentHandler.saveGame(document, null, new File("SaveGame.xml"));
            }
            Platform.exit();
            System.exit(0);
        }).start();
    }

    public static Document getDocument() {
        return document;
    }

    public void saveFile() {
        if(document == null) return;
        DocumentHandler.saveGame(document, Ozlympic.getCurrentStage());
    }

    public class AthleteMap {
        private String athleteName;
        private Integer athleteTime;
        private Integer athletePoints;

        AthleteMap(String athleteName, Integer athleteTime, Integer athletePoints) {
            this.athleteName = athleteName;
            this.athleteTime = athleteTime;
            this.athletePoints = athletePoints;
        }

        public String getAthleteName() {
            return athleteName;
        }

        public Integer getAthleteTime() {
            return athleteTime;
        }

        public Integer getAthletePoints() {
            return athletePoints;
        }
    }
}
