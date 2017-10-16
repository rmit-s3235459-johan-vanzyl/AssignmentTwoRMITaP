package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.persons.base.Athlete;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static com.teammj.controller.Main.athletes;

public class RHSpane implements Initializable {
    public TableView tblViewAthletesRank;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAthletesRank();
        tblViewAthletesRank.prefHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(350));
        tblViewAthletesRank.maxHeightProperty().bind(Ozlympic.getCurrentStage().heightProperty().subtract(350));
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
}
