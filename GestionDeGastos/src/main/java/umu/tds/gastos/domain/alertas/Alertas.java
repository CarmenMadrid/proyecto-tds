package umu.tds.gastos.domain.alertas;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import umu.tds.gastos.domain.alertas.patronEstrategias.*;

import umu.tds.gastos.domain.core.Categoria;
import umu.tds.gastos.domain.core.Gasto;

public class Alertas {
    private double limite;
	private Categoria categoria;
	private UUID id;
	private UUID idCuenta;
    LocalDate fechaCreacion;
    boolean mostrada = false;

    private EstrategiaTiempo tipo; 

    public Alertas() {
    }

    public Alertas(double limite, Categoria categoria, EstrategiaTiempo tipo) {
        this(limite, categoria, tipo, null);
    }

    public Alertas(double limite, Categoria categoria, EstrategiaTiempo tipo, UUID idCuenta) {
        this.limite = limite;
        this.categoria = categoria;
        this.id = UUID.randomUUID();
        this.idCuenta = idCuenta;
        this.fechaCreacion = LocalDate.now();
        this.tipo = tipo;
        this.mostrada = false;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(UUID idCuenta) {
        this.idCuenta = idCuenta;
    }

    public EstrategiaTiempo getTipo() {
        return tipo;
    }

    public void setTipo(EstrategiaTiempo tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    
	public int getAnoAlerta() {
		return fechaCreacion.getYear();
	}

	
	public int getMesAlerta() {
		return fechaCreacion.getMonthValue();
	}

	
	public int getSemanaAlerta() {
		return fechaCreacion.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
	}

    public List<Gasto> filtrarPorCategoria(List<Gasto> gastos) {
		if (this.categoria == null)
			return gastos;
		return gastos.stream().filter(g -> g.getCategoria() != null && g.getCategoria().equals(this.categoria))
				.collect(Collectors.toList());
	}
    
    public boolean superaLimite(List<Gasto> gastos) {
		List<Gasto> gastosCategoria = filtrarPorCategoria(gastos);
		double total = tipo.comprobar(this, gastosCategoria);
		return total > limite;
	}
    public boolean isMostrada() {
		return mostrada;
	}

	public void setMostrada(boolean m) {
		mostrada = m;
	}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Limite ");
        String[] parts = tipo.getNombreEstrategia().split(" ");
        sb.append(parts[parts.length - 1].toLowerCase()).append(" de ");
        sb.append(String.format("%.2f", getLimite())).append("€");
        if (getCategoria() != null) {
            sb.append(" en ").append(getCategoria().getNombre());
        }
        return sb.toString();
    }

    @JsonIgnore
	public String getMensaje() {
		String cat = (getCategoria() == null) ? "General" : getCategoria().getNombre();
		return "Has superado tu límite " + tipo.getNombreEstrategia().split(" ")[1] + " de " + getLimite()
				+ "€ en la categoría '" + cat + "'.";
	}
}
