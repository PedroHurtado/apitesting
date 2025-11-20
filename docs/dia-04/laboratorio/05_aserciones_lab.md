# Tipos de Aserciones - Laboratorio

## Objetivos

- Configurar aserciones de diferentes tipos
- Validar status codes HTTP
- Usar JSONPath Match para validar campos específicos
- Implementar validaciones de rendimiento
- Crear una Script Assertion personalizada

## Duración estimada

25 minutos

## Prerrequisitos

- Test Suite "Test Suite - Gestión de Usuarios" existente
- Familiaridad con la estructura JSON de JSONPlaceholder

## Ejercicio práctico: Validación completa con múltiples aserciones

### Paso 1: Crear Test Case para aserciones (3 minutos)

1. En el **Test Suite - Gestión de Usuarios**, crea un nuevo Test Case:
   - **Name**: `TC03 - Validación con aserciones múltiples`
   - **Description**: `Prueba exhaustiva con diferentes tipos de aserciones`

### Paso 2: Crear REST Request (3 minutos)

1. Añade un Test Step → **REST Request**
   - **Name**: `Obtener usuario específico`
2. Configura:
   - **Service**: Servicio JSONPlaceholder existente
   - **Resource**: `/users/1`
   - **Method**: GET
3. Ejecuta el request para verificar que funciona
4. Observa la respuesta JSON

### Paso 3: Añadir aserción de Status Code (3 minutos)

1. Con el Test Step seleccionado, ve a la pestaña **Assertions** (panel inferior)
2. Haz clic en **➕ (Add Assertion)**
3. Selecciona **Compliance, Status and Standards → Valid HTTP Status Codes**
4. En el diálogo de configuración:
   - Marca el checkbox **200**
   - Haz clic en **OK**
5. Verifica que aparece una aserción verde ✓ llamada "Valid HTTP Status Codes"

### Paso 4: Añadir aserciones Contains (4 minutos)

1. Añade nueva aserción → **Property Content → Contains**
2. Configura:
   - **Content**: `Leanne Graham`
   - **Ignore case**: Desmarcar
3. Haz clic en **OK**
4. Añade otra aserción Contains:
   - **Content**: `"id"`
5. Añade una aserción **Not Contains**:
   - **Content**: `error`

### Paso 5: Añadir aserciones JSONPath Match (5 minutos)

1. Añade aserción → **Property Content → JSONPath Match**
2. Configura la primera JSONPath:
   - **JSONPath Expression**: `$.id`
   - **Expected Result**: `1`
3. Haz clic en **Select from current** para probar
4. Verifica que muestra "Match!" y haz clic en **Save**

5. Añade más aserciones JSONPath:

**Aserción 2:**
```
JSONPath: $.name
Expected: Leanne Graham
```

**Aserción 3:**
```
JSONPath: $.email
Expected: Sincere@april.biz
```

**Aserción 4:**
```
JSONPath: $.address.city
Expected: Gwenborough
```

### Paso 6: Añadir aserción de Performance (3 minutos)

1. Añade aserción → **SLA → Response SLA**
2. Configura:
   - **Response Time**: `1000` (milisegundos)
3. Haz clic en **OK**
4. Esta aserción fallará si la respuesta tarda más de 1 segundo

### Paso 7: Añadir Script Assertion (4 minutos)

1. Añade aserción → **Script → Script Assertion**
2. En el editor de Groovy, escribe:

```groovy
import groovy.json.JsonSlurper

// Obtener y parsear la respuesta
def response = messageExchange.response.contentAsString
def json = new JsonSlurper().parseText(response)

// Validaciones personalizadas
assert json.id != null, "ID no puede ser null"
assert json.id instanceof Integer, "ID debe ser un número entero"
assert json.id > 0, "ID debe ser mayor que 0"

assert json.name != null && json.name.length() > 0, "Nombre no puede estar vacío"

assert json.email.contains("@"), "Email debe contener @"
assert json.email.contains("."), "Email debe contener punto"

assert json.username != null, "Username es obligatorio"
assert json.username.length() >= 3, "Username debe tener al menos 3 caracteres"

log.info("✓ Todas las validaciones personalizadas pasaron correctamente")
```

3. Haz clic en **OK**

### Paso 8: Ejecutar y validar todas las aserciones (3 minutos)

