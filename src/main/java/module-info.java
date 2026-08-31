module co.edu.poli.juego {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.vista to javafx.fxml;
    opens co.edu.poli.controller to javafx.fxml;

    exports co.edu.poli.vista;
    exports co.edu.poli.controller;
}