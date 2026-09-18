
package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Partida;
import co.edu.poli.servicios.ConexionDB;

public class DaoScoreImplementado implements PartidaDAO {

	private final Connection conexion;

	public DaoScoreImplementado() {
		conexion = ConexionDB.getInstancia().getConexion();
		
		
	}
	@Override
	public List<Partida> ultimasPartidas(int cantidad) {

	    List<Partida> partidas = new ArrayList<>();

	    String sql = """
	            SELECT *
	            FROM partida
	            ORDER BY id DESC
	            LIMIT ?
	            """;

	    try (PreparedStatement ps = conexion.prepareStatement(sql)) {

	        ps.setInt(1, cantidad);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                Partida partida = new Partida(
	                        rs.getInt("id"),
	                        rs.getInt("jugador_id"),
	                        String.valueOf(rs.getDouble("resultado")),
	                        rs.getTimestamp("fecha")
	                                .toLocalDateTime()
	                                .toLocalDate(),
	                        rs.getTime("tiempo")
	                                .toLocalTime()
	                                .toSecondOfDay(),
	                        rs.getInt("puntaje")
	                );

	                partidas.add(partida);
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("Error al consultar últimas partidas: "
	                + e.getMessage());
	    }

	    return partidas;
	}
	@Override
	public boolean crear(Partida objeto) {

		String sql = """
				INSERT INTO partida
				(jugador_id, resultado, fecha, tiempo, puntaje)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			System.out.println("ID jugador que recibe la partida: " + objeto.getJugadorId());

			ps.setInt(1, objeto.getJugadorId());

			ps.setDouble(2, Double.parseDouble(objeto.getResultado()));

			ps.setTimestamp(3, Timestamp.valueOf(objeto.getFechaPartida().atStartOfDay()));

			ps.setTime(4, Time.valueOf(LocalTime.ofSecondOfDay(objeto.getTiempoEjecucion())));

			ps.setInt(5, objeto.getPuntaje());

			int filas = ps.executeUpdate();

			if (filas > 0) {

				try (ResultSet rs = ps.getGeneratedKeys()) {

					if (rs.next()) {

						int idGenerado = rs.getInt(1);

						objeto.setId(idGenerado);

						System.out.println("Partida creada con ID: " + idGenerado);
					}
				}

				return true;
			}

		} catch (SQLException | NumberFormatException e) {

			System.out.println("Error al crear partida: " + e.getMessage());
		}

		return false;
	}

	@Override
	public List<Partida> listar() {

		List<Partida> partidas = new ArrayList<>();

		String sql = "SELECT * FROM partida";

		try (PreparedStatement ps = conexion.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				Partida partida = new Partida(

						rs.getInt("id"),

						rs.getInt("jugador_id"),

						String.valueOf(rs.getDouble("resultado")),

						rs.getTimestamp("fecha").toLocalDateTime().toLocalDate(),

						rs.getTime("tiempo").toLocalTime().toSecondOfDay(),

						rs.getInt("puntaje"));

				partidas.add(partida);
			}

		} catch (SQLException e) {

			System.out.println("Error al listar partidas: " + e.getMessage());
		}

		return partidas;
	}

	@Override
	public Partida buscarPorId(int id) {

		String sql = """
				SELECT *
				FROM partida
				WHERE id = ?
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					return new Partida(

							rs.getInt("id"),

							rs.getInt("jugador_id"),

							String.valueOf(rs.getDouble("resultado")),

							rs.getTimestamp("fecha").toLocalDateTime().toLocalDate(),

							rs.getTime("tiempo").toLocalTime().toSecondOfDay(),

							rs.getInt("puntaje"));
				}
			}

		} catch (SQLException e) {

			System.out.println("Error al buscar partida: " + e.getMessage());
		}

		return null;
	}

	@Override
	public boolean actualizar(Partida objeto) {

		String sql = """
				UPDATE partida
				SET jugador_id = ?,
				    resultado = ?,
				    fecha = ?,
				    tiempo = ?,
				    puntaje = ?
				WHERE id = ?
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, objeto.getJugadorId());

			ps.setDouble(2, Double.parseDouble(objeto.getResultado()));

			ps.setTimestamp(3, Timestamp.valueOf(objeto.getFechaPartida().atStartOfDay()));

			ps.setTime(4, Time.valueOf(LocalTime.ofSecondOfDay(objeto.getTiempoEjecucion())));

			ps.setInt(5, objeto.getPuntaje());

			ps.setInt(6, objeto.getId());

			return ps.executeUpdate() > 0;

		} catch (SQLException | NumberFormatException e) {

			System.out.println("Error al actualizar partida: " + e.getMessage());

			return false;
		}
	}

	@Override
	public boolean eliminar(int id) {

		String sql = """
				DELETE FROM partida
				WHERE id = ?
				""";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, id);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {

			System.out.println("Error al eliminar partida: " + e.getMessage());

			return false;
		}
	}
}
