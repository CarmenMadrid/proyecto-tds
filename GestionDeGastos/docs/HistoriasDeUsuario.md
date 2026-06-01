# Historias de Usuario

## HU-01: Registrar gasto

| Campo | Valor |
|-------|-------|
| **ID** | HU-01 |
| **Título** | Registrar un gasto personal |
| **Descripción** | Como usuario, quiero registrar un gasto indicando cantidad, fecha y categoría para llevar un control de mis gastos personales. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Registro correcto**
Dado que el usuario está en la ventana de añadir gasto
Cuando introduce una cantidad válida (> 0), una fecha y selecciona una categoría
Entonces el sistema crea el registro del gasto y lo muestra en la lista

**CA-02: Registro con datos incorrectos**
Dado que el usuario está en la ventana de añadir gasto
Cuando introduce una cantidad inválida (≤ 0) o deja campos obligatorios vacíos
Entonces el sistema muestra un mensaje de error y no crea el gasto

---

## HU-02: Editar gasto

| Campo | Valor |
|-------|-------|
| **ID** | HU-02 |
| **Título** | Editar un gasto existente |
| **Descripción** | Como usuario, quiero poder modificar la cantidad, fecha o categoría de un gasto ya registrado para mantener actualizado el registro de mis finanzas. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Modificación correcta**
Dado que el usuario está en la ventana de modificación de un gasto
Cuando modifica los datos por otros válidos
Entonces el sistema acepta la modificación y actualiza los datos en la vista

**CA-02: Modificación incorrecta**
Dado que el usuario está en la ventana de modificación de un gasto
Cuando introduce datos inválidos (cantidad ≤ 0, fecha vacía, etc.)
Entonces el sistema rechaza la modificación y muestra un mensaje de error

---

## HU-03: Eliminar gasto

| Campo | Valor |
|-------|-------|
| **ID** | HU-03 |
| **Título** | Eliminar un gasto |
| **Descripción** | Como usuario, quiero poder borrar un gasto para eliminar registros incorrectos o que ya no me interesen. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Eliminación confirmada**
Dado que el usuario selecciona un gasto y pulsa eliminar
Cuando confirma la operación en el diálogo de confirmación
Entonces el gasto se elimina del sistema de forma permanente y desaparece de la lista

**CA-02: Cancelación de la eliminación**
Dado que el usuario selecciona un gasto y pulsa eliminar
Cuando cancela la operación en el diálogo de confirmación
Entonces el gasto no se elimina y permanece en la lista

---

## HU-04: Gestionar categorías predefinidas

| Campo | Valor |
|-------|-------|
| **ID** | HU-04 |
| **Título** | Usar categorías predefinidas |
| **Descripción** | Como usuario, quiero que el sistema incluya categorías predefinidas (alimentación, transporte, entretenimiento, etc.) para clasificar mis gastos sin tener que crearlas desde cero. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Disponibilidad de categorías predefinidas**
Dado que el usuario abre el formulario de registro de gasto
Cuando despliega el selector de categorías
Entonces ve las categorías predefinidas disponibles

**CA-02: Protección de categorías predefinidas**
Dado que el usuario intenta eliminar una categoría predefinida
Cuando la categoría está en uso por algún gasto
Entonces el sistema impide su eliminación

---

## HU-05: Crear categorías personalizadas

| Campo | Valor |
|-------|-------|
| **ID** | HU-05 |
| **Título** | Crear categorías personalizadas |
| **Descripción** | Como usuario, quiero crear mis propias categorías de gasto para adaptar el sistema a mis necesidades. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Creación de categoría**
Dado que el usuario accede a la opción de nueva categoría
Cuando introduce un nombre válido y confirma
Entonces la nueva categoría se añade a la lista disponible al registrar gastos

**CA-02: Persistencia**
Dado que el usuario ha creado una categoría personalizada
Cuando cierra y vuelve a abrir la aplicación
Entonces la categoría sigue disponible

---

## HU-06: Gestionar gastos desde CLI

| Campo | Valor |
|-------|-------|
| **ID** | HU-06 |
| **Título** | Gestionar gastos desde menú interactivo en una terminal |
| **Descripción** | Como usuario, quiero gestionar gastos personales y compartidos mediante un menú interactivo en la línea de comandos para poder realizar las operaciones CRUD sin necesidad de la interfaz gráfica. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Menú principal con opciones**
Dado que el usuario inicia la terminal
Cuando se carga el menú
Entonces ve las opciones disponibles: ver, añadir, editar y eliminar gastos personales; ver cuentas compartidas; añadir, editar y eliminar gastos compartidos; y salir

