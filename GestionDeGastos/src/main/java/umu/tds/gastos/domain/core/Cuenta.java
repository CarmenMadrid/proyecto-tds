package umu.tds.gastos.domain.core;

import umu.tds.gastos.domain.filtros.Filtro;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cuenta {
    private UUID id;
    private String nombre;
    private boolean compartida;
    private List<Gasto> gastos;
    private List<Categoria> categorias;

    public Cuenta() {
        this.id = UUID.randomUUID();
        this.gastos = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    public Cuenta(String nombre) {
        this();
        this.nombre = nombre;
    }

    // Gastos

    public Gasto agregarGasto(double cantidad, LocalDate fecha, Categoria categoria) {
        Gasto gasto = new Gasto(cantidad, fecha, categoria);
        gastos.add(gasto);
        return gasto;
    }

    public void borrarGasto(Gasto gasto) {
        gastos.remove(gasto);
    }

    public void modificarGasto(Gasto gasto, double cantidad, LocalDate fecha, Categoria categoria) {
        for (Gasto g : gastos) {
            if (Objects.equals(g.getId(), gasto.getId())) {
                g.setCantidad(cantidad);
                g.setFecha(fecha);
                g.setCategoria(categoria);
                return;
            }
        }
    }

    public Optional<Gasto> getGasto(UUID idGasto) {
        return gastos.stream().filter(g -> Objects.equals(g.getId(), idGasto)).findFirst();
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    // Categorías 

    public void addCategoria(Categoria categoria) {
        if (categorias.stream().noneMatch(c -> c.getNombre().equalsIgnoreCase(categoria.getNombre()))) {
            categorias.add(categoria);
        }
    }

    public void eliminarCategoria(String nombreCategoria) {
        Categoria cat = categorias.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + nombreCategoria));

        boolean enUso = gastos.stream()
                .anyMatch(g -> g.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria));
        if (enUso) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoría '" + cat.getNombre()
                    + "' porque está en uso por uno o más gastos");
        }

        categorias.remove(cat);
    }

    public Optional<Categoria> getCategoria(String nombre) {
        return categorias.stream().filter(c -> c.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    public List<Categoria> getCategorias() {
        return new ArrayList<>(categorias);
    }

    // Filtros

    public List<Gasto> filtrarGastos(Filtro filtro) {
        return gastos.stream()
                .filter(filtro::cumple)
                .toList();
    }

    @JsonIgnore
    public Map<Persona, Double> getSaldos() {
        return new HashMap<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCompartida() {
        return compartida;
    }

    public void setCompartida(boolean compartida) {
        this.compartida = compartida;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return Objects.equals(id, cuenta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
