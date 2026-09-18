package co.edu.poli.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

import co.edu.poli.modelo.Jugador;
import co.edu.poli.servicios.ConexionDB;

public class DaoJugadorImplementado implements JugadorDAO {

	private final Connection conexion;

	public DaoJugadorImplementado() {
		conexion = ConexionDB.getInstancia().getConexion();
	}

	@Override
	public boolean crear(Jugador objeto) {

		String sql = """
				INSERT INTO jugador
				(fecha_partida)
				VALUES (?)
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setTimestamp(1, Timestamp.valueOf(objeto.getFechaPartida()));

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {

			System.out.println("Error al crear jugador: " + e.getMessage());

			return false;
		}
	}

	@Override
	public List<Jugador> listar() {
		List<Jugador> jugadores = new ArrayList<>();
		String sql = """
				SELECT id, fecha_partida
				FROM jugador ORDER BY id
				""";
		try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Timestamp timestamp = rs.getTimestamp("fecha_partida");
				LocalDateTime fechaPartida = timestamp.toLocalDateTime();
				Jugador jugador = new Jugador(rs.getInt("id"), fechaPartida);
				jugadores.add(jugador);
			}
		} catch (SQLException e) {
			System.out.println("Error al listar jugadores: " + e.getMessage());
		}
		return jugadores;
	}

	/**
	 * * Busca un jugador por su identificador. * * @param id identificador del
	 * jugador * @return jugador encontrado o null si no existe
	 */
	@Override
	public Jugador buscarPorId(int id) {
		String sql = """
				SELECT id, fecha_partida 
				FROM jugador WHERE id = ?
				""";
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Timestamp timestamp = rs.getTimestamp("fecha_partida");
					LocalDateTime fechaPartida = timestamp.toLocalDateTime();
					return new Jugador(rs.getInt("id"), fechaPartida);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar jugador: " + e.getMessage());
		}
		return null;
	}

	@Override
	public boolean actualizar(Jugador objeto) {
		return false;
	}

	@Override
	public boolean eliminar(int id) {
		return false;
	}

}
