package umu.tds.gastos.imports.lector;

import umu.tds.gastos.imports.dto.GastoCSV;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LectorCSV {

	private static final DateTimeFormatter FMT_FECHA =
            DateTimeFormatter.ofPattern("M/d/yyyy[ H:mm]", Locale.ENGLISH);

	public List<GastoCSV> leer(String archivo) throws IOException {
        List<GastoCSV> gastos = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(archivo), StandardCharsets.UTF_8)) {
            String cabecera = br.readLine();
            if (cabecera == null) return gastos;

            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                if (linea.isBlank()) continue;

                try {
                    GastoCSV g = parsearLinea(linea, numLinea);
                    if (g != null) {
                        gastos.add(g);
                    }
                } catch (Exception e) {
                    System.err.println("Línea " + numLinea + " ignorada: " + e.getMessage());
                }
            }
        }

        return gastos;
    }

    private GastoCSV parsearLinea(String linea, int numLinea) {
        String[] cols = linea.split(",");
        if (cols.length < 7) {
            throw new IllegalArgumentException("Se esperaban al menos 7 columnas, se encontraron " + cols.length);
        }

        String date = cols[0].trim();
        String cuentaNombre = cols[1].trim();
        String subcategory = cols[3].trim();
        String nombre = cols[4].trim();
        String payer = cols[5].trim();
        String amount = cols[6].trim();

        if (date.isEmpty() || amount.isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios vacíos");
        }

        LocalDate fecha = LocalDate.parse(date, FMT_FECHA);
        double importe = Double.parseDouble(amount);

        if (importe <= 0) {
            throw new IllegalArgumentException("El importe debe ser positivo: " + importe);
        }
        if (subcategory.isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }

        return new GastoCSV(fecha, importe, subcategory, payer, nombre, cuentaNombre);
    }
}
