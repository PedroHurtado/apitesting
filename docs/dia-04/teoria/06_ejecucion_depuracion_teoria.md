# Ejecución y Depuración de Planes de Prueba - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Ejecutar Test Suites y Test Cases en SoapUI
- Interpretar resultados de ejecución
- Utilizar herramientas de depuración
- Analizar logs y mensajes de error
- Generar reportes de ejecución
- Ejecutar tests desde línea de comandos

## Duración estimada

20 minutos

## Prerrequisitos

- Test Suites y Test Cases creados
- Comprensión de aserciones
- Familiaridad con la estructura de pruebas en SoapUI

## Contenido

### Modos de ejecución

SoapUI ofrece varios niveles de ejecución:

**1. Request individual**
```
Clic derecho en Request → Submit
O botón ▶ en la barra del request
```

**2. Test Step**
```
Clic derecho en Test Step → Run
Ejecuta solo ese paso específico
```

**3. Test Case**
```
Clic derecho en Test Case → Run
Ejecuta todos los Test Steps en secuencia
```

**4. Test Suite**
```
Clic derecho en Test Suite → Run
Ejecuta todos los Test Cases del suite
```

**5. Proyecto completo**
```
Clic derecho en Project → Run
Ejecuta todos los Test Suites y Test Cases
```

**Analogía simple:**

```
Ejecutar Request = Probar un ingrediente
Ejecutar Test Step = Probar un paso de receta
Ejecutar Test Case = Cocinar plato completo
Ejecutar Test Suite = Preparar menú completo
Ejecutar Project = Preparar banquete
```

### Interfaz de ejecución

Cuando ejecutas un Test Case o Test Suite, aparece una ventana con información en tiempo real:

**Panel de ejecución muestra:**

```
TestSuite Runner: Test Suite - Gestión de Usuarios
├── Progress: [████████░░] 80% Complete
├── Current: TC02 - Test con propiedades
├── Time: 00:05:23
├── Status: Running
└── Results:
    ├── Total: 10 tests
    ├── Passed: 8
    ├── Failed: 2
    └── Canceled: 0
```

### Resultados de ejecución

**Estados posibles de un Test Case:**

```
✓ PASS (verde): Todas las aserciones pasaron
❌ FAIL (rojo): Al menos una aserción falló
⚠ WARNING (amarillo): Advertencias pero no errores
○ UNKNOWN (gris): No ejecutado
⊗ CANCELED: Ejecución cancelada manualmente
```

### Panel TestCase Log

El **TestCase Log** muestra información detallada de la ejecución:

**Ejemplo de log de ejecución exitosa:**

```
14:23:45 INFO - Starting TestCase [TC01 - Crear Usuario]
14:23:45 INFO - Running TestCase Setup Script
14:23:46 INFO - Running TestStep [Paso 1 - POST /users]
14:23:46 INFO - Response: 201 Created (245 ms)
14:23:46 INFO - Assertion [Valid HTTP Status Codes] OK
14:23:46 INFO - Running TestStep [Paso 2 - Validar datos]
14:23:46 INFO - Script completed successfully
14:23:46 INFO - Running TestCase TearDown Script
14:23:46 INFO - TestCase [TC01] finished with status [FINISHED]
```

**Ejemplo de log con error:**

```
14:30:12 ERROR - Running TestStep [Obtener usuario]
14:30:12 ERROR - Response: 404 Not Found (123 ms)
14:30:12 ERROR - Assertion [Valid HTTP Status Codes] FAILED
14:30:12 ERROR - Expected: 200, Actual: 404
14:30:12 ERROR - TestCase [TC02] finished with status [FAILED]
```

### Niveles de logging

SoapUI soporta diferentes niveles de detalle en los logs:

```
1. ERROR: Solo errores críticos
2. WARN: Advertencias y errores
3. INFO: Información general (nivel recomendado)
4. DEBUG: Información detallada para debugging
5. TRACE: Máximo detalle (muy verboso)
```

**Configurar nivel de log:**
```
File → Preferences → UI Settings → Log Level
```

### Depuración de Test Steps

**Breakpoints (puntos de interrupción):**

```
1. Clic derecho en Test Step
2. Seleccionar "Set as Breakpoint"
3. El step se marca con ⊙
4. La ejecución se pausará antes de este step
```

**Uso de breakpoints:**

```
Test Case con breakpoint:
├── Step 1: POST /users (ejecuta)
├── Step 2: Extract ID (ejecuta)
├── ⊙ Step 3: GET /users/{id} (PAUSA AQUÍ)
└── Step 4: Validate (no ejecuta aún)
```

Permite inspeccionar propiedades y valores antes de continuar.

### Inspección de variables durante ejecución

Durante una pausa en breakpoint o después de la ejecución:

**Ver propiedades del Test Case:**

```groovy
// En Groovy Script Step o Assertion
testRunner.testCase.getPropertyNames().each { name ->
    log.info("${name} = " + testRunner.testCase.getPropertyValue(name))
}
```

**Inspeccionar respuesta:**

```groovy
// Ver respuesta completa
def response = messageExchange.response.contentAsString
log.info("Response: " + response)

// Ver headers
messageExchange.response.responseHeaders.each { name, value ->
    log.info("Header ${name}: ${value}")
}

// Ver tiempo de respuesta
log.info("Time: " + messageExchange.timeTaken + " ms")
```

### Análisis de fallos

Cuando un Test Case falla, SoapUI proporciona información para diagnosticar:

