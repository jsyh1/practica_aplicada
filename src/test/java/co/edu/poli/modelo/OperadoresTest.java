package co.edu.poli.modelo;

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

        Operador op = new Operador("10+5");

        double resultado = op.calcular();

        assertEquals(15, resultado);
    }

    /**
     * Prueba la resta de dos números.
     */
    @Test
    public void probarResta() {

        Operador op = new Operador("10-5");

        double resultado = op.calcular();

        assertEquals(5, resultado);
    }

    /**
     * Prueba la multiplicación de dos números.
     */
    @Test
    public void probarMultiplicacion() {

        Operador op = new Operador("10*5");

        double resultado = op.calcular();

        assertEquals(50, resultado);
    }

    /**
     * Prueba la división de dos números.
     */
    @Test
    public void probarDivision() {

        Operador op = new Operador("10/5");

        double resultado = op.calcular();

        assertEquals(2, resultado);
    }

    /**
     * Prueba la prioridad de la multiplicación.
     */
    @Test
    public void probarPrioridadMultiplicacion() {

        Operador op = new Operador("10+5*2");

        double resultado = op.calcular();

        assertEquals(20, resultado);
    }

    /**
     * Prueba el uso de paréntesis.
     */
    @Test
    public void probarParentesis() {

        Operador op = new Operador("(10+5)*2");

        double resultado = op.calcular();

        assertEquals(30, resultado);
    }

    /**
     * Prueba una ecuación con varias operaciones.
     */
    @Test
    public void probarEcuacionCompleta() {

        Operador op = new Operador("(10+5)*2-8/4");

        double resultado = op.calcular();

        assertEquals(28, resultado);
    }

    /**
     * Prueba una ecuación con paréntesis anidados.
     */
    @Test
    public void probarParentesisAnidados() {

        Operador op = new Operador("((10+5)*2)");

        double resultado = op.calcular();

        assertEquals(30, resultado);
    }

    /**
     * Prueba que no se permita comenzar una ecuación
     * con un número negativo.
     */
    @Test
    public void probarNumeroNegativo() {

        Operador op = new Operador("-10+5");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }

    /**
     * Prueba que no se permita una operación cuyo resultado
     * sea un número negativo.
     */
    @Test
    public void probarResultadoNegativo() {

        Operador op = new Operador("5-10");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }

    /**
     * Prueba que no se permita dividir entre cero.
     */
    @Test
    public void probarDivisionPorCero() {

        Operador op = new Operador("10/0");

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

        Operador op = new Operador("(10+5");

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

        Operador op = new Operador("");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }
}