# Tipos de Pasos de Pruebas (Test Steps) - Laboratorio

## Objetivos

- Implementar diferentes tipos de Test Steps
- Crear un flujo con Conditional Goto
- Utilizar DataSource para data-driven testing
- Combinar múltiples tipos de steps en un escenario completo

## Duración estimada

20 minutos

## Prerrequisitos

- Test Suite existente
- Comprensión de los diferentes tipos de Test Steps
- Familiaridad con Groovy básico

## Ejercicio práctico: Flujo complejo con múltiples tipos de steps

### Paso 1: Crear Test Case con flujo condicional (8 minutos)

1. Crea un nuevo Test Case:
   - **Name**: `TC05 - Flujo condicional de búsqueda`

2. **Step 1** - Añade Groovy Script:
   - **Name**: `Configurar ID de búsqueda`
   - Código:
```groovy
// Configurar ID a buscar
def userId = 3
testRunner.testCase.setPropertyValue("searchUserId", userId.toString())
testRunner.testCase.setPropertyValue("maxRetries", "3")
testRunner.testCase.setPropertyValue("currentRetry", "0")

log.info("Configurado búsqueda de usuario ID: " + userId)
```

3. **Step 2** - Añade REST Request:
   - **Name**: `Buscar usuario`
   - **Resource**: `/users/${#TestCase#searchUserId}`
   - **Method**: GET

4. **Step 3** - Añade Groovy Script:
   - **Name**: `Verificar resultado`
   - Código:
```groovy
import groovy.json.JsonSlurper

// Obtener código de respuesta
def statusCode = context.expand('${Buscar usuario#ResponseStatusCode}')
log.info("Status Code recibido: " + statusCode)

// Decidir si fue exitoso
if (statusCode == "200") {
    testRunner.testCase.setPropertyValue("searchSuccess", "true")
    
    // Parsear y loggear datos
    def response = context.expand('${Buscar usuario#Response}')
    def json = new JsonSlurper().parseText(response)
    log.info("✓ Usuario encontrado: " + json.name)
} else {
    testRunner.testCase.setPropertyValue("searchSuccess", "false")
    
    // Incrementar contador de reintentos
    def currentRetry = testRunner.testCase.getPropertyValue("currentRetry") as Integer
    currentRetry++
    testRunner.testCase.setPropertyValue("currentRetry", currentRetry.toString())
    
    log.info("❌ Usuario no encontrado (intento ${currentRetry})")
}
```

5. **Step 4** - Añade Conditional Goto:
   - **Name**: `Verificar si debe reintentar`
   - **Target Step**: `Buscar usuario`
   - **Condition**: `${#TestCase#searchSuccess} == 'false'`

6. **Step 5** - Añade Delay:
   - **Name**: `Espera entre reintentos`
   - **Delay**: 1000 ms

7. **Step 6** - Añade Groovy Script:
   - **Name**: `Resultado final`
   - Código:
```groovy
def success = testRunner.testCase.getPropertyValue("searchSuccess")
def retries = testRunner.testCase.getPropertyValue("currentRetry")

log.info("=====================================")
log.info("Búsqueda finalizada")
log.info("  Éxito: " + success)
log.info("  Intentos realizados: " + retries)
log.info("=====================================")

assert success == "true" : "Búsqueda no exitosa después de reintentos"
```

8. **Ejecuta el Test Case** y observa el flujo

### Paso 2: Crear Test Case con DataSource (12 minutos)

1. Crea un nuevo Test Case:
   - **Name**: `TC06 - Data-driven testing con Grid`

2. **Step 1** - Añade DataSource:
   - **Name**: `Datos de usuarios`
   - **Type**: Selecciona **Grid**
   - Haz clic en **OK**
   
3. Configura el DataSource Grid:
   - En el editor del DataSource, añade columnas:
     - Columna 1: `userId`
     - Columna 2: `expectedName`
   - Añade filas de datos:
     ```
     userId | expectedName
     -------|------------------
     1      | Leanne Graham
     2      | Ervin Howell
     3      | Clementine Bauch
     5      | Chelsey Dietrich
     ```
   - Haz clic en el icono de **Guardar** (💾)

4. **Step 2** - Añade Groovy Script:
   - **Name**: `Leer datos del DataSource`
   - Código:
```groovy
// Leer datos de la fila actual
def userId = context.expand('${Datos de usuarios#userId}')
def expectedName = context.expand('${Datos de usuarios#expectedName}')

log.info("-----------------------------------")
log.info("Probando usuario ID: " + userId)
log.info("Nombre esperado: " + expectedName)

// Guardar en propiedades para uso en steps siguientes
testRunner.testCase.setPropertyValue("currentUserId", userId)
testRunner.testCase.setPropertyValue("expectedName", expectedName)
```

5. **Step 3** - Añade REST Request:
   - **Name**: `Obtener usuario del dataset`
   - **Resource**: `/users/${#TestCase#currentUserId}`
   - **Method**: GET

