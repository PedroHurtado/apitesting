# Testing con Postman - Secuencias de Comandos

## Objetivos de Aprendizaje

Al finalizar este tema, el estudiante será capaz de:

1. Comprender la diferencia entre Pre-request Scripts y Tests
2. Utilizar el API `pm.*` para manipular variables y datos
3. Generar datos dinámicos en Pre-request Scripts
4. Implementar validaciones avanzadas con Chai assertions
5. Extraer datos del response y almacenarlos en variables
6. Encadenar requests para crear flujos de trabajo complejos
7. Crear flujos de autenticación y uso de tokens
8. Aplicar buenas prácticas en la escritura de scripts

---

## 1. Pre-request Scripts

### 1.1 ¿Qué son los Pre-request Scripts?

Los Pre-request Scripts son código JavaScript que se ejecuta **antes** de enviar el request. Permiten preparar datos dinámicos, configurar variables, y realizar operaciones necesarias antes de la petición.

**Casos de uso:**
- Generar timestamps actuales
- Crear datos aleatorios (emails, nombres, IDs)
- Calcular valores (hashes, firmas)
- Configurar variables dinámicas
- Preparar tokens de autenticación
- Lógica condicional para modificar el request

### 1.2 Ejemplo básico: Timestamp actual

**Escenario:** Necesitas enviar la fecha y hora actual en el body del request.

```javascript
// Generar timestamp actual
const timestamp = new Date().toISOString();

// Guardar en variable de entorno
pm.environment.set("current_timestamp", timestamp);

console.log("Timestamp generado:", timestamp);
```

**Uso en el Body:**
```json
{
  "name": "Test User",
  "createdAt": "{{current_timestamp}}"
}
```

### 1.3 Ejemplo: Generar datos aleatorios

**Escenario:** Crear un usuario con datos únicos cada vez.

```javascript
// Generar datos aleatorios
const randomId = Math.floor(Math.random() * 10000);
const randomEmail = `user_${randomId}@test.com`;
const randomUsername = `user${randomId}`;

// Guardar en variables
pm.environment.set("random_email", randomEmail);
pm.environment.set("random_username", randomUsername);

console.log("Email generado:", randomEmail);
console.log("Username generado:", randomUsername);
```

**Uso en el Body:**
```json
{
  "name": "Test User",
  "username": "{{random_username}}",
  "email": "{{random_email}}"
}
```

### 1.4 Ejemplo: Preparar autenticación

**Escenario:** Construir un token de autenticación antes del request.

```javascript
// Obtener credenciales de las variables de entorno
const apiKey = pm.environment.get("api_key");
const secret = pm.environment.get("api_secret");

// Construir token (ejemplo simple)
const token = btoa(`${apiKey}:${secret}`); // Base64 encode

// Guardar token
pm.environment.set("auth_token", token);

console.log("Token generado:", token);
```

### 1.5 Lógica condicional

**Escenario:** Cambiar parámetros según el entorno activo.

```javascript
const environment = pm.environment.name;

if (environment === "Production") {
    pm.environment.set("timeout", "30000");
    pm.environment.set("retries", "3");
} else {
    pm.environment.set("timeout", "5000");
    pm.environment.set("retries", "1");
}

console.log(`Configurado para ${environment}: timeout=${pm.environment.get("timeout")}`);
```

---

## 2. Tests y el API pm.*

### 2.1 Introducción al objeto pm

El objeto `pm` es el API principal de Postman para interactuar con el contexto del request y response.

**Componentes principales del API pm:**

```javascript
pm.test()           // Crear tests
pm.expect()         // Assertions
pm.response         // Acceder al response
pm.request          // Acceder al request
pm.environment      // Variables de entorno
pm.collectionVariables  // Variables de colección
pm.globals          // Variables globales
pm.variables        // Resolver variables (cualquier scope)
pm.info            // Información de la ejecución
```

### 2.2 pm.test() - Estructura básica

```javascript
pm.test("Descripción del test", function () {
    // Código del test
    // Usar pm.expect() para assertions
});
```

**Ejemplo:**
```javascript
pm.test("El status code es 200", function () {
    pm.expect(pm.response.code).to.equal(200);
});
```

### 2.3 pm.response - Acceder al response

```javascript
// Status code
pm.response.code         // 200, 404, 500, etc.
pm.response.status       // "OK", "Not Found", etc.

// Response time
pm.response.responseTime // Tiempo en milisegundos

// Headers
pm.response.headers.get("Content-Type")
pm.response.headers.has("Authorization")

// Body
pm.response.text()       // Body como string
pm.response.json()       // Body parseado como JSON
```

