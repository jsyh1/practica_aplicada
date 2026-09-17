package co.edu.poli.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class juegoTest {

    @Test
    void generarNumerosDebeGenerarCuatroNumeros() {

        juego partida = new juego(new String[9], new int[4]);

        int[] numeros = partida.generarNumeros();

        assertNotNull(numeros);
        assertEquals(4, numeros.length);
    }

    @Test
    void generarNumerosDebeEstarEntreUnoYDiez() {

        juego partida = new juego(new String[9], new int[4]);

        int[] numeros = partida.generarNumeros();

        for (int numero : numeros) {

            assertTrue(
                numero >= 1 && numero <= 10,
                "El número generado debe estar entre 1 y 10"
            );
        }
    }

    @Test
    void generarNumerosDebeGenerarUnaCombinacionValida() {

        juego partida = new juego(new String[9], new int[4]);

        int[] numeros = partida.generarNumeros();

        assertTrue(
            partida.puedeObtenerTodosLosResultados(numeros),
            "La combinación generada debe permitir obtener los resultados del 1 al 10"
        );
    }

    @Test
    void generarNumerosDebeGenerarCombinacionesValidasVariasVeces() {

        juego partida = new juego(new String[9], new int[4]);

        for (int i = 0; i < 10; i++) {

            int[] numeros = partida.generarNumeros();

            assertNotNull(numeros);
            assertEquals(4, numeros.length);

            for (int numero : numeros) {
                assertTrue(numero >= 1 && numero <= 10);
            }

            assertTrue(
                partida.puedeObtenerTodosLosResultados(numeros),
                "La combinación generada no permite obtener todos los resultados del 1 al 10"
            );
        }
    }

    @Test
    void puedeObtenerTodosLosResultadosDebeAceptarCombinacionValida() {

        juego partida = new juego(new String[9], new int[4]);

        int[] numeros = partida.generarNumeros();

        boolean resultado =
                partida.puedeObtenerTodosLosResultados(numeros);

        assertTrue(resultado);
    }
}

