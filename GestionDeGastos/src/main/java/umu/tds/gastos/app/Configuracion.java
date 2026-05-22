package umu.tds.gastos.app;

import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.persistence.CuentaRepository;
import umu.tds.gastos.persistence.json.CuentaRepositoryJson;

public class Configuracion {

    private static Configuracion instancia;

    private CuentaRepository cuentaRepository;
    private CuentaController cuentaController;

    private Configuracion() {
        this.cuentaRepository = new CuentaRepositoryJson();
        this.cuentaController = new CuentaController(cuentaRepository);
    }

    public static Configuracion getInstancia() {
        if (instancia == null) {
            instancia = new Configuracion();
        }
        return instancia;
    }

    static void setInstancia(Configuracion impl) {
        Configuracion.instancia = impl;
    }

    public CuentaController getCuentaController() {
        return cuentaController;
    }

    public CuentaRepository getCuentaRepository() {
        return cuentaRepository;
    }
}
