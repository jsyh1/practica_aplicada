package co.edu.poli.dao;

import java.util.List;

import co.edu.poli.modelo.Partida;

/**
 * DAO específico para la entidad Partida.
 *
 * @author Jsyh
 * @version 1.0
 */

public interface PartidaDAO extends DAO<Partida> {
	List<Partida> ultimasPartidas(int cantidad);
	List<Partida> ranking(int cantidad);

	/**
	 * Elimina las partidas abandonadas, sus resultados y los jugadores que queden
	 * sin partidas asociadas.
	 *
	 * @return cantidad de partidas eliminadas
	 * @throws IllegalStateException si falla la operación en la base de datos
	 */
	int eliminarPartidasAbandonadas();
}
