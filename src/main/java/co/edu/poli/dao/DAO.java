package co.edu.poli.dao;

import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD.
 *
 * @param <T> tipo de objeto que manejará el DAO
 * @author Jsyh
 * @version 1.0
 */
public interface DAO<T> {

	/**
	 * Crea un objeto en la base de datos.
	 *
	 * @param objeto objeto que se desea crear
	 * @return true si se creó correctamente
	 */
	boolean crear(T objeto);

	/**
	 * Obtiene todos los objetos.
	 *
	 * @return lista de objetos
	 */
	List<T> listar();

	/**
	 * Busca un objeto por su identificador.
	 *
	 * @param id identificador
	 * @return objeto encontrado
	 */
	T buscarPorId(int id);

	/**
	 * Actualiza un objeto.
	 *
	 * @param objeto objeto con los nuevos datos
	 * @return true si se actualizó correctamente
	 */
	boolean actualizar(T objeto);

	/**
	 * Elimina un objeto.
	 *
	 * @param id identificador del objeto
	 * @return true si se eliminó correctamente
	 */
	boolean eliminar(int id);
}