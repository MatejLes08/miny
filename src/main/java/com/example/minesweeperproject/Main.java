package com.example.minesweeperproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Random;

// Hlavná trieda aplikácie Minesweeper s JavaFX GUI
public class Main extends Application {
    // Herné parametre
    private int rows;                   // počet riadkov v mape
    private int cols;                   // počet stĺpcov v mape
    private int mines;                  // počet mín
    private int unflaggedMines;         // počet neoznačených mín (na začiatku rovný počtu mín)
    private int clickCount;             // počet kliknutí používateľa
    private Cell[][] cells;             // 2D pole herných políčok
    private Stage primaryStage;         // hlavné okno aplikácie

    // UI komponenty
    private Label unflaggedMinesLabel;  // zobrazí počet neoznačených mín
    private Label clickCountLabel;      // zobrazí počet kliknutí
    private Button resetButton;         // tlačidlo na resetovanie hry
    private boolean firstClick = true;  // indikátor prvého kliknutia

    //premenné pre prácu s časom
    private Label timerLabel;                   // zobrazí čas
    private javafx.animation.Timeline timeline; //vybral som timeline pre jeho jednoduchosť a praktickosť v tomto projekte
    private int secondsElapsed;                 // uplynutý čas v sekundách
    private boolean timerStarted = false;       // príznak, či sa časovač spustil


    public Main() {
        this(10, 10, 10);   // Predvolená hra: 10x10 s 10 mínami
    }

    public Main(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.unflaggedMines = mines;
        this.clickCount = 0;
        cells = new Cell[rows][cols];
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Vytvorenie hernej mriežky
        GridPane grid = new GridPane();
        initializeCells(grid);              // naplnenie gridu políčkami

        // Horný panel s informáciami a reset tlačidlom
        unflaggedMinesLabel = new Label("Unflagged Mines: " + unflaggedMines);
        timerLabel = new Label("Time: 0");
        clickCountLabel = new Label("Clicks: " + clickCount);
        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetGame());

