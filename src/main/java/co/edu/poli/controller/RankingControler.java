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

        List<Partida> partidas = new DaoScoreImplementado().ranking(5);

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

        Label puesto = new Label(String.valueOf(posicion));
        puesto.getStyleClass().addAll("medalla-ranking", "medalla-ranking-" + posicion);

        Label nombreJugador = new Label(nombre);
        nombreJugador.getStyleClass().add("ranking-jugador");

        Label fechaPartida = new Label(partida.getFechaPartida() != null
                ? partida.getFechaPartida().toString()
                : "Fecha no disponible");
        fechaPartida.getStyleClass().add("ranking-fecha");

        Label tiempo = new Label(partida.getTiempoEjecucion() + " s");
        tiempo.getStyleClass().add("ranking-tiempo");

        Label puntaje = new Label(partida.getPuntaje() + "/10");
        puntaje.getStyleClass().add("ranking-puntaje");

        VBox detalles = new VBox(1, nombreJugador, fechaPartida);
        HBox.setHgrow(detalles, Priority.ALWAYS);

        VBox estadisticas = new VBox(1, tiempo, puntaje);
        estadisticas.setAlignment(Pos.CENTER_RIGHT);

        HBox fila = new HBox(12, puesto, detalles, estadisticas);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.getStyleClass().add("tarjeta-ranking");
        if (posicion == 1) {
            fila.getStyleClass().add("tarjeta-ranking-primero");
        }
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
