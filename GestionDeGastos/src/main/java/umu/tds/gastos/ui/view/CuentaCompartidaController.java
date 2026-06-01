package umu.tds.gastos.ui.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.domain.core.Persona;

public class CuentaCompartidaController {

    @FXML private TextField nombreCuentaCompartida;

    @FXML private TableView<PersonaTabla> tablaPersonasCuenta;
    @FXML private TableColumn<PersonaTabla, String> columnaNombre;
    @FXML private TableColumn<PersonaTabla, Double> columnaPorcentaje;

    @FXML private TextField nombrePersona;
    @FXML private TextField porcentajePersona;

    @FXML private Button btnAddPersona;

    private ObservableList<PersonaTabla> personas = FXCollections.observableArrayList();


    public static class PersonaTabla {
        private final String nombre;
        private final double porcentaje;

        public PersonaTabla(String nombre, double porcentaje) {
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
    public void initialize() {
    	//Dice qué mostrar en la columna Nombre
        columnaNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        //Dice qué mostrar en la columna Porcentaje
        columnaPorcentaje.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPorcentaje()).asObject());
        //Conecta la tabla con la lista de personas
        tablaPersonasCuenta.setItems(personas);
    }


    @FXML
    private void añadirPersona() {

        String nombre = nombrePersona.getText().trim();
        String porcentajeStr = porcentajePersona.getText().trim();

        //Comprobamos que el campo no está vacío
        if (nombre.isEmpty()) {
        	mensajeError("El nombre no puede estar vacío.");
            return;
        }

        //Comprobamos que es un número el porcentaje
        double porcentaje;
        try {
            porcentaje = Double.parseDouble(porcentajeStr);
        } catch (NumberFormatException e) {
        	mensajeError("El porcentaje debe ser un número.");
            return;
        }
        
      //Comprobamos que el porcentaje está entre 0 y 100
        if (porcentaje < 0 || porcentaje > 100) {
        	mensajeError("El porcentaje debe estar entre 0 y 100.");
            return;
        }

        //Añadir a la tabla la persona
        personas.add(new PersonaTabla(nombre, porcentaje));

        //Limpiar campos para poder añadir más personas después
        nombrePersona.clear();
        porcentajePersona.clear();
    }


    @FXML
    private void aceptar() {

        String nombreCuenta = nombreCuentaCompartida.getText().trim();

        if (nombreCuenta.isEmpty()) {
        	mensajeError("Debe introducir un nombre para la cuenta compartida.");
            return;
        }

        if (personas.isEmpty()) {
        	mensajeError("Debe añadir al menos una persona.");
            return;
        }

        //Comprobar que da 100%
        double suma = personas.stream().mapToDouble(PersonaTabla::getPorcentaje).sum();
        if (Math.abs(suma - 100.0) > 0.001) {
        	mensajeError("Los porcentajes deben sumar 100. Suma actual: " + suma);
            return;
        }

        //Crear cuenta compartida
        CuentaCompartida cuenta = new CuentaCompartida();
        cuenta.setNombre(nombreCuenta);

        //Convertir personas de tabla
        java.util.List<Persona> listaModelo = new java.util.ArrayList<>();

        for (PersonaTabla p : personas) {
            Persona personaModelo = new Persona(p.getNombre());
            listaModelo.add(personaModelo);
        }

        cuenta.setPersonas(listaModelo);
        cuenta.setTipoReparto(CuentaCompartida.TipoReparto.PORCENTAJE);

        //Asignar porcentajes
        for (int i = 0; i < listaModelo.size(); i++) {
            cuenta.getPorcentajes().put(listaModelo.get(i), personas.get(i).getPorcentaje());
        }

        //Guardar en SceneManager
        SceneManager.getInstancia().getCuentas().add(cuenta);

        //Volver a la ventana principal
        SceneManager.getInstancia().showVentanaPrincipal();
    }


    @FXML
    private void cancelar() {
        SceneManager.getInstancia().showVentanaPrincipal();
    }

    private void mensajeError(String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error");
        a.setContentText(mensaje);
        a.showAndWait();
    }
}

