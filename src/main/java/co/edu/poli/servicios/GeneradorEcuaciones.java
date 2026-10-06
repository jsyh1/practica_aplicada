package co.edu.poli.servicios;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de construir ecuaciones por partes.
 *
 * <p>
 * Permite almacenar diferentes expresiones calculadas durante
 * una jugada y unirlas para formar una única ecuación completa.
 * </p>
 *
 * <p>
 * Ejemplo:
 * <br>
 * 2 + 4 = 6
 * <br>
 * 6 - 3 + 1 = 4
 * <br>
 * Ecuación completa:
 * <br>
 * 2 + 4 = 6 - 3 + 1 = 4
 * </p>
 */
public class GeneradorEcuaciones {

    /**
     * Lista que almacena las expresiones calculadas.
     */
    private final List<String> partes;

    /**
     * Constructor de la clase.
     *
     * <p>
     * Inicializa la lista de partes de la ecuación.
     * </p>
     */
    public GeneradorEcuaciones() {
        partes = new ArrayList<>();
    }

    /**
     * Agrega una expresión y su resultado a la ecuación.
     *
     * @param expresion expresión que se calculó
     * @param resultado resultado obtenido
     */
    public void agregarParte(String expresion, String resultado) {

        if (expresion == null || expresion.isBlank()) {
            return;
        }

        if (resultado == null || resultado.isBlank()) {
            return;
        }

        partes.add(expresion + " = " + resultado);
    }

    /**
     * Construye la ecuación completa.
     *
     * <p>
     * La primera expresión se conserva completa. En las siguientes
     * expresiones, si comienzan con el resultado anterior, este se
     * elimina para evitar repetirlo.
     * </p>
     *
     * <p>
     * Ejemplo:
     * <br>
     * 2 + 4 = 6
     * <br>
     * 6 - 3 + 1 = 4
     * <br>
     * Resultado:
     * <br>
     * 2 + 4 = 6 - 3 + 1 = 4
     * </p>
     *
     * @return ecuación completa
     */
    public String obtenerEcuacion() {

        if (partes.isEmpty()) {
            return "";
        }

        StringBuilder ecuacion = new StringBuilder();

        for (int i = 0; i < partes.size(); i++) {

            String parte = partes.get(i);

            int posicionIgual = parte.lastIndexOf("=");

            if (posicionIgual == -1) {
                continue;
            }

            String expresion = parte.substring(0, posicionIgual).trim();
            String resultado = parte.substring(posicionIgual + 1).trim();

            if (i == 0) {

                ecuacion.append(expresion)
                        .append(" = ")
                        .append(resultado);

            } else {

                String resultadoAnterior = obtenerResultadoAnterior(i - 1);

                if (expresion.startsWith(resultadoAnterior)) {
                    expresion = expresion.substring(
                            resultadoAnterior.length()
                    ).trim();
                }

                ecuacion.append(" ")
                        .append(expresion)
                        .append(" = ")
                        .append(resultado);
            }
        }

        return ecuacion.toString();
    }

    /**
     * Obtiene el resultado de una parte específica.
     *
     * @param indice índice de la parte
     * @return resultado de la parte
     */
    private String obtenerResultadoAnterior(int indice) {

        if (indice < 0 || indice >= partes.size()) {
            return "";
        }

        String parte = partes.get(indice);

        int posicionIgual = parte.lastIndexOf("=");

        if (posicionIgual == -1) {
            return "";
        }

        return parte.substring(posicionIgual + 1).trim();
    }

    /**
     * Obtiene las partes almacenadas.
     *
     * @return copia de las partes almacenadas
     */
    public List<String> obtenerPartes() {
        return new ArrayList<>(partes);
    }

    /**
     * Verifica si no existen partes almacenadas.
     *
     * @return true si está vacío, false en caso contrario
     */
    public boolean estaVacio() {
        return partes.isEmpty();
    }

    /**
     * Elimina todas las partes almacenadas.
     */
    public void limpiar() {
        partes.clear();
    }
}