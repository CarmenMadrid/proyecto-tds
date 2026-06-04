package umu.tds.gastos.ui.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

        cuentaController.crearCuentaPersonal(nombre);
        cerrarVentana();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        SceneManager.getInstancia().closeDialog();
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
        SceneManager.getInstancia().showError("Error", "No se puede crear la cuenta\n" + mensaje);
    }
}