**Ejemplo completo:**
```javascript
console.log("Status:", pm.response.code);
console.log("Status text:", pm.response.status);
console.log("Response time:", pm.response.responseTime + "ms");
console.log("Content-Type:", pm.response.headers.get("Content-Type"));

const jsonData = pm.response.json();
console.log("Body:", jsonData);
```

### 2.4 pm.request - Acceder al request enviado

```javascript
// URL
pm.request.url.toString()

// Method
pm.request.method

// Headers
pm.request.headers.get("Content-Type")

// Body
pm.request.body
```

**Ejemplo de uso en debugging:**
```javascript
console.log("Request enviado:");
console.log("Method:", pm.request.method);
console.log("URL:", pm.request.url.toString());
console.log("Headers:", pm.request.headers);
```

---

## 3. Chai Assertions Avanzadas

### 3.1 Introducción a Chai

Chai es la librería de assertions usada por Postman. Proporciona una sintaxis expresiva para validaciones.

**Estilos de Chai:**
- BDD (Behavior Driven Development): `expect` / `should`
- TDD (Test Driven Development): `assert`

Postman usa principalmente el estilo BDD con `expect`.

### 3.2 Assertions básicas

**Igualdad:**
```javascript
pm.expect(value).to.equal(expected);      // Igualdad estricta (===)
pm.expect(value).to.eql(expected);        // Igualdad profunda (para objetos)
pm.expect(value).to.not.equal(expected);  // Desigualdad
```

**Ejemplo:**
```javascript
pm.test("Validación de igualdad", function () {
    const jsonData = pm.response.json();
    
    pm.expect(jsonData.id).to.equal(1);
    pm.expect(jsonData.name).to.equal("John Doe");
    
    // Para objetos complejos, usar eql
    pm.expect(jsonData.address).to.eql({
        city: "Madrid",
        country: "España"
    });
});
```

### 3.3 Validación de tipos

```javascript
pm.expect(value).to.be.a('string');
pm.expect(value).to.be.a('number');
pm.expect(value).to.be.a('boolean');
pm.expect(value).to.be.an('array');
pm.expect(value).to.be.an('object');
pm.expect(value).to.be.null;
pm.expect(value).to.be.undefined;
```

**Ejemplo:**
```javascript
pm.test("Validación de tipos", function () {
    const jsonData = pm.response.json();
    
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
    pm.expect(jsonData.active).to.be.a('boolean');
    pm.expect(jsonData.roles).to.be.an('array');
    pm.expect(jsonData.address).to.be.an('object');
});
```

### 3.4 Comparaciones numéricas

```javascript
pm.expect(value).to.be.above(10);          // value > 10
pm.expect(value).to.be.below(100);         // value < 100
pm.expect(value).to.be.at.least(5);        // value >= 5
pm.expect(value).to.be.at.most(50);        // value <= 50
pm.expect(value).to.be.within(10, 50);     // 10 <= value <= 50
```

**Ejemplo:**
```javascript
pm.test("Validaciones numéricas", function () {
    const jsonData = pm.response.json();
    
    pm.expect(jsonData.age).to.be.above(0);
    pm.expect(jsonData.age).to.be.below(150);
    pm.expect(jsonData.score).to.be.within(0, 100);
    
    pm.expect(pm.response.responseTime).to.be.below(1000);
});
```

### 3.5 Validación de strings

```javascript
pm.expect(string).to.include('substring');     // Contiene
pm.expect(string).to.match(/regex/);           // Expresión regular
pm.expect(string).to.have.lengthOf(10);        // Longitud exacta
```

**Ejemplo:**
```javascript
pm.test("Validaciones de strings", function () {
    const jsonData = pm.response.json();
    
    pm.expect(jsonData.email).to.include('@');
    pm.expect(jsonData.email).to.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/);
    pm.expect(jsonData.username).to.have.lengthOf.above(3);
});
```

### 3.6 Validación de arrays

```javascript
pm.expect(array).to.be.an('array');
pm.expect(array).to.have.lengthOf(5);
pm.expect(array).to.include(item);
pm.expect(array).to.be.empty;
pm.expect(array).to.not.be.empty;
```

**Ejemplo:**
```javascript
pm.test("Validaciones de arrays", function () {
    const jsonData = pm.response.json();
    
    pm.expect(jsonData).to.be.an('array');
    pm.expect(jsonData).to.have.lengthOf(10);
    pm.expect(jsonData).to.not.be.empty;
    
    // Validar que contiene un elemento específico
    const userIds = jsonData.map(user => user.id);
    pm.expect(userIds).to.include(1);
});
```

