package co.edu.poli.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar la lógica principal del juego All Ten.
 *
 * <p>
 * La clase se encarga de:
 * </p>
 * <ul>
 * <li>Almacenar los símbolos disponibles para construir ecuaciones.</li>
 * <li>Generar los cuatro números utilizados en el juego.</li>
 * <li>Comprobar que los números generados permitan obtener los resultados
 * del 1 al 10.</li>
 * <li>Resolver ecuaciones matemáticas respetando la prioridad de operaciones.</li>
 * <li>Convertir resultados decimales a fracciones.</li>
 * </ul>
 *
 * <p>
 * Las operaciones matemáticas soportadas son:
 * </p>
 * <ul>
 * <li>Suma (+)</li>
 * <li>Resta (-)</li>
 * <li>Multiplicación (*)</li>
 * <li>División (/)</li>
 * <li>Paréntesis</li>
 * </ul>
 *
 * <p>
 * No se permiten resultados negativos ni divisiones entre cero.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class Calculadora {

    /**
     * Arreglo que contiene los símbolos disponibles para construir
     * las ecuaciones del juego.
     */
    private String[] simbolos;

    /**
     * Arreglo que contiene los cuatro números generados para el juego.
     */
    private int[] numeros;

    /**
     * Ecuación matemática que será procesada.
     */
    private String ecuacion;

    /**
     * Posición actual dentro de la ecuación durante el proceso de análisis.
     */
    private int posicion;

    /**
     * Construye una instancia del juego.
     *
     * @param simbolos arreglo de símbolos disponibles
     * @param numeros  arreglo de números disponibles
     * @param ecuacion ecuación matemática que se desea procesar
     */
    public Calculadora(String[] simbolos, int[] numeros, String ecuacion) {

        this.simbolos = simbolos;
        this.numeros = numeros;

        String sinEspacios = ecuacion.replace(" ", "");
        this.ecuacion = normalizar(sinEspacios);
        this.posicion = 0;
    }

    /**
     * Obtiene los símbolos disponibles del juego.
     *
     * @return arreglo de símbolos
     */
    public String[] getSimbolos() {
        return simbolos;
    }

    /**
     * Obtiene los números disponibles del juego.
     *
     * @return arreglo de números
     */
    public int[] getNumeros() {
        return numeros;
    }

    /**
     * Genera los símbolos utilizados por el juego.
     *
     * <p>
     * Los símbolos generados son:
     * </p>
     * <ul>
     * <li>{@code +} - Suma</li>
     * <li>{@code -} - Resta</li>
     * <li>{@code *} - Multiplicación</li>
     * <li>{@code /} - División</li>
     * <li>{@code (} - Paréntesis de apertura</li>
     * <li>{@code )} - Paréntesis de cierre</li>
     * <li>{@code =} - Igual</li>
     * <li>{@code ⌫} - Borrar último elemento</li>
     * <li>{@code ↶} - Borrar o regresar</li>
     * </ul>
     *
     * @return arreglo con los símbolos del juego
     */
    public String[] generarSimbolos() {

        this.simbolos = new String[9];

        this.simbolos[0] = "+";
        this.simbolos[1] = "-";
        this.simbolos[2] = "*";
        this.simbolos[3] = "/";
        this.simbolos[4] = "(";
        this.simbolos[5] = ")";
        this.simbolos[6] = "=";
        this.simbolos[7] = "⌫";
        this.simbolos[8] = "↶";

        return this.simbolos;
    }

    /**
     * Genera cuatro números aleatorios entre 1 y 10.
     *
     * <p>
     * Los números se generan repetidamente hasta encontrar una combinación
     * que permita obtener todos los resultados desde el 1 hasta el 10
     * utilizando operaciones matemáticas.
     * </p>
     *
     * @return arreglo con los cuatro números generados
     */
    public int[] generarNumeros() {

        do {

            this.numeros = new int[4];

            for (int i = 0; i < this.numeros.length; i++) {

                this.numeros[i] = (int) (Math.random() * 10) + 1;
            }

        } while (!esCombinacionValida(this.numeros));

        return this.numeros;
    }

    /**
     * Comprueba si una combinación de cuatro números permite obtener
     * todos los resultados desde el 1 hasta el 10.
     *
     * @param numeros números que serán evaluados
     * @return {@code true} si la combinación permite obtener todos los
     *         resultados del 1 al 10; {@code false} en caso contrario
     */
    private boolean esCombinacionValida(int[] numeros) {

        for (int objetivo = 1; objetivo <= 10; objetivo++) {

            if (!puedeObtenerResultado(numeros, objetivo)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Comprueba si un resultado específico puede obtenerse utilizando
     * los números disponibles.
     *
     * @param numeros  números disponibles para realizar las operaciones
     * @param objetivo resultado que se desea obtener
     * @return {@code true} si el resultado puede obtenerse; {@code false}
     *         en caso contrario
     */
    private boolean puedeObtenerResultado(int[] numeros, int objetivo) {

        List<Double> valores = new ArrayList<>();

        for (int numero : numeros) {
            valores.add((double) numero);
        }

        return buscarResultado(valores, objetivo);
    }

    /**
     * Busca recursivamente todas las combinaciones posibles de operaciones
     * entre los números disponibles.
     *
     * <p>
     * Para cada par de números se prueban las operaciones de suma, resta,
     * multiplicación y división. Los resultados obtenidos se vuelven a
     * utilizar recursivamente hasta obtener un único valor.
     * </p>
     *
     * @param valores  valores disponibles para realizar las operaciones
     * @param objetivo resultado que se desea encontrar
     * @return {@code true} si se encuentra una combinación que produce
     *         el objetivo; {@code false} en caso contrario
     */
    private boolean buscarResultado(List<Double> valores, int objetivo) {

        /*
         * Cuando solamente queda un valor se comprueba si corresponde
         * al resultado buscado.
         */
        if (valores.size() == 1) {

            double resultado = valores.get(0);

            return Math.abs(resultado - objetivo) < 0.000001;
        }

        /*
         * Se seleccionan dos valores diferentes para realizar
         * una operación entre ellos.
         */
        for (int i = 0; i < valores.size(); i++) {

            for (int j = i + 1; j < valores.size(); j++) {

                double a = valores.get(i);
                double b = valores.get(j);

                List<Double> restantes = new ArrayList<>();

                /*
                 * Se conservan los valores que no fueron seleccionados.
                 */
                for (int k = 0; k < valores.size(); k++) {

                    if (k != i && k != j) {
                        restantes.add(valores.get(k));
                    }
                }

                List<Double> resultados = new ArrayList<>();

                // Suma
                resultados.add(a + b);

                // Resta
                resultados.add(a - b);
                resultados.add(b - a);

                // Multiplicación
                resultados.add(a * b);

                // División a / b
                if (Math.abs(b) > 0.000001) {
                    resultados.add(a / b);
                }

                // División b / a
                if (Math.abs(a) > 0.000001) {
                    resultados.add(b / a);
                }

                /*
                 * Se prueba cada resultado obtenido de forma recursiva.
                 */
                for (double resultado : resultados) {

                    List<Double> nuevaLista =
                            new ArrayList<>(restantes);

                    nuevaLista.add(resultado);

                    if (buscarResultado(nuevaLista, objetivo)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Comprueba si los números proporcionados permiten obtener todos
     * los resultados desde el 1 hasta el 10.
     *
     * @param numeros números que se desean comprobar
     * @return {@code true} si todos los resultados del 1 al 10 pueden
     *         obtenerse; {@code false} en caso contrario
     */
    public boolean puedeObtenerTodosLosResultados(int[] numeros) {

        for (int objetivo = 1; objetivo <= 10; objetivo++) {

            if (!puedeObtenerResultado(numeros, objetivo)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Establece la ecuación que será procesada por la calculadora.
     *
     * @param ecuacion expresión matemática que se desea calcular
     */
    public void setEcuacion(String ecuacion) {

        if (ecuacion == null) {
            throw new IllegalArgumentException(
                "La ecuación no puede ser nula"
            );
        }

        String sinEspacios =
            ecuacion.replace(" ", "");

        this.ecuacion =
            normalizar(sinEspacios);

        /*
         * Reiniciar la posición para que la expresión
         * pueda ser procesada desde el principio.
         */
        this.posicion = 0;
    }
    
    // ============================================================
    // MÉTODOS PARA PROCESAR ECUACIONES
    // ============================================================

    /**
     * Normaliza una expresión matemática agregando automáticamente
     * el operador de multiplicación cuando existe una multiplicación
     * implícita.
     *
     * <p>
     * Algunos ejemplos son:
     * </p>
     * <ul>
     * <li>{@code 2(3)} se convierte en {@code 2*(3)}</li>
     * <li>{@code (2+1)(3)} se convierte en {@code (2+1)*(3)}</li>
     * <li>{@code (2+1)5} se convierte en {@code (2+1)*5}</li>
     * </ul>
     *
     * @param expresion expresión matemática que se desea normalizar
     * @return expresión normalizada
     */
    private String normalizar(String expresion) {

        String resultado = expresion;

        /*
         * Número o ')' seguido de '('.
         *
         * Ejemplos:
         * 2(2)      -> 2*(2)
         * (2+1)(3)  -> (2+1)*(3)
         */
        resultado = resultado.replaceAll(
                "(?<=[0-9)])(?=\\()",
                "*"
        );

        /*
         * ')' seguido de un número.
         *
         * Ejemplo:
         * (2+1)5 -> (2+1)*5
         */
        resultado = resultado.replaceAll(
                "(?<=\\))(?=[0-9])",
                "*"
        );

        return resultado;
    }

    /**
     * Procesa y resuelve la ecuación matemática almacenada.
     *
     * <p>
     * La ecuación se procesa respetando la prioridad de operaciones:
     * primero multiplicaciones y divisiones y posteriormente sumas y restas.
     * </p>
     *
     * @return resultado numérico de la ecuación
     *
     * @throws IllegalArgumentException si la ecuación está vacía, es inválida,
     *                                  contiene números negativos o presenta
     *                                  una estructura incorrecta
     * @throws ArithmeticException si se intenta dividir entre cero
     */
    public double calcular() {

        if (ecuacion == null || ecuacion.isEmpty()) {
            throw new IllegalArgumentException(
                    "La ecuación está vacía"
            );
        }

        double resultado = expresion();

        if (posicion < ecuacion.length()) {
            throw new IllegalArgumentException(
                    "Ecuación inválida"
            );
        }

        if (resultado < 0) {
            throw new IllegalArgumentException(
                    "No se permiten números negativos"
            );
        }

        return resultado;
    }

    /**
     * Procesa las operaciones de suma y resta.
     *
     * <p>
     * Este método utiliza {@link #termino()} para garantizar que las
     * multiplicaciones y divisiones tengan mayor prioridad que las
     * sumas y restas.
     * </p>
     *
     * @return resultado de la expresión matemática
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
     * Procesa las operaciones de multiplicación y división.
     *
     * <p>
     * Los factores utilizados son obtenidos mediante {@link #factor()}.
     * </p>
     *
     * @return resultado del término matemático
     *
     * @throws ArithmeticException si se intenta dividir entre cero
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
                    throw new ArithmeticException(
                            "No se puede dividir entre cero"
                    );
                }

                resultado /= divisor;

            } else {

                break;
            }
        }

        return resultado;
    }

    /**
     * Procesa un factor de la expresión matemática.
     *
     * <p>
     * Un factor puede ser un número o una expresión contenida dentro
     * de paréntesis.
     * </p>
     *
     * <p>
     * No se permiten números negativos. El signo {@code +} puede utilizarse
     * como signo positivo antes de un factor.
     * </p>
     *
     * @return resultado del factor procesado
     *
     * @throws IllegalArgumentException si falta un número, falta cerrar
     *                                  un paréntesis o se intenta utilizar
     *                                  un número negativo
     */
    private double factor() {

        if (posicion >= ecuacion.length()) {
            throw new IllegalArgumentException(
                    "Falta un número"
            );
        }

        char caracter = ecuacion.charAt(posicion);

        /*
         * Paréntesis de apertura.
         */
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

        /*
         * No se permiten números negativos.
         */
        if (caracter == '-') {

            throw new IllegalArgumentException(
                    "No se permiten números negativos"
            );
        }

        /*
         * Permitir números positivos precedidos por '+'.
         */
        if (caracter == '+') {

            posicion++;

            return factor();
        }

        return numero();
    }

    /**
     * Lee y convierte un número de la ecuación.
     *
     * <p>
     * El método permite números enteros y números decimales utilizando
     * el punto como separador decimal.
     * </p>
     *
     * @return número encontrado convertido a {@code double}
     *
     * @throws IllegalArgumentException si no se encuentra un número válido
     *                                  en la posición actual
     * @throws NumberFormatException si el contenido encontrado no puede
     *                               convertirse en un número
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
                    "Se esperaba un número en la posición "
                            + posicion
            );
        }

        return Double.parseDouble(
                ecuacion.substring(inicio, posicion)
        );
    }

    /**
     * Convierte un número decimal a una representación fraccionaria.
     *
     * <p>
     * Si el número es entero, se devuelve directamente como entero.
     * Si es decimal, se utiliza inicialmente un denominador de 1000
     * y posteriormente se simplifica la fracción utilizando el máximo
     * común divisor.
     * </p>
     *
     * <p>
     * Ejemplos:
     * </p>
     * <ul>
     * <li>{@code 5.0} se convierte en {@code "5"}</li>
     * <li>{@code 0.5} se convierte en {@code "1/2"}</li>
     * <li>{@code 0.25} se convierte en {@code "1/4"}</li>
     * </ul>
     *
     * @param numero número decimal que se desea convertir
     * @return representación del número como entero o fracción
     */
    public static String convertir(double numero) {

        /*
         * Si el resultado es entero.
         */
        if (numero == Math.floor(numero)) {

            return String.valueOf((int) numero);
        }

        int denominador = 1000;

        int numerador = (int) Math.round(
                numero * denominador
        );

        int mcd = calcularMCD(
                numerador,
                denominador
        );

        numerador /= mcd;
        denominador /= mcd;

        return numerador + "/" + denominador;
    }

    /**
     * Calcula el máximo común divisor (MCD) entre dos números.
     *
     * <p>
     * Para realizar el cálculo se utiliza el algoritmo de Euclides.
     * </p>
     *
     * @param a primer número entero
     * @param b segundo número entero
     * @return máximo común divisor entre {@code a} y {@code b}
     */
    private static int calcularMCD(int a, int b) {

        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {

            int temporal = b;

            b = a % b;
            a = temporal;
        }

        return a;
    }
}
