# Ejecución y Depuración de Planes de Prueba - Laboratorio

## Objetivos

- Ejecutar Test Suites completos
- Analizar resultados de ejecución
- Utilizar breakpoints para depuración
- Interpretar logs y mensajes de error
- Generar reportes de ejecución

## Duración estimada

20 minutos

## Prerrequisitos

- Test Suite con varios Test Cases creados
- Al menos un Test Case con múltiples aserciones

## Ejercicio práctico: Ejecutar y depurar suite completo

### Paso 1: Preparar Test Case con logging (5 minutos)

1. Crea un nuevo Test Case:
   - **Name**: `TC04 - Test con logging detallado`
   
2. Añade un Groovy Script Step:
   - **Name**: `Inicialización con logging`
   - Código:

```groovy
// Logging de inicio
log.info("=====================================")
log.info("Iniciando Test Case: " + testRunner.testCase.name)
log.info("Test Suite: " + testRunner.testCase.testSuite.name)
log.info("Timestamp: " + new Date().toString())
log.info("=====================================")

// Generar y guardar datos de prueba
def userId = 5
testRunner.testCase.setPropertyValue("userId", userId.toString())
log.info("UserID configurado: " + userId)
```

3. Añade un REST Request Step:
   - **Name**: `Obtener usuario`
   - **Resource**: `/users/${#TestCase#userId}`
   - **Method**: GET

4. Añade otro Groovy Script Step:
   - **Name**: `Procesar y loggear respuesta`
   - Código:

```groovy
import groovy.json.JsonSlurper

// Obtener respuesta
def response = context.expand('${Obtener usuario#Response}')
log.info("Respuesta recibida:")
log.info(response)

// Parsear y loggear datos
def json = new JsonSlurper().parseText(response)
log.info("-------------------------------------")
log.info("Datos del usuario:")
log.info("  ID: " + json.id)
log.info("  Nombre: " + json.name)
log.info("  Email: " + json.email)
log.info("  Ciudad: " + json.address.city)
log.info("-------------------------------------")

// Guardar en propiedades
testRunner.testCase.setPropertyValue("userName", json.name)

log.info("✓ Procesamiento completado exitosamente")
```

### Paso 2: Ejecutar Test Case individual (3 minutos)

1. Haz clic derecho sobre **TC04 - Test con logging detallado**
2. Selecciona **Run**
3. Observa la ventana de ejecución que aparece
4. Espera a que termine la ejecución
5. Verifica el estado final: ✓ FINISHED

### Paso 3: Analizar el log (3 minutos)

1. En el panel inferior, localiza la pestaña **TestCase Log**
2. Busca tu información de logging
3. Deberías ver algo como:

```
INFO - =====================================
INFO - Iniciando Test Case: TC04 - Test con logging detallado
INFO - Test Suite: Test Suite - Gestión de Usuarios
INFO - Timestamp: Thu Nov 20 14:35:22 CET 2025
INFO - =====================================
INFO - UserID configurado: 5
INFO - Respuesta recibida:
INFO - {"id":5,"name":"Chelsey Dietrich",...}
INFO - -------------------------------------
INFO - Datos del usuario:
INFO -   ID: 5
INFO -   Nombre: Chelsey Dietrich
INFO -   Email: Lucio_Hettinger@annie.ca
INFO -   Ciudad: Roscoeview
INFO -   -------------------------------------
INFO - ✓ Procesamiento completado exitosamente
```

### Paso 4: Configurar y usar breakpoint (4 minutos)

1. En **TC04**, haz clic derecho sobre el step **Procesar y loggear respuesta**
2. Selecciona **Set as Breakpoint**
3. Verifica que aparece el símbolo ⊙ junto al step
4. Ejecuta el Test Case nuevamente
5. La ejecución se pausará antes del step con breakpoint
6. En el panel **TestCase Log**, verás:
   ```
   INFO - Paused at step [Procesar y loggear respuesta]
   ```
7. Haz clic en **Continue** (o F5) para continuar
8. Quita el breakpoint: clic derecho → **Clear Breakpoint**

### Paso 5: Ejecutar Test Suite completo (3 minutos)

