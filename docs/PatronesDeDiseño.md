# Patrones de Diseño

## 1. Estrategia (Strategy)

**Problema:** Las alertas de gasto necesitan distintas formas de calcular el período (semanal o mensual).

**Solución:** Se define la interfaz `EstrategiaTiempo` con el método `comprobar(Alertas, List<Gasto>)`. Dos implementaciones concretas:

- `EstrategiaSemanal` — filtra y suma gastos por semana.
- `EstrategiaMensual` — filtra y suma gastos por mes.

La clase `Alertas` delega en la estrategia inyectada para verificar si se supera el límite (`superaLimite()`). Se puede cambiar la estrategia en tiempo de ejecución.

**Localización:** `domain.alertas.patronEstrategias`

---

## 2. Adaptador (Adapter)

**Problema:** Los ficheros CSV externos tienen un formato de texto plano que no es directamente usable por el dominio.

**Solución:** `LectorCSV` actúa como adaptador: lee el CSV línea por línea y lo convierte en objetos `GastoCSV` estructurados. El cliente (`ImportadorCSV`) usa estos objetos tipados sin conocer el parseo.

**Localización:** `imports.lector.LectorCSV`, `imports.dto.GastoCSV`

---

## 3. Método Factoría (Factory Method)

**Problema:** La aplicación debe crear el importador adecuado según el formato del fichero sin que el cliente conozca las clases concretas.

**Solución:** `ImportadorFactory` tiene un método estático `crearImportador(String formato)` que devuelve la implementación de `ImportadorGastos` correspondiente. Actualmente soporta CSV, pero añadir nuevos formatos solo requiere crear una nueva clase y registrarla.

**Localización:** `imports.ImportadorFactory`, `imports.ImportadorGastos`, `imports.ImportadorCSV`

---

## 4. Singleton

**Problema:** Ciertos componentes (configuración, gestión de escenas) deben tener una única instancia accesible globalmente.

**Solución:** Dos clases con `getInstancia()` e inicialización perezosa:

- `Configuracion` — centraliza repositorios y `CuentaController`.
- `SceneManager` — gestiona ventanas y transiciones JavaFX.

**Localización:** `app.Configuracion`, `ui.view.SceneManager`

---

## 5. Repositorio (Repository)

**Problema:** La persistencia debe estar abstraída para poder cambiar el mecanismo de almacenamiento sin afectar al dominio.

**Solución:** Una interfaz por entidad y una implementación concreta con Jackson para JSON:

| Interfaz | Implementación |
|----------|---------------|
| `CuentaRepository` | `CuentaRepositoryJson` |
| `AlertasRepository` | `AlertasRepositoryJson` |
| `NotificacionesRepository` | `NotificacionesRepositoryJson` |

Todas exponen operaciones CRUD y se inyectan en `Configuracion`.

**Localización:** `persistence` (interfaces), `persistence.json` (implementaciones)

---

## 6. Observador (Observer)

**Problema:** La UI debe reflejar los cambios en los datos automáticamente.

**Solución:** Dos mecanismos:

1. `verificarAlertas()` recorre las alertas activas y genera `Notificacion` si se supera el límite.
2. JavaFX PropertyListeners actualizan la UI al cambiar la cuenta seleccionada o la pestaña activa.

**Localización:** `controller.CuentaController.verificarAlertas()`, `ui.controller.VentanaPrincipalController`

---

## 7. Compuesto (Composite)

**Problema:** Los filtros de gastos deben poder combinarse y tratarse igual que un filtro individual.

**Solución:** `FiltroMultiple` implementa `Filtro` y contiene una lista de filtros. Su método `cumple(Gasto)` itera sobre todos evaluando AND lógico.

**Localización:** `domain.filtros.FiltroMultiple`

---

## 8. Fachada (Facade)

**Problema:** La UI y la CLI necesitan un punto de acceso simple a toda la lógica de negocio.

**Solución:** `CuentaController` encapsula cuentas, gastos, categorías, alertas, notificaciones, filtros e importación. Tanto la interfaz gráfica como la CLI lo usan sin conocer los subsistemas internos.

**Localización:** `controller.CuentaController`

---

## 9. MVC (Modelo-Vista-Controlador)

**Problema:** Separar la lógica de negocio, la presentación y el control de eventos.

**Solución:** Arquitectura en tres capas:
- **Modelo:** clases del dominio (`Cuenta`, `Gasto`, `Categoria`, `Alertas`, etc.).
- **Vista:** ficheros FXML + `SceneManager`.
- **Controladores:** `VentanaPrincipalController`, `LoginController`, etc. (UI) + `CuentaController` (lógica de aplicación).

**Localización:** `domain.core`, `ui.controller`, `ui/fxml/`

---

## Resumen

| Patrón | Tipo | Localización |
|--------|------|-------------|
| Estrategia | Comportamiento | `domain.alertas.patronEstrategias` |
| Adaptador | Estructural | `imports.lector.LectorCSV` |
| Método Factoría | Creacional | `imports.ImportadorFactory` |
| Singleton | Creacional | `app.Configuracion`, `ui.view.SceneManager` |
| Repositorio | Persistencia | `persistence` + `persistence.json` |
| Observador | Comportamiento | `controller.CuentaController`, PropertyListeners |
| Compuesto | Estructural | `domain.filtros` |
| Fachada | Estructural | `controller.CuentaController` |
| MVC | Arquitectural | `domain.core`, `ui.controller`, `ui/fxml/` |
