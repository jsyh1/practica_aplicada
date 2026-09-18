package co.edu.poli.modelo;

import java.time.LocalDateTime;

/**
 * Representa un jugador registrado en el sistema.
 *
 * @author Jsyh
 * @version 1.0
 */
public class Jugador {

    private int id;

    private LocalDateTime fechaPartida;

    /**
     * Constructor del jugador.
     *
     * @param id identificador del jugador
     * @param fechaPartida fecha y hora de registro de la partida
     */
    public Jugador(int id, LocalDateTime fechaPartida) {
        this.id = id;
        this.fechaPartida = fechaPartida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaPartida() {
        return fechaPartida;
    }

    public void setFechaPartida(LocalDateTime fechaPartida) {
        this.fechaPartida = fechaPartida;
    }
}