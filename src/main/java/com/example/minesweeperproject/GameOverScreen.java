package com.example.minesweeperproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// GameOver obrazovka
public class GameOverScreen extends Application {
    // Premenné pre uloženie parametrov hry a hlavného okna
    private Stage primaryStage;
    private int rows;
    private int cols;
    private int mines;
    private String time;
    private String message;

    // Konštruktor pre vytvorenie obrazovky Game Over s parametrami hry
    public GameOverScreen(Stage stage, int rows, int cols, int mines, String time, String message) {
        this.primaryStage = stage;
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.time = time;
        this.message = message;
    }

    // Metoda sa vola pri starte - na zobrazenie obrazovky
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        displayGameOverScreen();
    }

    // Zombrazenie obrazovka, tlacidiel a informacie
    public void displayGameOverScreen() {
        VBox layout = new VBox(10);
        layout.setPrefSize(450, 360);
        layout.setAlignment(Pos.CENTER); 

        // styly pre cas a nadpis
        String nadpisFont = "-fx-font-size: 40px;";
        String casFont = "-fx-font-size: 20px;";

        // Nadpis na vyhru alebo  prehru
        Label gameOverLabel = new Label(message);
        gameOverLabel.setStyle(nadpisFont);

        // Zobrazenie casu skoncenia hry
        Label timeLabel = new Label("Time: " + time);
        timeLabel.setStyle(casFont);

        // Tlačidlo na restart
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());

        // Tlačidlo na menu
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> openMainMenu());

        // Tlačidlo na zatvorenie apk
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        // pridava prvky do rozlozenia
        layout.getChildren().addAll(gameOverLabel, timeLabel, restartButton, mainMenuButton, exitButton);

        // nastavenie a zobrazenie okna
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game Over");
        primaryStage.show();
    }

    // restart hry so zachovanou velkostou a poctom min
    private void restartGame() {
        Main game = new Main(rows, cols, mines);
        game.start(primaryStage);
    }

    // otvorenie hl. menu
    private void openMainMenu() {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(primaryStage);
    }
}
