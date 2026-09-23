package co.edu.poli.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase Operadores.
 */
public class OperadorTest {

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

    /**
     * Prueba la multiplicación implícita entre un número
     * y un paréntesis: 2(2) debe interpretarse como 2*2.
     */
    @Test
    public void probarMultiplicacionImplicitaNumeroAntesDeParentesis() {

        Operador op = new Operador("2(2)");

        double resultado = op.calcular();

        assertEquals(4, resultado);
    }

    /**
     * Prueba la multiplicación implícita entre un número
     * y una suma dentro de paréntesis: 3(2+1) debe interpretarse
     * como 3*(2+1).
     */
    @Test
    public void probarMultiplicacionImplicitaConSumaDentroDeParentesis() {

        Operador op = new Operador("3(2+1)");

        double resultado = op.calcular();

        assertEquals(9, resultado);
    }

    /**
     * Prueba la multiplicación implícita entre dos paréntesis:
     * (2+1)(3) debe interpretarse como (2+1)*3.
     */
    @Test
    public void probarMultiplicacionImplicitaEntreDosParentesis() {

        Operador op = new Operador("(2+1)(3)");

        double resultado = op.calcular();

        assertEquals(9, resultado);
    }

    /**
     * Prueba la multiplicación implícita entre un paréntesis
     * y un número: (2+1)5 debe interpretarse como (2+1)*5.
     */
    @Test
    public void probarMultiplicacionImplicitaNumeroDespuesDeParentesis() {

        Operador op = new Operador("(2+1)5");

        double resultado = op.calcular();

        assertEquals(15, resultado);
    }

    /**
     * Prueba que la multiplicación explícita siga funcionando igual
     * después de agregar el soporte de multiplicación implícita.
     */
    @Test
    public void probarMultiplicacionExplicitaSigueFuncionando() {

        Operador op = new Operador("2*2");

        double resultado = op.calcular();

        assertEquals(4, resultado);
    }

    /**
     * Prueba que la multiplicación implícita respete la precedencia
     * de operaciones: 2+3(4) debe ser 2+(3*4)=14, nunca (2+3)*4=20.
     */
    @Test
    public void probarMultiplicacionImplicitaNoAlteraPrecedencia() {

        Operador op = new Operador("2+3(4)");

        double resultado = op.calcular();

        assertEquals(14, resultado);
    }

    /**
     * Prueba que una expresión no válida siga lanzando la excepción
     * correspondiente aunque contenga multiplicación implícita.
     */
    @Test
    public void probarExpresionInvalidaConMultiplicacionImplicita() {

        Operador op = new Operador("2(2+)");

        assertThrows(
            IllegalArgumentException.class,
            () -> op.calcular()
        );
    }
}