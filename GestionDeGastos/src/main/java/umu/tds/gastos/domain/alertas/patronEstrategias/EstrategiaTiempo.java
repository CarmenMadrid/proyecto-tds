package umu.tds.gastos.domain.alertas.patronEstrategias;

import umu.tds.gastos.domain.core.Gasto;

import java.util.List;

import umu.tds.gastos.domain.alertas.Alertas;

public interface EstrategiaTiempo {

    double comprobar(Alertas alerta, List<Gasto> gastos);

    String getNombreEstrategia();
}
