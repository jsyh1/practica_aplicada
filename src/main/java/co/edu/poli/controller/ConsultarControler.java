
package co.edu.poli.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoResultadoImplementado;
import co.edu.poli.dao.DaoScoreImplementado;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controlador encargado de consultar las partidas registradas, compartir sus
 * datos y exportar sus resultados a PNG.
 *
 * @author Jsyh
 * @version 1.0
 */
public class ConsultarControler {

	@FXML
	private VBox contenedorPartidas;

	@FXML
	private Label lblMensaje;

	/**
	 * Carga las últimas partidas registradas.
	 */
	@FXML
	public void initialize() {
		cargarPartidas();
	}

	private void cargarPartidas() {
		contenedorPartidas.getChildren().clear();

		DaoScoreImplementado dao = new DaoScoreImplementado();
		List<Partida> partidas = dao.ultimasPartidas(5);

		if (partidas == null || partidas.isEmpty()) {
			contenedorPartidas.getChildren().add(new Label("No hay partidas registradas."));
			return;
		}

		for (Partida partida : partidas) {
			contenedorPartidas.getChildren().add(crearFila(partida));
		}
	}

	/**
	 * Construye la fila de una partida del historial.
	 *
	 * @param partida partida que se mostrará
	 * @return contenedor visual de la partida
	 */
	private VBox crearFila(Partida partida) {
		VBox fila = new VBox(6);
		fila.getStyleClass().add("fila-partida");

		Jugador jugadorPartida = partida.getJugador();

		String nombre = obtenerNombreJugador(partida);

		Label jugador = new Label(
				"Jugador: " + nombre + (jugadorPartida != null ? " (#" + jugadorPartida.getId() + ")" : ""));
		jugador.getStyleClass().add("nombre-jugador");

		Label fecha = new Label(
				partida.getFechaPartida() != null ? partida.getFechaPartida().toString() : "Fecha no disponible");
		fecha.getStyleClass().add("fecha-partida");

		HBox encabezado = new HBox(10, jugador, crearEspacio(), fecha);
		encabezado.setAlignment(Pos.CENTER_LEFT);

		Label infoLinea = new Label(
				"Puntaje: " + partida.getPuntaje() + "/10 - Tiempo: " + partida.getTiempoEjecucion() + " s");

		String resultadosTexto;
		try {
			List<Resultado> resultados = cargarResultados(partida);
			resultadosTexto = resultados.isEmpty() ? "Sin resultados" : resultados.size() + " resultado(s)";
		} catch (RuntimeException e) {
			resultadosTexto = "No se pudieron consultar los resultados";
		}

		Label lblResultados = new Label("Resultados: " + resultadosTexto);

		VBox columnaTexto = new VBox(4, encabezado, infoLinea, lblResultados);
		HBox.setHgrow(columnaTexto, Priority.ALWAYS);

		Button btnCompartir = new Button("Compartir");
		btnCompartir.getStyleClass().add("btn-compartir");
		btnCompartir.setOnAction(e -> compartir(partida));

		Button btnExportar = new Button("Exportar PNG");
		btnExportar.setOnAction(e -> exportarPNG(partida));

		HBox botones = new HBox(8, btnCompartir, btnExportar);
		botones.setAlignment(Pos.CENTER_RIGHT);

		HBox filaCompleta = new HBox(12, columnaTexto, botones);
		filaCompleta.setAlignment(Pos.CENTER_LEFT);

		fila.getChildren().add(filaCompleta);
		return fila;
	}

	private Region crearEspacio() {
		Region espacio = new Region();
		HBox.setHgrow(espacio, Priority.ALWAYS);
		return espacio;
	}

	/**
	 * Obtiene el nombre actualizado del jugador desde la base de datos.
	 *
	 * @param partida partida cuyo jugador se desea consultar
	 * @return nombre del jugador o "Sin nombre" si no está disponible
	 */
	private String obtenerNombreJugador(Partida partida) {

		if (partida == null || partida.getJugador() == null) {
			return "Sin nombre";
		}

		int idJugador = partida.getJugador().getId();

		if (idJugador > 0) {
			DaoJugadorImplementado daoJugador = new DaoJugadorImplementado();

			Jugador jugadorBD = daoJugador.buscarPorId(idJugador);

			if (jugadorBD != null && jugadorBD.getNombre() != null && !jugadorBD.getNombre().isBlank()) {

				return jugadorBD.getNombre().trim();
			}
		}

		// Alternativa si la consulta no encuentra al jugador.
		String nombreActual = partida.getJugador().getNombre();

		if (nombreActual != null && !nombreActual.isBlank()) {
			return nombreActual.trim();
		}

		return "Sin nombre";
	}

	/**
	 * Consulta las ecuaciones asociadas a una partida.
	 *
	 * @param partida partida que se consulta
	 * @return resultados almacenados
	 */
	private List<Resultado> cargarResultados(Partida partida) {
		DaoResultadoImplementado daoResultado = new DaoResultadoImplementado();

		List<Resultado> resultados = daoResultado.listarPorPartida(partida);

		return resultados != null ? resultados : List.of();
	}