**CA-02: Registro de gasto personal**
Dado que el usuario selecciona la opción de añadir gasto personal
Cuando introduce una cantidad válida (> 0), una fecha y una categoría existente o nueva
Entonces el gasto se crea y persiste en el sistema

**CA-03: Registro de gasto compartido**
Dado que el usuario selecciona la opción de añadir gasto compartido
Cuando elige una cuenta compartida, introduce los datos del gasto y selecciona la persona que paga
Entonces el gasto se registra con el pagador asignado

**CA-04: Edición con valor actual por defecto**
Dado que el usuario edita un gasto existente
Cuando pulsa Enter en un campo sin introducir valor
Entonces se conserva el valor anterior de ese campo

**CA-05: Eliminación con confirmación**
Dado que el usuario selecciona un gasto para eliminar
Cuando confirma la operación escribiendo "s"
Entonces el gasto se elimina de forma permanente
Cuando escribe cualquier otra cosa
Entonces la eliminación se cancela y el gasto permanece

**CA-06: Cancelación con "salir"**
Dado que el usuario está en cualquier prompt del CLI
Cuando escribe "salir"
Entonces la operación en curso se cancela y se vuelve al menú principal sin realizar cambios

**CA-07: Visualización de cuentas compartidas**
Dado que el usuario selecciona la opción de ver cuentas compartidas
Cuando elige una cuenta
Entonces ve el nombre, tipo de reparto, miembros, saldos de cada persona y la lista de gastos

**CA-08: Persistencia de datos**
Dado que el usuario realiza cualquier operación de modificación
Cuando la operación finaliza correctamente
Entonces los cambios se persisten automáticamente

**CA-09: Consistencia entre CLI e interfaz gráfica**
Dado que el usuario realiza una operación desde el CLI
Cuando abre la interfaz gráfica
Entonces los cambios realizados desde CLI se reflejan en la interfaz gráfica

---

## HU-07: Visualizar gastos en tabla

| Campo | Valor |
|-------|-------|
| **ID** | HU-07 |
| **Título** | Visualizar gastos en formato tabla/lista |
| **Descripción** | Como usuario, quiero ver todos mis gastos en una tabla ordenada para consultarlos rápidamente. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Visualización de columnas**
Dado que el usuario navega a la vista de lista de gastos
Cuando la pantalla se carga
Entonces ve una tabla con las columnas: cantidad, fecha, categoría y descripción

**CA-02: Actualización en tiempo real**
Dado que el usuario añade, edita o elimina un gasto
Cuando la operación se completa
Entonces la tabla se actualiza automáticamente reflejando el cambio

**CA-03: Ordenación**
Dado que el usuario hace clic en el encabezado de una columna
Cuando pulsa sobre ella
Entonces los gastos se ordenan ascendentemente o descendentemente por esa columna

---

## HU-08: Visualizar gastos en gráficos

| Campo | Valor |
|-------|-------|
| **ID** | HU-08 |
| **Título** | Ver distribución de gastos en gráficos |
| **Descripción** | Como usuario, quiero ver gráficos de barras y/o circulares que muestren la distribución de mis gastos por categorías para visualizar de forma clara dónde gasto mi dinero. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Representación gráfica**
Dado que el usuario navega a la vista de gráficos
Cuando se muestran los datos
Entonces ve un gráfico de barras o circular con la distribución de gastos por categoría

**CA-02: Interactividad**
Dado que el usuario pasa el ratón sobre una sección del gráfico
Cuando se detiene sobre ella
Entonces ve un tooltip con el valor exacto y el nombre de la categoría

**CA-03: Sincronización con filtros**
Dado que el usuario tiene filtros activos
Cuando los gráficos se renderizan
Entonces solo reflejan los gastos que cumplen los filtros

---

## HU-09: Visualizar gastos en calendario

| Campo | Valor |
|-------|-------|
| **ID** | HU-09 |
| **Título** | Ver gastos en un calendario |
| **Descripción** | Como usuario, quiero ver mis gastos representados en un calendario (CalendarFX, vista Full Day) para tener una perspectiva temporal. |
| **Prioridad** | Baja |

**Criterios de aceptación:**

**CA-01: Gastos en calendario**
Dado que el usuario navega a la vista de calendario
Cuando se carga la vista
Entonces los gastos aparecen situados en el día correspondiente

