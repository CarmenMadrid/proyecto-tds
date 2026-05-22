package umu.tds.gastos.domain.filtros;

import umu.tds.gastos.domain.core.Gasto;

public interface Filtro {
    public boolean cumple(Gasto g);
}
