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
import javafx.scene.layout.AnchorPane;

import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.*;

final public class Main implements Initializable {

    private static Document document;
    static final ObservableList<Athlete> athletes = FXCollections.observableArrayList();
    static final ObservableList<Official> officials = FXCollections.observableArrayList();
    static final ObservableList<Person> persons = FXCollections.observableArrayList();
    static final ObservableList<Competitor> gamePersons = FXCollections.observableArrayList();
    static final ObservableList<Game> games = FXCollections.observableArrayList();
    static final ObservableList<AthleteMap> athleteGameMap = FXCollections.observableArrayList();

    public AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateDocument();
    }

    private void generateDocument() {
        if (document != null) return;
        document = DocumentHandler.generateSaveFile(null, athletes, officials, false, games);
        persons.addAll(athletes);
        persons.addAll(officials);
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

    public static void setDocument(Document document) {
        Main.document = document;
    }
}