**CA-02: Consulta por día**
Dado que el usuario hace clic en un día concreto del calendario
Cuando selecciona ese día
Entonces se muestran los gastos correspondientes a esa fecha

---

## HU-10: Filtrar por meses

| Campo | Valor |
|-------|-------|
| **ID** | HU-10 |
| **Título** | Filtrar gastos por una lista de meses |
| **Descripción** | Como usuario, quiero filtrar los gastos seleccionando uno o varios meses del año para visualizar únicamente los gastos de un periodo concreto. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Selección múltiple de meses**
Dado que el usuario accede al panel de filtros
Cuando selecciona uno o varios meses
Entonces solo se muestran los gastos pertenecientes a los meses seleccionados

**CA-02: Deselección de meses**
Dado que el usuario desmarca todos los meses
Cuando no hay selección
Entonces se muestran todos los gastos (sin filtro de mes)

---

## HU-11: Filtrar por intervalo de fechas

| Campo | Valor |
|-------|-------|
| **ID** | HU-11 |
| **Título** | Filtrar gastos por intervalo de fechas personalizado |
| **Descripción** | Como usuario, quiero filtrar gastos entre dos fechas que yo elija para acotar la búsqueda a un intervalo personalizado. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Intervalo válido**
Dado que el usuario introduce una fecha de inicio y una fecha de fin
Cuando la fecha de inicio es anterior o igual a la fecha de fin
Entonces se muestran todos los gastos comprendidos en ese intervalo

**CA-02: Intervalo inválido**
Dado que el usuario introduce una fecha de inicio posterior a la fecha de fin
Cuando confirma el filtro
Entonces el sistema muestra un mensaje de error

---

## HU-12: Filtrar por categorías

| Campo | Valor |
|-------|-------|
| **ID** | HU-12 |
| **Título** | Filtrar gastos por una lista de categorías |
| **Descripción** | Como usuario, quiero filtrar los gastos por una o varias categorías para centrarme en un tipo de gasto concreto. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Selección de categorías**
Dado que el usuario accede al panel de filtros
Cuando selecciona una o varias categorías
Entonces solo se muestran los gastos de las categorías seleccionadas

**CA-02: Sin selección**
Dado que el usuario no selecciona ninguna categoría
Cuando no hay filtro activo
Entonces se muestran los gastos de todas las categorías

---

## HU-13: Combinar filtros

| Campo | Valor |
|-------|-------|
| **ID** | HU-13 |
| **Título** | Combinar varios filtros simultáneamente |
| **Descripción** | Como usuario, quiero combinar filtros de meses, fechas y categorías para realizar consultas más precisas (ej.: gastos de transporte en julio y agosto). |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Combinación AND**
Dado que el usuario activa filtros de meses, intervalo de fechas y categorías
Cuando todos están activos simultáneamente
Entonces solo se muestran los gastos que cumplen TODAS las condiciones (AND)

**CA-02: Actualización dinámica**
Dado que el usuario modifica cualquier filtro activo
Cuando cambia su valor
Entonces los resultados se actualizan automáticamente

---

## HU-14: Crear alerta de gasto semanal

| Campo | Valor |
|-------|-------|
| **ID** | HU-14 |
| **Título** | Configurar alerta semanal |
| **Descripción** | Como usuario, quiero establecer un límite de gasto semanal para recibir una notificación si lo supero. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Configuración de alerta semanal**
Dado que el usuario accede a la configuración de alertas
Cuando establece un límite en euros para la semana y guarda
Entonces la alerta queda registrada en el sistema

**CA-02: Superación del límite semanal**
Dado que existe una alerta semanal activa
Cuando el gasto acumulado en la semana supera el límite configurado
Entonces el sistema genera una notificación automáticamente

---

## HU-15: Crear alerta de gasto mensual

| Campo | Valor |
|-------|-------|
| **ID** | HU-15 |
| **Título** | Configurar alerta mensual |
| **Descripción** | Como usuario, quiero establecer un límite de gasto mensual para recibir una notificación si lo supero. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Configuración de alerta mensual**
Dado que el usuario accede a la configuración de alertas
Cuando establece un límite en euros para el mes y guarda
Entonces la alerta queda registrada en el sistema

**CA-02: Superación del límite mensual**
Dado que existe una alerta mensual activa
Cuando el gasto acumulado en el mes supera el límite configurado
Entonces el sistema genera una notificación automáticamente

---

## HU-16: Crear alerta de gasto por categoría

