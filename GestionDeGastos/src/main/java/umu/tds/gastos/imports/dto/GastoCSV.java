package umu.tds.gastos.imports.dto;

import java.time.LocalDate;

public class GastoCSV {

    private LocalDate fecha;
    private double importe;
    private String categoria;
    private String pagador;
    private String nombre;
    private String cuentaNombre;

    public GastoCSV(LocalDate fecha, double importe, String categoria) {
        this(fecha, importe, categoria, null, null, null);
    }

    public GastoCSV(LocalDate fecha, double importe, String categoria, String pagador) {
        this(fecha, importe, categoria, pagador, null, null);
    }

    public GastoCSV(LocalDate fecha, double importe, String categoria, String pagador, String nombre) {
        this(fecha, importe, categoria, pagador, nombre, null);
    }

    public GastoCSV(LocalDate fecha, double importe, String categoria, String pagador, String nombre, String cuentaNombre) {
        this.fecha = fecha;
        this.importe = importe;
        this.categoria = categoria;
        this.pagador = pagador;
        this.nombre = nombre;
        this.cuentaNombre = cuentaNombre;
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
    
    public String getNombre() {
        return nombre;
    }

    public String getCuentaNombre() {
        return cuentaNombre;
    }
}