### 3.7 Validación de propiedades de objetos

```javascript
pm.expect(obj).to.have.property('key');
pm.expect(obj).to.have.all.keys('key1', 'key2', 'key3');
pm.expect(obj).to.include.keys('key1', 'key2');
```

**Ejemplo:**
```javascript
pm.test("Validación de estructura de objeto", function () {
    const jsonData = pm.response.json();
    
    // Verificar que tiene propiedades específicas
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('name');
    pm.expect(jsonData).to.have.property('email');
    
    // Verificar que tiene TODAS estas keys y ninguna más
    pm.expect(jsonData).to.have.all.keys('id', 'name', 'email', 'username', 'address', 'phone', 'website', 'company');
    
    // Verificar que incluye al menos estas keys (puede tener más)
    pm.expect(jsonData).to.include.keys('id', 'name', 'email');
});
```

### 3.8 Validaciones anidadas

```javascript
pm.test("Validar objeto anidado", function () {
    const jsonData = pm.response.json();
    
    // Validar que existe el objeto anidado
    pm.expect(jsonData).to.have.property('address');
    pm.expect(jsonData.address).to.be.an('object');
    
    // Validar propiedades del objeto anidado
    pm.expect(jsonData.address).to.have.property('city');
    pm.expect(jsonData.address).to.have.property('street');
    pm.expect(jsonData.address.city).to.be.a('string');
    
    // Validar objeto dentro de objeto
    pm.expect(jsonData.address).to.have.property('geo');
    pm.expect(jsonData.address.geo).to.have.all.keys('lat', 'lng');
});
```

### 3.9 Iteración sobre arrays con validaciones

```javascript
pm.test("Validar todos los elementos del array", function () {
    const users = pm.response.json();
    
    // Verificar que es un array
    pm.expect(users).to.be.an('array');
    
    // Validar cada usuario
    users.forEach(function(user) {
        pm.expect(user).to.have.property('id');
        pm.expect(user).to.have.property('name');
        pm.expect(user).to.have.property('email');
        
        pm.expect(user.id).to.be.a('number');
        pm.expect(user.name).to.be.a('string');
        pm.expect(user.email).to.include('@');
    });
});
```

---

## 4. Extraer y Guardar Datos del Response

### 4.1 Extraer valores simples

```javascript
// Parsear el response
const jsonData = pm.response.json();

// Extraer valores
const userId = jsonData.id;
const userName = jsonData.name;
const userEmail = jsonData.email;

// Guardar en variables
pm.environment.set("user_id", userId);
pm.environment.set("user_name", userName);
pm.environment.set("user_email", userEmail);

console.log("User ID guardado:", userId);
```

### 4.2 Extraer valores anidados

```javascript
const jsonData = pm.response.json();

// Extraer de objeto anidado
const city = jsonData.address.city;
const latitude = jsonData.address.geo.lat;

// Guardar
pm.environment.set("user_city", city);
pm.environment.set("user_lat", latitude);
```

### 4.3 Extraer del primer elemento de un array

```javascript
const users = pm.response.json();

// Verificar que el array no esté vacío
if (users && users.length > 0) {
    const firstUser = users[0];
    const firstUserId = firstUser.id;
    
    pm.environment.set("first_user_id", firstUserId);
    console.log("Primer user ID:", firstUserId);
}
```

### 4.4 Extraer valor específico de un array

```javascript
const posts = pm.response.json();

// Buscar un post específico
const targetPost = posts.find(post => post.userId === 1);

if (targetPost) {
    pm.environment.set("post_id", targetPost.id);
    pm.environment.set("post_title", targetPost.title);
}
```

### 4.5 Extraer múltiples valores en un loop

```javascript
const users = pm.response.json();

// Extraer todos los IDs en un array
const userIds = users.map(user => user.id);

// Guardar como string (para usar después)
pm.environment.set("all_user_ids", JSON.stringify(userIds));

console.log("IDs de usuarios:", userIds);
```

### 4.6 Variables en diferentes scopes

**Variables de entorno (recomendado para flujos):**
```javascript
pm.environment.set("key", "value");
pm.environment.get("key");
pm.environment.unset("key");
```

**Variables de colección:**
```javascript
pm.collectionVariables.set("key", "value");
pm.collectionVariables.get("key");
pm.collectionVariables.unset("key");
```

**Variables globales:**
```javascript
pm.globals.set("key", "value");
pm.globals.get("key");
pm.globals.unset("key");
```

**Variables locales (solo en ejecución actual):**
```javascript
pm.variables.set("key", "value");
pm.variables.get("key");
```

---

## 5. Encadenar Requests