| Campo | Valor |
|-------|-------|
| **ID** | HU-16 |
| **Título** | Configurar alerta de gasto por categoría |
| **Descripción** | Como usuario, quiero establecer un límite de gasto para una categoría específica (ej.: 100 €/mes en videojuegos) para controlar el gasto en una categoría concreta. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Configuración de alerta por categoría**
Dado que el usuario accede a la configuración de alertas
Cuando selecciona una categoría y establece un límite semanal y/o mensual
Entonces la alerta queda registrada y vinculada a esa categoría

**CA-02: Superación del límite por categoría**
Dado que existe una alerta activa para una categoría específica
Cuando el gasto acumulado en esa categoría supera el límite en el periodo correspondiente
Entonces el sistema genera una notificación

---

## HU-17: Recibir notificación de alerta

| Campo | Valor |
|-------|-------|
| **ID** | HU-17 |
| **Título** | Recibir notificación al superar un límite |
| **Descripción** | Como usuario, quiero recibir una notificación cuando se supere el límite configurado en una alerta para estar informado de mis excesos de gasto. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Notificación en pantalla**
Dado que se ha superado el límite de gasto de una alerta
Cuando el sistema detecta la superación
Entonces muestra una notificación al usuario (pop-up o panel de notificaciones)

**CA-02: Almacenamiento en historial**
Dado que se ha generado una notificación
Cuando la notificación es creada
Entonces se almacena automáticamente en el historial de notificaciones

---

## HU-18: Consultar historial de notificaciones

| Campo | Valor |
|-------|-------|
| **ID** | HU-18 |
| **Título** | Revisar notificaciones pasadas |
| **Descripción** | Como usuario, quiero consultar en cualquier momento las notificaciones recibidas anteriormente para revisar el historial de alertas pasadas. |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Visualización del historial**
Dado que el usuario accede al historial de notificaciones
Cuando la vista se carga
Entonces ve todas las notificaciones generadas, ordenadas por fecha

**CA-02: Conservación de notificaciones**
Dado que el usuario ha recibido notificaciones en el pasado
Cuando consulta el historial tiempo después
Entonces las notificaciones antiguas siguen disponibles (no se eliminan automáticamente)

---

## HU-19: Crear cuenta de gasto compartida

| Campo | Valor |
|-------|-------|
| **ID** | HU-19 |
| **Título** | Crear una cuenta compartida con otras personas |
| **Descripción** | Como usuario, quiero crear una cuenta de gasto compartida añadiendo a otras personas (por nombre) para gestionar gastos en grupo. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Creación correcta**
Dado que el usuario accede a la opción de nueva cuenta compartida
Cuando introduce un nombre para la cuenta y una lista de personas
Entonces la cuenta se crea con reparto equitativo por defecto

**CA-02: Lista de integrantes fija**
Dado que una cuenta compartida ya ha sido creada
Cuando el usuario intenta modificar la lista de personas
Entonces el sistema impide cualquier cambio en los integrantes

---

## HU-20: Añadir gasto a cuenta compartida

| Campo | Valor |
|-------|-------|
| **ID** | HU-20 |
| **Título** | Registrar un gasto en una cuenta compartida |
| **Descripción** | Como usuario, quiero añadir un gasto a una cuenta compartida, asociándolo a la persona que lo ha pagado, para dividir el gasto entre los miembros del grupo. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Registro en cuenta compartida**
Dado que el usuario selecciona una cuenta compartida
Cuando introduce un gasto asociado a una persona concreta de la cuenta
Entonces el gasto se divide entre todos los miembros según el reparto configurado

**CA-02: Actualización de saldos**
Dado que se ha registrado un gasto en una cuenta compartida
Cuando el gasto se divide entre los miembros
Entonces los saldos de cada persona se actualizan automáticamente

---

## HU-21: Ver saldos de cuenta compartida

| Campo | Valor |
|-------|-------|
| **ID** | HU-21 |
| **Título** | Consultar saldos pendientes en una cuenta compartida |
| **Descripción** | Como usuario, quiero ver cuánto dinero se debe cada persona dentro de una cuenta compartida para saber quién tiene saldo pendiente con el grupo. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Visualización de saldos**
Dado que el usuario accede al detalle de una cuenta compartida
Cuando la vista se carga
Entonces ve el saldo de cada persona (positivo = le deben, negativo = debe)

**CA-02: Actualización tras gasto**
Dado que se añade un nuevo gasto a la cuenta
Cuando el gasto se procesa
Entonces los saldos mostrados se actualizan automáticamente

---

## HU-22: Configurar reparto porcentual en cuenta compartida

