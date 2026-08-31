package co.edu.poli.servicios;

public class Fraccion {

    public static String convertir(double numero) {

        // Si el resultado es entero
        if (numero == Math.floor(numero)) {
            return String.valueOf((int) numero);
        }

        int denominador = 1000;
        int numerador = (int) Math.round(numero * denominador);

        int mcd = calcularMCD(numerador, denominador);

        numerador /= mcd;
        denominador /= mcd;

        return numerador + "/" + denominador;
    }

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