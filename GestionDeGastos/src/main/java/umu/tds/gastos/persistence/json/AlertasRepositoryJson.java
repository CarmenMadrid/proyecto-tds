package umu.tds.gastos.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import umu.tds.gastos.domain.alertas.Alertas;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.persistence.AlertasRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AlertasRepositoryJson implements AlertasRepository {

    private static final File ALERTAS_FILE = new File("data/alertas.json");

    private final JsonService jsonService;
    private List<Alertas> alertas;

    public AlertasRepositoryJson() {
        this.jsonService = new JsonService();
        cargarDatos();
    }

    private void cargarDatos() {
        alertas = jsonService.cargar(ALERTAS_FILE, new TypeReference<List<Alertas>>() {});
    }

    @Override
    public void addAlerta(Alertas alerta) {
        alertas.add(alerta);
        guardarDatos();
    }

    @Override
    public void updateAlerta(Alertas alerta) {
        for (int i = 0; i < alertas.size(); i++) {
            if (Objects.equals(alertas.get(i).getId(), alerta.getId())) {
                alertas.set(i, alerta);
                guardarDatos();
                return;
            }
        }
    }

    @Override
    public void deleteAlerta(UUID id) {
        alertas.removeIf(a -> Objects.equals(a.getId(), id));
        guardarDatos();
    }

    @Override
    public Optional<Alertas> getAlerta(UUID id) {
        return alertas.stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
    }

    @Override
    public List<Alertas> getAllAlertas() {
        return new ArrayList<>(alertas);
    }

    @Override
    public Alertas buscarAlerta(double limite, Categoria categoria, String nombreClaseAlerta) {
        return alertas.stream()
                .filter(a -> a.getLimite() == limite)
                .filter(a -> Objects.equals(a.getCategoria(), categoria))
                .filter(a -> a.getTipo().getClass().getSimpleName().equals("Estrategia" + nombreClaseAlerta))
                .findFirst()
                .orElse(null);
    }

    private void guardarDatos() {
        jsonService.guardar(ALERTAS_FILE, alertas);
    }
}
