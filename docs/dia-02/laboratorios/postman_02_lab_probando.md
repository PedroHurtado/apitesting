# Testing con Postman - Laboratorio: Probando

## Información del Laboratorio

**Duración estimada**: 70 minutos  
**Nivel**: Intermedio  
**Prerequisitos**: 
- Haber completado el Laboratorio 1 (Preparando el entorno)
- Postman configurado con workspace y environment
- Colección "JSONPlaceholder API" creada
- Conocimientos básicos de JSON

---

## Objetivos del Laboratorio

Al completar este laboratorio, serás capaz de:

1. Ejecutar requests con los principales métodos HTTP (GET, POST, PUT, PATCH, DELETE)
2. Configurar autenticación en requests
3. Trabajar con headers, query parameters y path variables
4. Enviar datos en diferentes formatos de body
5. Implementar validaciones básicas en responses
6. Utilizar la Postman Console para debugging
7. Construir un CRUD completo funcional

---

## Ejercicio 1: Implementar CRUD de Usuarios (40 minutos)

### Descripción
Implementarás las operaciones CRUD completas para el recurso "users" de JSONPlaceholder API, incluyendo validaciones básicas.

### Parte 1.1: GET - Listar todos los usuarios (8 minutos)

**Pasos:**

1. Abre tu colección "JSONPlaceholder API"
2. En la carpeta "Users Management", crea un nuevo request:
   - **Name**: "GET List All Users"
   - **Method**: GET
   - **URL**: `{{base_url}}/users`

3. Ve al tab "Headers" y verifica que estén estos headers (Postman los agrega automáticamente):
   - `Accept: application/json`

4. Ve al tab "Tests" y agrega estas validaciones:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response time is less than 1000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(1000);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Array has at least 1 user", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.length).to.be.above(0);
});

pm.test("First user has required properties", function () {
    var jsonData = pm.response.json();
    var firstUser = jsonData[0];
    pm.expect(firstUser).to.have.property('id');
    pm.expect(firstUser).to.have.property('name');
    pm.expect(firstUser).to.have.property('email');
    pm.expect(firstUser).to.have.property('username');
});
```

5. Click en "Save"
6. Click en "Send"
7. Verifica que todos los tests pasen (indicador verde)
8. Abre la Postman Console (`Ctrl+Alt+C`) y revisa el request completo

**Resultado esperado:**
- Status: 200 OK
- Response: Array con 10 usuarios
- Todos los tests en verde
- Response time < 1000ms

---

### Parte 1.2: GET - Obtener usuario por ID (8 minutos)

**Pasos:**

1. Crea un nuevo request en "Users Management":
   - **Name**: "GET User by ID"
   - **Method**: GET
   - **URL**: `{{base_url}}/users/:userId`

2. Ve al tab "Params" → sección "Path Variables"
3. Verás la variable `userId` detectada automáticamente
4. Asigna valor: `userId = 1`

5. Ve al tab "Tests" y agrega estas validaciones:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an object", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('object');
});

pm.test("User has correct structure", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('name');
    pm.expect(jsonData).to.have.property('username');
    pm.expect(jsonData).to.have.property('email');
    pm.expect(jsonData).to.have.property('address');
    pm.expect(jsonData).to.have.property('phone');
    pm.expect(jsonData).to.have.property('website');
    pm.expect(jsonData).to.have.property('company');
});

pm.test("User ID matches requested ID", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.eql(1);
});

pm.test("Data types are correct", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
    pm.expect(jsonData.email).to.be.a('string');
    pm.expect(jsonData.address).to.be.an('object');
});
```

6. Click en "Save"
7. Click en "Send"
8. Verifica que recibes los datos del usuario con ID 1

**Desafío adicional:**
Cambia el `userId` a `2`, `3`, etc. y verifica que obtienes usuarios diferentes.

**Resultado esperado:**
- Status: 200 OK
- Response: Objeto con datos completos del usuario
- Todos los tests en verde

---

### Parte 1.3: POST - Crear nuevo usuario (10 minutos)

**Pasos:**

1. Crea un nuevo request:
   - **Name**: "POST Create User"
   - **Method**: POST
   - **URL**: `{{base_url}}/users`

2. Ve al tab "Headers" y agrega:
   - **Key**: `Content-Type`
   - **Value**: `application/json`

3. Ve al tab "Body"
4. Selecciona "raw" y "JSON" del dropdown
5. Agrega este JSON:

