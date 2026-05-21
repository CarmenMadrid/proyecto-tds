// package umu.tds.gastos.persistence.impl;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import umu.tds.gastos.domain.core.Categoria;
// import umu.tds.gastos.domain.core.Gasto;
// import umu.tds.gastos.persistence.GastoRepository;

// import java.io.File;
// import java.io.IOException;
// import java.util.*;

// public class GastoRepositoryImpl implements GastoRepository {

//     private static final File GASTOS_FILE = new File("data/gastos.json");
//     private static final File CATEGORIAS_FILE = new File("data/categorias.json");

//     private final ObjectMapper mapper;
//     private List<Gasto> gastos;
//     private List<Categoria> categorias;

//     public GastoRepositoryImpl() {
//         this.mapper = new ObjectMapper();
//         mapper.registerModule(new JavaTimeModule());
//         mapper.enable(SerializationFeature.INDENT_OUTPUT);

//         this.gastos = new ArrayList<>();
//         this.categorias = new ArrayList<>();

//         GASTOS_FILE.getParentFile().mkdirs();
//         CATEGORIAS_FILE.getParentFile().mkdirs();

//         cargarDatos();
//     }

//     private void cargarDatos() {
//         if (GASTOS_FILE.exists()) {
//             gastos = leerLista(GASTOS_FILE, new TypeReference<List<Gasto>>() {});
//         }
//         if (CATEGORIAS_FILE.exists()) {
//             categorias = leerLista(CATEGORIAS_FILE, new TypeReference<List<Categoria>>() {});
//         } else {
//             inicializarCategoriasPredefinidas();
//         }
//     }

//     private void inicializarCategoriasPredefinidas() {
//         categorias.add(new Categoria("Alimentación"));
//         categorias.add(new Categoria("Transporte"));
//         categorias.add(new Categoria("Vivienda"));
//         categorias.add(new Categoria("Ocio"));
//         categorias.add(new Categoria("Salud"));
//         categorias.add(new Categoria("Otros"));
//         guardarCategorias();
//     }

//     // --- Gastos ---

//     @Override
//     public void addGasto(Gasto gasto) {
//         gastos.add(gasto);
//         guardarGastos();
//     }

//     @Override
//     public void updateGasto(Gasto gasto) {
//         for (int i = 0; i < gastos.size(); i++) {
//             if (Objects.equals(gastos.get(i).getId(), gasto.getId())) {
//                 gastos.set(i, gasto);
//                 guardarGastos();
//                 return;
//             }
//         }
//     }

//     @Override
//     public void deleteGasto(UUID id) {
//         gastos.removeIf(g -> Objects.equals(g.getId(), id));
//         guardarGastos();
//     }

//     @Override
//     public Optional<Gasto> getGasto(UUID id) {
//         return gastos.stream().filter(g -> Objects.equals(g.getId(), id)).findFirst();
//     }

//     @Override
//     public List<Gasto> getAllGastos() {
//         return new ArrayList<>(gastos);
//     }

//     // --- Categorías ---

//     @Override
//     public void addCategoria(Categoria categoria) {
//         if (getCategoria(categoria.getNombre()).isPresent()) return;
//         categorias.add(categoria);
//         guardarCategorias();
//     }

//     @Override
//     public void deleteCategoria(String nombre) {
//         categorias.removeIf(c -> c.getNombre().equalsIgnoreCase(nombre));
//         guardarCategorias();
//     }

//     @Override
//     public Optional<Categoria> getCategoria(String nombre) {
//         return categorias.stream()
//                 .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
//                 .findFirst();
//     }

//     @Override
//     public List<Categoria> getAllCategorias() {
//         return new ArrayList<>(categorias);
//     }

//     // --- Persistencia ---

//     private void guardarGastos() {
//         escribirLista(GASTOS_FILE, gastos);
//     }

//     private void guardarCategorias() {
//         escribirLista(CATEGORIAS_FILE, categorias);
//     }

//     private <T> List<T> leerLista(File f, TypeReference<List<T>> type) {
//         try {
//             return mapper.readValue(f, type);
//         } catch (IOException e) {
//             return new ArrayList<>();
//         }
//     }

//     private <T> void escribirLista(File f, List<T> lista) {
//         try {
//             mapper.writeValue(f, lista);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
