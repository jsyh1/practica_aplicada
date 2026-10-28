
package co.edu.poli.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoResultadoImplementado;
import co.edu.poli.dao.DaoScoreImplementado;
import co.edu.poli.modelo.Calculadora;
import co.edu.poli.modelo.EstadoPartida;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.servicios.ConexionDB;
import co.edu.poli.servicios.GeneradorEcuaciones;
import co.edu.poli.vista.App;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controlador principal de la calculadora.
 *
 * Gestiona la interacción entre la interfaz gráfica y los servicios encargados
 * de realizar las operaciones matemáticas.
 *
 * Permite ingresar números, operadores y paréntesis; calcular expresiones;
 * registrar resultados; consultar ayuda y reglas; y finalizar la partida.
 *
 * @author Jsyh
 * @version 1.0
 */
public class CalculadoraControler {

	/**
	 * Campo de texto donde se construye la expresión matemática.
	 */
	@FXML
	private TextField txtValor1;

	/**
	 * Conexión con la base de datos.
	 */
	private final ConexionDB db = new ConexionDB();

	/**
	 * Etiqueta utilizada para mostrar mensajes de error.
	 */
	@FXML
	private Label lblError;

	/**
	 * Expresión matemática construida por el jugador.
	 */
	private String exprecion = "";

	/**
	 * Botones numéricos utilizados en la expresión actual.
	 */
	private final List<Button> numerosUtilizados = new ArrayList<>();

	/**
	 * Contenedor de los números disponibles para la ronda.
	 */
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

	/**
	 * Botón utilizado para seleccionar la operación de raíz cuadrada.
	 */
	@FXML
	private Button btnRaiz;

	/**
	 * Botón utilizado para seleccionar la operación de potencia.
	 */
	@FXML
	private Button btnPotencia;

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

	@FXML
	private Button btnMenu;

	/**
	 * Calculadora asociada a la partida actual.
	 */
	private Calculadora calculadora;

	/**
	 * Menú emergente de opciones.
	 */
	private Popup menuPopup;

	/**
	 * Momento del último cierre del menú.
	 */
	private long ultimoCierreMenu = 0;

	/**
	 * Números originales de la ronda.
	 */
	private int[] numerosOriginales;

	/**
	 * Puntaje acumulado durante la partida.
	 */
	private int puntaje = 0;

	/**
	 * Partida actual.
	 */
	private Partida partidaActual;

	/**
	 * Jugador asociado a la partida.
	 */
	private Jugador jugadorActual;

	/**
	 * Indica si la partida ya terminó.
	 */
	private boolean partidaFinalizada = false;

	/**
	 * Indica si la opción de ayuda está disponible.
	 */
	private boolean ayudaDisponible = true;

	/**
	 * Generador de ecuaciones de la partida.
	 */
	private final GeneradorEcuaciones generadorEcuaciones = new GeneradorEcuaciones();

	private static CalculadoraControler actual;

	/**
	 * Contador utilizado para habilitar la ayuda.
	 */
	private int count = 1;

	private Timeline cronometro;

	/**
	 * Indica si está activada la dificultad avanzada.
	 *
	 * <p>
	 * Cuando es true, se permiten raíces y potencias.
	 * </p>
	 */
	private boolean dificultadAvanzada = false;

	
	/**
	 * Constructor predeterminado utilizado por JavaFX cuando no se especifica
	 * una dificultad inicial.
	 */
	public CalculadoraControler() {
	}

	/**
	 * Crea un controlador con la dificultad inicial indicada.
	 *
	 * @param dificultadAvanzada true para activar la dificultad avanzada.
	 */
	public CalculadoraControler(boolean dificultadAvanzada) {
	    this.dificultadAvanzada = dificultadAvanzada;
	}
	
	
	/**
	 * Inicializa el juego, registra el jugador y la partida, genera los números y
	 * configura los controles de la interfaz.
	 */
	@FXML
	public void initialize() {
		
		actual = this;

		DaoJugadorImplementado daoJugador = new DaoJugadorImplementado();

		jugadorActual = new Jugador(0, "", LocalDate.now());

		if (daoJugador.crear(jugadorActual)) {
			System.out.println("Jugador creado con ID: " + jugadorActual.getId());
		}

		puntaje = 0;
		lblResultado.setText("0/10");

		calculadora = new Calculadora(new String[9], new int[4], "");

		generarNumerosPorDificultad();

		String[] simbolos = calculadora.generarSimbolos();

		partidaActual = new Partida(jugadorActual, calculadora, LocalDate.now(), 0, 0);

		jugadorActual.agregarPartida(partidaActual);

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
			boton.getStyleClass().addAll("btn-base", "btn-numero");

			contenedorNumeros.getChildren().add(boton);
		}

