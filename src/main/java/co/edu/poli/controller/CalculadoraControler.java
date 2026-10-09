package co.edu.poli.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import co.edu.poli.dao.DaoJugadorImplementado;
import co.edu.poli.dao.DaoResultadoImplementado;
import co.edu.poli.dao.DaoScoreImplementado;
import co.edu.poli.modelo.Calculadora;
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
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;
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
	// private String operador;
	// private Button primerNumero;
	// private Button segundoNumero;

	private String exprecion = "";
	private List<Button> numerosUtilizados = new java.util.ArrayList<>();

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

	@FXML
	private Button btnMenu;

	private Calculadora calculadora;
	private Popup menuPopup;
	private long ultimoCierreMenu = 0;

	private int[] numerosOriginales;
	private int puntaje = 0;
	private Partida partidaActual;
	private Jugador jugadorActual;
	private boolean partidaFinalizada = false;
	private boolean ayudaDisponible = true;

	GeneradorEcuaciones generadorEcuaciones = new GeneradorEcuaciones();

	@FXML
	public void initialize() {
		DaoJugadorImplementado daoJugador = new DaoJugadorImplementado();

		jugadorActual = new Jugador(0, LocalDate.now());

		if (daoJugador.crear(jugadorActual)) {
			System.out.println("Jugador creado con ID: " + jugadorActual.getId());
		}

		puntaje = 0;

		lblResultado.setText("0/10");

		/*
		 * La Calculadora pertenece a la partida. Primero se crea la Calculadora porque
		 * la Partida necesita una para poder existir.
		 */
		calculadora = new Calculadora(new String[9], new int[4], "");

		/*
		 * Generar los números de la ronda.
		 */
		numerosOriginales = calculadora.generarNumeros();

		/*
		 * Generar los símbolos de la calculadora.
		 */
		String[] simbolos = calculadora.generarSimbolos();

		/*
		 * Crear la partida.
		 *
		 * La partida recibe: - Jugador - Calculadora - Fecha - Tiempo inicial - Puntaje
		 * inicial
		 */
		partidaActual = new Partida(jugadorActual, calculadora, LocalDate.now(), 0, 0);

		/*
		 * El jugador conoce su partida.
		 */
		jugadorActual.agregarPartida(partidaActual);

		/*
		 * Generar fecha e iniciar cronómetro.
		 */
		partidaActual.generarFechaPartida();
		partidaActual.iniciarTiempo();

		/*
		 * Guardar la partida.
		 */
		DaoScoreImplementado daoPartida = new DaoScoreImplementado();

		boolean partidaCreada = daoPartida.crear(partidaActual);

		if (!partidaCreada) {
			lblError.setText("No se pudo crear la partida");
		}

		/*
		 * Mostrar fecha.
		 */
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String fechaTexto = partidaActual.getFechaPartida().format(formato);

		lblFecha.setText(fechaTexto);

		/*
		 * Crear los botones de los cuatro números.
		 */
		for (int numero : numerosOriginales) {

			Button boton = new Button(String.valueOf(numero));

			boton.setOnAction(this::seleccionarNumero);
			boton.getStyleClass().addAll("btn-base", "btn-numero"); 
			contenedorNumeros.getChildren().add(boton);
		}

		/*
		 * Configurar los botones de operaciones.
		 */
		btnSuma.setText(simbolos[0]);
		btnResta.setText(simbolos[1]);
		btnMultiplicacion.setText(simbolos[2]);
		btnDivision.setText(simbolos[3]);
		btnParentesisAbre.setText(simbolos[4]);
		btnParentesisCierra.setText(simbolos[5]);
		btnBorrarUltimo.setText(simbolos[7]);
		btnBorrarTodo.setText(simbolos[8]);

		/*
		 * Crear cronómetro.
		 */
		Timeline cronometro = new Timeline(new KeyFrame(Duration.seconds(1), evento -> actualizarCronometro()));

		cronometro.setCycleCount(Timeline.INDEFINITE);

		cronometro.play();

		/*
		 * Iconos
		 */
		// Borrar último (retroceso)
		btnBorrarUltimo.setGraphic(crearIcono(
    		"M21 4 H8 l-7 8 7 8 h13 a2 2 0 0 0 2 -2 V6 a2 2 0 0 0 -2 -2 z "
    		+ "M18 9 L12 15 M12 9 L18 15"));
		btnBorrarUltimo.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		// Reiniciar (flecha circular)
		btnBorrarTodo.setGraphic(crearIcono(
   		 "M23 4 L23 10 L17 10 M20.49 15 a9 9 0 1 1 -2.12 -9.36 L23 10"));
		btnBorrarTodo.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		// Multiplicar (X)
		btnMultiplicacion.setGraphic(crearIcono("M18 6 L6 18 M6 6 L18 18"));
		btnMultiplicacion.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		// Suma
		btnSuma.setGraphic(crearIcono("M12 5 L12 19 M5 12 L19 12"));
		btnSuma.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		// Resta
		btnResta.setGraphic(crearIcono("M5 12 L19 12"));
		btnResta.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		// División
		btnDivision.setGraphic(crearIcono(
    	"M5 12 L19 12 "
    	+ "M11 6 a1 1 0 1 0 2 0 a1 1 0 1 0 -2 0 "
    	+ "M11 18 a1 1 0 1 0 2 0 a1 1 0 1 0 -2 0"));
		btnDivision.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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

	private SVGPath crearIcono(String contenido) {
    	SVGPath icono = new SVGPath();
    	icono.setContent(contenido);
    	icono.getStyleClass().add("icono");
    	return icono;
	}

	@FXML
	private void mostrarReglas() {
    try {
        	App.setRoot("reglas");
    	} catch (IOException ex) {
        	lblError.setText("Error al abrir las reglas");
    	}
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
		agregarOperador("+");
	}

	/**
	 * Selecciona el operador de resta y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de resta
	 */
	@FXML
	private void seleccionarResta(ActionEvent event) {
		agregarOperador("-");
	}

	/**
	 * Selecciona el operador de multiplicación y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de multiplicación
	 */
	@FXML
	private void seleccionarMultiplicacion(ActionEvent event) {
		agregarOperador("*");
	}

	/**
	 * Selecciona el operador de división y lo agrega a la expresión.
	 *
	 * @param event evento generado al presionar el botón de división
	 */
	@FXML
	private void seleccionarDivision(ActionEvent event) {
		agregarOperador("/");
	}

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

		// No permitir dos operadores seguidos
		if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/') {

			lblError.setText("No puede colocar dos operadores seguidos");
			return;
		}

		// No permitir operador después de "("
		if (ultimoCaracter == '(') {
			lblError.setText("Falta un número");
			return;
		}

		exprecion += nuevoOperador;

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());

		lblError.setText("");
	}

	private void finalizarPartida() {

		partidaFinalizada = true;

		partidaActual.setTiempoEjecucion(partidaActual.obtenerTiempoEjecucion());

		partidaActual.setPuntaje(puntaje);

		DaoScoreImplementado dao = new DaoScoreImplementado();

		boolean actualizada = dao.actualizar(partidaActual);

		if (actualizada) {
			System.out.println("Partida finalizada. ID: " + partidaActual.getId());

			System.out.println("Partida finalizada. ID: " + partidaActual.getId());

			System.out.println("Puntaje final: " + puntaje);

			System.out.println("Tiempo: " + partidaActual.getTiempoEjecucion() + " segundos");
		} else {

			partidaFinalizada = false;
			lblError.setText("No se pudo finalizar la partida");
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

		if (partidaFinalizada) {
			lblError.setText("La partida ya terminó");
			return;
		}

		if (texto.equals("(")) {

			/*
			 * Si hay un número antes del "(":
			 *
			 * 4(
			 *
			 * se convierte internamente en:
			 *
			 * 4*(
			 */
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

			// No permitir cerrar inmediatamente después de un operador
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

		// No permitir utilizar el mismo botón dos veces
		if (numerosUtilizados.contains(boton)) {
			lblError.setText("Ese número ya fue utilizado");
			return;
		}

		String numero = boton.getText();

		// Si la expresión está vacía, simplemente agregar el número
		if (exprecion.isEmpty()) {

			exprecion = numero;

		} else {

			char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

			/*
			 * Si después de un paréntesis de apertura viene un número:
			 *
			 * 4(2
			 *
			 * no necesitamos agregar operador.
			 */
			if (ultimoCaracter == '(') {

				exprecion += numero;

				/*
				 * Si después de un número viene otro número, se permite formar números de
				 * varias cifras:
				 *
				 * 1 + 2 -> 12
				 */
			} else if (Character.isDigit(ultimoCaracter)) {

				exprecion += numero;

				/*
				 * Si después de un paréntesis cerrado viene un número:
				 *
				 * (4)2
				 *
				 * se interpreta como multiplicación implícita:
				 *
				 * (4)*2
				 */
			} else if (ultimoCaracter == ')') {

				exprecion += "*" + numero;

				/*
				 * Si el último carácter es un operador, simplemente agregamos el número.
				 */
			} else {

				exprecion += numero;
			}
		}

		numerosUtilizados.add(boton);

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
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

		if (exprecion == null || exprecion.isEmpty()) {
			return;
		}

		char ultimoCaracter = exprecion.charAt(exprecion.length() - 1);

		/*
		 * Si el último carácter es un número, se elimina el número de la expresión y
		 * también se libera el botón utilizado.
		 */
		if (Character.isDigit(ultimoCaracter)) {

			exprecion = exprecion.substring(0, exprecion.length() - 1);

			if (!numerosUtilizados.isEmpty()) {

				Button ultimoBoton = numerosUtilizados.remove(numerosUtilizados.size() - 1);

				if (!contenedorNumeros.getChildren().contains(ultimoBoton)) {
					contenedorNumeros.getChildren().add(ultimoBoton);
				}
			}
		}

		/*
		 * Si se está borrando un paréntesis de cierre:
		 *
		 * 4*(2)
		 *
		 * pasa a:
		 *
		 * 4*(2
		 */
		else if (ultimoCaracter == ')') {

			exprecion = exprecion.substring(0, exprecion.length() - 1);
		}

		/*
		 * Si se está borrando un paréntesis de apertura y este tenía multiplicación
		 * implícita:
		 *
		 * 4*(
		 *
		 * debe pasar directamente a:
		 *
		 * 4
		 *
		 * y no quedar como:
		 *
		 * 4*
		 */
		else if (ultimoCaracter == '(') {

			exprecion = exprecion.substring(0, exprecion.length() - 1);

			if (!exprecion.isEmpty() && exprecion.charAt(exprecion.length() - 1) == '*') {

				exprecion = exprecion.substring(0, exprecion.length() - 1);
			}
		}

		/*
		 * Si el último carácter es un operador normal, solamente se elimina el
		 * operador.
		 */
		else if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/') {

			exprecion = exprecion.substring(0, exprecion.length() - 1);
		}

		txtValor1.setText(exprecion);
		txtValor1.positionCaret(exprecion.length());
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

		exprecion = "";
		numerosUtilizados.clear();
		generadorEcuaciones.limpiar();
	}

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

	// =========================
	// CALCULAR
	// =========================

	/**
	 * Calcula la expresión matemática ingresada por el usuario.
	 *
	 * <p>
	 * La expresión es procesada por la Calculadora asociada a la partida actual.
	 * </p>
	 *
	 * <p>
	 * Si el resultado es válido, se registra como un Resultado perteneciente a la
	 * Partida.
	 * </p>
	 *
	 * <p>
	 * Si la expresión contiene un error matemático o de sintaxis, se muestra el
	 * mensaje correspondiente al usuario.
	 * </p>
	 *
	 * @throws IllegalArgumentException si la expresión matemática es inválida.
	 * @throws ArithmeticException      si se intenta realizar una división entre
	 *                                  cero.
	 */
	int count = 0;

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

			/*
			 * La Calculadora que pertenece a la Partida recibe la expresión que acaba de
			 * construir el jugador.
			 */
			calculadora.setEcuacion(exprecion);

			/*
			 * Calcular la expresión.
			 */
			double resultado = calculadora.calcular();

			/*
			 * Convertir el resultado a entero o fracción.
			 *
			 * Ejemplo: 5.0 -> "5" 0.5 -> "1/2"
			 */
			String resultadoTexto = Calculadora.convertir(resultado);

			generadorEcuaciones.agregarParte(exprecion, resultadoTexto);
	        /*
	         * Crear botón con el resultado.
	         */
	        Button botonResultado =
	            new Button(resultadoTexto);
			botonResultado.getStyleClass().addAll("btn-base", "btn-numero");
			botonResultado.setOnAction(this::seleccionarNumero);

			/*
			 * Eliminar los números utilizados.
			 */
			contenedorNumeros.getChildren().removeAll(numerosUtilizados);

			/*
			 * Agregar el resultado como nuevo número.
			 */
			contenedorNumeros.getChildren().add(botonResultado);

			/*
			 * Limpiar expresión de la interfaz.
			 */
			txtValor1.setText("");
			lblError.setText("");
			exprecion = "";
			numerosUtilizados.clear();

			/*
			 * Solamente cuando queda un botón se valida el resultado final de la ronda.
			 */
			if (contenedorNumeros.getChildren().size() == 1) {

				/*
				 * No se permiten resultados mayores a 10.
				 */
				if (resultado > 10 || resultado != Math.floor(resultado)) {

					lblError.setText("No es valido números > 10 o fracciónes no exactas");
					

					count++;

					reiniciarNumeros();

					if (count >= 2) {

						ayudaDisponible = true;
					}

					return;
				}

				/*
				 * Validar que el resultado sea un número entero entre 1 y 10.
				 */
				if (resultado >= 1 && resultado <= 10 && resultado == Math.floor(resultado)) {

					/*
					 * Aumentar puntaje.
					 *
					 * El puntaje aumenta incluso si el resultado ya existe en la partida.
					 */
					puntaje++;

					lblResultado.setText(puntaje + "/10");

					int resultadoFinal = (int) resultado;

					DaoResultadoImplementado daoResultado = new DaoResultadoImplementado();

					/*
					 * Comprobar si el resultado ya existe en esta partida.
					 */
					boolean yaExiste = daoResultado.existePorPartidaYDato(partidaActual.getId(), resultadoFinal);

					/*
					 * Obtener la ecuación completa antes de limpiar el generador de ecuaciones.
					 */
					exprecion = generadorEcuaciones.obtenerEcuacion();

					// Se guarda en resultado

					generadorEcuaciones.limpiar();

					System.out.println("Ecuación completa: " + exprecion);

					/*
					 * Si el resultado todavía no existe, se guarda.
					 */
					if (!yaExiste) {

						/*
						 * Marcar visualmente el resultado.
						 */
						marcarResultado(resultadoFinal);

						/*
						 * Crear Resultado.
						 */
						Resultado nuevoResultado = new Resultado(0, partidaActual, exprecion, resultadoFinal);

						/*
						 * La partida conoce su resultado.
						 */
						partidaActual.agregarResultado(nuevoResultado);

						boolean resultadoCreado = daoResultado.crear(nuevoResultado);

						if (!resultadoCreado) {

							lblError.setText("No se pudo guardar el resultado");
						}

					} else {
						count++;

						/*
						 * El puntaje aumenta, pero no se guarda nuevamente el mismo resultado.
						 */
						// debe hacer como animacion de puntaje pero sin mostrar nada raro
					}

					System.out.println(partidaActual.getResultados());
					/*
					 * Actualizar puntaje de la partida.
					 */
					partidaActual.setPuntaje(puntaje);

					/*
					 * Comprobar si ya consiguió los resultados del 1 al 10.
					 */
					if (todosLosNumerosEncontrados()) {

						finalizarPartida();

					} else {

						/*
						 * Actualizar la partida en la base de datos.
						 */
						DaoScoreImplementado dao = new DaoScoreImplementado();

						boolean actualizada = dao.actualizar(partidaActual);

						if (!actualizada) {

							lblError.setText("No se pudo actualizar la partida");
						}

						/*
						 * Preparar una nueva ronda.
						 */
						reiniciarNumeros();
					}
				}
			}

		} catch (IllegalArgumentException | ArithmeticException e) {

			/*
			 * Mostrar el error producido por Calculadora.
			 */
			lblError.setText(e.getMessage());
		}
	}

	/**
	 * Marca el resultado indicado en verde.
	 *
	 * @param resultado número del resultado encontrado.
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
	 * Comprueba si todos los números han sido encontrados.
	 *
	 * @return true si las diez etiquetas están marcadas en verde; false en caso
	 *         contrario.
	 */
	private boolean todosLosNumerosEncontrados() {

		Label[] etiquetas = { lblResultado1, lblResultado2, lblResultado3, lblResultado4, lblResultado5, lblResultado6,
				lblResultado7, lblResultado8, lblResultado9, lblResultado10 };

		for (Label etiqueta : etiquetas) {
			if (!etiqueta.getStyleClass().contains("casilla-resuelta")) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * Muestra una tabla con las expresiones que permiten obtener
	 * los resultados del 1 al 10 utilizando los cuatro números
	 * generados para la partida actual.
	 */

	private void mostrarAyuda() {
    	try {
        	AyudaControler.setEcuaciones(calculadora.getEcuacionesResultados());
        	App.setRoot("ayuda");
    } 	catch (IOException ex) {
        lblError.setText("Error al abrir la ayuda");
    }

	}

	/**
	 * Muestra el menú de opciones de la partida.
	 *
	 * <p>
	 * El menú permite acceder a las instrucciones, consultar los
	 * resultados almacenados y, cuando corresponde, utilizar la ayuda
	 * con las expresiones generadas para la ronda.
	 * </p>
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
    	Button btnDificultad = new Button("Dificultad");

    	btnInstrucciones.setMaxWidth(Double.MAX_VALUE);
    	btnInstrucciones.getStyleClass().add("btn-menu-item");

    	btnConsultar.setMaxWidth(Double.MAX_VALUE);
    	btnConsultar.getStyleClass().add("btn-menu-item");
		btnConsultar.setMinWidth(Region.USE_PREF_SIZE);

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

    btnDificultad.setOnAction(e -> {
        popup.hide();
    });

    VBox contenedor = new VBox();
    contenedor.setPrefWidth(160);
    contenedor.setSpacing(8);

    contenedor.getStylesheets().add(
        getClass()
            .getResource("/co/edu/poli/juego/style.css")
            .toExternalForm()
    );

    contenedor.getStyleClass().add("menu-popup");

    contenedor.getChildren().addAll(
        btnInstrucciones,
        btnConsultar,
        btnDificultad
    );

    /*
     * El botón Ayuda solamente aparece después de que
     * se haya cumplido la condición establecida mediante count.
     */
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

    Bounds coordenadas =
        boton.localToScreen(
            boton.getBoundsInLocal()
        );

    double x = coordenadas.getMaxX() - contenedor.getPrefWidth();
    double y = coordenadas.getMaxY();

    popup.show(boton, x, y);
	}
}

