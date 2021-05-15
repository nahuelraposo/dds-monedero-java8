package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
	private LocalDate fecha;
	// En ningun lenguaje de programacion usen jamas doubles para modelar dinero en el mundo real
	// siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
	private double monto;
	private boolean esDeposito;

	public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
		this.fecha = fecha;
		this.monto = monto;
		this.esDeposito = esDeposito;
	}

	public double getMonto() {
		return monto;
	}

	private LocalDate getFecha() {
		return fecha;
	}

	public boolean esDeLaFecha(LocalDate fecha) {
		return this.getFecha().equals(fecha);
	}

	public boolean isDeposito() {
		return esDeposito;
	}

}
