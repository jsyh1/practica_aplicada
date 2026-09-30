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
import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.servicios.ConexionDB;

/**
 * Implementación del DAO para la entidad Partida.
 *
 * Se encarga de realizar las operaciones CRUD sobre la tabla
 * partida y de cargar los resultados asociados a cada partida.
 *
 * @author Jsyh
 * @version 1.0
 */
public class DaoScoreImplementado implements PartidaDAO {

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
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                ORDER BY id DESC
                LIMIT ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cantidad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int idJugador =
                            rs.getInt("jugador_id");

                    LocalDateHelper fecha =
                            new LocalDateHelper(
                                    rs.getTimestamp("fecha")
                            );

                    Jugador jugador = new Jugador(
                            idJugador,
                            fecha.getFecha()
                    );

                    /*
                     * Crear la Calculadora que necesita
                     * la Partida para poder existir.
                     */
                    Calculadora calculadora =
                            crearCalculadora();

                    Partida partida = new Partida(
                            rs.getInt("id"),
                            jugador,
                            calculadora,
                            fecha.getFecha(),
                            rs.getTime("tiempo")
                                    .toLocalTime()
                                    .toSecondOfDay(),
                            rs.getInt("puntaje")
                    );

                    cargarResultados(partida);

                    jugador.agregarPartida(partida);

                    partidas.add(partida);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar últimas partidas: "
                            + e.getMessage()
            );
        }

        return partidas;
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

            ps.setInt(
                    1,
                    partida.getId()
            );

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Resultado resultado =
                            new Resultado(
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
                    "Error al cargar resultados: "
                            + e.getMessage()
            );
        }
    }

    /**
     * Crea una partida en la base de datos.
     *
     * @param objeto partida que se desea crear
     * @return true si se creó correctamente
     */
    @Override
    public boolean crear(Partida objeto) {

        String sql = """
                INSERT INTO partida
                (jugador_id, fecha, tiempo, puntaje)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(
                    1,
                    objeto.getJugador().getId()
            );

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            objeto.getFechaPartida()
                                    .atStartOfDay()
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

            ps.setInt(
                    4,
                    objeto.getPuntaje()
            );

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs =
                             ps.getGeneratedKeys()) {

                    if (rs.next()) {

                        int idGenerado =
                                rs.getInt(1);

                        objeto.setId(idGenerado);

                        System.out.println(
                                "Partida creada con ID: "
                                        + idGenerado
                        );
                    }
                }

                return true;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear partida: "
                            + e.getMessage()
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
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                ORDER BY id
                """;

        try (PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                LocalDateHelper fecha =
                        new LocalDateHelper(
                                rs.getTimestamp("fecha")
                        );

                Jugador jugador = new Jugador(
                        rs.getInt("jugador_id"),
                        fecha.getFecha()
                );

                /*
                 * La Partida necesita una Calculadora.
                 */
                Calculadora calculadora =
                        crearCalculadora();

                Partida partida = new Partida(
                        rs.getInt("id"),
                        jugador,
                        calculadora,
                        fecha.getFecha(),
                        rs.getTime("tiempo")
                                .toLocalTime()
                                .toSecondOfDay(),
                        rs.getInt("puntaje")
                );

                cargarResultados(partida);

                jugador.agregarPartida(partida);

                partidas.add(partida);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar partidas: "
                            + e.getMessage()
            );
        }

        return partidas;
    }

    /**
     * Busca una partida por su identificador.
     *
     * @param id identificador de la partida
     * @return partida encontrada o null si no existe
     */
    @Override
    public Partida buscarPorId(int id) {

        String sql = """
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                WHERE id = ?
                """;

        try (PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    LocalDateHelper fecha =
                            new LocalDateHelper(
                                    rs.getTimestamp("fecha")
                            );

                    Jugador jugador = new Jugador(
                            rs.getInt("jugador_id"),
                            fecha.getFecha()
                    );

                    /*
                     * La Partida necesita una Calculadora.
                     */
                    Calculadora calculadora =
                            crearCalculadora();

                    Partida partida = new Partida(
                            rs.getInt("id"),
                            jugador,
                            calculadora,
                            fecha.getFecha(),
                            rs.getTime("tiempo")
                                    .toLocalTime()
                                    .toSecondOfDay(),
                            rs.getInt("puntaje")
                    );

                    cargarResultados(partida);

                    jugador.agregarPartida(partida);

                    return partida;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar partida: "
                            + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Actualiza una partida existente.
     *
     * @param objeto partida que se desea actualizar
     * @return true si se actualizó correctamente
     */
    @Override
    public boolean actualizar(Partida objeto) {

        String sql = """
                UPDATE partida
                SET jugador_id = ?,
                    fecha = ?,
                    tiempo = ?,
                    puntaje = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    objeto.getJugador().getId()
            );

            ps.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            objeto.getFechaPartida()
                                    .atStartOfDay()
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

            ps.setInt(
                    4,
                    objeto.getPuntaje()
            );

            ps.setInt(
                    5,
                    objeto.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar partida: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Elimina una partida.
     *
     * @param id identificador de la partida
     * @return true si se eliminó correctamente
     */
    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM partida
                WHERE id = ?
                """;

        try (PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar partida: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Crea una Calculadora vacía para reconstruir una Partida
     * obtenida desde la base de datos.
     *
     * La información de la Calculadora no se almacena actualmente
     * en la tabla partida, por lo que se crea con valores iniciales.
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

    /**
     * Clase auxiliar para convertir una fecha SQL
     * a LocalDate.
     */
    private static class LocalDateHelper {

        private final LocalDate fecha;

        /**
         * Constructor.
         *
         * @param timestamp fecha obtenida de SQL
         */
        public LocalDateHelper(Timestamp timestamp) {

            this.fecha = timestamp
                    .toLocalDateTime()
                    .toLocalDate();
        }

        /**
         * Obtiene la fecha convertida.
         *
         * @return fecha
         */
        public LocalDate getFecha() {
            return fecha;
        }
    }
}