```json
{
  "name": "María García",
  "username": "mariag",
  "email": "maria.garcia@example.com",
  "address": {
    "street": "Calle Mayor",
    "suite": "Apt. 5B",
    "city": "Madrid",
    "zipcode": "28001",
    "geo": {
      "lat": "40.4168",
      "lng": "-3.7038"
    }
  },
  "phone": "91-123-4567",
  "website": "mariag.com",
  "company": {
    "name": "García Consulting",
    "catchPhrase": "Soluciones innovadoras",
    "bs": "Consultoría empresarial"
  }
}
```

6. Ve al tab "Tests" y agrega:

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("User was created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData.id).to.be.a('number');
});

pm.test("Created user has correct data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.name).to.eql("María García");
    pm.expect(jsonData.username).to.eql("mariag");
    pm.expect(jsonData.email).to.eql("maria.garcia@example.com");
});

pm.test("Response includes all sent fields", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('address');
    pm.expect(jsonData).to.have.property('phone');
    pm.expect(jsonData).to.have.property('website');
    pm.expect(jsonData).to.have.property('company');
});

// Log del ID creado para referencia
console.log("Created user with ID:", pm.response.json().id);
```

7. Click en "Save"
8. Click en "Send"
9. Abre la Postman Console y verifica el ID del usuario creado

**Resultado esperado:**
- Status: 201 Created
- Response: Objeto con el usuario creado incluyendo un nuevo ID (usualmente 11)
- Todos los tests en verde
- Console muestra: "Created user with ID: 11"

---

### Parte 1.4: PUT - Actualizar usuario completo (10 minutos)

**Pasos:**

1. Crea un nuevo request:
   - **Name**: "PUT Update User"
   - **Method**: PUT
   - **URL**: `{{base_url}}/users/:userId`

2. Ve al tab "Params" → Path Variables
3. Asigna: `userId = 1`

4. Ve al tab "Body" → selecciona "raw" y "JSON"
5. Agrega este JSON (actualización completa):

```json
{
  "id": 1,
  "name": "Leanne Graham Actualizada",
  "username": "Bret_Updated",
  "email": "leanne.updated@example.com",
  "address": {
    "street": "Nueva Calle Principal",
    "suite": "Suite 100",
    "city": "Barcelona",
    "zipcode": "08001",
    "geo": {
      "lat": "41.3851",
      "lng": "2.1734"
    }
  },
  "phone": "93-000-1111",
  "website": "leanne-updated.com",
  "company": {
    "name": "Updated Company LLC",
    "catchPhrase": "Nuevo eslogan corporativo",
    "bs": "Servicios modernizados"
  }
}
```

6. Ve al tab "Tests":

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("User was updated successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.eql(1);
    pm.expect(jsonData.name).to.eql("Leanne Graham Actualizada");
    pm.expect(jsonData.email).to.eql("leanne.updated@example.com");
});

pm.test("All fields are present", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.all.keys('id', 'name', 'username', 'email', 'address', 'phone', 'website', 'company');
});

console.log("Updated user:", pm.response.json().name);
```

7. Click en "Save"
8. Click en "Send"

**Resultado esperado:**
- Status: 200 OK
- Response: Usuario con todos los campos actualizados
- Todos los tests en verde

---

### Parte 1.5: PATCH - Actualizar parcialmente (7 minutos)

**Pasos:**

1. Crea un nuevo request:
   - **Name**: "PATCH Update User Email"
   - **Method**: PATCH
   - **URL**: `{{base_url}}/users/:userId`

2. Path Variables: `userId = 1`

3. Body (raw - JSON):

```json
{
  "email": "nuevo.email@example.com",
  "phone": "555-NUEVO"
}
```

4. Tests:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Email was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.email).to.eql("nuevo.email@example.com");
});

pm.test("Phone was updated", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.phone).to.eql("555-NUEVO");
});

console.log("Partially updated user - new email:", pm.response.json().email);
```

5. Click en "Save" y "Send"

**Resultado esperado:**
- Status: 200 OK
- Solo los campos enviados están actualizados
- Tests en verde

---

### Parte 1.6: DELETE - Eliminar usuario (7 minutos)

**Pasos:**

1. Crea un nuevo request:
   - **Name**: "DELETE User"
   - **Method**: DELETE
   - **URL**: `{{base_url}}/users/:userId`

2. Path Variables: `userId = 1`

3. No necesitas agregar Body para DELETE

4. Tests:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response confirms deletion", function () {
    // JSONPlaceholder retorna un objeto vacío {}
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('object');
});

console.log("User deleted - Status:", pm.response.code);
```

5. Click en "Save" y "Send"

