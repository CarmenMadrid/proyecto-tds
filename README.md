# Gestión de Gastos

Aplicación de escritorio para la gestión y control de gastos personales y compartidos, desarrollada en JavaFX como proyecto de la asignatura Tecnología del Desarrollo del Software.

## Integrantes del grupo

| Nombre                  | Email                   | Subgrupo |
|-------------------------|-------------------------|----------|
| Paula García Algar      | p.garciaalgar@um.es     | 2.3      |
| Carmen Madrid Fernández | c.madridfernandez@um.es | 2.3      |
| Álvaro Sánchez García   | alvaro.s.g1@um.es       | 2.3      |

## Descripción

La aplicación permite gestionar gastos personales y compartidos mediante una interfaz gráfica (JavaFX) o una línea de comandos (CLI). Sus funcionalidades principales incluyen:

- **Gestión de cuentas**: creación de cuentas personales y cuentas compartidas con reparto equitativo o por porcentajes personalizados.
- **Registro de gastos**: creación, modificación y borrado de gastos, con categorización y asignación a participantes en cuentas compartidas.
- **Visualización**: tabla de gastos filtrable y gráficos (barras y circular) para analizar la distribución de los gastos.
- **Alertas de gasto**: configuración de límites semanales o mensuales, con generación automática de notificaciones al superarlos.
- **Filtros**: búsqueda combinada por categoría, fechas, persona y mes, mediante filtros componibles.
- **Importación de gastos**: lectura de gastos desde ficheros CSV externos.
- **Persistencia**: almacenamiento en archivos JSON mediante la librería Jackson.

El proyecto sigue una arquitectura MVC en capas y aplica los patrones de diseño Estrategia, Adaptador, Método Factoría, Singleton, Repositorio, Compuesto, Fachada y Observador.

## Cómo ejecutar

### Interfaz gráfica (GUI)

```bash
cd GestionDeGastos
mvn clean javafx:run
```

### Línea de comandos (CLI)

```bash
cd GestionDeGastos
mvn exec:java -Dexec.mainClass="umu.tds.gastos.cli.CLI" -q
```

### Tests

```bash
cd GestionDeGastos
mvn test
```

## Documentación

| Documento | Descripción |
|-----------|-------------|
| [Historias de usuario](docs/HistoriasDeUsuario.md) | Especificación de requisitos funcionales |
| [Decisiones de diseño](docs/DecisionesDeDiseño.md) | Arquitectura y decisiones de diseño relevantes |
| [Patrones de diseño](docs/PatronesDeDiseño.md) | Descripción de los patrones implementados |
| [Diagrama de dominio](docs/DiagramaDeDominio.md) | Modelo conceptual del dominio |
| [Diagrama de interacción](docs/DiagramaDeInteraccion.md) | Secuencia de interacción entre componentes para la HU3 |
| [Manual de usuario](docs/ManualDeUsuario.md) | Guía de uso de la aplicación |
| [Diagramas](docs/diagramas/) | Recursos gráficos adicionales |
