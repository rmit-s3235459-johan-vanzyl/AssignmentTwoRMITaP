package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.games.Game;
import com.teammj.model.persons.Cyclist;
import com.teammj.model.persons.Swimmer;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Main implements Initializable {

    private static Document document;
    private static final ArrayList<Athlete> athletes = new ArrayList<>();
    private static final ArrayDeque<Official> officials = new ArrayDeque<>();
    private static final ArrayDeque<Game> games = new ArrayDeque<>();
    public AnchorPane root;
    public VBox loadVbox;

    public void generateSaveFile() {
        document = DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadVbox.setOnMouseClicked(event -> loadFromFile());
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
                    System.out.println("Is cyclist");
                } else if(K instanceof Swimmer) {
                    System.out.println("Is swimmer");
                }
            });
        }
    }


    public void exit() {
        escape();
    }

    public static void escape() {
        if(document != null) {
            DocumentHandler.saveGame(document, null, new File("SaveGame.xml"));
        }
        Platform.exit();
        System.exit(0);
    }

    public static Document getDocument() {
        return document;
    }
}
