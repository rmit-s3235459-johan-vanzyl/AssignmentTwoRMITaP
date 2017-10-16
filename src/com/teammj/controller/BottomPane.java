package com.teammj.controller;

import com.teammj.model.DATA;
import com.teammj.model.games.CyclingGame;
import com.teammj.model.games.Game;
import com.teammj.model.games.SprintGame;
import com.teammj.model.games.SwimmingGame;
import com.teammj.model.persons.Competitor;
import com.teammj.model.persons.Referee;
import com.teammj.model.persons.base.Athlete;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.teammj.controller.Main.gamePersons;
import static com.teammj.controller.Main.games;
import static com.teammj.controller.Main.persons;

final public class BottomPane implements Initializable{
    public ComboBox cmbGType;
    public TableView<Competitor> tblGameParticipants;
    public Label addPfeedback;
    public Button startRaceBtn;
    public Label didIwin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGamePersonsTable();
    }

    public void clearPlayerTbl() {
        gamePersons.clear();
        startRaceBtn.setDisable(true);
    }

    public void startRace() {
        Game game;
        final Athlete[] predictedWinner = new Athlete[1];
        switch ((String) cmbGType.getValue()) {
            case "Swimming":
                game = new SwimmingGame(DocumentHandler.addSwimmingGame(Main.getDocument()));
                break;
            case "Sprinting":
                game = new SprintGame(DocumentHandler.addSprintingGame(Main.getDocument()));
                break;
            case "Cycling":
                game = new CyclingGame(DocumentHandler.addCyclingGame(Main.getDocument()));
                break;
            default:
                return;
        }
        Map<Athlete, Integer> athleteTimes = new HashMap<>(gamePersons.size() - 1);
        Map<Athlete, Integer> finalAthleteTimes = athleteTimes;
        final Referee[] referee = new Referee[1];
        gamePersons.forEach(competitor -> {
            if(competitor.getPersonType() != DATA.PERSON_TYPE.Referee) {
                Athlete athlete = (Athlete) competitor.getPerson();
                int time = athlete.compete(game);
                finalAthleteTimes.put(athlete, time);
                if(competitor.isPredictedWinner()) predictedWinner[0] = (Athlete) competitor.getPerson();
            } else {
                referee[0] = (Referee) competitor.getPerson();
            }
            game.addParticipant(competitor.getPerson());
        });

        athleteTimes = Utilities.sortMapByValue(finalAthleteTimes);
        referee[0].printAthleteTimes(athleteTimes);

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
            if (competitor.getPersonType() == DATA.PERSON_TYPE.Referee) {
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
                if(competitor.isPredictedWinner()) {
                    competitor.setPredictedWinner(false);
                    addPfeedback.setText("Referee cannot be the predicted winner!");
                    addPfeedback.getStyleClass().add("warning");
                    gameNotif("Drag Persons in Here!");
                }
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
}
