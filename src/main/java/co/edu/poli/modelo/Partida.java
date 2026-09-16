package co.edu.poli.modelo;

import java.time.LocalDate;

public class Partida {

	private String resultado;
	private LocalDate fechaPartida;
	private long tiempoEjecucion;
	private int puntaje;
	
	
	public Partida(String resultado, LocalDate fechaPartida, long tiempoEjecucion, int puntaje) {
		this.resultado = resultado;
		this.fechaPartida = fechaPartida;
		this.tiempoEjecucion = tiempoEjecucion;
		this.puntaje = puntaje;
	}


	public String getResultado() {
		return resultado;
	}


	public void setResultado(String resultado) {
		this.resultado = resultado;
	}


	public LocalDate getFechaPartida() {
		return fechaPartida;
	}


	public void setFechaPartida(LocalDate fechaPartida) {// crear clase en servicios para manejar esta fecha
		this.fechaPartida = fechaPartida;
	}


	public long getTiempoEjecucion() {
		return tiempoEjecucion;
	}


	public void setTiempoEjecucion(long tiempoEjecucion) {//crear clase en servicios para manejar este tiempo de ejecucion
		this.tiempoEjecucion = tiempoEjecucion;
	}


	public int getPuntaje() {
		return puntaje;
	}


	public void setPuntaje(int puntaje) {
		this.puntaje = puntaje;
	}
	
}
