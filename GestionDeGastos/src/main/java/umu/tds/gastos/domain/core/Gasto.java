package umu.tds.gastos.domain.core;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Objects;
import java.util.UUID;

public class Gasto {

    private UUID id;
    private double cantidad;
    private LocalDate fecha;
    private Categoria categoria;
    private Persona pagador;

    public Gasto() {
    }

    public Gasto(double cantidad, LocalDate fecha, Categoria categoria) {
        this.id = UUID.randomUUID();
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Gasto(double cantidad, LocalDate fecha, Categoria categoria, Persona pagador) {
        this(cantidad, fecha, categoria);
        this.pagador = pagador;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Persona getPagador() {
        return pagador;
    }

    public void setPagador(Persona pagador) {
        this.pagador = pagador;
    }

    public int getAno() {
		return getFecha().getYear();
	}

	public int getMes() {
		return getFecha().getMonthValue();
	}

	public int getSemana() {
		return getFecha().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
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
		return "Gasto [cantidad=" + cantidad + ", fecha=" + fecha + ", categoria=" + categoria + ", pagador=" + (pagador == null ? "null" : pagador) + ']';
	}

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
