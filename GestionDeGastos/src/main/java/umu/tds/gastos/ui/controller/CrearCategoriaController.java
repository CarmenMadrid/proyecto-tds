package umu.tds.gastos.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.ui.view.SceneManager;

public class CrearCategoriaController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField nombreCategoria;

    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    private Cuenta cuenta;

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @FXML
    void aceptar(ActionEvent event) {
        String nombre = nombreCategoria.getText().trim();
        if (nombre.isEmpty()) {
        	mensajeError("El nombre no puede estar vacío.");
            return;
        }
        if (cuenta == null) {
        	mensajeError("No hay cuenta seleccionada.");
            return;
        }
        boolean existe = cuentaController.obtenerCategorias(cuenta.getId())
                .stream().anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
        if (existe) {
            mensajeError("La categoría \"" + nombre + "\" ya existe.");
            return;
        }
        try {
            cuentaController.crearCategoria(cuenta.getId(), nombre);
            cerrar();
        } catch (Exception e) {
        	mensajeError("No se pudo crear la categoría: " + e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        SceneManager.getInstancia().closeDialog();
    }

    private void mensajeError(String msg) {
        SceneManager.getInstancia().showError("Error al crear categoría", msg);
    }

}
