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

public class Main extends Application {
    private int rows;
    private int cols;
    private int mines;
    private int unflaggedMines;
    private int clickCount;
    private Cell[][] cells;
    private Stage primaryStage;
    private Label unflaggedMinesLabel;
    private Label clickCountLabel;
    private Button resetButton;

    //premenné pre prácu s časom
    private Label timerLabel;
    private javafx.animation.Timeline timeline; //vybral som timeline pre jeho jednoduchosť a praktickosť v tomto projekte
    private int secondsElapsed;
    private boolean timerStarted = false;


    public Main() {
        this(10, 10, 10); // Základné hodnoty
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
        GridPane grid = new GridPane();
        initializeCells(grid);

        // inicializacia vrchného baru
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
        double sceneWidth = 40 * cols + 20; // dodatočný priestor na padding
        double sceneHeight = 40 * rows + 100; // dodatočný priestor na top bar a padding

        setupTimer();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Minesweeper");
        primaryStage.show();
    }

    private void setupTimer() {
        timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                    secondsElapsed++;
                    timerLabel.setText("Time: " + getTime(secondsElapsed));
                })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
    }


    // metóda na inicializáciu- pridanie mín a prázdnych políčok
    private void initializeCells(GridPane grid) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
                grid.add(cells[row][col].button, col, row);
            }
        }
        placeMines();
        calculateAdjacents();
    }

    private void placeMines() {
        int placedMines = 0;
        while (placedMines < mines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if (!cells[row][col].isMine) {
                cells[row][col].isMine = true;
                placedMines++;
            }
        }
    }

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

    private class Cell {
        int row, col;
        boolean isMine = false;
        boolean isRevealed = false;
        boolean isFlagged = false;
        int adjacentMines = 0;
        Button button = new Button();
        Image flag = new Image(Objects.requireNonNull(getClass().getResourceAsStream("flag.png")));
        ImageView flagImage = new ImageView(flag);

        Cell(int row, int col) {
            this.row = row;
            this.col = col;

            // Nastavený aj min size aj max aby sa nemenila veľkosť políčok, neodstraňuj
            button.setMinSize(40, 40);
            button.setMaxSize(40,40);

            // Upravujeme veľkosť obrázka vlajky aby pasoval na tlačidlo
            flagImage.fitWidthProperty().bind(button.widthProperty());
            flagImage.fitHeightProperty().bind(button.heightProperty());
            flagImage.setPreserveRatio(true);

            button.setStyle(
                    "-fx-focus-color: transparent;" +
                            "-fx-faint-focus-color: transparent;" +
                            "-fx-background-color: #bbbbbb;" + // Farba políčok
                            "-fx-text-fill: #000000;"          // Farba textu na políčku
            );

            // Keď klikneme na políčko
            button.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    reveal();       // odkrytie
                    clickCount++;
                    updateClickCountLabel();
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    toggleFlag();   // vlajka
                    clickCount++;
                    updateClickCountLabel();
                }
            });
        }

        void reveal() {                 // Kliknutie s ľavým tlačidlom na políčko
            if (isRevealed || isFlagged) return;    // Ak je políčko odkryté alebo zavlajkované, nezrob nič

            if (!timerStarted) {
                timerStarted = true;
                timeline.play();
            }


            isRevealed = true;
            button.setDisable(true);
            if (isMine) {               // Ak je políčko mína
                showGameOverScreen("You Lose");   // Ukončíme hru
            } else {
                button.setText(adjacentMines > 0 ? String.valueOf(adjacentMines) : "");
                if (adjacentMines == 0) {
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

                winCheck();
            }
        }

        void toggleFlag() {             // Kliknutie s pravým tlačidlom na políčko
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

    private void updateUnflaggedMinesLabel() {
        unflaggedMinesLabel.setText("Unflagged Mines: " + unflaggedMines);
    }

    private void updateClickCountLabel() {
        clickCountLabel.setText("Clicks: " + clickCount);
    }

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

    private void showGameOverScreen(String message) {
        GameOverScreen gameOverScreen = new GameOverScreen(primaryStage, rows, cols, mines, getTime(secondsElapsed), message);
        gameOverScreen.displayGameOverScreen();
    }

    public String getTime(int secondsElapsed) {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String sekundyText;

        if (seconds < 10) {sekundyText = "0" + seconds;}
        else {sekundyText = "" + seconds;}

        return minutes + ":" + sekundyText;
    }
}