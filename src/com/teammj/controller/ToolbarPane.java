package com.teammj.controller;

import com.teammj.Ozlympic;
import com.teammj.view.components.ToolBar;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.teammj.controller.Main.*;

final public class ToolbarPane implements Initializable{
    public VBox loader;
    public ToolBar rootTB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loader.setOnMouseClicked(event -> loadFile());
        rootTB.prefWidthProperty().bind(Ozlympic.getCurrentStage().widthProperty());
        rootTB.maxWidthProperty().bind(Ozlympic.getCurrentStage().widthProperty());
    }

    public void saveFile() {
        if (Main.getDocument() == null) return;
        DocumentHandler.saveGame(Main.getDocument(), Ozlympic.getCurrentStage());
    }

    public void generateSaveFile() {
        if(Main.getDocument() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Generating file");
            alert.setHeaderText("Potential information loss.");
            alert.setContentText("If you continue, the current game will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            } else {
                return;
            }
        }

        athletes.clear();
        officials.clear();
        persons.clear();
        gamePersons.clear();
        games.clear();
        athleteGameMap.clear();
        Main.setDocument(
                DocumentHandler.generateSaveFile(Ozlympic.getCurrentStage(), athletes, officials, true)
        );
        persons.addAll(athletes);
        persons.addAll(officials);
    }

    public void exit() {
        Main.escape();
    }

    public static void loadFile(String... args) {
        athletes.clear();
        officials.clear();
        persons.clear();
        gamePersons.clear();
        athleteGameMap.clear();

        games.forEach(game -> game.setCount(1));
        games.clear();

        Document document;
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

        Main.setDocument(document);

        persons.addAll(athletes);
        persons.addAll(officials);
    }
}
