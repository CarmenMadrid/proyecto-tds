# Patrones de Diseño Utilizados en GestionDeGastos

## 1. Patrón Estrategia (Strategy)

**Propósito:** Definir una familia de algoritmos, encapsular cada uno y hacerlos intercambiables. Permite que el algoritmo varíe independientemente de los clientes que lo usan.

**Implementación:** Sistema de alertas (`Alertas`, `EstrategiaTiempo`)

El sistema de alertas permite configurar límites de gasto con distintos comportamientos de reinicio periódico. Se definió la interfaz `EstrategiaTiempo` que declara `comprobar(Alertas, List<Gasto>)` y dos implementaciones concretas:

- **`EstrategiaSemanal`**: calcula el gasto acumulado en la semana ISO actual.
- **`EstrategiaMensual`**: calcula el gasto acumulado en el mes actual.

La clase `Alertas` (contexto) delega en la estrategia la verificación de si se ha superado el límite (`superaLimite()`), pudiendo cambiar de estrategia en tiempo de ejecución. También se usa Jackson con `@JsonTypeInfo` y `@JsonSubTypes` para serializar/deserializar correctamente las estrategias.

**Localización:**
- `umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaTiempo` — interfaz
- `umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaSemanal` — implementación semanal
- `umu.tds.gastos.domain.alertas.patronEstrategias.EstrategiaMensual` — implementación mensual
- `umu.tds.gastos.domain.alertas.Alertas` — contexto

---

## 2. Patrón Adaptador (Adapter)

**Propósito:** Convertir la interfaz de una clase en otra interfaz que el cliente espera. Permite que clases con interfaces incompatibles trabajen juntas.

**Implementación:** Importación de gastos desde ficheros CSV (`LectorCSV`)

El sistema necesita importar gastos desde ficheros CSV con un formato específico (columnas: fecha, importe, categoría, pagador, nombre, cuenta). Para ello, la clase `LectorCSV` actúa como **adaptador** que:

1. Recibe la ruta de un archivo CSV.
2. Lee línea por línea con `BufferedReader`.
3. Parsea y valida cada línea (fechas, números, etc.).
4. Adapta los datos crudos del CSV a objetos `GastoCSV` (DTO estructurado).

Los clientes (`ImportadorCSV`) trabajan con objetos `GastoCSV` tipados, no con cadenas de texto sin procesar.

**Localización:**
- `umu.tds.gastos.imports.lector.LectorCSV` — adaptador
- `umu.tds.gastos.imports.dto.GastoCSV` — DTO resultado de la adaptación
- `umu.tds.gastos.imports.ImportadorCSV` — cliente del adaptador

---

## 3. Patrón Método Factoría (Factory Method)

**Propósito:** Definir una interfaz para crear objetos, permitiendo que la clase decida qué implementación concreta instanciar.

**Implementación:** Creación de importadores según el formato (`ImportadorFactory`)

La clase `ImportadorFactory` proporciona un método estático `crearImportador(String formato)` que devuelve la implementación adecuada de `ImportadorGastos` según el formato solicitado:

```java
public static ImportadorGastos crearImportador(String formato) {
    switch (formato.toLowerCase()) {
        case "csv": return new ImportadorCSV();
        default: throw new IllegalArgumentException("Formato no soportado: " + formato);
    }
}
```

El producto (`ImportadorGastos`) es una interfaz con el método `importar(String archivo, Cuenta cuenta)`. Actualmente existe un producto concreto (`ImportadorCSV`), pero añadir nuevos formatos (JSON, XML bancario) solo requiere crear una nueva clase y registrarla en la factoría.

**Localización:**
- `umu.tds.gastos.imports.ImportadorFactory` — factoría
- `umu.tds.gastos.imports.ImportadorGastos` — interfaz del producto
- `umu.tds.gastos.imports.ImportadorCSV` — producto concreto

---

## 4. Patrón Singleton

**Propósito:** Garantizar que una clase tenga una única instancia y proporcionar un punto de acceso global a ella.

**Implementación:** Gestión de configuración y escenas

### 4.1. `Configuracion`
Centraliza el acceso a los repositorios (`CuentaRepository`, `AlertasRepository`, `NotificacionesRepository`) y al controlador principal (`CuentaController`). Se accede mediante:

```java
Configuracion.getInstancia().getCuentaController();
```

Uso de inicialización perezosa (lazy initialization) en `getInstancia()`.

### 4.2. `SceneManager`
Gestiona la ventana principal y las transiciones entre escenas JavaFX (login, ventana principal, diálogos overlay). Al ser un recurso compartido, se implementa como Singleton:

```java
SceneManager.getInstancia().cargarVentanaPrincipal();
```

