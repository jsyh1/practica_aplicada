/**
 * Define el módulo principal de la aplicación de gestión de drones.
 *
 * <p>
 * Este módulo establece las dependencias necesarias para utilizar
 * JavaFX, acceder a la base de datos y gestionar las variables
 * de entorno de la aplicación.
 * </p>
 *
 * <p>
 * También configura los paquetes que pueden utilizarse mediante
 * reflexión desde JavaFX y los paquetes que se exponen a otros módulos.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
module co.edu.poli.juego {

    /**
     * Permite utilizar los componentes y controles de JavaFX.
     */
    requires javafx.controls;

    /**
     * Permite utilizar archivos FXML para definir interfaces gráficas.
     */
    requires javafx.fxml;

    /**
     * Permite utilizar las funcionalidades de acceso a bases de datos
     * mediante JDBC.
     */
    requires java.sql;

    /**
     * Permite gestionar variables de entorno mediante la biblioteca
     * dotenv-java.
     */
    requires io.github.cdimascio.dotenv.java;
    requires javafx.graphics;

    /**
     * Permite que JavaFX acceda mediante reflexión a las clases
     * del paquete de la interfaz gráfica.
     */
    opens co.edu.poli.vista to javafx.fxml;

    /**
     * Permite que JavaFX acceda mediante reflexión a las clases
     * del paquete de controladores.
     */
    opens co.edu.poli.controller to javafx.fxml;

    /**
     * Expone el paquete de la interfaz gráfica para que pueda
     * ser utilizado por otros módulos.
     */
    exports co.edu.poli.vista;

    /**
     * Expone el paquete de controladores para que pueda
     * ser utilizado por otros módulos.
     */
    exports co.edu.poli.controller;
    
    // ====== AÑADE ESTAS LÍNEAS CON TUS PAQUETES FALTANTES ======
    exports co.edu.poli.modelo;    // Reemplaza si tu paquete se llama diferente (ej. modelo)
    exports co.edu.poli.servicios;  // Reemplaza si tu paquete se llama diferente (ej. servicios)
    // ===========================================================

}