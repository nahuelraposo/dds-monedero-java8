package dds.monedero.model;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class MonederoTest {
	private Cuenta cuenta;

	@Before
	public void init() {
		cuenta = new Cuenta();
	}

	@Test
	public void Poner() {
		cuenta.poner(1500);

		Assert.assertEquals(cuenta.getSaldo(), 1500, 0.0);
	}

	@Test
	public void sacoLaMitadDeLoQuePongo() {
		cuenta.poner(1000);
		cuenta.sacar(500);

		Assert.assertEquals(cuenta.getSaldo(), 500, 0.0);
	}

	@Test
	public void TresDepositos() {
		cuenta.poner(1500);
		cuenta.poner(456);
		cuenta.poner(1900);

		Assert.assertEquals(cuenta.getSaldo(), 3856, 0.0);
	}

	@Test
	public void obtengoMontoExtraidoDeAhora() {

		cuenta.poner(1000);
		cuenta.agregarMovimiento(new Movimiento(LocalDate.now(), 100, false));

		Assert.assertEquals(cuenta.getMontoExtraidoA(LocalDate.now()), 100, 0.0);
	}

	@Test(expected = MaximaCantidadDepositosException.class)
	public void MasDeTresDepositos() {
		cuenta.poner(1500);
		cuenta.poner(456);
		cuenta.poner(1900);
		cuenta.poner(245);
	}

	@Test(expected = SaldoMenorException.class)
	public void ExtraerMasQueElSaldo() {
		cuenta.setSaldo(90);
		cuenta.sacar(1001);
	}

	@Test(expected = MaximoExtraccionDiarioException.class)
	public void ExtraerMasDe1000() {
		cuenta.setSaldo(5000);
		cuenta.sacar(1001);
	}

	@Test(expected = MontoNegativoException.class)
	public void PonerMontoNegativo() {
		cuenta.poner(-1500);
	}

	@Test(expected = MontoNegativoException.class)
	public void ExtraerMontoNegativo() {
		cuenta.sacar(-500);
	}

}