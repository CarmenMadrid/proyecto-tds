package umu.tds.gastos.domain.filtros;

import umu.tds.gastos.domain.core.Gasto;
import java.time.Month; //como gasto usa LocalDate hay que usar Month para la comparacion
import java.util.List; //para guardar los meses
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
        if(meses == null || meses.isEmpty()) { //si meses es null o vacio es como un filtro abierto
            return true;
        }
            if (meses.contains(g.getFecha().getMonth())) {
                return true;
            }
        return false;
    }
}
