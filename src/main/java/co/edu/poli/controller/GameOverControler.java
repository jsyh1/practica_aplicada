
package co.edu.poli.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoResultadoImplementado;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.vista.App;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

	@FXML
	private Button btnVolver;

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
	/**
	 * Exporta el resumen de la partida a una imagen PNG.
	 */
	@FXML
	private void exportar() {

		if (partidaActual == null) {
			lblMensaje.setText("No hay una partida para exportar.");
			return;
		}

		FileChooser selector = new FileChooser();
		selector.setTitle("Exportar resultados de la partida");
		selector.setInitialFileName("resultados_partida.png");

		selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen PNG", "*.png"));

		Stage ventana = (Stage) btnExportar.getScene().getWindow();

		File archivo = selector.showSaveDialog(ventana);

		if (archivo == null) {
			lblMensaje.setText("Exportación cancelada.");
			return;
		}

		// Garantiza que el archivo termine en .png.
		if (!archivo.getName().toLowerCase(Locale.ROOT).endsWith(".png")) {
			archivo = new File(archivo.getParentFile(), archivo.getName() + ".png");
		}

		try {
			WritableImage imagen = crearReportePNG();

			boolean guardado = ImageIO.write(SwingFXUtils.fromFXImage(imagen, null), "png", archivo);

			if (!guardado) {
				throw new IOException("No se encontró un codificador PNG disponible.");
			}

			lblMensaje.setText("Imagen PNG exportada correctamente.");

		} catch (IOException | RuntimeException e) {
			lblMensaje.setText("No se pudo exportar la imagen PNG.");
			e.printStackTrace();
		}
	}

	/**
	 * Construye una vista independiente del informe y la convierte en una imagen
	 * que puede guardarse como PNG.
	 *
	 * @return imagen PNG preparada para exportar
	 */
	private WritableImage crearReportePNG() {

		String nombreJugador = "Sin nombre";

		if (partidaActual.getJugador() != null && partidaActual.getJugador().getNombre() != null
				&& !partidaActual.getJugador().getNombre().isBlank()) {

			nombreJugador = partidaActual.getJugador().getNombre();
		}

		// Contenedor principal del informe.
		VBox reporte = new VBox(16);
		reporte.setPadding(new Insets(32));
		reporte.setPrefWidth(820);
		reporte.setStyle("-fx-background-color: #f1f5f9;" + "-fx-font-family: 'Arial';");

		// Encabezado.
		Label marca = new Label("ALL TEN");
		marca.setStyle("-fx-text-fill: #64748b;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;");

		Label titulo = new Label("Resumen de partida");
		titulo.setStyle("-fx-text-fill: #182235;" + "-fx-font-size: 28px;" + "-fx-font-weight: bold;");

		// Tarjetas con los datos de la partida.
		HBox datos = new HBox(12);
		datos.setAlignment(Pos.CENTER_LEFT);

		datos.getChildren().addAll(crearTarjetaDato("JUGADOR", nombreJugador),
				crearTarjetaDato("PARTIDA", "#" + partidaActual.getId()),
				crearTarjetaDato("PUNTAJE", partidaActual.getPuntaje() + "/10"),
				crearTarjetaDato("TIEMPO", partidaActual.getTiempoEjecucion() + " segundos"));

		Label tituloResultados = new Label("Resultados obtenidos");
		tituloResultados.setStyle("-fx-text-fill: #182235;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;");

		// Agrega todas las ecuaciones, no solamente las visibles
		// en el TextArea de la ventana.
		VBox listaResultados = new VBox(8);

		if (resultadosActuales == null || resultadosActuales.isEmpty()) {

			Label vacio = new Label("No se encontraron resultados para esta partida.");

			vacio.setWrapText(true);
			listaResultados.getChildren().add(vacio);

		} else {

			for (int i = 0; i < resultadosActuales.size(); i++) {

				Resultado resultado = resultadosActuales.get(i);

				Label ecuacion = new Label((i + 1) + ". " + resultado.getResultado() + " = " + resultado.getDato());

				ecuacion.setWrapText(true);
				ecuacion.setMaxWidth(Double.MAX_VALUE);
				ecuacion.setPadding(new Insets(12));

				ecuacion.setStyle("-fx-background-color: white;" + "-fx-text-fill: #26354d;" + "-fx-font-size: 14px;"
						+ "-fx-background-radius: 6;" + "-fx-border-color: #e2e8f0;" + "-fx-border-radius: 6;");

				listaResultados.getChildren().add(ecuacion);
			}
		}

		Label pie = new Label("Informe generado por All Ten");
		pie.setStyle("-fx-text-fill: #64748b;" + "-fx-font-size: 11px;");

		reporte.getChildren().addAll(marca, titulo, new Separator(), datos, tituloResultados, listaResultados, pie);

		// Crea una escena temporal para que JavaFX pueda aplicar
		// los estilos y calcular las dimensiones de la vista.
		Scene escenaTemporal = new Scene(reporte);

		reporte.applyCss();

		double ancho = 820;
		double alto = Math.ceil(reporte.prefHeight(ancho));

		reporte.resize(ancho, alto);
		escenaTemporal.getRoot().applyCss();
		reporte.layout();

		WritableImage imagen = new WritableImage((int) Math.ceil(ancho), (int) Math.ceil(alto));

		reporte.snapshot(new SnapshotParameters(), imagen);

		return imagen;
	}

	/**
	 * Vuelve al juego, cerrando la pantalla de fin de partida mediante la
	 * sustitución de la vista actual por una nueva.
	 */
	@FXML
	private void volverAJugar() {
		try {
	        // Obtener la ventana actual de Game Over.
	        Stage ventanaGameOver = (Stage) btnVolver.getScene().getWindow();

	        // Cerrar Game Over.
	        ventanaGameOver.close();
			App.reiniciarJuego();
		} catch (IOException e) {
			lblMensaje.setText("No se pudo iniciar una nueva partida.");
			e.printStackTrace();
		}
	}

	/**
	 * Construye una tarjeta visual para un dato de la partida.
	 *
	 * @param etiqueta nombre del dato
	 * @param valor    valor que se mostrará
	 * @return tarjeta visual
	 */
	private VBox crearTarjetaDato(String etiqueta, String valor) {

		Label lblEtiqueta = new Label(etiqueta);

		lblEtiqueta.setStyle("-fx-text-fill: #64748b;" + "-fx-font-size: 10px;" + "-fx-font-weight: bold;");

		Label lblValor = new Label(valor);
		lblValor.setWrapText(true);

		lblValor.setStyle("-fx-text-fill: #182235;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;");

		VBox tarjeta = new VBox(6, lblEtiqueta, lblValor);

		tarjeta.setPadding(new Insets(12));
		tarjeta.setPrefWidth(170);
		tarjeta.setMinWidth(150);
		tarjeta.setMaxWidth(170);

		tarjeta.setStyle("-fx-background-color: white;" + "-fx-background-radius: 8;" + "-fx-border-color: #e2e8f0;"
				+ "-fx-border-radius: 8;");

		return tarjeta;
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
