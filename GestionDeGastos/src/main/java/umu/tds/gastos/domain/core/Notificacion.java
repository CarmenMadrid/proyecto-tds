package umu.tds.gastos.domain.core;
import umu.tds.gastos.domain.alertas.Alertas;
import java.time.LocalDate;

import com.google.common.base.Preconditions;

public class Notificacion {
    private String mensaje;
	private LocalDate fecha;
	private Alertas alerta;
	private String nombreCuenta;

    public Notificacion() { 
    }

    public Notificacion(Alertas alerta, String nombreCuenta) {
        this.alerta = Preconditions.checkNotNull(alerta);
		this.nombreCuenta = Preconditions.checkNotNull(nombreCuenta);
        
        this.mensaje = alerta.getMensaje();
        this.fecha = LocalDate.now();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(fecha).append("] ");
        if (nombreCuenta != null && !nombreCuenta.isEmpty()) {
            sb.append(nombreCuenta).append(": ");
        }
        if (alerta != null && alerta.getTipo() != null) {
            String[] parts = alerta.getTipo().getNombreEstrategia().split(" ");
            sb.append("Has superado tu limite ").append(parts[parts.length - 1].toLowerCase()).append(" de ");
            sb.append(String.format("%.2f", alerta.getLimite())).append("\u20AC");
            if (alerta.getCategoria() != null) {
                sb.append(" en ").append(alerta.getCategoria().getNombre());
            }
        } else {
            sb.append(mensaje);
        }
        return sb.toString();
    }
}
