package umu.tds.gastos.ui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static final String FXML_ROOT = "/umu/tds/gastos/ui/fxml/";
    private static SceneManager instancia;
    private Stage stage;

    private SceneManager() {
    }

    public static SceneManager getInstancia() {
        if (instancia == null) {
            instancia = new SceneManager();
        }
        return instancia;
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    public void showVentanaPrincipal() {
        cargarEscena("VentanaPrincipal.fxml", "Gestión de Gastos");
    }

    public void showNuevoGasto() {
        cargarEscena("NuevoGasto.fxml", "Nuevo Gasto");
    }

    public void showGraficos() {
        cargarEscena("Graficos.fxml", "Gráficos");
    }

    public void showCalendario() {
        cargarEscena("Calendario.fxml", "Calendario");
    }

    public void showAlertas() {
        cargarEscena("Alertas.fxml", "Alertas");
    }

    public void showCuentasCompartidas() {
        cargarEscena("CuentasCompartidas.fxml", "Cuentas Compartidas");
    }

    public void showImportar() {
        cargarEscena("Importar.fxml", "Importar Gastos");
    }

    private void cargarEscena(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
