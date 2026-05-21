package umu.tds.gastos.domain.core;

import java.util.Objects;

public class Categoria {

    private String nombre;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return nombre != null && categoria.nombre != null
                ? nombre.equalsIgnoreCase(categoria.nombre)
                : Objects.equals(nombre, categoria.nombre);
    }

    @Override
    public int hashCode() {
        return nombre != null ? Objects.hash(nombre.toLowerCase()) : 0;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
