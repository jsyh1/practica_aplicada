
package co.edu.poli.controler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import co.edu.poli.controller.GameOverControler;
import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.modelo.Calculadora;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

class GameOverControlerTest {

	@Test
	void probarCompletarPartidaYGuardarNombre() throws Exception {

		iniciarJavaFX();

		DaoJugadorImplementado dao = new DaoJugadorImplementado();

		Jugador jugador = new Jugador(0, "", LocalDate.now());

		assertTrue(dao.crear(jugador));
		assertTrue(jugador.getId() > 0);

		try {
			Calculadora calculadora = new Calculadora(new String[9], new int[4], "2+3");

			Partida partida = new Partida(25, jugador, calculadora, LocalDate.now(), 87, 8);

			Resultado resultado = new Resultado(1, partida, "2+3", 5);

			partida.agregarResultado(resultado);

			GameOverControler controlador = new GameOverControler();

			TextField txtNombre = new TextField();
			Label lblMensaje = new Label();
			Label lblPuntaje = new Label();
			Label lblTiempo = new Label();
			Label lblResultados = new Label();

			Button btnCargar = new Button();
			Button btnCompartir = new Button();
			Button btnExportar = new Button();

			asignarCampo(controlador, "txtNombre", txtNombre);
			asignarCampo(controlador, "lblMensaje", lblMensaje);
			asignarCampo(controlador, "lblPuntaje", lblPuntaje);
			asignarCampo(controlador, "lblTiempo", lblTiempo);
			asignarCampo(controlador, "lblResultados", lblResultados);
			asignarCampo(controlador, "btnCargar", btnCargar);
			asignarCampo(controlador, "btnCompartir", btnCompartir);
			asignarCampo(controlador, "btnExportar", btnExportar);

			controlador.establecerPartida(partida);

			ejecutarEnJavaFX(() -> {
				txtNombre.setText("JugadorPrueba");

				// Simular la acción del botón Cargar.
				try {
					var metodo = GameOverControler.class.getDeclaredMethod("cargarNombre");

					metodo.setAccessible(true);
					metodo.invoke(controlador);

				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				assertEquals("Nombre guardado correctamente.", lblMensaje.getText());

				assertEquals("Puntaje: 8", lblPuntaje.getText());

				assertEquals("Tiempo de juego: 87 segundos", lblTiempo.getText());

				assertTrue(lblResultados.getText().contains("2+3"), "Debe mostrar el resultado de la partida.");

				assertTrue(btnCargar.isDisabled());
				assertFalse(btnCompartir.isDisabled());
				assertFalse(btnExportar.isDisabled());
			});

			Jugador guardado = dao.buscarPorId(jugador.getId());

			assertTrue(guardado != null);
			assertEquals("JugadorPrueba", guardado.getNombre());

		} finally {
			dao.eliminar(jugador.getId());
		}
	}

	/**
	 * Inicia JavaFX una sola vez para las pruebas.
	 *
	 * @throws InterruptedException si se interrumpe la espera
	 */
	private static void iniciarJavaFX() throws InterruptedException {
	    CountDownLatch latch = new CountDownLatch(1);

	    try {
	        Platform.startup(latch::countDown);
	    } catch (IllegalStateException e) {
	        // JavaFX ya fue inicializado.
	        latch.countDown();
	    }

	    if (!latch.await(10, TimeUnit.SECONDS)) {
	        throw new AssertionError("No se pudo iniciar JavaFX.");
	    }
	}
	private void ejecutarEnJavaFX(Runnable operacion) throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		AtomicReference<Throwable> error = new AtomicReference<>();

		Platform.runLater(() -> {
			try {
				operacion.run();
			} catch (Throwable e) {
				error.set(e);
			} finally {
				latch.countDown();
			}
		});

		assertTrue(latch.await(10, TimeUnit.SECONDS), "La operación JavaFX tardó demasiado.");

		if (error.get() != null) {
			throw new AssertionError("La prueba falló en JavaFX.", error.get());
		}
	}

	private void asignarCampo(Object objeto, String nombre, Object valor) throws Exception {

		Field campo = objeto.getClass().getDeclaredField(nombre);
		campo.setAccessible(true);
		campo.set(objeto, valor);
	}
}
