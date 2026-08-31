package co.edu.poli.servicios;

/**
 * Clase encargada de procesar y resolver ecuaciones matemáticas.
 * 
 * Soporta:
 * - Suma (+)
 * - Resta (-)
 * - Multiplicación (*)
 * - División (/)
 * - Paréntesis ()
 * 
 * Respeta la prioridad de operaciones matemáticas.
 */
public class Operadores {

    private String ecuacion;
    private int posicion;

    /**
     * Constructor de la clase Operadores.
     *
     * @param ecuacion ecuación que se desea resolver
     */
    public Operadores(String ecuacion) {
        this.ecuacion = ecuacion.replace(" ", "");
        this.posicion = 0;
    }

    /**
     * Procesa la ecuación y retorna el resultado.
     *
     * @return resultado de la ecuación
     * @throws IllegalArgumentException si la ecuación es inválida
     * @throws ArithmeticException si se intenta dividir entre cero
     */
    public double calcular() {

        if (ecuacion == null || ecuacion.isEmpty()) {
            throw new IllegalArgumentException("La ecuación está vacía");
        }

        double resultado = expresion();

        if (posicion < ecuacion.length()) {
            throw new IllegalArgumentException("Ecuación inválida");
        }

        return resultado;
    }

    /**
     * Procesa sumas y restas.
     *
     * @return resultado de la expresión
     */
    private double expresion() {

        double resultado = termino();

        while (posicion < ecuacion.length()) {

            char operador = ecuacion.charAt(posicion);

            if (operador == '+') {
                posicion++;
                resultado += termino();

            } else if (operador == '-') {
                posicion++;
                resultado -= termino();

            } else {
                break;
            }
        }

        return resultado;
    }

    /**
     * Procesa multiplicaciones y divisiones.
     *
     * @return resultado del término
     */
    private double termino() {

        double resultado = factor();

        while (posicion < ecuacion.length()) {

            char operador = ecuacion.charAt(posicion);

            if (operador == '*') {
                posicion++;
                resultado *= factor();

            } else if (operador == '/') {
                posicion++;

                double divisor = factor();

                if (divisor == 0) {
                    throw new ArithmeticException("No se puede dividir entre cero");
                }

                resultado /= divisor;

            } else {
                break;
            }
        }

        return resultado;
    }

    /**
     * Procesa números y expresiones dentro de paréntesis.
     *
     * @return resultado del factor
     */
    private double factor() {

        if (posicion >= ecuacion.length()) {
            throw new IllegalArgumentException("Falta un número");
        }

        char caracter = ecuacion.charAt(posicion);

        // Paréntesis de apertura
        if (caracter == '(') {

            posicion++;

            double resultado = expresion();

            if (posicion >= ecuacion.length()
                    || ecuacion.charAt(posicion) != ')') {

                throw new IllegalArgumentException(
                        "Falta cerrar el paréntesis"
                );
            }

            posicion++;

            return resultado;
        }

        // Números negativos
        if (caracter == '-') {
            posicion++;
            return -factor();
        }

        // Números positivos
        if (caracter == '+') {
            posicion++;
            return factor();
        }

        return numero();
    }

    /**
     * Lee un número de la ecuación.
     *
     * @return número encontrado
     */
    private double numero() {

        int inicio = posicion;

        while (posicion < ecuacion.length()) {

            char caracter = ecuacion.charAt(posicion);

            if ((caracter >= '0' && caracter <= '9')
                    || caracter == '.') {

                posicion++;

            } else {
                break;
            }
        }

        if (inicio == posicion) {
            throw new IllegalArgumentException(
                    "Se esperaba un número en la posición " + posicion
            );
        }

        return Double.parseDouble(
                ecuacion.substring(inicio, posicion)
        );
    }
}
