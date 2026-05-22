package umu.tds.gastos.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import umu.tds.gastos.domain.core.Cuenta;

public class VentanaPrincipalController {

    @FXML private ListView<Cuenta> listaCuentas;

    @FXML private Button btnCrearCuenta;
    @FXML private Button btnCrearCompartida;
    @FXML private Button btnEliminarCuenta;


    @FXML
    public void initialize() {
        cargarCuentas();
    }

    private void cargarCuentas() {
        listaCuentas.getItems().setAll(
                SceneManager.getInstancia().getCuentas()
        );
    }

    @FXML
    private void crearCuenta() {
        SceneManager.getInstancia().showCrearCuenta();
    }

    @FXML
    private void crearCuentaCompartida() {
        SceneManager.getInstancia().showCrearCuentaCompartida();
    }


    @FXML
    private void eliminarCuenta() {

        Cuenta seleccionada = listaCuentas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarError("Debe seleccionar una cuenta para eliminarla.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar cuenta");
        alert.setHeaderText("¿Seguro que quiere eliminar esta cuenta?");
        alert.setContentText(seleccionada.getNombre());

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {

                //Eliminar de SceneManager
                SceneManager.getInstancia().eliminarCuenta(seleccionada);

                //Refrescar lista para ver que se ha eliminado 
                cargarCuentas();
            }
        });
    }

    private void mostrarError(String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