6. **Step 4** - Añade Groovy Script:
   - **Name**: `Validar contra dataset`
   - Código:
```groovy
import groovy.json.JsonSlurper

// Obtener datos esperados
def expectedName = testRunner.testCase.getPropertyValue("expectedName")
def userId = testRunner.testCase.getPropertyValue("currentUserId")

// Parsear respuesta
def response = context.expand('${Obtener usuario del dataset#Response}')
def json = new JsonSlurper().parseText(response)

// Validar
log.info("Nombre en respuesta: " + json.name)

assert json.name == expectedName : "Nombre no coincide para usuario ${userId}"
log.info("✓ Validación exitosa para usuario ${userId}")
log.info("-----------------------------------")
```

7. **Step 5** - Añade DataSource Loop:
   - Haz clic derecho en **Datos de usuarios** (el DataSource)
   - Selecciona **Add DataSource Loop**
   - **Target Step**: Selecciona `Leer datos del DataSource`
   - Haz clic en **OK**

8. **Ejecuta el Test Case** y observa cómo se ejecuta 4 veces (una por cada fila del dataset)

### Resultados esperados

Al finalizar deberías tener:

✓ Test Case con flujo condicional (TC05) funcionando
✓ Test Case con data-driven testing (TC06) ejecutándose 4 veces
✓ Comprensión de Conditional Goto y DataSource

**TC05 - Estructura:**
```
TC05 - Flujo condicional de búsqueda
├── Configurar ID de búsqueda [Groovy Script]
├── Buscar usuario [REST Request] ← (punto de retry)
├── Verificar resultado [Groovy Script]
├── Verificar si debe reintentar [Conditional Goto]
├── Espera entre reintentos [Delay]
└── Resultado final [Groovy Script]
```

**TC06 - Estructura:**
```
TC06 - Data-driven testing con Grid
├── Datos de usuarios [DataSource: 4 filas]
├── Leer datos del DataSource [Groovy Script] ← (inicio loop)
├── Obtener usuario del dataset [REST Request]
├── Validar contra dataset [Groovy Script]
└── DataSource Loop → volver a "Leer datos"
```

**Salida esperada TC06:**
```
-----------------------------------
Probando usuario ID: 1
Nombre esperado: Leanne Graham
Nombre en respuesta: Leanne Graham
✓ Validación exitosa para usuario 1
-----------------------------------
Probando usuario ID: 2
Nombre esperado: Ervin Howell
Nombre en respuesta: Ervin Howell
✓ Validación exitosa para usuario 2
-----------------------------------
[...continúa para usuarios 3 y 5]
```

## Autoevaluación

**Pregunta 1:** ¿Qué hace el Conditional Goto Test Step?
<details>
<summary>Respuesta</summary>
Conditional Goto permite implementar lógica condicional en el flujo de Test Steps. Evalúa una condición y, si es verdadera, salta a un Test Step específico en lugar de continuar con el siguiente step en secuencia. Es útil para implementar loops, retry logic y flujos con múltiples caminos.
</details>

**Pregunta 2:** ¿Para qué sirve el DataSource Test Step?
<details>
<summary>Respuesta</summary>
DataSource permite implementar data-driven testing al leer datos de fuentes externas (Excel, CSV, XML, Grid, bases de datos) y ejecutar los mismos test steps con diferentes conjuntos de datos. Esto facilita probar múltiples escenarios sin duplicar Test Cases.
</details>

**Pregunta 3:** ¿Cuál es la diferencia entre Run TestCase y Groovy Script?
<details>
<summary>Respuesta</summary>
Run TestCase ejecuta otro Test Case completo como sub-rutina (con todos sus steps, setup y teardown), mientras que Groovy Script ejecuta código Groovy personalizado dentro del Test Case actual. Run TestCase es para reutilización de flujos completos, Groovy Script es para lógica personalizada puntual.
</details>

**Pregunta 4:** ¿Cuándo usarías un Delay step?
<details>
<summary>Respuesta</summary>
Delay se usa para pausar la ejecución durante un tiempo específico. Es útil cuando se necesita esperar procesamiento asíncrono, respetar rate limits de APIs, simular comportamiento humano realista, o dar tiempo a sistemas externos para completar operaciones.
</details>

## Ejercicio adicional (opcional)

Modifica el TC06 para incluir un caso de error:

1. Añade una fila en el DataSource:
   ```
   userId: 99999
   expectedName: No Existe
   ```
2. Modifica el script de validación para manejar el caso 404
3. Ejecuta y observa cómo se comporta con el dato inválido

## Notas importantes

- DataSource Loop requiere que el DataSource esté antes en la secuencia
- Conditional Goto solo puede saltar hacia adelante o hacia atrás dentro del mismo Test Case
- Los Test Steps dentro de un loop tienen acceso a los datos de la fila actual
- Usar demasiados Conditional Goto puede hacer el flujo difícil de mantener
- DataSource con muchas filas puede hacer las pruebas lentas
