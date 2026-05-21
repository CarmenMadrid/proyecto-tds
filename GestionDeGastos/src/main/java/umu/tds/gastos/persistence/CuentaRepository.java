package umu.tds.gastos.persistence;

import umu.tds.gastos.domain.core.Cuenta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CuentaRepository {
    void addCuenta(Cuenta cuenta);
    void updateCuenta(Cuenta cuenta);
    void deleteCuenta(UUID id);
    Optional<Cuenta> getCuenta(UUID id);
    List<Cuenta> getAllCuentas();
}
