# Tipos de Aserciones - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Comprender qué son las aserciones y su importancia
- Identificar los diferentes tipos de aserciones en SoapUI
- Configurar aserciones para validar respuestas REST/SOAP
- Seleccionar la aserción apropiada según el caso de uso
- Implementar validaciones complejas combinando múltiples aserciones

## Duración estimada

20 minutos

## Prerrequisitos

- Comprender REST/SOAP requests y responses
- Familiaridad con JSON y XML
- Conocimientos básicos de HTTP status codes

## Contenido

### ¿Qué son las aserciones?

Una **aserción** (assertion) es una validación que verifica si la respuesta de un servicio cumple con criterios esperados. Las aserciones son el corazón del testing automatizado, ya que determinan si una prueba pasa o falla.

**Analogía simple:**

```
Aserción = Control de calidad en fábrica
  ├── Criterio: "El producto debe pesar entre 500g y 510g"
  ├── Validación: Pesar el producto
  └── Resultado: PASS (dentro del rango) o FAIL (fuera del rango)
```

### Importancia de las aserciones

Sin aserciones, un test solo verifica que el servicio responde, pero no valida que la respuesta sea correcta:

**Sin aserciones:**
```
Request: GET /users/1
Response: Recibida (cualquier contenido)
Resultado: ✓ PASS (simplemente respondió)
```

**Con aserciones:**
```
Request: GET /users/1
Aserciones:
  ✓ Status code = 200
  ✓ Response contiene campo "id"
  ✓ Campo "id" = 1
  ✓ Response time < 1000ms
Resultado: ✓ PASS (todas las validaciones correctas)
```

### Categorías de aserciones en SoapUI

SoapUI organiza las aserciones en varias categorías:

```
Aserciones
├── Property Content (Contenido)
│   ├── Contains
│   ├── Not Contains
│   ├── XPath Match
│   └── JSONPath Match
├── Compliance, Status and Standards (Conformidad)
│   ├── Valid HTTP Status Codes
│   ├── Invalid HTTP Status Codes
│   └── Schema Compliance
├── Script (Personalizadas)
│   └── Script Assertion
└── SLA (Rendimiento)
    └── Response SLA
```

### Tipos de aserciones más utilizados

#### 1. Valid HTTP Status Codes

Valida que el código de estado HTTP esté dentro de un rango esperado.

**Ejemplo:**
```
Assertion: Valid HTTP Status Codes
Códigos válidos: 200, 201
Si respuesta = 200 → PASS
Si respuesta = 404 → FAIL
```

**Casos de uso:**
- Verificar operación exitosa (200-299)
- Validar error esperado (400-499)

#### 2. Contains / Not Contains

Verifica si la respuesta contiene (o no contiene) un texto específico.

**Ejemplo con Contains:**
```
Assertion: Contains
Content: "Leanne Graham"
Response: {"id": 1, "name": "Leanne Graham", ...}
Resultado: PASS (contiene el texto)
```

**Ejemplo con Not Contains:**
```
Assertion: Not Contains
Content: "error"
Response: {"id": 1, "name": "Leanne Graham"}
Resultado: PASS (no contiene "error")
```

**Casos de uso:**
- Verificar presencia de datos específicos
- Asegurar ausencia de mensajes de error

#### 3. JSONPath Match

Valida valores específicos en la respuesta JSON usando expresiones JSONPath.

**Ejemplo:**
```
Assertion: JSONPath Match
JSONPath: $.id
Expected Value: 1

Response:
{
  "id": 1,
  "name": "Leanne Graham",
  "email": "Sincere@april.biz"
}

Resultado: PASS ($.id es 1)
```

**Expresiones JSONPath comunes:**

```
$.id              → Campo id en raíz
$.address.city    → Campo city dentro de address
$[0].id          → Campo id del primer elemento de array
$..email         → Todos los campos email en cualquier nivel
$.length()       → Longitud del array
```

**Casos de uso:**
- Validar campos específicos del JSON
- Verificar estructuras anidadas
- Validar arrays

#### 4. XPath Match

Similar a JSONPath, pero para respuestas XML (servicios SOAP).

**Ejemplo:**
```
Assertion: XPath Match
XPath: //id[1]
Expected Value: 1

Response XML:
<user>
  <id>1</id>
  <name>Leanne Graham</name>
</user>

Resultado: PASS
```

#### 5. Response SLA

Valida que el tiempo de respuesta esté dentro de un límite aceptable.

**Ejemplo:**
```
Assertion: Response SLA
Max time: 1000ms

Tiempo real de respuesta: 245ms
Resultado: PASS

Tiempo real de respuesta: 1523ms
Resultado: FAIL
```

**Casos de uso:**
- Pruebas de rendimiento
- Validar SLAs contractuales
- Detectar degradación de performance

#### 6. Schema Compliance

Valida que la respuesta cumple con un esquema JSON o XML definido.

**Ejemplo con JSON Schema:**
```
Assertion: JsonSchema Compliance
Schema:
{
  "type": "object",
  "required": ["id", "name", "email"],
  "properties": {
    "id": {"type": "number"},
    "name": {"type": "string"},
    "email": {"type": "string", "format": "email"}
  }
}
```

