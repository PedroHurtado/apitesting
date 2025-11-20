# Creación/Edición de Test Cases y Test Steps - Laboratorio

## Objetivos

- Crear un Test Case completo con múltiples Test Steps
- Configurar REST Request Steps
- Implementar Groovy Script Steps
- Usar Property Transfer para compartir datos entre steps
- Ejecutar y validar el flujo completo

## Duración estimada

25 minutos

## Prerrequisitos

- Test Suite "Test Suite - Gestión de Usuarios" creado en laboratorio anterior
- SoapUI abierto con el proyecto JSONPlaceholder cargado

## Ejercicio práctico: Crear Test Case de ciclo de vida de usuario

### Paso 1: Crear el Test Case (3 minutos)

1. Haz clic derecho sobre **Test Suite - Gestión de Usuarios**
2. Selecciona **New TestCase**
3. Configura:
   - **Name**: `TC01 - Consultar y validar usuario`
   - **Description**: `Obtiene la lista de usuarios y valida el primer usuario`
4. Haz clic en **OK**

### Paso 2: Añadir primer Test Step - REST Request (5 minutos)

1. Haz clic derecho sobre el Test Case recién creado
2. Selecciona **Add Step → REST Request**
3. Configura:
   - **Step Name**: `Paso 1 - Obtener lista usuarios`
4. En el editor del step:
   - **Service**: Crea nuevo servicio con URI `https://jsonplaceholder.typicode.com`
   - **Resource**: `/users`
   - **Method**: GET
5. Haz clic en el botón **▶ (Submit)** para probar
6. Verifica que obtienes respuesta con código 200

### Paso 3: Añadir Groovy Script Step (5 minutos)

1. Clic derecho en el Test Case → **Add Step → Groovy Script**
2. Configura:
   - **Step Name**: `Paso 2 - Extraer primer usuario`
3. En el editor de script, escribe:

```groovy
// Obtener la respuesta del paso anterior
def response = context.expand('${Paso 1 - Obtener lista usuarios#Response}')

// Parsear JSON
import groovy.json.JsonSlurper
def jsonSlurper = new JsonSlurper()
def usuarios = jsonSlurper.parseText(response)

// Extraer datos del primer usuario
def primerUsuario = usuarios[0]
def userId = primerUsuario.id
def userName = primerUsuario.name
def userEmail = primerUsuario.email

// Guardar en propiedades del Test Case
testRunner.testCase.setPropertyValue("userId", userId.toString())
testRunner.testCase.setPropertyValue("userName", userName)
testRunner.testCase.setPropertyValue("userEmail", userEmail)

// Log para verificar
log.info("Usuario extraído - ID: ${userId}, Nombre: ${userName}")
```

4. Guarda el proyecto

### Paso 4: Añadir segundo REST Request (5 minutos)

1. Clic derecho en el Test Case → **Add Step → REST Request**
2. Configura:
   - **Step Name**: `Paso 3 - Obtener usuario específico`
3. En el editor:
   - **Service**: Usa el servicio existente de JSONPlaceholder
   - **Resource**: `/users/{id}`
   - **Method**: GET
4. En la sección de **Request Parameters**:
   - Encuentra el parámetro `id` en la tabla
   - En la columna **Value**, introduce: `${#TestCase#userId}`
   - Esto usará el ID extraído en el paso anterior
5. Haz clic en **▶ (Submit)** para probar
6. Deberías obtener los datos del primer usuario

### Paso 5: Añadir Groovy Script de validación (5 minutos)

1. Clic derecho en el Test Case → **Add Step → Groovy Script**
2. Configura:
   - **Step Name**: `Paso 4 - Validar respuesta`
3. En el editor, escribe:

```groovy
// Obtener respuesta del paso anterior
def response = context.expand('${Paso 3 - Obtener usuario específico#Response}')

// Parsear JSON
import groovy.json.JsonSlurper
def jsonSlurper = new JsonSlurper()
def usuario = jsonSlurper.parseText(response)

// Obtener valores esperados
def expectedName = testRunner.testCase.getPropertyValue("userName")
def expectedEmail = testRunner.testCase.getPropertyValue("userEmail")

// Validar
assert usuario.name == expectedName : "El nombre no coincide"
assert usuario.email == expectedEmail : "El email no coincide"

log.info("✓ Validación exitosa - Usuario: ${usuario.name}")
```

4. Guarda el proyecto

### Paso 6: Ejecutar el Test Case completo (2 minutos)

1. Haz clic derecho sobre el Test Case **TC01 - Consultar y validar usuario**
2. Selecciona **Run**
3. Observa la ejecución en el panel **TestCase Log**
4. Verifica que todos los steps se ejecutan con éxito (iconos verdes ✓)

### Resultados esperados

Al finalizar deberías tener:

✓ Un Test Case con 4 Test Steps configurados
✓ Flujo completo: Obtener lista → Extraer datos → Consultar individual → Validar
✓ Ejecución exitosa del Test Case completo
✓ Logs mostrando información de cada paso

**Estructura del Test Case:**
```
TC01 - Consultar y validar usuario
├── Paso 1 - Obtener lista usuarios [REST Request GET /users]
├── Paso 2 - Extraer primer usuario [Groovy Script]
├── Paso 3 - Obtener usuario específico [REST Request GET /users/{id}]
└── Paso 4 - Validar respuesta [Groovy Script]
```

**Salida esperada en el log:**
```
INFO: Usuario extraído - ID: 1, Nombre: Leanne Graham
INFO: ✓ Validación exitosa - Usuario: Leanne Graham
```

## Autoevaluación

**Pregunta 1:** ¿Qué es un Test Step en SoapUI?
<details>
<summary>Respuesta</summary>
Un Test Step es la unidad mínima de ejecución en SoapUI. Representa una acción específica como enviar una petición HTTP, ejecutar código Groovy, validar una respuesta, o transferir datos entre pasos.
</details>

**Pregunta 2:** ¿Cómo se accede a una propiedad del Test Case desde un Groovy Script?
<details>
<summary>Respuesta</summary>
Se usa: `testRunner.testCase.getPropertyValue("nombrePropiedad")` para leer y `testRunner.testCase.setPropertyValue("nombrePropiedad", valor)` para escribir.
</details>

**Pregunta 3:** ¿En qué orden se ejecutan los Test Steps?
<details>
<summary>Respuesta</summary>
Los Test Steps se ejecutan secuencialmente de arriba hacia abajo, en el orden en que aparecen en el Test Case, a menos que se use un Conditional Goto que altere el flujo.
</details>

**Pregunta 4:** ¿Para qué sirve la sintaxis `${#TestCase#nombrePropiedad}`?
<details>
<summary>Respuesta</summary>
Es una expansión de propiedad que permite referenciar valores almacenados en las propiedades del Test Case. SoapUI reemplaza esta expresión con el valor real de la propiedad en tiempo de ejecución.
</details>

## Ejercicio adicional (opcional)

Añade un Test Step de tipo **Delay**:

1. Entre el Paso 2 y el Paso 3, añade **Add Step → Delay**
2. Configura un delay de 1000 ms (1 segundo)
3. Ejecuta el Test Case nuevamente
4. Observa cómo la ejecución se pausa en ese punto

## Notas importantes

- Los Test Steps se pueden reordenar arrastrándolos
- Usa nombres descriptivos para facilitar el mantenimiento
- El contexto de Groovy (`context`) da acceso a respuestas de steps anteriores
- Puedes deshabilitar steps temporalmente (clic derecho → Disable)
- Las propiedades del Test Case permiten compartir datos entre steps
