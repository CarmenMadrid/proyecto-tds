package umu.tds.gastos.cli;

import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.*;
import umu.tds.gastos.domain.filtros.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLI {

    private static class CancelarOperacion extends RuntimeException {
        CancelarOperacion() { super("Operación cancelada."); }
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final CuentaController controller;
    private final Scanner scanner;

    public CLI(CuentaController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        while (true) {
            mostrarMenu();
            String input = scanner.nextLine().trim();
            try {
                switch (Integer.parseInt(input)) {
                    case 1 -> { Cuenta c = seleccionarCuentaPersonal(); if (c != null) verGastos(c); }
                    case 2 -> anadirGastoPersonal();
                    case 3 -> editarGastoPersonal();
                    case 4 -> { Cuenta c = seleccionarCuentaPersonal(); if (c != null) eliminarGasto(c); }
                    case 5 -> crearCuentaPersonal();
                    case 6 -> eliminarCuentaPersonal();
                    case 7 -> menuCuentasCompartidas();
                    case 8 -> anadirGastoCompartido();
                    case 9 -> editarGastoCompartido();
                    case 10 -> eliminarGastoCompartido();
                    case 11 -> crearCuentaCompartida();
                    case 12 -> eliminarCuentaCompartida();
                    case 13 -> filtrarGastos();
                    case 14 -> { System.out.println("\n¡Hasta luego!\n"); return; }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un número.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println(" ======================================");
        System.out.println("     GESTIÓN DE GASTOS");
        System.out.println(" ======================================");
        System.out.println(" --- Cuentas personales ---");
        System.out.println(" 1. Ver cuentas personales");
        System.out.println(" 2. Añadir gasto personal");
        System.out.println(" 3. Editar gasto personal");
        System.out.println(" 4. Eliminar gasto personal");
        System.out.println(" 5. Crear cuenta personal");
        System.out.println(" 6. Eliminar cuenta personal");
        System.out.println(" --- Cuentas compartidas ---");
        System.out.println(" 7. Ver cuentas compartidas");
        System.out.println(" 8. Añadir gasto compartido");
        System.out.println(" 9. Editar gasto compartido");
        System.out.println(" 10. Eliminar gasto compartido");
        System.out.println(" 11. Crear cuenta compartida");
        System.out.println(" 12. Eliminar cuenta compartida");
        System.out.println(" --- Generales ---");
        System.out.println(" 13. Filtrar gastos");
        System.out.println(" 14. Salir");
        System.out.println("---------------------------------------");
        System.out.print("Seleccione: ");
    }

    // ========================================================================
    // Operaciones cuenta personal
    // ========================================================================

    private void crearCuentaPersonal() {
        System.out.println("\n--- Crear cuenta personal ---\n");
        try {
            String nombre = leerLinea("Nombre de la cuenta (o 'salir'): ");
            controller.crearCuentaPersonal(nombre);
            System.out.println("\u2713 Cuenta personal creada correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void eliminarCuentaPersonal() {
        System.out.println("\n--- Eliminar cuenta personal ---\n");
        Cuenta c = seleccionarCuentaPersonal();
        if (c == null) return;
        try {
            String resp = leerLinea("¿Seguro que desea eliminar \"" + c.getNombre() + "\"? (s/N): ");
            if (!resp.equalsIgnoreCase("s") && !resp.equalsIgnoreCase("si")) {
                System.out.println("Cancelado.\n");
                return;
            }
            controller.eliminarCuenta(c.getId());
            System.out.println("\u2713 Cuenta personal eliminada correctamente.\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void anadirGastoPersonal() {
        Cuenta c = seleccionarCuentaPersonal();
        if (c == null) return;
        System.out.println("\n--- Añadir gasto personal ---\n");
        try {
            double cantidad = leerCantidad("Cantidad (o 'salir'): ");
            LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter=hoy] (o 'salir'): ");
            String nombreCat = leerCategoria("Categoría (o 'salir'): ", c);
            controller.registrarGasto(c.getId(), cantidad, fecha, nombreCat);
            System.out.println("\u2713 Gasto registrado correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void editarGastoPersonal() {
        Cuenta c = seleccionarCuentaPersonal();
        if (c == null) return;
        System.out.println("\n--- Editar gasto personal ---");
        List<Gasto> gastos = controller.obtenerGastos(c.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.\n");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto (o 'salir'): ");
            System.out.println("(Enter para mantener el valor actual)");
            double cantidad = leerCantidadOpcional("Cantidad [" + g.getCantidad() + "] (o 'salir'): ", g.getCantidad());
            LocalDate fecha = leerFechaOpcional("Fecha [" + g.getFecha() + "] (o 'salir'): ", g.getFecha());
            String nombreCat = leerCategoriaOpcional("Categoría [" + g.getCategoria().getNombre() + "] (o 'salir'): ", c);
            controller.editarGasto(c.getId(), g.getId(), cantidad, fecha, nombreCat);
            System.out.println("\u2713 Gasto actualizado correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    // ========================================================================
    // Operaciones cuentas compartidas
    // ========================================================================

    private void crearCuentaCompartida() {
        System.out.println("\n--- Crear cuenta compartida ---\n");
        try {
            String nombre = leerLinea("Nombre de la cuenta (o 'salir'): ");
            System.out.println("Introduzca los nombres de los participantes (deje en blanco para terminar):");
            List<Persona> personas = new ArrayList<>();
            while (true) {
                String p = leerLinea("  Participante (o 'salir'): ");
                if (p.isEmpty()) break;
                personas.add(new Persona(p));
            }
            if (personas.size() < 2) {
                System.out.println("Se necesitan al menos 2 participantes.\n");
                return;
            }
            System.out.println("Tipo de reparto:");
            System.out.println("1. Equitativo");
            System.out.println("2. Personalizado\n");
            String tipo = leerLinea("Seleccione (o 'salir'): ");
            CuentaCompartida.TipoReparto reparto = tipo.equals("2")
                    ? CuentaCompartida.TipoReparto.PERSONALIZADO
                    : CuentaCompartida.TipoReparto.EQUITATIVO;
            controller.crearCuentaCompartida(nombre, personas, reparto);
            System.out.println("\u2713 Cuenta compartida creada correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void eliminarCuentaCompartida() {
        System.out.println("\n--- Eliminar cuenta compartida ---\n");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        try {
            String resp = leerLinea("¿Seguro que desea eliminar \"" + cc.getNombre() + "\"? (s/N): ");
            if (!resp.equalsIgnoreCase("s") && !resp.equalsIgnoreCase("si")) {
                System.out.println("Cancelado.\n");
                return;
            }
            controller.eliminarCuenta(cc.getId());
            System.out.println("\u2713 Cuenta compartida eliminada correctamente.\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void menuCuentasCompartidas() {
        System.out.println("\n--- Cuentas compartidas ---\n");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        mostrarDetalleCuenta(cc);
        while (true) {
            System.out.println();
            if (cc.getTipoReparto() == CuentaCompartida.TipoReparto.PERSONALIZADO) {
                System.out.println(" 1. Volver al menú principal");
                System.out.println(" 2. Configurar porcentajes de reparto\n");
                String input = leerLinea("Seleccione (o 'salir'): ");
                try {
                    int op = Integer.parseInt(input);
                    if (op == 1) return;
                    if (op == 2) {
                        configurarPorcentajes(cc);
                        mostrarDetalleCuenta(cc);
                    } else {
                        System.out.println("Opción inválida.");
                    }
                } catch (CancelarOperacion e) {
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Número inválido.");
                }
            } else {
                return;
            }
        }
    }

    private void anadirGastoCompartido() {
        System.out.println("\n--- Añadir gasto compartido ---\n");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        try {
            double cantidad = leerCantidad("Cantidad (o 'salir'): ");
            LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter=hoy] (o 'salir'): ");
            String nombreCat = leerCategoria("Categoría (o 'salir'): ", cc);
            Persona pagador = seleccionarPersona(cc);
            controller.registrarGasto(cc.getId(), cantidad, fecha, nombreCat, pagador);
            System.out.println("\u2713 Gasto registrado correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void editarGastoCompartido() {
        System.out.println("\n--- Editar gasto compartido ---\n");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        List<Gasto> gastos = controller.obtenerGastos(cc.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos en esta cuenta.\n");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto (o 'salir'): ");
            System.out.println("(Enter para mantener el valor actual)");
            double cantidad = leerCantidadOpcional("Cantidad [" + g.getCantidad() + "] (o 'salir'): ", g.getCantidad());
            LocalDate fecha = leerFechaOpcional("Fecha [" + g.getFecha() + "] (o 'salir'): ", g.getFecha());
            String nombreCat = leerCategoriaOpcional("Categoría [" + g.getCategoria().getNombre() + "] (o 'salir'): ", cc);
            Persona pagador = seleccionarPersona(cc, "Pagador actual: " + g.getPagador() + ". ¿Nuevo pagador (o 'salir')? ");
            controller.editarGastoConPagador(cc.getId(), g.getId(), cantidad, fecha, nombreCat, pagador);
            System.out.println("\u2713 Gasto actualizado correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void eliminarGastoCompartido() {
        System.out.println("\n--- Eliminar gasto compartido ---\n");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        eliminarGasto(cc);
    }

    // ========================================================================
    // Operaciones de filtrado
    // ========================================================================

    private void filtrarGastos() {
        System.out.println("\n--- Filtrar gastos ---\n");
        try {
            Cuenta cuenta = seleccionarCuentaFiltro();
            if (cuenta == null) return;

            FiltroMultiple filtros = new FiltroMultiple();

            System.out.println("(deje en blanco o N para saltar cada filtro)\n");

            List<Month> meses = preguntarMeses();
            if (meses != null && !meses.isEmpty()) {
                filtros.agregarFiltro(new FiltroMeses(meses));
            }

            FiltroFechas rango = preguntarRangoFechas();
            if (rango != null) {
                filtros.agregarFiltro(rango);
            }

            List<Categoria> cats = preguntarCategorias(cuenta);
            if (cats != null && !cats.isEmpty()) {
                filtros.agregarFiltro(new FiltroCategorias(cats));
            }

            List<Gasto> resultados;
            if (filtros.getFiltros().isEmpty()) {
                resultados = controller.obtenerGastos(cuenta.getId());
                System.out.println("(Sin filtros \u2014 mostrando todos)\n");
            } else {
                resultados = controller.filtrarGastos(cuenta.getId(), filtros);
            }

            if (resultados.isEmpty()) {
                System.out.println("No se encontraron gastos con esos filtros.\n");
            } else {
                System.out.println("Resultados (" + resultados.size() + " gastos):\n");
                mostrarGastos(resultados, cuenta.isCompartida());
                System.out.println();
            }

        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private Cuenta seleccionarCuentaFiltro() {
        System.out.println("¿Sobre qué cuenta?");
        System.out.println("1. Personal");
        System.out.println("2. Compartida\n");
        while (true) {
            String linea = leerLinea("Seleccione (o 'salir'): ");
            if (linea.equals("1")) {
                Cuenta c = seleccionarCuentaPersonal();
                if (c == null) continue;
                return c;
            }
            if (linea.equals("2")) {
                CuentaCompartida cc = seleccionarCuentaCompartida();
                if (cc == null) return null;
                return cc;
            }
            System.out.println("Opción inválida.\n");
        }
    }

    private List<Month> preguntarMeses() {
        if (!preguntarSiNo("¿Filtrar por meses? (s/N): ")) return null;
        System.out.println();
        System.out.println("  1. ENERO       7. JULIO");
        System.out.println("  2. FEBRERO     8. AGOSTO");
        System.out.println("  3. MARZO       9. SEPTIEMBRE");
        System.out.println("  4. ABRIL      10. OCTUBRE");
        System.out.println("  5. MAYO       11. NOVIEMBRE");
        System.out.println("  6. JUNIO      12. DICIEMBRE");
        while (true) {
            try {
                String linea = leerLinea("Números (ej: 1,7,8) (o 'salir'): ");
                String[] partes = linea.split(",");
                List<Month> seleccionados = new ArrayList<>();
                for (String p : partes) {
                    int num = Integer.parseInt(p.trim());
                    if (num < 1 || num > 12) throw new NumberFormatException();
                    seleccionados.add(Month.of(num));
                }
                return seleccionados;
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Use números del 1 al 12 separados por coma.");
            }
        }
    }

    private FiltroFechas preguntarRangoFechas() {
        if (!preguntarSiNo("¿Filtrar por fechas? (s/N): ")) return null;
        System.out.println();
        while (true) {
            try {
                LocalDate inicio = leerFecha("Fecha inicio (yyyy-MM-dd) (o 'salir'): ");
                LocalDate fin = leerFecha("Fecha fin (yyyy-MM-dd) (o 'salir'): ");
                try {
                    return new FiltroFechas(inicio, fin);
                } catch (IllegalArgumentException e) {
                    System.out.println("La fecha fin debe ser posterior a la fecha inicio.");
                }
            } catch (CancelarOperacion e) { throw e; }
        }
    }

    private List<Categoria> preguntarCategorias(Cuenta cuenta) {
        if (!preguntarSiNo("¿Filtrar por categorías? (s/N): ")) return null;
        List<Categoria> disponibles = controller.obtenerCategorias(cuenta.getId());
        if (disponibles.isEmpty()) {
            System.out.println("No hay categorías disponibles.");
            return null;
        }
        System.out.println();
        System.out.println("Categorías:");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println((i + 1) + ". " + disponibles.get(i).getNombre());
        }
        while (true) {
            try {
                String linea = leerLinea("Números (ej: 1,3) (o 'salir'): ");
                String[] partes = linea.split(",");
                List<Categoria> seleccionadas = new ArrayList<>();
                for (String p : partes) {
                    int num = Integer.parseInt(p.trim());
                    if (num < 1 || num > disponibles.size()) throw new NumberFormatException();
                    seleccionadas.add(disponibles.get(num - 1));
                }
                return seleccionadas;
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private boolean preguntarSiNo(String prompt) {
        String linea = leerLinea(prompt);
        return linea.equalsIgnoreCase("s");
    }

    // ========================================================================
    // Helpers 
    // ========================================================================

    private Cuenta seleccionarCuentaPersonal() {
        List<Cuenta> personales = controller.obtenerCuentas().stream()
                .filter(c -> !c.isCompartida())
                .collect(Collectors.toList());
        if (personales.isEmpty()) {
            System.out.println("No hay cuentas personales. Cree una primero.\n");
            return null;
        }
        if (personales.size() == 1) {
            return personales.get(0);
        }
        System.out.println("Cuentas personales:");
        for (int i = 0; i < personales.size(); i++) {
            System.out.println((i + 1) + ". " + personales.get(i).getNombre());
        }
        System.out.print("Seleccione cuenta (o 'salir'): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("salir")) return null;
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= personales.size()) {
                System.out.println("Índice inválido.\n");
                return null;
            }
            return personales.get(idx);
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.\n");
            return null;
        }
    }

    private CuentaCompartida seleccionarCuentaCompartida() {
        List<Cuenta> todas = controller.obtenerCuentas();
        List<CuentaCompartida> compartidas = todas.stream()
                .filter(CuentaCompartida.class::isInstance)
                .map(CuentaCompartida.class::cast)
                .collect(Collectors.toList());
        if (compartidas.isEmpty()) {
            System.out.println("No hay cuentas compartidas.\n");
            return null;
        }
        System.out.println("Cuentas compartidas:");
        for (int i = 0; i < compartidas.size(); i++) {
            CuentaCompartida cc = compartidas.get(i);
            System.out.println((i + 1) + ". " + cc.getNombre() + " (" + cc.getPersonas().size() + " personas)");
        }
        System.out.print("Seleccione cuenta (o 'salir'): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("salir")) return null;
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= compartidas.size()) {
                System.out.println("Índice inválido.\n");
                return null;
            }
            return compartidas.get(idx);
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.\n");
            return null;
        }
    }

    private void mostrarDetalleCuenta(CuentaCompartida cc) {
        System.out.println();
        System.out.println("Cuenta: " + cc.getNombre());
        System.out.println("Tipo de reparto: " + cc.getTipoReparto());
        System.out.print("Miembros: ");
        System.out.println(cc.getPersonas().stream().map(Persona::getNombre).collect(Collectors.joining(", ")));

        if (cc.getTipoReparto() == CuentaCompartida.TipoReparto.PERSONALIZADO) {
            Map<String, Double> pcts = controller.obtenerPorcentajes(cc.getId());
            if (!pcts.isEmpty()) {
                System.out.print("Porcentajes: ");
                System.out.println(pcts.entrySet().stream()
                        .map(e -> e.getKey() + "=" + String.format("%.0f", e.getValue()) + "%")
                        .collect(Collectors.joining(", ")));
            }
        }

        System.out.println("\nSaldos:");
        Map<Persona, Double> saldos = controller.obtenerSaldos(cc.getId());
        if (saldos.isEmpty()) {
            System.out.println("  No hay saldos calculados.");
        } else {
            for (Map.Entry<Persona, Double> e : saldos.entrySet()) {
                System.out.printf("  %s: %+.2f\u20ac%n", e.getKey().getNombre(), e.getValue());
            }
        }
        System.out.println();
        verGastos(cc);
    }

    private void verGastos(Cuenta cuenta) {
        List<Gasto> gastos = controller.obtenerGastos(cuenta.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.");
            return;
        }
        mostrarGastos(gastos, cuenta.isCompartida());
    }

    private void mostrarGastos(List<Gasto> gastos, boolean compartida) {
        if (compartida) {
            System.out.printf(" %-3s %-10s %-12s %-15s %s%n", "#", "Cantidad", "Fecha", "Categoría", "Pagador");
            System.out.println(" " + "-".repeat(65));
            for (int i = 0; i < gastos.size(); i++) {
                Gasto g = gastos.get(i);
                System.out.printf(" %-3d %-10.2f %-12s %-15s %s%n",
                        i + 1, g.getCantidad(), g.getFecha(),
                        g.getCategoria().getNombre(),
                        g.getPagador() != null ? g.getPagador().getNombre() : "-");
            }
        } else {
            System.out.println();
            System.out.printf(" %-3s %-10s %-12s %s%n", "#", "Cantidad", "Fecha", "Categoría");
            System.out.println(" " + "-".repeat(50));
            for (int i = 0; i < gastos.size(); i++) {
                Gasto g = gastos.get(i);
                System.out.printf(" %-3d %-10.2f %-12s %s%n",
                        i + 1, g.getCantidad(), g.getFecha(),
                        g.getCategoria().getNombre());
            }
        }
    }

    private void eliminarGasto(Cuenta cuenta) {
        List<Gasto> gastos = controller.obtenerGastos(cuenta.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.\n");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto a eliminar (o 'salir'): ");
            System.out.println();
            System.out.printf("Gasto seleccionado:%n");
            System.out.printf("  Cantidad: %.2f\u20ac%n", g.getCantidad());
            System.out.println("  Fecha: " + g.getFecha());
            System.out.println("  Categoría: " + g.getCategoria().getNombre());
            if (g.getPagador() != null) {
                System.out.println("  Pagador: " + g.getPagador().getNombre());
            }
            System.out.print("¿Desea eliminar este gasto? (s/N): ");
            String conf = scanner.nextLine().trim().toLowerCase();
            if (!conf.equals("s")) {
                System.out.println("Eliminación cancelada.\n");
                return;
            }
            controller.eliminarGasto(cuenta.getId(), g.getId());
            System.out.println("\u2713 Gasto eliminado correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private Gasto seleccionarGasto(List<Gasto> gastos, String prompt) {
        System.out.println();
        for (int i = 0; i < gastos.size(); i++) {
            Gasto g = gastos.get(i);
            System.out.printf(" %d. %.2f\u20ac  %s  (%s)%n",
                    i + 1, g.getCantidad(), g.getFecha(),
                    g.getCategoria().getNombre());
        }
        while (true) {
            try {
                String linea = leerLinea(prompt);
                int idx = Integer.parseInt(linea) - 1;
                if (idx >= 0 && idx < gastos.size()) return gastos.get(idx);
                System.out.println("Índice inválido.");
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private Persona seleccionarPersona(CuentaCompartida cc) {
        return seleccionarPersona(cc, "Seleccione pagador (o 'salir'): ");
    }

    private Persona seleccionarPersona(CuentaCompartida cc, String mensaje) {
        List<Persona> personas = cc.getPersonas();
        System.out.println("Pagadores:");
        for (int i = 0; i < personas.size(); i++) {
            System.out.println((i + 1) + ". " + personas.get(i).getNombre());
        }
        while (true) {
            try {
                String linea = leerLinea(mensaje);
                int idx = Integer.parseInt(linea) - 1;
                if (idx >= 0 && idx < personas.size()) return personas.get(idx);
                System.out.println("Índice inválido.");
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private void configurarPorcentajes(CuentaCompartida cc) {
        System.out.println("\n--- Configurar porcentajes de reparto ---");
        System.out.println("(Enter para mantener el valor actual)\n");
        List<Persona> personas = cc.getPersonas();
        Map<String, Double> actuales = controller.obtenerPorcentajes(cc.getId());
        Map<String, Double> nuevos = new HashMap<>();
        try {
            for (Persona p : personas) {
                double actual = actuales.getOrDefault(p.getNombre(), 0.0);
                String prompt = String.format("Porcentaje para %s [%.0f%%] (o 'salir'): ",
                        p.getNombre(), actual);
                String linea = leerLinea(prompt);
                if (linea.isEmpty()) {
                    nuevos.put(p.getNombre(), actual);
                } else {
                    double pct = Double.parseDouble(linea);
                    if (pct < 0 || pct > 100) {
                        System.out.println("El porcentaje debe estar entre 0 y 100.");
                        return;
                    }
                    nuevos.put(p.getNombre(), pct);
                }
            }
            double suma = nuevos.values().stream().mapToDouble(Double::doubleValue).sum();
            if (Math.abs(suma - 100.0) > 0.001) {
                System.out.printf("Los porcentajes deben sumar 100%%. Suma actual: %.1f%%\n", suma);
                return;
            }
            controller.configurarPorcentajes(cc.getId(), nuevos);
            System.out.println("\u2713 Porcentajes actualizados correctamente.\n");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage() + "\n");
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.\n");
        }
    }

    private String leerLinea(String prompt) {
        System.out.print(prompt);
        String linea = scanner.nextLine().trim();
        if (linea.equalsIgnoreCase("salir")) throw new CancelarOperacion();
        return linea;
    }

    private double leerCantidad(String prompt) {
        while (true) {
            try {
                String linea = leerLinea(prompt);
                double c = Double.parseDouble(linea);
                if (c <= 0) {
                    System.out.println("La cantidad debe ser positiva.");
                } else {
                    return c;
                }
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private double leerCantidadOpcional(String prompt, double defecto) {
        while (true) {
            try {
                String linea = leerLinea(prompt);
                if (linea.isEmpty()) return defecto;
                double c = Double.parseDouble(linea);
                if (c <= 0) {
                    System.out.println("La cantidad debe ser positiva.");
                } else {
                    return c;
                }
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }

    private LocalDate leerFecha(String prompt) {
        while (true) {
            try {
                String linea = leerLinea(prompt);
                if (linea.isEmpty()) return LocalDate.now();
                return LocalDate.parse(linea, FMT);
            } catch (CancelarOperacion e) { throw e; }
            catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use yyyy-MM-dd.");
            }
        }
    }

    private LocalDate leerFechaOpcional(String prompt, LocalDate defecto) {
        while (true) {
            try {
                String linea = leerLinea(prompt);
                if (linea.isEmpty()) return defecto;
                return LocalDate.parse(linea, FMT);
            } catch (CancelarOperacion e) { throw e; }
            catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use yyyy-MM-dd.");
            }
        }
    }

    private String leerCategoria(String prompt, Cuenta cuenta) {
        List<Categoria> cats = controller.obtenerCategorias(cuenta.getId());
        System.out.println("Categorías: " +
                cats.stream().map(Categoria::getNombre).collect(Collectors.joining(", ")));
        while (true) {
            String linea = leerLinea(prompt);
            if (!linea.isEmpty()) return linea;
            System.out.println("La categoría no puede estar vacía.");
        }
    }

    private String leerCategoriaOpcional(String prompt, Cuenta cuenta) {
        List<Categoria> cats = controller.obtenerCategorias(cuenta.getId());
        System.out.println("Categorías: " +
                cats.stream().map(Categoria::getNombre).collect(Collectors.joining(", ")));
        String linea = leerLinea(prompt);
        if (linea.isEmpty()) return null;
        return linea;
    }

    public static void main(String[] args) {
        CuentaController controller = Configuracion.getInstancia().getCuentaController();
        new CLI(controller).ejecutar();
    }
}