	/**
	 * Copia al portapapeles un resumen de la partida.
	 *
	 * @param partida partida que se compartirá
	 */
	private void compartir(Partida partida) {
		if (partida == null) {
			lblMensaje.setText("No hay una partida para compartir.");
			return;
		}

		StringBuilder mensaje = new StringBuilder();

		mensaje.append("ALL TEN - RESUMEN DE PARTIDA").append(System.lineSeparator()).append("Partida: #")
				.append(partida.getId()).append(System.lineSeparator()).append("Jugador: ")
				.append(obtenerNombreJugador(partida)).append(System.lineSeparator()).append("Puntaje: ")
				.append(partida.getPuntaje()).append("/10").append(System.lineSeparator()).append("Tiempo: ")
				.append(partida.getTiempoEjecucion()).append(" segundos").append(System.lineSeparator())
				.append(System.lineSeparator()).append("Resultados:").append(System.lineSeparator());

		try {
			List<Resultado> resultados = cargarResultados(partida);

			if (resultados.isEmpty()) {
				mensaje.append("No se encontraron resultados.");
			} else {
				for (int i = 0; i < resultados.size(); i++) {
					Resultado resultado = resultados.get(i);

					mensaje.append(i + 1).append(". ").append(resultado.getResultado()).append(" = ")
							.append(resultado.getDato()).append(System.lineSeparator());
				}
			}

			ClipboardContent contenido = new ClipboardContent();
			contenido.putString(mensaje.toString());
			Clipboard.getSystemClipboard().setContent(contenido);

			lblMensaje.setText("Resumen de la partida copiado al portapapeles.");

		} catch (RuntimeException e) {
			lblMensaje.setText("No se pudieron consultar los resultados de la partida.");
			e.printStackTrace();
		}
	}

	/**
	 * Exporta una partida del historial como imagen PNG.
	 *
	 * @param partida partida que se exportará
	 */
	private void exportarPNG(Partida partida) {
		if (partida == null) {
			lblMensaje.setText("No hay una partida para exportar.");
			return;
		}

		FileChooser selector = new FileChooser();
		selector.setTitle("Exportar resultados de la partida");
		selector.setInitialFileName("resultados_partida_" + partida.getId() + ".png");
		selector.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen PNG", "*.png"));

		Stage ventana = contenedorPartidas.getScene() != null ? (Stage) contenedorPartidas.getScene().getWindow()
				: null;

		File archivo = selector.showSaveDialog(ventana);

		if (archivo == null) {
			lblMensaje.setText("Exportación cancelada.");
			return;
		}

		if (!archivo.getName().toLowerCase(Locale.ROOT).endsWith(".png")) {
			archivo = new File(archivo.getParentFile(), archivo.getName() + ".png");
		}

		try {
			WritableImage imagen = crearReportePNG(partida);

			boolean guardado = ImageIO.write(SwingFXUtils.fromFXImage(imagen, null), "png", archivo);

			if (!guardado) {
				throw new IOException("No se encontró un codificador PNG disponible.");
			}

			lblMensaje.setText("Partida #" + partida.getId() + " exportada correctamente.");

		} catch (IOException | RuntimeException e) {
			lblMensaje.setText("No se pudo exportar la imagen PNG.");
			e.printStackTrace();
		}
	}

	/**
	 * Construye el informe visual con el diseño de GameOverControler.
	 *
	 * @param partida partida que aparecerá en el informe
	 * @return imagen preparada para guardarse como PNG
	 */
	private WritableImage crearReportePNG(Partida partida) {
		String nombreJugador = obtenerNombreJugador(partida);

		VBox reporte = new VBox(16);
		reporte.setPadding(new Insets(32));
		reporte.setPrefWidth(820);
		reporte.setStyle("-fx-background-color: #f1f5f9;" + "-fx-font-family: 'Arial';");

		Label marca = new Label("ALL TEN");
		marca.setStyle("-fx-text-fill: #64748b;" + "-fx-font-size: 14px;" + "-fx-font-weight: bold;");

		Label titulo = new Label("Resumen de partida");
		titulo.setStyle("-fx-text-fill: #182235;" + "-fx-font-size: 28px;" + "-fx-font-weight: bold;");

		HBox datos = new HBox(12);
		datos.setAlignment(Pos.CENTER_LEFT);

		datos.getChildren().addAll(crearTarjetaDato("JUGADOR", nombreJugador),
				crearTarjetaDato("PARTIDA", "#" + partida.getId()),
				crearTarjetaDato("PUNTAJE", partida.getPuntaje() + "/10"),
				crearTarjetaDato("TIEMPO", partida.getTiempoEjecucion() + " segundos"));

		Label tituloResultados = new Label("Resultados obtenidos");
		tituloResultados.setStyle("-fx-text-fill: #182235;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;");

		VBox listaResultados = new VBox(8);

		List<Resultado> resultados = cargarResultados(partida);

		if (resultados.isEmpty()) {
			Label vacio = new Label("No se encontraron resultados para esta partida.");
			vacio.setWrapText(true);
			listaResultados.getChildren().add(vacio);
		} else {
			for (int i = 0; i < resultados.size(); i++) {
				Resultado resultado = resultados.get(i);

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

		Scene escenaTemporal = new Scene(reporte, 820, 600);

		reporte.applyCss();
		reporte.autosize();

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
	 * Crea una tarjeta para presentar un dato de la partida.
	 *
	 * @param etiqueta etiqueta del dato
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
	 * Regresa a la pantalla del juego.
	 */
	@FXML
	private void calljuego() {
		try {
			App.setRoot("juego");
		} catch (IOException e) {
			System.out.println("No se pudo volver al juego: " + e.getMessage());
		}
	}
}
