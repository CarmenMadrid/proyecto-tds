# Arquitectura de la Aplicación y Decisiones de Diseño

## Estructura en capas

```
Vista (FXML / CLI)
    ↓  (llama a)
CuentaController
    ↓  (usa)
Repositorios (Interfaces)
    ↓  (implementan)
RepositoriosJson (Jackson)
    ↓  (persiste en)
Archivos JSON (data/*.json)
```

Cada capa solo conoce a la inferior. La vista nunca accede directamente a los datos.

| Capa | Componentes |
|------|------------|
| **Vista** | FXML + JavaFX controllers (`ui.controller`), CLI (`cli.CLI`) |
| **Control** | `CuentaController` |
| **Dominio** | `domain.core`, `domain.alertas`, `domain.filtros` |
| **Persistencia** | `persistence` (interfaces), `persistence.json` (implementaciones Jackson) |

---

## Paquetes y responsabilidades

| Paquete | Responsabilidad |
|---------|-----------------|
| `app` | Punto de entrada (`MainApp.java`) y ensamblado de dependencias (`Configuracion` Singleton) |
| `controller` | Lógica de aplicación: orquesta el dominio y la persistencia (`CuentaController`) |
| `domain.core` | Entidades de negocio: `Cuenta`, `CuentaCompartida`, `Gasto`, `Categoria`, `Persona`, `Notificacion` |
| `domain.alertas` | Alertas de gasto con `EstrategiaTiempo` (Strategy: semanal/mensual) |
| `domain.filtros` | Filtros de búsqueda (Strategy + Composite) |
| `persistence` | Interfaces de repositorio (`CuentaRepository`, `AlertasRepository`, `NotificacionesRepository`) |
| `persistence.json` | Implementaciones JSON via Jackson (`*RepositoryJson`, `JsonService`) |
| `imports` | Importación de gastos desde ficheros externos (Factory + Adapter) |
| `ui.controller` | Controladores JavaFX que manejan eventos de la interfaz |
| `ui.view` | Gestión de escenas y navegación (`SceneManager` Singleton) |
| `cli` | Interfaz de línea de comandos alternativa |

---

## Archivos de datos

| Fichero | Contenido |
|---------|-----------|
| `data/cuentas.json` | Cuentas personales |
| `data/cuentas-compartidas.json` | Cuentas compartidas |
| `data/alertas.json` | Alertas y límites de gasto |
| `data/notificaciones.json` | Notificaciones generadas |

---

## Decisiones de Diseño

### 1. Paquetes organizados por capas, no por funcionalidad

Se optó por una estructura de paquetes que refleja la arquitectura en capas (`domain`, `controller`, `persistence`, `ui`) en lugar de agrupar por funcionalidad (ej. un paquete "cuentas" que contuviera modelo, controlador y vistas de cuenta). Esto hace que la separación entre capas sea explícita en la estructura del proyecto y facilita cambios y pruebas.

### 2. CuentaController como punto de entrada único

Toda la lógica de negocio se canaliza a través de un único controlador (`CuentaController`), que actúa como fachada del sistema. Tanto la interfaz gráfica como la CLI interactúan exclusivamente con esta clase. Se eligió esta opción frente a dividir la lógica en varios controladores especializados (uno para cuentas, otro para gastos, otro para alertas) porque el dominio es pequeño y un único controlador simplifica la navegación del código, evitando dependencias cruzadas entre varios controladores.

### 3. Inicialización automática con datos por defecto

Al arrancar por primera vez (archivos JSON vacíos), el sistema crea una cuenta personal con nombre "Personal" y seis categorías predefinidas (Comida, Transporte, Ocio, Salud, Educación, Otros). Esto evita que el usuario se encuentre con una aplicación vacía y tenga que crear todo desde cero, mejorando la experiencia de inicio.

### 4. Separación de cuentas personales y compartidas en archivos distintos

Las cuentas personales se guardan en `data/cuentas.json` y las compartidas en `data/cuentas-compartidas.json`, en lugar de usar un único archivo con un campo discriminador. Esto simplifica la lógica de carga y evita mezclar dos tipos de entidad con estructuras diferentes (las compartidas tienen participantes, porcentajes y saldos).

### 5. Diálogos modales con nested event loop en lugar de callbacks

Las pestañas emergentes se implementan con `Platform.enterNestedEventLoop()` de JavaFX, que pausa el hilo de la interfaz mientras el diálogo está abierto y lo reanuda al cerrarse. Se descartó la alternativa de usar callbacks o propiedades observables para recibir el resultado del diálogo porque el flujo secuencial resulta más legible: el código que abre el diálogo espera y recibe el resultado en la misma línea, sin tener que seguir la traza entre distintas clases.

### 6. Interfaz de línea de comandos como clase independiente con su propio main

La CLI (`cli.CLI`) tiene su propio `main()` y no depende de JavaFX. Esto permite ejecutar la aplicación en consola sin necesidad de entorno gráfico, útil para pruebas rápidas o para entornos sin interfaz gráfica. Comparte la misma configuración y datos que la GUI a través de `Configuracion`.

### 7. Filtros con Strategy + Composite

El sistema de filtros combina el patrón Strategy (cada tipo de filtro es una estrategia) con Composite (`FiltroMultiple` agrupa varios filtros y los evalúa como AND). Esto permite construir consultas complejas combinando filtros simples.
