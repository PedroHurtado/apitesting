# Creación de Planes de Pruebas (Test Suites) - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Comprender qué es un Test Suite y su propósito
- Crear y organizar Test Suites en SoapUI
- Configurar propiedades y opciones de un Test Suite
- Aplicar estrategias de organización para pruebas complejas
- Entender la relación entre Test Suites, Test Cases y Test Steps

## Duración estimada

20 minutos

## Prerrequisitos

- Tener SoapUI instalado
- Comprender la interfaz básica de SoapUI
- Tener un proyecto REST o SOAP creado

## Contenido

### ¿Qué es un Test Suite?

Un **Test Suite** (plan de pruebas) es un contenedor lógico que agrupa múltiples Test Cases relacionados. Representa un conjunto coherente de pruebas que validan un aspecto específico o funcionalidad completa de un servicio web.

**Analogía simple:**

```
Test Suite = Examen completo de matemáticas
  └── Test Case 1 = Problemas de álgebra
  └── Test Case 2 = Problemas de geometría
  └── Test Case 3 = Problemas de cálculo
```

### Estructura jerárquica en SoapUI

SoapUI organiza las pruebas en una jerarquía de tres niveles:

```
Proyecto
├── Test Suite 1: Pruebas de Usuarios
│   ├── Test Case 1: Crear Usuario
│   │   ├── Test Step 1: POST /users
│   │   └── Test Step 2: Validar respuesta
│   └── Test Case 2: Obtener Usuario
│       ├── Test Step 1: GET /users/{id}
│       └── Test Step 2: Verificar datos
└── Test Suite 2: Pruebas de Autenticación
    └── Test Case 1: Login válido
        └── Test Step 1: POST /login
```

### Propósito de los Test Suites

Los Test Suites permiten:

1. **Organización**: Agrupar casos de prueba por funcionalidad o módulo
2. **Ejecución conjunta**: Ejecutar múltiples test cases en secuencia
3. **Reporting**: Generar informes consolidados de resultados
4. **Mantenimiento**: Facilitar la gestión de pruebas complejas
5. **Reutilización**: Compartir configuraciones entre test cases

**Ejemplo de organización:**

```
API de Comercio Electrónico
├── Test Suite: Gestión de Productos
│   ├── Listar productos
│   ├── Crear producto
│   ├── Actualizar producto
│   └── Eliminar producto
├── Test Suite: Gestión de Pedidos
│   ├── Crear pedido
│   ├── Consultar pedido
│   └── Cancelar pedido
└── Test Suite: Autenticación
    ├── Login
    └── Logout
```

### Creación de un Test Suite

**Método 1: Desde el menú contextual**

```
1. Clic derecho sobre el proyecto
2. Seleccionar "New TestSuite"
3. Introducir nombre descriptivo
```

**Método 2: Desde la barra de menú**

```
Project → Add New TestSuite
```

**Ejemplo de nombres descriptivos:**

- ✓ `Test Suite: API Usuarios - Funcionalidad CRUD`
- ✓ `Pruebas de Integración - Servicio Pagos`
- ✗ `TestSuite1` (poco descriptivo)
- ✗ `Pruebas` (demasiado genérico)

### Propiedades de un Test Suite

Cada Test Suite tiene propiedades configurables:

**1. Name (Nombre)**
- Identificador del Test Suite
- Debe ser descriptivo y único

**2. Description (Descripción)**
- Documentación del propósito
- Ámbito de las pruebas incluidas

**Ejemplo:**
```
Nombre: Test Suite - API Usuarios
Descripción: Pruebas funcionales completas del módulo de gestión
de usuarios, incluyendo operaciones CRUD, validaciones de datos
y manejo de errores.
```

**3. Setup Script**
- Código Groovy que se ejecuta antes del Test Suite
- Útil para inicialización global

**Ejemplo simple:**
```groovy
// Configurar timestamp para el test suite
testSuite.setPropertyValue("inicio", new Date().toString())
log.info("Iniciando Test Suite: " + testSuite.name)
```