1. Haz clic en el botón **▶ (Submit Request)** del Test Step
2. Observa la pestaña **Assertions**
3. Verifica que todas las aserciones muestran ✓ verde:
   - Valid HTTP Status Codes
   - Contains (3 aserciones)
   - JSONPath Match (4 aserciones)
   - Response SLA
   - Script Assertion

### Paso 9: Probar fallo de aserción (2 minutos)

1. Modifica el resource a `/users/99999` (usuario inexistente)
2. Ejecuta el request
3. Observa que varias aserciones fallan:
   - ❌ Valid HTTP Status Codes (esperaba 200, obtuvo 404)
   - ❌ Contains "Leanne Graham" (no está en la respuesta)
   - ❌ JSONPath Match (campos no existen)
4. Revierte el cambio a `/users/1`

### Resultados esperados

Al finalizar deberías tener:

✓ Un Test Case con múltiples tipos de aserciones configuradas
✓ 11 aserciones en total funcionando correctamente
✓ Validación completa de status, contenido, estructura y performance

**Resumen de aserciones configuradas:**
```
Obtener usuario específico [REST Request /users/1]
└── Assertions (11 total):
    ├── Valid HTTP Status Codes: 200
    ├── Contains: "Leanne Graham"
    ├── Contains: "id"
    ├── Not Contains: "error"
    ├── JSONPath Match: $.id = 1
    ├── JSONPath Match: $.name = "Leanne Graham"
    ├── JSONPath Match: $.email = "Sincere@april.biz"
    ├── JSONPath Match: $.address.city = "Gwenborough"
    ├── Response SLA: < 1000ms
    └── Script Assertion: Validaciones personalizadas (3)
```

**Panel de Assertions mostrará:**
```
✓ Valid HTTP Status Codes - 200
✓ Contains - Leanne Graham
✓ Contains - "id"
✓ Not Contains - error
✓ JSONPath Match - $.id[1]
✓ JSONPath Match - $.name[Leanne Graham]
✓ JSONPath Match - $.email[Sincere@april.biz]
✓ JSONPath Match - $.address.city[Gwenborough]
✓ Response SLA - 245ms
✓ Script Assertion
```

## Autoevaluación

**Pregunta 1:** ¿Qué diferencia hay entre Contains y JSONPath Match?
<details>
<summary>Respuesta</summary>
Contains busca texto en cualquier parte de la respuesta (búsqueda simple de cadena), mientras que JSONPath Match extrae un valor específico de la estructura JSON usando una ruta y lo compara con un valor esperado. JSONPath es más preciso y específico.
</details>

**Pregunta 2:** ¿Cuándo debería usar una Script Assertion en lugar de aserciones estándar?
<details>
<summary>Respuesta</summary>
Script Assertion se usa cuando necesitas validaciones complejas que no pueden expresarse con aserciones estándar: validaciones condicionales, comparaciones entre múltiples campos, cálculos, validaciones de formatos personalizados, o lógica de negocio específica.
</details>

**Pregunta 3:** ¿Qué indica un icono rojo ❌ en una aserción?
<details>
<summary>Respuesta</summary>
Indica que la aserción ha fallado: la respuesta no cumple con los criterios definidos. Esto causa que el Test Step completo se marque como FAILED y puede detener la ejecución del Test Case si está configurado con "Fail on Error".
</details>

**Pregunta 4:** ¿Para qué sirve Response SLA assertion?
<details>
<summary>Respuesta</summary>
Valida que el tiempo de respuesta del servicio está dentro de un límite aceptable (SLA = Service Level Agreement). Es fundamental para pruebas de rendimiento y para asegurar que el servicio cumple con los requisitos de performance.
</details>

## Ejercicio adicional (opcional)

Modifica el Test Case para probar un endpoint diferente:

1. Cambia el resource a `/posts/1`
2. Ajusta las aserciones JSONPath para validar:
   - `$.userId = 1`
   - `$.id = 1`
   - `$.title` contiene texto
   - `$.body` contiene texto
3. Ejecuta y verifica

## Notas importantes

- Las aserciones se ejecutan después de recibir la respuesta
- Si una aserción falla, las siguientes aún se ejecutan (se pueden ver todos los errores)
- JSONPath es case-sensitive
- Response SLA puede variar según la velocidad de la red
- Script Assertions son más lentas que aserciones estándar
- Puedes deshabilitar aserciones temporalmente (clic derecho → Disable)
