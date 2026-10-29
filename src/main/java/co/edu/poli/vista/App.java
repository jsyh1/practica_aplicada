package co.edu.poli.vista;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import co.edu.poli.controller.CalculadoraControler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 */
public class App extends Application {

	private static Scene scene;
	private static final String[] FUENTES_NUNITO = {
			"Nunito-Regular.ttf",
			"Nunito-Bold.ttf"
	};
	// Guarda las vistas que ya fueron cargadas
	private static final Map<String, Parent> vistas = new HashMap<>();

	/**
	 * Inicia la aplicación JavaFX.
	 *
	 * @param stage ventana principal de la aplicación
	 * @throws IOException si no se puede cargar el archivo FXML
	 */
	@Override
	public void start(Stage stage) throws IOException {

		cargarFuentesNunito();
		Parent juego = loadFXML("juego");

		// Guardamos la vista del juego
		vistas.put("juego", juego);

		// Usamos la misma vista que acabamos de cargar
		scene = new Scene(juego);

		stage.setTitle("All Ten");

		stage.getIcons().add(new Image(App.class.getResourceAsStream("/co/edu/poli/juego/img/logo.jpg")));

		stage.setScene(scene);
		stage.show();
	}

	private static void cargarFuentesNunito() {
		for (String nombreFuente : FUENTES_NUNITO) {
			String recurso = "/co/edu/poli/juego/fonts/" + nombreFuente;
			try (InputStream fuente = App.class.getResourceAsStream(recurso)) {
				if (fuente == null) {
					throw new IllegalStateException("No se encontró la fuente Nunito: " + recurso);
				}
				if (Font.loadFont(fuente, 12) == null) {
					throw new IllegalStateException("No se pudo cargar la fuente Nunito: " + recurso);
				}
			} catch (IOException ex) {
				throw new IllegalStateException("Error al cargar la fuente Nunito: " + recurso, ex);
			}
		}
	}

	/**
	 * Recarga completamente la pantalla del juego, creando una nueva instancia del
	 * FXML y de su controlador.
	 *
	 * @throws IOException si no se puede cargar la vista del juego
	 */
	public static void reiniciarJuego() throws IOException {
		Parent nuevoJuego = loadFXML("juego");

		// Reemplaza la vista almacenada para evitar reutilizar el estado anterior.
		vistas.put("juego", nuevoJuego);

		// Muestra la nueva instancia en la ventana principal.
		scene.setRoot(nuevoJuego);

		nuevoJuego.applyCss();
		nuevoJuego.requestLayout();
	}


	/**
	 * Actualiza la vista del juego conservando la escena principal.
	 *
	 * @param nuevoJuego nueva raíz de la interfaz del juego
	 */
	public static void actualizarJuego(Parent nuevoJuego) {
	    if (scene == null || scene.getWindow() == null) {
	        throw new IllegalStateException(
	            "La escena principal no está asociada a una ventana."
	        );
	    }

	    Stage stage = (Stage) scene.getWindow();

	    vistas.put("juego", nuevoJuego);
	    scene.setRoot(nuevoJuego);

	    nuevoJuego.applyCss();
	    nuevoJuego.requestLayout();

	    if (!stage.isMaximized() && !stage.isFullScreen()) {
	        double alturaNecesaria = nuevoJuego.prefHeight(scene.getWidth());

	        if (alturaNecesaria > 0) {
	            double decoracion = stage.getHeight() - scene.getHeight();
	            stage.setHeight(alturaNecesaria + decoracion);
	        }
	    }
	}
	
	
	/**
	 * Cambia la vista principal de la aplicación.
	 *
	 * @param fxml nombre del archivo FXML sin extensión
	 * @throws IOException si no se puede cargar el archivo FXML
	 */
	public static void setRoot(String fxml) throws IOException {

		// "consulta" y "ayuda" se recargan siempre para mostrar datos actualizados
		boolean cacheable = !"consulta".equals(fxml) && !"ayuda".equals(fxml);

		Parent vista = cacheable ? vistas.get(fxml) : null;

		if (vista == null) {
			vista = loadFXML(fxml);

			if (cacheable) {
				vistas.put(fxml, vista);
			}
		}

		Stage stage = (Stage) scene.getWindow();

		// Ajusta la altura si la ventana está en su tamaño normal
		boolean ajustarAltura = !stage.isMaximized() && !stage.isFullScreen();

		double decoracion = stage.getHeight() - scene.getHeight();

		scene.setRoot(vista);
		vista.applyCss();

		if (ajustarAltura) {
			double alturaNecesaria = vista.prefHeight(scene.getWidth());
			stage.setHeight(alturaNecesaria + decoracion);
		}
	}

	/**
	 * Carga un archivo FXML.
	 *
	 * @param fxml nombre del archivo FXML sin extensión
	 * @return componente raíz de la vista
	 * @throws IOException si no se puede cargar el archivo FXML
	 */
	private static Parent loadFXML(String fxml) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/co/edu/poli/juego/" + fxml + ".fxml"));

		Parent raiz = fxmlLoader.load();
		habilitarEscalado(raiz);

		return raiz;
	}

	/**
	 * Método principal de la aplicación.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		launch();
	}

	/**
	 * Hace que la tarjeta de la vista se agrande o se encoja junto con la ventana,
	 * manteniendo sus proporciones.
	 */
	private static void habilitarEscalado(Parent raiz) {

		if (!(raiz instanceof StackPane contenedor)) {
			return;
		}

		if (contenedor.getChildren().isEmpty() || !(contenedor.getChildren().get(0) instanceof Region tarjeta)) {
			return;
		}

		tarjeta.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		tarjeta.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		ChangeListener<Number> ajustar = (obs, antes, ahora) -> {

			// Medidas reales de la tarjeta ya acomodada (la escala no las altera)
			double anchoTarjeta = tarjeta.getWidth();
			double altoTarjeta = tarjeta.getHeight();

			if (anchoTarjeta <= 0 || altoTarjeta <= 0 || contenedor.getWidth() <= 0 || contenedor.getHeight() <= 0) {
				return;
			}

			double escala = Math.min(contenedor.getWidth() / anchoTarjeta, contenedor.getHeight() / altoTarjeta);

			if (Math.abs(escala - 1.0) < 0.02) {
				escala = 1.0;
			}

			tarjeta.setScaleX(escala);
			tarjeta.setScaleY(escala);
		};

		contenedor.widthProperty().addListener(ajustar);
		contenedor.heightProperty().addListener(ajustar);
		tarjeta.widthProperty().addListener(ajustar);
		tarjeta.heightProperty().addListener(ajustar);
	}

	@Override
	public void stop() {
    	CalculadoraControler.abandonarPartidaActual();
	}
}