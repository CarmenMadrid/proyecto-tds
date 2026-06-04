package umu.tds.gastos.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;

public class CuentaNuevaController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField nombreCuenta;
    
    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();
    
    @FXML
    void aceptar(ActionEvent event) {
        String nombre = nombreCuenta.getText().trim();
        if (nombre.isEmpty()) {
        	mensajeError("El nombre no puede estar vacío.");
            return;
        }
        
        boolean existe = cuentaController.obtenerCuentas().stream()
                .anyMatch(c -> c.getNombre().equals(nombreCuenta));

        if (existe) {
            mensajeError("Ya existe una cuenta con el nombre \"" + nombreCuenta + "\". Elige otro nombre.");
            return;
        }

        cuentaController.crearCuentaPersonal(nombre);
        cerrarVentana();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    /*
    @FXML
    private void aceptar() {

        //Primero necesitamos tener el texto
        String nombre = nombreCuenta.getText().trim();

        //Comprobamos que no esté vacío
        if (nombre.isEmpty()) {
        	mensajeError("El nombre no puede estar vacío.");
            return;
        }

        //Volver a la ventana principal
        SceneManager.getInstancia().showVentanaPrincipal();
    }


    @FXML
    private void cancelar() {
        SceneManager.getInstancia().showVentanaPrincipal();
    }*/


    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se puede crear la cuenta");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
