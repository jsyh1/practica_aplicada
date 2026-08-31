package co.edu.poli.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Operadores.
 */
public class OperadoresTest {

    /**
     * Prueba la suma de dos números.
     */
    @Test
    public void probarSuma() {

        Operadores op = new Operadores("10+5");

        double resultado = op.calcular();

        assertEquals(15, resultado);
    }

    /**
     * Prueba la resta de dos números.
     */
    @Test
    public void probarResta() {

        Operadores op = new Operadores("10-5");

        double resultado = op.calcular();

        assertEquals(5, resultado);
    }

    /**
     * Prueba la multiplicación de dos números.
     */
    @Test
    public void probarMultiplicacion() {

        Operadores op = new Operadores("10*5");

        double resultado = op.calcular();

        assertEquals(50, resultado);
    }

    /**
     * Prueba la división de dos números.
     */
    @Test
    public void probarDivision() {

        Operadores op = new Operadores("10/5");

        double resultado = op.calcular();

        assertEquals(2, resultado);
    }

    /**
     * Prueba la prioridad de la multiplicación.
     */
    @Test
    public void probarPrioridadMultiplicacion() {

        Operadores op = new Operadores("10+5*2");

        double resultado = op.calcular();

        assertEquals(20, resultado);
    }

    /**
     * Prueba el uso de paréntesis.
     */
    @Test
    public void probarParentesis() {

        Operadores op = new Operadores("(10+5)*2");

        double resultado = op.calcular();

        assertEquals(30, resultado);
    }

    /**
     * Prueba una ecuación con varias operaciones.
     */
    @Test
    public void probarEcuacionCompleta() {

        Operadores op = new Operadores("(10+5)*2-8/4");

        double resultado = op.calcular();

        assertEquals(28, resultado);
    }

    /**
     * Prueba una ecuación con paréntesis anidados.
     */
    @Test
    public void probarParentesisAnidados() {

        Operadores op = new Operadores("((10+5)*2)");

        double resultado = op.calcular();

        assertEquals(30, resultado);
    }

    /**
     * Prueba números negativos.
     */
    @Test
    public void probarNumeroNegativo() {

        Operadores op = new Operadores("-10+5");

        double resultado = op.calcular();

        assertEquals(-5, resultado);
    }

    /**
     * Prueba que no se permita dividir entre cero.
     */
    @Test
    public void probarDivisionPorCero() {

        Operadores op = new Operadores("10/0");

        assertThrows(
            ArithmeticException.class,
            () -> op.calcular()
        );
    }

    /**
     * Prueba que no se permita una ecuación con paréntesis sin cerrar.
     */
    @Test
    public void probarParentesisSinCerrar() {

        Operadores op = new Operadores("(10+5");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }

    /**
     * Prueba una ecuación vacía.
     */
    @Test
    public void probarEcuacionVacia() {

        Operadores op = new Operadores("");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }
}

