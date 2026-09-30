package co.edu.poli.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Calculadora.
 *
 * @author Jsyh
 * @version 1.0
 */
class CalculadoraTest {

    /**
     * Verifica una suma sencilla.
     */
    @Test
    void testSuma() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "2+3"
        );

        assertEquals(5.0, calculadora.calcular());
    }

    /**
     * Verifica una resta.
     */
    @Test
    void testResta() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "8-3"
        );

        assertEquals(5.0, calculadora.calcular());
    }

    /**
     * Verifica una multiplicación.
     */
    @Test
    void testMultiplicacion() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "4*5"
        );

        assertEquals(20.0, calculadora.calcular());
    }

    /**
     * Verifica una división.
     */
    @Test
    void testDivision() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "10/2"
        );

        assertEquals(5.0, calculadora.calcular());
    }

    /**
     * Verifica la prioridad de operaciones.
     */
    @Test
    void testPrioridadOperaciones() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "2+3*4"
        );

        assertEquals(14.0, calculadora.calcular());
    }

    /**
     * Verifica el uso de paréntesis.
     */
    @Test
    void testParentesis() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "(2+3)*4"
        );

        assertEquals(20.0, calculadora.calcular());
    }

    /**
     * Verifica una expresión con varias operaciones.
     */
    @Test
    void testExpresionCompleja() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "10-(2+3)*2"
        );

        assertEquals(0.0, calculadora.calcular());
    }

    /**
     * Verifica que se puedan utilizar espacios en la ecuación.
     */
    @Test
    void testEspacios() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "2 + 3 * 4"
        );

        assertEquals(14.0, calculadora.calcular());
    }

    /**
     * Verifica la conversión de números enteros.
     */
    @Test
    void testConvertirEntero() {
        assertEquals("10", Calculadora.convertir(10.0));
    }

    /**
     * Verifica la conversión de un número decimal a fracción.
     */
    @Test
    void testConvertirFraccion() {
        assertEquals("1/2", Calculadora.convertir(0.5));
    }

    /**
     * Verifica que no se permitan divisiones por cero.
     */
    
    @Test
    void testDivisionPorCero() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "10/0"
        );

        assertThrows(
                ArithmeticException.class,
                calculadora::calcular
        );
    }
    

    /**
     * Verifica que una ecuación vacía produzca una excepción.
     */
    @Test
    void testEcuacionVacia() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                ""
        );

        assertThrows(
                IllegalArgumentException.class,
                calculadora::calcular
        );
    }

    /**
     * Verifica que no se permitan resultados negativos.
     */
    @Test
    void testResultadoNegativo() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "2-5"
        );

        assertThrows(
                IllegalArgumentException.class,
                calculadora::calcular
        );
    }

    /**
     * Verifica que la generación de símbolos produzca los nueve símbolos.
     */
    @Test
    void testGenerarSimbolos() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                ""
        );

        String[] simbolos = calculadora.generarSimbolos();

        assertNotNull(simbolos);
        assertEquals(9, simbolos.length);

        assertEquals("+", simbolos[0]);
        assertEquals("-", simbolos[1]);
        assertEquals("*", simbolos[2]);
        assertEquals("/", simbolos[3]);
        assertEquals("(", simbolos[4]);
        assertEquals(")", simbolos[5]);
        assertEquals("=", simbolos[6]);
    }

    /**
     * Verifica que se generen cuatro números.
     */
    @Test
    void testGenerarNumeros() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                ""
        );

        int[] numeros = calculadora.generarNumeros();

        assertNotNull(numeros);
        assertEquals(4, numeros.length);

        for (int numero : numeros) {
            assertTrue(numero >= 1 && numero <= 10);
        }
    }

    /**
     * Verifica que una combinación generada permita
     * obtener todos los resultados del 1 al 10.
     */
    @Test
    void testPuedeObtenerTodosLosResultados() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                ""
        );

        int[] numeros = calculadora.generarNumeros();

        assertTrue(
                calculadora.puedeObtenerTodosLosResultados(numeros)
        );
    }

    /**
     * Verifica que setEcuacion permita cambiar la ecuación
     * después de crear la calculadora.
     */
    @Test
    void testSetEcuacion() {
        Calculadora calculadora = new Calculadora(
                new String[9],
                new int[4],
                "2+2"
        );

        calculadora.calcular();

        calculadora.setEcuacion("5*3");

        assertEquals(15.0, calculadora.calcular());
    }
}