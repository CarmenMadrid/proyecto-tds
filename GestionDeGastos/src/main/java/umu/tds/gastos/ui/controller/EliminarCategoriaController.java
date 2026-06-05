package umu.tds.gastos.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.ui.view.SceneManager;

import java.util.List;

public class EliminarCategoriaController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    private Cuenta cuenta;
    private Runnable onDeleted;

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public void setOnDeleted(Runnable onDeleted) {
        this.onDeleted = onDeleted;
    }

    @FXML
    void initialize() {
        comboCategoria.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Categoria c) { return c == null ? "" : c.getNombre(); }
            public Categoria fromString(String s) { return null; }
        });
    }

    public void setCategorias(List<Categoria> categorias) {
        comboCategoria.getItems().setAll(categorias);
        if (!categorias.isEmpty()) {
            comboCategoria.setValue(categorias.get(0));
        }
    }

    @FXML
    void aceptar(ActionEvent event) {
        Categoria seleccionada = comboCategoria.getValue();
        if (seleccionada == null) {
            mensajeError("Seleccione una categoría.");
            return;
        }
        if (cuenta == null) {
            mensajeError("No hay cuenta seleccionada.");
            return;
        }
        if (cuentaController.categoriaEnUso(cuenta.getId(), seleccionada.getNombre())) {
            mensajeError("La categoría \"" + seleccionada.getNombre() + "\" tiene gastos asociados y no puede eliminarse.");
            return;
        }
        String nombre = seleccionada.getNombre();
        cerrar();
        javafx.application.Platform.runLater(() ->
            SceneManager.getInstancia().showConfirmation(
                "Eliminar categoría",
                "¿Seguro que desea eliminar la categoría \"" + nombre + "\"?",
                () -> {
                    try {
                        cuentaController.eliminarCategoria(cuenta.getId(), nombre);
                        if (onDeleted != null) onDeleted.run();
                    } catch (Exception e) {
                        SceneManager.getInstancia().showError("Error", "No se pudo eliminar la categoría: " + e.getMessage());
                    }
                }));
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        SceneManager.getInstancia().closeDialog();
    }

    private void mensajeError(String msg) {
        SceneManager.getInstancia().showError("Error al eliminar categoría", msg);
    }
}
