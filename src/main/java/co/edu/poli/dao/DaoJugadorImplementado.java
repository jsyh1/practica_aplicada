package co.edu.poli.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
				(fecha)
				VALUES (?)
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

			ps.setTimestamp(1, Timestamp.valueOf(objeto.getFechaPartida().atStartOfDay()));

			int filas = ps.executeUpdate();

			if (filas > 0) {

				try (ResultSet rs = ps.getGeneratedKeys()) {

					if (rs.next()) {
						objeto.setId(rs.getInt(1));
					}
				}

				return true;
			}

		} catch (SQLException e) {

			System.out.println("Error al crear jugador: " + e.getMessage());
		}

		return false;
	}

	@Override
	public List<Jugador> listar() {

		List<Jugador> jugadores = new ArrayList<>();

		String sql = """
				SELECT id, fecha
				FROM jugador
				ORDER BY id
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Date fecha = rs.getDate("fecha");

				LocalDate fechaPartida = fecha.toLocalDate();

				Jugador jugador = new Jugador(rs.getInt("id"), fechaPartida);

				jugadores.add(jugador);
			}

		} catch (SQLException e) {

			System.out.println("Error al listar jugadores: " + e.getMessage());
		}

		return jugadores;
	}

	/**
	 * Busca un jugador por su identificador.
	 *
	 * @param id identificador del jugador
	 * @return jugador encontrado o null si no existe
	 */
	@Override
	public Jugador buscarPorId(int id) {

		String sql = """
				SELECT id, fecha
				FROM jugador
				WHERE id = ?
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					Date fecha = rs.getDate("fecha");

					LocalDate fechaPartida = fecha.toLocalDate();

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
