package umu.tds.gastos.ui.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import umu.tds.gastos.domain.alertas.Alertas;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.ui.controller.AddAlertaController;
import umu.tds.gastos.ui.controller.ConfirmacionController;
import umu.tds.gastos.ui.controller.CrearCategoriaController;
import umu.tds.gastos.ui.controller.CrearGastoController;
import umu.tds.gastos.ui.controller.EditarAlertaController;
import umu.tds.gastos.ui.controller.EditarGastoController;
import umu.tds.gastos.ui.controller.EliminarCategoriaController;
import umu.tds.gastos.ui.controller.ImportarController;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneManager {

    private static SceneManager instancia;
    private Scene escenaActual;
    private Stage primaryStage;
    private Object dialogKey;

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

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showLogin() {
        cargarYMostrar("Login");
        primaryStage.setMaximized(true);
    }

    public void showVentanaPrincipal() {
        cargarYMostrar("VentanaPrincipal");
        primaryStage.setMaximized(true);
    }

    public void showCrearCuenta() {
        showOverlayDialog("CuentaNueva.fxml", null);
    }

    public void showCrearCuentaCompartida() {
        showOverlayDialog("CuentaCompartida.fxml", null);
    }

    public void showCrearGasto(Cuenta cuenta) {
        showOverlayDialog("CrearGasto.fxml", loader -> {
            CrearGastoController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
        });
    }

    public void showEditarGasto(Gasto gasto, Cuenta cuenta) {
        showOverlayDialog("EditarGasto.fxml", loader -> {
            EditarGastoController ctrl = loader.getController();
            ctrl.setGasto(gasto, cuenta);
        });
    }

    public void showCrearCategoria(Cuenta cuenta) {
        showOverlayDialog("CrearCategoria.fxml", loader -> {
            CrearCategoriaController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
        });
    }

    public void showEliminarCategoria(Cuenta cuenta, java.util.List<umu.tds.gastos.domain.core.Categoria> categorias, Runnable onDeleted) {
        showOverlayDialog("EliminarCategoria.fxml", loader -> {
            EliminarCategoriaController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
            ctrl.setCategorias(categorias);
            ctrl.setOnDeleted(onDeleted);
        });
    }

    public void showAddAlerta(Cuenta cuenta, java.util.List<Categoria> categorias) {
        showOverlayDialog("AddAlerta.fxml", loader -> {
            AddAlertaController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
            ctrl.setCategorias(categorias);
        });
    }

    public void showEditarAlerta(Alertas alerta, java.util.List<Categoria> categorias) {
        showOverlayDialog("EditarAlerta.fxml", loader -> {
            EditarAlertaController ctrl = loader.getController();
            ctrl.setAlerta(alerta);
            ctrl.setCategorias(categorias);
        });
    }

    public void showImportar(Cuenta cuenta, Consumer<Integer> onImported) {
    	//Creo que en vez de Integer sería String
        showOverlayDialog("Importar.fxml", loader -> {
            ImportarController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
            ctrl.setOnImported(onImported);
        });
    }

    private void showOverlayDialog(String fxml, Consumer<FXMLLoader> initController) {
        try {
            primaryStage.setMaximized(true);

            StackPane oldRoot = (StackPane) escenaActual.getRoot();
            Parent originalContent = (Parent) oldRoot.getChildren().get(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml));
            Parent root = loader.load();

            if (initController != null) {
                initController.accept(loader);
            }

            Pane overlay = new Pane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

            StackPane dialogWrapper = new StackPane(root);
            dialogWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            StackPane newRoot = new StackPane(originalContent, overlay, dialogWrapper);
            escenaActual.setRoot(newRoot);
            primaryStage.setMaximized(true);

            dialogKey = new Object();
            Platform.enterNestedEventLoop(dialogKey);

            oldRoot.getChildren().add(originalContent);
            escenaActual.setRoot(oldRoot);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarYMostrar(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            root.setStyle("-fx-font-size: 14px;");
            StackPane stack = new StackPane(root);

            if (escenaActual == null) {
                escenaActual = new Scene(stack);
                escenaActual.getStylesheets().add(getClass().getResource("/umu/tds/gastos/ui/css/styles.css").toExternalForm());
                primaryStage.setScene(escenaActual);
                primaryStage.show();
            } else {
                escenaActual.setRoot(stack);
                escenaActual.getStylesheets().add(getClass().getResource("/umu/tds/gastos/ui/css/styles.css").toExternalForm());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar de escena a: " + fxml, e);
        }
    }

    private void showAlertOverlay(String titulo, String mensaje) {
        StackPane mainStack = (StackPane) escenaActual.getRoot();

        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.color(0, 0, 0, 0.5));
        overlay.widthProperty().bind(mainStack.widthProperty());
        overlay.heightProperty().bind(mainStack.heightProperty());

        Label titleLabel = new Label(titulo);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label msgLabel = new Label(mensaje);
        msgLabel.setStyle("-fx-font-size: 14px;");
        msgLabel.setWrapText(true);

        VBox alertBox = new VBox(15, titleLabel, msgLabel);
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        alertBox.setPadding(new Insets(20));
        alertBox.setMaxSize(400, Region.USE_PREF_SIZE);

        Button btnOk = new Button("Aceptar");
        btnOk.setStyle("-fx-font-size: 14px;");
        btnOk.setOnAction(e -> {
            mainStack.getChildren().removeAll(overlay, alertBox);
            if (dialogKey != null) {
                Platform.exitNestedEventLoop(dialogKey, null);
                dialogKey = null;
            }
        });
        alertBox.getChildren().add(btnOk);

        mainStack.getChildren().addAll(overlay, alertBox);
    }

    public void showError(String titulo, String mensaje) {
        Object prevKey = dialogKey;
        showAlertOverlay(titulo, mensaje);
        dialogKey = new Object();
        Platform.enterNestedEventLoop(dialogKey);
        dialogKey = prevKey;
    }

    public void showConfirmation(String titulo, String mensaje, Runnable onAccept) {
        try {
            primaryStage.setMaximized(true);

            StackPane oldRoot = (StackPane) escenaActual.getRoot();
            Parent originalContent = (Parent) oldRoot.getChildren().get(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + "Confirmacion.fxml"));
            Parent root = loader.load();

            ConfirmacionController controller = loader.getController();
            controller.setMensaje(mensaje);
            controller.setOnAccept(onAccept);

            Pane overlay = new Pane();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

            StackPane dialogWrapper = new StackPane(root);
            dialogWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            StackPane newRoot = new StackPane(originalContent, overlay, dialogWrapper);
            escenaActual.setRoot(newRoot);
            primaryStage.setMaximized(true);

            dialogKey = new Object();
            Platform.enterNestedEventLoop(dialogKey);

            oldRoot.getChildren().add(originalContent);
            escenaActual.setRoot(oldRoot);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        if (dialogKey != null) {
            Platform.exitNestedEventLoop(dialogKey, null);
            dialogKey = null;
        }
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml + ".fxml"));
        return loader.load();
    }
}