| Campo | Valor |
|-------|-------|
| **ID** | HU-22 |
| **Título** | Definir porcentajes de gasto en cuenta compartida |
| **Descripción** | Como usuario, quiero definir qué porcentaje del gasto asume cada persona en una cuenta compartida para personalizar el reparto (en lugar del reparto equitativo por defecto). |
| **Prioridad** | Media |

**Criterios de aceptación:**

**CA-01: Porcentajes válidos**
Dado que el usuario configura los porcentajes de reparto
Cuando la suma de todos los porcentajes es exactamente 100 %
Entonces el sistema guarda la configuración y la aplica a futuros gastos

**CA-02: Porcentajes inválidos**
Dado que el usuario configura los porcentajes de reparto
Cuando la suma de los porcentajes no es 100 %
Entonces el sistema muestra un mensaje de error y no guarda la configuración

---

## HU-23: Importar gastos desde fichero

| Campo | Valor |
|-------|-------|
| **ID** | HU-23 |
| **Título** | Importar gastos desde un fichero externo |
| **Descripción** | Como usuario, quiero importar un listado de gastos desde un fichero de texto (ej.: exportación de una plataforma bancaria) para incorporar gastos externos sin tener que introducirlos manualmente. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Importación correcta**
Dado que el usuario selecciona un fichero con formato soportado
Cuando el fichero tiene datos de gasto válidos
Entonces los gastos se incorporan a la aplicación y se muestran en la lista

**CA-02: Múltiples formatos**
Dado que el usuario intenta importar ficheros de distintos formatos
Cuando los ficheros tienen estructuras diferentes
Entonces el sistema es capaz de procesarlos correctamente

**CA-03: Formato no soportado**
Dado que el usuario selecciona un fichero con un formato no soportado
Cuando el sistema no reconoce la estructura
Entonces muestra un mensaje de error indicando que el formato no es válido

---

## HU-24: Gestionar gastos desde CLI (avanzado)

| Campo | Valor |
|-------|-------|
| **ID** | HU-24 |
| **Título** | Listar y filtrar gastos desde CLI |
| **Descripción** | Como usuario, quiero listar y filtrar gastos desde la línea de comandos por meses, rango de fechas y categorías para consultar mis gastos sin necesidad de abrir la interfaz gráfica. |
| **Prioridad** | Baja |

**Criterios de aceptación:**

**CA-01: Opción de filtrar en menú**
Dado que el usuario inicia la terminal
Cuando se carga el menú
Entonces ve la opción de filtrar

**CA-02: Filtro opcional por meses**
Dado que el usuario responde "s" a filtrar por meses
Cuando introduce números de mes separados por coma (ej: 1,7,8)
Entonces solo se muestran los gastos de los meses seleccionados
Cuando responde "N" o deja en blanco
Entonces no se aplica filtro de mes

**CA-03: Filtro opcional por rango de fechas**
Dado que el usuario responde "s" a filtrar por fechas
Cuando introduce una fecha de inicio anterior o igual a la fecha de fin
Entonces solo se muestran los gastos comprendidos en ese intervalo
Cuando la fecha de inicio es posterior a la de fin
Entonces el sistema muestra un mensaje de error y solicita de nuevo las fechas

**CA-04: Filtro opcional por categorías**
Dado que el usuario responde "s" a filtrar por categorías
Cuando selecciona una o varias categorías por número
Entonces solo se muestran los gastos de las categorías seleccionadas

**CA-05: Combinación de filtros (AND)**
Dado que el usuario activa varios filtros simultáneamente
Cuando confirma la selección
Entonces solo se muestran los gastos que cumplen todas las condiciones

**CA-06: Visualización de resultados**
Dado que el usuario completa la configuración de filtros
Cuando hay gastos que cumplen los criterios
Entonces se muestran en formato tabla con el número total de resultados, 
Cuando no hay gastos que cumplan los criterios
Entonces se muestra un mensaje indicando que no se encontraron resultados y Cuando no hay filtros aplicados
Entonces se muestran todos los gastos en la consola

---

## HU-25: Persistencia de datos

| Campo | Valor |
|-------|-------|
| **ID** | HU-25 |
| **Título** | Persistencia de los datos entre sesiones |
| **Descripción** | Como usuario, quiero que todos los datos introducidos se guarden automáticamente y estén disponibles al reiniciar la aplicación para no perder la información entre sesiones. |
| **Prioridad** | Alta |

**Criterios de aceptación:**

