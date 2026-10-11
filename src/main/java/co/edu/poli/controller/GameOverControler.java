
package co.edu.poli.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoResultadoImplementado;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controlador de la ventana que se muestra al finalizar una partida.
 *
 * Permite guardar el nombre del jugador, consultar las ecuaciones registradas,
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
	private TextArea lblResultados;

	@FXML
	private Button btnCargar;

	@FXML
	private Button btnCompartir;

	@FXML
	private Button btnExportar;

	private Partida partidaActual;

	private Resultado resultadoActual;

	private List<Resultado> resultadosActuales = new ArrayList<>();

	/**
	 * Recibe la partida finalizada y consulta sus resultados asociados en la base
	 * de datos.
	 *
	 * @param partida partida que acaba de finalizar
	 */
	public void establecerPartida(Partida partida) {
		this.partidaActual = partida;
		this.resultadosActuales.clear();

		if (partidaActual == null) {
			return;
		}

		DaoResultadoImplementado daoResultado = new DaoResultadoImplementado();

		resultadosActuales = daoResultado.listarPorPartida(partidaActual);

		btnCompartir.setDisable(true);
		btnExportar.setDisable(true);

		lblPuntaje.setText("Puntaje: " + partidaActual.getPuntaje() + "/10");

		lblTiempo.setText("Tiempo de juego: " + partidaActual.getTiempoEjecucion() + " segundos");

		lblResultados.setText(construirTextoResultados());

		System.out.println("Partida consultada: " + partidaActual.getId());

		System.out.println("Resultados cargados: " + resultadosActuales.size());
	}

	/**
	 * Valida y guarda el nombre del jugador.
	 */
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
	 * Presenta el puntaje, el tiempo y las ecuaciones de la partida.
	 */
	private void mostrarResultados() {

		lblPuntaje.setText("Puntaje: " + partidaActual.getPuntaje() + "/10");

		lblTiempo.setText("Tiempo de juego: " + partidaActual.getTiempoEjecucion() + " segundos");

		lblResultados.setText(construirTextoResultados());
	}

	/**
	 * Construye el listado de ecuaciones y sus valores.
	 *
	 * @return texto con los resultados de la partida
	 */
	private String construirTextoResultados() {

		if (resultadosActuales == null || resultadosActuales.isEmpty()) {
			return "No se encontraron resultados para esta partida.";
		}

		StringBuilder texto = new StringBuilder();

		for (int i = 0; i < resultadosActuales.size(); i++) {

			resultadoActual = resultadosActuales.get(i);

			texto.append(i + 1).append(". ").append(resultadoActual.getResultado()).append(" = ")
					.append(resultadoActual.getDato()).append(System.lineSeparator());
		}

		return texto.toString();
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

		selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));

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
	 * Construye el resumen completo de la partida.
	 *
	 * @return texto con el jugador, puntaje, tiempo y ecuaciones
	 */
	private String generarResumen() {

		String nombre = "Sin nombre";

		if (partidaActual.getJugador() != null && partidaActual.getJugador().getNombre() != null
				&& !partidaActual.getJugador().getNombre().isBlank()) {
			nombre = partidaActual.getJugador().getNombre();
		}

		StringBuilder resumen = new StringBuilder();

		resumen.append("ALL TEN - RESUMEN DE PARTIDA").append(System.lineSeparator())
				.append("--------------------------------").append(System.lineSeparator()).append("Partida: ")
				.append(partidaActual.getId()).append(System.lineSeparator()).append("Jugador: ").append(nombre)
				.append(System.lineSeparator()).append("Puntaje: ").append(partidaActual.getPuntaje()).append("/10")
				.append(System.lineSeparator()).append("Tiempo: ").append(partidaActual.getTiempoEjecucion())
				.append(" segundos").append(System.lineSeparator()).append(System.lineSeparator()).append("Resultados:")
				.append(System.lineSeparator());

		resumen.append(construirTextoResultados());

		return resumen.toString();
	}
}
