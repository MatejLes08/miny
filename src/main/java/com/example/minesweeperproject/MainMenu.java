package com.example.minesweeperproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setPrefSize(450, 360);
        menuLayout.setAlignment(Pos.CENTER);

        Label minesweeperLabel = new Label("Minesweeper");
        minesweeperLabel.setStyle("-fx-font-size: 35px");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> openSettings(primaryStage));

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        menuLayout.getChildren().addAll(minesweeperLabel, startGameButton, exitButton);

        Scene menuScene = new Scene(menuLayout);
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Minesweeper Menu");
        primaryStage.show();
    }

    private void openSettings(Stage primaryStage) {
        SettingsScreen settingsScreen = new SettingsScreen();
        settingsScreen.setPrimaryStage(primaryStage);
        settingsScreen.displaySettingsScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}