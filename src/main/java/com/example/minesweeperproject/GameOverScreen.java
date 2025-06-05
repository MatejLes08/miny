package com.example.minesweeperproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOverScreen extends Application {
    private Stage primaryStage;
    private int rows;
    private int cols;
    private int mines;
    private String time;
    private String message;

    public GameOverScreen(Stage stage, int rows, int cols, int mines, String time, String message) {
        this.primaryStage = stage;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.time = time;
        this.message = message;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        displayGameOverScreen();
    }

    public void displayGameOverScreen() {
        VBox layout = new VBox(10);
        layout.setPrefSize(450, 360);
        layout.setAlignment(Pos.CENTER);

        //vytvorenie rôznych veľkostí pre
        String nadpisFont = "-fx-font-size: 40px;";
        String casFont = "-fx-font-size: 20px;";

        Label gameOverLabel = new Label(message);
        gameOverLabel.setStyle(nadpisFont);

        Label timeLabel = new Label("Celkový čas: " + time);
        timeLabel.setStyle(casFont);

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> openMainMenu());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        layout.getChildren().addAll(gameOverLabel, timeLabel, restartButton, mainMenuButton, exitButton);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Over");
        primaryStage.show();
    }

    private void restartGame() {
        Main game = new Main(rows, cols, mines);
        game.start(primaryStage);
    }

    private void openMainMenu() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(primaryStage);
    }
}