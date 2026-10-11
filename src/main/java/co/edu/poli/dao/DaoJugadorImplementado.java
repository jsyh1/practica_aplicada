package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Jugador;
import co.edu.poli.servicios.ConexionDB;

/**
 * Implementación de las operaciones de acceso a datos para la entidad Jugador.
 *
 * <p>
 * Esta clase permite registrar, consultar, actualizar y eliminar jugadores
 * mediante sentencias SQL ejecutadas sobre la conexión proporcionada
 * por ConexionDB.
 * </p>
 *
 * @author Jsyh
 * @version 1.1
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
     * Registra un jugador en la base de datos y recupera su identificador.
     *
     * @param objeto jugador que se desea registrar
     * @return true si el registro se realiza correctamente; false en caso contrario
     */
    @Override
    public boolean crear(Jugador objeto) {

        String sql = """
                INSERT INTO jugador (nombre, fecha)
                VALUES (?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, objeto.getNombre());
            ps.setDate(2, Date.valueOf(objeto.getFechaPartida()));

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
     * @return lista de jugadores ordenada por identificador
     */
    @Override
    public List<Jugador> listar() {

        List<Jugador> jugadores = new ArrayList<>();

        String sql = """
                SELECT id, nombre, fecha
                FROM jugador
                ORDER BY id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Jugador jugador = new Jugador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDate("fecha").toLocalDate()
                );

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
                SELECT id, nombre, fecha
                FROM jugador
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Jugador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDate("fecha").toLocalDate()
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar jugador: " + e.getMessage());
        }

        return null;
    }

    /**
     * Actualiza el nombre y la fecha de un jugador existente.
     *
     * @param objeto jugador con los datos actualizados
     * @return true si se actualiza el registro; false en caso contrario
     */
    @Override
    public boolean actualizar(Jugador objeto) {

        String sql = """
                UPDATE jugador
                SET nombre = ?, fecha = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, objeto.getNombre());
            ps.setDate(2, Date.valueOf(objeto.getFechaPartida()));
            ps.setInt(3, objeto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
        }

        return false;
    }

    /**
     * Elimina un jugador mediante su identificador.
     *
     * <p>
     * Las partidas y los resultados asociados también se eliminarán
     * debido a las restricciones ON DELETE CASCADE de la base de datos.
     * </p>
     *
     * @param id identificador del jugador que se desea eliminar
     * @return true si se elimina el registro; false en caso contrario
     */
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM jugador
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar jugador: " + e.getMessage());
        }

        return false;
    }
}

