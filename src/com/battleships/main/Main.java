package com.battleships.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    //TODO zamienic placeholdery na zmienne w labelach
    //TODO wstawianie statkow
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Battleships");
        primaryStage.setScene(new Scene(root, 780, 580));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
