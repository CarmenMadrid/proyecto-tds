package umu.tds.gastos.domain.filtros;

import java.util.List;
import umu.tds.gastos.domain.core.Gasto;

public class FiltroMultiple implements Filtro {
    private final List<Filtro> filtros;

    public FiltroMultiple(List<Filtro> filtros) {
        this.filtros = new java.util.ArrayList<>(filtros);
    }
    public FiltroMultiple() {
        this.filtros = new java.util.ArrayList<>(); 
    }
    public List<Filtro> getFiltros() {
        return filtros;
    }
    public void agregarFiltro(Filtro filtro) {
        if (filtro != null) {
            ((java.util.ArrayList<Filtro>) filtros).add(filtro);
        }
    }
    @Override
    public boolean cumple(Gasto g) {
        if (g == null) {
            return false;
        }
        for (Filtro filtro : filtros) {
            if (filtro == null || !filtro.cumple(g)) {
                return false;
            }
        }
        return true;
    }
}
