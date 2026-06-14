package umu.tds.gastos.domain.alertas.patronEstrategias;
import umu.tds.gastos.domain.core.Gasto;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.List;

import umu.tds.gastos.domain.alertas.Alertas;

public class EstrategiaSemanal implements EstrategiaTiempo {
    @Override
    public String getNombreEstrategia() {
        return "Estrategia Semanal";
    }
    @Override
    public double comprobar(Alertas alerta, List<Gasto> gastos) {
		LocalDate hoy = LocalDate.now();
		int semana = hoy.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
int     ano = hoy.get(IsoFields.WEEK_BASED_YEAR);
		return gastos.stream().filter(g -> g.getSemana() == semana).filter(g -> g.getAno() == ano)
				.mapToDouble(Gasto::getCantidad).sum();
	}

}
