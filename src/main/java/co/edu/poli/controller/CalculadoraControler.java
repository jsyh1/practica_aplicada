package co.edu.poli.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculadoraControler {

    @FXML
    private TextField txtValor1;

    @FXML
    private Label lblError;

    private String operador;

    @FXML
    private void seleccionarSuma() {
        operador = "+";
        mostrarOperador();
    }

    @FXML
    private void seleccionarResta() {
        operador = "-";
        mostrarOperador();
    }

    @FXML
    private void seleccionarMultiplicacion() {
        operador = "×";
        mostrarOperador();
    }

    @FXML
    private void seleccionarDivision() {
        operador = "÷";
        mostrarOperador();
    }

    private void mostrarOperador() {

        String texto = txtValor1.getText();

        // Validar que exista un número
        if (texto.isEmpty()) {
            lblError.setText("Debe ingresar un número antes de seleccionar un operador.");
            return;
        }

        // Quitar mensaje de error
        lblError.setText("");

        // Si ya existe un operador, reemplazarlo
        if (texto.endsWith("+") ||
            texto.endsWith("-") ||
            texto.endsWith("×") ||
            texto.endsWith("÷")) {

            texto = texto.substring(0, texto.length() - 1);
        }

        txtValor1.setText(texto + operador);

        // Colocar el cursor al final
        txtValor1.positionCaret(txtValor1.getText().length());
    }
}