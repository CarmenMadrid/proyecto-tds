package umu.tds.gastos.app;

import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.persistence.AlertasRepository;
import umu.tds.gastos.persistence.CuentaRepository;
import umu.tds.gastos.persistence.NotificacionesRepository;
import umu.tds.gastos.persistence.json.AlertasRepositoryJson;
import umu.tds.gastos.persistence.json.CuentaRepositoryJson;
import umu.tds.gastos.persistence.json.NotificacionesRepositoryJson;

public class Configuracion {

    private static Configuracion instancia;

    private CuentaRepository cuentaRepository;
    private AlertasRepository alertasRepository;
    private NotificacionesRepository notificacionesRepository;
    private CuentaController cuentaController;

    private Configuracion() {
        this.cuentaRepository = new CuentaRepositoryJson();
        this.alertasRepository = new AlertasRepositoryJson();
        this.notificacionesRepository = new NotificacionesRepositoryJson();
        this.cuentaController = new CuentaController(cuentaRepository, alertasRepository, notificacionesRepository);
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
