package co.edu.poli.controller;

import co.edu.poli.servicios.Fraccion;
import co.edu.poli.servicios.Operadores;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador principal de la calculadora.
 *
 * Gestiona la interacción entre la interfaz gráfica y los servicios
 * encargados de realizar las operaciones matemáticas.
 *
 * Permite:
 * - Ingresar números.
 * - Seleccionar operadores matemáticos.
 * - Ingresar paréntesis.
 * - Eliminar el último carácter ingresado.
 * - Calcular expresiones matemáticas.
 * - Mostrar resultados enteros o en forma de fracción.
 * - Mostrar mensajes de error.
 */
public class CalculadoraControler {

    /**
     * Campo de texto donde se ingresa la expresión matemática
     * y se muestra el resultado.
     */
    @FXML
    private TextField txtValor1;

    /**
     * Etiqueta utilizada para mostrar mensajes de error
     * al usuario.
     */
    @FXML
    private Label lblError;

    /**
     * Etiqueta destinada a mostrar el resultado de la operación.
     */
    @FXML
    private Label lblResultado;

    /**
     * Almacena temporalmente el operador seleccionado.
     */
    private String operador;

    // =========================
    // OPERADORES
    // =========================

    /**
     * Selecciona el operador de suma y lo agrega a la expresión.
     *
     * @param event evento generado al presionar el botón de suma
     */
    @FXML
    private void seleccionarSuma(ActionEvent event) {

        operador = "+";

        Button boton = (Button) event.getSource();

        String suma = boton.getText();

        lblError.setText("");

        agregarTexto(suma);
    }

    /**
     * Selecciona el operador de resta y lo agrega a la expresión.
     *
     * @param event evento generado al presionar el botón de resta
     */
    @FXML
    private void seleccionarResta(ActionEvent event) {

        operador = "-";

        Button boton = (Button) event.getSource();

        String resta = boton.getText();

        lblError.setText("");

        agregarTexto(resta);
    }

    /**
     * Selecciona el operador de multiplicación y lo agrega a la expresión.
     *
     * @param event evento generado al presionar el botón
     *              de multiplicación
     */
    @FXML
    private void seleccionarMultiplicacion(ActionEvent event) {

        operador = "*";

        Button boton = (Button) event.getSource();

        String multiplicacion = boton.getText();

        lblError.setText("");

        agregarTexto(multiplicacion);
    }

    /**
     * Selecciona el operador de división y lo agrega a la expresión.
     *
     * @param event evento generado al presionar el botón de división
     */
    @FXML
    private void seleccionarDivision(ActionEvent event) {

        operador = "÷";

        Button boton = (Button) event.getSource();

        String divicion = boton.getText();

        lblError.setText("");

        agregarTexto(divicion);
    }

    // =========================
    // PARENTESIS
    // =========================

    /**
     * Agrega un paréntesis de apertura a la expresión matemática.
     *
     * @param event evento generado al presionar el botón
     *              de paréntesis de apertura
     */
    @FXML
    private void seleccionarParentesisAbierto(ActionEvent event) {

        Button boton = (Button) event.getSource();

        String pAbierto = boton.getText();

        lblError.setText("");

        agregarTexto(pAbierto);
    }

    /**
     * Agrega un paréntesis de cierre a la expresión matemática.
     *
     * @param event evento generado al presionar el botón
     *              de paréntesis de cierre
     */
    @FXML
    private void seleccionarParentesisCerrado(ActionEvent event) {

        Button boton = (Button) event.getSource();

        String pCerrado = boton.getText();

        lblError.setText("");

        agregarTexto(pCerrado);
    }

    // =========================
    // AGREGAR TEXTO
    // =========================

    /**
     * Agrega un texto al final de la expresión matemática.
     *
     * Después de agregar el texto, posiciona el cursor al final
     * del contenido del campo de texto.
     *
     * @param texto texto que se desea agregar a la expresión
     */
    private void agregarTexto(String texto) {

        String actual = txtValor1.getText();

        txtValor1.setText(actual + texto);

        txtValor1.positionCaret(
                txtValor1.getText().length());
    }

    // =========================
    // NUMEROS
    // =========================

    /**
     * Obtiene el número del botón presionado y lo agrega
     * a la expresión matemática.
     *
     * @param event evento generado al presionar un botón numérico
     */
    @FXML
    private void seleccionarNumero(ActionEvent event) {

        Button boton = (Button) event.getSource();

        String numero = boton.getText();

        lblError.setText("");

        agregarTexto(numero);
    }

    // =========================
    // BORRAR
    // =========================

    /**
     * Elimina el último carácter de la expresión matemática.
     *
     * Si el campo de texto está vacío, no realiza ninguna acción.
     * También limpia cualquier mensaje de error mostrado.
     */
    @FXML
    private void borrarUltimo() {

        String actual = txtValor1.getText();

        if (!actual.isEmpty()) {

            txtValor1.setText(
                    actual.substring(0, actual.length() - 1));
        }

        lblError.setText("");
    }

    // =========================
    // CALCULAR
    // =========================

    /**
     * Calcula la expresión matemática ingresada por el usuario.
     *
     * Utiliza la clase {@link Operadores} para procesar la expresión
     * y obtener el resultado. Posteriormente utiliza la clase
     * {@link Fraccion} para convertir los resultados decimales
     * a su representación fraccionaria cuando sea necesario.
     *
     * Si la expresión contiene un error matemático o de sintaxis,
     * se muestra el mensaje correspondiente en la etiqueta de error.
     *
     * @throws IllegalArgumentException si la expresión matemática
     *                                  es inválida
     * @throws ArithmeticException      si se intenta realizar una
     *                                  división entre cero
     */
    @FXML
    private void resultado() {

        try {

            Operadores op = new Operadores(txtValor1.getText());

            double resultado = op.calcular();

            String str = Fraccion.convertir(resultado);

            txtValor1.setText(str);
            lblError.setText("");

        } catch (IllegalArgumentException | ArithmeticException e) {

            lblError.setText(e.getMessage());
        }
    }
}