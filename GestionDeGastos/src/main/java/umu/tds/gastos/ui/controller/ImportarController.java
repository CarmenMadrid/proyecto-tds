package umu.tds.gastos.ui.controller;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.ui.view.SceneManager;

public class ImportarController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSeleccionar;

    @FXML
    private ComboBox<Cuenta> comboCuenta;

    @FXML
    private TextField archivoField;

    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    private File selectedFile;
    private Consumer<Integer> onImported;

    @FXML
    public void initialize() {
        comboCuenta.setConverter(new StringConverter<>() {
            public String toString(Cuenta c) { return c == null ? "" : c.getNombre(); }
            public Cuenta fromString(String s) { return null; }
        });
        comboCuenta.getItems().setAll(cuentaController.obtenerCuentas());
    }

    public void setCuenta(Cuenta cuenta) {
        if (cuenta != null) {
            comboCuenta.setValue(cuenta);
        }
    }

    public void setOnImported(Consumer<Integer> onImported) {
        this.onImported = onImported;
    }

    @FXML
    void seleccionarArchivo(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
        File file = fc.showOpenDialog(SceneManager.getInstancia().getPrimaryStage());
        if (file != null) {
            selectedFile = file;
            archivoField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void aceptar(ActionEvent event) {
        Cuenta cuenta = comboCuenta.getValue();
        if (cuenta == null) {
            mensajeError("Seleccione una cuenta.");
            return;
        }
        if (selectedFile == null) {
            mensajeError("Seleccione un archivo.");
            return;
        }
        try {
            int count = cuentaController.importarGastos(cuenta.getId(), selectedFile.getAbsolutePath(), "CSV");
            if (onImported != null) onImported.accept(count);
            SceneManager.getInstancia().closeDialog();
        } catch (IOException e) {
            mensajeError("Error al importar: " + e.getMessage());
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        SceneManager.getInstancia().closeDialog();
    }

    private void mensajeError(String msg) {
        SceneManager.getInstancia().showError("Error al importar", msg);
    }
}
