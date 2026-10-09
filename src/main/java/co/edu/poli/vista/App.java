package co.edu.poli.vista;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 */
public class App extends Application {

    private static Scene scene;
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

    Parent juego = loadFXML("juego");

    // Guardamos la vista del juego
    vistas.put("juego", juego);

    // Usamos la misma vista que acabamos de cargar
    scene = new Scene(juego);

    stage.setTitle("All Ten");

    stage.getIcons().add(
        new Image(
            App.class.getResourceAsStream(
                "/co/edu/poli/juego/img/logo.jpg"
            )
        )
    );

    stage.setScene(scene);
    stage.show();
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

    FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("/co/edu/poli/juego/" + fxml + ".fxml")
    );

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
 * Hace que la tarjeta de la vista se agrande o se encoja junto con la
 * ventana, manteniendo sus proporciones.
 */
private static void habilitarEscalado(Parent raiz) {

    if (!(raiz instanceof StackPane contenedor)) {
        return;
    }

    if (contenedor.getChildren().isEmpty()
            || !(contenedor.getChildren().get(0) instanceof Region tarjeta)) {
        return;
    }

    tarjeta.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    tarjeta.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    ChangeListener<Number> ajustar = (obs, antes, ahora) -> {

        // Medidas reales de la tarjeta ya acomodada (la escala no las altera)
        double anchoTarjeta = tarjeta.getWidth();
        double altoTarjeta = tarjeta.getHeight();

        if (anchoTarjeta <= 0 || altoTarjeta <= 0
                || contenedor.getWidth() <= 0 || contenedor.getHeight() <= 0) {
            return;
        }

        double escala = Math.min(
            contenedor.getWidth() / anchoTarjeta,
            contenedor.getHeight() / altoTarjeta
        );

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
}