### 5.1 ¿Qué es el encadenamiento?

El encadenamiento de requests consiste en usar datos del response de un request en requests subsiguientes. Es fundamental para crear flujos de trabajo complejos.

**Flujo típico:**
```
Request 1: Login → Guarda token
Request 2: Get User → Usa token, guarda user_id
Request 3: Get User Posts → Usa token y user_id
Request 4: Create Post → Usa token y user_id
```

### 5.2 Ejemplo básico: Flujo de autenticación

**Request 1: POST Login**

Pre-request Script:
```javascript
// Limpiar token anterior
pm.environment.unset("access_token");
```

Body:
```json
{
  "username": "admin",
  "password": "password123"
}
```

Tests:
```javascript
pm.test("Login successful", function () {
    pm.response.to.have.status(200);
    
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('token');
    
    // Guardar token para requests subsiguientes
    pm.environment.set("access_token", jsonData.token);
    
    console.log("Token guardado:", jsonData.token);
});
```

**Request 2: GET User Profile** (usa el token)

Authorization tab:
- Type: Bearer Token
- Token: `{{access_token}}`

Tests:
```javascript
pm.test("Profile retrieved successfully", function () {
    pm.response.to.have.status(200);
    
    const jsonData = pm.response.json();
    
    // Guardar user_id para siguientes requests
    pm.environment.set("current_user_id", jsonData.id);
    
    console.log("User ID:", jsonData.id);
});
```

### 5.3 Ejemplo avanzado: CRUD encadenado

**Request 1: POST Create User**

Pre-request Script:
```javascript
// Generar datos únicos
const randomId = Date.now();
pm.environment.set("test_email", `test_${randomId}@example.com`);
pm.environment.set("test_username", `user_${randomId}`);
```

Body:
```json
{
  "name": "Test User",
  "username": "{{test_username}}",
  "email": "{{test_email}}"
}
```

Tests:
```javascript
pm.test("User created", function () {
    pm.response.to.have.status(201);
    
    const jsonData = pm.response.json();
    
    // Guardar el ID del usuario creado
    pm.environment.set("created_user_id", jsonData.id);
    
    console.log("Created user with ID:", jsonData.id);
});
```

**Request 2: GET User** (usa el ID del usuario creado)

URL: `{{base_url}}/users/{{created_user_id}}`

Tests:
```javascript
pm.test("Retrieved created user", function () {
    pm.response.to.have.status(200);
    
    const jsonData = pm.response.json();
    
    // Verificar que es el usuario correcto
    pm.expect(jsonData.email).to.eql(pm.environment.get("test_email"));
    
    console.log("User retrieved successfully");
});
```

**Request 3: PUT Update User** (actualiza el usuario creado)

URL: `{{base_url}}/users/{{created_user_id}}`

Pre-request Script:
```javascript
// Preparar nuevos datos
pm.environment.set("updated_name", "Updated Test User");
```

Body:
```json
{
  "id": {{created_user_id}},
  "name": "{{updated_name}}",
  "username": "{{test_username}}",
  "email": "{{test_email}}"
}
```

Tests:
```javascript
pm.test("User updated", function () {
    pm.response.to.have.status(200);
    
    const jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql("Updated Test User");
    
    console.log("User updated successfully");
});
```

**Request 4: DELETE User** (elimina el usuario creado)

URL: `{{base_url}}/users/{{created_user_id}}`

Tests:
```javascript
pm.test("User deleted", function () {
    pm.response.to.have.status(200);
    
    // Limpiar variables
    pm.environment.unset("created_user_id");
    pm.environment.unset("test_email");
    pm.environment.unset("test_username");
    
    console.log("User deleted and variables cleaned");
});
```

### 5.4 Ejemplo: Flujo con relaciones (Users → Posts → Comments)

**Request 1: GET Users**

Tests:
```javascript
pm.test("Get users", function () {
    pm.response.to.have.status(200);
    
    const users = pm.response.json();
    
    // Guardar ID del primer usuario
    if (users && users.length > 0) {
        pm.environment.set("user_id", users[0].id);
        console.log("User ID selected:", users[0].id);
    }
});
```

**Request 2: GET User Posts**

URL: `{{base_url}}/users/{{user_id}}/posts`

Tests:
```javascript
pm.test("Get user posts", function () {
    pm.response.to.have.status(200);
    
    const posts = pm.response.json();
    
    // Guardar ID del primer post
    if (posts && posts.length > 0) {
        pm.environment.set("post_id", posts[0].id);
        console.log("Post ID selected:", posts[0].id);
    }
});
```

**Request 3: GET Post Comments**

