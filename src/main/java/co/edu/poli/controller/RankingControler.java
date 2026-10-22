package co.edu.poli.controller;

import java.io.IOException;
import java.util.List;

import co.edu.poli.dao.DaoScoreImplementado;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.vista.App;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controlador de la pantalla de ranking de partidas.
 */
public class RankingControler {

    @FXML
    private VBox contenedorRanking;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        cargarRanking();
    }

    private void cargarRanking() {
        contenedorRanking.getChildren().clear();

        List<Partida> partidas = new DaoScoreImplementado().ranking(10);

        if (partidas.isEmpty()) {
            contenedorRanking.getChildren().add(new Label("No hay partidas completas para mostrar."));
            return;
        }

        for (int i = 0; i < partidas.size(); i++) {
            contenedorRanking.getChildren().add(crearFila(i + 1, partidas.get(i)));
        }
    }

    private HBox crearFila(int posicion, Partida partida) {
        Jugador jugador = partida.getJugador();
        String nombre = jugador != null && jugador.getNombre() != null && !jugador.getNombre().isBlank()
                ? jugador.getNombre()
                : "Sin nombre";

        Label puesto = new Label("#" + posicion);
        puesto.getStyleClass().add("nombre-jugador");

        Label nombreJugador = new Label(nombre);
        nombreJugador.getStyleClass().add("nombre-jugador");

        Label datosPartida = new Label(
                "Puntaje: " + partida.getPuntaje() + "/10 - Tiempo: " + partida.getTiempoEjecucion() + " s");

        VBox detalles = new VBox(4, nombreJugador, datosPartida);
        HBox.setHgrow(detalles, Priority.ALWAYS);

        Region espacio = new Region();
        HBox.setHgrow(espacio, Priority.ALWAYS);

        HBox fila = new HBox(12, puesto, detalles, espacio);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.getStyleClass().add("fila-partida");
        return fila;
    }

    @FXML
    private void volverConsulta() {
        try {
            App.setRoot("consulta");
        } catch (IOException e) {
            lblMensaje.setText("No se pudo volver a la consulta de partidas.");
            e.printStackTrace();
        }
    }
}
