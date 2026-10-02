package co.edu.poli.controler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import co.edu.poli.controller.ConsultarControler;
import co.edu.poli.modelo.Calculadora;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;

class ConsultarControlerTest {

    @Test
    void probarCompartirPartidaTerminada() throws Exception {

        // -------------------------------------------------
        // 1. Crear jugador
        // -------------------------------------------------

        Jugador jugador = new Jugador(
            10,
            LocalDate.now()
        );


        // -------------------------------------------------
        // 2. Crear calculadora
        // -------------------------------------------------

        Calculadora calculadora = new Calculadora(
            new String[9],
            new int[4],
            "2+3"
        );


        // -------------------------------------------------
        // 3. Crear partida terminada
        // -------------------------------------------------

        Partida partida = new Partida(
            25,
            jugador,
            calculadora,
            LocalDate.now(),
            87,
            8
        );


        // -------------------------------------------------
        // 4. Agregar un resultado
        // -------------------------------------------------

        Resultado resultado = new Resultado(
            1,
            partida,
            "5",
            5
        );

        partida.agregarResultado(resultado);


        // -------------------------------------------------
        // 5. Crear controlador
        // -------------------------------------------------

        ConsultarControler controller = new ConsultarControler();


        // -------------------------------------------------
        // 6. Crear Label para lblMensaje
        // -------------------------------------------------

        iniciarJavaFX();

        Platform.runLater(() -> {
            try {

                Label lblMensaje = new Label();

                Field campoMensaje =
                    ConsultarControler.class.getDeclaredField(
                        "lblMensaje"
                    );

                campoMensaje.setAccessible(true);
                campoMensaje.set(controller, lblMensaje);


                // -----------------------------------------
                // 7. Obtener método compartir()
                // -----------------------------------------

                Method metodoCompartir =
                    ConsultarControler.class.getDeclaredMethod(
                        "compartir",
                        Partida.class
                    );

                metodoCompartir.setAccessible(true);


                // -----------------------------------------
                // 8. Ejecutar compartir()
                // -----------------------------------------

                metodoCompartir.invoke(
                    controller,
                    partida
                );


                // -----------------------------------------
                // 9. Comprobar mensaje
                // -----------------------------------------

                assertEquals(
                    "Información preparada para compartir",
                    lblMensaje.getText()
                );


                // -----------------------------------------
                // 10. Comprobar portapapeles
                // -----------------------------------------

                String textoClipboard =
                    Clipboard.getSystemClipboard().getString();

                assertTrue(
                    textoClipboard.contains("Partida #25")
                );

                assertTrue(
                    textoClipboard.contains("Jugador #10")
                );

                assertTrue(
                    textoClipboard.contains("Puntaje: 8")
                );

                assertTrue(
                    textoClipboard.contains("Tiempo: 87s")
                );

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Inicializa el entorno JavaFX necesario
     * para utilizar Clipboard.
     */
    private void iniciarJavaFX() {

        try {

            Platform.startup(() -> {});

        } catch (IllegalStateException e) {
            // JavaFX ya estaba iniciado.
        }
    }
}