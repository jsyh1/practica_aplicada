package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Jugador;
import co.edu.poli.servicios.ConexionDB;

/**
 * Implementación de las operaciones de acceso a datos para la entidad Jugador.
 *
 * <p>
 * Esta clase permite registrar y consultar jugadores mediante sentencias
 * SQL ejecutadas sobre la conexión proporcionada por ConexionDB.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class DaoJugadorImplementado implements JugadorDAO {

    /**
     * Conexión utilizada para ejecutar las operaciones sobre la base de datos.
     */
    private final Connection conexion;

    /**
     * Construye el objeto DAO e inicializa la conexión con la base de datos.
     */
    public DaoJugadorImplementado() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    /**
     * Registra un jugador en la base de datos.
     *
     * <p>
     * Después de insertar el registro, recupera el identificador generado
     * y lo asigna al objeto Jugador recibido.
     * </p>
     *
     * @param objeto jugador que se desea registrar
     * @return true si el registro se realiza correctamente; false en caso contrario
     */
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

    /**
     * Consulta todos los jugadores registrados en la base de datos.
     *
     * <p>
     * Los registros recuperados se convierten en objetos Jugador
     * y se almacenan en una lista ordenada por identificador.
     * </p>
     *
     * @return lista de jugadores encontrados; una lista vacía si no existen
     *         registros o se presenta un error
     */
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
     * Busca un jugador mediante su identificador único.
     *
     * @param id identificador del jugador que se desea consultar
     * @return jugador encontrado o null si no existe o ocurre un error
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

    /**
     * Actualiza la información de un jugador existente.
     *
     * @param objeto jugador que contiene la información que se desea actualizar
     * @return false, debido a que la operación no está implementada
     */
    @Override
    public boolean actualizar(Jugador objeto) {
        return false;
    }

    /**
     * Elimina un jugador mediante su identificador.
     *
     * @param id identificador del jugador que se desea eliminar
     * @return false, debido a que la operación no está implementada
     */
    @Override
    public boolean eliminar(int id) {
        return false;
    }

}