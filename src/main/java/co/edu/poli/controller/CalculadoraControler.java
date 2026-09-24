package co.edu.poli.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoScoreImplementado;
import co.edu.poli.modelo.Fraccion;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Operador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.juego;
import co.edu.poli.servicios.ConexionDB;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Controlador principal de la calculadora.
 *
 * Gestiona la interacción entre la interfaz gráfica y los servicios encargados
 * de realizar las operaciones matemáticas.
 *
 * Permite: - Ingresar números. - Seleccionar operadores matemáticos. - Ingresar
 * paréntesis. - Eliminar el último carácter ingresado. - Calcular expresiones
 * matemáticas. - Mostrar resultados enteros o en forma de fracción. - Mostrar
 * mensajes de error.
 */
public class CalculadoraControler {

	/**
	 * Campo de texto donde se ingresa la expresión matemática y se muestra el
	 * resultado.
	 */
	@FXML
	private TextField txtValor1;

	ConexionDB db = new ConexionDB();

	/**
	 * Etiqueta utilizada para mostrar mensajes de error al usuario.
	 */
	@FXML
	private Label lblError;

	/**
	 * Etiqueta destinada a mostrar el resultado de la operación.
	 */

	/**
	 * Almacena temporalmente el operador seleccionado.
	 */
	private String operador;
	private Button primerNumero;
	private Button segundoNumero;

	// botones de numeros de partida

	@FXML
	private HBox contenedorNumeros;

	@FXML
	private Label lblFecha;

	@FXML
	private Label lblTiempo;	

	@FXML
	private Label lblResultado1;

	@FXML
	private Label lblResultado2;

	@FXML
	private Label lblResultado3;

	@FXML
	private Label lblResultado4;

	@FXML
	private Label lblResultado5;

	@FXML
	private Label lblResultado6;

	@FXML
	private Label lblResultado7;

	@FXML
	private Label lblResultado8;

	@FXML
	private Label lblResultado9;

	@FXML
	private Label lblResultado10;

	@FXML
	private Label lblResultado;

	@FXML
	private Button btnSuma;

	@FXML
	private Button btnResta;

	@FXML
	private Button btnMultiplicacion;

	@FXML
	private Button btnDivision;

	@FXML
	private Button btnParentesisAbre;

	@FXML
	private Button btnParentesisCierra;

	@FXML
	private Button btnBorrarUltimo;

	@FXML
	private Button btnBorrarTodo;

	@FXML
	private Button btnIgual;
	
	@FXML
	private Button btnReglas;

	

	private juego partida;

	private int[] numerosOriginales;
	private int puntaje = 0;
	private Partida partidaActual;
	private Jugador jugadorActual;
	private boolean partidaFinalizada = false;
	
