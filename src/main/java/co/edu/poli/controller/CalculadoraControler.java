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
import co.edu.poli.modelo.Resultado;
import co.edu.poli.modelo.juego;
import co.edu.poli.servicios.ConexionDB;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

	private String expresion = "";
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

	private juego partida;

	private int[] numerosOriginales;
	private int puntaje = 0;
	private Partida partidaActual;
	private Jugador jugadorActual;
	private boolean partidaFinalizada = false;

	@FXML
	public void initialize() {

		DaoJugadorImplementado daoJugador = new DaoJugadorImplementado();

		jugadorActual = new Jugador(0, LocalDate.now());

		if (daoJugador.crear(jugadorActual)) {

			System.out.println("Jugador creado con ID: " + jugadorActual.getId());
		}

		puntaje = 0;

		lblResultado.setText("0/10");

		partida = new juego(new String[9], new int[4]);

		numerosOriginales = partida.generarNumeros();

		String[] simbolos = partida.generarSimbolos();

		// Crear la partida asociada al jugador
		partidaActual = new Partida(jugadorActual, LocalDate.now(), 0, 0);

		// El jugador conoce su partida
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

		String texto = "Con los 4 números de la ronda, arma una expresión matemática "
				+ "distinta para lograr cada resultado del 1 al 10.\n\n" + "Reglas:\n"
				+ "• Usa cada uno de los 4 números exactamente una vez por expresión.\n"
				+ "• Puedes usar +, -, *, ÷ y paréntesis.\n"
				+ "• Se permiten fracciones y resultados negativos intermedios.\n"
				+ "• Puedes unir dos números para formar uno de varias cifras " + "(ej: 1 y 2 → 12).\n\n"
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

		if (expresion.isEmpty()) {
			lblError.setText("Seleccione primero un número");
			return;
		}

		char ultimoCaracter = expresion.charAt(expresion.length() - 1);

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

		expresion += nuevoOperador;

		txtValor1.setText(expresion);
		txtValor1.positionCaret(expresion.length());

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
			if (!expresion.isEmpty()) {

				char ultimoCaracter = expresion.charAt(expresion.length() - 1);

				if (Character.isDigit(ultimoCaracter) || ultimoCaracter == ')') {
					expresion += "*";
				}
			}

			expresion += "(";

		} else if (texto.equals(")")) {

			if (expresion.isEmpty()) {
				lblError.setText("No se puede cerrar un paréntesis vacío");
				return;
			}

			char ultimoCaracter = expresion.charAt(expresion.length() - 1);

			// No permitir cerrar inmediatamente después de un operador
			if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/'
					|| ultimoCaracter == '(') {

				lblError.setText("El paréntesis no puede cerrarse aquí");
				return;
			}

			expresion += ")";

		} else {

			expresion += texto;
		}

		txtValor1.setText(expresion);
		txtValor1.positionCaret(expresion.length());
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
		if (expresion.isEmpty()) {

			expresion = numero;

		} else {

			char ultimoCaracter = expresion.charAt(expresion.length() - 1);

			/*
			 * Si después de un paréntesis de apertura viene un número:
			 *
			 * 4(2
			 *
			 * no necesitamos agregar operador.
			 */
			if (ultimoCaracter == '(') {

				expresion += numero;

				/*
				 * Si después de un número viene otro número, se permite formar números de
				 * varias cifras:
				 *
				 * 1 + 2 -> 12
				 */
			} else if (Character.isDigit(ultimoCaracter)) {

				expresion += numero;

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

				expresion += "*" + numero;

				/*
				 * Si el último carácter es un operador, simplemente agregamos el número.
				 */
			} else {

				expresion += numero;
			}
		}

		numerosUtilizados.add(boton);

		txtValor1.setText(expresion);
		txtValor1.positionCaret(expresion.length());
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

		if (expresion == null || expresion.isEmpty()) {
			return;
		}

		char ultimoCaracter = expresion.charAt(expresion.length() - 1);

		/*
		 * Si el último carácter es un número, se elimina el número de la expresión y
		 * también se libera el botón utilizado.
		 */
		if (Character.isDigit(ultimoCaracter)) {

			expresion = expresion.substring(0, expresion.length() - 1);

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

			expresion = expresion.substring(0, expresion.length() - 1);
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

			expresion = expresion.substring(0, expresion.length() - 1);

			if (!expresion.isEmpty() && expresion.charAt(expresion.length() - 1) == '*') {

				expresion = expresion.substring(0, expresion.length() - 1);
			}
		}

		/*
		 * Si el último carácter es un operador normal, solamente se elimina el
		 * operador.
		 */
		else if (ultimoCaracter == '+' || ultimoCaracter == '-' || ultimoCaracter == '*' || ultimoCaracter == '/') {

			expresion = expresion.substring(0, expresion.length() - 1);
		}

		txtValor1.setText(expresion);
		txtValor1.positionCaret(expresion.length());
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

		expresion = "";
		numerosUtilizados.clear();
	}

	private void reiniciarNumeros() {

		contenedorNumeros.getChildren().clear();

		for (int numero : numerosOriginales) {

			Button boton = new Button(String.valueOf(numero));

			boton.setOnAction(this::seleccionarNumero);

			contenedorNumeros.getChildren().add(boton);
		}

		expresion = "";
		numerosUtilizados.clear();

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
			lblError.setText("La partida ya terminó");//no se a probado
			return;
		}

		if (expresion.isEmpty()) {
			lblError.setText("Ingrese una expresión");
			return;
		}

		try {

			String expresionCalculada = expresion;
			System.out.println(expresionCalculada);//si es correcto y da un resultado valido que no existiera se debe guardar la ecuacion y su resultado

			Operador op = new Operador(expresionCalculada);
			double resultado = op.calcular();

			String resultadoTexto = Fraccion.convertir(resultado);

			// Eliminar los cuatro números utilizados
			contenedorNumeros.getChildren().removeAll(numerosUtilizados);

			// Crear el botón con el resultado
			Button botonResultado = new Button(resultadoTexto);

			botonResultado.setOnAction(this::seleccionarNumero);

			// Agregar el resultado
			contenedorNumeros.getChildren().add(botonResultado);

			txtValor1.setText("");
			lblError.setText("");

			expresion = "";
			numerosUtilizados.clear();

			/*
			 * SOLAMENTE cuando queda un botón se valida si el resultado final está entre 1
			 * y 10.
			 */
			if (contenedorNumeros.getChildren().size() == 1) {

				if (resultado > 10) {
					lblError.setText("no pueden existir numero mayor a 10");
					reiniciarNumeros();
					return;
				} else {

					if (resultado >= 1 && resultado <= 10 && resultado == Math.floor(resultado)) {

						int resultadoFinal = (int) resultado;

						// Aumentar puntaje
						puntaje++;

						lblResultado.setText(puntaje + "/10");

						// Marcar resultado encontrado
						marcarResultado(resultadoFinal);

						// Crear objeto Resultado
						Resultado nuevoResultado = new Resultado(0, partidaActual, expresion, resultadoFinal);

						// Agregarlo a la partida
						partidaActual.agregarResultado(nuevoResultado);

						// Actualizar puntaje de la partida
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

				texto.append("ID: ").append(p.getId()).append("\n");

				texto.append("Jugador: ").append(p.getJugador()).append("\n");

				texto.append("Fecha: ").append(p.getFechaPartida()).append("\n");

				texto.append("Resultados: ");

				if (p.getResultados().isEmpty()) {

					texto.append("Sin resultados\n");

				} else {

					for (Resultado r : p.getResultados()) {

						texto.append(r.getResultado()).append(" (dato: ").append(r.getDato()).append(")");

					}

					texto.append("\n");
				}

				texto.append("Puntaje: ").append(p.getPuntaje()).append("\n");

				texto.append("Tiempo: ").append(p.getTiempoEjecucion()).append(" segundos").append("\n");

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

		Label[] etiquetas = { lblResultado1, lblResultado2, lblResultado3, lblResultado4, lblResultado5, lblResultado6,
				lblResultado7, lblResultado8, lblResultado9, lblResultado10 };

		etiquetas[resultado - 1].setText(String.valueOf(resultado));
		etiquetas[resultado - 1].setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
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
	private void Compartir() {

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

	@FXML
	private void mostrarMenu(ActionEvent event) {

    Popup popup = new Popup();
    popup.setAutoHide(true);

    Button btnInstrucciones = new Button("Instrucciones");
    Button btnConsultar = new Button("Consultar");
	Button btnCompartir = new Button("Compartir");


    btnInstrucciones.setMaxWidth(Double.MAX_VALUE);
    btnConsultar.setMaxWidth(Double.MAX_VALUE);
	btnCompartir.setMaxWidth(Double.MAX_VALUE);

    btnInstrucciones.setOnAction(e -> {
        popup.hide();
        mostrarReglas();
    });

	btnCompartir.setOnAction(e -> {
        popup.hide();
        Compartir();
    });

    btnConsultar.setOnAction(e -> {
        popup.hide();
        consultarPartida();
    });


    VBox contenedor = new VBox(btnInstrucciones, btnCompartir,btnConsultar);
    contenedor.setSpacing(4);
    contenedor.setStyle("-fx-background-color: white; -fx-padding: 8; "
        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
    popup.getContent().add(contenedor);

    Button boton = (Button) event.getSource();
    Bounds coordenadas = boton.localToScreen(boton.getBoundsInLocal());

    popup.show(boton, coordenadas.getMinX(), coordenadas.getMaxY());
	}

}