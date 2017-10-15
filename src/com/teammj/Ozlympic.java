package com.teammj;

import com.teammj.controller.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Entry point to start the project.
 * Please launch main.
 * The logic will continue into the Main class from here on as specified
 * in the main.fxml file
 * @author Johan van Zyl
 * @author Michael Guida
 * @see com.teammj.controller.Main
 */
public class Ozlympic extends Application {

    private static Stage currentStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentStage = primaryStage;
        createStage();
        handleQuitCatch();
    }

    private void handleQuitCatch() {
        currentStage.setOnCloseRequest(event -> Main.escape());
    }

    // Main entry point
    public static void main(String[] args) {
        if(args.length > 0) {
            Main.loadFromFile(args[0]);
        }
        launch(args);
    }

    /**
     * Create scene for stage from fxml file.
     * Also sets stylesheet(s) for scene.
     */
    private void createStage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            Scene scene = new Scene (root, 1280, 720);//720p?
            final ObservableList<String> styleSheets = scene.getStylesheets();
            styleSheets.add(Ozlympic.class.getResource("/main.css").toExternalForm());
            currentStage.setTitle("Ozlympics");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("__Please ensure 'resources' folder is set in IDE (tested in Intellij)___.");
            System.err.println("__Please ensure 'source' folder is set in IDE (tested in Intellij)___.");
            System.err.println("If you still get errors, please contact s3235459@student.rmit.edu.au");
            e.printStackTrace();
        }
    }

    /**
     * Possibly redundant for project
     * @return the current stage
     */
    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static void setCurrentStage(Stage currentStage) {
        Ozlympic.currentStage = currentStage;
    }

}