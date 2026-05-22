package umu.tds.gastos.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CuentaNueva {
	
	@FXML
    private TextField nombreCuenta;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

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
    }


    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se puede crear la cuenta");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
