package umu.tds.gastos.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.alertas.Alertas;
import umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaMensual;
import umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaSemanal;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.ui.view.SceneManager;

public class EditarAlertaController {

    @FXML
    private TextField cantidadAlerta;

    @FXML
    private CheckBox chboxMensual;

    @FXML
    private CheckBox chboxSemanal;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    private Alertas alerta;

    public void setAlerta(Alertas alerta) {
        this.alerta = alerta;
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
        if (alerta != null) {
            cantidadAlerta.setText(String.valueOf(alerta.getLimite()));
            if (alerta.getCategoria() != null) {
                comboCategoria.setValue(alerta.getCategoria());
            }
            if (alerta.getTipo() instanceof EstrategiaSemanal) {
                chboxSemanal.setSelected(true);
            } else if (alerta.getTipo() instanceof EstrategiaMensual) {
                chboxMensual.setSelected(true);
            }
        }
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
            cc.editarAlerta(alerta.getId(), limite, categoria, tipo);
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