**Resultado esperado:**
- Status: 200 OK
- Response: `{}` (objeto vacío)
- Tests en verde

**Nota**: JSONPlaceholder no elimina realmente los recursos, solo simula la operación.

---

## Ejercicio 2: Trabajar con Query Parameters (10 minutos)

### Descripción
Aprenderás a filtrar y paginar resultados usando query parameters.

### Pasos:

1. Crea un nuevo request en la carpeta "Posts Management":
   - **Name**: "GET Posts by User with Pagination"
   - **Method**: GET
   - **URL**: `{{base_url}}/posts`

2. Ve al tab "Params" y agrega estos Query Parameters:

| KEY | VALUE |
|-----|-------|
| userId | 1 |
| _limit | 5 |
| _sort | id |
| _order | desc |

3. Observa cómo Postman construye la URL automáticamente:
   ```
   {{base_url}}/posts?userId=1&_limit=5&_sort=id&_order=desc
   ```

4. Tests:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Array length respects limit", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.length).to.be.at.most(5);
});

pm.test("All posts belong to user 1", function () {
    var jsonData = pm.response.json();
    jsonData.forEach(function(post) {
        pm.expect(post.userId).to.eql(1);
    });
});

console.log("Retrieved posts count:", pm.response.json().length);
```

5. Click en "Save" y "Send"
6. Prueba cambiar los valores de los parámetros y observa los resultados

**Resultado esperado:**
- Máximo 5 posts retornados
- Todos del usuario con ID 1
- Ordenados por ID descendente

---

## Ejercicio 3: Trabajar con Diferentes Formatos de Body (10 minutos)

### Descripción
Experimentarás con diferentes formatos de envío de datos.

### Parte 3.1: Usando form-data

1. Crea un request de prueba:
   - **Name**: "POST Test Form Data"
   - **Method**: POST
   - **URL**: `https://postman-echo.com/post`

2. Ve al tab "Body"
3. Selecciona "form-data"
4. Agrega estos campos:

| KEY | VALUE |
|-----|-------|
| name | Juan Pérez |
| email | juan@example.com |
| age | 30 |
| country | España |

5. Click en "Send"
6. Observa en el response cómo se recibieron los datos bajo `form`

### Parte 3.2: Usando x-www-form-urlencoded

1. En el mismo request, cambia el tipo de Body a "x-www-form-urlencoded"
2. Los campos se mantienen
3. Click en "Send"
4. Observa en el response cómo ahora los datos están bajo `form` pero codificados de forma diferente

### Parte 3.3: Comparación

Abre la Postman Console y compara:
- El `Content-Type` header en cada caso
- La estructura del body enviado
- Cómo el servidor recibe los datos

**Resultado esperado:**
Comprenderás las diferencias entre form-data y x-www-form-urlencoded.

---

## Ejercicio 4: Debugging con Postman Console (10 minutos)

### Descripción
Practicarás el uso de la consola para debugging.

### Pasos:

1. Abre Postman Console (`Ctrl+Alt+C` o `Cmd+Alt+C`)

2. Ve a tu request "GET User by ID"

3. En el tab "Pre-request Script" agrega:

```javascript
console.log("=== PRE-REQUEST SCRIPT ===");
console.log("Request URL:", pm.request.url.toString());
console.log("Environment:", pm.environment.name);
console.log("Base URL variable:", pm.environment.get("base_url"));
console.log("User ID:", pm.variables.get("userId"));
```

4. En el tab "Tests" agrega al inicio:

```javascript
console.log("=== TESTS SCRIPT ===");
console.log("Response Status:", pm.response.code);
console.log("Response Time:", pm.response.responseTime + "ms");

var jsonData = pm.response.json();
console.log("User Name:", jsonData.name);
console.log("User Email:", jsonData.email);
console.log("Complete Response:", JSON.stringify(jsonData, null, 2));

// Tests existentes...
```

5. Click en "Send"

6. En la Postman Console, observa:
   - Logs del Pre-request Script
   - El request completo enviado
   - Logs del Tests Script
   - El response completo recibido

**Resultado esperado:**
Verás todo el flujo de ejecución con información detallada para debugging.

---

## Autoevaluación

### Preguntas de Comprensión

1. **¿Qué método HTTP usarías para cada operación?**
   - Crear un nuevo usuario: ___________
   - Leer datos de un usuario: ___________
   - Actualizar completamente un usuario: ___________
   - Actualizar solo el email de un usuario: ___________
   - Eliminar un usuario: ___________