	@FXML
	public void initialize() {
		DaoJugadorImplementado j = new DaoJugadorImplementado();

		jugadorActual = new Jugador(0, LocalDate.now());

		if (j.crear(jugadorActual)) {

			System.out.println("Jugador creado con ID: " + jugadorActual.getId());
		}
		puntaje = 0;
		lblResultado.setText("0/10");

		partida = new juego(new String[9], new int[4]);

		numerosOriginales = partida.generarNumeros();
		String[] simbolos = partida.generarSimbolos();

		// Mostrar fecha Actual
		partidaActual = new Partida(jugadorActual.getId(), "0", LocalDate.now(), 0, 0);

		partidaActual.generarFechaPartida();
		partidaActual.iniciarTiempo();

		DaoScoreImplementado daoPartida = new DaoScoreImplementado();

		boolean partidaCreada = daoPartida.crear(partidaActual);

		if (!partidaCreada) {
			lblError.setText("No se pudo crear la partida");
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fechaTexto = partidaActual.getFechaPartida().format(formato);
		lblFecha.setText(fechaTexto);

		for (int numero : numerosOriginales) {

			Button boton = new Button(String.valueOf(numero));

			boton.setOnAction(this::seleccionarNumero);

			contenedorNumeros.getChildren().add(boton);
		}

		btnSuma.setText(simbolos[0]);
		btnResta.setText(simbolos[1]);
		btnMultiplicacion.setText(simbolos[2]);
		btnDivision.setText(simbolos[3]);
		btnParentesisAbre.setText(simbolos[4]);
		btnParentesisCierra.setText(simbolos[5]);
		btnIgual.setText(simbolos[6]);
		btnBorrarUltimo.setText(simbolos[7]);
		btnBorrarTodo.setText(simbolos[8]);

		// Iniciar el temporizador para actualizar el tiempo transcurrido
		Timeline cronometro = new Timeline(new KeyFrame(Duration.seconds(1), evento -> actualizarCronometro()));
		cronometro.setCycleCount(Timeline.INDEFINITE);
		cronometro.play();
	}
	// =========================
	// CRONOMETRO
	// =========================

	private void actualizarCronometro() {

    long segundosTotales = partidaActual.obtenerTiempoEjecucion();

    long minutos = segundosTotales / 60;
    long segundos = segundosTotales % 60;

    String tiempoTexto = String.format("%02d:%02d", minutos, segundos);

    lblTiempo.setText(tiempoTexto);
	}
	// =========================
	// CRONOMETRO
	// =========================
	@FXML
	private void mostrarReglas() {

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Reglas");
    alert.setHeaderText("Reglas del juego");

    String texto =
        "Con los 4 números de la ronda, arma una expresión matemática "
        + "distinta para lograr cada resultado del 1 al 10.\n\n"
        + "Reglas:\n"
        + "• Usa cada uno de los 4 números exactamente una vez por expresión.\n"
        + "• Puedes usar +, -, *, ÷ y paréntesis.\n"
        + "• Se permiten fracciones y resultados negativos intermedios.\n"
        + "• Puedes unir dos números para formar uno de varias cifras "
        + "(ej: 1 y 2 → 12).\n\n"
        + "Completas la ronda cuando encuentres los 10 resultados "
        + "(1 al 10) usando siempre los mismos 4 números.";

    	alert.setContentText(texto);
    	alert.showAndWait();
	}

	// =========================
	// OPERADORES
	// =========================

	

	/**
	 * Selecciona el operador de suma y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de suma
	 */
	@FXML
	private void seleccionarSuma(ActionEvent event) {

		operador = "+";

		lblError.setText("");

		if (primerNumero != null) {
			txtValor1.setText(primerNumero.getText() + operador);
		}
	}

	/**
	 * Selecciona el operador de resta y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de resta
	 */
	@FXML
	private void seleccionarResta(ActionEvent event) {

		operador = "-";

		lblError.setText("");

		if (primerNumero != null) {
			txtValor1.setText(primerNumero.getText() + operador);
		}
	}

	private void finalizarPartida() {
		
	    partidaFinalizada = true;

		partidaActual.setTiempoEjecucion(partidaActual.obtenerTiempoEjecucion());

		partidaActual.setPuntaje(puntaje);

		DaoScoreImplementado dao = new DaoScoreImplementado();

		boolean actualizada = dao.actualizar(partidaActual);

		if (actualizada) {
			System.out.println("Partida finalizada. ID: " + partidaActual.getId());
			
	        System.out.println("Partida finalizada. ID: "
	                + partidaActual.getId());

			System.out.println("Puntaje final: " + puntaje);

			System.out.println("Tiempo: " + partidaActual.getTiempoEjecucion() + " segundos");
		} else {
			
	        partidaFinalizada = false;
			lblError.setText("No se pudo finalizar la partida");
		}
	}

	/**
	 * Selecciona el operador de multiplicación y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de multiplicación
	 */
	@FXML
	private void seleccionarMultiplicacion(ActionEvent event) {

		operador = "*";

		lblError.setText("");

		if (primerNumero != null) {
			txtValor1.setText(primerNumero.getText() + operador);
		}
	}

	/**
	 * Selecciona el operador de división y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de división
	 */
	@FXML
	private void seleccionarDivision(ActionEvent event) {

		operador = "/";

		lblError.setText("");

		if (primerNumero != null) {
			txtValor1.setText(primerNumero.getText() + operador);
		}
	}

	// =========================
	// PARENTESIS
	// =========================

	/**
	 * Agrega un paréntesis de apertura a la expresión matemática.
	 *
	 * @param event evento generado al presionar el botón de paréntesis de apertura
	 */
	@FXML
	private void seleccionarParentesisAbierto(ActionEvent event) {

		Button boton = (Button) event.getSource();

		String pAbierto = boton.getText();

		lblError.setText("");

		agregarTexto(pAbierto);
	}

	/**
	 * Agrega un paréntesis de cierre a la expresión matemática.
	 *
	 * @param event evento generado al presionar el botón de paréntesis de cierre
	 */
	@FXML
	private void seleccionarParentesisCerrado(ActionEvent event) {

		Button boton = (Button) event.getSource();

		String pCerrado = boton.getText();

		lblError.setText("");

		agregarTexto(pCerrado);
	}

	// =========================
	// AGREGAR TEXTO
	// =========================

	/**
	 * Agrega un texto al final de la expresión matemática.
	 *
	 * Después de agregar el texto, posiciona el cursor al final del contenido del
	 * campo de texto.
	 *
	 * @param texto texto que se desea agregar a la expresión
	 */
	private void agregarTexto(String texto) {

		String actual = txtValor1.getText();

		txtValor1.setText(actual + texto);

		txtValor1.positionCaret(txtValor1.getText().length());
	}

	// =========================
	// NUMEROS
	// =========================

	/**
	 * Obtiene el número del botón presionado y lo agrega a la expresión matemática.
	 *
	 * @param event evento generado al presionar un botón numérico
	 */
	@FXML
	private void seleccionarNumero(ActionEvent event) {
		
	    if (partidaFinalizada) {
	        lblError.setText("La partida ya terminó");
	        return;
	    }

		Button boton = (Button) event.getSource();

		lblError.setText("");

		if (primerNumero == null) {

			primerNumero = boton;

			txtValor1.setText(boton.getText());

		} else if (segundoNumero == null && boton != primerNumero) {

			segundoNumero = boton;

			txtValor1.setText(primerNumero.getText() + operador + segundoNumero.getText());
		}
	}

	// =========================
	// BORRAR
	// =========================

	/**
	 * Elimina el último carácter de la expresión matemática.
	 *
	 * Si el campo de texto está vacío, no realiza ninguna acción. También limpia
	 * cualquier mensaje de error mostrado.
	 */
	@FXML
	private void borrarUltimo() {

		String actual = txtValor1.getText();

		if (actual.isEmpty()) {
			return;
		}

		// Borrar el último carácter visualmente
		txtValor1.setText(actual.substring(0, actual.length() - 1));

		// Si había un segundo número seleccionado, se deselecciona
		if (segundoNumero != null) {
			segundoNumero = null;
		}
		// Si no hay segundo número, se elimina el operador
		else if (operador != null) {
			operador = null;
		}
		// Si no hay operador, se elimina el primer número
		else if (primerNumero != null) {
			primerNumero = null;
		}

		lblError.setText("");
	}

	/**
	 * Borra todo el contenido del campo de texto y limpia el mensaje de error.
	 *
	 * @param event evento generado al presionar el botón de borrar todo
	 */
	@FXML
	private void borrarTodo(ActionEvent event) {

		reiniciarNumeros();
		
		txtValor1.setText("");
		lblError.setText("");

		primerNumero = null;
		segundoNumero = null;
		operador = null;
	}

	private void reiniciarNumeros() {

		contenedorNumeros.getChildren().clear();

		for (int numero : numerosOriginales) {

			Button boton = new Button(String.valueOf(numero));

			boton.setOnAction(this::seleccionarNumero);

			contenedorNumeros.getChildren().add(boton);
		}

		primerNumero = null;
		segundoNumero = null;
		operador = null;

		txtValor1.setText("");
	}

	// =========================
	// CALCULAR
	// =========================

	/**
	 * Calcula la expresión matemática ingresada por el usuario.
	 *
	 * Utiliza la clase {@link Operador} para procesar la expresión y obtener el
	 * resultado. Posteriormente utiliza la clase {@link Fraccion} para convertir
	 * los resultados decimales a su representación fraccionaria cuando sea
	 * necesario.
	 *
	 * Si la expresión contiene un error matemático o de sintaxis, se muestra el
	 * mensaje correspondiente en la etiqueta de error.
	 *
	 * @throws IllegalArgumentException si la expresión matemática es inválida
	 * @throws ArithmeticException      si se intenta realizar una división entre
	 *                                  cero
	 */

	@FXML
	private void resultado() {

		if (partidaFinalizada) {
		    lblError.setText("La partida ya terminó");
		    return;
		}
		
		if (primerNumero == null || segundoNumero == null || operador == null) {
			lblError.setText("Seleccione dos números y un operador");
			return;
		}

		try {

			String expresion = primerNumero.getText() + operador + segundoNumero.getText();

			Operador op = new Operador(expresion);
			double resultado = op.calcular();

			String resultadoTexto = Fraccion.convertir(resultado);

			// Crear el botón con CUALQUIER resultado válido
			Button botonResultado = new Button(resultadoTexto);
			botonResultado.setOnAction(this::seleccionarNumero);

			// Eliminar los dos números utilizados
			contenedorNumeros.getChildren().remove(primerNumero);
			contenedorNumeros.getChildren().remove(segundoNumero);

			// Agregar el resultado
			contenedorNumeros.getChildren().add(botonResultado);

			// Limpiar expresión
			txtValor1.setText("");
			lblError.setText("");

			// Limpiar selección
			primerNumero = null;
			segundoNumero = null;
			operador = null;

			/*
			 * SOLAMENTE cuando queda un botón se valida si el resultado final está entre 1
			 * y 10.
			 */
			if (contenedorNumeros.getChildren().size() == 1) {

				if (resultado >= 1 && resultado <= 10 && resultado == Math.floor(resultado)) {

					int resultadoFinal = (int) resultado;

					// Resultado correcto
					puntaje++;

					lblResultado.setText(puntaje + "/10");

					marcarResultado(resultadoFinal);

					partidaActual.setResultado(String.valueOf(resultadoFinal));
					partidaActual.setPuntaje(puntaje);

					if (todosLosNumerosEncontrados()) {

						finalizarPartida();

					} else {

						DaoScoreImplementado dao = new DaoScoreImplementado();

						boolean actualizada = dao.actualizar(partidaActual);

						if (!actualizada) {
							lblError.setText("No se pudo actualizar la partida");
						}

						reiniciarNumeros();
					}
				} else {

					// Resultado final incorrecto
					lblError.setText("No es un número correcto para resultado");

					reiniciarNumeros();
				}
			}

		} catch (IllegalArgumentException | ArithmeticException e) {

			// Por ejemplo: resultado negativo no permitido
			lblError.setText(e.getMessage());
		}
	}

	@FXML
	private void consultarPartida() {

	    DaoScoreImplementado dao = new DaoScoreImplementado();

	    List<Partida> partidas = dao.ultimasPartidas(5);

	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Consulta de partidas");
	    alert.setHeaderText("Partidas registradas");

	    if (partidas.isEmpty()) {

	        alert.setContentText("No hay partidas registradas.");

	    } else {

	        StringBuilder texto = new StringBuilder();

	        for (Partida p : partidas) {

	            texto.append("ID: ")
	                 .append(p.getId())
	                 .append("\n");

	            texto.append("Jugador: ")
	                 .append(p.getJugadorId())
	                 .append("\n");

	            texto.append("Fecha: ")
	                 .append(p.getFechaPartida())
	                 .append("\n");

	            texto.append("Resultado: ")
	                 .append(p.getResultado())
	                 .append("\n");

	            texto.append("Puntaje: ")
	                 .append(p.getPuntaje())
	                 .append("\n");

	            texto.append("Tiempo: ")
	                 .append(p.getTiempoEjecucion())
	                 .append(" segundos")
	                 .append("\n");

	            texto.append("----------------------------\n");
	        }

	        alert.setContentText(texto.toString());
	    }

	    alert.showAndWait();
	}

	/**
	 * Marca el resultado indicado en verde.
	 *
	 * @param resultado número del resultado encontrado.
	 */
	private void marcarResultado(int resultado) {

	    Label[] etiquetas = {
	        lblResultado1, lblResultado2, lblResultado3, lblResultado4,
	        lblResultado5, lblResultado6, lblResultado7, lblResultado8,
	        lblResultado9, lblResultado10
	    };

	    etiquetas[resultado - 1].setText(String.valueOf(resultado));
	    etiquetas[resultado - 1].setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
	}
	/**
	 * Comprueba si todos los números han sido encontrados.
	 *
	 * @return true si las diez etiquetas están marcadas en verde;
	 *         false en caso contrario.
	 */
	private boolean todosLosNumerosEncontrados() {

	    Label[] etiquetas = {
	        lblResultado1, lblResultado2, lblResultado3, lblResultado4,
	        lblResultado5, lblResultado6, lblResultado7, lblResultado8,
	        lblResultado9, lblResultado10
	    };

	    for (Label etiqueta : etiquetas) {
	        if (!etiqueta.getStyle().contains("-fx-text-fill: green")) {
	            return false;
	        }
	    }

	    return true;
	}

	/**
	 * Inicia el mecanismo disponible para compartir la información mostrada en la
	 * aplicación.
	 *
	 * Si existe información para compartir, esta se copia al portapapeles del
	 * sistema.
	 *
	 * Si ocurre un error o no existe un mecanismo compatible, se informa al usuario
	 * sin detener la aplicación.
	 */
	@FXML
	private void btnCompartir() {

		try {

			String resultado = txtValor1.getText();

			if (resultado == null || resultado.isEmpty()) {

				lblError.setText("No hay información para compartir");
				return;
			}

			String mensaje = "🧮 Resultado: " + resultado;

			ClipboardContent contenido = new ClipboardContent();
			contenido.putString(mensaje);

			Clipboard.getSystemClipboard().setContent(contenido);

			lblError.setText("Información preparada para compartir");

		} catch (Exception e) {

			lblError.setText("No existe un mecanismo compatible para compartir");
		}
	}

}