package co.edu.poli.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Calculadora.
 *
 * Verifica el cálculo de expresiones aritméticas, el manejo
 * de raíces y potencias, y el tratamiento de expresiones inválidas.
 */
class CalculadoraTest {

    private Calculadora calculadora;

    /**
     * Inicializa la calculadora antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        calculadora = new Calculadora(new String[9], new int[4], "");
    }

    /**
     * Comprueba la suma de dos números.
     */
    @Test
    void calcularSuma() {
        assertEquals(5.0, calcular("2+3"), 0.000001);
    }

    /**
     * Comprueba la resta de dos números.
     */
    @Test
    void calcularResta() {
        assertEquals(3.0, calcular("8-5"), 0.000001);
    }

    /**
     * Comprueba la multiplicación de dos números.
     */
    @Test
    void calcularMultiplicacion() {
        assertEquals(24.0, calcular("6*4"), 0.000001);
    }

    /**
     * Comprueba la división de dos números.
     */
    @Test
    void calcularDivision() {
        assertEquals(4.0, calcular("20/5"), 0.000001);
    }

    /**
     * Comprueba que se respete la prioridad de la multiplicación.
     */
    @Test
    void respetaPrioridadDeOperaciones() {
        assertEquals(14.0, calcular("2+3*4"), 0.000001);
    }

    /**
     * Comprueba el uso de paréntesis.
     */
    @Test
    void calculaExpresionConParentesis() {
        assertEquals(20.0, calcular("(2+3)*4"), 0.000001);
    }

    /**
     * Comprueba la potencia y su prioridad sobre la multiplicación.
     */
    @Test
    void calculaPotencia() {
        assertEquals(16.0, calcular("2^3*2"), 0.000001);
    }

    /**
     * Comprueba que los paréntesis se evalúen antes de la potencia.
     */
    @Test
    void calculaPotenciaConParentesis() {
        assertEquals(25.0, calcular("(2+3)^2"), 0.000001);
    }

    /**
     * Comprueba que la potencia tenga prioridad sobre la suma.
     */
    @Test
    void respetaPrioridadDePotencia() {
        assertEquals(11.0, calcular("2+3^2"), 0.000001);
    }

    /**
     * Comprueba el cálculo de una raíz cuadrada.
     */
    @Test
    void calculaRaizCuadrada() {
        assertEquals(3.0, calcular("√(9)"), 0.000001);
    }

    /**
     * Comprueba una raíz cuadrada dentro de una expresión.
     */
    @Test
    void calculaRaizDentroDeExpresion() {
        assertEquals(6.0, calcular("√(16)+2"), 0.000001);
    }

    /**
     * Comprueba una expresión que combina raíces y potencias.
     */
    @Test
    void combinaRaizYPotencia() {
        assertEquals(11.0, calcular("√(9)+2^3"), 0.000001);
    }

    /**
     * Comprueba que dividir entre cero produzca un error.
     */
    @Test
    void divisionPorCeroLanzaExcepcion() {
        assertThrows(ArithmeticException.class, () -> calcular("8/0"));
    }

    /**
     * Comprueba que una ecuación nula se rechace al crear o configurar la calculadora.
     */
    @Test
    void ecuacionNulaLanzaExcepcionDeArgumento() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Calculadora(new String[9], new int[4], null)
        );
        assertThrows(IllegalArgumentException.class, () -> calculadora.setEcuacion(null));
    }

    /**
     * Comprueba que una raíz de un número negativo no sea válida.
     */
    @Test
    void raizDeNumeroNegativoLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calcular("√(0-1)")
        );
    }

    /**
     * Comprueba que una expresión incompleta no se acepte.
     */
    @Test
    void expresionIncompletaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> calcular("2+"));
    }

    /**
     * Comprueba que el contenido numérico mal formado conserve su excepción específica.
     */
    @Test
    void numeroMalFormadoLanzaExcepcionDeFormato() {
        assertThrows(NumberFormatException.class, () -> calcular("1..2"));
    }

    /**
     * Comprueba que no se acepte una base negativa para una potencia.
     */
    @Test
    void potenciaConBaseNegativaLanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calcular("(0-2)^2")
        );
    }

    /**
     * Comprueba que el resultado de una potencia sea finito.
     */
    @Test
    void potenciaExcesivaLanzaExcepcion() {
        assertThrows(ArithmeticException.class, () -> calcular("999999^10"));
    }

    /**
     * Comprueba que la ayuda pueda calcularse con los números que aún están
     * disponibles sin exigir volver a utilizar los números ya retirados.
     */
    @Test
    void buscaSolucionesConNumerosDisponibles() {
        int[] disponibles = {2, 3};

        List<String> soluciones = calculadora.buscarSoluciones(disponibles);

        assertTrue(soluciones.contains("(2 + 3) = 5") || soluciones.contains("(3 + 2) = 5"));
        assertEquals(2, disponibles.length);
        assertEquals(2, disponibles[0]);
        assertEquals(3, disponibles[1]);
    }

    /**
     * Comprueba que la búsqueda avanzada acepte una cantidad parcial de números
     * para representar el estado actual de la partida.
     */
    @Test
    void buscaAyudaAvanzadaConNumerosDisponibles() {
        int[] disponibles = {2, 3};

        List<String> soluciones = calculadora.buscarSolucionesConRaicesYPotencias(disponibles);

        assertTrue(soluciones.stream().anyMatch(solucion -> solucion.endsWith(" = 5")));
        assertEquals(2, disponibles.length);
    }

    /**
     * Comprueba que el generador normal consuma cada uno de los cuatro números
     * exactamente una vez en cada ecuación encontrada.
     */
    @Test
    void generadorNormalUsaCadaNumeroUnaSolaVez() {
        int[] numeros = {1, 2, 3, 4};

        List<String> soluciones = calculadora.buscarSoluciones(numeros);

        assertEquals(10, soluciones.size());
        soluciones.forEach(solucion -> assertUsaCadaNumeroUnaVez(solucion, numeros));
    }

    /**
     * Comprueba la misma regla para el generador avanzado, incluyendo raíces y
     * potencias.
     */
    @Test
    void generadorAvanzadoUsaCadaNumeroUnaSolaVez() {
        int[] numeros = {1, 3, 4, 5};

        List<String> soluciones = calculadora.buscarSolucionesConRaicesYPotencias(numeros);

        assertEquals(10, soluciones.size());
        soluciones.forEach(solucion -> assertUsaCadaNumeroUnaVez(solucion, numeros));
    }

    private void assertUsaCadaNumeroUnaVez(String solucion, int[] numeros) {
        String expresion = solucion.substring(0, solucion.indexOf(" = "));
        List<Integer> numerosUsados = new ArrayList<>();
        var matcher = Pattern.compile("(?<!\\d)\\d+(?!\\d)").matcher(expresion);

        while (matcher.find()) {
            numerosUsados.add(Integer.parseInt(matcher.group()));
        }

        assertEquals(
                java.util.Arrays.stream(numeros).sorted().boxed().toList(),
                numerosUsados.stream().sorted().toList(),
                () -> "La ecuación debe usar únicamente los números disponibles una vez: " + solucion
        );
    }

    /**
     * Evalúa una expresión asignándola previamente a la calculadora.
     *
     * @param ecuacion expresión matemática que se desea evaluar
     * @return resultado numérico de la expresión
     */
    private double calcular(String ecuacion) {
        calculadora.setEcuacion(ecuacion);
        return calculadora.calcular();
    }
}
