# Creación de Planes de Pruebas (Test Suites) - Laboratorio

## Objetivos

- Crear un Test Suite organizado para una API
- Configurar propiedades del Test Suite
- Implementar scripts de setup y teardown básicos
- Comprender la estructura jerárquica de pruebas

## Duración estimada

20 minutos

## Prerrequisitos

- Proyecto REST de JSONPlaceholder creado en el laboratorio anterior
- SoapUI abierto con el proyecto cargado

## Ejercicio práctico: Organizar pruebas de la API JSONPlaceholder

### Paso 1: Crear un Test Suite (5 minutos)

1. En el proyecto `JSONPlaceholder API`, haz clic derecho sobre el nombre del proyecto
2. Selecciona **New TestSuite**
3. Configura el Test Suite:
   - **Name**: `Test Suite - Gestión de Usuarios`
   - Haz clic en **OK**
4. El nuevo Test Suite aparecerá en el navegador de proyectos

### Paso 2: Configurar propiedades del Test Suite (5 minutos)

1. Haz doble clic en el Test Suite recién creado
2. En el panel central, accede a la pestaña **Properties**
3. Añade una propiedad personalizada:
   - Haz clic en el botón **➕ (Add)**
   - **Name**: `baseURL`
   - **Value**: `https://jsonplaceholder.typicode.com`
   - Haz clic en **OK**
4. Añade otra propiedad:
   - **Name**: `testEnvironment`
   - **Value**: `Development`

### Paso 3: Configurar Setup Script (5 minutos)

1. En el editor del Test Suite, selecciona la pestaña **Setup Script**
2. Introduce el siguiente código Groovy:

```groovy
// Script de inicialización del Test Suite
log.info("=================================")
log.info("Iniciando: " + testSuite.name)
log.info("Entorno: " + testSuite.getPropertyValue("testEnvironment"))
log.info("Fecha: " + new Date().toString())
log.info("=================================")

// Establecer timestamp de inicio
testSuite.setPropertyValue("startTime", System.currentTimeMillis().toString())
```

3. Guarda el proyecto (**File → Save Project**)

### Paso 4: Configurar TearDown Script (3 minutos)

1. Selecciona la pestaña **TearDown Script**
2. Introduce el siguiente código:

```groovy
// Script de finalización del Test Suite
def startTime = testSuite.getPropertyValue("startTime") as Long
def endTime = System.currentTimeMillis()
def duration = (endTime - startTime) / 1000

log.info("=================================")
log.info("Finalizando: " + testSuite.name)
log.info("Duración: " + duration + " segundos")
log.info("=================================")
```

3. Guarda el proyecto

### Paso 5: Verificar configuración (2 minutos)

1. Haz clic derecho sobre el Test Suite
2. Selecciona **Show TestSuite Editor**
3. Verifica que puedes ver:
   - ✓ Nombre del Test Suite
   - ✓ Pestaña Properties con las propiedades creadas
   - ✓ Setup Script con el código de inicialización
   - ✓ TearDown Script con el código de finalización

### Resultados esperados

Al finalizar este ejercicio deberías tener:

✓ Un Test Suite llamado "Test Suite - Gestión de Usuarios"
✓ Dos propiedades personalizadas (baseURL y testEnvironment)
✓ Setup Script que registra información de inicio
✓ TearDown Script que calcula la duración de ejecución
✓ Estructura lista para añadir Test Cases

**Estructura resultante:**
```
JSONPlaceholder API
└── Test Suite - Gestión de Usuarios
    ├── Properties
    │   ├── baseURL = https://jsonplaceholder.typicode.com
    │   └── testEnvironment = Development
    ├── Setup Script (configurado)
    └── TearDown Script (configurado)
```

## Autoevaluación

**Pregunta 1:** ¿Qué propósito tiene un Test Suite en SoapUI?
<details>
<summary>Respuesta</summary>
Un Test Suite agrupa múltiples Test Cases relacionados en un contenedor lógico, permitiendo organizarlos, ejecutarlos conjuntamente y generar informes consolidados. Representa un conjunto coherente de pruebas que validan un aspecto específico del servicio.
</details>

**Pregunta 2:** ¿Cuándo se ejecuta el Setup Script de un Test Suite?
<details>
<summary>Respuesta</summary>
El Setup Script se ejecuta ANTES de que se ejecuten todos los Test Cases contenidos en el Test Suite. Es útil para inicialización, configuración de variables globales o preparación del entorno de pruebas.
</details>

**Pregunta 3:** ¿Para qué sirven las propiedades personalizadas en un Test Suite?
<details>
<summary>Respuesta</summary>
Las propiedades personalizadas permiten almacenar valores que pueden ser reutilizados por todos los Test Cases dentro del Test Suite, como URLs base, credenciales de prueba, configuraciones de entorno, etc. Facilitan la configuración centralizada y el mantenimiento.
</details>

**Pregunta 4:** ¿Qué diferencia hay entre un Test Suite y un Test Case?
<details>
<summary>Respuesta</summary>
Un Test Suite es un contenedor que agrupa múltiples Test Cases relacionados. Un Test Case es un escenario de prueba específico que contiene uno o más Test Steps (pasos de prueba). La jerarquía es: Proyecto → Test Suite → Test Case → Test Step.
</details>

## Ejercicio adicional (opcional)

Crea un segundo Test Suite para pruebas de posts:

1. Crea un nuevo Test Suite llamado "Test Suite - Gestión de Posts"
2. Añade propiedades similares (baseURL, testEnvironment)
3. Modifica los scripts para identificar este Test Suite específicamente
4. Observa cómo puedes tener múltiples Test Suites organizados en el mismo proyecto

## Notas importantes

- Los Test Suites pueden ejecutarse independientemente
- Las propiedades definidas en el Test Suite están disponibles para todos sus Test Cases
- Los scripts en Groovy permiten lógica personalizada
- Puedes copiar/duplicar Test Suites para crear variantes
