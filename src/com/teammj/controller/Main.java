package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.*;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class Main implements Initializable {

    private static Document document;
    private static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    private static final ObservableList<Official> officials = FXCollections.observableArrayList();
    private static final ObservableList<Person> persons = FXCollections.observableArrayList();
    private static final ObservableList<Competitor> gamePersons = FXCollections.observableArrayList();
    private static final ObservableList<Game> games = FXCollections.observableArrayList();
    private static final ObservableList<AthleteMap> athleteGameMap = FXCollections.observableArrayList();

    public AnchorPane root;
    public VBox loadVbox;
    public TableView<Person> tblViewPersons;
    public AnchorPane centerPane;
    public TableView<Game> tblviewgames;
    public TableView<AthleteMap> tblViewGame;
    public TableView<Athlete> tblViewAthletesRank;
    public ToggleGroup toggleGroup;
    public TextField txtFieldAName;
    public TextField txtFieldAAge;
    public ComboBox cmbAState;
    public Button addAthlete;
    public Label addAfeedBack;
    public TextField txtFieldRAge;
    public TextField txtFieldRName;
    public ComboBox cmbRState;
    public Label addRfeedBack;
    public Button addReferee;
    public TableView<Competitor> tblGameParticipants;
    public Label addPfeedback;
    public ComboBox cmbGType;
    public Button startRaceBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVbox.setOnMouseClicked(event -> loadFromFile());
        setupPersonsTable();
        setupGamesTable();
        setupGameTable();
        setupGamePersonsTable();
        setupAthletesRank();
        setupValidators();

        generateDocument();
    }

    private void setupValidators() {
        txtFieldAName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldAName.getText().matches(DATA.FULL_NAME)) {
                    addAfeedBack.setText("Wrong name input");
                    txtFieldAName.setText("");
                } else {
                    addAfeedBack.setText("");
                }
            }

        });

        txtFieldAAge.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldAAge.getText().matches(DATA.POSTIVE_INTEGER_ONE_TO_NINE)) {
                    addAfeedBack.setText("Wrong age input");
                    txtFieldAAge.setText("");
                } else {
                    addAfeedBack.setText("");
                }
            }
        });

        txtFieldRName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldRName.getText().matches(DATA.FULL_NAME)) {
                    addRfeedBack.setText("Wrong name input");
                    txtFieldRName.setText("");
                } else {
                    addRfeedBack.setText("");
                }
            }

        });

        txtFieldRAge.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!txtFieldRAge.getText().matches(DATA.POSTIVE_INTEGER_ONE_TO_NINE)) {
                    addRfeedBack.setText("Wrong age input");
                    txtFieldRAge.setText("");
                } else {
                    addRfeedBack.setText("");
                }
            }
        });

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
                if(item != null) {
                    if(item) checkPredictedErr();
                }
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
                                gamePersons.add(new Competitor(person.getName(), person.getUniqueID(), false, person.getPersonType()));
                            } else {
                                addPfeedback.setText("Cannot add more than 2 refs!");
                                addPfeedback.getStyleClass().add("warning");
                                gameNotif("Drag Persons in Here!");
                            }
                        } else {
                            gamePersons.add(new Competitor(person.getName(), person.getUniqueID(), false, person.getPersonType()));
                        }
                    }
                });

                if (gamePersons.size() > 0) {
                    startRaceBtn.setDisable(false);
                }
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

    }

    private void checkPredictedErr() {
        gamePersons.forEach(competitor -> {
            if(competitor.getPersonType() == DATA.PERSON_TYPE.Referee) {
                competitor.setPredictedWinner(false);
            }

        });
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

    private void setupPersonsTable() {
        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Person, DATA.PERSON_TYPE> typeColumn = new TableColumn<>("Type");
        TableColumn<Person, DATA.STATE> stateColumn = new TableColumn<>("State");
        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("personType"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("fromState"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        nameColumn.setPrefWidth(140.0);
        typeColumn.setPrefWidth(70.0);

        tblViewPersons.setRowFactory(tv -> {
            TableRow<Person> tableRow = new TableRow<>();

            tableRow.setOnDragDetected(event -> {
                if (!tableRow.isEmpty()) {
                    Dragboard dragboard = tableRow.startDragAndDrop(TransferMode.COPY);
                    dragboard.setDragView(tableRow.snapshot(null, null));
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(tableRow.getItem().getUniqueID().toString());
                    dragboard.setContent(clipboardContent);
                    event.consume();
                }
            });

            return tableRow;
        });

        tblViewPersons.setItems(persons);
        tblViewPersons.getColumns().addAll(nameColumn, typeColumn, stateColumn, ageColumn);
    }

    public static void loadFromFile(String... args) {

        athletes.clear();
        officials.clear();

        games.forEach(game -> game.setCount(1));

        games.clear();

        if (args.length > 0) {
            File file = new File(args[0]);
            System.out.println(file);
            if (!file.exists()) {
                System.err.println("Specified load file does not exist");
                System.exit(1);
            }
            document = DocumentHandler.loadFromSavedFile(
                    null,
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

    public void saveFile() {
        if (document == null) return;
        DocumentHandler.saveGame(document, Ozlympic.getCurrentStage());
    }

    public void addNewAthlete() {
        try {
            if (txtFieldAAge.getText().length() < 1) {
                addAfeedBack.setText("Please input age.");
                return;
            }
            if (txtFieldAName.getText().length() < 3) {
                addAfeedBack.setText("Please enter name.");
                return;
            }

            String typeToAdd = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
            String sState = (String) cmbAState.getValue();
            if (sState == null) {
                addAfeedBack.setText("Please select state");
                return;
            }

            Integer age = Integer.valueOf(txtFieldAAge.getText());
            String name = txtFieldAName.getText();
            DATA.STATE stateType;

            switch (sState) {
                case "NSW":
                    stateType = DATA.STATE.NSW;
                    break;
                case "QLD":
                    stateType = DATA.STATE.QLD;
                    break;
                case "SA":
                    stateType = DATA.STATE.SA;
                    break;
                case "TAS":
                    stateType = DATA.STATE.TAS;
                    break;
                case "VIC":
                    stateType = DATA.STATE.VIC;
                    break;
                case "WA":
                    stateType = DATA.STATE.WA;
                    break;
                default:
                    addAfeedBack.setText("Please select state");
                    return;
            }
            addAfeedBack.setText("");

            Element element;

            Athlete athlete = null;
            switch (typeToAdd) {
                case "Cyclist":
                    element = DocumentHandler.addCyclist(document);
                    if (element != null)
                        athlete = new Cyclist(name, age, stateType, element);
                    break;
                case "Sprinter":
                    element = DocumentHandler.addSprinter(document);
                    if (element != null)
                        athlete = new Sprinter(name, age, stateType, element);
                    break;
                case "Swimmer":
                    element = DocumentHandler.addSwimmer(document);
                    if (element != null)
                        athlete = new Swimmer(name, age, stateType, element);
                    break;
                case "SuperAthlete":
                    element = DocumentHandler.addSuperAthlete(document);
                    if (element != null)
                        athlete = new SuperAthlete(name, age, stateType, element);
                    break;
                default:
                    return;
            }

            if (athlete != null) {
                persons.add(athlete);
                athletes.add(athlete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewReferee() {
        try {
            if (txtFieldRAge.getText().length() < 1) {
                addRfeedBack.setText("Please input age.");
                return;
            }
            if (txtFieldRName.getText().length() < 3) {
                addRfeedBack.setText("Please enter name.");
                return;
            }

            String sState = (String) cmbRState.getValue();
            if (sState == null) {
                addRfeedBack.setText("Please select state");
                return;
            }

            Integer age = Integer.valueOf(txtFieldRAge.getText());
            String name = txtFieldRName.getText();
            DATA.STATE stateType;

            switch (sState) {
                case "NSW":
                    stateType = DATA.STATE.NSW;
                    break;
                case "QLD":
                    stateType = DATA.STATE.QLD;
                    break;
                case "SA":
                    stateType = DATA.STATE.SA;
                    break;
                case "TAS":
                    stateType = DATA.STATE.TAS;
                    break;
                case "VIC":
                    stateType = DATA.STATE.VIC;
                    break;
                case "WA":
                    stateType = DATA.STATE.WA;
                    break;
                default:
                    addRfeedBack.setText("Please select state");
                    return;
            }
            addRfeedBack.setText("");

            Element element = DocumentHandler.addReferee(document);
            if (element == null) return;
            Referee referee = new Referee(name, age, stateType, element);
            persons.add(referee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearPlayerTbl() {
        gamePersons.clear();
        startRaceBtn.setDisable(true);
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

        public Competitor(String name, UUID uuid, boolean predictedWinner, DATA.PERSON_TYPE personType) {
            this.name = name;
            this.uuid = uuid;
            this.predictedWinner.set(predictedWinner);
            this.personType = personType;
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

        public void setPersonType(DATA.PERSON_TYPE personType) {
            this.personType = personType;
        }
    }
}
