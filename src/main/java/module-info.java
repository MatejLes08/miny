module com.example.minesweeperproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.minesweeperproject to javafx.fxml;
    exports com.example.minesweeperproject;
}