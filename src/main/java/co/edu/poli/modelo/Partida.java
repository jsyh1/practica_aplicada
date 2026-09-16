package co.edu.poli.modelo;

import java.time.LocalDate;

public class Partida {

    private int id;

    private int jugadorId;

    private String resultado;

    private LocalDate fechaPartida;

    private long tiempoEjecucion;

    private int puntaje;

    // Guarda el momento exacto en que comienza la partida.
    // No se guarda en la base de datos.
    private long inicioPartida;

    /**
     * Constructor utilizado para crear una nueva partida.
     * El id lo genera automáticamente la base de datos.
     */
    public Partida(int jugadorId, String resultado,
                   LocalDate fechaPartida,
                   long tiempoEjecucion, int puntaje) {

        this.jugadorId = jugadorId;
        this.resultado = resultado;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;
    }

    /**
     * Constructor utilizado cuando la partida
     * ya existe en la base de datos.
     */
    public Partida(int id, int jugadorId, String resultado,
                   LocalDate fechaPartida,
                   long tiempoEjecucion, int puntaje) {

        this.id = id;
        this.jugadorId = jugadorId;
        this.resultado = resultado;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(int jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDate getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(LocalDate fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(long tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    /**
     * Genera y establece la fecha actual de la partida.
     */
    public void generarFechaPartida() {
        this.fechaPartida = LocalDate.now();
    }

    /**
     * Inicia el contador de tiempo de la partida.
     */
    public void iniciarTiempo() {
        this.inicioPartida = System.currentTimeMillis();
    }

    /**
     * Calcula el tiempo transcurrido desde el inicio de la partida.
     *
     * @return tiempo de ejecución en segundos
     */
    public long obtenerTiempoEjecucion() {

        this.tiempoEjecucion =
                (System.currentTimeMillis() - inicioPartida) / 1000;

        return this.tiempoEjecucion;
    }
}