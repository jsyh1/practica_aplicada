
package co.edu.poli.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controlador de la ventana que se muestra al finalizar una partida.
 *
 * Permite cargar el nombre del jugador, consultar los resultados,
 * compartir el resumen y exportarlo a un archivo de texto.
 *
 * @author Jsyh
 * @version 1.0
 */
public class GameOverControler {

    @FXML
    private TextField txtNombre;

    @FXML
    private Label lblMensaje;

    @FXML
    private Label lblPuntaje;

    @FXML
    private Label lblTiempo;

    @FXML
    private Label lblResultados;

    @FXML
    private Button btnCargar;

    @FXML
    private Button btnCompartir;

    @FXML
    private Button btnExportar;

    private Partida partidaActual;

    /**
     * Recibe la partida que acaba de finalizar.
     *
     * @param partida partida finalizada
     */
    public void establecerPartida(Partida partida) {
        this.partidaActual = partida;

        if (partidaActual != null) {
            btnCompartir.setDisable(true);
            btnExportar.setDisable(true);
        }
    }

    
    @FXML
    private void cargarNombre() {
        if (partidaActual == null || partidaActual.getJugador() == null) {
            lblMensaje.setText("No se recibió la información del jugador.");
            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            lblMensaje.setText("Escribe el nombre del jugador.");
            return;
        }

        Jugador jugador = partidaActual.getJugador();

        if (jugador.getId() <= 0) {
            lblMensaje.setText("El jugador no tiene un ID válido.");
            return;
        }

        jugador.setNombre(nombre);

        DaoJugadorImplementado daoJugador = new DaoJugadorImplementado();

        if (!daoJugador.actualizar(jugador)) {
            lblMensaje.setText("No se pudo guardar el nombre en la base de datos.");
            return;
        }

        mostrarResultados();

        btnCompartir.setDisable(false);
        btnExportar.setDisable(false);
        btnCargar.setDisable(true);
        txtNombre.setEditable(false);

        lblMensaje.setText("Nombre guardado correctamente.");
    }
    


    /**
     * Presenta el puntaje, el tiempo y los resultados de la partida.
     */
    private void mostrarResultados() {
        lblPuntaje.setText(
            "Puntaje: " + partidaActual.getPuntaje()
        );

        lblTiempo.setText(
            "Tiempo de juego: "
            + partidaActual.getTiempoEjecucion()
            + " segundos"
        );

        List<?> resultados = partidaActual.getResultados();

        if (resultados == null || resultados.isEmpty()) {
            lblResultados.setText("No hay resultados registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < resultados.size(); i++) {
            texto.append(i + 1)
                 .append(". ")
                 .append(resultados.get(i))
                 .append(System.lineSeparator());
        }

        lblResultados.setText(texto.toString());
    }

    /**
     * Copia el resumen de la partida al portapapeles.
     */
    @FXML
    private void compartir() {
        if (partidaActual == null) {
            lblMensaje.setText("No hay una partida para compartir.");
            return;
        }

        String resumen = generarResumen();

        ClipboardContent contenido = new ClipboardContent();
        contenido.putString(resumen);

        Clipboard.getSystemClipboard().setContent(contenido);

        lblMensaje.setText("Resumen copiado al portapapeles.");
    }

    /**
     * Exporta el resumen de la partida a un archivo de texto.
     */
    @FXML
    private void exportar() {
        if (partidaActual == null) {
            lblMensaje.setText("No hay una partida para exportar.");
            return;
        }

        FileChooser selector = new FileChooser();
        selector.setTitle("Exportar resultados de la partida");
        selector.setInitialFileName("resultados_partida.txt");
        selector.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivo de texto", "*.txt")
        );

        Stage ventana = (Stage) btnExportar.getScene().getWindow();
        java.io.File archivo = selector.showSaveDialog(ventana);

        if (archivo == null) {
            lblMensaje.setText("Exportación cancelada.");
            return;
        }

        try {
            Files.writeString(Path.of(archivo.getAbsolutePath()), generarResumen());
            lblMensaje.setText("Resultados exportados correctamente.");
        } catch (IOException e) {
            lblMensaje.setText("No se pudo exportar el archivo.");
            e.printStackTrace();
        }
    }

    /**
     * Construye el resumen que se comparte o exporta.
     *
     * @return texto con el nombre, puntaje, tiempo y resultados
     */
    private String generarResumen() {
        String nombre = partidaActual.getJugador().getNombre();

        StringBuilder resumen = new StringBuilder();

        resumen.append("ALL TEN - RESUMEN DE PARTIDA")
               .append(System.lineSeparator())
               .append("Partida: ").append(partidaActual.getId())
               .append(System.lineSeparator())
               .append("Jugador: ").append(nombre)
               .append(System.lineSeparator())
               .append("Puntaje: ").append(partidaActual.getPuntaje())
               .append(System.lineSeparator())
               .append("Tiempo: ").append(partidaActual.getTiempoEjecucion())
               .append(" segundos")
               .append(System.lineSeparator())
               .append(System.lineSeparator())
               .append("Resultados:")
               .append(System.lineSeparator());

        List<?> resultados = partidaActual.getResultados();

        if (resultados == null || resultados.isEmpty()) {
            resumen.append("Sin resultados registrados.");
        } else {
            for (int i = 0; i < resultados.size(); i++) {
                resumen.append(i + 1)
                       .append(". ")
                       .append(resultados.get(i))
                       .append(System.lineSeparator());
            }
        }

        return resumen.toString();
    }
}