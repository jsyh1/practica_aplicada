
package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Calculadora;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.servicios.ConexionDB;

/**
 * Implementación del DAO para la entidad Resultado.
 *
 * <p>
 * Realiza las operaciones CRUD sobre la tabla resultado y reconstruye
 * las relaciones entre Resultado, Partida, Jugador y Calculadora.
 * También recupera el nombre del jugador asociado a cada partida.
 * </p>
 *
 * @author Jsyh
 * @version 1.1
 */
public class DaoResultadoImplementado implements ResultadoDAO {

    /**
     * Conexión utilizada para acceder a la base de datos.
     */
    private final Connection conexion;

    /**
     * Constructor que obtiene la conexión a la base de datos.
     */
    public DaoResultadoImplementado() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    /**
     * Crea un resultado en la base de datos.
     *
     * @param objeto resultado que se desea crear
     * @return true si se creó correctamente; false en caso contrario
     */
    @Override
    public boolean crear(Resultado objeto) {

        String sql = """
                INSERT INTO resultado (partida_id, resultado, dato)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, objeto.getPartida().getId());
            ps.setString(2, objeto.getResultado());
            ps.setInt(3, objeto.getDato());

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
            System.out.println(
                    "Error al crear resultado: " + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Comprueba si un dato ya existe en una partida.
     *
     * @param partidaId identificador de la partida
     * @param dato valor numérico del resultado
     * @return true si existe; false en caso contrario
     */
    public boolean existePorPartidaYDato(int partidaId, int dato) {

        String sql = """
                SELECT 1
                FROM resultado
                WHERE partida_id = ?
                  AND dato = ?
                LIMIT 1
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, partidaId);
            ps.setInt(2, dato);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al comprobar el resultado: " + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Lista todos los resultados registrados, incluyendo los datos
     * de la partida y del jugador asociado.
     *
     * @return lista de resultados
     */
    @Override
    public List<Resultado> listar() {

        List<Resultado> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    r.id,
                    r.partida_id,
                    r.resultado,
                    r.dato,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM resultado r
                INNER JOIN partida p
                    ON r.partida_id = p.id
                INNER JOIN jugador j
                    ON p.jugador_id = j.id
                ORDER BY r.id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Partida partida = construirPartida(rs);

                Resultado resultado = new Resultado(
                        rs.getInt("id"),
                        partida,
                        rs.getString("resultado"),
                        rs.getInt("dato")
                );

                partida.agregarResultado(resultado);
                resultados.add(resultado);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar resultados: " + e.getMessage()
            );
        }

        return resultados;
    }

    /**
     * Busca un resultado por su identificador, incluyendo la información
     * de la partida y el nombre del jugador asociado.
     *
     * @param id identificador del resultado
     * @return resultado encontrado o null si no existe
     */
    @Override
    public Resultado buscarPorId(int id) {

        String sql = """
                SELECT
                    r.id,
                    r.partida_id,
                    r.resultado,
                    r.dato,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM resultado r
                INNER JOIN partida p
                    ON r.partida_id = p.id
                INNER JOIN jugador j
                    ON p.jugador_id = j.id
                WHERE r.id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Partida partida = construirPartida(rs);

                    Resultado resultado = new Resultado(
                            rs.getInt("id"),
                            partida,
                            rs.getString("resultado"),
                            rs.getInt("dato")
                    );

                    partida.agregarResultado(resultado);

                    return resultado;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar resultado: " + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza un resultado existente.
     *
     * @param objeto resultado con los datos actualizados
     * @return true si se actualizó correctamente; false en caso contrario
     */
    @Override
    public boolean actualizar(Resultado objeto) {

        String sql = """
                UPDATE resultado
                SET partida_id = ?,
                    resultado = ?,
                    dato = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, objeto.getPartida().getId());
            ps.setString(2, objeto.getResultado());
            ps.setInt(3, objeto.getDato());
            ps.setInt(4, objeto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar resultado: " + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Elimina un resultado por su identificador.
     *
     * @param id identificador del resultado
     * @return true si se eliminó correctamente; false en caso contrario
     */
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM resultado
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al eliminar resultado: " + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Construye una partida con su jugador, nombre, fecha,
     * calculadora y datos de ejecución.
     *
     * @param rs resultado de la consulta SQL
     * @return partida reconstruida
     * @throws SQLException si ocurre un error al leer los datos
     */
    private Partida construirPartida(ResultSet rs) throws SQLException {

        int jugadorId = rs.getInt("jugador_id");
        String nombreJugador = rs.getString("jugador_nombre");

        LocalDate fechaJugador = rs.getDate("fecha_jugador")
                .toLocalDate();

        Jugador jugador = new Jugador(
                jugadorId,
                nombreJugador,
                fechaJugador
        );

        LocalDate fechaPartida = rs.getTimestamp("fecha_partida")
                .toLocalDateTime()
                .toLocalDate();

        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                ""
        );

        Partida partida = new Partida(
                rs.getInt("partida_id"),
                jugador,
                calculadora,
                fechaPartida,
                rs.getTime("tiempo")
                        .toLocalTime()
                        .toSecondOfDay(),
                rs.getInt("puntaje")
        );

        jugador.agregarPartida(partida);

        return partida;
    }
}

