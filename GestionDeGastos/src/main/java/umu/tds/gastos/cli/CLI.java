package umu.tds.gastos.cli;

// import umu.tds.gastos.app.Configuracion;
// import umu.tds.gastos.controller.GastoController;
// import umu.tds.gastos.domain.core.Gasto;

// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;
// import java.time.format.DateTimeParseException;
// import java.util.List;
// import java.util.Scanner;
// import java.util.UUID;

// public class CLI {

//     private final GastoController controller;
//     private final Scanner scanner;

//     public CLI(GastoController controller) {
//         this.controller = controller;
//         this.scanner = new Scanner(System.in);
//     }

//     public void ejecutar(String[] args) {
//         if (args.length == 0) {
//             modoInteractivo();
//             return;
//         }

//         String comando = args[0].toLowerCase();
//         switch (comando) {
//             case "add" -> cmdAdd(args);
//             case "list" -> cmdList();
//             case "delete" -> cmdDelete(args);
//             case "categorias" -> cmdCategorias();
//             default -> mostrarAyuda();
//         }
//     }

//     private void modoInteractivo() {
//         System.out.println("Gestión de Gastos - CLI");
//         System.out.println("Escribe 'help' para ver los comandos disponibles.");
//         while (true) {
//             System.out.print("> ");
//             String linea = scanner.nextLine().trim();
//             if (linea.equals("exit") || linea.equals("quit")) break;
//             if (linea.isBlank()) continue;
//             ejecutar(linea.split(" "));
//         }
//     }

//     private void cmdAdd(String[] args) {
//         double cantidad = 0;
//         LocalDate fecha = LocalDate.now();
//         String categoria = "Otros";

//         for (int i = 1; i < args.length; i++) {
//             switch (args[i]) {
//                 case "--cantidad" -> cantidad = Double.parseDouble(args[++i]);
//                 case "--fecha" -> fecha = parseFecha(args[++i]);
//                 case "--categoria" -> categoria = args[++i];
//             }
//         }

//         try {
//             Gasto gasto = controller.registrarGasto(cantidad, fecha, categoria);
//             System.out.println("Gasto registrado: " + gasto);
//         } catch (Exception e) {
//             System.out.println("Error: " + e.getMessage());
//         }
//     }

//     private void cmdList() {
//         List<Gasto> gastos = controller.obtenerTodosLosGastos();
//         if (gastos.isEmpty()) {
//             System.out.println("No hay gastos registrados.");
//             return;
//         }
//         System.out.printf("%-36s %10s %12s %s%n", "ID", "Cantidad", "Fecha", "Categoría");
//         System.out.println("-".repeat(80));
//         for (Gasto g : gastos) {
//             System.out.printf("%-36s %10.2f %12s %s%n",
//                     g.getId(), g.getCantidad(), g.getFecha(),
//                     g.getCategoria() != null ? g.getCategoria().getNombre() : "");
//         }
//     }

//     private void cmdDelete(String[] args) {
//         UUID id = null;
//         for (int i = 1; i < args.length; i++) {
//             if (args[i].equals("--id")) {
//                 id = UUID.fromString(args[++i]);
//             }
//         }
//         if (id == null) {
//             System.out.println("Uso: delete --id <uuid>");
//             return;
//         }
//         controller.eliminarGasto(id);
//         System.out.println("Gasto eliminado.");
//     }

//     private void cmdCategorias() {
//         System.out.println("Categorías disponibles:");
//         controller.obtenerCategorias()
//                 .forEach(c -> System.out.println("  - " + c.getNombre()));
//     }

//     private void mostrarAyuda() {
//         System.out.println("Comandos:");
//         System.out.println("  add --cantidad <num> --fecha <yyyy-MM-dd> --categoria <nombre>");
//         System.out.println("  list");
//         System.out.println("  delete --id <uuid>");
//         System.out.println("  categorias");
//         System.out.println("  help");
//         System.out.println("  exit");
//     }

//     private LocalDate parseFecha(String s) {
//         try {
//             return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
//         } catch (DateTimeParseException e) {
//             System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
//             return LocalDate.now();
//         }
//     }

//     public static void main(String[] args) {
//         GastoController controller = Configuracion.getInstancia().getGastoController();
//         new CLI(controller).ejecutar(args);
//     }
// }
