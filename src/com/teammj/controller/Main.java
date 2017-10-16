package com.teammj.controller;

import com.teammj.model.games.Game;
import com.teammj.model.persons.AthleteMap;
import com.teammj.model.persons.Competitor;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import com.teammj.model.persons.base.Person;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Main controller for application
 */
final public class Main implements Initializable {

    private static Document document = null;
    static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    static final ObservableList<Official> officials = FXCollections.observableArrayList();
    static final ObservableList<Person> persons = FXCollections.observableArrayList();
    static final ObservableList<Competitor> gamePersons = FXCollections.observableArrayList();
    static final ObservableList<Game> games = FXCollections.observableArrayList();
    static final ObservableList<AthleteMap> athleteGameMap = FXCollections.observableArrayList();

    public AnchorPane root;
    public static boolean firstLaunch = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkFirstTimeLaunch();
        generateDocument();
    }

    private void checkFirstTimeLaunch() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(firstLaunch) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("First Time Launch");
                    alert.setHeaderText("Welcome!");
                    alert.setContentText("For the first launch, we recommend to press Generate button or load the sample 'SaveFile.xml'. Game is automatically saved as 'AutoSave.xml' after exit.");
                    alert.showAndWait();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("First Time Launch");
                    alert.setHeaderText("...Just another one");
                    alert.setContentText("Drag and drop athletes and referee from persons table to the new game table to create a game.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void generateDocument() {
        if (document != null) return;
        document = DocumentHandler.generateSaveFile(null, athletes, officials, false, games);
        persons.addAll(athletes);
        persons.addAll(officials);
    }

    /**
     * Handles auto save event when closed
     */
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

    public static void setDocument(Document document) {
        Main.document = document;
    }
}
