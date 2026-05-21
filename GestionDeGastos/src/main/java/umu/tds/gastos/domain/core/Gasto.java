package umu.tds.gastos.domain.core;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Gasto {

    private UUID id;
    private double cantidad;
    private LocalDate fecha;
    private String categoria;

    public Gasto(double cantidad, LocalDate fecha, String categoria) {
        this.id = UUID.randomUUID();
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gasto gasto = (Gasto) o;
        return id == gasto.id;
    }

    @Override
	public String toString() {
		return "Gasto (" + id + ") [cantidad=" + cantidad + ", fecha=" + fecha + ", categoria=" + categoria + ']';
	}

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
