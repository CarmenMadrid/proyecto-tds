package umu.tds.gastos.ui.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
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
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.ui.controller.AddAlertaController;
import umu.tds.gastos.ui.controller.ConfirmacionController;
import umu.tds.gastos.ui.controller.CrearCategoriaController;
import umu.tds.gastos.ui.controller.CrearGastoController;
import umu.tds.gastos.ui.controller.EditarAlertaController;
import umu.tds.gastos.ui.controller.EditarGastoController;
import umu.tds.gastos.ui.controller.EliminarCategoriaController;
import umu.tds.gastos.ui.controller.ImportarController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;

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

    public void showAccountSummary(Cuenta cuenta) {
        try {
            primaryStage.setMaximized(true);

            StackPane oldRoot = (StackPane) escenaActual.getRoot();
            Parent originalContent = (Parent) oldRoot.getChildren().get(0);

            CuentaController cc = Configuracion.getInstancia().getCuentaController();
            List<Gasto> gastos = cc.obtenerGastos(cuenta.getId());
            double total = gastos.stream().mapToDouble(Gasto::getCantidad).sum();

            VBox content = new VBox(16);
            content.setPadding(new Insets(24));
            content.setStyle("-fx-background-color: #F5F7FA;");
            content.setPrefWidth(560);

            Label nombreCuenta = new Label(cuenta.getNombre());
            nombreCuenta.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1E2A38;");

            Label tipoCuenta = new Label(cuenta.isCompartida() ? "Cuenta compartida" : "Cuenta personal");
            tipoCuenta.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");

            content.getChildren().addAll(nombreCuenta, tipoCuenta);

            HBox metrics = new HBox(15);
            metrics.getChildren().addAll(
                    createMetricCard("Total gastado", String.format("%.2f €", total)),
                    createMetricCard("Gastos", String.valueOf(gastos.size()))
            );
            content.getChildren().add(metrics);

            if (cuenta.isCompartida()) {
                CuentaCompartida cc2 = (CuentaCompartida) cuenta;
                Label lblReparto = new Label("Reparto: " + (cc2.getTipoReparto() == CuentaCompartida.TipoReparto.EQUITATIVO ? "Equitativo" : "Personalizado"));
                lblReparto.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
                content.getChildren().add(lblReparto);

                Label lblParticipantes = new Label("Participantes");
                lblParticipantes.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1E2A38;");
                content.getChildren().add(lblParticipantes);

                Map<String, Double> porcentajes = cc.obtenerPorcentajes(cuenta.getId());
                VBox listaParticipantes = new VBox(8);

                if (porcentajes.isEmpty()) {
                    double eq = 100.0 / cc2.getPersonas().size();
                    for (Persona p : cc2.getPersonas()) {
                        HBox fila = crearFilaParticipante(p.getNombre(), eq);
                        listaParticipantes.getChildren().add(fila);
                    }
                } else {
                    for (Map.Entry<String, Double> e : porcentajes.entrySet()) {
                        HBox fila = crearFilaParticipante(e.getKey(), e.getValue());
                        listaParticipantes.getChildren().add(fila);
                    }
                }
                content.getChildren().add(listaParticipantes);
            }

            List<Alertas> alertas = cc.getAlertasByCuenta(cuenta.getId());
            Label lblAlertas = new Label("Límites y alertas");
            lblAlertas.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1E2A38;");
            content.getChildren().add(lblAlertas);

            if (alertas.isEmpty()) {
                Label sinAlertas = new Label("No hay límites configurados para esta cuenta.");
                sinAlertas.setStyle("-fx-text-fill: #9CA3AF; -fx-font-style: italic;");
                content.getChildren().add(sinAlertas);
            } else {
                VBox listaAlertas = new VBox(4);
                for (Alertas a : alertas) {
                    Label item = new Label("  " + a.toString());
                    item.setStyle("-fx-text-fill: #555; -fx-font-size: 13px;");
                    listaAlertas.getChildren().add(item);
                }
                content.getChildren().add(listaAlertas);
            }

            HBox buttons = new HBox(10);
            buttons.setAlignment(Pos.CENTER_RIGHT);
            Button btnCerrar = new Button("Cerrar");
            btnCerrar.setStyle("-fx-background-color: #3D5A80; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold;");
            btnCerrar.setPrefWidth(120);
            btnCerrar.setOnAction(e -> {
                escenaActual.setRoot(oldRoot);
                primaryStage.setMaximized(true);
                if (dialogKey != null) {
                    Platform.exitNestedEventLoop(dialogKey, null);
                    dialogKey = null;
                }
            });
            buttons.getChildren().add(btnCerrar);
            content.getChildren().add(buttons);

            TitledPane root = new TitledPane("Resumen de cuenta", content);
            root.setAnimated(false);

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

    private VBox createMetricCard(String titulo, String valor) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        Label lblValor = new Label(valor);
        lblValor.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E2A38;");

        VBox card = new VBox(5, lblTitulo, lblValor);
        card.setPadding(new Insets(15));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #E5E7EB;");

        return card;
    }

    private HBox crearFilaParticipante(String nombre, double porcentaje) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);

        Label lblName = new Label(nombre);
        lblName.setPrefWidth(100);
        lblName.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E2A38;");

        ProgressBar pb = new ProgressBar(porcentaje / 100.0);
        pb.setPrefWidth(200);
        pb.setStyle("-fx-accent: #3D5A80;");

        Label lblPct = new Label(String.format("%.0f%%", porcentaje));
        lblPct.setPrefWidth(50);
        lblPct.setStyle("-fx-text-fill: #6B7280;");

        fila.getChildren().addAll(lblName, pb, lblPct);
        return fila;
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
