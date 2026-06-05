package umu.tds.gastos.domain.alertas.patronEstrategias;

import umu.tds.gastos.domain.core.Gasto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import umu.tds.gastos.domain.alertas.Alertas;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EstrategiaSemanal.class, name = "EstrategiaSemanal"),
    @JsonSubTypes.Type(value = EstrategiaMensual.class, name = "EstrategiaMensual")
})
public interface EstrategiaTiempo {

    double comprobar(Alertas alerta, List<Gasto> gastos);

    String getNombreEstrategia();
}
