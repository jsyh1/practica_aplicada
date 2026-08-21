package co.edu.poli.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CalculadoraControler {

    @FXML
    private TextField txtValor1;

    @FXML
    private Label lblError;

    private String operador;



    // =========================
    // OPERADORES
    // =========================

    @FXML
    private void seleccionarSuma() {
        operador = "+";
        //mostrarOperador();
    }

    @FXML
    private void seleccionarResta() {
        operador = "-";
       // mostrarOperador();
    }

    @FXML
    private void seleccionarMultiplicacion() {
        operador = "×";
       // mostrarOperador();
    }

    @FXML
    private void seleccionarDivision() {
        operador = "÷";
        //mostrarOperador();
    }

    // =========================
    // PARENTESIS
    // =========================

    @FXML
    private void seleccionarParentesisAbierto() {

//        String expresion = txtValor1.getText();
//
//        // Puede abrir al inicio
//        if (expresion.isEmpty()) {
//            lblError.setText("");
//            agregarTexto("(");
//            return;
//        }
//
//        // Puede abrir después de un operador
//        if (terminaEnOperador()) {
//            lblError.setText("");
//            agregarTexto("(");
//            return;
//        }
//
//        // Puede abrir otro paréntesis después de '('
//        if (expresion.endsWith("(")) {
//            lblError.setText("");
//            agregarTexto("(");
//            return;
//        }
//
//        // No puede abrir después de un número
//        // ni después de un paréntesis cerrado
//        lblError.setText(
//            "No se puede abrir un paréntesis en esta posición."
//        );
    }

    @FXML
    private void seleccionarParentesisCerrado() {
//
//        String expresion = txtValor1.getText();
//
//        // Debe existir un paréntesis abierto
//        if (!parentesisValidos()) {
//            lblError.setText(
//                "No hay un paréntesis abierto para cerrar."
//            );
//            return;
//        }
//
//        // Debe haber un número o ')' antes
//        if (!terminaEnNumero()) {
//            lblError.setText(
//                "El paréntesis no puede cerrarse en esta posición."
//            );
//            return;
//        }
//
//        lblError.setText("");
//        agregarTexto(")");
    }

    // =========================
    // AGREGAR TEXTO
    // =========================

//    private void agregarTexto(String texto) {
//
//        String actual = txtValor1.getText();
//
//        txtValor1.setText(actual + texto);
//
//        txtValor1.positionCaret(
//            txtValor1.getText().length()
//        );
//    }

    // =========================
    // NUMEROS 
    // =========================
    @FXML
    private void seleccionarNumero(ActionEvent event) {

//    Button boton = (Button) event.getSource();
//
//    String numero = boton.getText();
//
//    lblError.setText("");
//
//    agregarTexto(numero);
    }

    // =========================
    // MOSTRAR OPERADOR
    // =========================

//    private void mostrarOperador() {
//
//        String texto = txtValor1.getText();
//
//        // No se puede seleccionar un operador
//        // si la expresión está vacía
//        if (texto.isEmpty()) {
//
//            lblError.setText(
//                "Debe ingresar un número antes de seleccionar un operador."
//            );
//
//            return;
//        }
//
//        // No se puede poner operador después de otro operador
//        if (terminaEnOperador()) {
//
//            lblError.setText(
//                "No se pueden colocar dos operadores seguidos."
//            );
//
//            return;
//        }
//
//        // No se puede poner operador inmediatamente
//        // después de un paréntesis abierto
//        if (texto.endsWith("(")) {
//
//            lblError.setText(
//                "Debe ingresar un número después del paréntesis."
//            );
//
//            return;
//        }
//
//        lblError.setText("");
//
//        agregarTexto(operador);
//   }

    // =========================
    // VALIDAR OPERADOR
    // =========================
//
//    private boolean terminaEnOperador() {
//
//        String expresion = txtValor1.getText();
//
//        if (expresion.isEmpty()) {
//            return false;
//        }
//
//        char ultimo = expresion.charAt(
//            expresion.length() - 1
//        );
//
//        return ultimo == '+'
//            || ultimo == '-'
//            || ultimo == '×'
//            || ultimo == '÷';
//    }

    // =========================
    // VALIDAR NUMERO O PARENTESIS
    // =========================

//    private boolean terminaEnNumero() {
//
//        String expresion = txtValor1.getText();
//
//        if (expresion.isEmpty()) {
//            return false;
//        }
//
//        char ultimo = expresion.charAt(
//            expresion.length() - 1
//        );
//
//        return Character.isDigit(ultimo)
//            || ultimo == ')';
//    }

    // =========================
    // VALIDAR PARENTESIS
    // =========================

//    private boolean parentesisValidos() {
//
//        int abiertos = 0;
//        int cerrados = 0;
//
//        String expresion = txtValor1.getText();
//
//        for (int i = 0; i < expresion.length(); i++) {
//
//            if (expresion.charAt(i) == '(') {
//                abiertos++;
//            }
//
//            if (expresion.charAt(i) == ')') {
//                cerrados++;
//            }
//        }
//
//        return abiertos > cerrados;
//    }
}