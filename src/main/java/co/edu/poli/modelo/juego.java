package co.edu.poli.modelo;

public class juego {

    private String[] simbolos;
    private int[] numeros;

    public juego(String[] simbolos, int[] numeros) {
        this.simbolos = simbolos;
        this.numeros = numeros;
    }

    public String[] getSimbolos() {
        return simbolos;
    }

    public int[] getNumeros() {
        return numeros;
    }
    
    public String[] generarSimbolos() {
    	this.simbolos = new String[9];
    	this.simbolos[0]="+";
    	this.simbolos[1]="-";
    	this.simbolos[2]="*";
    	this.simbolos[3]="/";
    	this.simbolos[4]="(";
    	this.simbolos[5]=")";
    	this.simbolos[6]="=";
    	this.simbolos[7]="⌫";
    	this.simbolos[8]="↶";
    	return this.simbolos;
    }

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
     * Comprueba si los cuatro números permiten obtener
     * todos los resultados del 1 al 10.
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
     * Comprueba si un objetivo puede obtenerse utilizando
     * los números disponibles.
     */
    private boolean puedeObtenerResultado(int[] numeros, int objetivo) {

        java.util.List<Double> valores = new java.util.ArrayList<>();

        for (int numero : numeros) {
            valores.add((double) numero);
        }

        return buscarResultado(valores, objetivo);
    }

    /**
     * Prueba todas las combinaciones posibles de operaciones
     * entre los números.
     */
    private boolean buscarResultado(java.util.List<Double> valores, int objetivo) {

        if (valores.size() == 1) {

            double resultado = valores.get(0);

            return Math.abs(resultado - objetivo) < 0.000001;
        }

        for (int i = 0; i < valores.size(); i++) {

            for (int j = i + 1; j < valores.size(); j++) {

                double a = valores.get(i);
                double b = valores.get(j);

                java.util.List<Double> restantes = new java.util.ArrayList<>();

                for (int k = 0; k < valores.size(); k++) {

                    if (k != i && k != j) {
                        restantes.add(valores.get(k));
                    }
                }

                java.util.List<Double> resultados = new java.util.ArrayList<>();

                // Suma
                resultados.add(a + b);

                // Resta
                resultados.add(a - b);
                resultados.add(b - a);

                // Multiplicación
                resultados.add(a * b);

                // División
                if (Math.abs(b) > 0.000001) {
                    resultados.add(a / b);
                }

                if (Math.abs(a) > 0.000001) {
                    resultados.add(b / a);
                }

                for (double resultado : resultados) {

                    java.util.List<Double> nuevaLista =
                            new java.util.ArrayList<>(restantes);

                    nuevaLista.add(resultado);

                    if (buscarResultado(nuevaLista, objetivo)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    public boolean puedeObtenerTodosLosResultados(int[] numeros) {

        for (int objetivo = 1; objetivo <= 10; objetivo++) {

            if (!puedeObtenerResultado(numeros, objetivo)) {
                return false;
            }
        }

        return true;
    }
    
    
}