2. **¿Cuál es la diferencia principal entre PUT y PATCH?**
   
   Respuesta: _______________________________________________________

3. **¿Dónde se envían los query parameters en un request?**
   - [ ] En el Body
   - [ ] En los Headers
   - [ ] En la URL después del ?
   - [ ] En el método HTTP

4. **¿Qué status code esperas recibir después de un POST exitoso?**
   - [ ] 200 OK
   - [ ] 201 Created
   - [ ] 204 No Content
   - [ ] 400 Bad Request

5. **¿Para qué sirve el tab "Tests" en Postman?**
   
   Respuesta: _______________________________________________________

### Ejercicio Práctico de Validación

6. **Escribe un test que valide que un array tiene exactamente 10 elementos:**

```javascript
pm.test("Array has 10 elements", function () {
    var jsonData = pm.response.json();
    // Completa el test aquí
    
});
```

7. **Escribe un test que valide que el email de un usuario contiene el símbolo @:**

```javascript
pm.test("Email is valid format", function () {
    var jsonData = pm.response.json();
    // Completa el test aquí
    
});
```

---

## Soluciones de Autoevaluación

### Respuestas

1. **Métodos HTTP:**
   - Crear: POST
   - Leer: GET
   - Actualizar completamente: PUT
   - Actualizar parcialmente: PATCH
   - Eliminar: DELETE

2. **PUT vs PATCH:**
   PUT reemplaza el recurso completo, debes enviar todos los campos. PATCH actualiza solo los campos que envías, el resto permanece sin cambios.

3. **Query parameters:** En la URL después del ?

4. **Status code POST:** 201 Created

5. **Tab Tests:** Sirve para escribir validaciones automáticas que verifican que el response cumple con lo esperado (status code, estructura, valores, etc.)

6. **Test array con 10 elementos:**
```javascript
pm.test("Array has 10 elements", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.length).to.eql(10);
});
```

7. **Test email válido:**
```javascript
pm.test("Email is valid format", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.email).to.include("@");
});
```

---

## Desafío Final (Opcional)

### Implementar CRUD Completo de Posts

**Objetivo:** Crear una carpeta "Posts Management Complete" con un CRUD completo de posts.

**Requisitos:**

1. **GET All Posts** - Listar todos los posts
   - Validar que es un array
   - Validar que tiene al menos 1 post

2. **GET Post by ID** - Obtener un post específico (usar variable `{{post_id}}`)
   - Validar estructura del post
   - Validar que tiene: id, userId, title, body

3. **GET Posts by User** - Filtrar posts por userId usando query params
   - Validar que todos los posts pertenecen al usuario correcto

4. **POST Create Post** - Crear un nuevo post
   - Body:
   ```json
   {
     "title": "Mi nuevo post",
     "body": "Contenido del post",
     "userId": 1
   }
   ```
   - Validar status 201
   - Validar que retorna un ID

5. **PUT Update Post** - Actualizar un post completo
   - Validar status 200
   - Validar que los datos se actualizaron

6. **DELETE Post** - Eliminar un post
   - Validar status 200

**Bonus:** Agregar console.log() en cada request para tracking del flujo.

---

## Resumen del Laboratorio

En este laboratorio has aprendido:

✅ Implementar operaciones CRUD completas (GET, POST, PUT, PATCH, DELETE)  
✅ Configurar y enviar requests con diferentes métodos HTTP  
✅ Trabajar con query parameters y path variables  
✅ Enviar datos en diferentes formatos (JSON, form-data, x-www-form-urlencoded)  
✅ Implementar validaciones básicas con el tab Tests  
✅ Usar la Postman Console para debugging  
✅ Estructurar tests con múltiples validaciones  
✅ Interpretar status codes y responses  

**Habilidades clave adquiridas:**
- Testing de APIs REST
- Validación automatizada de responses
- Debugging de requests
- Implementación de CRUD completo

---

## Próximos Pasos

Una vez completado este laboratorio, estarás preparado para:

- **Laboratorio 3**: "Testing con Postman - Secuencias de Comandos"
  - Pre-request Scripts avanzados
  - Encadenamiento de requests
  - Uso de variables dinámicas
  - Flujos de trabajo automatizados

---

## Recursos Adicionales

- **HTTP Methods RFC**: https://tools.ietf.org/html/rfc7231#section-4
- **JSONPlaceholder Guide**: https://jsonplaceholder.typicode.com/guide/
- **Postman Echo API**: https://postman-echo.com/
- **Chai Assertion Library**: https://www.chaijs.com/api/bdd/

---

