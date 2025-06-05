package com.example.minesweeperproject;

// Import potrebných tried z JavaFX pre GUI
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
    // Premenná pre hlavné okno aplikácie
    private Stage primaryStage;

    // Setter pre nastavenie hlavného okna
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    // Hlavná metóda pre spustenie JavaFX aplikácie
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        displaySettingsScreen(); // Zobrazenie obrazovky s nastaveniami
    }

    // Metóda na vytvorenie a zobrazenie obrazovky s nastaveniami hry
    public void displaySettingsScreen() {
        // Vertikálny layout s medzerou 10 pixelov
        VBox settingsLayout = new VBox(10);
        settingsLayout.setPrefSize(450, 360); // Nastavenie veľkosti okna
        settingsLayout.setAlignment(Pos.CENTER); // Centrovať obsah

        // Vytvorenie vstupných polí pre riadky
        Label rowsLabel = new Label("Rows (max 15):");
        TextField rowsInput = new TextField("10");
        rowsInput.setMaxWidth(200); // Obmedzenie šírky poľa

        // Vytvorenie vstupných polí pre stĺpce
        Label colsLabel = new Label("Columns (max 30):");
        TextField colsInput = new TextField("10");
        colsInput.setMaxWidth(200);

        // Vytvorenie vstupných polí pre počet mín
        Label minesLabel = new Label("Mines:");
        TextField minesInput = new TextField("10");
        minesInput.setMaxWidth(200);

        // Tlačidlo na spustenie hry
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> {
            // Spracovanie vstupov a validácia
            try {
                int rows = Integer.parseInt(rowsInput.getText());
                int cols = Integer.parseInt(colsInput.getText());
                int mines = Integer.parseInt(minesInput.getText());

                // Kontrola platnosti vstupov
                if (rows > 0 && cols > 0 && mines >= 0 && mines < rows * cols && rows < 16 && cols < 31) {
                    startGame(rows, cols, mines); // Spustenie hry
                } else {
                    showAlert("Invalid Input", "Please enter valid numbers for rows, columns, and mines.");
                }
            } catch (NumberFormatException ex) {
                // Chyba pri nečíselnom vstupe
                showAlert("Invalid Input", "Please enter valid numbers for rows, columns, and mines.");
            }
        });

        // Pridanie všetkých prvkov do layoutu
        settingsLayout.getChildren().addAll(rowsLabel, rowsInput, colsLabel, colsInput, minesLabel, minesInput, startGameButton);

        // Vytvorenie a nastavenie scény
        Scene settingsScene = new Scene(settingsLayout);
        primaryStage.setScene(settingsScene);
        primaryStage.setTitle("Minesweeper Settings"); // Názov okna
        primaryStage.show(); // Zobrazenie okna
    }

    // Metóda na spustenie hry s danými parametrami
    private void startGame(int rows, int cols, int mines) {
        Main game = new Main(rows, cols, mines);
        game.start(primaryStage); // Prepnutie na hlavnú hru
    }

    // Metóda na zobrazenie chybového upozornenia
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Zobrazenie dialógu
    }
}