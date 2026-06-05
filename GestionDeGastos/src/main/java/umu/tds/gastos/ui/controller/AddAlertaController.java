package umu.tds.gastos.ui.controller;

import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.ui.view.SceneManager;

public class AddAlertaController {

    @FXML
    private TextField cantidadAlerta;

    @FXML
    private CheckBox chboxMensual;

    @FXML
    private CheckBox chboxSemanal;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    private Cuenta cuenta;

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    @FXML
    public void initialize() {
        comboCategoria.setConverter(new StringConverter<>() {
            public String toString(Categoria c) { return c == null ? "" : c.getNombre(); }
            public Categoria fromString(String s) { return null; }
        });
    }

    public void setCategorias(java.util.List<Categoria> categorias) {
        comboCategoria.getItems().add(null);
        comboCategoria.getItems().addAll(categorias);
    }

    @FXML
    void aceptar(ActionEvent event) {
        String cantText = cantidadAlerta.getText().trim();
        if (cantText.isEmpty()) {
            SceneManager.getInstancia().showError("Error", "Introduzca una cantidad límite.");
            return;
        }
        double limite;
        try {
            limite = Double.parseDouble(cantText);
        } catch (NumberFormatException e) {
            SceneManager.getInstancia().showError("Error", "La cantidad debe ser un número.");
            return;
        }
        if (limite <= 0) {
            SceneManager.getInstancia().showError("Error", "El límite debe ser positivo.");
            return;
        }
        if (!chboxSemanal.isSelected() && !chboxMensual.isSelected()) {
            SceneManager.getInstancia().showError("Error", "Seleccione un tipo de alerta (Semanal o Mensual).");
            return;
        }
        String tipo = chboxSemanal.isSelected() ? "Semanal" : "Mensual";
        Categoria categoria = comboCategoria.getValue();
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        try {
            UUID idCuenta = cuenta != null ? cuenta.getId() : null;
            cc.crearAlerta(limite, categoria, tipo, idCuenta);
            SceneManager.getInstancia().closeDialog();
        } catch (Exception e) {
            SceneManager.getInstancia().showError("Error", e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        SceneManager.getInstancia().closeDialog();
    }
}
