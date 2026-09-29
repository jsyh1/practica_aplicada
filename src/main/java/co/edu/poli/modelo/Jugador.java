package co.edu.poli.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un jugador registrado en el sistema.
 */
public class Jugador {

    private int id;
    private LocalDate fechaPartida;

    private List<Partida> partidas;

    public Jugador(int id, LocalDate fechaPartida) {
        this.id = id;
        this.fechaPartida = fechaPartida;
        this.partidas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(LocalDate fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void agregarPartida(Partida partida) {
        partidas.add(partida);
    }
}