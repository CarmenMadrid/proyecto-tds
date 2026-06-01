package umu.tds.gastos.persistence;

import umu.tds.gastos.domain.core.Notificacion;
import umu.tds.gastos.domain.alertas.Alertas;
import java.util.List;

public interface NotificacionesRepository {
    void addNotificacion(Notificacion notificacion);
    List<Notificacion> getAllNotificaciones();
    List<Notificacion> getNotificacionesByCuenta(String nombreCuenta);
    void deleteNotificacion(Notificacion notificacion);
    void borrarPorAlerta(Alertas alerta);
}
