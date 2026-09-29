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

import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
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
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                ORDER BY id DESC
                LIMIT ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cantidad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int idJugador = rs.getInt("jugador_id");

                    LocalDateHelper fecha = new LocalDateHelper(
                            rs.getTimestamp("fecha")
                    );

                    Jugador jugador = new Jugador(
                            idJugador,
                            fecha.getFecha()
                    );

                    Partida partida = new Partida(
                            rs.getInt("id"),
                            jugador,
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
                    "Error al cargar resultados: "
                    + e.getMessage()
            );
        }
    }

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

            ps.setInt(
                    4,
                    objeto.getPuntaje()
            );

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {

                        int idGenerado = rs.getInt(1);

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

    @Override
    public List<Partida> listar() {

        List<Partida> partidas = new ArrayList<>();

        String sql = """
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                ORDER BY id
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                LocalDateHelper fecha = new LocalDateHelper(
                        rs.getTimestamp("fecha")
                );

                Jugador jugador = new Jugador(
                        rs.getInt("jugador_id"),
                        fecha.getFecha()
                );

                Partida partida = new Partida(
                        rs.getInt("id"),
                        jugador,
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

    @Override
    public Partida buscarPorId(int id) {

        String sql = """
                SELECT id, jugador_id, fecha, tiempo, puntaje
                FROM partida
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    LocalDateHelper fecha = new LocalDateHelper(
                            rs.getTimestamp("fecha")
                    );

                    Jugador jugador = new Jugador(
                            rs.getInt("jugador_id"),
                            fecha.getFecha()
                    );

                    Partida partida = new Partida(
                            rs.getInt("id"),
                            jugador,
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

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    objeto.getJugador().getId()
            );

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
                    "Error al eliminar partida: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Clase auxiliar para convertir la fecha de SQL.
     */
    private static class LocalDateHelper {

        private final java.time.LocalDate fecha;

        public LocalDateHelper(Timestamp timestamp) {
            this.fecha = timestamp
                    .toLocalDateTime()
                    .toLocalDate();
        }

        public java.time.LocalDate getFecha() {
            return fecha;
        }
    }
}

