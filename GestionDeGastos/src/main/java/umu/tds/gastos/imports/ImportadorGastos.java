package umu.tds.gastos.imports;

import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.Gasto;

import java.io.IOException;
import java.util.List;

public interface ImportadorGastos {

    List<Gasto> importar(String archivo, Cuenta cuenta) throws IOException;
}
