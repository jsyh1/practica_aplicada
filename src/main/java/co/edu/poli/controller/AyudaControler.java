package co.edu.poli.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.vista.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AyudaControler {

    /*
     * Ecuaciones de la partida en curso. Las entrega CalculadoraControler
     * justo antes de abrir esta pantalla.
     */
    private static List<String> ecuaciones = new ArrayList<>();

    public static void setEcuaciones(List<String> lista) {
        ecuaciones = new ArrayList<>(lista);
    }

    @FXML
    private TableView<String[]> tablaEcuaciones;

    @FXML
    private TableColumn<String[], String> colResultado;

    @FXML
    private TableColumn<String[], String> colExpresion;

    @FXML
    public void initialize() {

        colResultado.setCellValueFactory(
            dato -> new SimpleStringProperty(dato.getValue()[1])
        );
        colResultado.setCellFactory(columna -> new TableCell<>() {
            private final Label resultado = new Label();

            {
                resultado.getStyleClass().add("numero-resultado-ayuda");
                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(String valor, boolean vacio) {
                super.updateItem(valor, vacio);
                if (vacio || valor == null) {
                    setGraphic(null);
                } else {
                    resultado.setText(valor);
                    setGraphic(resultado);
                }
            }
        });

        colExpresion.setCellValueFactory(
            dato -> new SimpleStringProperty(dato.getValue()[0])
        );

        tablaEcuaciones.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );
        colResultado.setMinWidth(70);
        colResultado.setPrefWidth(85);
        colExpresion.setMinWidth(150);

        for (String ecuacion : ecuaciones) {

            String[] partes = ecuacion.split(" = ");

            if (partes.length == 2) {
                tablaEcuaciones.getItems().add(partes);
            }
        }
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