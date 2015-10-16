package com.battleships.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(boardLeft);
        System.out.println(boardLeft);
        System.out.println(boardRight);
    }
}
