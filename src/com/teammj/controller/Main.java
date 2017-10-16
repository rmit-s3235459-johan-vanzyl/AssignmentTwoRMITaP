package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.CyclingGame;
import com.teammj.model.games.Game;
import com.teammj.model.games.SprintGame;
import com.teammj.model.games.SwimmingGame;
import com.teammj.model.persons.*;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Main implements Initializable {

    private static Document document;
    static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    static final ObservableList<Official> officials = FXCollections.observableArrayList();
    static final ObservableList<Person> persons = FXCollections.observableArrayList();
    static final ObservableList<Competitor> gamePersons = FXCollections.observableArrayList();
    static final ObservableList<Game> games = FXCollections.observableArrayList();
    static final ObservableList<AthleteMap> athleteGameMap = FXCollections.observableArrayList();

    public AnchorPane root;
    public AnchorPane centerPane;
    public TableView<Game> tblviewgames;
    public TableView<AthleteMap> tblViewGame;
    public TableView<Athlete> tblViewAthletesRank;
    public TableView<Competitor> tblGameParticipants;
    public Label addPfeedback;
    public ComboBox cmbGType;
    public Button startRaceBtn;
    public Label didIwin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGamesTable();
        setupGameTable();
        setupGamePersonsTable();
        setupAthletesRank();

        generateDocument();

    }


    private void generateDocument() {
        if (document != null) return;
        document = DocumentHandler.generateSaveFile(null, athletes, officials, false, games);
        persons.addAll(athletes);
        persons.addAll(officials);
    }

    public void generateSaveFile() {
        document = DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials, true);
        persons.addAll(athletes);
        persons.addAll(officials);
    }

    private void setupAthletesRank() {
        TableColumn<Athlete, String> athleteName = new TableColumn<>("Name");
        TableColumn<Athlete, Integer> athleteScore = new TableColumn<>("Points");
        athleteName.setCellValueFactory(new PropertyValueFactory<>("name"));
        athleteScore.setCellValueFactory(new PropertyValueFactory<>("points"));

        athleteName.setPrefWidth(140.0);

        tblViewAthletesRank.setItems(athletes);
        tblViewAthletesRank.getColumns().addAll(athleteName, athleteScore);

    }

    private void setupGameTable() {
        TableColumn<AthleteMap, String> nameColumn = new TableColumn<>("Name");
        TableColumn<AthleteMap, Integer> timeTaken = new TableColumn<>("Time Taken (s)");
        TableColumn<AthleteMap, Integer> athletePoints = new TableColumn<>("Total Points");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("athleteName"));
        timeTaken.setCellValueFactory(new PropertyValueFactory<>("athleteTime"));
        athletePoints.setCellValueFactory(new PropertyValueFactory<>("athletePoints"));

        nameColumn.setPrefWidth(140.0);
        timeTaken.setPrefWidth(100.0);

        tblViewGame.setItems(athleteGameMap);
        tblViewGame.getColumns().addAll(nameColumn, timeTaken, athletePoints);
    }

    private void setupGamesTable() {
        TableColumn<Game, DATA.GAMETYPE> gameType = new TableColumn<>("Game Type");
        TableColumn<Game, Integer> gameCount = new TableColumn<>("Game No");
        gameType.setCellValueFactory(new PropertyValueFactory<>("gametype"));
        gameCount.setCellValueFactory(new PropertyValueFactory<>("count"));

        tblviewgames.setItems(games);
        tblviewgames.getColumns().addAll(gameType, gameCount);
        tblviewgames.setOnMouseClicked(event -> {
            if (tblviewgames.getSelectionModel().getSelectedItem() != null) {
                fillInGameMap(tblviewgames.getSelectionModel().getSelectedItem());
            }

        });
    }

    private void setupGamePersonsTable() {
        TableColumn<Competitor, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Competitor, DATA.PERSON_TYPE> typeColumn = new TableColumn<>("Type");
        TableColumn<Competitor, Boolean> predictedWinner = new TableColumn<>("Winner?");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("personType"));
        predictedWinner.setCellValueFactory(new PropertyValueFactory<>("predictedWinner"));
        predictedWinner.setCellFactory(tc -> new CheckBoxTableCell<Competitor, Boolean>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                Platform.runLater(() -> checkPredictedErr());
            }
        });

        nameColumn.setPrefWidth(140.0);

        tblGameParticipants.setOnDragOver(event -> {

            if (event.getGestureSource() != tblGameParticipants &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });


        tblGameParticipants.setOnDragDropped(event -> {
            String sGameType = (String) cmbGType.getValue();
            if (sGameType == null) {
                addPfeedback.setText("Please select game type first!");
                addPfeedback.getStyleClass().add("warning");
                gameNotif("Drag Persons in Here!");
                return;
            }

            Dragboard dragboard = event.getDragboard();
            try {
                String possibleUUID = dragboard.getString();

                final Integer[] refCount = {0};
                gamePersons.forEach(person -> {
                    if (person.getPersonType() == DATA.PERSON_TYPE.Referee) {
                        refCount[0]++;
                    }
                });
                persons.forEach(person -> {
                    if (Objects.equals(person.getUniqueID().toString(), possibleUUID)) {
                        final boolean[] pleaseReturn = {false};
                        // Check for duplicates
                        gamePersons.forEach(person2 -> {
                            if (Objects.equals(person2.getUuid().toString(), possibleUUID)) {
                                addPfeedback.setText("Cannot add the same person twice!");
                                addPfeedback.getStyleClass().add("warning");
                                gameNotif("Drag Persons in Here!");
                                pleaseReturn[0] = true;
                            }
                        });
                        if (pleaseReturn[0]) return;
                        if (person.getPersonType() != DATA.PERSON_TYPE.SuperAthlete && person.getPersonType() != DATA.PERSON_TYPE.Referee) {
                            switch (sGameType) {
                                case "Swimming":
                                    if (person.getPersonType() != DATA.PERSON_TYPE.Swimmer) {
                                        pleaseReturn[0] = true;
                                    }
                                    break;
                                case "Sprinting":
                                    if (person.getPersonType() != DATA.PERSON_TYPE.Sprinter) {
                                        pleaseReturn[0] = true;
                                    }
                                    break;
                                case "Cycling":
                                    if (person.getPersonType() != DATA.PERSON_TYPE.Cyclist) {
                                        pleaseReturn[0] = true;
                                    }
                                    break;
                            }
                        }
                        if (pleaseReturn[0]) {
                            addPfeedback.setText("Please add appropriate athlete to game!");
                            addPfeedback.getStyleClass().add("warning");
                            gameNotif("Drag Persons in Here!");
                            return;
                        }
                        if (person.getPersonType() == DATA.PERSON_TYPE.Referee) {
                            if (refCount[0] == 0) {
                                gamePersons.add(new Competitor(person.getName(), person.getUniqueID(), false, person.getPersonType(), person));
                            } else {
                                addPfeedback.setText("Cannot add more than 2 refs!");
                                addPfeedback.getStyleClass().add("warning");
                                gameNotif("Drag Persons in Here!");
                            }
                        } else {
                            gamePersons.add(new Competitor(person.getName(), person.getUniqueID(), false, person.getPersonType(), person));
                        }
                    }
                });

                event.setDropCompleted(true);
                event.consume();
            } catch (Exception e) {
                e.printStackTrace();
                event.setDropCompleted(false);
            }
        });

        tblGameParticipants.getColumns().addAll(nameColumn, typeColumn, predictedWinner);
        tblGameParticipants.setItems(gamePersons);
        tblGameParticipants.setEditable(true);

        gamePersons.addListener((ListChangeListener<Competitor>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    Platform.runLater(() -> checkGameCanStart());
                }
            }
        });
    }

    private void checkGameCanStart() {
        final Boolean[] refFound = {false, false};
        final Integer[] count = {0};
        gamePersons.forEach(competitor -> {
            if (competitor.personType == DATA.PERSON_TYPE.Referee) {
                refFound[0] = true;
            } else {
                if (competitor.isPredictedWinner()) {
                    refFound[1] = true;
                }
                count[0]++;
            }
        });
        if (count[0] >= 4 && count[0] <= 8 && refFound[0] && refFound[1]) {
            startRaceBtn.setDisable(false);
        } else {
            startRaceBtn.setDisable(true);
        }
    }

    private void checkPredictedErr() {
        final Integer[] count = {0};
        gamePersons.forEach(competitor -> {
            if (competitor.getPersonType() == DATA.PERSON_TYPE.Referee) {
                competitor.setPredictedWinner(false);
            } else {
                if (competitor.isPredictedWinner()) {
                    if (count[0] > 0) competitor.setPredictedWinner(false);
                    count[0]++;
                }
            }
        });
        checkGameCanStart();
    }

    private void gameNotif(String s) {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Platform.runLater(() -> {
                    addPfeedback.setText(s);
                    addPfeedback.getStyleClass().clear();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fillInGameMap(Game selectedItem) {
        athleteGameMap.clear();
        tblViewGame.getItems().clear();
        HashMap<Athlete, Integer> map = (HashMap<Athlete, Integer>) selectedItem.getAthleteTimes();
        map.forEach((a, i) -> athleteGameMap.add(new AthleteMap(a.getName(), i, a.getPoints())));
        selectedItem.getParticipants().forEach(person -> {
            if (person instanceof Referee) {
                athleteGameMap.add(new AthleteMap(person.getName() + " (ref)", 0, 0));
            }
        });
    }

    public static void escape() {
        new Thread(() -> {
            if (document != null) {
                DocumentHandler.saveGame(document, null, new File("AutoSave.xml"));
            }
            Platform.exit();
            System.exit(0);
        }).start();
    }

    public static Document getDocument() {
        return document;
    }

    public void clearPlayerTbl() {
        gamePersons.clear();
        startRaceBtn.setDisable(true);
    }

    public void startRace() {// Let persons compete and place their times into a map
        Game game;
        final Athlete[] predictedWinner = new Athlete[1];
        switch ((String) cmbGType.getValue()) {
            case "Swimming":
                game = new SwimmingGame(DocumentHandler.addSwimmingGame(document));
                break;
            case "Sprinting":
                game = new SprintGame(DocumentHandler.addSprintingGame(document));
                break;
            case "Cycling":
                game = new CyclingGame(DocumentHandler.addCyclingGame(document));
                break;
            default:
                return;
        }
        Map<Athlete, Integer> athleteTimes = new HashMap<>(gamePersons.size() - 1);
        Map<Athlete, Integer> finalAthleteTimes = athleteTimes;
        gamePersons.forEach(competitor -> {
            if(competitor.personType != DATA.PERSON_TYPE.Referee) {
                Athlete athlete = (Athlete) competitor.getPerson();
                int time = athlete.compete(game);
                finalAthleteTimes.put(athlete, time);
                if(competitor.isPredictedWinner()) predictedWinner[0] = (Athlete) competitor.getPerson();
            }
            game.addParticipant(competitor.getPerson());
        });

        athleteTimes = Utilities.sortMapByValue(finalAthleteTimes);

        // Get the winner
        Athlete winner = athleteTimes.keySet().iterator().next();
        if (winner != null) {
            if (predictedWinner[0].getUniqueID().equals(winner.getUniqueID())) {
                System.out.println("Congratulations! You predicated correctly!\n");
                didIwin.setText("CONGRATS YOU PREDICTED CORRECTLY!!");
            } else {
                didIwin.setText("Incorrectly guessed winner :(");
                System.out.println("Incorrectly guessed winner :(\n");
            }
        }
        game.setHaveIbeenRan(true, false);
        game.setAthleteTimes(athleteTimes);
        games.add(game);

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

    public class Competitor {
        private String name;
        private UUID uuid;
        private BooleanProperty predictedWinner = new SimpleBooleanProperty();
        private DATA.PERSON_TYPE personType;
        private Person person;

        public Competitor(String name, UUID uuid, boolean predictedWinner, DATA.PERSON_TYPE personType, Person person) {
            this.name = name;
            this.uuid = uuid;
            this.predictedWinner.set(predictedWinner);
            this.personType = personType;
            this.person = person;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public boolean isPredictedWinner() {
            return predictedWinner.get();
        }

        public BooleanProperty predictedWinnerProperty() {
            return predictedWinner;
        }

        public void setPredictedWinner(boolean predictedWinner) {
            this.predictedWinner.set(predictedWinner);
        }

        public DATA.PERSON_TYPE getPersonType() {
            return personType;
        }

        public Person getPerson() {
            return person;
        }
    }

    public static void setDocument(Document document) {
        Main.document = document;
    }


}