**Casos de uso:**
- Validar contratos de API
- Asegurar estructura correcta
- Detectar cambios no autorizados en API

#### 7. Script Assertion

Permite validaciones personalizadas usando Groovy.

**Ejemplo simple:**
```groovy
import groovy.json.JsonSlurper

// Parsear respuesta JSON
def response = messageExchange.response.contentAsString
def json = new JsonSlurper().parseText(response)

// Validación personalizada
assert json.id > 0 : "ID debe ser mayor que 0"
assert json.name.length() > 0 : "Nombre no puede estar vacío"
assert json.email.contains("@") : "Email debe contener @"
```

**Casos de uso:**
- Validaciones complejas con lógica personalizada
- Comparaciones entre múltiples campos
- Validaciones que requieren cálculos

### Configuración de aserciones

**Añadir una aserción:**

```
1. Seleccionar el REST/SOAP Request Step
2. En el panel inferior, ir a pestaña "Assertions"
3. Clic en botón ➕ (Add Assertion)
4. Seleccionar tipo de aserción
5. Configurar parámetros
```

**Ejemplo de configuración paso a paso:**

```
Test Step: GET /users/1

Aserción 1: Valid HTTP Status Codes
  └── Codes: 200

Aserción 2: JSONPath Match
  ├── JSONPath: $.id
  └── Expected: 1

Aserción 3: Response SLA
  └── Max Time: 1000
```

### Combinación de aserciones

Un test robusto usa múltiples aserciones para validación completa:

**Ejemplo de validación exhaustiva:**

```
Request: POST /users
Body: {"name": "Test User", "email": "test@example.com"}

Aserciones:
✓ Valid HTTP Status Codes: 201 (creación exitosa)
✓ Contains: "id" (respuesta incluye ID asignado)
✓ JSONPath Match: $.name → "Test User" (nombre correcto)
✓ JSONPath Match: $.email → "test@example.com" (email correcto)
✓ Response SLA: < 2000ms (rendimiento aceptable)
✓ Not Contains: "error" (sin mensajes de error)
```

### Estrategias de validación

**Nivel 1: Validación básica (smoke test)**
```
✓ Status code correcto
✓ Respuesta no vacía
```

**Nivel 2: Validación funcional**
```
✓ Status code correcto
✓ Campos obligatorios presentes
✓ Valores de campos correctos
```

**Nivel 3: Validación exhaustiva**
```
✓ Status code correcto
✓ Schema compliance
✓ Todos los campos validados
✓ Relaciones de datos correctas
✓ Performance dentro de SLA
```

### Aserciones para diferentes escenarios

**Caso exitoso (Happy Path):**
```
GET /users/1
✓ Status: 200
✓ JSONPath $.id: 1
✓ Contains: "name"
```

**Caso de error esperado:**
```
GET /users/99999
✓ Status: 404
✓ Contains: "not found"
✓ Response time: < 500ms
```

**Caso de validación de datos:**
```
POST /users
✓ Status: 201
✓ JSONPath $.id: [any number]
✓ JSONPath $.email: [valid email format]
✓ Schema compliance
```

### Buenas prácticas

1. **Validar siempre el status code**: Primera línea de defensa
2. **Aserciones específicas**: Validar campos críticos individualmente
3. **Validar estructura**: Usar schema compliance para APIs nuevas
4. **Mensajes descriptivos**: Especialmente en script assertions
5. **Balance**: No sobre-validar (lento) ni sub-validar (poco confiable)

**Ejemplo de mala práctica:**
```
❌ Solo validar status code
❌ Usar solo "Contains" con textos genéricos
❌ No validar performance
```

**Ejemplo de buena práctica:**
```
✓ Status code + campos clave
✓ JSONPath para validaciones precisas
✓ Response SLA en endpoints críticos
✓ Schema compliance en cambios de API
```

### Depuración de aserciones fallidas

Cuando una aserción falla, SoapUI proporciona información detallada:

```
FAILED Assertion: JSONPath Match
JSONPath: $.id
Expected: 1
Actual: 2
Message: JSONPath match failed for path [$.id], expecting [1], actual was [2]
```

## Referencias externas

- **SoapUI Assertions Guide**: https://www.soapui.org/functional-testing/assertion-teststep/
- **JSONPath Syntax**: https://goessner.net/articles/JsonPath/
- **XPath Tutorial**: https://www.w3schools.com/xml/xpath_intro.asp
- **JSON Schema**: https://json-schema.org/
- **HTTP Status Codes**: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status

## Resumen

Las aserciones son el componente crítico que transforma requests simples en tests automatizados efectivos. SoapUI ofrece múltiples tipos de aserciones organizadas por categorías: contenido, conformidad, performance y personalizadas. La selección y combinación apropiada de aserciones según el escenario de prueba es fundamental para crear tests confiables. JSONPath Match y Valid HTTP Status Codes son las aserciones más utilizadas para servicios REST, mientras que Script Assertions permiten validaciones personalizadas complejas.
