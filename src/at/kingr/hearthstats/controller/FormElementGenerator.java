package at.kingr.hearthstats.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by roli on 14.03.17.
 */
public class FormElementGenerator {
    public static Label generateCustomGridLabel(String labelText) {
        Label label = new Label(labelText);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    public static Button generateCustomGridButton(String buttonText) {
        Button button = new Button(buttonText);
//        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER);
        return button;
    }

    public static GridPane generateCustomGridGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        return gridPane;
    }
}
