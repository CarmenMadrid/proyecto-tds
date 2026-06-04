package umu.tds.gastos.imports;

public class ImportadorFactory {

    public static ImportadorGastos crearImportador(String formato) {
        switch (formato.toLowerCase()) {
            case "csv":
                return new ImportadorCSV();
            default:
                throw new IllegalArgumentException(
                        "Formato no soportado: " + formato + ". Formatos disponibles: csv");
        }
    }
}
