package com.teammj;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    }

    // Main entry point
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Create scene for stage from fxml file.
     * Also sets stylesheet(s) for scene.
     */
    private void createStage() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
            Scene scene = new Scene (root, 1024, 720);//720p?
            final ObservableList<String> styleSheets = scene.getStylesheets();
            styleSheets.add(Ozlympic.class.getResource("/main.css").toExternalForm());
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("__Please ensure main.fxml is available in the directory__.");
            System.err.println("__Please ensure main.css is available in the directory__.");
            System.err.println("__Please ensure 'resources' folder is set in IDE (tested in Intellij)___.");
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