URL: `{{base_url}}/posts/{{post_id}}/comments`

Tests:
```javascript
pm.test("Get post comments", function () {
    pm.response.to.have.status(200);
    
    const comments = pm.response.json();
    
    pm.expect(comments).to.be.an('array');
    console.log("Retrieved comments:", comments.length);
    
    // Validar que los comentarios pertenecen al post correcto
    const postId = parseInt(pm.environment.get("post_id"));
    comments.forEach(function(comment) {
        pm.expect(comment.postId).to.eql(postId);
    });
});
```

---

## 6. Buenas Prácticas

### 6.1 Nombres descriptivos de tests

**❌ Mal:**
```javascript
pm.test("Test 1", function () { ... });
pm.test("Check response", function () { ... });
```

**✅ Bien:**
```javascript
pm.test("Status code is 200", function () { ... });
pm.test("User has valid email format", function () { ... });
pm.test("Response contains required user fields", function () { ... });
```

### 6.2 DRY (Don't Repeat Yourself)

**❌ Mal:**
```javascript
// En cada request, repetir el mismo código
pm.test("Status OK", function () {
    pm.response.to.have.status(200);
    pm.expect(pm.response.responseTime).to.be.below(1000);
});
```

**✅ Bien:**
Usar tests a nivel de colección o carpeta para validaciones comunes.

### 6.3 Manejo de errores

```javascript
pm.test("Safe data extraction", function () {
    const jsonData = pm.response.json();
    
    // Verificar que existe antes de acceder
    if (jsonData && jsonData.users && jsonData.users.length > 0) {
        pm.environment.set("user_id", jsonData.users[0].id);
    } else {
        console.log("No users found in response");
    }
});
```

### 6.4 Logging útil

```javascript
// ✅ Logging descriptivo
console.log("=== User Creation Flow ===");
console.log("Step 1: Generating random data");
console.log("Email:", pm.environment.get("test_email"));
console.log("Step 2: Creating user");

pm.test("User created", function () {
    const jsonData = pm.response.json();
    console.log("User created with ID:", jsonData.id);
    console.log("Step 3: Saving user ID for next requests");
});
```

### 6.5 Limpieza de variables

```javascript
// Al final de un flujo, limpiar variables temporales
pm.test("Cleanup", function () {
    pm.environment.unset("temp_user_id");
    pm.environment.unset("temp_token");
    pm.environment.unset("test_data");
    
    console.log("Variables cleaned up");
});
```

---

## 7. Referencias y Recursos

### 7.1 Documentación oficial
- **Postman Scripting**: https://learning.postman.com/docs/writing-scripts/intro-to-scripts/
- **Pre-request Scripts**: https://learning.postman.com/docs/writing-scripts/pre-request-scripts/
- **Test Scripts**: https://learning.postman.com/docs/writing-scripts/test-scripts/
- **Postman Sandbox API**: https://learning.postman.com/docs/writing-scripts/script-references/postman-sandbox-api-reference/

### 7.2 Chai documentation
- **Chai BDD API**: https://www.chaijs.com/api/bdd/
- **Chai Guide**: https://www.chaijs.com/guide/

### 7.3 JavaScript resources
- **MDN JavaScript**: https://developer.mozilla.org/es/docs/Web/JavaScript
- **JavaScript Array methods**: https://developer.mozilla.org/es/docs/Web/JavaScript/Reference/Global_Objects/Array

---

## 8. Preguntas de Autoevaluación

1. ¿Cuándo se ejecutan los Pre-request Scripts y cuándo los Tests?
2. ¿Para qué sirve el método `pm.environment.set()`?
3. ¿Cuál es la diferencia entre `.equal()` y `.eql()` en Chai?
4. ¿Cómo extraerías el ID de un usuario del response y lo guardarías para el siguiente request?
5. ¿Qué scope de variable es más apropiado para un flujo de autenticación temporal?
6. ¿Por qué es importante validar que un array no esté vacío antes de acceder a `array[0]`?
7. ¿Qué ventaja tiene encadenar requests?

---

## Resumen

En este tema hemos aprendido:

- Los **Pre-request Scripts** se ejecutan antes del request y preparan datos dinámicos
- El **API pm.*** proporciona acceso completo al contexto del request y response
- **Chai assertions** permite validaciones expresivas y completas
- Podemos **extraer datos** del response y almacenarlos en variables de diferentes scopes
- El **encadenamiento de requests** permite crear flujos complejos usando datos entre requests
- Las **buenas prácticas** incluyen nombres descriptivos, manejo de errores, y limpieza de variables
- El **logging efectivo** facilita el debugging y comprensión de flujos

---


