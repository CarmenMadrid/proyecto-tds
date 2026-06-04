package umu.tds.gastos.domain.alertas.patronEstrategias;
import umu.tds.gastos.domain.core.Gasto;

import java.util.List;

import umu.tds.gastos.domain.alertas.Alertas;
public class EstrategiaSemanal implements EstrategiaTiempo {
    @Override
    public String getNombreEstrategia() {
        return "Estrategia Semanal";
    }
    @Override
    public double comprobar(Alertas alerta, List<Gasto> gastos) {
		int semana = alerta.getSemanaAlerta();
		int ano = alerta.getAnoAlerta();
		return gastos.stream().filter(g -> g.getSemana() == semana).filter(g -> g.getAno() == ano)
				.mapToDouble(Gasto::getCantidad).sum();
	}

}