**1. Panel de Assertions**
```
❌ JSONPath Match - $.id
   Expected: 1
   Actual: 2
   Message: Value mismatch for path $.id
```

**2. Request/Response Raw**
```
Ver la petición enviada y respuesta recibida exactas
Útil para identificar problemas de formato
```

**3. TestCase Log**
```
Traza completa de la ejecución
Stack trace de excepciones si las hay
```

### Opciones de ejecución avanzadas

**Configuración de ejecución:**

```
Clic derecho en Test Case → Options

Opciones:
├── Abort on error: Detener si hay error
├── Fail on error: Marcar como fallido si hay error
├── Setup Script: Ejecutar antes del test
├── TearDown Script: Ejecutar después del test
└── Timeout: Tiempo máximo de ejecución
```

**Ejemplo de configuración:**

```
Test Case: Prueba Crítica
├── Abort on error: ☑ (detener inmediatamente)
├── Timeout: 30000 ms (30 segundos)
└── Fail on error: ☑ (marcar como fallido)
```

### Ejecución secuencial vs paralela

**Secuencial (por defecto):**
```
Test Case 1 → Test Case 2 → Test Case 3
(uno después del otro)
```

**Paralela (configuración avanzada):**
```
Test Case 1 ┐
Test Case 2 ├→ (simultáneamente)
Test Case 3 ┘
```

La ejecución paralela requiere configuración especial y cuidado con dependencias.

### Reportes de ejecución

SoapUI puede generar reportes en varios formatos:

**Reporte HTML básico:**

```
Clic derecho en Test Suite → Launch Test Runner
En la ventana:
├── Generate Report: ☑
├── Report Format: HTML
├── Output Folder: ./reports
└── Launch
```

**Contenido del reporte:**

```
Test Suite Report
├── Summary
│   ├── Total Tests: 10
│   ├── Passed: 8
│   ├── Failed: 2
│   └── Success Rate: 80%
├── Test Cases
│   ├── TC01 - Status: PASS
│   └── TC02 - Status: FAIL
│       └── Failed Assertion: JSONPath Match
└── Execution Time: 00:02:15
```

### Ejecución desde línea de comandos

SoapUI incluye **testrunner** para integración CI/CD:

**Sintaxis básica:**

```bash
# Windows
testrunner.bat -s"TestSuite" -c"TestCase" proyecto.xml

# Linux/Mac
testrunner.sh -s"TestSuite" -c"TestCase" proyecto.xml
```

**Ejemplo con opciones:**

```bash
testrunner.bat \
  -s"Test Suite - Gestión de Usuarios" \
  -c"TC01 - Crear Usuario" \
  -r \
  -f./reports \
  -PENV=production \
  proyecto-soapui.xml
```

**Parámetros comunes:**

```
-s: Test Suite a ejecutar
-c: Test Case específico
-r: Generar reporte
-f: Carpeta de reportes
-P: Propiedades (ej: -PbaseURL=http://...)
```

### Técnicas de depuración

**1. Log estratégico**

```groovy
// Al inicio de script
log.info("=== Iniciando validación ===")
log.info("UserID: " + testRunner.testCase.getPropertyValue("userId"))

// Durante procesamiento
log.info("Respuesta parseada exitosamente")

// Al final
log.info("=== Validación completada ===")
```

**2. Assertions temporales**

```
Añadir aserciones extra temporalmente para validar pasos intermedios
Deshabilitar después de resolver el problema
```

**3. Requests de prueba**

```
Duplicar request problemático
Simplificar paso a paso hasta aislar el problema
```

**4. Modo DEBUG**

```
Activar nivel de log DEBUG temporalmente
Analizar salida detallada
Volver a INFO después
```

### Buenas prácticas de ejecución

1. **Tests independientes**: Cada Test Case debe poder ejecutarse solo
2. **Cleanup en TearDown**: Limpiar datos de prueba creados
3. **Timeouts razonables**: Evitar esperas infinitas
4. **Logging significativo**: Facilita diagnóstico de problemas
5. **Reportes automatizados**: Generar para cada ejecución importante

**Ejemplo de estructura robusta:**

```
Test Case: Ciclo completo usuario
├── Setup Script
│   └── Generar datos únicos
├── Steps
│   ├── Crear usuario (con assertions)
│   ├── Verificar creación (con assertions)
│   └── Limpiar usuario
└── TearDown Script
    └── Verificar cleanup y log de resultado
```

## Referencias externas

- **Test Execution Guide**: https://www.soapui.org/functional-testing/running-tests/
- **Command Line Execution**: https://www.soapui.org/test-automation/running-from-command-line/
- **Continuous Integration**: https://www.soapui.org/test-automation/integrating-with-ci-cd/
- **Debugging Best Practices**: https://smartbear.com/learn/automated-testing/debugging-tests/
- **Test Reporting**: https://www.soapui.org/test-automation/test-reporting/

## Resumen

La ejecución y depuración efectiva de pruebas en SoapUI requiere comprender los diferentes niveles de ejecución, interpretar correctamente los logs y resultados, y utilizar las herramientas de depuración disponibles. Los breakpoints permiten pausar la ejecución para inspeccionar el estado, mientras que los logs detallados facilitan el diagnóstico de problemas. La generación de reportes y la ejecución desde línea de comandos son fundamentales para la integración con procesos de CI/CD. Una estrategia de depuración sistemática combinada con buenas prácticas de logging asegura la identificación rápida y resolución de problemas en las pruebas.
