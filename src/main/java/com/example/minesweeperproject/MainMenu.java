package com.example.minesweeperproject;

// Importy JavaFX komponentov potrebných pre tvorbu GUI
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Trieda MainMenu predstavuje hlavné menu aplikácie Minesweeper.
 * Rozširuje triedu Application, čo je základná trieda pre JavaFX aplikácie.
 */
public class MainMenu extends Application {

    /**
     * Metóda start() je vstupný bod JavaFX aplikácie, kde sa vytvára GUI.
     * @param primaryStage hlavné okno aplikácie
     */
    @Override
    public void start(Stage primaryStage) {
        // Vytvorenie vertikálneho kontajnera s rozostupom 10 pixelov medzi prvkami
        VBox menuLayout = new VBox(10);
        menuLayout.setPrefSize(450, 360); // Nastavenie šírky a výšky kontajnera
        menuLayout.setAlignment(Pos.CENTER); // Zarovnanie obsahu na stred

        // Názov hry ako Label s väčším fontom
        Label minesweeperLabel = new Label("Minesweeper");
        minesweeperLabel.setStyle("-fx-font-size: 35px");

        // Tlačidlo pre spustenie hry
        Button startGameButton = new Button("Start Game");
        // Po kliknutí sa otvorí obrazovka s nastaveniami
        startGameButton.setOnAction(e -> openSettings(primaryStage));

        // Tlačidlo pre ukončenie aplikácie
        Button exitButton = new Button("Exit");
        // Po kliknutí sa zatvorí hlavné okno
        exitButton.setOnAction(e -> primaryStage.close());

        // Pridanie komponentov (label + tlačidlá) do rozloženia
        menuLayout.getChildren().addAll(minesweeperLabel, startGameButton, exitButton);

        // Vytvorenie scény s rozložením a nastavenie do hlavného okna
        Scene menuScene = new Scene(menuLayout);
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Minesweeper Menu"); // Názov hlavného okna
        primaryStage.show(); // Zobrazenie hlavného okna
    }

    /**
     * Pomocná metóda, ktorá otvorí obrazovku s nastaveniami.
     * @param primaryStage aktuálne hlavné okno aplikácie
     */
    private void openSettings(Stage primaryStage) {
        // Vytvorenie novej inštancie nastavení
        SettingsScreen settingsScreen = new SettingsScreen();
        // Nastavenie scény do rovnakého primárneho okna (prepneme scénu)
        settingsScreen.setPrimaryStage(primaryStage);
        // Zobrazenie obrazovky s nastaveniami
        settingsScreen.displaySettingsScreen();
    }

    /**
     * metóda main, ktorá spúšťa aplikáciu.
     * launch() volá metódu start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
