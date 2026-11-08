package co.edu.poli.servicios;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

/** Pruebas de conexión y disponibilidad de la base de datos. */
class ConexionDBTest {

    @Test
    void probarConexionExitosa() {

        ConexionDB db = ConexionDB.getInstancia();

        Connection conexion = db.getConexion();

        assertNotNull(conexion,
                "La conexión a la base de datos no debería ser null");
    }

    @Test
    void probarConexionValida() throws SQLException {

        ConexionDB db = ConexionDB.getInstancia();

        Connection conexion = db.getConexion();

        assertNotNull(conexion,
                "La conexión no debería ser null");

        assertFalse(conexion.isClosed(),
                "La conexión debería estar abierta");

        assertTrue(conexion.isValid(2),
                "La conexión debería ser válida");
    }

    @Test
    void probarCerrarConexion() throws SQLException {

        ConexionDB db = ConexionDB.getInstancia();

        Connection conexion = db.getConexion();

        assertNotNull(conexion);

        db.cerrarConexion();

        assertTrue(conexion.isClosed(),
                "La conexión debería estar cerrada");
    }
}
