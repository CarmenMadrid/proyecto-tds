package umu.tds.gastos.domain.filtros;

import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Persona;

public class FiltroPersona implements Filtro {
    private final Persona persona;

    public FiltroPersona(Persona persona) {
        this.persona = persona;
    }

    public Persona getPersona() {
        return persona;
    }

    @Override
    public boolean cumple(Gasto g) {
        if (g == null) return false;
        if (persona == null) return true;
        return persona.equals(g.getPagador());
    }
}
