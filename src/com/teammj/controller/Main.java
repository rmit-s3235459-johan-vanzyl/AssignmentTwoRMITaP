package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.model.games.Game;
import com.teammj.model.persons.Cyclist;
import com.teammj.model.persons.Swimmer;
import com.teammj.model.persons.base.Athlete;
import com.teammj.model.persons.base.Official;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class Main implements Initializable {
    public AnchorPane root;
    public Button generateTxtFile;
    public BorderPane topBorderPane;

    private Document document;
    private ArrayList<Athlete> athletes;
    ArrayDeque<Official> officials;

    public void generateSaveFile() {
        athletes = new ArrayList<>();
        officials = new ArrayDeque<>();
        document = DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //generateTxtFile.setText("Generate\n  save file");
    }

    public void loadFromFile() {
        ArrayList<Athlete> athletes = new ArrayList<>();
        ArrayDeque<Official> officials = new ArrayDeque<>();
        ArrayDeque<Game> games = new ArrayDeque<>();
        document = DocumentHandler.loadFromSavedFile(
                Ozlympic.getCurrentStage(),
                athletes,
                officials,
                games
        );

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

    public void saveToFile() {
        if(document == null) return; //TODO: prompt window
        DocumentHandler.saveGame(document, Ozlympic.getCurrentStage());
    }

}
