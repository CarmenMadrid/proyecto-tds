package umu.tds.gastos.domain.filtros;

import java.time.LocalDate;
import umu.tds.gastos.domain.core.Gasto;

public class FiltroFechas implements Filtro {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public FiltroFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("fechaFin no puede ser antes de fechaInicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public boolean cumple(Gasto g) {
        if (g == null || g.getFecha() == null) {
            return false;
        }
        LocalDate fechaGasto = g.getFecha();
        return !fechaGasto.isBefore(fechaInicio) && !fechaGasto.isAfter(fechaFin);
    }
}
