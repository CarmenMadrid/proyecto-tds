package umu.tds.gastos.domain.core;
import umu.tds.gastos.domain.alertas.Alertas;
import java.time.LocalDate;

import com.google.common.base.Preconditions;

public class Notificacion {
    private String mensaje;
	private LocalDate fecha;
	private Alertas alerta;
	private String nombreCuenta;

    public Notificacion(String mensaje, LocalDate fecha, Alertas alerta, String nombreCuenta) {
        this.alerta = Preconditions.checkNotNull(alerta);
		this.nombreCuenta = Preconditions.checkNotNull(nombreCuenta);
        
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.alerta = alerta;
        this.nombreCuenta = nombreCuenta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Alertas getAlerta() {
        return alerta;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }
}
