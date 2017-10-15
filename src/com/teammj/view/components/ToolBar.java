package com.teammj.view.components;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Note
 * Normally all of these inner classes would be in a package
 * However, due to the requirements, we stuck them all in this class.
 *
 * Please use code folding to make navigation easier (if so desired)
 */

public class ToolBar extends BorderPane{
    private HBox leftBox = new HBox();
    private HBox rightBox = new HBox();

    public ToolBar() {
        defaultInit();
    }

    private void defaultInit() {
        this.getStyleClass().add("c-tool-bar");
        this.setLeft(leftBox);
        this.setRight(rightBox);
        leftBox.setPickOnBounds(false);
        rightBox.setPickOnBounds(false);
        leftBox.getStyleClass().add("tool-bar-left-box");
        rightBox.getStyleClass().add("tool-bar-right-box");
    }


    public void setLeftItems(Node... nodes) {
        this.leftBox.getChildren().setAll(nodes);
    }

    public void setRightItems(Node... nodes) {
        this.rightBox.getChildren().setAll(nodes);
    }

    public ObservableList<Node> getLeftItems() {
        return this.leftBox.getChildren();
    }

    public ObservableList<Node> getRightItems() {
        return this.rightBox.getChildren();
    }
}