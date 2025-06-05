package com.example.minesweeperproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsScreen extends Application {
    private Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        displaySettingsScreen();
    }

    public void displaySettingsScreen() {
        VBox settingsLayout = new VBox(10);
        settingsLayout.setPrefSize(450, 360);
        settingsLayout.setAlignment(Pos.CENTER);

        Label rowsLabel = new Label("Rows (max 15):");
        TextField rowsInput = new TextField("10");
        rowsInput.setMaxWidth(200);

        Label colsLabel = new Label("Columns (max 30):");
        TextField colsInput = new TextField("10");
        colsInput.setMaxWidth(200);

        Label minesLabel = new Label("Mines:");
        TextField minesInput = new TextField("10");
        minesInput.setMaxWidth(200);

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> {

            // Zachytávanie nesprávnych vstupov pri nastaveniach
            try {
                int rows = Integer.parseInt(rowsInput.getText());
                int cols = Integer.parseInt(colsInput.getText());
                int mines = Integer.parseInt(minesInput.getText());

                if (rows > 0 && cols > 0 && mines >= 0 && mines < rows * cols && rows < 16 && cols < 31) {
                    startGame(rows, cols, mines);
                } else {
                    showAlert("Invalid Input", "Please enter valid numbers for rows, columns, and mines.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for rows, columns, and mines.");
            }
        });

        settingsLayout.getChildren().addAll(rowsLabel, rowsInput, colsLabel, colsInput, minesLabel, minesInput, startGameButton);

        Scene settingsScene = new Scene(settingsLayout);
        primaryStage.setScene(settingsScene);
        primaryStage.setTitle("Minesweeper Settings");
        primaryStage.show();
    }

    private void startGame(int rows, int cols, int mines) {
        Main game = new Main(rows, cols, mines);
        game.start(primaryStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}