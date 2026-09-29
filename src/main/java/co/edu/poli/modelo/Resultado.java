package co.edu.poli.modelo;

/**
 * Representa un resultado obtenido durante una partida.
 */
public class Resultado {

    private int id;

    private Partida partida;

    private String resultado;

    private int dato;

    public Resultado() {
    }

    public Resultado(int id,
                     Partida partida,
                     String resultado,
                     int dato) {

        this.id = id;
        this.partida = partida;
        this.resultado = resultado;
        this.dato = dato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }
}