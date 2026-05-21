package umu.tds.gastos.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Cuenta;
import umu.tds.gastos.domain.core.CuentaCompartida;
import umu.tds.gastos.persistence.CuentaRepository;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CuentaRepositoryJson implements CuentaRepository {

    private static final File PERSONALES_FILE = new File("data/cuentas.json");
    private static final File COMPARTIDAS_FILE = new File("data/cuentas-compartidas.json");

    private final JsonService jsonService;
    private List<Cuenta> cuentas;

    public CuentaRepositoryJson() {
        this.jsonService = new JsonService();
        cargarDatos();
    }

    private void cargarDatos() {
        List<Cuenta> personales = jsonService.cargar(PERSONALES_FILE, new TypeReference<List<Cuenta>>() {});
        List<CuentaCompartida> compartidas = jsonService.cargar(COMPARTIDAS_FILE, new TypeReference<List<CuentaCompartida>>() {});

        cuentas = Stream.concat(personales.stream(), compartidas.stream())
                .collect(Collectors.toCollection(ArrayList::new));

        if (cuentas.isEmpty()) {
            crearCuentaPersonalDefecto();
        }
    }

    private void crearCuentaPersonalDefecto() {
        Cuenta personal = new Cuenta("Personal");
        personal.addCategoria(new Categoria("Alimentación"));
        personal.addCategoria(new Categoria("Transporte"));
        personal.addCategoria(new Categoria("Vivienda"));
        personal.addCategoria(new Categoria("Ocio"));
        personal.addCategoria(new Categoria("Salud"));
        personal.addCategoria(new Categoria("Otros"));
        cuentas.add(personal);
        guardarDatos();
    }

    @Override
    public void addCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
        guardarDatos();
    }

    @Override
    public void updateCuenta(Cuenta cuenta) {
        for (int i = 0; i < cuentas.size(); i++) {
            if (Objects.equals(cuentas.get(i).getId(), cuenta.getId())) {
                cuentas.set(i, cuenta);
                guardarDatos();
                return;
            }
        }
    }

    @Override
    public void deleteCuenta(UUID id) {
        cuentas.removeIf(c -> Objects.equals(c.getId(), id));
        guardarDatos();
    }

    @Override
    public Optional<Cuenta> getCuenta(UUID id) {
        return cuentas.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
    }

    @Override
    public List<Cuenta> getAllCuentas() {
        return new ArrayList<>(cuentas);
    }

    private void guardarDatos() {
        List<Cuenta> personales = cuentas.stream()
                .filter(c -> !c.isCompartida())
                .collect(Collectors.toList());

        List<CuentaCompartida> compartidas = cuentas.stream()
                .filter(CuentaCompartida.class::isInstance)
                .map(CuentaCompartida.class::cast)
                .collect(Collectors.toList());

        jsonService.guardar(PERSONALES_FILE, personales);
        jsonService.guardar(COMPARTIDAS_FILE, compartidas);
    }
}
