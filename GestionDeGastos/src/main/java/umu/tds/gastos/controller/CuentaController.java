package umu.tds.gastos.controller;

import com.google.common.base.Preconditions;

import umu.tds.gastos.domain.alertas.*;
import umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaTiempo;
import umu.tds.gastos.domain.core.*;
import umu.tds.gastos.domain.filtros.*;
import umu.tds.gastos.persistence.AlertasRepository;
import umu.tds.gastos.persistence.CuentaRepository;
import umu.tds.gastos.persistence.NotificacionesRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CuentaController {

    private final CuentaRepository cuentaRepository;
    private AlertasRepository alertaRepository;
	private NotificacionesRepository notificacionesRepository;

    public CuentaController(CuentaRepository cuentaRepository, AlertasRepository alertaRepository, NotificacionesRepository notificacionesRepository) {
        this.cuentaRepository = cuentaRepository;
        this.alertaRepository = alertaRepository;
        this.notificacionesRepository = notificacionesRepository;
    }

    // Cuentas

    public Cuenta crearCuentaPersonal(String nombre) {
        Preconditions.checkNotNull(nombre, "El nombre no puede ser nulo");
        Cuenta cuenta = new Cuenta(nombre);
        cuentaRepository.addCuenta(cuenta);
        return cuenta;
    }

    public Cuenta crearCuentaCompartida(String nombre, List<Persona> personas, CuentaCompartida.TipoReparto tipoReparto) {
        Preconditions.checkNotNull(nombre, "El nombre no puede ser nulo");
        CuentaCompartida cuenta = new CuentaCompartida(nombre, personas, tipoReparto);
        cuentaRepository.addCuenta(cuenta);
        return cuenta;
    }

    public void eliminarCuenta(UUID id) {
        cuentaRepository.deleteCuenta(id);
    }

    public List<Cuenta> obtenerCuentas() {
        return cuentaRepository.getAllCuentas();
    }

    public Optional<Cuenta> obtenerCuenta(UUID id) {
        return cuentaRepository.getCuenta(id);
    }

    public Cuenta obtenerCuentaPersonal() {
        return cuentaRepository.getAllCuentas().stream()
                .filter(c -> !c.isCompartida())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No existe cuenta personal"));
    }

    // Gastos 

    public Gasto registrarGasto(UUID idCuenta, double cantidad, LocalDate fecha, String nombreCategoria) {
        return registrarGasto(idCuenta, cantidad, fecha, nombreCategoria, null);
    }

    public Gasto registrarGasto(UUID idCuenta, double cantidad, LocalDate fecha, String nombreCategoria, Persona pagador) {
        Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor que 0");
        Preconditions.checkNotNull(fecha, "La fecha no puede ser nula");
        Preconditions.checkNotNull(nombreCategoria, "La categoría no puede ser nula");

        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        Categoria categoria = cuenta.getCategoria(nombreCategoria)
                .orElseGet(() -> {
                    Categoria nueva = new Categoria(nombreCategoria);
                    cuenta.addCategoria(nueva);
                    return nueva;
                });

        Gasto gasto;
        if (cuenta instanceof CuentaCompartida comp) {
            gasto = comp.agregarGasto(cantidad, fecha, categoria, pagador);
        } else {
            gasto = cuenta.agregarGasto(cantidad, fecha, categoria);
        }
        cuentaRepository.updateCuenta(cuenta);
        return gasto;
    }

    public void editarGasto(UUID idCuenta, UUID idGasto, double cantidad, LocalDate fecha, String nombreCategoria) {
        Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor que 0");
        Preconditions.checkNotNull(fecha, "La fecha no puede ser nula");

        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        Categoria categoria = null;
        if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
            String cat = nombreCategoria.trim();
            categoria = cuenta.getCategoria(cat)
                    .orElseGet(() -> {
                        Categoria nueva = new Categoria(cat);
                        cuenta.addCategoria(nueva);
                        return nueva;
                    });
        }

        Gasto gasto = cuenta.getGasto(idGasto)
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));
        cuenta.modificarGasto(gasto, cantidad, fecha, categoria != null ? categoria : gasto.getCategoria());

        cuentaRepository.updateCuenta(cuenta);
    }

    public void eliminarGasto(UUID idCuenta, UUID idGasto) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        Gasto gasto = cuenta.getGasto(idGasto)
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));
        cuenta.borrarGasto(gasto);
        cuentaRepository.updateCuenta(cuenta);
    }

    public List<Gasto> obtenerGastos(UUID idCuenta) {
        return cuentaRepository.getCuenta(idCuenta)
                .map(Cuenta::getGastos)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
    }

    // Categorías 

    public List<Categoria> obtenerCategorias(UUID idCuenta) {
        return cuentaRepository.getCuenta(idCuenta)
                .map(Cuenta::getCategorias)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
    }

    public Categoria crearCategoria(UUID idCuenta, String nombre) {
        Preconditions.checkNotNull(nombre, "El nombre no puede ser nulo");
        Preconditions.checkArgument(!nombre.trim().isEmpty(), "El nombre no puede estar vacío");

        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        String nombreTrim = nombre.trim();
        Optional<Categoria> existente = cuenta.getCategoria(nombreTrim);
        if (existente.isPresent()) {
            return existente.get();
        }
        Categoria nueva = new Categoria(nombreTrim);
        cuenta.addCategoria(nueva);
        cuentaRepository.updateCuenta(cuenta);
        return nueva;
    }

    // Saldos (cuentas compartidas) 

    public Map<Persona, Double> obtenerSaldos(UUID idCuenta) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (!cuenta.isCompartida()) {
            throw new UnsupportedOperationException("La cuenta personal no tiene saldos");
        }
        return cuenta.getSaldos();
    }

    // Filtros

    public List<Gasto> filtrarGastos(UUID idCuenta, Filtro filtro) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return cuenta.filtrarGastos(filtro);
    }
    
    public List<Gasto> filtrarGastos(UUID idCuenta, List<Categoria> categorias, LocalDate fechaInicio, LocalDate fechaFin,List<Month> meses) {

		List<Filtro> listaFiltros = new ArrayList<>();

		Filtro filtroCategorias = new FiltroCategorias(new ArrayList<>(categorias));
		Filtro filtroFechas = new FiltroFechas(fechaInicio, fechaFin);
		Filtro filtroMeses = new FiltroMeses(meses);

		listaFiltros.add(filtroCategorias);
        listaFiltros.add(filtroFechas); 
        listaFiltros.add(filtroMeses);
        Filtro filtroMultiple = new FiltroMultiple(listaFiltros);
		return filtrarGastos(idCuenta, filtroMultiple);
	}	
    //Gestion de alertas y notificaciones


	private EstrategiaTiempo instanciarEstrategia(String nombreAlerta) {
		try {
			String rutaCompleta = "umu.tds.gastos.domain.alertas.patronEstrategias.Estrategia" + nombreAlerta;
			return (EstrategiaTiempo) Class.forName(rutaCompleta).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creando alerta: " + nombreAlerta, e);
		}
	}

    public void crearAlerta(double limite, Categoria categoria, String nombreClaseAlerta)
			throws RuntimeException, IllegalArgumentException {
		// Si categoria es null es una alerta general
		Preconditions.checkArgument(limite > 0, "El límite debe ser positivo.");

		EstrategiaTiempo tipoAlerta = instanciarEstrategia(nombreClaseAlerta);
        Preconditions.checkArgument(alertaRepository.buscarAlerta(limite, categoria, nombreClaseAlerta) == null,
                "Ya existe la alerta");

        Alertas nuevaAlerta = new Alertas(limite, categoria, tipoAlerta);
        alertaRepository.addAlerta(nuevaAlerta);

        verificarAlertas();
    }

    public void borrarAlerta(Alertas alerta) {
		Preconditions.checkNotNull(alerta, "Selecciona una alerta para eliminarla.");

		// Si borro una alerta, borro todas sus notificaciones
		notificacionesRepository.borrarPorAlerta(alerta);

		alertaRepository.deleteAlerta(alerta.getId());
	}

    
    private void verificarAlertas() {
		Preconditions.checkNotNull(obtenerCuentaPersonal(), "Cuenta personal es null");

		// Obtener lista plana de TODOS los gastos de la cuenta personal
		List<Gasto> gastosPersonales = obtenerGastos(obtenerCuentaPersonal().getId());

		// Comprobar cada alerta
		for (Alertas alerta : alertaRepository.getAllAlertas()) {

			if (alerta.superaLimite(gastosPersonales)) {

				if (!alerta.isMostrada()) {
					alerta.setMostrada(true);
					alertaRepository.updateAlerta(alerta);

					// Crear notificación
					Notificacion notif = new Notificacion(alerta,"Cuenta Personal");
					notificacionesRepository.addNotificacion(notif);
				}

			} else if (alerta.isMostrada()) {
				
				notificacionesRepository.borrarPorAlerta(alerta);

				alerta.setMostrada(false);
				alertaRepository.updateAlerta(alerta);
			}

		}

	}

	public List<Alertas> getAlertas() {
		return alertaRepository.getAllAlertas();
	}

	public List<Notificacion> getNotificaciones() {
		return notificacionesRepository.getAllNotificaciones();
	}

	public List<String> getNombresPeriodosDisponibles() {
		return List.of("Semanal", "Mensual");
	}
}
