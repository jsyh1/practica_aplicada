package co.edu.poli.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.modelo.Jugador;
import co.edu.poli.modelo.Partida;
import co.edu.poli.modelo.Resultado;
import co.edu.poli.servicios.ConexionDB;

public class DaoResultadoImplementado implements ResultadoDAO {

    private final Connection conexion;

    public DaoResultadoImplementado() {
        conexion = ConexionDB.getInstancia().getConexion();
    }

    @Override
    public boolean crear(Resultado objeto) {

        String sql = """
                INSERT INTO resultado
                (partida_id, resultado, dato)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(
                sql,
                java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(
                    1,
                    objeto.getPartida().getId()
            );

            ps.setString(
                    2,
                    objeto.getResultado()
            );

            ps.setInt(
                    3,
                    objeto.getDato()
            );

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
                    "Error al crear resultado: "
                    + e.getMessage()
            );
        }

        return false;
    }

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
                    p.fecha,
                    p.tiempo,
                    p.puntaje
                FROM resultado r
                INNER JOIN partida p
                    ON r.partida_id = p.id
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
                    "Error al listar resultados: "
                    + e.getMessage()
            );
        }

        return resultados;
    }

    /**
     * Busca un resultado por su identificador.
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
                    p.fecha,
                    p.tiempo,
                    p.puntaje
                FROM resultado r
                INNER JOIN partida p
                    ON r.partida_id = p.id
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
                    "Error al buscar resultado: "
                    + e.getMessage()
            );
        }

        return null;
    }

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

            ps.setInt(
                    1,
                    objeto.getPartida().getId()
            );

            ps.setString(
                    2,
                    objeto.getResultado()
            );

            ps.setInt(
                    3,
                    objeto.getDato()
            );

            ps.setInt(
                    4,
                    objeto.getId()
            );

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar resultado: "
                    + e.getMessage()
            );
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {

        String sql = """
                DELETE FROM resultado
                WHERE id = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar resultado: "
                    + e.getMessage()
            );
        }

        return false;
    }

    /**
     * Construye una partida utilizando los datos obtenidos
     * mediante la consulta SQL.
     *
     * @param rs resultado de la consulta
     * @return partida construida
     * @throws SQLException si ocurre un error al leer los datos
     */
    private Partida construirPartida(ResultSet rs) throws SQLException {

        int jugadorId = rs.getInt("jugador_id");

        LocalDate fecha = rs.getTimestamp("fecha")
                .toLocalDateTime()
                .toLocalDate();

        Jugador jugador = new Jugador(
                jugadorId,
                fecha
        );

        Partida partida = new Partida(
                rs.getInt("partida_id"),
                jugador,
                fecha,
                rs.getTime("tiempo")
                        .toLocalTime()
                        .toSecondOfDay(),
                rs.getInt("puntaje")
        );

        jugador.agregarPartida(partida);

        return partida;
    }
}
