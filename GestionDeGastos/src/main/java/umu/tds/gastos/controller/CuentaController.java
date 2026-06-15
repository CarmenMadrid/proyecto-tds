package umu.tds.gastos.controller;

import com.google.common.base.Preconditions;

import umu.tds.gastos.domain.alertas.*;
import umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaTiempo;
import umu.tds.gastos.domain.core.*;
import umu.tds.gastos.domain.filtros.*;
import umu.tds.gastos.persistence.AlertasRepository;
import umu.tds.gastos.imports.ImportadorFactory;
import umu.tds.gastos.imports.ImportadorGastos;
import umu.tds.gastos.persistence.CuentaRepository;
import umu.tds.gastos.persistence.NotificacionesRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class CuentaController {

    private final CuentaRepository cuentaRepository;
    private final AlertasRepository alertaRepository;
	private final NotificacionesRepository notificacionesRepository;

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
    /*******************************************************************************************************************/
    public Gasto registrarGasto(UUID idCuenta, String nombre, double cantidad, LocalDate fecha, String nombreCategoria, Persona pagador) {
        Gasto gasto = registrarGasto(idCuenta, cantidad, fecha, nombreCategoria, pagador);
        gasto.setNombre(nombre);
        return gasto;
    }
    
    /*******************************************************************************************************************/

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
        verificarAlertas();
        return gasto;
    }
    
    
    public void editarGasto(UUID idCuenta, UUID idGasto, String nombre, double cantidad, LocalDate fecha, String nombreCategoria) {
        editarGasto(idCuenta, idGasto, cantidad, fecha, nombreCategoria);
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        Gasto gasto = cuenta.getGasto(idGasto)
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));
        if (nombre != null && !nombre.trim().isEmpty()) {
            gasto.setNombre(nombre);
        }
        cuentaRepository.updateCuenta(cuenta);
        verificarAlertas();
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
        verificarAlertas();
    }

    public void editarGastoConPagador(UUID idCuenta, UUID idGasto, double cantidad, LocalDate fecha, String nombreCategoria, Persona pagador) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (!(cuenta instanceof CuentaCompartida cc)) {
            throw new IllegalArgumentException("Solo cuentas compartidas tienen pagador");
        }
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
        cc.modificarGasto(gasto, cantidad, fecha, categoria != null ? categoria : gasto.getCategoria());
        if (pagador != null) {
            Preconditions.checkArgument(cc.getPersonas().contains(pagador), "El pagador no es miembro de esta cuenta");
            gasto.setPagador(pagador);
            cc.calcularSaldos();
        }
        cuentaRepository.updateCuenta(cuenta);
        verificarAlertas();
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

    public List<Categoria> obtenerTodasLasCategorias() {
        return cuentaRepository.getAllCuentas().stream()
                .flatMap(c -> c.getCategorias().stream())
                .distinct()
                .collect(Collectors.toList());
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
    
    
    /* COMENTARLO
    public void eliminarCategoria(UUID idCuenta, String nombreCategoria) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        cuenta.eliminarCategoria(nombreCategoria);
        cuentaRepository.updateCuenta(cuenta);
    }*/

    public void eliminarCategoria(UUID idCuenta, String nombreCategoria) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        cuenta.eliminarCategoria(nombreCategoria);
        cuentaRepository.updateCuenta(cuenta);
    }

    public boolean categoriaEnUso(UUID idCuenta, String nombreCategoria) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return cuenta.getGastos().stream()
                .anyMatch(g -> g.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria));
    }

    // Saldos y reparto (cuentas compartidas)

    public Map<Persona, Double> obtenerSaldos(UUID idCuenta) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (!cuenta.isCompartida()) {
            throw new UnsupportedOperationException("La cuenta personal no tiene saldos");
        }
        return cuenta.getSaldos();
    }

    public CuentaCompartida obtenerCuentaCompartida(UUID idCuenta) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (!cuenta.isCompartida()) {
            throw new UnsupportedOperationException("La cuenta no es compartida");
        }
        return (CuentaCompartida) cuenta;
    }

    public Map<String, Double> obtenerPorcentajes(UUID idCuenta) {
        CuentaCompartida cc = obtenerCuentaCompartida(idCuenta);
        return cc.getPorcentajes().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().getNombre(),
                        Map.Entry::getValue));
    }

    public void configurarPorcentajes(UUID idCuenta, Map<String, Double> porcentajesPorNombre) {
        CuentaCompartida cc = obtenerCuentaCompartida(idCuenta);
        Map<Persona, Double> porcentajes = new HashMap<>();
        for (Map.Entry<String, Double> entry : porcentajesPorNombre.entrySet()) {
            Persona p = cc.getPersonas().stream()
                    .filter(per -> per.getNombre().equalsIgnoreCase(entry.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Persona no encontrada: " + entry.getKey()));
            porcentajes.put(p, entry.getValue());
        }
        cc.setPorcentajes(porcentajes);
        cuentaRepository.updateCuenta(cc);
    }

    // Filtros

    public List<Gasto> filtrarGastos(UUID idCuenta, Filtro filtro) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return cuenta.filtrarGastos(filtro);
    }
    

    public List<Gasto> filtrarGastos(UUID idCuenta, List<Categoria> categorias, LocalDate fechaInicio, LocalDate fechaFin, List<Month> meses) {
        return filtrarGastos(idCuenta, categorias, fechaInicio, fechaFin, meses, null);
    }

    public List<Gasto> filtrarGastos(UUID idCuenta, List<Categoria> categorias, LocalDate fechaInicio, LocalDate fechaFin, List<Month> meses, Persona pagador) {
        List<Filtro> listaFiltros = new ArrayList<>();

		Filtro filtroCategorias = new FiltroCategorias(new ArrayList<>(categorias));
		Filtro filtroFechas = new FiltroFechas(fechaInicio, fechaFin);
		Filtro filtroMeses = new FiltroMeses(meses);
		Filtro filtroPersona = new FiltroPersona(pagador);

	    listaFiltros.add(filtroCategorias);
        listaFiltros.add(filtroFechas);
        listaFiltros.add(filtroMeses);
        listaFiltros.add(filtroPersona);
        Filtro filtroMultiple = new FiltroMultiple(listaFiltros);
	    return filtrarGastos(idCuenta, filtroMultiple);
	}

    // Importar

    public int importarGastos(UUID idCuenta, String archivo, String formato) throws IOException {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        ImportadorGastos importador = ImportadorFactory.crearImportador(formato);
        List<Gasto> importados = importador.importar(archivo, cuenta);

        int count = procesarGastosImportados(cuenta, importados);
        if (count > 0) {
            cuentaRepository.updateCuenta(cuenta);
            verificarAlertas();
        }
        return count;
    }

    private int procesarGastosImportados(Cuenta cuenta, List<Gasto> importados) {
        int count = 0;
        for (Gasto g : importados) {
            Categoria cat = cuenta.getCategoria(g.getCategoria().getNombre())
                    .orElseGet(() -> {
                        Categoria nueva = new Categoria(g.getCategoria().getNombre());
                        cuenta.addCategoria(nueva);
                        return nueva;
                    });

            Gasto nuevo;
            if (cuenta instanceof CuentaCompartida comp) {
                Persona pagador = g.getPagador();
                if (pagador == null || !comp.getPersonas().contains(pagador)) {
                    pagador = comp.getPersonas().get(0);
                }
                nuevo = comp.agregarGasto(g.getCantidad(), g.getFecha(), cat, pagador);
            } else {
                nuevo = cuenta.agregarGasto(g.getCantidad(), g.getFecha(), cat);
            }
            if (!g.getNombre().isBlank()) {
                nuevo.setNombre(g.getNombre());
            }
            count++;
        }
        return count;
    }

    // Datos para gráficas

    public Map<String, Double> obtenerGastosPorCategoria(UUID idCuenta) {
        return obtenerGastosPorCategoria(idCuenta, null);
    }

    public Map<String, Double> obtenerGastosPorCategoria(UUID idCuenta, Filtro filtro) {
        Cuenta cuenta = cuentaRepository.getCuenta(idCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        List<Gasto> gastos = (filtro != null) ? cuenta.filtrarGastos(filtro) : cuenta.getGastos();
        return gastos.stream()
                .collect(Collectors.groupingBy(
                        g -> g.getCategoria().getNombre(),
                        Collectors.summingDouble(Gasto::getCantidad)));
    }



	private EstrategiaTiempo instanciarEstrategia(String nombreAlerta) {
		try {
			String rutaCompleta = "umu.tds.gastos.domain.alertas.patronEstrategias.Estrategia" + nombreAlerta;
			return (EstrategiaTiempo) Class.forName(rutaCompleta).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creando alerta: " + nombreAlerta, e);
		}
	}

    public void crearAlerta(double limite, Categoria categoria, String nombreClaseAlerta, UUID idCuenta)
			throws RuntimeException, IllegalArgumentException {
		Preconditions.checkArgument(limite > 0, "El límite debe ser positivo.");

		EstrategiaTiempo tipoAlerta = instanciarEstrategia(nombreClaseAlerta);
        Preconditions.checkArgument(alertaRepository.buscarAlerta(limite, categoria, nombreClaseAlerta) == null,
                "Ya existe la alerta");

        Alertas nuevaAlerta = new Alertas(limite, categoria, tipoAlerta, idCuenta);
        alertaRepository.addAlerta(nuevaAlerta);

        verificarAlertas();
    }

    public void editarAlerta(UUID idAlerta, double limite, Categoria categoria, String nombreClaseAlerta) {
        Preconditions.checkArgument(limite > 0, "El límite debe ser positivo.");
        Alertas alerta = alertaRepository.getAlerta(idAlerta)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada"));
        EstrategiaTiempo tipoAlerta = instanciarEstrategia(nombreClaseAlerta);
        alerta.setLimite(limite);
        alerta.setCategoria(categoria);
        alerta.setTipo(tipoAlerta);
        alertaRepository.updateAlerta(alerta);
        verificarAlertas();
    }

    public void borrarAlerta(Alertas alerta) {
		Preconditions.checkNotNull(alerta, "Selecciona una alerta para eliminarla.");

		notificacionesRepository.borrarPorAlerta(alerta);

		alertaRepository.deleteAlerta(alerta.getId());
	}

    
    private void verificarAlertas() {
		for (Alertas alerta : alertaRepository.getAllAlertas()) {
			List<Gasto> gastosCuenta;
			UUID idCuenta = alerta.getIdCuenta();
			String nombreCuenta = "Global";
			if (idCuenta != null) {
				Cuenta cuenta = cuentaRepository.getCuenta(idCuenta).orElse(null);
				if (cuenta == null) continue;
				gastosCuenta = cuenta.getGastos();
				nombreCuenta = cuenta.getNombre();
			} else {
				gastosCuenta = cuentaRepository.getAllCuentas().stream()
						.flatMap(c -> c.getGastos().stream())
						.collect(Collectors.toList());
			}

			if (alerta.superaLimite(gastosCuenta)) {

				if (!alerta.isMostrada()) {
					alerta.setMostrada(true);
					alertaRepository.updateAlerta(alerta);

					Notificacion notif = new Notificacion(alerta, nombreCuenta);
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

	public List<Alertas> getAlertasByCuenta(UUID idCuenta) {
		return alertaRepository.getAlertasByCuenta(idCuenta);
	}

	public List<Notificacion> getNotificaciones() {
		return notificacionesRepository.getAllNotificaciones();
	}

	public List<String> getNombresPeriodosDisponibles() {
		return List.of("Semanal", "Mensual");
	}
}
