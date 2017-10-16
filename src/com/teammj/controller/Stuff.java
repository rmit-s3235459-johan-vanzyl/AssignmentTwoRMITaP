package com.teammj.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;

public class Stuff {


    public static void main(String[] args){

//        Tooltip loadTip = new Tooltip();
//        loadTip.setText("Click here to load a file");
//
//        loader.setTooltip(loadTip);
//
//        Tooltip saveTip = new Tooltip();
//        loadTip.setText("Click here to save file");
//
//        onMouseClicked.setToolTip(saveTip);
//        saveFile.setToolTip(saveTip);
//
//        Tooltip genTip = new Tooltip();
//        loadTip.setText("Click here to generate people");
//
//        onMouseClicked.setToolTip(genTip);
//        generateSaveFile.setToolTip(genTip);
//
//        onMouseClicked.setToolTip(genTip);
//        generateSaveFile.setToolTip(genTip);
//
//        Tooltip exitTip = new Tooltip();
//        exitTip.setText("Click here to exit program");
//
//        onMouseClicked.setToolTip(exitTip);
//        exit.setToolTip(exitTip);

        //testing push

        Alert dialog1 = new Alert(Alert.AlertType.INFORMATION);
        dialog1.setTitle("Dialog 1");
        dialog1.setHeaderText("Dialog box");
        dialog1.setContentText("This is a message");

        dialog1.showAndWait();


    }


}
