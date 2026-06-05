package umu.tds.gastos.ui.controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.ui.view.SceneManager;

public class CrearGastoController {

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
    private ComboBox<Cuenta> comboCuenta;

    @FXML
    private ComboBox<Persona> comboPersona;

    @FXML
    private TextField nombreCuenta;
    
    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();

    @FXML
    public void initialize() {
        comboCuenta.getItems().setAll(cuentaController.obtenerCuentas());

        comboCuenta.setConverter(new StringConverter<>() {
            public String toString(Cuenta c) { return c == null ? "" : c.getNombre(); }
            public Cuenta fromString(String s) { return null; }
        });
        
        comboPersona.setConverter(new StringConverter<>() {
            public String toString(Persona p) { return p == null ? "" : p.getNombre(); }
            public Persona fromString(String s) { return null; }
        });

        comboCuenta.valueProperty().addListener((obs, old, nueva) -> {
            if (nueva != null) actualizarParaCuenta(nueva);
        });
        comboPersona.setDisable(true);
    }

    public void setCuenta(Cuenta cuenta) {
        if (cuenta != null) {
            comboCuenta.setValue(cuenta);
            actualizarParaCuenta(cuenta);
        }
    }

    private void actualizarParaCuenta(Cuenta cuenta) {
        comboCategoria.getItems().setAll(cuentaController.obtenerCategorias(cuenta.getId()));
        comboPersona.getItems().clear();
        if (cuenta instanceof CuentaCompartida cc) {
            comboPersona.getItems().setAll(cc.getPersonas());
            comboPersona.setDisable(false);
        } else {
            comboPersona.setValue(null);
            comboPersona.setDisable(true);
        }
    }

    @FXML
    void aceptar(ActionEvent event) {
        Cuenta cuenta = comboCuenta.getValue();
        if (cuenta == null) {
        	mensajeError("Seleccione una cuenta.");
            return;
        }
        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadGasto.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
        	mensajeError("Introduce una cantidad válida mayor que 0.");
            return;
        }
        LocalDate fecha = calFecha.getValue();
        if (fecha == null) {
        	mensajeError("Seleccione una fecha.");
            return;
        }

        Categoria seleccionada = comboCategoria.getValue();
        String nombreCategoria = null;
		if (seleccionada != null) {
        	nombreCategoria=seleccionada.getNombre();
        }
        if (nombreCategoria == null) {
        	mensajeError("Seleccione una categoría.");
            return;
        }

        Persona pagador = comboPersona.getValue();
        if (cuenta instanceof CuentaCompartida && pagador == null) {
        	mensajeError("Seleccione el pagador para la cuenta compartida.");
            return;
        }
        String nombre = nombreCuenta.getText().trim();
        cuentaController.registrarGasto(cuenta.getId(), nombre, cantidad, fecha, nombreCategoria, pagador);
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
        SceneManager.getInstancia().showError("Error al crear gasto", msg);
    }

}
