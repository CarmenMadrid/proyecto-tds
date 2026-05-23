package umu.tds.gastos.domain.alertas.patronEstrategias;

import umu.tds.gastos.domain.core.Gasto;

import java.util.List;

import umu.tds.gastos.domain.alertas.Alertas;

public class EstrategiaMensual implements EstrategiaTiempo {
    @Override
    public String getNombreEstrategia() {
        return "Estrategia Mensual";
    }
    @Override
    public double comprobar(Alertas alerta, List<Gasto> gastos) {
        int mes = alerta.getMesAlerta();
		int ano = alerta.getAnoAlerta();

        return gastos.stream().filter(g -> g.getMes() == mes).filter(g -> g.getAno() == ano)
				.mapToDouble(Gasto::getCantidad).sum();
        
    }
}
