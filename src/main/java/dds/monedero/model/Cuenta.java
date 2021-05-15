package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

	private double saldo;
	private List<Movimiento> movimientos = new ArrayList<>();

	public Cuenta() {
		saldo = 0;
	}

	public Cuenta(double monto, List<Movimiento> movimientos) {
		this.saldo = monto;
		this.movimientos = movimientos;
	}

	public void poner(double cuanto) {
		validarMontoPositivo(cuanto);
		validarLimiteDepositos();
		this.agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, true));
		this.saldo = saldo + cuanto;
	}

	public void sacar(double cuanto) {
		validarMontoPositivo(cuanto);
		validarSaldoRestante(cuanto);
		validarLimiteDeExtraccionDiaria(cuanto);
		this.agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, false));
		this.saldo = saldo - cuanto;
	}

	private void validarLimiteDepositos() {
		if (this.getCantDepositos() >= 3) {
			throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
		}
	}

	private void validarMontoPositivo(double cuanto) {
		if (cuanto <= 0) {
			throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
		}
	}

	private void validarLimiteDeExtraccionDiaria(double cuanto) {
		if (cuanto > this.limite()) {
			throw new MaximoExtraccionDiarioException(
					"No puede extraer mas de $ " + 1000 + " diarios, limite: " + this.limite());
		}
	}

	private void validarSaldoRestante(double cuanto) {
		if (saldoInsuficiente(cuanto)) {
			throw new SaldoMenorException("No puede sacar mas de " + this.saldo + " $");
		}
	}

	private double limite() {
		return 1000 - getMontoExtraidoA(LocalDate.now());
	}

	private boolean saldoInsuficiente(double cuanto) {
		return this.saldo - cuanto < 0;
	}

	public void agregarMovimiento(Movimiento movimiento) {
		movimientos.add(movimiento);
	}

	public double getMontoExtraidoA(LocalDate fecha) {
		return getMovimientos().stream()
				.filter(movimiento -> movimiento.esDeLaFecha(fecha) && !movimiento.isDeposito())
				.mapToDouble(Movimiento::getMonto)
				.sum();
	}

	private List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) { this.saldo = saldo; }

	private long getCantDepositos() {
		return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
	}

}