		btnSuma.setText(simbolos[0]);
		btnResta.setText(simbolos[1]);
		btnMultiplicacion.setText(simbolos[2]);
		btnDivision.setText(simbolos[3]);
		btnParentesisAbre.setText(simbolos[4]);
		btnParentesisCierra.setText(simbolos[5]);
		btnBorrarUltimo.setText(simbolos[7]);
		btnBorrarTodo.setText(simbolos[8]);
		btnRaiz.setText(simbolos[9]);
		btnPotencia.setText(simbolos[10]);

		cronometro = new Timeline(new KeyFrame(Duration.seconds(1), evento -> actualizarCronometro()));

		cronometro.setCycleCount(Timeline.INDEFINITE);
		cronometro.play();

		btnBorrarUltimo.setGraphic(crearIcono(
				"M21 4 H8 l-7 8 7 8 h13 a2 2 0 0 0 2 -2 V6 a2 2 0 0 0 -2 -2 z " + "M18 9 L12 15 M12 9 L18 15"));
		btnBorrarUltimo.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		btnBorrarTodo.setGraphic(crearIcono("M23 4 L23 10 L17 10 M20.49 15 a9 9 0 1 1 -2.12 -9.36 L23 10"));
		btnBorrarTodo.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		btnMultiplicacion.setGraphic(crearIcono("M18 6 L6 18 M6 6 L18 18"));
		btnMultiplicacion.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		btnSuma.setGraphic(crearIcono("M12 5 L12 19 M5 12 L19 12"));
		btnSuma.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		btnResta.setGraphic(crearIcono("M5 12 L19 12"));
		btnResta.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		btnDivision.setGraphic(crearIcono(
				"M5 12 L19 12 " + "M11 6 a1 1 0 1 0 2 0 a1 1 0 1 0 -2 0 " + "M11 18 a1 1 0 1 0 2 0 a1 1 0 1 0 -2 0"));
		btnDivision.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		actualizarBotonesDificultad();
	}
	
	/**
	 * Recarga la interfaz e inicia una nueva partida con la dificultad elegida.
	 *
	 * @param dificultadAvanzada true para activar la dificultad avanzada.
	 */
	private void recargarPartida(boolean dificultadAvanzada) {
	    if (cronometro != null) {
	        cronometro.stop();
	    }

	    try {
	        FXMLLoader loader = new FXMLLoader(
	            getClass().getResource("/co/edu/poli/juego/juego.fxml")
	        );

	        loader.setControllerFactory(tipo -> {
	            if (tipo == CalculadoraControler.class) {
	                return new CalculadoraControler(dificultadAvanzada);
	            }

	            try {
	                return tipo.getDeclaredConstructor().newInstance();
	            } catch (ReflectiveOperationException ex) {
	                throw new RuntimeException(
	                    "No se pudo crear el controlador: " + tipo.getName(), ex
	                );
	            }
	        });

	        Parent root = loader.load();
			// La partida anterior no se terminó: queda abandonada.
			// Se llama por instancia (no la versión estática), porque tras load()
			// "actual" ya apunta al controlador nuevo.
			abandonarPartida();
	        // Actualiza la vista usando la escena que administra App.
	        App.actualizarJuego(root);

	    } catch (IOException ex) {
	        lblError.setText("No se pudo reiniciar la partida.");
	        ex.printStackTrace();
	    }
	}
	
	
	/**
	 * Actualiza la visibilidad de los operadores avanzados según la dificultad
	 * seleccionada.
	 */
	private void actualizarBotonesDificultad() {
		btnRaiz.setVisible(dificultadAvanzada);
		btnRaiz.setManaged(dificultadAvanzada);

		btnPotencia.setVisible(dificultadAvanzada);
		btnPotencia.setManaged(dificultadAvanzada);
	}

	/**
	 * Actualiza el cronómetro de la partida.
	 */
	private void actualizarCronometro() {

		long segundosTotales = partidaActual.obtenerTiempoEjecucion();

		long minutos = segundosTotales / 60;
		long segundos = segundosTotales % 60;

		lblTiempo.setText(String.format("%02d:%02d", minutos, segundos));
	}

	/**
	 * Crea un icono SVG.
	 *
	 * @param contenido contenido SVG del icono
	 * @return icono generado
	 */
	private SVGPath crearIcono(String contenido) {

		SVGPath icono = new SVGPath();
		icono.setContent(contenido);
		icono.getStyleClass().add("icono");

		return icono;
	}

	/**
	 * Abre la pantalla de reglas.
	 */
	@FXML
	private void mostrarReglas() {

		try {
			App.setRoot("reglas");
		} catch (IOException ex) {
			lblError.setText("Error al abrir las reglas");
		}
	}

	/**
	 * Agrega el operador de suma.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarSuma(ActionEvent event) {
		agregarOperador("+");
	}

	/**
	 * Agrega el operador de resta.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarResta(ActionEvent event) {
		agregarOperador("-");
	}

	/**
	 * Agrega el operador de multiplicación.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarMultiplicacion(ActionEvent event) {
		agregarOperador("*");
	}

	/**
	 * Agrega el operador de división.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarDivision(ActionEvent event) {
		agregarOperador("/");
	}

	/**
	 * Inserta una raíz cuadrada en la expresión.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarRaiz(ActionEvent event) {
		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		if (!dificultadAvanzada) {
			lblError.setText("Active la dificultad avanzada para usar raíces");
			return;
		}

		if (exprecion.isEmpty()) {
			exprecion = "√(";
		} else {
			char ultimo = exprecion.charAt(exprecion.length() - 1);

			if (Character.isDigit(ultimo) || ultimo == ')') {
				exprecion += "*√(";
			} else if (ultimo == '+' || ultimo == '-' || ultimo == '*' || ultimo == '/' || ultimo == '^'
					|| ultimo == '(') {
				exprecion += "√(";
			} else {
				lblError.setText("No se puede insertar la raíz aquí");
				return;
			}
		}

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
		lblError.setText("");
	}

	/**
	 * Inserta el operador de potencia.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarPotencia(ActionEvent event) {
		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		if (!dificultadAvanzada) {
			lblError.setText("Active la dificultad avanzada para usar potencias");
			return;
		}

		if (exprecion.isEmpty()) {
			lblError.setText("Seleccione primero un número");
			return;
		}

		char ultimo = exprecion.charAt(exprecion.length() - 1);

		if (!Character.isDigit(ultimo) && ultimo != ')') {
			lblError.setText("La potencia debe ir después de un número o paréntesis");
			return;
		}

		exprecion += "^";

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
		lblError.setText("");
	}

	/**
	 * Agrega un operador a la expresión actual.
	 *
	 * @param nuevoOperador operador que se agregará
	 */
	private void agregarOperador(String nuevoOperador) {

		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		if (exprecion.isEmpty()) {
			lblError.setText("Seleccione primero un número");
			return;
		}

		char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

		if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/'
				|| ultimoCaracter == '^') {

			lblError.setText("No puede colocar dos operadores seguidos");
			return;
		}

		if (ultimoCaracter == '(') {
			lblError.setText("Falta un número");
			return;
		}

		exprecion += nuevoOperador;

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());

		lblError.setText("");
	}

	/**
	 * Guarda el tiempo y el puntaje finales de la partida.
	 */
	private void finalizarPartida() {

		partidaFinalizada = true;

		partidaActual.setTiempoEjecucion(partidaActual.obtenerTiempoEjecucion());

		partidaActual.setPuntaje(puntaje);
		partidaActual.setEstado(EstadoPartida.FINALIZADA);
		
		DaoScoreImplementado dao = new DaoScoreImplementado();
		boolean actualizada = dao.actualizar(partidaActual);
		cronometro.stop();

		if (actualizada) {

			System.out.println("Partida finalizada. ID: " + partidaActual.getId());
			System.out.println("Puntaje final: " + puntaje);
			System.out.println("Tiempo: " + partidaActual.getTiempoEjecucion() + " segundos");

			mostrarAlerta();

		} else {

			partidaFinalizada = false;
			partidaActual.setEstado(EstadoPartida.EN_CURSO);
			lblError.setText("No se pudo finalizar la partida");
		}
	}

	/**
 	* Marca como ABANDONADA la partida de la pantalla de juego activa, si
 	* todavía está en curso. La usa App al cerrar la ventana.
 	*/
	public static void abandonarPartidaActual() {
    	if (actual != null) {
        	actual.abandonarPartida();
    	}
	}

	private void abandonarPartida() {

    	if (partidaActual == null
            	|| partidaActual.getEstado() != EstadoPartida.EN_CURSO) {
        	return;
    	}

    	partidaActual.setEstado(EstadoPartida.ABANDONADA);

    	new DaoScoreImplementado().actualizar(partidaActual);
	}

	/**
	 * Genera los números según la dificultad seleccionada.
	 *
	 * En dificultad normal utiliza el generador existente. En dificultad avanzada
	 * busca una combinación que permita obtener todos los objetivos del 1 al 10.
	 */
	private void generarNumerosPorDificultad() {
		if (!dificultadAvanzada) {
			numerosOriginales = calculadora.generarNumeros();
			return;
		}

		final int MAX_INTENTOS = 10000;
		java.util.Random random = new java.util.Random();

		for (int intento = 0; intento < MAX_INTENTOS; intento++) {
			int[] candidatos = new int[4];

			for (int i = 0; i < candidatos.length; i++) {
				candidatos[i] = random.nextInt(10) + 1;
			}

			List<String> soluciones = calculadora.buscarSolucionesConRaicesYPotencias(candidatos);

			if (soluciones != null && soluciones.size() == 10) {
				numerosOriginales = candidatos;

				calculadora.getEcuacionesResultados().clear();
				calculadora.getEcuacionesResultados().addAll(soluciones);

				return;
			}
		}

		throw new IllegalStateException("No se encontró una combinación válida para la dificultad avanzada.");
	}

	/**
	 * Agrega un paréntesis de apertura.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarParentesisAbierto(ActionEvent event) {

		Button boton = (Button) event.getSource();
		String pAbierto = boton.getText();

		lblError.setText("");

		agregarTexto(pAbierto);
	}

	/**
	 * Agrega un paréntesis de cierre.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void seleccionarParentesisCerrado(ActionEvent event) {

		Button boton = (Button) event.getSource();
		String pCerrado = boton.getText();

		lblError.setText("");

		agregarTexto(pCerrado);
	}

	/**
	 * Agrega texto a la expresión matemática y contempla la multiplicación
	 * implícita antes de un paréntesis de apertura.
	 *
	 * @param texto texto que se agregará
	 */
	private void agregarTexto(String texto) {

		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		if (texto.equals("(")) {

			if (!exprecion.isEmpty()) {

				char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

				if (Character.isDigit(ultimoCaracter) || ultimoCaracter == ')') {
					exprecion += "*";
				}
			}

			exprecion += "(";

		} else if (texto.equals(")")) {

			if (exprecion.isEmpty()) {
				lblError.setText("No se puede cerrar un paréntesis vacío");
				return;
			}

			char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

			if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/'
					|| ultimoCaracter == '(') {

				lblError.setText("El paréntesis no puede cerrarse aquí");
				return;
			}

			exprecion += ")";

		} else {

			exprecion += texto;
		}

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
	}

	/**
	 * Agrega un número a la expresión.
	 *
	 * Evita reutilizar botones numéricos y contempla la multiplicación implícita
	 * después de un paréntesis de cierre.
	 *
	 * @param event evento del botón numérico
	 */
	@FXML
	private void seleccionarNumero(ActionEvent event) {

		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		Button boton = (Button) event.getSource();

		lblError.setText("");

		if (numerosUtilizados.contains(boton)) {
			lblError.setText("Ese número ya fue utilizado");
			return;
		}

		String numero = boton.getText();

		if (exprecion.isEmpty()) {

			exprecion = numero;

		} else {

			char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

			if (ultimoCaracter == '(') {

				exprecion += numero;

			} else if (Character.isDigit(ultimoCaracter)) {

				exprecion += numero;

			} else if (ultimoCaracter == ')') {

				exprecion += "*" + numero;

			} else {

				exprecion += numero;
			}
		}

		numerosUtilizados.add(boton);

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
	}

	/**
	 * Elimina el último elemento de la expresión y libera el botón numérico
	 * correspondiente cuando se elimina un número completo.
	 */
	@FXML
	private void borrarUltimo() {

		if (exprecion == null || exprecion.isEmpty()) {
			return;
		}

		int longitud = exprecion.length();
		char ultimoCaracter = exprecion.charAt(longitud - 1);

		if (Character.isDigit(ultimoCaracter)) {

			// Identificar el número completo al que pertenece el último dígito.
			int inicioNumero = longitud - 1;

			while (inicioNumero > 0 && Character.isDigit(exprecion.charAt(inicioNumero - 1))) {
				inicioNumero--;
			}

			String numeroCompleto = exprecion.substring(inicioNumero);

			// Eliminar el último dígito.
			exprecion = exprecion.substring(0, longitud - 1);

			// Liberar el botón si se eliminó un número de un solo dígito.
			if (inicioNumero == longitud - 1 && !numerosUtilizados.isEmpty()) {

				Button botonLiberado = null;

				for (int i = numerosUtilizados.size() - 1; i >= 0; i--) {
					Button boton = numerosUtilizados.get(i);

					if (boton.getText().equals(numeroCompleto)) {
						botonLiberado = numerosUtilizados.remove(i);
						break;
					}
				}

				if (botonLiberado != null && !contenedorNumeros.getChildren().contains(botonLiberado)) {
					contenedorNumeros.getChildren().add(botonLiberado);
				}
			}

		} else if (ultimoCaracter == ')') {

			// Eliminar el paréntesis de cierre.
			exprecion = exprecion.substring(0, longitud - 1);

		} else if (ultimoCaracter == '(') {

			// Eliminar el paréntesis de apertura.
			exprecion = exprecion.substring(0, longitud - 1);

			// Si pertenece a una raíz, eliminar también el símbolo √.
			if (!exprecion.isEmpty() && exprecion.charAt(exprecion.length() - 1) == '√') {

				exprecion = exprecion.substring(0, exprecion.length() - 1);

				// Eliminar la multiplicación implícita anterior a la raíz.
				if (!exprecion.isEmpty() && exprecion.charAt(exprecion.length() - 1) == '*') {
					exprecion = exprecion.substring(0, exprecion.length() - 1);
				}

			} else if (!exprecion.isEmpty() && exprecion.charAt(exprecion.length() - 1) == '*') {

				// Eliminar la multiplicación implícita anterior al paréntesis.
				exprecion = exprecion.substring(0, exprecion.length() - 1);
			}

		} else if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/'
				|| ultimoCaracter == '^' || ultimoCaracter == '√') {

			// Eliminar operadores y símbolos de raíz.
			exprecion = exprecion.substring(0, longitud - 1);
		}

		// Actualizar la expresión mostrada.
		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());

		lblError.setText("");
	}

	/**
	 * Limpia la expresión y reinicia los números disponibles.
	 *
	 * @param event evento del botón
	 */
	@FXML
	private void borrarTodo(ActionEvent event) {

		reiniciarNumeros();

		txtValor1.setText("");
		lblError.setText("");

		exprecion = "";
		numerosUtilizados.clear();

		generadorEcuaciones.limpiar();
	}

	/**
	 * Restablece los cuatro números originales de la ronda.
	 */
	private void reiniciarNumeros() {

		contenedorNumeros.getChildren().clear();

		for (int numero : numerosOriginales) {

			Button boton = new Button(String.valueOf(numero));

			boton.getStyleClass().addAll("btn-base", "btn-numero");
			boton.setOnAction(this::seleccionarNumero);

			contenedorNumeros.getChildren().add(boton);
		}

		exprecion = "";
		numerosUtilizados.clear();

		txtValor1.setText("");
	}

	/**
	 * Calcula la expresión, registra los resultados válidos y actualiza el puntaje
	 * y el estado de la partida.
	 */
	@FXML
	private void resultado() {

		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			lblError.setWrapText(true);
			return;
		}

		if (exprecion.isEmpty()) {
			lblError.setText("Ingrese una expresión");
			lblError.setWrapText(true);
			return;
		}

		try {

			System.out.println("Expresión: " + exprecion);

			calculadora.setEcuacion(exprecion);

			double resultado = calculadora.calcular();

			String resultadoTexto = Calculadora.convertir(resultado);

			generadorEcuaciones.agregarParte(exprecion, resultadoTexto);

			Button botonResultado = new Button(resultadoTexto);

			botonResultado.getStyleClass().addAll("btn-base", "btn-numero");

			botonResultado.setOnAction(this::seleccionarNumero);

			contenedorNumeros.getChildren().removeAll(numerosUtilizados);

			contenedorNumeros.getChildren().add(botonResultado);

			txtValor1.setText("");
			lblError.setText("");

			exprecion = "";
			numerosUtilizados.clear();

			if (contenedorNumeros.getChildren().size() == 1) {

				if (resultado > 10 || resultado != Math.floor(resultado) || resultado == 0) {

					lblError.setText("No es válido números > 10 o fracciones no exactas o = 0");

					count++;
					System.out.println(count + "A");

					reiniciarNumeros();

					if (count >= 10) {
						ayudaDisponible = true;
					}

					return;
				}

				if (resultado >= 1 && resultado <= 10 && resultado == Math.floor(resultado)) {

					puntaje++;

					lblResultado.setText(puntaje + "/10");

					int resultadoFinal = (int) resultado;

					DaoResultadoImplementado daoResultado = new DaoResultadoImplementado();

					boolean yaExiste = daoResultado.existePorPartidaYDato(partidaActual.getId(), resultadoFinal);

					exprecion = generadorEcuaciones.obtenerEcuacion();

					generadorEcuaciones.limpiar();

					System.out.println("Ecuación completa: " + exprecion);

					if (!yaExiste) {

						marcarResultado(resultadoFinal);

						Resultado nuevoResultado = new Resultado(0, partidaActual, exprecion, resultadoFinal);

						partidaActual.agregarResultado(nuevoResultado);

						boolean resultadoCreado = daoResultado.crear(nuevoResultado);

						if (!resultadoCreado) {
							lblError.setText("No se pudo guardar el resultado");
						}

					} else {

						count++;
						System.out.println(count + "B");
						if (count >= 10) {
							ayudaDisponible = true;
						}
					}

					System.out.println(partidaActual.getResultados());

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
				}
			}

		} catch (IllegalArgumentException | ArithmeticException e) {

			lblError.setText(e.getMessage());
		}
	}

	/**
	 * Marca visualmente un resultado encontrado.
	 *
	 * @param resultado resultado entre 1 y 10
	 */
	private void marcarResultado(int resultado) {

		Label[] etiquetas = { lblResultado1, lblResultado2, lblResultado3, lblResultado4, lblResultado5, lblResultado6,
				lblResultado7, lblResultado8, lblResultado9, lblResultado10 };

		Node celda = etiquetas[resultado - 1].getParent();

		etiquetas[resultado - 1].setText(String.valueOf(resultado));

		celda.getStyleClass().remove("casilla-pendiente");
		celda.getStyleClass().add("casilla-resuelta");
	}

	/**
	 * Comprueba si se encontraron los resultados del 1 al 10.
	 *
	 * @return true cuando todos los resultados están resueltos
	 */
	private boolean todosLosNumerosEncontrados() {

		Label[] etiquetas = { lblResultado1, lblResultado2, lblResultado3, lblResultado4, lblResultado5, lblResultado6,
				lblResultado7, lblResultado8, lblResultado9, lblResultado10 };

		for (Label etiqueta : etiquetas) {

			Node celda = etiqueta.getParent();

			if (celda == null || !celda.getStyleClass().contains("casilla-resuelta")) {

				return false;
			}
		}

		return true;
	}

	/**
	 * Abre la pantalla de ayuda con las ecuaciones disponibles.
	 */
	private void mostrarAyuda() {

		try {

			AyudaControler.setEcuaciones(calculadora.getEcuacionesResultados());

			App.setRoot("ayuda");

		} catch (IOException ex) {

			lblError.setText("Error al abrir la ayuda");
		}
	}

	/**
	 * Muestra el menú de opciones de la partida.
	 *
	 * Incluye instrucciones, consulta de partidas, dificultad y ayuda.
	 *
	 * @param event evento generado al presionar el botón de menú
	 */
	@FXML
	private void mostrarMenu(ActionEvent event) {

		if (menuPopup != null && menuPopup.isShowing()) {
			menuPopup.hide();
			return;
		}

		if (System.currentTimeMillis() - ultimoCierreMenu < 400) {
			return;
		}

		Popup popup = new Popup();

		popup.setAutoHide(true);
		popup.setOnHidden(e -> ultimoCierreMenu = System.currentTimeMillis());

		menuPopup = popup;

		Button btnInstrucciones = new Button("Instrucciones");

		Button btnConsultar = new Button("Consultar Partida");

		Button btnDificultad = new Button(dificultadAvanzada ? "Dificultad: Avanzada" : "Dificultad: Normal");

		btnInstrucciones.setMaxWidth(Double.MAX_VALUE);
		btnInstrucciones.getStyleClass().add("btn-menu-item");

		btnConsultar.setMaxWidth(Double.MAX_VALUE);
		btnConsultar.setMinWidth(Region.USE_PREF_SIZE);
		btnConsultar.getStyleClass().add("btn-menu-item");

		btnDificultad.setMaxWidth(Double.MAX_VALUE);
		btnDificultad.getStyleClass().add("btn-menu-item");

		btnInstrucciones.setOnAction(e -> {
			popup.hide();
			mostrarReglas();
		});

		btnConsultar.setOnAction(e -> {

			popup.hide();

			try {
				App.setRoot("consulta");
			} catch (IOException ex) {
				lblError.setText("Error al ver consulta");
			}
		});

		// nuevo boton
		btnDificultad.setOnAction(e -> {
		    boolean nuevaDificultad = !dificultadAvanzada;

		    popup.hide();
		    recargarPartida(nuevaDificultad);
		});

		VBox contenedor = new VBox(8);
		contenedor.setPrefWidth(220);

		java.net.URL css = getClass().getResource("/co/edu/poli/juego/style.css");

		if (css != null) {
			contenedor.getStylesheets().add(css.toExternalForm());
		}

		contenedor.getStyleClass().add("menu-popup");

		contenedor.getChildren().addAll(btnInstrucciones, btnConsultar, btnDificultad);

		if (ayudaDisponible) {

			Button btnAyuda = new Button("Ayuda");

			btnAyuda.setMaxWidth(Double.MAX_VALUE);
			btnAyuda.getStyleClass().add("btn-menu-item");

			btnAyuda.setOnAction(e -> {
				popup.hide();
				mostrarAyuda();
			});

			contenedor.getChildren().add(btnAyuda);
		}

		popup.getContent().add(contenedor);

		Button boton = (Button) event.getSource();

		Bounds coordenadas = boton.localToScreen(boton.getBoundsInLocal());

		if (coordenadas != null) {

			double x = coordenadas.getMaxX() - contenedor.getPrefWidth();

			double y = coordenadas.getMaxY();

			popup.show(boton, x, y);
		}
	}

	/**
	 * Muestra la ventana final con la información de la partida completada.
	 */
	private void mostrarAlerta() {

		try {

			javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
					App.class.getResource("/co/edu/poli/juego/gameOver.fxml"));

			Parent root = loader.load();

			GameOverControler controlador = loader.getController();

			controlador.establecerPartida(partidaActual);

			Stage ventana = new Stage();

			ventana.setTitle("All Ten - Partida completada");

			ventana.initModality(Modality.APPLICATION_MODAL);

			ventana.setResizable(false);
			ventana.setScene(new Scene(root));

			ventana.showAndWait();

		} catch (IOException e) {

			lblError.setText("No se pudo mostrar la ventana de resultados.");

			e.printStackTrace();
		}
	}
}
