package co.edu.poli.controller;

import java.io.IOException;

import co.edu.poli.vista.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReglasControler {

    @FXML
    private Label lblReglas;

    @FXML
    public void initialize() {

        lblReglas.setText(
            "Con los 4 números de la ronda, arma una expresión matemática "
            + "distinta para lograr cada resultado del 1 al 10.\n\n"
            + "Reglas:\n"
            + "• Usa cada uno de los 4 números exactamente una vez por expresión.\n"
            + "• Puedes usar +, -, *, ÷ y paréntesis.\n"
            + "• Se permiten fracciones y resultados negativos intermedios.\n"
            + "• Puedes unir dos números para formar uno de varias cifras "
            + "(ej: 1 y 2 → 12).\n\n"
            + "Completas la ronda cuando encuentres los 10 resultados "
            + "(1 al 10) usando siempre los mismos 4 números."
        );
    }

    @FXML
    private void calljuego() {

        try {
            App.setRoot("juego");
        } catch (IOException e) {
            System.out.println("No se pudo volver al juego: " + e.getMessage());
        }
    }
}