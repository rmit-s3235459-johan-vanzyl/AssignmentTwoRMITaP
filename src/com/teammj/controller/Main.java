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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;
import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class Main implements Initializable {

    private static Document document;
    private static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    private static final ObservableList<Official> officials = FXCollections.observableArrayList();
    private static final ObservableList<Person> persons = FXCollections.observableArrayList();
    private static final ObservableList<Game> games = FXCollections.observableArrayList();
    public AnchorPane root;
    public VBox loadVbox;
    public TableView<Person> tblViewPersons;
    public AnchorPane centerPane;

    public void generateSaveFile() {
        document = DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVbox.setOnMouseClicked(event -> loadFromFile());
        setupPersonsTable();
    }

    private void setupPersonsTable() {
        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Person, DATA.PERSON_TYPE> typeColumn = new TableColumn<>("Type");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("personType"));

        nameColumn.setMinWidth(160.0);
        typeColumn.setMinWidth(100.0);

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

        for (Athlete a : athletes) {
            System.out.println(a.getUniqueID().toString());
        }

        for(Game game : games) {
            System.out.println(game.getUniqueID());
            Map<Athlete, Integer> athleteIntegerMap = game.getAthleteTimes();
            athleteIntegerMap.forEach((K,V) -> {
                System.out.println(K.getUniqueID());
                System.out.println(V);
                if(K instanceof Cyclist) {
                    System.out.println("Is Cyclist");
                } else if(K instanceof Swimmer) {
                    System.out.println("Is Swimmer");
                }
            });
        }
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
    }
}
