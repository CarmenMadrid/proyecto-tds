package umu.tds.gastos.persistence;

import umu.tds.gastos.domain.alertas.Alertas;
import umu.tds.gastos.domain.core.Categoria;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertasRepository {
    void addAlerta(Alertas alerta);
    void updateAlerta(Alertas alerta);
    void deleteAlerta(UUID id);
    Optional<Alertas> getAlerta(UUID id);
    List<Alertas> getAllAlertas();
    List<Alertas> getAlertasByCuenta(UUID idCuenta);
    Alertas buscarAlerta(double limite, Categoria categoria, String nombreClaseAlerta);
}