**Localización:**
- `umu.tds.gastos.app.Configuracion` — `getInstancia()`
- `umu.tds.gastos.ui.view.SceneManager` — `getInstancia()`

---

## 5. Patrón Repositorio (Repository)

**Propósito:** Mediar entre el dominio y las capas de persistencia, actuando como una colección de objetos en memoria con operaciones CRUD. Centraliza el acceso a datos y mejora la mantenibilidad.

**Implementación:** Almacenamiento en JSON con Jackson

Por cada entidad del dominio existe una interfaz de repositorio y una implementación concreta que persiste en JSON:

| Interfaz | Implementación | Fichero |
|----------|---------------|---------|
| `CuentaRepository` | `CuentaRepositoryJson` | `data/cuentas.json`, `data/cuentas-compartidas.json` |
| `AlertasRepository` | `AlertasRepositoryJson` | `data/alertas.json` |
| `NotificacionesRepository` | `NotificacionesRepositoryJson` | `data/notificaciones.json` |

Cada implementación utiliza la librería Jackson para serializar/deserializar y dispone de un método `guardarDatos()` que persiste automáticamente tras cada operación de escritura. Las interfaces definen contratos como:

```java
public interface CuentaRepository {
    void addCuenta(Cuenta cuenta);
    void updateCuenta(Cuenta cuenta);
    void deleteCuenta(UUID id);
    Optional<Cuenta> getCuenta(UUID id);
    List<Cuenta> getAllCuentas();
}
```

**Localización:**
- `umu.tds.gastos.persistence.CuentaRepository` — interfaz
- `umu.tds.gastos.persistence.json.CuentaRepositoryJson` — implementación
- `umu.tds.gastos.persistence.AlertasRepository` — interfaz
- `umu.tds.gastos.persistence.json.AlertasRepositoryJson` — implementación
- `umu.tds.gastos.persistence.NotificacionesRepository` — interfaz
- `umu.tds.gastos.persistence.json.NotificacionesRepositoryJson` — implementación

---

## 6. Patrón Observador (Observer)

**Propósito:** Definir una dependencia uno-a-muchos entre objetos, de forma que cuando un objeto cambie su estado, todos los dependientes sean notificados automáticamente.

**Implementación:** Dos variantes complementarias

### 6.1. Notificaciones ante superación de límites
Cuando un usuario supera el límite de una alerta, `CuentaController.verificarAlertas()` recorre todas las alertas activas de la cuenta, invoca `superaLimite()` (que usa la estrategia correspondiente), y si se supera el límite crea una nueva `Notificacion` y la persiste en `NotificacionesRepository`.

### 6.2. UI reactiva con PropertyListeners de JavaFX
La interfaz gráfica se actualiza automáticamente ante cambios mediante los mecanismos nativos de JavaFX:

- **Listeners de selección de cuenta** (`comboCuenta.valueProperty().addListener(...)`): al cambiar la cuenta seleccionada se recargan los gastos, gráficas, filtros y alertas.
- **Listeners de selección de pestaña** (`tabAlertas.selectedProperty().addListener(...)`): al activar una pestaña se refrescan sus datos.
- **Propiedades observables**: los controladores exponen propiedades (`SimpleStringProperty`, `SimpleDoubleProperty`, etc.) que la vista enlaza mediante `fx:bidirectional`.

**Localización:**
- `umu.tds.gastos.controller.CuentaController.verificarAlertas()` — generación de notificaciones
- `umu.tds.gastos.ui.controller.VentanaPrincipalController` — listeners JavaFX (líneas 252-260, 535-569)

---

## 7. Patrón Compuesto (Composite)

**Propósito:** Componer objetos en estructuras de árbol para representar jerarquías parte-todo. Permite tratar objetos individuales y composiciones de manera uniforme.

**Implementación:** Filtros múltiples (`FiltroMultiple`)

El sistema de filtros de gastos permite filtrar por categoría, fechas, persona o mes de forma individual o combinada. La interfaz `Filtro` declara `cumple(Gasto): boolean`. `FiltroMultiple` implementa la misma interfaz pero contiene una lista de objetos `Filtro` y los evalúa como AND lógico:

```java
public class FiltroMultiple implements Filtro {
    private final List<Filtro> filtros;
    public void agregarFiltro(Filtro filtro) { ... }
    public boolean cumple(Gasto g) {
        for (Filtro filtro : filtros)
            if (!filtro.cumple(g)) return false;
        return true;
    }
}
```

De esta forma, `FiltroMultiple` se usa exactamente igual que cualquier filtro individual, permitiendo composiciones anidadas.

