package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

	private double saldo = 0;
	private List<Movimiento> movimientos = new ArrayList<>();

// corrijo lo que puse antes, uso este método para inicializar con saldo 0 y sin movimientos la cuenta
	public Cuenta() {
		saldo = 0;
	}

	public Cuenta(double monto, List<Movimiento> movimientos) {
		this.saldo = monto;
		this.movimientos = movimientos;
	}

	public void poner(double cuanto) {
		if (cuanto <= 0) {
			throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
		}
		if (this.cantDepositos() >= 3) {
			throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
		} else {
			this.agregarMovimiento(LocalDate.now(), cuanto, true);
			this.saldo = saldo + cuanto;
		}
	}

	public long cantDepositos() {
		return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
	}

	public void sacar(double cuanto) {
		if (cuanto <= 0) {
			throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
		}
		if (faltaDeSaldo(cuanto)) {
			throw new SaldoMenorException("No puede sacar mas de " + this.saldo + " $");
		}

		if (cuanto > this.limite()) {
			throw new MaximoExtraccionDiarioException(
					"No puede extraer mas de $ " + 1000 + " diarios, limite: " + this.limite());
		}

		else {
			this.agregarMovimiento(LocalDate.now(), cuanto, false);
			this.saldo = saldo - cuanto;
		}
	}

	public double limite() {
		double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
		return 1000 - montoExtraidoHoy;
	}

	public boolean faltaDeSaldo(double cuanto) {
		return this.saldo - cuanto < 0;
	}

	public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
		Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
		movimientos.add(movimiento);
	}

	public double getMontoExtraidoA(LocalDate fecha) {
		return getMovimientos().stream()
				.filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
				.mapToDouble(Movimiento::getMonto).sum();
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
