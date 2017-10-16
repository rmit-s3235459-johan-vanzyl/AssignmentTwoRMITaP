package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.DATA;
import com.teammj.model.games.Game;
import com.teammj.model.persons.AthleteMap;
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

/**
 * Controller Class for the center pane
 * @author Johan van Zyl
 * @author Michael Guida
 */
final public class CenterPane implements Initializable {
    public TableView<Game> tblviewgames;
    public TableView<AthleteMap> tblViewGame;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGamesTable();
        setupGameTable();
        bindAnchors();
    }

    /**
     * Stretches content according window size
     */
    private void bindAnchors() {
        tblviewgames.prefHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
        tblviewgames.maxHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
        tblViewGame.prefHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
        tblViewGame.maxHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(DATA.HEIGHT_FROM_BOTTOM));
        tblViewGame.prefWidthProperty().bind(Ozlympic.getCurrentStage().widthProperty().subtract(918));
    }

    /**
     * Sets up the game menu where to choose games from
     */
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

    /**
     * Once a player selected game, we need to display competitors
     * @param selectedItem - selected game
     */
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

    /**
     * Sets up the game details table
     */
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
