package umu.tds.gastos.ui.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.Gasto;

import java.io.IOException;


public class SceneManager {

    private static SceneManager instancia;
    //private Stage stage;
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
    
    public void showVentanaPrincipal() {
        cargarYMostrar("VentanaPrincipal");
        primaryStage.setMaximized(true);
    }

    public void showCrearCuenta() {
        showModal("CuentaNueva.fxml", "Crear Cuenta");
    }

    public void showCrearCuentaCompartida() {
        showModal("CuentaCompartida.fxml", "Crear Cuenta Compartida");
    }


    public void showCrearGasto(Cuenta cuenta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + "CrearGasto.fxml"));
            Parent root = loader.load();
            CrearGastoController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
            Stage stage = new Stage();
            stage.setTitle("Crear Gasto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showEditarGasto(Gasto gasto, Cuenta cuenta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + "EditarGasto.fxml"));
            Parent root = loader.load();
            EditarGastoController ctrl = loader.getController();
            ctrl.setGasto(gasto, cuenta);
            Stage stage = new Stage();
            stage.setTitle("Editar Gasto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCrearCategoria(Cuenta cuenta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + "CrearCategoria.fxml"));
            Parent root = loader.load();
            CrearCategoriaController ctrl = loader.getController();
            ctrl.setCuenta(cuenta);
            Stage stage = new Stage();
            stage.setTitle("Crear Categoría");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
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
                primaryStage.setScene(escenaActual);
                primaryStage.show();
            } else {
                escenaActual.setRoot(stack);
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
        showAlertOverlay(titulo, mensaje);
        dialogKey = new Object();
        Platform.enterNestedEventLoop(dialogKey);
    }

    public void showConfirmation(String titulo, String mensaje, Runnable onAccept) {
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

        Button btnOk = new Button("Aceptar");
        btnOk.setStyle("-fx-font-size: 14px;");
        Button btnCancel = new Button("Cancelar");
        btnCancel.setStyle("-fx-font-size: 14px;");

        VBox btnBox = new VBox(10, btnOk, btnCancel);
        btnBox.setAlignment(Pos.CENTER);

        VBox alertBox = new VBox(15, titleLabel, msgLabel, btnBox);
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        alertBox.setPadding(new Insets(20));
        alertBox.setMaxSize(400, Region.USE_PREF_SIZE);

        mainStack.getChildren().addAll(overlay, alertBox);

        btnOk.setOnAction(e -> {
            mainStack.getChildren().removeAll(overlay, alertBox);
            if (dialogKey != null) {
                Platform.exitNestedEventLoop(dialogKey, null);
                dialogKey = null;
            }
            if (onAccept != null) onAccept.run();
        });
        btnCancel.setOnAction(e -> {
            mainStack.getChildren().removeAll(overlay, alertBox);
            if (dialogKey != null) {
                Platform.exitNestedEventLoop(dialogKey, null);
                dialogKey = null;
            }
        });

        dialogKey = new Object();
        Platform.enterNestedEventLoop(dialogKey);
    }

    public void closeDialog() {
        if (dialogKey != null) {
            Platform.exitNestedEventLoop(dialogKey, null);
            dialogKey = null;
        }
    }

    public void showModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml));
            Parent root = loader.load();

            Rectangle overlay = new Rectangle();
            overlay.setFill(Color.color(0, 0, 0, 0.5));
            StackPane mainStack = (StackPane) escenaActual.getRoot();
            overlay.widthProperty().bind(mainStack.widthProperty());
            overlay.heightProperty().bind(mainStack.heightProperty());

            StackPane dialogWrapper = new StackPane(root);
            dialogWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            mainStack.getChildren().addAll(overlay, dialogWrapper);

            dialogKey = new Object();
            Platform.enterNestedEventLoop(dialogKey);

            mainStack.getChildren().removeAll(overlay, dialogWrapper);
            primaryStage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ROOT + fxml + ".fxml"));
        return loader.load();
    }
}
