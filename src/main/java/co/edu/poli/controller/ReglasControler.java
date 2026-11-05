package co.edu.poli.controller;

import java.io.IOException;

import co.edu.poli.vista.App;
import javafx.fxml.FXML;

public class ReglasControler {

    @FXML
    private void calljuego() {

        try {
            App.setRoot("juego");
        } catch (IOException e) {
            System.out.println("No se pudo volver al juego: " + e.getMessage());
        }
    }
}