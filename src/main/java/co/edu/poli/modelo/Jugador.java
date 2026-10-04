package co.edu.poli.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un jugador registrado en el sistema.
 *
 * <p>
 * Almacena el identificador del jugador, la fecha asociada a su registro
 * y la lista de partidas relacionadas con él.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class Jugador {

    /**
     * Identificador único del jugador.
     */
    private int id;

    /**
     * Fecha asociada al registro del jugador.
     */
    private LocalDate fechaPartida;

    /**
     * Lista de partidas asociadas al jugador.
     */
    private List<Partida> partidas;

    /**
     * Construye un jugador con su identificador y fecha.
     *
     * <p>
     * Inicializa una lista vacía para almacenar sus partidas.
     * </p>
     *
     * @param id identificador del jugador
     * @param fechaPartida fecha asociada al jugador
     */
    public Jugador(int id, LocalDate fechaPartida) {
        this.id = id;
        this.fechaPartida = fechaPartida;
        this.partidas = new ArrayList<>();
    }

    /**
     * Obtiene el identificador del jugador.
     *
     * @return identificador del jugador
     */
    public int getId() {
        return id;
    }

    /**
     * Modifica el identificador del jugador.
     *
     * @param id nuevo identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha asociada al jugador.
     *
     * @return fecha del jugador
     */
    public LocalDate getFechaPartida() {
        return fechaPartida;
    }

    /**
     * Modifica la fecha asociada al jugador.
     *
     * @param fechaPartida nueva fecha
     */
    public void setFechaPartida(LocalDate fechaPartida) {
        this.fechaPartida = fechaPartida;
    }

    /**
     * Obtiene la lista de partidas asociadas al jugador.
     *
     * @return lista de partidas del jugador
     */
    public List<Partida> getPartidas() {
        return partidas;
    }

    /**
     * Agrega una partida a la lista de partidas del jugador.
     *
     * @param partida partida que se desea asociar al jugador
     */
    public void agregarPartida(Partida partida) {
        partidas.add(partida);
    }
}