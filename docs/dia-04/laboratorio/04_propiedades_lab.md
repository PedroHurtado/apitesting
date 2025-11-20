# Propiedades de un Test Case - Laboratorio

## Objetivos

- Crear propiedades en diferentes niveles (Project, TestSuite, TestCase)
- Utilizar expansión de propiedades en REST requests
- Implementar Property Transfer para extraer datos
- Crear propiedades dinámicas con Groovy

## Duración estimada

20 minutos

## Prerrequisitos

- Test Suite y Test Case del laboratorio anterior
- SoapUI abierto con el proyecto JSONPlaceholder

## Ejercicio práctico: Sistema de propiedades multinivel

### Paso 1: Crear propiedades a nivel Project (3 minutos)

1. Haz clic derecho sobre el proyecto **JSONPlaceholder API**
2. Selecciona **Show Project View**
3. En el panel inferior, localiza la pestaña **Custom Properties**
4. Haz clic en **➕ (Add)**
5. Crea las siguientes propiedades:

```
Name: baseURL
Value: https://jsonplaceholder.typicode.com

Name: apiName
Value: JSONPlaceholder

Name: defaultTimeout
Value: 5000
```

6. Guarda el proyecto

### Paso 2: Crear propiedades a nivel TestSuite (2 minutos)

1. Haz doble clic en **Test Suite - Gestión de Usuarios**
2. Ve a la pestaña **Properties**
3. Añade una nueva propiedad:

```
Name: testEnvironment
Value: Development
```

### Paso 3: Crear nuevo Test Case con propiedades (5 minutos)

1. Crea un nuevo Test Case en el Test Suite:
   - **Name**: `TC02 - Test con propiedades dinámicas`
2. En el nuevo Test Case, ve a la pestaña **Properties**
3. Añade propiedades:

```
Name: userId
Value: 5

Name: expectedUsername
Value: Kamren
```

### Paso 4: Crear REST Request usando expansión de propiedades (5 minutos)

1. En el Test Case **TC02**, añade un Test Step:
   - **Add Step → REST Request**
   - **Name**: `Obtener usuario por ID`
2. Configura el request usando propiedades:
   - **Service**: Selecciona el servicio JSONPlaceholder existente
   - **Resource**: `/users/{id}`
   - **Method**: GET
3. En la tabla de **Template Parameters**:
   - Para el parámetro `id`, usa: `${#TestCase#userId}`
4. En la **Request URL** verás algo como:
   ```
   ${#Project#baseURL}/users/${#TestCase#userId}
   ```
5. Ejecuta el request (▶) y verifica que obtiene el usuario con ID 5

### Paso 5: Añadir Property Transfer (5 minutos)

1. Añade un nuevo Test Step:
   - **Add Step → Property Transfer**
   - **Name**: `Extraer datos de usuario`
2. Configura el Property Transfer:
   - Haz clic en el botón **➕** en la parte superior
   - **Name**: `Transferir username`
   - **Source**: `Obtener usuario por ID`
   - **Source Property**: Selecciona **ResponseAsXml**
   - En el campo **Path Language**: Cambia a `JSONPath`
   - En **Property Path**: Escribe `$.username`
   - **Target**: `TC02 - Test con propiedades dinámicas`
   - **Target Property**: `actualUsername`
3. Haz clic en el botón **Test** para verificar la extracción
4. Deberías ver el valor extraído: `Kamren`

### Paso 6: Añadir Groovy Script de validación (3 minutos)

1. Añade un Test Step → **Groovy Script**
2. **Name**: `Validar username extraído`
3. Código:

```groovy
// Obtener valores de propiedades
def expected = testRunner.testCase.getPropertyValue("expectedUsername")
def actual = testRunner.testCase.getPropertyValue("actualUsername")

// Log de información
log.info("=================================")
log.info("API: " + context.expand('${#Project#apiName}'))
log.info("Entorno: " + context.expand('${#TestSuite#testEnvironment}'))
log.info("User ID probado: " + context.expand('${#TestCase#userId}'))
log.info("Username esperado: " + expected)
log.info("Username obtenido: " + actual)
log.info("=================================")

// Validar
assert actual == expected : "Username no coincide. Esperado: ${expected}, Obtenido: ${actual}"

log.info("✓ Validación exitosa!")
```

4. Guarda el proyecto

### Paso 7: Ejecutar y verificar (2 minutos)

1. Ejecuta el Test Case completo (clic derecho → Run)
2. Observa el log en el panel inferior
3. Deberías ver la información de las propiedades expandidas

### Resultados esperados

Al finalizar deberías tener:

✓ Propiedades en tres niveles (Project, TestSuite, TestCase)
✓ Request usando expansión de propiedades
✓ Property Transfer extrayendo datos de la respuesta
✓ Validación usando propiedades

**Estructura del Test Case:**
```
TC02 - Test con propiedades dinámicas
├── Properties:
│   ├── userId = 5
│   ├── expectedUsername = Kamren
│   └── actualUsername = (extraído dinámicamente)
├── Obtener usuario por ID [REST Request]
├── Extraer datos de usuario [Property Transfer]
└── Validar username extraído [Groovy Script]
```

**Salida esperada en log:**
```
=================================
API: JSONPlaceholder
Entorno: Development
User ID probado: 5
Username esperado: Kamren
Username obtenido: Kamren
=================================
✓ Validación exitosa!
```

## Autoevaluación

**Pregunta 1:** ¿Cuál es la diferencia entre propiedades de Project y TestCase?
<details>
<summary>Respuesta</summary>
Las propiedades de Project están disponibles para todo el proyecto (todos los Test Suites y Test Cases), mientras que las propiedades de TestCase solo están disponibles dentro de ese Test Case específico. Las de Project son útiles para configuración global como URLs base, mientras que las de TestCase son para datos específicos del escenario.
</details>

**Pregunta 2:** ¿Qué sintaxis se usa para expandir una propiedad de TestCase en un request?
<details>
<summary>Respuesta</summary>
Se usa la sintaxis `${#TestCase#nombrePropiedad}`. Por ejemplo: `${#TestCase#userId}` expande al valor de la propiedad "userId" del Test Case actual.
</details>

**Pregunta 3:** ¿Para qué sirve el Property Transfer step?
<details>
<summary>Respuesta</summary>
Property Transfer permite extraer datos de una respuesta (usando JSONPath o XPath) y guardarlos como propiedades para su uso en steps posteriores. Es fundamental para crear flujos donde un request depende de datos obtenidos en requests anteriores.
</details>

**Pregunta 4:** ¿Cómo se accede a una propiedad desde Groovy?
<details>
<summary>Respuesta</summary>
Se usa `testRunner.testCase.getPropertyValue("nombrePropiedad")` para leer y `testRunner.testCase.setPropertyValue("nombrePropiedad", valor)` para escribir. También se puede usar `context.expand('${#TestCase#nombrePropiedad}')` para expandir.
</details>

## Ejercicio adicional (opcional)

Modifica el Test Case para probar con diferentes usuarios:

1. Cambia la propiedad `userId` a `3`
2. Cambia `expectedUsername` a `Samantha`
3. Ejecuta nuevamente el Test Case
4. Observa cómo el mismo flujo funciona con datos diferentes

## Notas importantes

- Las propiedades son case-sensitive
- La expansión ocurre en tiempo de ejecución
- Property Transfer requiere que el step fuente se haya ejecutado antes
- JSONPath usa la notación `$.campo` para acceder a campos JSON
- Las propiedades se pueden usar en cualquier campo de texto de SoapUI
