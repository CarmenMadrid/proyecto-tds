package umu.tds.gastos.ui.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.Cuenta;

public class VentanaPrincipalController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="areaComando"
    private TextArea areaComando; // Value injected by FXMLLoader

    @FXML // fx:id="barChar"
    private Tab barChar; // Value injected by FXMLLoader

    @FXML // fx:id="barChart"
    private BarChart<?, ?> barChart; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddAlerta"
    private Button btnAddAlerta; // Value injected by FXMLLoader

    @FXML // fx:id="btnBorrar"
    private Button btnBorrar; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearCategoria"
    private Button btnCrearCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearCompartida"
    private Button btnCrearCompartida; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearCuenta"
    private Button btnCrearCuenta; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearGasto"
    private Button btnCrearGasto; // Value injected by FXMLLoader

    @FXML // fx:id="btnEditarAlerta"
    private Button btnEditarAlerta; // Value injected by FXMLLoader

    @FXML // fx:id="btnEditarGasto"
    private Button btnEditarGasto; // Value injected by FXMLLoader

    @FXML // fx:id="btnEliminarAlerta"
    private Button btnEliminarAlerta; // Value injected by FXMLLoader

    @FXML // fx:id="btnEliminarCuenta"
    private Button btnEliminarCuenta; // Value injected by FXMLLoader

    @FXML // fx:id="btnEliminarGasto"
    private Button btnEliminarGasto; // Value injected by FXMLLoader

    @FXML // fx:id="btnFiltrar"
    private Button btnFiltrar; // Value injected by FXMLLoader

    @FXML // fx:id="btnImportar"
    private Button btnImportar; // Value injected by FXMLLoader

    @FXML // fx:id="btnOpciones"
    private Button btnOpciones; // Value injected by FXMLLoader

    @FXML // fx:id="calFechaFin"
    private DatePicker calFechaFin; // Value injected by FXMLLoader

    @FXML // fx:id="calFechaInicio"
    private DatePicker calFechaInicio; // Value injected by FXMLLoader

    @FXML // fx:id="cantidadCol"
    private TableColumn<?, ?> cantidadCol; // Value injected by FXMLLoader

    @FXML // fx:id="cantidadColFiltro"
    private TableColumn<?, ?> cantidadColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="categoriaCol"
    private TableColumn<?, ?> categoriaCol; // Value injected by FXMLLoader

    @FXML // fx:id="categoriaColFiltro"
    private TableColumn<?, ?> categoriaColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="comboCuenta"
    private ComboBox<?> comboCuenta; // Value injected by FXMLLoader

    @FXML // fx:id="comboCuentaGraficas"
    private ComboBox<?> comboCuentaGraficas; // Value injected by FXMLLoader

    @FXML // fx:id="fechaCol"
    private TableColumn<?, ?> fechaCol; // Value injected by FXMLLoader

    @FXML // fx:id="fechaColFiltro"
    private TableColumn<?, ?> fechaColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="gastosTV"
    private TableView<?> gastosTV; // Value injected by FXMLLoader

    @FXML // fx:id="gastosTVFiltro"
    private TableView<?> gastosTVFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="listAlertas"
    private ListView<?> listAlertas; // Value injected by FXMLLoader

    @FXML // fx:id="listCategoria"
    private ListView<?> listCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="listImport"
    private ListView<?> listImport; // Value injected by FXMLLoader

    @FXML // fx:id="listMisCuentas"
    private ListView<Cuenta> listMisCuentas; // Value injected by FXMLLoader

    @FXML // fx:id="listNotificaciones"
    private ListView<?> listNotificaciones; // Value injected by FXMLLoader

    @FXML // fx:id="nombreCol"
    private TableColumn<?, ?> nombreCol; // Value injected by FXMLLoader

    @FXML // fx:id="nombreColFiltro"
    private TableColumn<?, ?> nombreColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="personaCol"
    private TableColumn<?, ?> personaCol; // Value injected by FXMLLoader

    @FXML // fx:id="personaColFiltro"
    private TableColumn<?, ?> personaColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="pieChart"
    private PieChart pieChart; // Value injected by FXMLLoader

    @FXML // fx:id="tbAbril"
    private ToggleButton tbAbril; // Value injected by FXMLLoader

    @FXML // fx:id="tbAgosto"
    private ToggleButton tbAgosto; // Value injected by FXMLLoader

    @FXML // fx:id="tbDiciembre"
    private ToggleButton tbDiciembre; // Value injected by FXMLLoader

    @FXML // fx:id="tbEnero"
    private ToggleButton tbEnero; // Value injected by FXMLLoader

    @FXML // fx:id="tbFebrero"
    private ToggleButton tbFebrero; // Value injected by FXMLLoader

    @FXML // fx:id="tbJulio"
    private ToggleButton tbJulio; // Value injected by FXMLLoader

    @FXML // fx:id="tbJunio"
    private ToggleButton tbJunio; // Value injected by FXMLLoader

    @FXML // fx:id="tbMarzo"
    private ToggleButton tbMarzo; // Value injected by FXMLLoader

    @FXML // fx:id="tbMayo"
    private ToggleButton tbMayo; // Value injected by FXMLLoader

    @FXML // fx:id="tbNoviembre"
    private ToggleButton tbNoviembre; // Value injected by FXMLLoader

    @FXML // fx:id="tbOctubre"
    private ToggleButton tbOctubre; // Value injected by FXMLLoader

    @FXML // fx:id="tbSeptiembre"
    private ToggleButton tbSeptiembre; // Value injected by FXMLLoader

    @FXML // fx:id="txtComando"
    private TextField txtComando; // Value injected by FXMLLoader



    @FXML
    public void initialize() {
        cargarCuentas();
    }
    
    private void cargarCuentas() {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        listMisCuentas.getItems().setAll(cc.obtenerCuentas());
    }
    
    @FXML
    private void crearCuenta() {
        SceneManager.getInstancia().showCrearCuenta();
        cargarCuentas(); // refrescar
    }
    
    @FXML
    private void crearCuentaCompartida() {
        SceneManager.getInstancia().showCrearCuentaCompartida();
        cargarCuentas(); //refrescar 
    }
    
    @FXML
    private void eliminarCuenta() {

        Cuenta seleccionada = listMisCuentas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
        	mensajeError("Debe seleccionar una cuenta para eliminarla.");
            return;
        }

        SceneManager.getInstancia().showConfirmation(
            "Eliminar cuenta",
            "¿Seguro que quiere eliminar esta cuenta?\n" + seleccionada.getNombre(),
            () -> {
                Configuracion.getInstancia()
                        .getCuentaController()
                        .eliminarCuenta(seleccionada.getId());
                cargarCuentas();
            }
        );
    }
    
    private void mensajeError(String mensaje) {
        SceneManager.getInstancia().showError("Error", "No se puede crear la cuenta\n" + mensaje);
    }


    /*
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


    @FXML /*
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
    }*/

}
