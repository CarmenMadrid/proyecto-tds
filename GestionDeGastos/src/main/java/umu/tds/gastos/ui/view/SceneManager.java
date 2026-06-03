package umu.tds.gastos.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import umu.tds.gastos.domain.core.Cuenta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class SceneManager {

    private static SceneManager instancia;
    //private Stage stage;
    private Scene escenaActual;
    private FXMLLoader ultimoLoader;
    private Stage primaryStage;

    private static final String FXML_ROOT = "/umu/tds/gastos/ui/fxml/";

    private SceneManager() {}

    public static SceneManager getInstancia() {
        if (instancia == null) {
            instancia = new SceneManager();
        }
        return instancia;
    }

    public void init(Stage stage) {
        this.primaryStage = stage;
    }
    
    public void showVentanaPrincipal() {
        cargarYMostrar("VentanaPrincipal");
    }

    public void showCrearCuenta() {
        showModal("CuentaNueva.fxml", "Crear Cuenta");
    }

    public void showCrearCuentaCompartida() {
        showModal("CuentaCompartida.fxml", "Crear Cuenta Compartida");
    }

    public void showCrearGasto() {
        showModal("GastoNuevo.fxml", "Crear Gasto");
    }

    public void showCrearCategoria() {
        showModal("CategoriaNueva.fxml", "Crear Categoría");
    }


    private void cargarYMostrar(String fxml) {
        try {
            Parent root = loadFXML(fxml);

            if (escenaActual == null) {
                escenaActual = new Scene(root);
                primaryStage.setScene(escenaActual);
                primaryStage.show();
            } else {
                escenaActual.setRoot(root);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar de escena a: " + fxml, e);
        }
    }

    private void cargarYMostrarDialogo(String fxml, String titulo) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml + ".fxml"));
            DialogPane pane = loader.load();
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle(titulo);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el diálogo: " + fxml + ".fxml", e);
        }
    }
    
    
    public void showModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml + ".fxml"));
        return loader.load();
    }
}
