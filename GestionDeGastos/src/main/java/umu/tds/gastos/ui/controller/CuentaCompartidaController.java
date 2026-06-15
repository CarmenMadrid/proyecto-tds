package umu.tds.gastos.ui.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Persona;
import umu.tds.gastos.ui.view.SceneManager;

import java.util.*;
import java.util.stream.Collectors;

public class CuentaCompartidaController {

    @FXML
    private TextField nombreCuentaCompartida;
    @FXML
    private TextField nombrePersona;
    @FXML
    private TableView<PersonaEntry> tablaPersonas;
    @FXML
    private TableColumn<PersonaEntry, String> columnaNombre;
    @FXML
    private TableColumn<PersonaEntry, Double> columnaPorcentaje;
    @FXML
    private TableColumn<PersonaEntry, Void> columnaAccion;
    @FXML
    private RadioButton rbEquitativo;
    @FXML
    private RadioButton rbPersonalizado;
    @FXML
    private ToggleGroup grupoReparto;
    @FXML
    private Label lblResumenParticipantes;
    @FXML
    private Label lblResumenMetodo;
    @FXML
    private Label lblResumenTotal;

    private final ObservableList<PersonaEntry> personas = FXCollections.observableArrayList();
    private final CuentaController cuentaController = Configuracion.getInstancia().getCuentaController();

    private static class PersonaEntry {
        private final StringProperty nombre = new SimpleStringProperty();
        private final DoubleProperty porcentaje = new SimpleDoubleProperty();

        PersonaEntry(String nombre, double porcentaje) {
            this.nombre.set(nombre);
            this.porcentaje.set(porcentaje);
        }

        StringProperty nombreProperty() { return nombre; }
        DoubleProperty porcentajeProperty() { return porcentaje; }
        String getNombre() { return nombre.get(); }
        double getPorcentaje() { return porcentaje.get(); }
        void setPorcentaje(double p) { porcentaje.set(p); }
    }

    @FXML
    void initialize() {
        tablaPersonas.setItems(personas);
        tablaPersonas.setEditable(false);

        columnaNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        columnaPorcentaje.setCellValueFactory(c -> c.getValue().porcentajeProperty().asObject());

        columnaAccion.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("✕");
            {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #C0392B; -fx-font-size: 14px; -fx-cursor: hand;");
                btn.setOnAction(e -> {
                    PersonaEntry entry = getTableView().getItems().get(getIndex());
                    eliminarPersona(entry);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        grupoReparto.selectedToggleProperty().addListener((obs, old, nuevo) -> {
            recalcularPorcentajes();
            configurarColumnaPorcentaje(rbPersonalizado.isSelected());
            tablaPersonas.refresh();
            actualizarResumen();
        });

        configurarColumnaPorcentaje(false);
        actualizarResumen();
    }

    private void configurarColumnaPorcentaje(boolean editable) {
        if (editable) {
            columnaPorcentaje.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
                @Override
                public String toString(Double value) {
                    if (value == null) return "";
                    return String.format("%.1f", value);
                }
                @Override
                public Double fromString(String text) {
                    try {
                        return Double.parseDouble(text.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }));
            columnaPorcentaje.setOnEditCommit(event -> {
                Double val = event.getNewValue();
                if (val == null || val < 0 || val > 100) {
                    tablaPersonas.refresh();
                    return;
                }
                event.getRowValue().setPorcentaje(val);
                actualizarResumen();
            });
            tablaPersonas.setEditable(true);
        } else {
            columnaPorcentaje.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double value, boolean empty) {
                    super.updateItem(value, empty);
                    setText(empty || value == null ? null : String.format("%.1f", value));
                }
            });
            columnaPorcentaje.setOnEditCommit(null);
            tablaPersonas.setEditable(false);
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
        personas.add(new PersonaEntry(nombre, 0));
        nombrePersona.clear();
        recalcularPorcentajes();
        tablaPersonas.refresh();
        actualizarResumen();
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

        CuentaCompartida.TipoReparto tipo = rbPersonalizado.isSelected()
                ? CuentaCompartida.TipoReparto.PERSONALIZADO
                : CuentaCompartida.TipoReparto.EQUITATIVO;

        if (tipo == CuentaCompartida.TipoReparto.PERSONALIZADO) {
            double suma = personas.stream()
                    .mapToDouble(PersonaEntry::getPorcentaje).sum();
            if (Math.abs(suma - 100.0) > 0.001) {
                mensajeError("Los porcentajes deben sumar 100. Suma actual: "
                        + String.format("%.1f", suma));
                return;
            }
        }

        List<Persona> listaPersonas = personas.stream()
                .map(p -> new Persona(p.getNombre()))
                .collect(Collectors.toList());

        CuentaCompartida cuenta = (CuentaCompartida)
                cuentaController.crearCuentaCompartida(nombreCuenta, listaPersonas, tipo);

        if (tipo == CuentaCompartida.TipoReparto.PERSONALIZADO) {
            Map<Persona, Double> mapa = new HashMap<>();
            for (PersonaEntry pp : personas) {
                Persona persona = listaPersonas.stream()
                        .filter(p -> p.getNombre().equals(pp.getNombre()))
                        .findFirst()
                        .orElseThrow();
                mapa.put(persona, pp.getPorcentaje());
            }
            cuenta.setPorcentajes(mapa);
        }

        cerrar();
    }

    private void eliminarPersona(PersonaEntry entry) {
        personas.remove(entry);
        recalcularPorcentajes();
        tablaPersonas.refresh();
        actualizarResumen();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrar();
    }

    private void cerrar() {
        SceneManager.getInstancia().closeDialog();
    }

    private void recalcularPorcentajes() {
        if (rbEquitativo.isSelected() && !personas.isEmpty()) {
            double eq = Math.round(100.0 / personas.size() * 100.0) / 100.0;
            for (PersonaEntry p : personas) {
                p.setPorcentaje(eq);
            }
        }
    }

    private void actualizarResumen() {
        int count = personas.size();
        lblResumenParticipantes.setText("Participantes: " + count);

        if (rbEquitativo.isSelected()) {
            lblResumenMetodo.setText("Método: Equitativo");
            if (count > 0) {
                lblResumenTotal.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold;");
                lblResumenTotal.setText("Reparto: " + String.format("%.1f", 100.0 / count) + "% por persona");
            } else {
                lblResumenTotal.setStyle("-fx-text-fill: #555;");
                lblResumenTotal.setText("Reparto total: —");
            }
        } else {
            lblResumenMetodo.setText("Método: Personalizado");
            if (count > 0) {
                double suma = personas.stream()
                        .mapToDouble(PersonaEntry::getPorcentaje).sum();
                double diff = 100.0 - suma;
                if (Math.abs(diff) < 0.01) {
                    lblResumenTotal.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold;");
                    lblResumenTotal.setText("Reparto total: " + String.format("%.1f", suma) + "% ✓");
                } else {
                    lblResumenTotal.setStyle("-fx-text-fill: #C0392B; -fx-font-weight: bold;");
                    lblResumenTotal.setText("Reparto total: " + String.format("%.1f", suma) + "%  (falta " + String.format("%.1f", diff) + "%)");
                }
            } else {
                lblResumenTotal.setStyle("-fx-text-fill: #555;");
                lblResumenTotal.setText("Reparto total: —");
            }
        }
    }

    private void mensajeError(String mensaje) {
        SceneManager.getInstancia().showError("Error", "No se puede crear la cuenta\n" + mensaje);
    }
}
