package umu.tds.gastos.domain.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CuentaCompartida extends Cuenta {

    public enum TipoReparto {
        EQUITATIVO, PERSONALIZADO
    }

    private List<Persona> personas;
    private TipoReparto tipoReparto;
    private Map<Persona, Double> porcentajes;
    private Map<Persona, Double> saldos;

    public CuentaCompartida() {
        super();
        setCompartida(true);
        this.personas = new ArrayList<>();
        this.porcentajes = new HashMap<>();
        this.saldos = new HashMap<>();
        this.tipoReparto = TipoReparto.EQUITATIVO;
    }

    public CuentaCompartida(String nombre, List<Persona> personas, TipoReparto tipo) {
        super(nombre);
        setCompartida(true);
        Preconditions.checkNotNull(tipo, "Debes elegir el tipo de reparto");
        Preconditions.checkArgument(personas != null && !personas.isEmpty(),
                "Debe haber al menos una persona en la cuenta");
        this.personas = personas;
        this.porcentajes = new HashMap<>();
        this.saldos = new HashMap<>();
        this.tipoReparto = tipo;
    }

    // Gastos

    @Override
    public Gasto agregarGasto(double cantidad, LocalDate fecha, Categoria categoria) {
        throw new UnsupportedOperationException(
                "Se debe especificar el pagador en cuentas compartidas");
    }

    public Gasto agregarGasto(double cantidad, LocalDate fecha, Categoria categoria, Persona pagador) {
        Preconditions.checkArgument(personas.contains(pagador), "El pagador no es miembro de esta cuenta");
        Gasto gasto = new Gasto(cantidad, fecha, categoria, pagador);
        getGastos().add(gasto);
        calcularSaldos();
        return gasto;
    }

    @Override
    public void borrarGasto(Gasto gasto) {
        super.borrarGasto(gasto);
        calcularSaldos();
    }

    @Override
    public void modificarGasto(Gasto gasto, double cantidad, LocalDate fecha, Categoria categoria) {
        super.modificarGasto(gasto, cantidad, fecha, categoria);
        calcularSaldos();
    }

    // Saldos

    public void calcularSaldos() {
        if (personas == null || personas.isEmpty()) return;

        saldos.clear();
        for (Persona p : personas) {
            saldos.put(p, 0.0);
        }

        double totalGastado = getGastos().stream().mapToDouble(Gasto::getCantidad).sum();
        if (totalGastado == 0) return;

        Map<Persona, Double> pagadoPorCadaUno = getGastos().stream()
                .filter(g -> g.getPagador() != null)
                .collect(Collectors.groupingBy(Gasto::getPagador,
                        Collectors.summingDouble(Gasto::getCantidad)));

        if (tipoReparto == TipoReparto.EQUITATIVO) {
            double cuota = totalGastado / personas.size();
            for (Persona p : personas) {
                saldos.put(p, pagadoPorCadaUno.getOrDefault(p, 0.0) - cuota);
            }
        } else {
            for (Persona p : personas) {
                double pagado = pagadoPorCadaUno.getOrDefault(p, 0.0);
                double porcentaje = porcentajes.getOrDefault(p, 0.0);
                saldos.put(p, pagado - totalGastado * (porcentaje / 100.0));
            }
        }
    }

    @JsonProperty("saldos")
    public Map<Persona, Double> getSaldos() {
        if (saldos == null || saldos.isEmpty()) {
            calcularSaldos();
        }
        return saldos != null ? new HashMap<>(saldos) : new HashMap<>();
    }

    public double getSaldoDe(Persona miembro) {
        return getSaldos().getOrDefault(miembro, 0.0);
    }


    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public TipoReparto getTipoReparto() {
        return tipoReparto;
    }

    public void setTipoReparto(TipoReparto tipoReparto) {
        this.tipoReparto = tipoReparto;
    }

    public Map<Persona, Double> getPorcentajes() {
        return porcentajes;
    }

    public void setPorcentajes(Map<Persona, Double> porcentajes) {
        if (porcentajes == null || porcentajes.isEmpty()) {
            this.porcentajes = new HashMap<>();
            return;
        }
        double total = porcentajes.values().stream().mapToDouble(Double::doubleValue).sum();
        Preconditions.checkArgument(Math.abs(total - 100.0) < 0.001,
                "Los porcentajes deben sumar 100. Suma actual: " + total);
        this.porcentajes = porcentajes;
    }

    public void setSaldos(Map<Persona, Double> saldos) {
        this.saldos = saldos;
    }
}
