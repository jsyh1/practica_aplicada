package co.edu.poli.modelo;

/**
 * Representa un resultado obtenido durante una partida.
 *
 * <p>
 * Almacena el identificador del resultado, la partida a la que pertenece,
 * la expresión utilizada y el valor numérico obtenido.
 * </p>
 *
 * @author Jsyh
 * @version 1.0
 */
public class Resultado {

    /**
     * Identificador único del resultado.
     */
    private int id;

    /**
     * Partida a la que pertenece el resultado.
     */
    private Partida partida;

    /**
     * Expresión matemática utilizada para obtener el resultado.
     */
    private String resultado;

    /**
     * Valor numérico obtenido al evaluar la expresión.
     */
    private int dato;

    /**
     * Construye un resultado sin inicializar sus atributos.
     */
    public Resultado() {

    }

    /**
     * Construye un resultado con todos sus atributos.
     *
     * @param id identificador del resultado
     * @param partida partida a la que pertenece
     * @param resultado expresión matemática utilizada
     * @param dato valor numérico obtenido
     */
    public Resultado(int id,

                     Partida partida,

                     String resultado,

                     int dato) {

        this.id = id;

        this.partida = partida;

        this.resultado = resultado;

        this.dato = dato;

    }

    /**
     * Obtiene el identificador del resultado.
     *
     * @return identificador del resultado
     */
    public int getId() {

        return id;

    }

    /**
     * Modifica el identificador del resultado.
     *
     * @param id nuevo identificador
     */
    public void setId(int id) {

        this.id = id;

    }

    /**
     * Obtiene la partida asociada al resultado.
     *
     * @return partida correspondiente
     */
    public Partida getPartida() {

        return partida;

    }

    /**
     * Modifica la partida asociada al resultado.
     *
     * @param partida nueva partida asociada
     */
    public void setPartida(Partida partida) {

        this.partida = partida;

    }

    /**
     * Obtiene la expresión matemática almacenada.
     *
     * @return expresión utilizada para obtener el resultado
     */
    public String getResultado() {

        return resultado;

    }

    /**
     * Modifica la expresión matemática almacenada.
     *
     * @param resultado nueva expresión matemática
     */
    public void setResultado(String resultado) {

        this.resultado = resultado;

    }

    /**
     * Obtiene el valor numérico del resultado.
     *
     * @return valor numérico obtenido
     */
    public int getDato() {

        return dato;

    }

    /**
     * Modifica el valor numérico del resultado.
     *
     * @param dato nuevo valor numérico
     */
    public void setDato(int dato) {

        this.dato = dato;

    }

}