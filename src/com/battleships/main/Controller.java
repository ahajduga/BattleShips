package com.battleships.main;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Adam on 2015-10-16.
 */
public class Controller implements Initializable {

    @FXML
    private GridPane boardLeft;
    @FXML
    private GridPane boardRight;

    private Label currentLabel;
    @FXML
    private Label mast1Field;
    @FXML
    private Label mast2Field;
    @FXML
    private Label mast3Field;
    @FXML
    private Label mast4Field;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Circle rect = new Circle(10);

        Circle rect1 = new Circle(10);
        rect1.setFill(Color.BLUE);
        rect.setFill(Color.RED);
        boardLeft.add(rect, 0, 0);

        boardLeft.add(rect1, 0, 1);


        mast1Field.setText("1m, Left: 4");
        mast2Field.setText("2m, Left: 3");
        mast3Field.setText("3m, Left: 2");
        mast4Field.setText("4m, Left: 1");

    }

    public void shipSelected(Event event) {
        if (currentLabel != null)
            currentLabel.setStyle("-fx-background-color:white;");
        currentLabel = (Label) event.getSource();

        currentLabel.setStyle("-fx-background-color:yellow;");

    }
}