        HBox topBar = new HBox(20, unflaggedMinesLabel, timerLabel, clickCountLabel, resetButton);
        topBar.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, topBar, grid);
        root.setAlignment(Pos.CENTER);

        // výpočet veľkosti okna
        double sceneWidth = 40 * cols + 20;     // 20 = dodatočný priestor na padding
        double sceneHeight = 40 * rows + 100;   // 100 = dodatočný priestor na top bar a padding

        setupTimer(); // nastavenie časovača

        // Zobrazenie scény
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Minesweeper");
        primaryStage.show();
    }

    // Nastavenie časovača, ktorý každú sekundu zvýši počet sekúnd a aktualizuje zobrazenie
    private void setupTimer() {
        timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                    secondsElapsed++;
                    timerLabel.setText("Time: " + getTime(secondsElapsed));
                })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE); // beží navždy
    }


    // Inicializácia políčok v mriežke
    private void initializeCells(GridPane grid) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);           // vytvorenie nového políčka
                grid.add(cells[row][col].button, col, row);     // pridanie tlačidla do gridu
            }
        }
    }

    // Náhodné rozmiestnenie mín, s výnimkou kliknutého políčka (safeRow, safeCol)
    private void placeMines(int safeRow, int safeCol) {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            if ((r == safeRow && c == safeCol) || cells[r][c].isMine) {
                continue;
            }

            cells[r][c].isMine = true;
            placedMines++;
        }
    }

    // Výpočet počtu susedných mín pre každé ne-mínové políčko
    private void calculateAdjacents() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine) {
                    int adjacentMines = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int r = row + i;
                            int c = col + j;
                            if (r >= 0 && r < rows && c >= 0 && c < cols && cells[r][c].isMine) {
                                adjacentMines++;
                            }
                        }
                    }
                    cells[row][col].adjacentMines = adjacentMines;
                }
            }
        }
    }

    // Trieda reprezentujúca jedno políčko v hre
    private class Cell {
        int row, col;
        boolean isMine = false;
        boolean isRevealed = false;
        boolean isFlagged = false;
        int adjacentMines = 0;
        Button button = new Button();       // vizuálne zobrazenie políčka
        Image flag = new Image(Objects.requireNonNull(getClass().getResourceAsStream("flag.png")));
        ImageView flagImage = new ImageView(flag);

        Cell(int row, int col) {
            this.row = row;
            this.col = col;

            // Pevná veľkosť políčka (nezmení sa ani pri rozťahovaní okna)
            button.setMinSize(40, 40);
            button.setMaxSize(40,40);

            // Upravujeme veľkosť obrázka vlajky aby pasoval na tlačidlo
            flagImage.fitWidthProperty().bind(button.widthProperty());
            flagImage.fitHeightProperty().bind(button.heightProperty());
            flagImage.setPreserveRatio(true);

            // Štýl tlačidla
            button.setStyle(
                    "-fx-focus-color: transparent;" +
                            "-fx-faint-focus-color: transparent;" +
                            "-fx-background-color: #bbbbbb;" + // Farba políčok
                            "-fx-text-fill: #000000;"          // Farba textu na políčku
            );

            // Reakcia na kliknutie ľavým alebo pravým tlačidlom myši
            button.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    reveal();       // odkryť políčko
                    clickCount++;
                    updateClickCountLabel();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    toggleFlag();   // označiť vlajkou
                    clickCount++;
                    updateClickCountLabel();
                }
            });
        }

        // Odkrytie políčka (ľavý klik)
        void reveal() {
            if (isRevealed || isFlagged) return;    // Ak je políčko odkryté alebo zavlajkované, nezrob nič

            if (firstClick) {
                firstClick = false;
                placeMines(row, col);  // vytvor míny s výnimkou na tejto pozícii
                calculateAdjacents();
            }

            if (!timerStarted) {
                timerStarted = true;
                timeline.play();        // spustenie časovača
            }

            isRevealed = true;
            button.setDisable(true);
            if (isMine) {               // Ak je políčko mína
                showGameOverScreen("You Lose");   // Ukončíme hru
            } else {
                button.setText(adjacentMines > 0 ? String.valueOf(adjacentMines) : "");
                if (adjacentMines == 0) {
                    // Rekurzívne odkrytie susedov ak je 0 mín
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int r = row + i;
                            int c = col + j;
                            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                                cells[r][c].reveal();
                            }
                        }
                    }
                }

                winCheck();     // skontroluj výhru
            }
        }

        // Prepínanie vlajky (pravý klik)
        void toggleFlag() {
            if (isRevealed) return;     // Ak je odkryté, neurob nič

            // S isFlagged booleanom dávame alebo odstraňujeme obrázok vlajky
            isFlagged = !isFlagged;

            button.setGraphic(isFlagged ? flagImage : null);

            if (isFlagged) {
                unflaggedMines--;
            } else {
                unflaggedMines++;
            }
            updateUnflaggedMinesLabel();
        }

        // Kontrola výhry – ak sú všetky ne-minové políčka odkryté
        void winCheck() {
            int uncoveredSafeCells = 0;

            for (int row = 0; row < rows; row++) {
                for (int stlpec = 0; stlpec < cols; stlpec++) {
                    if (!cells[row][stlpec].isMine && cells[row][stlpec].isRevealed) {
                        uncoveredSafeCells++;
                    }
                }
            }

            int totalSafeCells = (rows * cols) - mines;

            if (uncoveredSafeCells == totalSafeCells) {
                showGameOverScreen("You Win");
            }
        }

    }

    // Aktualizácia textu počítadla neoznačených mín
    private void updateUnflaggedMinesLabel() {
        unflaggedMinesLabel.setText("Unflagged Mines: " + unflaggedMines);
    }

    // Aktualizácia textu počtu kliknutí
    private void updateClickCountLabel() {
        clickCountLabel.setText("Clicks: " + clickCount);
    }

    // Reset hry do pôvodného stavu
    private void resetGame() {
        if (timeline != null) {
            timeline.stop();
        }
        secondsElapsed = 0;
        timerLabel.setText("Time: 0");
        timerStarted = false;

        this.unflaggedMines = mines;
        this.clickCount = 0;
        updateUnflaggedMinesLabel();
        updateClickCountLabel();
        start(primaryStage);
    }

    // Zobrazenie výslednej obrazovky po výhre alebo prehre
    private void showGameOverScreen(String message) {
        GameOverScreen gameOverScreen = new GameOverScreen(primaryStage, rows, cols, mines, getTime(secondsElapsed), message);
        gameOverScreen.displayGameOverScreen();
    }

    // Formátovanie času v mm:ss
    public String getTime(int secondsElapsed) {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String sekundyText;

        if (seconds < 10) {sekundyText = "0" + seconds;}
        else {sekundyText = "" + seconds;}

        return minutes + ":" + sekundyText;
    }
}