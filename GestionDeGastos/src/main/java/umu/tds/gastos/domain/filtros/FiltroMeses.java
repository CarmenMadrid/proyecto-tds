package umu.tds.gastos.domain.filtros;

import umu.tds.gastos.domain.core.Gasto;
import java.time.Month;
import java.util.List; 
public class FiltroMeses implements Filtro {
    private final List<Month> meses;

    public FiltroMeses(List<Month> meses) {
        this.meses = meses;
    }

    public List<Month> getMeses() {
        return meses;
    }

    @Override
    public boolean cumple(Gasto g) {
        if(meses == null || meses.isEmpty()) {
            return true;
        }
        if (meses.contains(g.getFecha().getMonth())) {
            return true;
        }
        return false;
    }
}
