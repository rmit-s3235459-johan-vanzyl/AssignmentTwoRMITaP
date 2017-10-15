package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.*;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
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
    public TableView<Game> tblviewgames;
    public TableView<AthleteMap> tblViewGame;
    public TableView<Athlete> tblViewAthletesRank;
    public ToggleGroup toggleGroup;
    public TextField txtFieldAName;
    public TextField txtFieldAAge;
    public ComboBox cmbAState;
    public Button addAthlete;
    public Label addAfeedBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVbox.setOnMouseClicked(event -> loadFromFile());
        setupPersonsTable();
        setupGamesTable();
        setupGameTable();
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
                    Integer index = tableRow.getIndex();
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
            if(txtFieldAAge.getText().length() < 1) {
                addAfeedBack.setText("Please input age.");
                return;
            }
            if(txtFieldAName.getText().length() < 3) {
                addAfeedBack.setText("Please enter name.");
                return;
            }

            String typeToAdd = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
            String sState = (String) cmbAState.getValue();
            if(sState == null) {
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

            if(athlete != null) {
                persons.add(athlete);
                athletes.add(athlete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
