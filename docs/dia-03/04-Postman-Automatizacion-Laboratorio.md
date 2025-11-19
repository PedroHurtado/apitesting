# Laboratorio: Automatización de Pruebas en Postman

## Objetivos del Laboratorio

Al finalizar este laboratorio, el alumno será capaz de:

- Crear y organizar colecciones de pruebas automatizadas
- Implementar flujos de trabajo utilizando variables y scripts
- Ejecutar baterías de pruebas con Collection Runner
- Utilizar Newman para ejecutar pruebas desde línea de comandos
- Implementar data-driven testing con archivos de datos

## Duración Estimada

120 minutos

## Requisitos Previos

- Tener instalado Postman
- Tener instalado Node.js (para Newman)
- Completar los laboratorios de "Preparando el entorno", "Probando" y "Secuencias de comandos"
- Conocimientos básicos de línea de comandos

## Recursos Necesarios

- API pública: JSONPlaceholder (https://jsonplaceholder.typicode.com)
- Editor de texto para crear archivos de datos

## Parte 1: Creación de Colección Automatizada (30 minutos)

### Ejercicio 1.1: Crear una Colección Completa

**Objetivo**: Crear una colección organizada para gestión de posts y usuarios.

**Pasos**:

1. Crear nueva colección:
   - Clic en "New" > "Collection"
   - Nombre: "API Testing Automation Lab"
   - Descripción: "Colección para automatización de pruebas completas de la API JSONPlaceholder"

2. Crear carpetas dentro de la colección:
   - Carpeta 1: "Setup"
   - Carpeta 2: "Users Management"
   - Carpeta 3: "Posts Management"
   - Carpeta 4: "Cleanup"

3. En la carpeta "Setup", crear petición:
   - **Nombre**: "Get Base URL Status"
   - **Método**: GET
   - **URL**: `https://jsonplaceholder.typicode.com/posts/1`
   - **Test Script**:
   ```javascript
   pm.test("API está disponible", function() {
       pm.response.to.have.status(200);
   });
   
   pm.test("Response es JSON", function() {
       pm.response.to.be.json;
   });
   
   console.log("✓ API inicializada correctamente");
   ```

4. Guardar la petición

**Resultado Esperado**:
- Colección creada con estructura organizada en carpetas
- Petición de verificación funcional
- Console muestra mensaje de inicialización

### Ejercicio 1.2: Configurar Variables de Colección

**Objetivo**: Definir variables reutilizables para toda la colección.

**Pasos**:

1. Seleccionar la colección "API Testing Automation Lab"

2. Ir a la pestaña "Variables"

3. Agregar las siguientes variables:
   - `baseURL` = `https://jsonplaceholder.typicode.com`
   - `testUserId` = (dejar vacío, se asignará en ejecución)
   - `testPostId` = (dejar vacío, se asignará en ejecución)
   - `testTitle` = `Automated Test Post`
   - `timestamp` = (dejar vacío, se generará dinámicamente)

4. Clic en "Save"

5. Modificar la petición "Get Base URL Status":
   - Cambiar URL a: `{{baseURL}}/posts/1`
   - Guardar

6. Ejecutar para verificar que funciona con la variable

**Resultado Esperado**:
- Variables definidas en la colección
- Petición utilizando la variable `{{baseURL}}`
- Petición ejecutada exitosamente

## Parte 2: Flujo Automatizado de Usuarios (35 minutos)

### Ejercicio 2.1: Flujo Completo de Usuario

**Objetivo**: Crear un flujo automatizado que obtiene, crea, verifica y lista usuarios.

**Paso 1 - Crear petición "Get Random User"** (carpeta "Users Management"):

- **Método**: GET
- **URL**: `{{baseURL}}/users/5`
- **Test Script**:
```javascript
pm.test("Usuario obtenido exitosamente", function() {
    pm.response.to.have.status(200);
});

pm.test("Usuario tiene datos completos", function() {
    const user = pm.response.json();
    pm.expect(user).to.have.property('id');
    pm.expect(user).to.have.property('name');
    pm.expect(user).to.have.property('email');
    pm.expect(user).to.have.property('username');
});

// Guardar ID de usuario de referencia
const user = pm.response.json();
pm.collectionVariables.set("referenceUserId", user.id);
console.log("Usuario de referencia ID:", user.id);
```

**Paso 2 - Crear petición "Create New User"** (carpeta "Users Management"):

- **Método**: POST
- **URL**: `{{baseURL}}/users`
- **Headers**: `Content-Type: application/json`
- **Body (raw JSON)**:
```json
{
    "name": "Test User Automated",
    "username": "testuser_{{$timestamp}}",
    "email": "testuser{{$randomInt}}@automation.test",
    "address": {
        "street": "Test Street",
        "city": "Test City"
    }
}
```
- **Test Script**:
```javascript
pm.test("Usuario creado exitosamente", function() {
    pm.response.to.have.status(201);
});

pm.test("Respuesta contiene datos del nuevo usuario", function() {
    const user = pm.response.json();
    pm.expect(user).to.have.property('id');
    pm.collectionVariables.set("testUserId", user.id);
});

const newUser = pm.response.json();
console.log("Nuevo usuario creado con ID:", newUser.id);
```

**Paso 3 - Crear petición "Get Created User"** (carpeta "Users Management"):

- **Método**: GET
- **URL**: `{{baseURL}}/users/{{testUserId}}`
- **Test Script**:
```javascript
pm.test("Usuario recuperado correctamente", function() {
    pm.response.to.have.status(200);
});

pm.test("ID coincide con el creado", function() {
    const userId = pm.response.json().id;
    const expectedId = pm.collectionVariables.get("testUserId");
    pm.expect(userId.toString()).to.eql(expectedId.toString());
});

console.log("Usuario verificado exitosamente");
```

**Paso 4 - Crear petición "List All Users"** (carpeta "Users Management"):

- **Método**: GET
- **URL**: `{{baseURL}}/users`
- **Test Script**:
```javascript
pm.test("Lista de usuarios obtenida", function() {
    pm.response.to.have.status(200);
});

pm.test("Respuesta es un array", function() {
    const users = pm.response.json();
    pm.expect(users).to.be.an('array');
    pm.expect(users.length).to.be.greaterThan(0);
});

pm.test("Usuarios tienen estructura correcta", function() {
    const users = pm.response.json();
    users.forEach(user => {
        pm.expect(user).to.have.property('id');
        pm.expect(user).to.have.property('name');
        pm.expect(user).to.have.property('email');
    });
});

console.log("Total de usuarios:", pm.response.json().length);
```

**Resultado Esperado**:
- 4 peticiones creadas en secuencia lógica
- Variables `referenceUserId` y `testUserId` se asignan dinámicamente
- Cada petición valida y pasa datos a la siguiente

### Ejercicio 2.2: Flujo de Posts Vinculado a Usuario

**Objetivo**: Crear posts asociados al usuario creado anteriormente.

**Paso 1 - Crear petición "Create Post for User"** (carpeta "Posts Management"):

- **Método**: POST
- **URL**: `{{baseURL}}/posts`
- **Pre-request Script**:
```javascript
// Generar timestamp para título único
const timestamp = new Date().getTime();
pm.collectionVariables.set("timestamp", timestamp);
```
- **Body (raw JSON)**:
```json
{
    "title": "{{testTitle}} - {{timestamp}}",
    "body": "Este es un post de prueba automatizado creado para testing",
    "userId": {{testUserId}}
}
```
- **Test Script**:
```javascript
pm.test("Post creado exitosamente", function() {
    pm.response.to.have.status(201);
});

pm.test("Post contiene datos correctos", function() {
    const post = pm.response.json();
    pm.expect(post).to.have.property('id');
    pm.expect(post).to.have.property('title');
    pm.expect(post).to.have.property('userId');
    
    // Guardar ID del post para siguientes peticiones
    pm.collectionVariables.set("testPostId", post.id);
});

const post = pm.response.json();
console.log("Post creado - ID:", post.id, "| Título:", post.title);
```

**Paso 2 - Crear petición "Get Posts by User"** (carpeta "Posts Management"):

- **Método**: GET
- **URL**: `{{baseURL}}/posts?userId={{testUserId}}`
- **Test Script**:
```javascript
pm.test("Posts del usuario obtenidos", function() {
    pm.response.to.have.status(200);
});

pm.test("Posts pertenecen al usuario correcto", function() {
    const posts = pm.response.json();
    const userId = pm.collectionVariables.get("testUserId");
    
    posts.forEach(post => {
        pm.expect(post.userId.toString()).to.eql(userId.toString());
    });
});

console.log("Posts encontrados para el usuario:", pm.response.json().length);
```

**Paso 3 - Crear petición "Update Post"** (carpeta "Posts Management"):

- **Método**: PUT
- **URL**: `{{baseURL}}/posts/{{testPostId}}`
- **Body (raw JSON)**:
```json
{
    "id": {{testPostId}},
    "title": "{{testTitle}} - UPDATED",
    "body": "Este post ha sido actualizado mediante automatización",
    "userId": {{testUserId}}
}
```
- **Test Script**:
```javascript
pm.test("Post actualizado exitosamente", function() {
    pm.response.to.have.status(200);
});

pm.test("Título fue actualizado", function() {
    const post = pm.response.json();
    pm.expect(post.title).to.include("UPDATED");
});

console.log("Post actualizado correctamente");
```

**Paso 4 - Crear petición "Delete Post"** (carpeta "Cleanup"):

- **Método**: DELETE
- **URL**: `{{baseURL}}/posts/{{testPostId}}`
- **Test Script**:
```javascript
pm.test("Post eliminado exitosamente", function() {
    pm.response.to.have.status(200);
});

// Limpiar variables
pm.collectionVariables.unset("testPostId");
pm.collectionVariables.unset("testUserId");
pm.collectionVariables.unset("timestamp");

console.log("Post eliminado y variables limpiadas");
```

**Resultado Esperado**:
- Flujo completo: crear post → obtener posts por usuario → actualizar post → eliminar post
- Variables se pasan correctamente entre peticiones
- Variables se limpian al final

## Parte 3: Ejecución con Collection Runner (20 minutos)

### Ejercicio 3.1: Ejecutar Colección Completa

**Objetivo**: Utilizar Collection Runner para ejecutar toda la colección automáticamente.

**Pasos**:

1. En el panel izquierdo, hacer clic derecho en "API Testing Automation Lab"

2. Seleccionar "Run collection"

3. En la ventana de Collection Runner:
   - Verificar que todas las peticiones están seleccionadas
   - Orden de ejecución debe ser: Setup → Users → Posts → Cleanup
   - Configurar:
     - Iterations: `1`
     - Delay: `100` ms
     - Data file: ninguno (por ahora)
   - Dejar marcado "Save responses"

4. Clic en "Run API Testing Automation Lab"

5. Observar la ejecución en tiempo real

6. Una vez completada:
   - Revisar el resumen: peticiones ejecutadas, tests pasados/fallados
   - Expandir cada petición para ver detalles
   - Verificar que todos los tests son verdes
   - Revisar la consola para ver los mensajes de log

**Resultado Esperado**:
- Todas las peticiones se ejecutan en orden
- Todos los tests pasan (verde)
- Console muestra mensajes de progreso
- Variables se crean, usan y limpian correctamente

### Ejercicio 3.2: Analizar Resultados

**Objetivo**: Interpretar los resultados de la ejecución.

**Pasos**:

1. En la vista de resultados, identificar:
   - Total de peticiones: ___
   - Total de tests: ___
   - Tests pasados: ___
   - Tests fallidos: ___
   - Tiempo total: ___

2. Hacer clic en la petición "Create New User":
   - Ver la respuesta guardada
   - Revisar los tests ejecutados
   - Verificar el tiempo de respuesta

3. Hacer clic en "View Summary":
   - Revisar gráficos de rendimiento
   - Identificar la petición más lenta

4. Exportar resultados (opcional):
   - Clic en "Export Results"
   - Guardar como JSON

**Resultado Esperado**:
- Comprensión de métricas de ejecución
- Identificación de posibles cuellos de botella
- Capacidad de analizar logs y respuestas

## Parte 4: Newman - Ejecución por Línea de Comandos (20 minutos)

### Ejercicio 4.1: Instalar y Configurar Newman

**Objetivo**: Preparar el entorno para ejecutar colecciones desde la terminal.

**Pasos**:

1. Abrir terminal o línea de comandos

2. Verificar que Node.js está instalado:
```bash
node --version
```
(Debe mostrar versión 12 o superior)

3. Instalar Newman globalmente:
```bash
npm install -g newman
```

4. Verificar instalación:
```bash
newman --version
```

5. Instalar reporter HTML (opcional):
```bash
npm install -g newman-reporter-html
```

**Resultado Esperado**:
- Newman instalado correctamente
- Comando `newman` disponible en terminal

### Ejercicio 4.2: Exportar y Ejecutar Colección

**Objetivo**: Ejecutar la colección desde línea de comandos.

**Pasos**:

1. En Postman, hacer clic derecho en "API Testing Automation Lab"

2. Seleccionar "Export"

3. Elegir formato "Collection v2.1"

4. Guardar como: `api-testing-automation.json` en una carpeta de trabajo

5. Desde la terminal, navegar a la carpeta donde guardaste el archivo:
```bash
cd ruta/a/tu/carpeta
```

6. Ejecutar la colección con Newman:
```bash
newman run api-testing-automation.json
```

7. Observar la salida en la terminal:
   - Progreso de cada petición
   - Resultados de tests
   - Resumen final

8. Ejecutar con reporte HTML:
```bash
newman run api-testing-automation.json --reporters cli,html --reporter-html-export reporte.html
```

9. Abrir `reporte.html` en un navegador para ver el reporte detallado

**Resultado Esperado**:
- Colección ejecutada desde terminal
- Salida muestra todas las peticiones y tests
- Reporte HTML generado con detalles visuales

### Ejercicio 4.3: Opciones Avanzadas de Newman

**Objetivo**: Utilizar opciones adicionales para control de ejecución.

**Pasos**:

1. Ejecutar con timeout personalizado (10 segundos por petición):
```bash
newman run api-testing-automation.json --timeout-request 10000
```

2. Ejecutar 3 iteraciones:
```bash
newman run api-testing-automation.json --iteration-count 3
```

3. Ejecutar con delay entre peticiones (500ms):
```bash
newman run api-testing-automation.json --delay-request 500
```

4. Detener en el primer error:
```bash
newman run api-testing-automation.json --bail
```

5. Ejecutar con todas las opciones combinadas:
```bash
newman run api-testing-automation.json \
  --iteration-count 2 \
  --delay-request 200 \
  --timeout-request 10000 \
  --reporters cli,html \
  --reporter-html-export reporte-final.html
```

**Resultado Esperado**:
- Comprensión de diferentes opciones de Newman
- Capacidad de personalizar ejecución según necesidades
- Reportes generados correctamente

## Parte 5: Data-Driven Testing (15 minutos)

### Ejercicio 5.1: Crear Archivo de Datos

**Objetivo**: Preparar datos externos para pruebas parametrizadas.

**Pasos**:

1. Crear un archivo llamado `test-users.csv` con el siguiente contenido:
```csv
name,username,email,city
Juan Pérez,juanp,juan.perez@test.com,Madrid
María García,mariag,maria.garcia@test.com,Barcelona
Carlos López,carlosl,carlos.lopez@test.com,Valencia
Ana Martínez,anam,ana.martinez@test.com,Sevilla
```

2. Guardar el archivo en la misma carpeta que la colección exportada

3. En Postman, modificar la petición "Create New User":
   - Cambiar el Body a:
```json
{
    "name": "{{name}}",
    "username": "{{username}}",
    "email": "{{email}}",
    "address": {
        "street": "Test Street",
        "city": "{{city}}"
    }
}
```

4. Modificar el Test Script de "Create New User":
```javascript
pm.test("Usuario creado exitosamente", function() {
    pm.response.to.have.status(201);
});

pm.test("Respuesta contiene datos correctos del CSV", function() {
    const user = pm.response.json();
    const expectedName = pm.iterationData.get("name");
    
    pm.expect(user).to.have.property('id');
    pm.collectionVariables.set("testUserId", user.id);
});

const newUser = pm.response.json();
const name = pm.iterationData.get("name");
console.log(`Usuario creado: ${name} con ID: ${newUser.id}`);
```

5. Guardar y exportar nuevamente la colección

**Resultado Esperado**:
- Archivo CSV creado con datos de prueba
- Petición modificada para usar variables del CSV

### Ejercicio 5.2: Ejecutar con Data-Driven Testing

**Objetivo**: Ejecutar la colección múltiples veces con diferentes datos.

**Pasos**:

1. En Collection Runner:
   - Seleccionar la colección
   - Clic en "Select File" en la sección "Data"
   - Seleccionar `test-users.csv`
   - El campo "Iterations" se actualizará automáticamente a 4 (número de filas)
   - Hacer clic en "Run"

2. Observar cómo se ejecuta 4 veces, una por cada usuario en el CSV

3. Revisar los resultados de cada iteración

4. Ejecutar lo mismo con Newman:
```bash
newman run api-testing-automation.json --iteration-data test-users.csv
```

5. Observar en la terminal la ejecución de las 4 iteraciones

**Resultado Esperado**:
- Colección ejecutada 4 veces
- Cada iteración usa datos diferentes del CSV
- Console muestra cada nombre de usuario creado
- Todos los tests pasan en todas las iteraciones

## Autoevaluación

Responde las siguientes preguntas para verificar tu comprensión:

### Preguntas Teóricas

1. ¿Cuál es la diferencia entre Collection Runner y Newman?

2. ¿En qué orden de prioridad se aplican las variables en Postman (de mayor a menor alcance)?

3. ¿Para qué sirve el parámetro `--bail` en Newman?

4. ¿Qué ventaja tiene usar data-driven testing con archivos CSV?

5. ¿Cómo se limpia una variable de colección desde un test script?

### Preguntas Prácticas

6. Si ejecutas una colección con `--iteration-count 5` y `--delay-request 1000`, ¿cuánto tiempo mínimo tomará completar la ejecución si la colección tiene 4 peticiones?

7. ¿Qué comando usarías para ejecutar una colección con Newman, generar un reporte HTML, usar un archivo de datos y detenerse al primer error?

8. Si una petición necesita usar el ID creado en la petición anterior, ¿qué debe hacer el test script de la primera petición?

## Respuestas de Autoevaluación

1. **Collection Runner** es la interfaz gráfica en Postman para ejecutar colecciones visualmente. **Newman** es una herramienta de línea de comandos que ejecuta las mismas colecciones pero desde la terminal, ideal para CI/CD y automatización sin interfaz gráfica.

2. Orden de prioridad (de mayor a menor alcance):
   - Variables locales
   - Variables de datos (data)
   - Variables de entorno (environment)
   - Variables de colección (collection)
   - Variables globales (global)

3. El parámetro `--bail` detiene la ejecución de la colección inmediatamente al encontrar el primer test que falle, en lugar de continuar ejecutando las peticiones restantes.

4. Data-driven testing permite ejecutar la misma colección múltiples veces con diferentes conjuntos de datos sin modificar las peticiones, facilitando pruebas exhaustivas y reduciendo duplicación de código.

5. Se usa el método `unset()`:
```javascript
pm.collectionVariables.unset("nombreVariable");
```

6. Tiempo mínimo: 20 segundos
   - Cálculo: 5 iteraciones × 4 peticiones = 20 peticiones totales
   - Entre cada petición hay 1000ms (1 segundo) de delay
   - 20 peticiones × 1 segundo = 20 segundos
   - (Más el tiempo de las propias peticiones)

7. Comando completo:
```bash
newman run coleccion.json --iteration-data datos.csv --reporters html --reporter-html-export reporte.html --bail
```

8. El test script debe guardar el ID en una variable usando:
```javascript
pm.collectionVariables.set("variableId", pm.response.json().id);
```
Y la segunda petición debe usar `{{variableId}}` en su URL o body.

## Ejercicios de Ampliación (Opcionales)

### Ejercicio Extra 1: Implementar Reintentos

Modifica la colección para que reintente automáticamente una petición si falla:

```javascript
// En Pre-request Script
const maxRetries = 3;
const retryCount = pm.collectionVariables.get("retryCount") || 0;

if (retryCount < maxRetries) {
    pm.collectionVariables.set("retryCount", retryCount + 1);
} else {
    pm.collectionVariables.unset("retryCount");
}

// En Test Script
pm.test("Verificar respuesta", function() {
    if (pm.response.code !== 200 && pm.collectionVariables.get("retryCount") < maxRetries) {
        postman.setNextRequest("nombre-peticion-actual");
    }
    pm.response.to.have.status(200);
});
```

### Ejercicio Extra 2: Crear Script de Integración CI/CD

Crea un script `run-tests.sh` para integración en pipelines:

```bash
#!/bin/bash

echo "Ejecutando pruebas de API..."

newman run api-testing-automation.json \
  --iteration-data test-users.csv \
  --reporters cli,html,json \
  --reporter-html-export reports/reporte-$(date +%Y%m%d-%H%M%S).html \
  --reporter-json-export reports/results.json

EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo "✓ Todas las pruebas pasaron correctamente"
else
    echo "✗ Algunas pruebas fallaron"
    exit 1
fi
```

### Ejercicio Extra 3: Validaciones Avanzadas

Implementa validaciones de esquema JSON usando bibliotecas externas:

```javascript
const schema = {
    "type": "object",
    "required": ["id", "name", "email"],
    "properties": {
        "id": {"type": "number"},
        "name": {"type": "string"},
        "email": {"type": "string", "format": "email"}
    }
};

pm.test("Esquema JSON válido", function() {
    pm.response.to.have.jsonSchema(schema);
});
```

## Resumen del Laboratorio

En este laboratorio has aprendido a:

- Crear colecciones organizadas con carpetas y estructura lógica
- Implementar flujos de trabajo automatizados utilizando variables
- Ejecutar baterías de pruebas con Collection Runner
- Utilizar Newman para automatización desde línea de comandos
- Implementar data-driven testing con archivos de datos externos
- Generar reportes de ejecución en diferentes formatos
- Preparar colecciones para integración en pipelines de CI/CD

Estos conocimientos te permiten automatizar completamente el testing de APIs, reducir tiempo de pruebas manuales e integrar validaciones en procesos de desarrollo continuo.
