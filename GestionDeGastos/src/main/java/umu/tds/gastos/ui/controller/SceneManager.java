package umu.tds.gastos.ui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import umu.tds.gastos.domain.core.Cuenta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private static SceneManager instancia;
    private Stage stage;
    private Scene escenaActual;

    private List<Cuenta> cuentas = new ArrayList<>();

    private static final String FXML_ROOT = "/umu/tds/gastos/ui/fxml/";

    private SceneManager() {}

    public static SceneManager getInstancia() {
        if (instancia == null) {
            instancia = new SceneManager();
        }
        return instancia;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void eliminarCuenta(Cuenta cuenta) {
        cuentas.remove(cuenta);
    }

    public void init(Stage stage) {
        this.stage = stage;
    }


    public void showVentanaPrincipal() {
        cargarYMostrar("VentanaPrincipal");
    }

    public void showCrearCuenta() {
        cargarYMostrar("CrearCuenta");
    }

    public void showCrearCuentaCompartida() {
        cargarYMostrar("CrearCuentaCompartida");
    }


    public void showNuevoGasto() {
        cargarYMostrar("NuevoGasto");
    }

    public void showGraficos() {
        cargarYMostrar("Graficos");
    }

    public void showCalendario() {
        cargarYMostrar("Calendario");
    }

    public void showAlertas() {
        cargarYMostrar("Alertas");
    }

    public void showCuentasCompartidas() {
        cargarYMostrar("CuentasCompartidas");
    }

    public void showImportar() {
        cargarYMostrar("Importar");
    }

    private void cargarYMostrar(String fxml) {
        try {
            Parent root = loadFXML(fxml);

            if (escenaActual == null) {
                escenaActual = new Scene(root);
                stage.setScene(escenaActual);
                stage.show();
            } else {
                escenaActual.setRoot(root);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void cargarYMostrarDialogo(String fxml, String titulo) {
        try {
            DialogPane pane = (DialogPane) loadFXML(fxml);
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle(titulo);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml + ".fxml"));
        return loader.load();
    }
}
