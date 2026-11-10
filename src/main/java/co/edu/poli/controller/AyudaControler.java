package co.edu.poli.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.vista.App;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Controlador de la pantalla que muestra las ecuaciones de ayuda. */
public class AyudaControler {

    /*
     * Ecuaciones de la partida en curso. Las entrega CalculadoraControler
     * justo antes de abrir esta pantalla.
     */
    private static List<String> ecuaciones = new ArrayList<>();

    public static void setEcuaciones(List<String> lista) {
        ecuaciones = lista == null ? new ArrayList<>() : new ArrayList<>(lista);
    }

    @FXML
    private VBox contenedorEcuaciones;

    @FXML
    public void initialize() {
        for (int i = 0; i < ecuaciones.size(); i++) {
            String[] partes = ecuaciones.get(i).split(" = ", 2);
            if (partes.length == 2) {
                contenedorEcuaciones.getChildren().add(
                        crearFila(partes[0], partes[1], i)
                );
            }
        }
    }

    private HBox crearFila(String expresion, String resultado, int indice) {
        HBox fila = new HBox();
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.getStyleClass().add("fila-ayuda");
        if (indice % 2 == 1) {
            fila.getStyleClass().add("fila-ayuda-alterna");
        }

        StackPane celdaResultado = new StackPane();
        celdaResultado.getStyleClass().add("celda-resultado-ayuda");

        Label numeroResultado = new Label(resultado);
        numeroResultado.getStyleClass().add("numero-resultado-ayuda");
        celdaResultado.getChildren().add(numeroResultado);

        Label textoExpresion = new Label(expresion);
        textoExpresion.getStyleClass().add("expresion-ayuda");
        textoExpresion.setWrapText(true);
        textoExpresion.setMaxWidth(Double.MAX_VALUE);
        textoExpresion.setMinWidth(0);
        textoExpresion.setPadding(new Insets(10, 8, 10, 8));
        HBox.setHgrow(textoExpresion, Priority.ALWAYS);

        fila.getChildren().addAll(celdaResultado, textoExpresion);
        return fila;
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
