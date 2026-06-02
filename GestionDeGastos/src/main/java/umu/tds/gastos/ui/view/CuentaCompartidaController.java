package umu.tds.gastos.ui.view;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Persona;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CuentaCompartidaController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnAddPersona;

    @FXML
    private Button btnCancelar;

    @FXML
    private CheckBox btnPorcentaje;

    @FXML
    private TableColumn<PersonaPorcentaje, String> columnaNombre;

    @FXML
    private TableColumn<PersonaPorcentaje, Double> columnaPorcentaje;

    @FXML
    private TextField nombreCuentaCompartida;

    @FXML
    private TextField nombrePersona;

    @FXML
    private TextField porcentajePersona;

    @FXML
    private TableView<PersonaPorcentaje> tablaPersonasCuenta;

    private final ObservableList<PersonaPorcentaje> personas =
            FXCollections.observableArrayList();

    private final CuentaController cuentaController =
            Configuracion.getInstancia().getCuentaController();

    @FXML
    void initialize() {
        tablaPersonasCuenta.setItems(personas);

        columnaNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));

        columnaPorcentaje.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getPorcentaje()));
    }
    
    // Clase auxiliar para la tabla
    public static class PersonaPorcentaje {
        private final String nombre;
        private final double porcentaje;

        public PersonaPorcentaje(String nombre, double porcentaje) {
            this.nombre = nombre;
            this.porcentaje = porcentaje;
        }

        public String getNombre() {
            return nombre;
        }

        public double getPorcentaje() {
            return porcentaje;
        }
    }

    @FXML
    void addPersona(ActionEvent event) {
        String nombre = nombrePersona.getText().trim();

        if (nombre.isEmpty()) {
            mensajeError("Introduce un nombre para la persona");
            return;
        }
        
        boolean existe = personas.stream()
                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre));

        if (existe) {
            mensajeError("El nombre \"" + nombre + "\" ya existe. Introduce un nombre diferente.");
            return;
        }

        double porcentaje = 0;

        if (btnPorcentaje.isSelected()) {
            try {
                porcentaje = Double.parseDouble(porcentajePersona.getText().trim());
            } catch (NumberFormatException e) {
                mensajeError("El porcentaje introducido no es válido");
                return;
            }
        }

        personas.add(new PersonaPorcentaje(nombre, porcentaje));

        nombrePersona.clear();
        porcentajePersona.clear();
    }

   
    @FXML
    void aceptar(ActionEvent event) {
        String nombreCuenta = nombreCuentaCompartida.getText().trim();

        if (nombreCuenta.isEmpty()) {
            mensajeError("El nombre de la cuenta no puede estar vacío");
            return;
        }
        
        boolean existe = cuentaController.obtenerCuentas().stream()
                .anyMatch(c -> c.getNombre().equals(nombreCuenta));

        if (existe) {
            mensajeError("Ya existe una cuenta con el nombre \"" + nombreCuenta + "\". Elige otro nombre.");
            return;
        }

        if (personas.isEmpty()) {
            mensajeError("Añade al menos una persona");
            return;
        }

        List<Persona> listaPersonas = personas.stream()
                .map(p -> new Persona(p.getNombre()))
                .collect(Collectors.toList());

        CuentaCompartida.TipoReparto tipo = btnPorcentaje.isSelected()
                        ? CuentaCompartida.TipoReparto.PORCENTAJE
                        : CuentaCompartida.TipoReparto.EQUITATIVO;

        CuentaCompartida cuenta = (CuentaCompartida)
                cuentaController.crearCuentaCompartida(nombreCuenta, listaPersonas, tipo);

        if (tipo == CuentaCompartida.TipoReparto.PORCENTAJE) {
            Map<Persona, Double> mapa = new HashMap<>();

            for (PersonaPorcentaje pp : personas) {
                Persona persona = listaPersonas.stream()
                        .filter(p -> p.getNombre().equals(pp.getNombre()))
                        .findFirst()
                        .orElseThrow();

                mapa.put(persona, pp.getPorcentaje());
            }

            try {
                cuenta.setPorcentajes(mapa);
            } catch (IllegalArgumentException e) {
                mensajeError(e.getMessage());
                return;
            }
        }

        cerrar();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se puede crear la cuenta");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    

}
