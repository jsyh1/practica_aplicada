module co.edu.poli.juego {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.juego to javafx.fxml;
    opens co.edu.poli.controller to javafx.fxml;

    exports co.edu.poli.juego;
    exports co.edu.poli.controller;
}