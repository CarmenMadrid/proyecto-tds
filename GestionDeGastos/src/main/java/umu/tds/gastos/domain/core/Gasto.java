package umu.tds.gastos.domain.core;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Gasto {

    private UUID id;
    private double cantidad;
    private LocalDate fecha;
    private Categoria categoria;

    public Gasto() {
        if (this.id == null) {
			this.id = UUID.randomUUID();
		}
    }

    public Gasto(double cantidad, LocalDate fecha, Categoria categoria) {
        this.id = UUID.randomUUID();
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }
    
    public UUID getId() {
        if (this.id == null) {
			this.id = UUID.randomUUID();
		}
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gasto gasto = (Gasto) o;
        return Objects.equals(id, gasto.id);
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