1. Haz clic derecho sobre **Test Suite - Gestión de Usuarios**
2. Selecciona **Run**
3. Aparecerá la ventana **TestSuite Runner**
4. Observa cómo se ejecutan todos los Test Cases en secuencia:
   ```
   Running TestCase [TC01 - Consultar y validar usuario]
   Running TestCase [TC02 - Test con propiedades dinámicas]
   Running TestCase [TC03 - Validación con aserciones múltiples]
   Running TestCase [TC04 - Test con logging detallado]
   ```
5. Verifica el resumen al finalizar:
   ```
   Total TestCases: 4
   Passed: 4
   Failed: 0
   Time: 00:00:15
   ```

### Paso 6: Provocar un error intencional (2 minutos)

1. En **TC03 - Validación con aserciones múltiples**
2. Modifica el REST Request:
   - Cambia el resource de `/users/1` a `/users/99999`
3. Ejecuta solo este Test Case
4. Observa los errores en las aserciones:
   ```
   ❌ Valid HTTP Status Codes: Expected [200] but got [404]
   ❌ Contains: Content [Leanne Graham] not found
   ❌ JSONPath Match: Path [$.id] not found in response
   ```
5. Revierte el cambio a `/users/1`

### Resultados esperados

Al finalizar deberías:

✓ Haber ejecutado Test Cases individuales y Test Suite completo
✓ Comprender cómo leer el TestCase Log
✓ Saber usar breakpoints para pausar ejecución
✓ Poder identificar y analizar errores de aserciones

**Estado final del Test Suite:**
```
Test Suite - Gestión de Usuarios
├── TC01 - Consultar y validar usuario ✓ PASS
├── TC02 - Test con propiedades dinámicas ✓ PASS
├── TC03 - Validación con aserciones múltiples ✓ PASS
└── TC04 - Test con logging detallado ✓ PASS

Results: 4/4 PASSED (100%)
```

## Autoevaluación

**Pregunta 1:** ¿Cuál es la diferencia entre ejecutar un Test Step y ejecutar un Test Case?
<details>
<summary>Respuesta</summary>
Ejecutar un Test Step ejecuta solo ese paso individual, mientras que ejecutar un Test Case ejecuta todos los Test Steps del caso de prueba en secuencia, incluyendo los scripts de Setup y TearDown si existen.
</details>

**Pregunta 2:** ¿Para qué sirven los breakpoints en SoapUI?
<details>
<summary>Respuesta</summary>
Los breakpoints permiten pausar la ejecución de un Test Case justo antes de un Test Step específico, lo que facilita la depuración al poder inspeccionar el estado de propiedades, revisar logs y verificar valores antes de continuar con la ejecución.
</details>

**Pregunta 3:** ¿Dónde se puede ver información detallada sobre la ejecución de los tests?
<details>
<summary>Respuesta</summary>
En la pestaña "TestCase Log" del panel inferior de SoapUI, que muestra todos los mensajes de log (INFO, WARN, ERROR) generados durante la ejecución, incluyendo información de requests, responses, aserciones y scripts personalizados.
</details>

**Pregunta 4:** ¿Qué indica el estado FAILED en un Test Case?
<details>
<summary>Respuesta</summary>
Indica que al menos una aserción del Test Case falló durante la ejecución, es decir, que la respuesta no cumplió con los criterios de validación esperados. El log mostrará qué aserción específica falló y por qué.
</details>

## Ejercicio adicional (opcional)

Experimenta con niveles de logging:

1. Ve a **File → Preferences → UI Settings**
2. Cambia **Log Level** a **DEBUG**
3. Ejecuta un Test Case nuevamente
4. Observa la cantidad adicional de información en el log
5. Vuelve a cambiar a **INFO** para uso normal

## Notas importantes

- Los logs se acumulan en el panel TestCase Log (usa Clear para limpiar)
- F5 continúa ejecución después de un breakpoint
- Ctrl+C cancela una ejecución en progreso
- Los breakpoints se guardan con el proyecto
- El log puede exportarse para análisis posterior (clic derecho → Export)
- Usa log.info() liberalmente durante desarrollo, luego reduce para producción