**Localización:**
- `umu.tds.gastos.domain.filtros.Filtro` — interfaz
- `umu.tds.gastos.domain.filtros.FiltroMultiple` — composite
- `umu.tds.gastos.domain.filtros.FiltroCategorias` — hoja
- `umu.tds.gastos.domain.filtros.FiltroFechas` — hoja
- `umu.tds.gastos.domain.filtros.FiltroPersona` — hoja
- `umu.tds.gastos.domain.filtros.FiltroMeses` — hoja

---

## 8. Patrón Fachada (Facade)

**Propósito:** Proporcionar una interfaz unificada y simplificada a un conjunto de interfaces de un subsistema.

**Implementación:** `CuentaController`

`CuentaController` actúa como fachada del subsistema de dominio. Encapsula:

- Gestión de cuentas (personales y compartidas)
- CRUD de gastos
- Categorías y su gestión
- Alertas y verificación de límites
- Notificaciones
- Filtros y consultas
- Cálculo de saldos y porcentajes
- Importación de gastos desde ficheros

Tanto la interfaz gráfica (`VentanaPrincipalController`, etc.) como la CLI obtienen el controlador a través de `Configuracion.getInstancia().getCuentaController()` y solo interactúan con esta fachada, sin conocer los repositorios, las estrategias ni los detalles de persistencia.

**Localización:**
- `umu.tds.gastos.controller.CuentaController`

---

## 9. Patrón Especificación (Specification / Filter)

**Propósito:** Encapsular una regla de negocio que determina si un objeto cumple cierto criterio, permitiendo combinaciones lógicas.

**Implementación:** Filtros de gastos

Cada filtro concreto implementa `Filtro` con una regla específica:

| Clase | Regla |
|-------|-------|
| `FiltroCategorias` | El gasto pertenece a una categoría de la lista |
| `FiltroFechas` | La fecha del gasto está en el rango \[inicio, fin\] |
| `FiltroPersona` | El pagador del gasto coincide con la persona |
| `FiltroMeses` | El mes del gasto está en la lista de meses |
| `FiltroMultiple` | Combinación AND de varias especificaciones |

**Localización:**
- `umu.tds.gastos.domain.filtros` (paquete completo)

---

## 10. Patrón Modelo-Vista-Controlador (MVC)

**Propósito:** Separar la lógica de negocio, la presentación y el control de la aplicación en tres componentes independientes.

**Implementación:** Arquitectura completa de la aplicación

| Capa | Componentes | Responsabilidad |
|------|-------------|----------------|
| **Modelo** | `Cuenta`, `Gasto`, `Categoria`, `Alertas`, `Persona`, `Notificacion`, `CuentaCompartida` | Lógica de negocio y datos |
| **Vista** | Ficheros FXML (`ui/fxml/`), `SceneManager` | Interfaz de usuario |
| **Controlador** | `VentanaPrincipalController`, `LoginController`, `CrearGastoController`, `CrearCategoriaController` (en `ui/controller/`) + `CuentaController` (en `controller/`) | Manejo de eventos, coordinación entre vista y modelo |

Los controladores de UI se enlazan con las vistas FXML mediante `fx:controller` y utilizan propiedades observables para la comunicación bidireccional.

**Localización:**
- `umu.tds.gastos.domain.core` — modelo
- `umu.tds.gastos.domain.alertas` — modelo (alertas)
- `umu.tds.gastos.ui.view` y `ui/fxml/` — vistas
- `umu.tds.gastos.ui.controller` — controladores de UI
- `umu.tds.gastos.controller.CuentaController` — controlador de aplicación (fachada)

---

## Resumen de cumplimiento

| Patrón | Tipo | ¿Requerido? | Localización principal |
|--------|------|-------------|----------------------|
| Estrategia | Comportamiento | Sí (alertas) | `EstrategiaTiempo`, `EstrategiaSemanal`, `EstrategiaMensual`, `Alertas` |
| Adaptador | Estructural | Sí (importación) | `LectorCSV`, `GastoCSV` |
| Método Factoría | Creacional | Sí (importación) | `ImportadorFactory`, `ImportadorGastos`, `ImportadorCSV` |
| Singleton | Creacional | Sí (acceso global) | `Configuracion`, `SceneManager` |
| Repositorio | Persistencia | Sí (almacenamiento) | `*Repository` (interfaces) + `*RepositoryJson` (implementaciones) |
| Observador | Comportamiento | No obligatorio | `verificarAlertas()`, PropertyListeners JavaFX |
| Compuesto | Estructural | No obligatorio | `FiltroMultiple`, `Filtro` |
| Fachada | Estructural | No obligatorio | `CuentaController` |
| Especificación | Comportamiento | No obligatorio | `Filtro` y sus implementaciones |
| MVC | Arquitectural | Implícito | `domain.core`, `ui.controller`, `ui/fxml/` |
