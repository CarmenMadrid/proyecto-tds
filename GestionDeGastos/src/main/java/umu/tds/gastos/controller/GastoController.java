// package umu.tds.gastos.controller;

// import com.google.common.base.Preconditions;
// import umu.tds.gastos.domain.core.Categoria;
// import umu.tds.gastos.domain.core.Gasto;
// import umu.tds.gastos.persistence.GastoRepository;

// import java.time.LocalDate;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// public class GastoController {

//     private final GastoRepository gastoRepository;

//     public GastoController(GastoRepository gastoRepository) {
//         this.gastoRepository = gastoRepository;
//     }

//     public Gasto registrarGasto(double cantidad, LocalDate fecha, String nombreCategoria) {
//         Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor que 0");
//         Preconditions.checkNotNull(fecha, "La fecha no puede ser nula");
//         Preconditions.checkNotNull(nombreCategoria, "La categoría no puede ser nula");
//         Preconditions.checkArgument(!nombreCategoria.trim().isEmpty(), "La categoría no puede estar vacía");

//         Categoria categoria = obtenerOCrearCategoria(nombreCategoria.trim());
//         Gasto gasto = new Gasto(cantidad, fecha, categoria);
//         gastoRepository.addGasto(gasto);
//         return gasto;
//     }

//     public void editarGasto(UUID id, double cantidad, LocalDate fecha, String nombreCategoria) {
//         Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor que 0");
//         Preconditions.checkNotNull(fecha, "La fecha no puede ser nula");

//         Gasto gasto = gastoRepository.getGasto(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado: " + id));

//         Categoria categoria = null;
//         if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
//             categoria = obtenerOCrearCategoria(nombreCategoria.trim());
//         }

//         gasto.setCantidad(cantidad);
//         gasto.setFecha(fecha);
//         if (categoria != null) {
//             gasto.setCategoria(categoria);
//         }
//         gastoRepository.updateGasto(gasto);
//     }

//     public void eliminarGasto(UUID id) {
//         Preconditions.checkNotNull(id, "El id no puede ser nulo");
//         gastoRepository.deleteGasto(id);
//     }

//     public Optional<Gasto> obtenerGasto(UUID id) {
//         return gastoRepository.getGasto(id);
//     }

//     public List<Gasto> obtenerTodosLosGastos() {
//         return gastoRepository.getAllGastos();
//     }

//     public List<Categoria> obtenerCategorias() {
//         return gastoRepository.getAllCategorias();
//     }

//     public Categoria crearCategoria(String nombre) {
//         Preconditions.checkNotNull(nombre, "El nombre no puede ser nulo");
//         Preconditions.checkArgument(!nombre.trim().isEmpty(), "El nombre no puede estar vacío");

//         String nombreTrim = nombre.trim();
//         Optional<Categoria> existente = gastoRepository.getCategoria(nombreTrim);
//         if (existente.isPresent()) {
//             return existente.get();
//         }
//         Categoria nueva = new Categoria(nombreTrim);
//         gastoRepository.addCategoria(nueva);
//         return nueva;
//     }

//     private Categoria obtenerOCrearCategoria(String nombre) {
//         return gastoRepository.getCategoria(nombre)
//                 .orElseGet(() -> {
//                     Categoria nueva = new Categoria(nombre);
//                     gastoRepository.addCategoria(nueva);
//                     return nueva;
//                 });
//     }
// }
