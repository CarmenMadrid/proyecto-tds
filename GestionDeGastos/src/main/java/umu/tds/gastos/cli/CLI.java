package umu.tds.gastos.cli;

import umu.tds.gastos.app.Configuracion;
import umu.tds.gastos.controller.CuentaController;
import umu.tds.gastos.domain.core.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLI {

    private static class CancelarOperacion extends RuntimeException {
        CancelarOperacion() { super("Operaci\u00f3n cancelada."); }
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final CuentaController controller;
    private final Scanner scanner;
    private final Cuenta cuentaPersonal;

    public CLI(CuentaController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.cuentaPersonal = controller.obtenerCuentaPersonal();
    }

    public void ejecutar() {
        while (true) {
            mostrarMenu();
            String input = scanner.nextLine().trim();
            try {
                switch (Integer.parseInt(input)) {
                    case 1 -> verGastos(cuentaPersonal);
                    case 2 -> anadirGastoPersonal();
                    case 3 -> editarGastoPersonal();
                    case 4 -> eliminarGasto(cuentaPersonal);
                    case 5 -> menuCuentasCompartidas();
                    case 6 -> anadirGastoCompartido();
                    case 7 -> editarGastoCompartido();
                    case 8 -> eliminarGastoCompartido();
                    case 9 -> { System.out.println("\u00a1Hasta luego!"); return; }
                    default -> System.out.println("Opci\u00f3n inv\u00e1lida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduzca un n\u00famero.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550");
        System.out.println("     GESTI\u00d3N DE GASTOS");
        System.out.println("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550");
        System.out.println(" 1. Ver gastos personales");
        System.out.println(" 2. A\u00f1adir gasto personal");
        System.out.println(" 3. Editar gasto personal");
        System.out.println(" 4. Eliminar gasto personal");
        System.out.println(" 5. Ver cuentas compartidas");
        System.out.println(" 6. A\u00f1adir gasto compartido");
        System.out.println(" 7. Editar gasto compartido");
        System.out.println(" 8. Eliminar gasto compartido");
        System.out.println(" 9. Salir");
        System.out.println("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500");
        System.out.print("Seleccione: ");
    }

    // ========================================================================
    // Operaciones cuenta personal
    // ========================================================================

    private void anadirGastoPersonal() {
        System.out.println("\n--- A\u00f1adir gasto personal ---");
        try {
            double cantidad = leerCantidad("Cantidad: ");
            LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter=hoy]: ");
            String nombreCat = leerCategoria("Categor\u00eda: ", cuentaPersonal);
            controller.registrarGasto(cuentaPersonal.getId(), cantidad, fecha, nombreCat);
            System.out.println("Gasto registrado.");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarGastoPersonal() {
        System.out.println("\n--- Editar gasto personal ---");
        List<Gasto> gastos = controller.obtenerGastos(cuentaPersonal.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto: ");
            System.out.println("(Enter para mantener el valor actual)");
            double cantidad = leerCantidadOpcional("Cantidad (" + g.getCantidad() + "): ", g.getCantidad());
            LocalDate fecha = leerFechaOpcional("Fecha (" + g.getFecha() + "): ", g.getFecha());
            String nombreCat = leerCategoriaOpcional("Categor\u00eda (" + g.getCategoria().getNombre() + "): ", cuentaPersonal);
            controller.editarGasto(cuentaPersonal.getId(), g.getId(), cantidad, fecha, nombreCat);
            System.out.println("Gasto actualizado.");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ========================================================================
    // Operaciones cuentas compartidas
    // ========================================================================

    private void menuCuentasCompartidas() {
        System.out.println("\n--- Cuentas compartidas ---");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        mostrarDetalleCuenta(cc);
    }

    private void anadirGastoCompartido() {
        System.out.println("\n--- A\u00f1adir gasto compartido ---");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        try {
            double cantidad = leerCantidad("Cantidad: ");
            LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd) [Enter=hoy]: ");
            String nombreCat = leerCategoria("Categor\u00eda: ", cc);
            Persona pagador = seleccionarPersona(cc);
            controller.registrarGasto(cc.getId(), cantidad, fecha, nombreCat, pagador);
            System.out.println("Gasto registrado.");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarGastoCompartido() {
        System.out.println("\n--- Editar gasto compartido ---");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        List<Gasto> gastos = controller.obtenerGastos(cc.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos en esta cuenta.");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto: ");
            System.out.println("(Enter para mantener el valor actual)");
            double cantidad = leerCantidadOpcional("Cantidad (" + g.getCantidad() + "): ", g.getCantidad());
            LocalDate fecha = leerFechaOpcional("Fecha (" + g.getFecha() + "): ", g.getFecha());
            String nombreCat = leerCategoriaOpcional("Categor\u00eda (" + g.getCategoria().getNombre() + "): ", cc);
            controller.editarGasto(cc.getId(), g.getId(), cantidad, fecha, nombreCat);
            System.out.println("Gasto actualizado.");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarGastoCompartido() {
        System.out.println("\n--- Eliminar gasto compartido ---");
        CuentaCompartida cc = seleccionarCuentaCompartida();
        if (cc == null) return;
        eliminarGasto(cc);
    }

    // ========================================================================
    // Helpers 
    // ========================================================================

    private CuentaCompartida seleccionarCuentaCompartida() {
        List<Cuenta> todas = controller.obtenerCuentas();
        List<CuentaCompartida> compartidas = todas.stream()
                .filter(CuentaCompartida.class::isInstance)
                .map(CuentaCompartida.class::cast)
                .collect(Collectors.toList());
        if (compartidas.isEmpty()) {
            System.out.println("No hay cuentas compartidas.");
            return null;
        }
        System.out.println("Cuentas compartidas:");
        for (int i = 0; i < compartidas.size(); i++) {
            CuentaCompartida cc = compartidas.get(i);
            System.out.printf(" %d. %s (%d personas)%n", i + 1, cc.getNombre(), cc.getPersonas().size());
        }
        System.out.print("Seleccione cuenta (o 'salir'): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("salir")) return null;
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= compartidas.size()) {
                System.out.println("\u00cdndice inv\u00e1lido.");
                return null;
            }
            return compartidas.get(idx);
        } catch (NumberFormatException e) {
            System.out.println("N\u00famero inv\u00e1lido.");
            return null;
        }
    }

    private void mostrarDetalleCuenta(CuentaCompartida cc) {
        System.out.println("\nCuenta: " + cc.getNombre());
        System.out.println("Tipo de reparto: " + cc.getTipoReparto());
        System.out.print("Miembros: ");
        System.out.println(cc.getPersonas().stream().map(Persona::getNombre).collect(Collectors.joining(", ")));

        System.out.println("\nSaldos:");
        Map<Persona, Double> saldos = controller.obtenerSaldos(cc.getId());
        if (saldos.isEmpty()) {
            System.out.println("  No hay saldos calculados.");
        } else {
            for (Map.Entry<Persona, Double> e : saldos.entrySet()) {
                System.out.printf("  %s: %+.2f\u20ac%n", e.getKey().getNombre(), e.getValue());
            }
        }

        verGastos(cc);
    }

    private void verGastos(Cuenta cuenta) {
        List<Gasto> gastos = controller.obtenerGastos(cuenta.getId());
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos.");
            return;
        }
        boolean compartida = cuenta.isCompartida();
        if (compartida) {
            System.out.printf(" %-3s %-10s %-12s %-15s %s%n", "#", "Cantidad", "Fecha", "Categor\u00eda", "Pagador");
            System.out.println(" " + "-".repeat(70));
            for (int i = 0; i < gastos.size(); i++) {
                Gasto g = gastos.get(i);
                System.out.printf(" %-3d %-10.2f %-12s %-15s %s%n",
                        i + 1, g.getCantidad(), g.getFecha(),
                        g.getCategoria().getNombre(),
                        g.getPagador() != null ? g.getPagador().getNombre() : "-");
            }
        } else {
            System.out.printf(" %-3s %-10s %-12s %s%n", "#", "Cantidad", "Fecha", "Categor\u00eda");
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
            System.out.println("No hay gastos.");
            return;
        }
        try {
            Gasto g = seleccionarGasto(gastos, "Seleccione gasto a eliminar: ");
            System.out.printf("Gasto: %.2f\u20ac, %s, %s%n", g.getCantidad(), g.getFecha(),
                    g.getCategoria().getNombre());
            if (g.getPagador() != null) {
                System.out.println("Pagador: " + g.getPagador().getNombre());
            }
            System.out.print("\u00bfEliminar? (s/N): ");
            String conf = scanner.nextLine().trim().toLowerCase();
            if (!conf.equals("s")) {
                System.out.println("Cancelado.");
                return;
            }
            controller.eliminarGasto(cuenta.getId(), g.getId());
            System.out.println("Gasto eliminado.");
        } catch (CancelarOperacion e) {
            System.out.println(e.getMessage());
        }
    }

    private Gasto seleccionarGasto(List<Gasto> gastos, String prompt) {
        for (int i = 0; i < gastos.size(); i++) {
            Gasto g = gastos.get(i);
            System.out.printf(" %d. %.2f\u20ac - %s (%s)%n",
                    i + 1, g.getCantidad(), g.getFecha(),
                    g.getCategoria().getNombre());
        }
        while (true) {
            try {
                String linea = leerLinea(prompt);
                int idx = Integer.parseInt(linea) - 1;
                if (idx >= 0 && idx < gastos.size()) return gastos.get(idx);
                System.out.println("\u00cdndice inv\u00e1lido.");
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("N\u00famero inv\u00e1lido.");
            }
        }
    }

    private Persona seleccionarPersona(CuentaCompartida cc) {
        List<Persona> personas = cc.getPersonas();
        System.out.println("Pagadores:");
        for (int i = 0; i < personas.size(); i++) {
            System.out.printf(" %d. %s%n", i + 1, personas.get(i).getNombre());
        }
        while (true) {
            try {
                String linea = leerLinea("Seleccione pagador: ");
                int idx = Integer.parseInt(linea) - 1;
                if (idx >= 0 && idx < personas.size()) return personas.get(idx);
                System.out.println("\u00cdndice inv\u00e1lido.");
            } catch (CancelarOperacion e) { throw e; }
            catch (NumberFormatException e) {
                System.out.println("N\u00famero inv\u00e1lido.");
            }
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
                System.out.println("N\u00famero inv\u00e1lido.");
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
                System.out.println("N\u00famero inv\u00e1lido.");
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
                System.out.println("Formato inv\u00e1lido. Use yyyy-MM-dd.");
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
                System.out.println("Formato inv\u00e1lido. Use yyyy-MM-dd.");
            }
        }
    }

    private String leerCategoria(String prompt, Cuenta cuenta) {
        List<Categoria> cats = controller.obtenerCategorias(cuenta.getId());
        System.out.println("Categor\u00edas: " +
                cats.stream().map(Categoria::getNombre).collect(Collectors.joining(", ")));
        while (true) {
            String linea = leerLinea(prompt);
            if (!linea.isEmpty()) return linea;
            System.out.println("La categor\u00eda no puede estar vac\u00eda.");
        }
    }

    private String leerCategoriaOpcional(String prompt, Cuenta cuenta) {
        List<Categoria> cats = controller.obtenerCategorias(cuenta.getId());
        System.out.println("Categor\u00edas: " +
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
