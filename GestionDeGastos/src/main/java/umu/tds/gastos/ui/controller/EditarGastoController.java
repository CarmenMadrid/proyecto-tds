package umu.tds.gastos.ui.controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.ui.view.SceneManager;

public class EditarGastoController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private DatePicker calFecha;

    @FXML
    private TextField cantidadGasto;

    @FXML
    private ComboBox<Categoria> comboCategoria;

    @FXML
    private Label lblPersona;

    @FXML
    private ComboBox<Persona> comboPersona;

    @FXML
    private TextField nombreCuenta;

    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    private Cuenta cuenta;
    private Gasto gasto;

    @FXML
    public void initialize() {
        comboCategoria.setConverter(new StringConverter<>() {
            public String toString(Categoria c) { return c == null ? "" : c.getNombre(); }
            public Categoria fromString(String s) { return null; }
        });
        comboPersona.setConverter(new StringConverter<>() {
            public String toString(Persona p) { return p == null ? "" : p.getNombre(); }
            public Persona fromString(String s) { return null; }
        });
        ocultarPersona();
    }

    private void ocultarPersona() {
        lblPersona.setVisible(false);
        lblPersona.setManaged(false);
        comboPersona.setVisible(false);
        comboPersona.setManaged(false);
    }

    public void setGasto(Gasto gasto, Cuenta cuenta) {
        this.gasto = gasto;
        this.cuenta = cuenta;

        comboCategoria.getItems().setAll(cuentaController.obtenerCategorias(cuenta.getId()));
        comboCategoria.setValue(gasto.getCategoria());

        nombreCuenta.setText(gasto.getNombre());
        cantidadGasto.setText(String.valueOf(gasto.getCantidad()));
        calFecha.setValue(gasto.getFecha());

        if (cuenta instanceof CuentaCompartida cc) {
            lblPersona.setVisible(true);
            lblPersona.setManaged(true);
            comboPersona.setVisible(true);
            comboPersona.setManaged(true);
            comboPersona.getItems().setAll(cc.getPersonas());
            comboPersona.setValue(gasto.getPagador());
            comboPersona.setDisable(false);
        } else {
            comboPersona.setValue(null);
            ocultarPersona();
        }
    }

    @FXML
    void aceptar(ActionEvent event) {
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadGasto.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
        	mensajeError("Introduce una cantidad mayor que 0.");
            return;
        }
        LocalDate fecha = calFecha.getValue();
        if (fecha == null) {
        	mensajeError("Seleccione una fecha.");
            return;
        }
        Categoria categoria = comboCategoria.getValue();
        String nombreCat = categoria != null ? categoria.getNombre() : gasto.getCategoria().getNombre();
        String nombre = nombreCuenta.getText().trim();
        cuentaController.editarGasto(cuenta.getId(), gasto.getId(), nombre, cantidad, fecha, nombreCat);
        cerrar();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        SceneManager.getInstancia().closeDialog();
    }

    private void mensajeError(String msg) {
        SceneManager.getInstancia().showError("Error al editar gasto", msg);
    }

}
