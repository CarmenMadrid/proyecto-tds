package umu.tds.gastos.imports.dto;

import java.time.LocalDate;

public class GastoCSV {

    private LocalDate fecha;
    private double importe;
    private String categoria;
    private String pagador;

    public GastoCSV(LocalDate fecha, double importe, String categoria) {
        this(fecha, importe, categoria, null);
    }

    public GastoCSV(LocalDate fecha, double importe, String categoria, String pagador) {
        this.fecha = fecha;
        this.importe = importe;
        this.categoria = categoria;
        this.pagador = pagador;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getImporte() {
        return importe;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPagador() {
        return pagador;
    }
}
