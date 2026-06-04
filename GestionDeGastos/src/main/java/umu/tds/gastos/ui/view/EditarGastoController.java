package umu.tds.gastos.ui.view;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Persona;

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
    private ComboBox<Cuenta> comboCuenta;

    @FXML
    private ComboBox<Persona> comboPersona;

    @FXML
    private TextField nombreCuenta;


    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    private Cuenta cuenta;
    private Gasto gasto;

    @FXML
    public void initialize() {
        comboCuenta.setConverter(new StringConverter<>() {
            public String toString(Cuenta c) { return c == null ? "" : c.getNombre(); }
            public Cuenta fromString(String s) { return null; }
        });
        comboPersona.setConverter(new StringConverter<>() {
            public String toString(Persona p) { return p == null ? "" : p.getNombre(); }
            public Persona fromString(String s) { return null; }
        });
    }

    public void setGasto(Gasto gasto, Cuenta cuenta) {
        this.gasto = gasto;
        this.cuenta = cuenta;

        comboCuenta.getItems().setAll(cuenta);
        comboCuenta.setValue(cuenta);
        comboCuenta.setDisable(true);

        comboCategoria.getItems().setAll(cuentaController.obtenerCategorias(cuenta.getId()));
        comboCategoria.setValue(gasto.getCategoria());

        nombreCuenta.setText(gasto.getNombre());
        cantidadGasto.setText(String.valueOf(gasto.getCantidad()));
        calFecha.setValue(gasto.getFecha());

        if (cuenta instanceof CuentaCompartida cc) {
            comboPersona.getItems().setAll(cc.getPersonas());
            comboPersona.setValue(gasto.getPagador());
            comboPersona.setDisable(false);
        } else {
            comboPersona.setDisable(true); //Si no es una cuenta compartida no puede tocar el desplegable
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
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    private void mensajeError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error al crear gasto");
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
