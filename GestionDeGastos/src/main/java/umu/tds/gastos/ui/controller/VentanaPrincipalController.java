package umu.tds.gastos.ui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.alertas.Alertas;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Gasto;
import umu.tds.gastos.domain.core.Notificacion;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.ui.view.SceneManager;
public class VentanaPrincipalController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="barChar"
    private Tab barChar; // Value injected by FXMLLoader

    @FXML // fx:id="barChart"
    private BarChart<String, Number> barChart; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddAlerta"
    private Button btnAddAlerta; // Value injected by FXMLLoader

    @FXML // fx:id="btnBorrar"
    private Button btnBorrar; // Value injected by FXMLLoader

    @FXML // fx:id="btnCrearCategoria"
    private Button btnCrearCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="btnEliminarCategoria"
    private Button btnEliminarCategoria; // Value injected by FXMLLoader

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
    private TableColumn<Gasto, Double> cantidadCol; // Value injected by FXMLLoader

    @FXML // fx:id="cantidadColFiltro"
    private TableColumn<Gasto, Double> cantidadColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="categoriaCol"
    private TableColumn<Gasto, String> categoriaCol; // Value injected by FXMLLoader

    @FXML // fx:id="categoriaColFiltro"
    private TableColumn<Gasto, String> categoriaColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="comboCuenta"
    private ComboBox<Cuenta> comboCuenta; // Value injected by FXMLLoader
    
    @FXML // fx:id="comboCuentaGraficas"
    private ComboBox<Cuenta> comboCuentaGraficas; // Value injected by FXMLLoader

    @FXML // fx:id="comboCuentaFiltro"
    private ComboBox<Cuenta> comboCuentaFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="comboCuentaAlertas"
    private ComboBox<Cuenta> comboCuentaAlertas; // Value injected by FXMLLoader

    @FXML // fx:id="fechaCol"
    private TableColumn<Gasto, LocalDate> fechaCol; // Value injected by FXMLLoader

    @FXML // fx:id="fechaColFiltro"
    private TableColumn<Gasto, LocalDate> fechaColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="gastosTV"
    private TableView<Gasto> gastosTV; // Value injected by FXMLLoader

    @FXML // fx:id="gastosTVFiltro"
    private TableView<Gasto> gastosTVFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="listAlertas"
    private ListView<Alertas> listAlertas; // Value injected by FXMLLoader

    @FXML // fx:id="comboCategoriaFiltro"
    private ComboBox<Categoria> comboCategoriaFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="comboPagadorFiltro"
    private ComboBox<Persona> comboPagadorFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="lblPagadorFiltro"
    private Label lblPagadorFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="listImport"
    private ListView<String> listImport; // Value injected by FXMLLoader

    @FXML // fx:id="listMisCuentas"
    private ListView<Cuenta> listMisCuentas; // Value injected by FXMLLoader

    @FXML // fx:id="listNotificaciones"
    private ListView<Notificacion> listNotificaciones; // Value injected by FXMLLoader

    @FXML // fx:id="tabAlertas"
    private Tab tabAlertas; // Value injected by FXMLLoader

    @FXML // fx:id="tabFiltro"
    private Tab tabFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="nombreCol"
    private TableColumn<Gasto, String> nombreCol; // Value injected by FXMLLoader

    @FXML // fx:id="personaCol"
    private TableColumn<Gasto, String> personaCol; // Value injected by FXMLLoader

    @FXML // fx:id="nombreColFiltro"
    private TableColumn<Gasto, String> nombreColFiltro; // Value injected by FXMLLoader

    @FXML // fx:id="personaColFiltro"
    private TableColumn<Gasto, String> personaColFiltro; // Value injected by FXMLLoader

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

    //Mapa de UUID de gasto para poder crear y editar
    private final Map<UUID, Cuenta> gastoCuentaMap = new HashMap<>();
    
    //Para que los dos desplegables sean el mismo
    private boolean sincronizandoCombos = false;

    @FXML
    public void initialize() {
        configurarTablaGastos();
        configurarTablaFiltro();
        configurarListaCategoria();
        configurarComboCuenta();

        refrescarCuentas();
        recargarAlertas();

        comboCuentaAlertas.setConverter(new StringConverter<>() {
            public String toString(Cuenta c) { return c == null ? "Todas las cuentas" : c.getNombre(); }
            public Cuenta fromString(String s) { return null; }
        });
        comboCuentaAlertas.valueProperty().addListener((obs, old, nueva) -> recargarAlertas());

        tabAlertas.selectedProperty().addListener((obs, old, nueva) -> {
            if (nueva) recargarAlertas();
        });

        tabFiltro.selectedProperty().addListener((obs, old, nueva) -> {
            if (nueva) {
                comboCuentaFiltro.setValue(comboCuenta.getValue());
                Cuenta cuenta = comboCuentaFiltro.getValue();
                actualizarListaCategorias(cuenta);
                comboPagadorFiltro.setValue(null);
                CuentaController cc = Configuracion.getInstancia().getCuentaController();
                if (cuenta != null) {
                    gastosTVFiltro.getItems().setAll(cc.obtenerGastos(cuenta.getId()));
                } else {
                    List<Gasto> todos = cc.obtenerCuentas().stream()
                            .flatMap(c -> cc.obtenerGastos(c.getId()).stream())
                            .collect(Collectors.toList());
                    gastosTVFiltro.getItems().setAll(todos);
                }
            }
        });
    }
    
    
    private void configurarListaCategoria() {
        comboCategoriaFiltro.setConverter(new StringConverter<>() {
            public String toString(Categoria c) { return c == null ? "" : c.getNombre(); }
            public Categoria fromString(String s) { return null; }
        });
        comboPagadorFiltro.setConverter(new StringConverter<>() {
            public String toString(Persona p) { return p == null ? "" : p.getNombre(); }
            public Persona fromString(String s) { return null; }
        });
        lblPagadorFiltro.setVisible(false);
        lblPagadorFiltro.setManaged(false);
        comboPagadorFiltro.setVisible(false);
        comboPagadorFiltro.setManaged(false);
    }
    
    private void configurarTablaGastos() {
        nombreCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        cantidadCol.setCellValueFactory(d ->
                new SimpleObjectProperty<>(d.getValue().getCantidad()));
        fechaCol.setCellValueFactory(d ->
                new SimpleObjectProperty<>(d.getValue().getFecha()));
        categoriaCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getCategoria().getNombre()));
        personaCol.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getPagador() != null
                                ? d.getValue().getPagador().getNombre() : "Yo"));
    }
    
    private void configurarTablaFiltro() {
        nombreColFiltro.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getNombre()));
        cantidadColFiltro.setCellValueFactory(d ->
                new SimpleObjectProperty<>(d.getValue().getCantidad()));
        fechaColFiltro.setCellValueFactory(d ->
                new SimpleObjectProperty<>(d.getValue().getFecha()));
        categoriaColFiltro.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getCategoria().getNombre()));
        personaColFiltro.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getPagador() != null
                                ? d.getValue().getPagador().getNombre() : "Yo"));
    }

    private void refrescarCuentas() {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        List<Cuenta> cuentas = cc.obtenerCuentas();
        listMisCuentas.getItems().setAll(cuentas);
        Cuenta seleccionada = comboCuenta.getValue();
        sincronizandoCombos = true;
        comboCuenta.getItems().setAll(cuentas);
        comboCuentaGraficas.getItems().setAll(cuentas);
        comboCuentaFiltro.getItems().setAll(cuentas);
        comboCuentaAlertas.getItems().setAll(cuentas);
        sincronizandoCombos = false;
        if (seleccionada != null && cuentas.contains(seleccionada)) {
            comboCuenta.setValue(seleccionada);
            comboCuentaGraficas.setValue(seleccionada);
            comboCuentaFiltro.setValue(seleccionada);
            comboCuentaAlertas.setValue(seleccionada);
        }
    }
    
    
    @FXML
    private void crearCuenta() {
        SceneManager.getInstancia().showCrearCuenta();
        refrescarCuentas();
        recargarCuentaActual();
    }

    @FXML
    private void crearCuentaCompartida() {
        SceneManager.getInstancia().showCrearCuentaCompartida();
        refrescarCuentas();
        recargarCuentaActual();
    }
    
    @FXML
    private void eliminarCuenta() {
        Cuenta seleccionada = listMisCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeError("Selecciona una cuenta para eliminarla.");
            return;
        }
        SceneManager.getInstancia().showConfirmation(
            "Eliminar cuenta",
            "¿Seguro que quiere eliminar la cuenta \"" +
            seleccionada.getNombre() + "\"?",
            () -> {
                boolean eraPrincipal = seleccionada.equals(comboCuenta.getValue());
                Configuracion.getInstancia()
                        .getCuentaController()
                        .eliminarCuenta(seleccionada.getId());
                if (eraPrincipal) {
                    sincronizandoCombos = true;
                    comboCuenta.setValue(null);
                    comboCuentaGraficas.setValue(null);
                    sincronizandoCombos = false;
                    gastosTV.getItems().clear();
                    barChart.getData().clear();
                    pieChart.getData().clear();
                }
                refrescarCuentas();
                recargarCuentaActual();
            });
    }

    private void recargarCuentaActual() {
        Cuenta actual = cuentaActual();
        if (actual != null) {
            cargarGastosCuenta(actual);
            actualizarGraficas(actual);
            actualizarListaCategorias(actual);
        } else {
            gastosTV.getItems().clear();
            barChart.getData().clear();
            pieChart.getData().clear();
        }
        recargarAlertas();
    }

    private void mensajeError(String mensaje) {
        SceneManager.getInstancia().showError("Error", mensaje);
    }
    
    private Cuenta cuentaActual() {
        return (Cuenta) comboCuenta.getValue();
    }

    private void cargarGastosCuenta(Cuenta cuenta) {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        gastoCuentaMap.clear(); 
        List<Gasto> gastos = cc.obtenerGastos(cuenta.getId());
        gastos.forEach(g -> gastoCuentaMap.put(g.getId(), cuenta));
        gastosTV.getItems().setAll(gastos);
    }

    private void actualizarListaCategorias(Cuenta cuenta) {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        if (cuenta != null) {
            comboCategoriaFiltro.getItems().setAll(cc.obtenerCategorias(cuenta.getId()));
        } else {
            comboCategoriaFiltro.getItems().setAll(cc.obtenerTodasLasCategorias());
        }
        comboPagadorFiltro.getItems().clear();
        if (cuenta instanceof CuentaCompartida cc2) {
            comboPagadorFiltro.getItems().setAll(cc2.getPersonas());
            lblPagadorFiltro.setVisible(true);
            lblPagadorFiltro.setManaged(true);
            comboPagadorFiltro.setVisible(true);
            comboPagadorFiltro.setManaged(true);
        } else {
            lblPagadorFiltro.setVisible(false);
            lblPagadorFiltro.setManaged(false);
            comboPagadorFiltro.setVisible(false);
            comboPagadorFiltro.setManaged(false);
        }
    }

    private void cargarTodosLosGastos() {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        gastoCuentaMap.clear(); //limpiamos para volver a hacerlo
        List<Gasto> todos = cc.obtenerCuentas().stream()
                .flatMap(c -> { //para cada cuenta un stream 
                    List<Gasto> gs = cc.obtenerGastos(c.getId()); //pedimos la lista de gastos
                    gs.forEach(g -> gastoCuentaMap.put(g.getId(), c)); //guardamos cada gasto con el id de la cuenta
                    return gs.stream();
                })
                .collect(Collectors.toList());
        gastosTV.getItems().setAll(todos);
    }

    @FXML
    void crearGasto(ActionEvent event) {
        Cuenta preseleccionada = cuentaActual();
        if (preseleccionada == null) {
            mensajeError("Seleccione una cuenta primero.");
            return;
        }
        SceneManager.getInstancia().showCrearGasto(preseleccionada);
        recargarCuentaActual();
    }

    @FXML
    void crearCategoria(ActionEvent event) {
        Cuenta cuenta = cuentaActual();
        if (cuenta == null) {
            mensajeError("Seleccione una cuenta primero.");
            return;
        }
        SceneManager.getInstancia().showCrearCategoria(cuenta);
        actualizarListaCategorias(cuenta);
    }
    
    @FXML
    void eliminarCategoria(ActionEvent event) {
        Cuenta cuenta = cuentaActual();
        if (cuenta == null) {
            mensajeError("Seleccione una cuenta primero.");
            return;
        }
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        List<Categoria> categorias = cc.obtenerCategorias(cuenta.getId());
        if (categorias.isEmpty()) {
            mensajeError("No hay categorías para eliminar.");
            return;
        }
        SceneManager.getInstancia().showEliminarCategoria(cuenta, categorias, () -> {
            actualizarListaCategorias(cuenta);
            recargarCuentaActual();
        });
    }
    
    @FXML
    void editarGasto(ActionEvent event) {
        Gasto seleccionado = gastosTV.getSelectionModel().getSelectedItem();
        if (seleccionado == null) { mensajeError("Seleccione un gasto."); return; }
        Cuenta cuenta = gastoCuentaMap.get(seleccionado.getId());
        if (cuenta == null) { mensajeError("No se encontró la cuenta."); return; }
        SceneManager.getInstancia().showEditarGasto(seleccionado, cuenta);
        cargarGastosCuenta(cuenta);
        actualizarGraficas(cuenta);
    }

    @FXML
    void eliminarGasto(ActionEvent event) {
        Gasto seleccionado = gastosTV.getSelectionModel().getSelectedItem();
        if (seleccionado == null) { mensajeError("Seleccione un gasto."); return; }
        Cuenta cuenta = gastoCuentaMap.get(seleccionado.getId());
        SceneManager.getInstancia().showConfirmation(
            "Eliminar gasto",
            "¿Seguro que desea eliminar el gasto \"" +
            seleccionado.toString() + "\"?",
            () -> {
                Configuracion.getInstancia().getCuentaController()
                        .eliminarGasto(cuenta.getId(), seleccionado.getId());
                cargarGastosCuenta(cuenta);
                actualizarGraficas(cuenta);
            });
    }
    
    /*Configuramos ambos desplegables con el mismo converter y listeners.
    Cuando se selecciona una cuenta en cualquiera de los dos el otro se actualiza automáticamente.*/
    private void configurarComboCuenta() {
        StringConverter<Cuenta> conv = new StringConverter<>() {
            public String toString(Cuenta c) { return c == null ? "" : c.getNombre(); }
            public Cuenta fromString(String s) { return null; }
        };
        comboCuenta.setConverter(conv);
        comboCuentaGraficas.setConverter(conv);

        comboCuenta.valueProperty().addListener((obs, old, nueva) -> {
            if (sincronizandoCombos || nueva == null) return;
            sincronizandoCombos = true;
            comboCuentaGraficas.setValue(nueva);
            comboCuentaFiltro.setValue(nueva);
            comboCuentaAlertas.setValue(nueva);
            sincronizandoCombos = false;
            cargarGastosCuenta(nueva);
            actualizarGraficas(nueva);
            actualizarListaCategorias(nueva);
        });

        comboCuentaGraficas.valueProperty().addListener((obs, old, nueva) -> {
            if (sincronizandoCombos || nueva == null) return;
            sincronizandoCombos = true;
            comboCuenta.setValue(nueva);
            comboCuentaFiltro.setValue(nueva);
            comboCuentaAlertas.setValue(nueva);
            sincronizandoCombos = false;
            cargarGastosCuenta(nueva);
            actualizarGraficas(nueva);
        });

        comboCuentaFiltro.valueProperty().addListener((obs, old, nueva) -> {
            if (sincronizandoCombos) return;
            sincronizandoCombos = true;
            if (nueva != null) {
                comboCuenta.setValue(nueva);
                comboCuentaGraficas.setValue(nueva);
            }
            sincronizandoCombos = false;
            actualizarListaCategorias(nueva);
            comboCategoriaFiltro.setValue(null);
            comboPagadorFiltro.setValue(null);
        });
    } 
    
    private void actualizarGraficas(Cuenta cuenta) {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        Map<String, Double> datos = cc.obtenerGastosPorCategoria(cuenta.getId());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(cuenta.getNombre());
        datos.forEach((cat, total) -> series.getData().add(new XYChart.Data<>(cat, total)));
        barChart.getData().clear();
        barChart.getData().add(series);

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        datos.forEach((cat, total) -> pieData.add(new PieChart.Data(cat, total)));
        pieChart.setData(pieData);
    }
    
    @FXML
    void importar(ActionEvent event) {
        SceneManager.getInstancia().showImportar(cuentaActual(), count -> {
            Cuenta c = cuentaActual();
            String msg = "Se han importado " + count + " gastos";
            if (c != null) msg += " en la cuenta \"" + c.getNombre() + "\"";
            listImport.getItems().add(0, msg);
        });
        recargarCuentaActual();
    }
    
    private void recargarAlertas() {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        Cuenta cuenta = comboCuentaAlertas.getValue();
        if (cuenta != null) {
            listAlertas.getItems().setAll(cc.getAlertasByCuenta(cuenta.getId()));
        } else {
            listAlertas.getItems().setAll(cc.getAlertas());
        }
        listNotificaciones.getItems().setAll(cc.getNotificaciones());
    }

    @FXML
    void addAlerta(ActionEvent event) {
        Cuenta cuenta = comboCuentaAlertas.getValue();
        if (cuenta == null) {
            mensajeError("Seleccione una cuenta primero.");
            return;
        }
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        SceneManager.getInstancia().showAddAlerta(cuenta, cc.obtenerCategorias(cuenta.getId()));
        recargarAlertas();
    }

    @FXML
    void editarAlerta(ActionEvent event) {
        Alertas seleccionada = listAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeError("Seleccione una alerta para editar.");
            return;
        }
        Cuenta cuenta = comboCuentaAlertas.getValue();
        if (cuenta == null) {
            mensajeError("Seleccione una cuenta primero.");
            return;
        }
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        SceneManager.getInstancia().showEditarAlerta(seleccionada, cc.obtenerCategorias(cuenta.getId()));
        recargarAlertas();
    }

    @FXML
    void eliminarAlerta(ActionEvent event) {
        Alertas seleccionada = listAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensajeError("Seleccione una alerta para eliminar.");
            return;
        }
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        String nombreCuenta = cc.obtenerCuentas().stream()
                .filter(c -> c.getId().equals(seleccionada.getIdCuenta()))
                .findFirst().map(Cuenta::getNombre).orElse("Desconocida");
        String tipo = seleccionada.getTipo().getNombreEstrategia().split(" ")[1].toLowerCase();
        StringBuilder sb = new StringBuilder();
        sb.append(nombreCuenta).append(": Límite ").append(tipo).append(" de ");
        sb.append(String.format("%.2f", seleccionada.getLimite())).append("\u20AC");
        if (seleccionada.getCategoria() != null) {
            sb.append(" en ").append(seleccionada.getCategoria().getNombre());
        }
        SceneManager.getInstancia().showConfirmation(
            "Eliminar alerta",
            "¿Seguro que desea eliminar la alerta \"" + sb.toString() + "\"?",
            () -> {
                cc.borrarAlerta(seleccionada);
                recargarAlertas();
            });
    }

    @FXML
    void borrar(ActionEvent event) {
        comboCategoriaFiltro.setValue(null);
        comboPagadorFiltro.setValue(null);
        comboCuentaFiltro.setValue(null);
        calFechaInicio.setValue(null);
        calFechaFin.setValue(null);
        ToggleButton[] toggles = {tbEnero, tbFebrero, tbMarzo, tbAbril, tbMayo, tbJunio,
                tbJulio, tbAgosto, tbSeptiembre, tbOctubre, tbNoviembre, tbDiciembre};
        for (ToggleButton tb : toggles) tb.setSelected(false);
        CuentaController cc = Configuracion.getInstancia().getCuentaController();
        Cuenta cuenta = comboCuentaFiltro.getValue();
        if (cuenta != null) {
            gastosTVFiltro.getItems().setAll(cc.obtenerGastos(cuenta.getId()));
        } else {
            List<Gasto> todos = cc.obtenerCuentas().stream()
                    .flatMap(c -> cc.obtenerGastos(c.getId()).stream())
                    .collect(Collectors.toList());
            gastosTVFiltro.getItems().setAll(todos);
        }
    }

    
    @FXML
    void filtrar(ActionEvent event) {
        CuentaController cc = Configuracion.getInstancia().getCuentaController();

        Categoria categoria = comboCategoriaFiltro.getValue();
        List<Categoria> categorias = categoria != null ? List.of(categoria) : List.of();
        Persona pagador = comboPagadorFiltro.getValue();
        LocalDate fechaInicio = calFechaInicio.getValue();
        LocalDate fechaFin = calFechaFin.getValue();

        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            mensajeError("La fecha de fin no puede ser anterior a la fecha de inicio.");
            return;
        }

        List<Month> meses = new ArrayList<>();
        ToggleButton[] toggles = {tbEnero, tbFebrero, tbMarzo, tbAbril, tbMayo, tbJunio,
                tbJulio, tbAgosto, tbSeptiembre, tbOctubre, tbNoviembre, tbDiciembre};
        Month[] months = {Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER,
                Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER};
        for (int i = 0; i < toggles.length; i++) {
            if (toggles[i].isSelected()) meses.add(months[i]);
        }

        Cuenta cuenta = comboCuentaFiltro.getValue();
        if (cuenta != null) {
            gastosTVFiltro.getItems().setAll(cc.filtrarGastos(cuenta.getId(), categorias, fechaInicio, fechaFin, meses, pagador));
        } else {
            List<Gasto> filtrados = cc.obtenerCuentas().stream()
                    .flatMap(c -> cc.filtrarGastos(c.getId(), categorias, fechaInicio, fechaFin, meses, pagador).stream())
                    .collect(Collectors.toList());
            gastosTVFiltro.getItems().setAll(filtrados);
        }
    }


}