**4. TearDown Script**
- Código Groovy que se ejecuta después del Test Suite
- Útil para limpieza de recursos

**Ejemplo simple:**
```groovy
// Registrar fin del test suite
log.info("Finalizando Test Suite: " + testSuite.name)
testSuite.setPropertyValue("fin", new Date().toString())
```

### Configuración de ejecución

**Opciones de ejecución de Test Suite:**

- **Fail on Error**: Detener ejecución si un test case falla
- **Abort on Error**: Cancelar completamente si hay error
- **Timeout**: Tiempo máximo de ejecución (ms)

**Ejemplo de configuración:**

```
Test Suite: Pruebas Críticas
  ├── Fail on Error: ☑ Activado (detener si falla)
  ├── Timeout: 300000 ms (5 minutos)
  └── Keep Session: ☑ (mantener sesión HTTP)
```

### Estrategias de organización

**Estrategia 1: Por funcionalidad**

```
├── Test Suite: Autenticación
├── Test Suite: Gestión Usuarios
├── Test Suite: Gestión Productos
└── Test Suite: Carrito de Compra
```

**Estrategia 2: Por tipo de prueba**

```
├── Test Suite: Pruebas Funcionales
├── Test Suite: Pruebas de Validación
├── Test Suite: Pruebas de Seguridad
└── Test Suite: Pruebas de Rendimiento
```

**Estrategia 3: Por criticidad**

```
├── Test Suite: Smoke Tests (pruebas de humo)
├── Test Suite: Regresión Crítica
└── Test Suite: Regresión Completa
```

### Buenas prácticas

1. **Nombres significativos**: Usa nombres que describan claramente el propósito
2. **Granularidad adecuada**: Agrupa test cases relacionados lógicamente
3. **Documentación**: Incluye descripciones detalladas
4. **Scripts de setup/teardown**: Usa para preparación y limpieza común
5. **Independencia**: Los test suites deben poder ejecutarse independientemente
6. **Orden lógico**: Organiza test cases en orden de dependencia

**Ejemplo de mala práctica:**

```
❌ Test Suite sin nombre descriptivo
❌ Mezclar pruebas de módulos diferentes
❌ Dependencias ocultas entre test suites
❌ Sin documentación del propósito
```

**Ejemplo de buena práctica:**

```
✓ Test Suite: API Usuarios - CRUD Completo
  Descripción: Validación de operaciones Create, Read,
  Update y Delete sobre el recurso /users
✓ Test cases independientes y autocontenidos
✓ Setup script para datos de prueba
✓ TearDown script para limpieza
```

### Ejecución de Test Suites

Un Test Suite se puede ejecutar de varias formas:

1. **Manualmente**: Clic derecho → Run
2. **Desde línea de comandos**: testrunner.bat/sh
3. **Integración CI/CD**: Maven, Jenkins, etc.

**Ejemplo de ejecución manual:**

```
1. Clic derecho en el Test Suite
2. Seleccionar "Run"
3. Observar progreso en "Test Suite Log"
4. Revisar resultados al finalizar
```

## Referencias externas

- **SoapUI Test Suite Documentation**: https://www.soapui.org/functional-testing/structuring-and-running-tests/
- **Best Practices for Test Organization**: https://www.soapui.org/docs/functional-testing/best-practices/
- **Groovy Scripting in SoapUI**: https://www.soapui.org/scripting-properties/scripting/
- **Test Automation Framework Design**: https://smartbear.com/learn/automated-testing/test-automation-frameworks/

## Resumen

Los Test Suites son contenedores fundamentales en SoapUI que permiten organizar y gestionar conjuntos de pruebas relacionadas. Una estructura bien organizada de Test Suites facilita el mantenimiento, ejecución y reporte de resultados. Utilizar nombres descriptivos, documentación adecuada y scripts de setup/teardown son prácticas esenciales para crear Test Suites efectivos. La estrategia de organización debe adaptarse a las necesidades del proyecto, ya sea por funcionalidad, tipo de prueba o criticidad.
