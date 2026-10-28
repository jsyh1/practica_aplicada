
package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Calculadora;
import co.edu.poli.modelo.EstadoPartida;
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.servicios.ConexionDB;

/**
 * Implementación del DAO para la entidad Partida.
 *
 * <p>
 * Permite realizar operaciones CRUD sobre la tabla partida y cargar
 * los resultados asociados. También recupera la información del jugador
 * relacionado con cada partida, incluyendo su nombre.
 * </p>
 *
 * @author Jsyh
 * @version 1.1
 */
public class DaoScoreImplementado implements PartidaDAO {

    /**
     * Conexión utilizada para acceder a la base de datos.
     */
    private final Connection conexion;

    /**
     * Constructor que obtiene la conexión a la base de datos.
     */
    public DaoScoreImplementado() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    /**
     * Consulta las últimas partidas registradas.
     *
     * @param cantidad cantidad máxima de partidas a consultar
     * @return lista de las últimas partidas
     */
    @Override
    public List<Partida> ultimasPartidas(int cantidad) {

        List<Partida> partidas = new ArrayList<>();

        String sql = """
                SELECT
                    p.id AS partida_id,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    p.estado,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM partida p
                INNER JOIN jugador j ON p.jugador_id = j.id
                WHERE p.estado = ?
                ORDER BY p.id DESC
                LIMIT ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, EstadoPartida.FINALIZADA.name());
            ps.setInt(2, cantidad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    partidas.add(crearPartidaDesdeResultadoSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar últimas partidas: " + e.getMessage()
            );
        }

        return partidas;
    }

    /**
     * Genera el RAnking.
     *
     * @param cantidad cantidad máxima de partidas a consultar
     * @return 
     */
    public List<Partida> ranking (int cantidad) {
    List<Partida> partidas = new ArrayList<>();

        String sql = """
                SELECT
                    p.id AS partida_id,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    p.estado,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM partida p
                INNER JOIN jugador j ON p.jugador_id = j.id
                WHERE p.estado = ?
                ORDER BY p.tiempo ASC, p.id ASC
                LIMIT ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, EstadoPartida.FINALIZADA.name());
            ps.setInt(2, cantidad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    partidas.add(crearPartidaDesdeResultadoSet(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al consultar Ranking: " + e.getMessage()
            );
        }

        return partidas;    
        
    }

    

    /**
     * Crea un objeto Partida a partir de un registro de la base de datos.
     *
     * @param rs resultado de la consulta SQL
     * @return partida reconstruida con su jugador y sus resultados
     * @throws SQLException si ocurre un error al leer los datos
     */
    private Partida crearPartidaDesdeResultadoSet(ResultSet rs)
    
            throws SQLException {
                
        int idJugador = rs.getInt("jugador_id");

        String nombreJugador = rs.getString("jugador_nombre");

        LocalDate fechaJugador = rs.getDate("fecha_jugador").toLocalDate();

        Jugador jugador = new Jugador(
                idJugador,
                nombreJugador,
                fechaJugador
        );

        LocalDate fechaPartida = rs.getTimestamp("fecha_partida")
                .toLocalDateTime()
                .toLocalDate();

        Calculadora calculadora = crearCalculadora();

        Partida partida = new Partida(
                rs.getInt("partida_id"),
                jugador,
                calculadora,
                fechaPartida,
                rs.getTime("tiempo").toLocalTime().toSecondOfDay(),
                rs.getInt("puntaje")
        );

        cargarResultados(partida);
        partida.setEstado(EstadoPartida.valueOf(rs.getString("estado")));
        jugador.agregarPartida(partida);

        return partida;
    }

    /**
     * Carga los resultados relacionados con una partida.
     *
     * @param partida partida a la que se agregarán los resultados
     */
    private void cargarResultados(Partida partida) {

        String sql = """
                SELECT id, partida_id, resultado, dato
                FROM resultado
                WHERE partida_id = ?
                ORDER BY id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, partida.getId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Resultado resultado = new Resultado(
                            rs.getInt("id"),
                            partida,
                            rs.getString("resultado"),
                            rs.getInt("dato")
                    );

                    partida.agregarResultado(resultado);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al cargar resultados: " + e.getMessage()
            );
        }
    }

    /**
     * Crea una partida en la base de datos.
     *
     * @param objeto partida que se desea crear
     * @return true si se creó correctamente; false en caso contrario
     */
    @Override
    public boolean crear(Partida objeto) {

        String sql = """
                INSERT INTO partida
                (jugador_id, fecha, tiempo, puntaje, estado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, objeto.getJugador().getId());

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            objeto.getFechaPartida().atStartOfDay()
                    )
            );

            ps.setTime(
                    3,
                    Time.valueOf(
                            LocalTime.ofSecondOfDay(
                                    objeto.getTiempoEjecucion()
                            )
                    )
            );

            ps.setInt(4, objeto.getPuntaje());
            ps.setString(5, objeto.getEstado().name());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        objeto.setId(idGenerado);

                        System.out.println(
                                "Partida creada con ID: " + idGenerado
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al crear partida: " + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Lista todas las partidas registradas.
     *
     * @return lista de partidas
     */
    @Override
    public List<Partida> listar() {

        List<Partida> partidas = new ArrayList<>();

        String sql = """
                SELECT
                    p.id AS partida_id,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    p.estado,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM partida p
                INNER JOIN jugador j ON p.jugador_id = j.id
                ORDER BY p.id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                partidas.add(crearPartidaDesdeResultadoSet(rs));
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar partidas: " + e.getMessage()
            );
        }

        return partidas;
    }

    /**
     * Busca una partida mediante su identificador.
     *
     * @param id identificador de la partida
     * @return partida encontrada o null si no existe
     */
    @Override
    public Partida buscarPorId(int id) {

        String sql = """
                SELECT
                    p.id AS partida_id,
                    p.jugador_id,
                    p.fecha AS fecha_partida,
                    p.tiempo,
                    p.puntaje,
                    p.estado,
                    j.nombre AS jugador_nombre,
                    j.fecha AS fecha_jugador
                FROM partida p
                INNER JOIN jugador j ON p.jugador_id = j.id
                WHERE p.id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearPartidaDesdeResultadoSet(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar partida: " + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza una partida existente.
     *
     * @param objeto partida que contiene los datos actualizados
     * @return true si se actualizó correctamente; false en caso contrario
     */
    @Override
    public boolean actualizar(Partida objeto) {

        String sql = """
                UPDATE partida
                SET jugador_id = ?,
                fecha = ?,
                tiempo = ?,
                puntaje = ?,
                estado = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, objeto.getJugador().getId());

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            objeto.getFechaPartida().atStartOfDay()
                    )
            );

            ps.setTime(
                    3,
                    Time.valueOf(
                            LocalTime.ofSecondOfDay(
                                    objeto.getTiempoEjecucion()
                            )
                    )
            );

            ps.setInt(4, objeto.getPuntaje());
            ps.setString(5, objeto.getEstado().name());
            ps.setInt(6, objeto.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar partida: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina una partida mediante su identificador.
     *
     * @param id identificador de la partida
     * @return true si se eliminó correctamente; false en caso contrario
     */
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
            System.out.println(
                    "Error al eliminar partida: " + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Crea una calculadora vacía para reconstruir una partida.
     *
     * <p>
     * Los datos de la calculadora no se almacenan actualmente
     * en la tabla partida.
     * </p>
     *
     * @return calculadora inicializada
     */
    private Calculadora crearCalculadora() {

        return new Calculadora(
                new String[9],
                new int[4],
                ""
        );
    }
}

