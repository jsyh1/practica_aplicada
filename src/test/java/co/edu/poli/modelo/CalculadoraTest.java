package co.edu.poli.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertThrows(RuntimeException.class, () -> calcular("2+"));
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
        assertThrows(RuntimeException.class, () -> calcular("999999^10"));
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
