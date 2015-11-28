package com.battleships.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

enum Player {
    HUMAN, AI
};

public class Main extends Application {
    //TODO pobieranie ilosci poszczegolnych statkow z funkcji i wypelnianie labeli
    //TODO po wybraniu typu statku i kliknieciu w cell pojawia sie shadow
    //TODO drugi klik po wybraniu kierunku ustawia statek (po sprawdzeniu czy mozna)
    //TODO wysyla info o ustawieniu statku
    //TODO po ustawieniu wszystkich statkow aktywuje sie przycisk start

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
