package co.edu.poli.modelo;

import java.time.LocalDate;

/**
 * Representa un jugador registrado en el sistema.
 *
 * @author Jsyh
 * @version 1.0
 */
public class Jugador {

    private int id;

    private LocalDate fechaPartida;

    /**
     * Constructor del jugador.
     *
     * @param id identificador del jugador
     * @param fechaPartida fecha y hora de registro de la partida
     */
    public Jugador(int id, LocalDate fechaPartida) {
        this.id = id;
        this.fechaPartida = fechaPartida;
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
}