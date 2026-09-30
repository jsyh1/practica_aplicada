package co.edu.poli.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una partida del juego All Ten.
 *
 * <p>
 * Una partida está asociada obligatoriamente a un {@link Jugador} y a una
 * {@link Calculadora}. La calculadora es utilizada para procesar las
 * operaciones y ecuaciones realizadas durante la partida.
 * </p>
 *
 * <p>
 * Además, una partida almacena la fecha en la que fue realizada, el tiempo
 * de ejecución, el puntaje obtenido y los resultados generados durante
 * el desarrollo de la partida.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class Partida {

    /**
     * Identificador único de la partida.
     */
    private int id;

    /**
     * Jugador propietario de la partida.
     */
    private Jugador jugador;

    /**
     * Calculadora utilizada durante la partida.
     */
    private Calculadora calculadora;

    /**
     * Fecha en la que se realizó la partida.
     */
    private LocalDate fechaPartida;

    /**
     * Tiempo total de ejecución de la partida expresado en segundos.
     */
    private long tiempoEjecucion;

    /**
     * Puntaje obtenido por el jugador durante la partida.
     */
    private int puntaje;

    /**
     * Momento en milisegundos en el que comenzó la partida.
     */
    private long inicioPartida;

    /**
     * Lista de resultados obtenidos durante la partida.
     */
    private List<Resultado> resultados;

    /**
     * Construye una partida sin identificador.
     *
     * <p>
     * Para crear una partida es obligatorio proporcionar un jugador y una
     * calculadora.
     * </p>
     *
     * @param jugador jugador que realiza la partida
     * @param calculadora calculadora utilizada durante la partida
     * @param fechaPartida fecha de la partida
     * @param tiempoEjecucion tiempo de ejecución en segundos
     * @param puntaje puntaje obtenido
     */
    public Partida(
            Jugador jugador,
            Calculadora calculadora,
            LocalDate fechaPartida,
            long tiempoEjecucion,
            int puntaje) {

        if (jugador == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener un jugador");
        }

        if (calculadora == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener una calculadora");
        }

        this.jugador = jugador;
        this.calculadora = calculadora;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;

        this.resultados = new ArrayList<>();
    }

    /**
     * Construye una partida con un identificador.
     *
     * <p>
     * Para crear una partida es obligatorio proporcionar un jugador y una
     * calculadora.
     * </p>
     *
     * @param id identificador de la partida
     * @param jugador jugador que realiza la partida
     * @param calculadora calculadora utilizada durante la partida
     * @param fechaPartida fecha de la partida
     * @param tiempoEjecucion tiempo de ejecución en segundos
     * @param puntaje puntaje obtenido
     */
    public Partida(
            int id,
            Jugador jugador,
            Calculadora calculadora,
            LocalDate fechaPartida,
            long tiempoEjecucion,
            int puntaje) {

        if (jugador == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener un jugador");
        }

        if (calculadora == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener una calculadora");
        }

        this.id = id;
        this.jugador = jugador;
        this.calculadora = calculadora;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;

        this.resultados = new ArrayList<>();
    }

    /**
     * Obtiene el identificador de la partida.
     *
     * @return identificador de la partida
     */
    public int getId() {
        return id;
    }

    /**
     * Modifica el identificador de la partida.
     *
     * @param id nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el jugador asociado a la partida.
     *
     * @return jugador de la partida
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Modifica el jugador asociado a la partida.
     *
     * @param jugador nuevo jugador
     */
    public void setJugador(Jugador jugador) {

        if (jugador == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener un jugador");
        }

        this.jugador = jugador;
    }

    /**
     * Obtiene la calculadora utilizada por la partida.
     *
     * @return calculadora de la partida
     */
    public Calculadora getCalculadora() {
        return calculadora;
    }

    /**
     * Modifica la calculadora asociada a la partida.
     *
     * @param calculadora nueva calculadora
     */
    public void setCalculadora(Calculadora calculadora) {

        if (calculadora == null) {
            throw new IllegalArgumentException(
                    "Una partida debe tener una calculadora");
        }

        this.calculadora = calculadora;
    }

    /**
     * Obtiene la fecha de la partida.
     *
     * @return fecha de la partida
     */
    public LocalDate getFechaPartida() {
        return fechaPartida;
    }

    /**
     * Modifica la fecha de la partida.
     *
     * @param fechaPartida nueva fecha
     */
    public void setFechaPartida(LocalDate fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    /**
     * Obtiene el tiempo de ejecución de la partida.
     *
     * @return tiempo de ejecución en segundos
     */
    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    /**
     * Modifica el tiempo de ejecución de la partida.
     *
     * @param tiempoEjecucion nuevo tiempo de ejecución en segundos
     */
    public void setTiempoEjecucion(long tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    /**
     * Obtiene el puntaje obtenido en la partida.
     *
     * @return puntaje de la partida
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Modifica el puntaje de la partida.
     *
     * @param puntaje nuevo puntaje
     */
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Obtiene los resultados de la partida.
     *
     * @return lista de resultados
     */
    public List<Resultado> getResultados() {
        return resultados;
    }

    /**
     * Agrega un resultado a la partida.
     *
     * @param resultado resultado que se desea agregar
     */
    public void agregarResultado(Resultado resultado) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "El resultado no puede ser null");
        }

        resultados.add(resultado);
    }

    /**
     * Genera y asigna la fecha actual a la partida.
     */
    public void generarFechaPartida() {
        this.fechaPartida = LocalDate.now();
    }

    /**
     * Inicia el contador de tiempo de la partida.
     *
     * <p>
     * El tiempo se almacena utilizando el valor actual del reloj del sistema
     * en milisegundos.
     * </p>
     */
    public void iniciarTiempo() {
        this.inicioPartida = System.currentTimeMillis();
    }

    /**
     * Calcula y actualiza el tiempo transcurrido desde el inicio de la partida.
     *
     * <p>
     * El tiempo obtenido se expresa en segundos.
     * </p>
     *
     * @return tiempo transcurrido en segundos
     */
    public long obtenerTiempoEjecucion() {

        this.tiempoEjecucion =
                (System.currentTimeMillis() - inicioPartida) / 1000;

        return this.tiempoEjecucion;
    }
}
