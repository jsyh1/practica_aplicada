package co.edu.poli.controller;

import java.io.IOException;
import java.util.List;

import co.edu.poli.dao.DaoScoreImplementado;
import co.edu.poli.modelo.Partida;
import co.edu.poli.vista.App;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConsultarControler {

    @FXML
    private VBox contenedorPartidas;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {

        DaoScoreImplementado dao = new DaoScoreImplementado();
        List<Partida> partidas = dao.ultimasPartidas(5);
        if (partidas.isEmpty()) {

            contenedorPartidas.getChildren().add(
                new Label("No hay partidas registradas.")
            );

        } else {

            for (Partida p : partidas) {

                contenedorPartidas.getChildren().add(crearFila(p));
            }
        }
    }

    private VBox crearFila(Partida p) {

        VBox fila = new VBox(6);
        fila.setStyle(
            "-fx-background-color: #d9d9d9; -fx-padding: 10; "
            + "-fx-background-radius: 8;"
        );

        Label jugador = new Label("Jugador #" + p.getJugador().getId());
        Label fecha = new Label("Fecha: " + p.getFechaPartida());
        Label puntaje = new Label("Puntaje: " + p.getPuntaje());
        Label tiempo = new Label("Tiempo: " + p.getTiempoEjecucion() + " segundos");

        String resultadosTexto = p.getResultados().isEmpty()
            ? "Sin resultados"
            : p.getResultados().size() + " resultado(s)";

        Label resultados = new Label("Resultados: " + resultadosTexto);

        Button btnCompartir = new Button("Compartir");
        btnCompartir.setOnAction(e -> compartir(p));

        HBox filaBoton = new HBox(btnCompartir);
        filaBoton.setAlignment(Pos.CENTER_RIGHT);

        fila.getChildren().addAll(
            jugador, fecha, puntaje, tiempo, resultados, filaBoton
        );

        return fila;
    }

    private void compartir(Partida p) {

        if (p.getResultados().isEmpty()) {

            lblMensaje.setText("No hay información para compartir");
            return;
        }

        String mensaje = "Partida #" + p.getId()
            + " — Jugador #" + p.getJugador().getId()
            + " — Puntaje: " + p.getPuntaje()
            + " — Tiempo: " + p.getTiempoEjecucion() + "s";

        ClipboardContent contenido = new ClipboardContent();
        contenido.putString(mensaje);

        Clipboard.getSystemClipboard().setContent(contenido);

        lblMensaje.setText("Información preparada para compartir");
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
