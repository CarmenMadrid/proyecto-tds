package umu.tds.gastos.imports;

import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.imports.dto.GastoCSV;
import umu.tds.gastos.imports.lector.LectorCSV;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ImportadorCSV implements ImportadorGastos {

    private final LectorCSV lector;

    public ImportadorCSV() {
        this.lector = new LectorCSV();
    }

    @Override
    public List<Gasto> importar(String archivo, Cuenta cuenta) throws IOException {
        List<GastoCSV> gastosCSV = lector.leer(archivo);

        return gastosCSV.stream()
                .map(r -> convertir(r, cuenta))
                .collect(Collectors.toList());
    }

    private Gasto convertir(GastoCSV r, Cuenta cuenta) {
        Categoria cat = new Categoria(r.getCategoria());
        Persona pagador = null;

        if (cuenta instanceof CuentaCompartida comp && r.getPagador() != null && !r.getPagador().isBlank()) {
            pagador = comp.getPersonas().stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(r.getPagador()))
                    .findFirst()
                    .orElse(null);
            return new Gasto(r.getImporte(), r.getFecha(), cat, pagador);
        }

        return new Gasto(r.getImporte(), r.getFecha(), cat);
    }
}
