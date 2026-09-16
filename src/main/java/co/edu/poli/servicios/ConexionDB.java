package co.edu.poli.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Clase Singleton encargada de gestionar la conexión con la base de datos
 * MySQL.
 *
 * <p>
 * El patrón Singleton garantiza que solamente exista una instancia de esta
 * clase durante la ejecución del programa.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class ConexionDB {

	/**
	 * Única instancia de la clase {@code db}.
	 */
	private static ConexionDB instancia;

	private static final Dotenv dotenv = Dotenv.configure().load();

	private static final String URL = dotenv.get("DB_URL");

	private static final String USUARIO = dotenv.get("DB_USUARIO");

	private static final String CLAVE = dotenv.get("DB_CLAVE");

	/**
	 * Objeto que representa la conexión activa con la base de datos.
	 */
	private Connection conexion;

	/**
	 * Constructor privado que impide que otras clases creen directamente objetos
	 * de tipo {@code db}.
	 *
	 * <p>
	 * Al crear la única instancia de la clase, se establece automáticamente
	 * la conexión con la base de datos.
	 * </p>
	 */
	private ConexionDB() {
		conectar();
	}

	/**
	 * Obtiene la única instancia de la clase {@code db}.
	 *
	 * <p>
	 * Si la instancia aún no existe, se crea utilizando el constructor privado.
	 * </p>
	 *
	 * @return instancia única de {@code db}
	 */
	public static ConexionDB getInstancia() {

		if (instancia == null) {
			instancia = new ConexionDB();
		}

		return instancia;
	}

	/**
	 * Establece una conexión con la base de datos utilizando las credenciales
	 * configuradas.
	 *
	 * <p>
	 * Si ocurre un error durante la conexión, se muestra el mensaje correspondiente
	 * en la consola.
	 * </p>
	 *
	 * @return conexión establecida o {@code null} si ocurre un error
	 */
	private Connection conectar() {

		try {
			conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);

			System.out.println("Conexion exitosa a la base de datos");

		} catch (SQLException e) {

			System.out.println("Error al conectar a la base de datos");

			System.out.println(e.getMessage());
		}

		return conexion;
	}

	/**
	 * Obtiene la conexión actual con la base de datos.
	 *
	 * @return objeto {@link Connection} utilizado para acceder a la base de datos
	 */
	public Connection getConexion() {
		return conexion;
	}

	/**
	 * Cierra la conexión activa con la base de datos.
	 *
	 * <p>
	 * La conexión solamente se cierra si existe y actualmente se encuentra abierta.
	 * </p>
	 */
	public void cerrarConexion() {

		try {

			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
				System.out.println("Conexion cerrada correctamente");
			}

		} catch (SQLException e) {

			System.out.println("Error al cerrar la conexion");

			System.out.println(e.getMessage());
		}
	}
}