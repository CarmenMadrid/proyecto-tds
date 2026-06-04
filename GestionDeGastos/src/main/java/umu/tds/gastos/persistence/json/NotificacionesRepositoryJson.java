package umu.tds.gastos.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import umu.tds.gastos.domain.core.Notificacion;
import umu.tds.gastos.persistence.NotificacionesRepository;
import umu.tds.gastos.domain.alertas.Alertas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NotificacionesRepositoryJson implements NotificacionesRepository {

    private static final File NOTIFICACIONES_FILE = new File("data/notificaciones.json");

    private final JsonService jsonService;
    private List<Notificacion> notificaciones;

    public NotificacionesRepositoryJson() {
        this.jsonService = new JsonService();
        cargarDatos();
    }

    private void cargarDatos() {
        notificaciones = jsonService.cargar(NOTIFICACIONES_FILE, new TypeReference<List<Notificacion>>() {});
    }

    @Override
    public void addNotificacion(Notificacion notificacion) {
        notificaciones.add(notificacion);
        guardarDatos();
    }

    @Override
    public List<Notificacion> getAllNotificaciones() {
        return new ArrayList<>(notificaciones);
    }

    @Override
    public List<Notificacion> getNotificacionesByCuenta(String nombreCuenta) {
        return notificaciones.stream()
                .filter(n -> Objects.equals(n.getNombreCuenta(), nombreCuenta))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNotificacion(Notificacion notificacion) {
        notificaciones.remove(notificacion);
        guardarDatos();
    }
    @Override
    public void borrarPorAlerta(Alertas alerta) {
        notificaciones.removeIf(n -> Objects.equals(n.getAlerta().getId(), alerta.getId()));
        guardarDatos();
    }

    private void guardarDatos() {
        jsonService.guardar(NOTIFICACIONES_FILE, notificaciones);
    }
}
