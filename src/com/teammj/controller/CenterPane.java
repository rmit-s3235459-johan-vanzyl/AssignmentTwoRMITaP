package com.teammj.controller;

import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.Referee;
import com.teammj.model.persons.base.Athlete;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static com.teammj.controller.Main.athleteGameMap;
import static com.teammj.controller.Main.games;

public class CenterPane implements Initializable {
    public TableView<Game> tblviewgames;
    public TableView<AthleteMap> tblViewGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGamesTable();
        setupGameTable();
    }

    private void setupGamesTable() {
        TableColumn<Game, DATA.GAMETYPE> gameType = new TableColumn<>("Game Type");
        TableColumn<Game, Integer> gameCount = new TableColumn<>("Game No");
        gameType.setCellValueFactory(new PropertyValueFactory<>("gametype"));
        gameCount.setCellValueFactory(new PropertyValueFactory<>("mycount"));

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
}
