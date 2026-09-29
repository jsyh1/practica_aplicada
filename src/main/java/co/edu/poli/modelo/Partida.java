package co.edu.poli.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Partida {

    private int id;

    private Jugador jugador;

    private LocalDate fechaPartida;

    private long tiempoEjecucion;

    private int puntaje;

    private long inicioPartida;

    private List<Resultado> resultados;

    public Partida(Jugador jugador,
                   LocalDate fechaPartida,
                   long tiempoEjecucion,
                   int puntaje) {

        this.jugador = jugador;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;

        this.resultados = new ArrayList<>();
    }

    public Partida(int id,
                   Jugador jugador,
                   LocalDate fechaPartida,
                   long tiempoEjecucion,
                   int puntaje) {

        this.id = id;
        this.jugador = jugador;
        this.fechaPartida = fechaPartida;
        this.tiempoEjecucion = tiempoEjecucion;
        this.puntaje = puntaje;

        this.resultados = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
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

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void agregarResultado(Resultado resultado) {
        resultados.add(resultado);
    }

    public void generarFechaPartida() {
        this.fechaPartida = LocalDate.now();
    }

    public void iniciarTiempo() {
        this.inicioPartida = System.currentTimeMillis();
    }

    public long obtenerTiempoEjecucion() {

        this.tiempoEjecucion =
                (System.currentTimeMillis() - inicioPartida) / 1000;

        return this.tiempoEjecucion;
    }
}