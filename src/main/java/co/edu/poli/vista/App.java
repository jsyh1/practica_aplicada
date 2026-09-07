package co.edu.poli.vista;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Inicia la aplicación JavaFX.
     *
     * @param stage ventana principal de la aplicación
     * @throws IOException si no se puede cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("juego"));

        // Nombre de la ventana
        stage.setTitle("All Ten");

        // Logo de la ventana
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
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
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
            App.class.getResource(
                "/co/edu/poli/juego/" + fxml + ".fxml"
            )
        );

        return fxmlLoader.load();
    }

    /**
     * Método principal de la aplicación.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch();
    }
}