**CA-01: Persistencia completa**
Dado que el usuario introduce gastos, categorías, cuentas compartidas, alertas y notificaciones
Cuando cierra la aplicación y la vuelve a abrir
Entonces todos los datos están intactos y disponibles

**CA-02: Persistencia automática**
Dado que el usuario realiza cualquier operación de modificación de datos
Cuando la operación finaliza
Entonces los cambios se persisten automáticamente sin acción adicional del usuario

---

## Resumen de prioridades

| Prioridad | Total | IDs |
|-----------|-------|-----|
| **Alta** | 15 | HU-01, HU-02, HU-03, HU-04, HU-06, HU-07, HU-13, HU-14, HU-15, HU-17, HU-19, HU-20, HU-21, HU-23, HU-25 |
| **Media** | 8 | HU-05, HU-08, HU-10, HU-11, HU-12, HU-16, HU-18, HU-22 |
| **Baja** | 2 | HU-09, HU-24 |

---

## Requisitos No Funcionales

### RNF-01: Aplicación de escritorio

| Campo | Valor |
|-------|-------|
| **ID** | RNF-01 |
| **Descripción** | La aplicación debe desarrollarse como una aplicación de escritorio (no web ni móvil) utilizando JavaFX. |
| **Categoría** | Tecnología |

### RNF-02: Persistencia de la información

| Campo | Valor |
|-------|-------|
| **ID** | RNF-02 |
| **Descripción** | Todos los datos introducidos por el usuario deben persistirse en una fuente de datos. Se utilizará Jackson para el almacenamiento en formato JSON. |
| **Categoría** | Almacenamiento |

### RNF-03: Soporte de múltiples formatos de importación

| Campo | Valor |
|-------|-------|
| **ID** | RNF-03 |
| **Descripción** | El sistema debe ser capaz de importar datos de gasto desde distintos formatos de fichero. La arquitectura debe estar preparada para añadir nuevos formatos sin modificar el código existente. |
| **Categoría** | Arquitectura |

### RNF-04: Compatibilidad con librerías externas

| Campo | Valor |
|-------|-------|
| **ID** | RNF-04 |
| **Descripción** | La aplicación debe utilizar librerías externas para funcionalidades específicas: CalendarFX para la visualización en calendario y librerías de gráficos para representaciones de barras y circulares. |
| **Categoría** | Tecnología |

### RNF-05: Gestión de dependencias con Maven

| Campo | Valor |
|-------|-------|
| **ID** | RNF-05 |
| **Descripción** | Todas las dependencias de librerías externas deben gestionarse mediante Maven. |
| **Categoría** | Tecnología |

### RNF-06: Control de versiones con Git

| Campo | Valor |
|-------|-------|
| **ID** | RNF-06 |
| **Descripción** | El proyecto debe gestionarse mediante Git con un repositorio compartido en GitHub. |
| **Categoría** | Tecnología |

### RNF-07: Arquitectura MVC (Modelo-Vista-Controlador)

| Campo | Valor |
|-------|-------|
| **ID** | RNF-07 |
| **Descripción** | La aplicación debe seguir el patrón MVC con separación en capas: modelo de dominio (`domain/`), repositorios (`repository/` + `repository/impl/`), controladores (`controller/`) que contienen la lógica de negocio, y la interfaz de usuario (`ui/`) que incluye las vistas FXML. Los controladores deben ser reutilizables desde la interfaz gráfica y desde la CLI. |
| **Categoría** | Arquitectura |

### RNF-08: Inversión de dependencias

| Campo | Valor |
|-------|-------|
| **ID** | RNF-08 |
| **Descripción** | Las dependencias entre capas deben resolverse mediante inyección de dependencias a través de la clase singleton `Configuracion`. Los repositorios se definen como interfaces y se inyectan en los controladores, permitiendo cambiar la implementación sin modificar el código cliente. |
| **Categoría** | Arquitectura |

### RNF-09: Singleton en clases de acceso global

| Campo | Valor |
|-------|-------|
| **ID** | RNF-09 |
| **Descripción** | Las clases cuyo acceso a la única instancia en el sistema deba ser global (como `Configuracion` y `SceneManager`) deben implementar el patrón Singleton. |
| **Categoría** | Diseño |

### RNF-10: Patrón Repositorio

| Campo | Valor |
|-------|-------|
| **ID** | RNF-10 |
| **Descripción** | El acceso a los datos debe realizarse a través del patrón Repositorio, desacoplando la capa de almacenamiento del resto de la aplicación. Se usará Jackson para la persistencia en JSON. |
| **Categoría** | Arquitectura |
