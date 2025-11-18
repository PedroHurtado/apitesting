# Testing con Postman - Laboratorio: Secuencias de Comandos

## Información del Laboratorio

**Duración estimada**: 120 minutos  
**Nivel**: Avanzado  
**Prerequisitos**: 
- Haber completado los Laboratorios 1 y 2
- Conocimientos de JavaScript básico
- Comprensión de métodos de array (map, filter, find, forEach)
- Colección "JSONPlaceholder API" configurada

---

## Objetivos del Laboratorio

Al completar este laboratorio, serás capaz de:

1. Implementar Pre-request Scripts para generar datos dinámicos
2. Utilizar el API pm.* para manipular variables y contexto
3. Crear validaciones avanzadas con Chai assertions
4. Extraer datos del response y almacenarlos estratégicamente
5. Encadenar múltiples requests para crear flujos complejos
6. Implementar un flujo completo de CRUD con encadenamiento
7. Aplicar buenas prácticas de scripting y debugging

---

## Ejercicio 1: Pre-request Scripts - Datos Dinámicos (25 minutos)

### Descripción
Aprenderás a generar datos dinámicos antes de enviar requests, evitando conflictos y permitiendo tests repetibles.

### Parte 1.1: Generar datos aleatorios para crear usuario (10 minutos)

**Pasos:**

1. Crea un nuevo request en "Users Management":
   - **Name**: "POST Create User with Dynamic Data"
   - **Method**: POST
   - **URL**: `{{base_url}}/users`

2. Ve al tab "Pre-request Script" y agrega:

```javascript
// Generar timestamp único
const timestamp = Date.now();

// Generar datos aleatorios
const randomId = Math.floor(Math.random() * 10000);
const randomName = `Test User ${timestamp}`;
const randomUsername = `user_${timestamp}`;
const randomEmail = `user_${timestamp}@test.com`;
const randomPhone = `${Math.floor(Math.random() * 900) + 100}-${Math.floor(Math.random() * 9000) + 1000}`;

// Guardar en variables de entorno
pm.environment.set("dynamic_name", randomName);
pm.environment.set("dynamic_username", randomUsername);
pm.environment.set("dynamic_email", randomEmail);
pm.environment.set("dynamic_phone", randomPhone);

// Generar fecha de creación
const createdAt = new Date().toISOString();
pm.environment.set("created_at", createdAt);

// Logging para debugging
console.log("=== Datos generados ===");
console.log("Name:", randomName);
console.log("Username:", randomUsername);
console.log("Email:", randomEmail);
console.log("Phone:", randomPhone);
console.log("Created at:", createdAt);
```

3. Ve al tab "Body" → selecciona "raw" y "JSON":

```json
{
  "name": "{{dynamic_name}}",
  "username": "{{dynamic_username}}",
  "email": "{{dynamic_email}}",
  "phone": "{{dynamic_phone}}",
  "website": "testuser.com",
  "company": {
    "name": "Test Company",
    "catchPhrase": "Automated testing",
    "bs": "API testing solutions"
  }
}
```

4. Ve al tab "Tests":

```javascript
pm.test("User created with status 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response contains generated data", function () {
    const jsonData = pm.response.json();
    
    // Verificar que los datos enviados están en el response
    pm.expect(jsonData.name).to.eql(pm.environment.get("dynamic_name"));
    pm.expect(jsonData.username).to.eql(pm.environment.get("dynamic_username"));
    pm.expect(jsonData.email).to.eql(pm.environment.get("dynamic_email"));
    
    console.log("User created with ID:", jsonData.id);
});

pm.test("Generated email has valid format", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.email).to.match(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/);
});
```

5. Click en "Save" y "Send"
6. Ejecuta el request varias veces y observa que cada vez genera datos únicos

**Resultado esperado:**
- Cada ejecución genera datos diferentes
- Status 201 Created
- Response contiene los datos generados
- Email tiene formato válido

---

### Parte 1.2: Generar datos condicionales según el environment (15 minutos)

**Escenario:** Necesitas comportamientos diferentes en Development y Production.

**Pasos:**

1. Crea un nuevo request:
   - **Name**: "POST Create User - Environment Aware"
   - **Method**: POST
   - **URL**: `{{base_url}}/users`

2. Pre-request Script:

```javascript
// Obtener el nombre del environment actual
const environmentName = pm.environment.name;

console.log("=== Environment Aware Script ===");
console.log("Current environment:", environmentName);

// Configurar datos según el environment
if (environmentName === "Production") {
    // En producción, usar datos más realistas
    pm.environment.set("test_email", "admin.prod@company.com");
    pm.environment.set("test_phone", "800-123-4567");
    pm.environment.set("test_website", "company.com");
    console.log("Using PRODUCTION configuration");
    
} else if (environmentName === "Staging") {
    // En staging, usar datos de staging
    pm.environment.set("test_email", "admin.staging@company.com");
    pm.environment.set("test_phone", "800-000-0000");
    pm.environment.set("test_website", "staging.company.com");
    console.log("Using STAGING configuration");
    
} else {
    // En development, usar datos de prueba aleatorios
    const timestamp = Date.now();
    pm.environment.set("test_email", `dev_${timestamp}@test.local`);
    pm.environment.set("test_phone", "555-TEST");
    pm.environment.set("test_website", "localhost:3000");
    console.log("Using DEVELOPMENT configuration");
}

// Datos comunes para todos los environments
pm.environment.set("test_name", "Environment Test User");
pm.environment.set("test_username", `envuser_${Date.now()}`);

console.log("Email configured:", pm.environment.get("test_email"));
```

3. Body (raw - JSON):

```json
{
  "name": "{{test_name}}",
  "username": "{{test_username}}",
  "email": "{{test_email}}",
  "phone": "{{test_phone}}",
  "website": "{{test_website}}"
}
```

4. Tests:

```javascript
pm.test("User created successfully", function () {
    pm.response.to.have.status(201);
    
    const jsonData = pm.response.json();
    const environment = pm.environment.name;
    
    console.log(`User created in ${environment} environment`);
    console.log("Email used:", jsonData.email);
});
```

5. **Prueba con diferentes environments:**
   - Cambia a "Development" y ejecuta
   - Cambia a "Staging" y ejecuta
   - Observa los diferentes valores en el Console

**Resultado esperado:**
- Datos diferentes según el environment activo
- Logging claro del environment usado
- Tests pasan en todos los environments

---

## Ejercicio 2: Validaciones Avanzadas con Chai (20 minutos)

### Descripción
Implementarás validaciones complejas usando todo el poder de Chai assertions.

### Pasos:

1. Crea un request:
   - **Name**: "GET Users - Advanced Validations"
   - **Method**: GET
   - **URL**: `{{base_url}}/users`

2. Tests (validaciones avanzadas):

```javascript
// Test 1: Validaciones básicas
pm.test("Status and response time", function () {
    pm.response.to.have.status(200);
    pm.expect(pm.response.responseTime).to.be.below(2000);
    pm.response.to.have.header("Content-Type");
});

// Test 2: Validar estructura del array
pm.test("Response is valid array of users", function () {
    const users = pm.response.json();
    
    pm.expect(users).to.be.an('array');
    pm.expect(users).to.not.be.empty;
    pm.expect(users.length).to.be.above(0);
    pm.expect(users.length).to.be.at.most(100);
    
    console.log("Total users:", users.length);
});

// Test 3: Validar estructura de cada usuario
pm.test("All users have required structure", function () {
    const users = pm.response.json();
    
    users.forEach(function(user, index) {
        // Validar propiedades requeridas
        pm.expect(user, `User ${index} missing properties`).to.have.all.keys(
            'id', 'name', 'username', 'email', 
            'address', 'phone', 'website', 'company'
        );
        
        // Validar tipos de datos
        pm.expect(user.id, `User ${index} id is not number`).to.be.a('number');
        pm.expect(user.name, `User ${index} name is not string`).to.be.a('string');
        pm.expect(user.email, `User ${index} email is not string`).to.be.a('string');
        
        // Validar que el nombre no esté vacío
        pm.expect(user.name.length, `User ${index} has empty name`).to.be.above(0);
    });
    
    console.log("All users validated successfully");
});

// Test 4: Validar emails
pm.test("All emails have valid format", function () {
    const users = pm.response.json();
    const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    
    users.forEach(function(user) {
        pm.expect(user.email, `Invalid email: ${user.email}`).to.match(emailRegex);
    });
});

// Test 5: Validar objeto address anidado
pm.test("All users have valid address structure", function () {
    const users = pm.response.json();
    
    users.forEach(function(user, index) {
        pm.expect(user.address, `User ${index} address missing`).to.be.an('object');
        pm.expect(user.address, `User ${index} address incomplete`).to.include.keys('street', 'city', 'zipcode');
        
        // Validar geo dentro de address
        pm.expect(user.address, `User ${index} missing geo`).to.have.property('geo');
        pm.expect(user.address.geo, `User ${index} geo incomplete`).to.have.all.keys('lat', 'lng');
        
        // Validar formato de coordenadas (strings)
        pm.expect(user.address.geo.lat).to.be.a('string');
        pm.expect(user.address.geo.lng).to.be.a('string');
    });
});

// Test 6: Validar rangos de IDs
pm.test("User IDs are in valid range", function () {
    const users = pm.response.json();
    
    users.forEach(function(user) {
        pm.expect(user.id).to.be.within(1, 1000);
    });
});

// Test 7: Validar unicidad de emails
pm.test("All emails are unique", function () {
    const users = pm.response.json();
    const emails = users.map(user => user.email);
    const uniqueEmails = [...new Set(emails)];
    
    pm.expect(uniqueEmails.length, "Duplicate emails found").to.eql(emails.length);
    
    console.log("All emails are unique:", uniqueEmails.length);
});

// Test 8: Estadísticas (bonus)
pm.test("Log user statistics", function () {
    const users = pm.response.json();
    
    const domains = users.map(user => user.email.split('@')[1]);
    const uniqueDomains = [...new Set(domains)];
    
    console.log("=== User Statistics ===");
    console.log("Total users:", users.length);
    console.log("Unique email domains:", uniqueDomains.length);
    console.log("Domains:", uniqueDomains);
});
```

3. Click en "Save" y "Send"
4. Revisa que todos los tests pasen
5. Examina el Console para ver las estadísticas

**Resultado esperado:**
- 8 tests pasan exitosamente
- Validaciones exhaustivas de estructura y datos
- Estadísticas útiles en el Console

---

## Ejercicio 3: Extraer y Guardar Datos (15 minutos)

### Descripción
Aprenderás diferentes técnicas para extraer datos del response y almacenarlos estratégicamente.

### Parte 3.1: Extracción básica

**Pasos:**

1. Crea un request:
   - **Name**: "GET User - Extract Data"
   - **Method**: GET
   - **URL**: `{{base_url}}/users/1`

2. Tests:

```javascript
pm.test("Extract and save user data", function () {
    pm.response.to.have.status(200);
    
    const user = pm.response.json();
    
    // Extraer valores simples
    pm.environment.set("saved_user_id", user.id);
    pm.environment.set("saved_user_name", user.name);
    pm.environment.set("saved_user_email", user.email);
    
    // Extraer valores anidados
    pm.environment.set("saved_user_city", user.address.city);
    pm.environment.set("saved_user_lat", user.address.geo.lat);
    pm.environment.set("saved_user_lng", user.address.geo.lng);
    
    // Extraer y guardar objeto completo (como string JSON)
    pm.environment.set("saved_user_address", JSON.stringify(user.address));
    pm.environment.set("saved_user_company", JSON.stringify(user.company));
    
    console.log("=== Data Extracted ===");
    console.log("User ID:", pm.environment.get("saved_user_id"));
    console.log("User Name:", pm.environment.get("saved_user_name"));
    console.log("User City:", pm.environment.get("saved_user_city"));
    console.log("Full Address:", pm.environment.get("saved_user_address"));
});
```

3. Click en "Save" y "Send"
4. Ve al tab "Environments" y verifica que todas las variables se guardaron

---

### Parte 3.2: Extracción de arrays

**Pasos:**

1. Crea un request:
   - **Name**: "GET Users - Extract Array Data"
   - **Method**: GET
   - **URL**: `{{base_url}}/users`

2. Tests:

```javascript
pm.test("Extract data from array", function () {
    const users = pm.response.json();
    
    // Extraer primer usuario
    if (users && users.length > 0) {
        const firstUser = users[0];
        pm.environment.set("first_user_id", firstUser.id);
        console.log("First user ID:", firstUser.id);
    }
    
    // Extraer último usuario
    if (users && users.length > 0) {
        const lastUser = users[users.length - 1];
        pm.environment.set("last_user_id", lastUser.id);
        console.log("Last user ID:", lastUser.id);
    }
    
    // Extraer usuario aleatorio
    if (users && users.length > 0) {
        const randomIndex = Math.floor(Math.random() * users.length);
        const randomUser = users[randomIndex];
        pm.environment.set("random_user_id", randomUser.id);
        console.log("Random user ID:", randomUser.id);
    }
    
    // Extraer todos los IDs en un array
    const allIds = users.map(user => user.id);
    pm.environment.set("all_user_ids", JSON.stringify(allIds));
    console.log("All user IDs:", allIds);
    
    // Extraer usuarios de una ciudad específica
    const berlinUsers = users.filter(user => 
        user.address.city === "Gwenborough"
    );
    if (berlinUsers.length > 0) {
        pm.environment.set("berlin_user_id", berlinUsers[0].id);
        console.log("Found user from Gwenborough:", berlinUsers[0].id);
    }
    
    // Buscar usuario por email
    const targetUser = users.find(user => 
        user.email === "Sincere@april.biz"
    );
    if (targetUser) {
        pm.environment.set("target_user_id", targetUser.id);
        console.log("Found target user:", targetUser.id);
    }
});
```

3. Click en "Save" y "Send"
4. Revisa las variables creadas en el Environment

**Resultado esperado:**
- Variables con IDs de diferentes usuarios extraídos
- Logging claro de los datos extraídos
- Comprensión de diferentes técnicas de extracción

---

## Ejercicio 4: Flujo Encadenado de Users → Posts → Comments (30 minutos)

### Descripción
Crearás un flujo completo que navega a través de recursos relacionados, extrayendo y usando datos entre requests.

### Request 1: GET List Users

**Pasos:**

1. Crea una nueva carpeta: "Flow: Users-Posts-Comments"
2. Dentro de la carpeta, crea el primer request:
   - **Name**: "1. GET List Users"
   - **Method**: GET
   - **URL**: `{{base_url}}/users`

3. Tests:

```javascript
console.log("=== STEP 1: GET USERS ===");

pm.test("Users retrieved successfully", function () {
    pm.response.to.have.status(200);
    
    const users = pm.response.json();
    pm.expect(users).to.be.an('array');
    pm.expect(users.length).to.be.above(0);
});

pm.test("Extract first user ID", function () {
    const users = pm.response.json();
    
    if (users && users.length > 0) {
        const firstUser = users[0];
        
        // Guardar datos del primer usuario
        pm.environment.set("flow_user_id", firstUser.id);
        pm.environment.set("flow_user_name", firstUser.name);
        pm.environment.set("flow_user_email", firstUser.email);
        
        console.log("Selected User ID:", firstUser.id);
        console.log("User Name:", firstUser.name);
        console.log("Next: Get posts for this user");
    }
});
```

4. Click en "Save" y "Send"

---

### Request 2: GET User Posts

**Pasos:**

1. En la misma carpeta, crea:
   - **Name**: "2. GET User Posts"
   - **Method**: GET
   - **URL**: `{{base_url}}/users/{{flow_user_id}}/posts`

2. Tests:

```javascript
console.log("=== STEP 2: GET USER POSTS ===");
console.log("Getting posts for user ID:", pm.environment.get("flow_user_id"));

pm.test("Posts retrieved successfully", function () {
    pm.response.to.have.status(200);
    
    const posts = pm.response.json();
    pm.expect(posts).to.be.an('array');
});

pm.test("Validate posts belong to correct user", function () {
    const posts = pm.response.json();
    const userId = parseInt(pm.environment.get("flow_user_id"));
    
    posts.forEach(function(post) {
        pm.expect(post.userId).to.eql(userId);
    });
    
    console.log("All posts belong to user", userId);
    console.log("Total posts:", posts.length);
});

pm.test("Extract first post ID", function () {
    const posts = pm.response.json();
    
    if (posts && posts.length > 0) {
        const firstPost = posts[0];
        
        // Guardar datos del primer post
        pm.environment.set("flow_post_id", firstPost.id);
        pm.environment.set("flow_post_title", firstPost.title);
        
        console.log("Selected Post ID:", firstPost.id);
        console.log("Post Title:", firstPost.title.substring(0, 50) + "...");
        console.log("Next: Get comments for this post");
    }
});
```

3. Click en "Save" y "Send"

---

### Request 3: GET Post Comments

**Pasos:**

1. Crea:
   - **Name**: "3. GET Post Comments"
   - **Method**: GET
   - **URL**: `{{base_url}}/posts/{{flow_post_id}}/comments`

2. Tests:

```javascript
console.log("=== STEP 3: GET POST COMMENTS ===");
console.log("Getting comments for post ID:", pm.environment.get("flow_post_id"));

pm.test("Comments retrieved successfully", function () {
    pm.response.to.have.status(200);
    
    const comments = pm.response.json();
    pm.expect(comments).to.be.an('array');
});

pm.test("Validate comments belong to correct post", function () {
    const comments = pm.response.json();
    const postId = parseInt(pm.environment.get("flow_post_id"));
    
    comments.forEach(function(comment) {
        pm.expect(comment.postId).to.eql(postId);
    });
    
    console.log("All comments belong to post", postId);
    console.log("Total comments:", comments.length);
});

pm.test("Validate comment structure", function () {
    const comments = pm.response.json();
    
    comments.forEach(function(comment) {
        pm.expect(comment).to.have.all.keys('postId', 'id', 'name', 'email', 'body');
        pm.expect(comment.email).to.include('@');
    });
});

pm.test("Display flow summary", function () {
    console.log("=== FLOW COMPLETED ===");
    console.log("User:", pm.environment.get("flow_user_name"));
    console.log("User Email:", pm.environment.get("flow_user_email"));
    console.log("Post Title:", pm.environment.get("flow_post_title"));
    console.log("Total Comments:", pm.response.json().length);
});
```

3. Click en "Save" y "Send"

**Para ejecutar el flujo completo:**
1. Ejecuta "1. GET List Users"
2. Ejecuta "2. GET User Posts"
3. Ejecuta "3. GET Post Comments"
4. Revisa el Console para ver el flujo completo

**Resultado esperado:**
- Datos fluyen correctamente entre los 3 requests
- Validaciones confirman las relaciones correctas
- Console muestra un resumen claro del flujo

---

## Ejercicio 5: CRUD Completo Encadenado (30 minutos)

### Descripción
Implementarás un flujo CRUD completo donde cada operación depende de la anterior.

### Request 1: POST Create User

**Pasos:**

1. Crea una nueva carpeta: "Flow: Complete CRUD"
2. Crea el primer request:
   - **Name**: "1. POST Create User"
   - **Method**: POST
   - **URL**: `{{base_url}}/users`

3. Pre-request Script:

```javascript
console.log("=== CRUD FLOW: CREATE USER ===");

// Generar datos únicos
const timestamp = Date.now();
const uniqueId = Math.floor(Math.random() * 10000);

pm.environment.set("crud_name", `CRUD Test User ${timestamp}`);
pm.environment.set("crud_username", `crud_user_${uniqueId}`);
pm.environment.set("crud_email", `crud_${timestamp}@test.com`);
pm.environment.set("crud_phone", `555-${uniqueId}`);

console.log("Generated data:");
console.log("Name:", pm.environment.get("crud_name"));
console.log("Email:", pm.environment.get("crud_email"));
```

4. Body (raw - JSON):

```json
{
  "name": "{{crud_name}}",
  "username": "{{crud_username}}",
  "email": "{{crud_email}}",
  "phone": "{{crud_phone}}",
  "address": {
    "street": "Test Street",
    "city": "Test City",
    "zipcode": "12345"
  },
  "company": {
    "name": "Test Company",
    "catchPhrase": "Testing APIs"
  }
}
```

5. Tests:

```javascript
pm.test("User created successfully", function () {
    pm.response.to.have.status(201);
});

pm.test("Save created user ID", function () {
    const user = pm.response.json();
    
    pm.expect(user).to.have.property('id');
    pm.environment.set("crud_user_id", user.id);
    
    console.log("User created with ID:", user.id);
    console.log("Next: Retrieve the created user");
});

pm.test("Verify sent data in response", function () {
    const user = pm.response.json();
    
    pm.expect(user.name).to.eql(pm.environment.get("crud_name"));
    pm.expect(user.email).to.eql(pm.environment.get("crud_email"));
});
```

6. Click en "Save" y "Send"

---

### Request 2: GET Retrieve Created User

**Pasos:**

1. Crea:
   - **Name**: "2. GET Retrieve Created User"
   - **Method**: GET
   - **URL**: `{{base_url}}/users/{{crud_user_id}}`

2. Tests:

```javascript
console.log("=== CRUD FLOW: RETRIEVE USER ===");
console.log("Retrieving user ID:", pm.environment.get("crud_user_id"));

pm.test("User retrieved successfully", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify user data matches creation", function () {
    const user = pm.response.json();
    
    pm.expect(user.id).to.eql(parseInt(pm.environment.get("crud_user_id")));
    pm.expect(user.email).to.eql(pm.environment.get("crud_email"));
    
    console.log("User verified:");
    console.log("ID:", user.id);
    console.log("Name:", user.name);
    console.log("Email:", user.email);
    console.log("Next: Update the user");
});
```

3. Click en "Save" y "Send"

---

### Request 3: PUT Update User

**Pasos:**

1. Crea:
   - **Name**: "3. PUT Update User"
   - **Method**: PUT
   - **URL**: `{{base_url}}/users/{{crud_user_id}}`

2. Pre-request Script:

```javascript
console.log("=== CRUD FLOW: UPDATE USER ===");

// Preparar datos actualizados
pm.environment.set("crud_updated_name", pm.environment.get("crud_name") + " UPDATED");
pm.environment.set("crud_updated_phone", "555-UPDATED");

console.log("Updating user ID:", pm.environment.get("crud_user_id"));
console.log("New name:", pm.environment.get("crud_updated_name"));
```

3. Body (raw - JSON):

```json
{
  "id": {{crud_user_id}},
  "name": "{{crud_updated_name}}",
  "username": "{{crud_username}}",
  "email": "{{crud_email}}",
  "phone": "{{crud_updated_phone}}",
  "address": {
    "street": "Updated Street",
    "city": "Updated City",
    "zipcode": "99999"
  },
  "company": {
    "name": "Updated Company",
    "catchPhrase": "Updated catchphrase"
  }
}
```

4. Tests:

```javascript
pm.test("User updated successfully", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify updated data", function () {
    const user = pm.response.json();
    
    pm.expect(user.name).to.eql(pm.environment.get("crud_updated_name"));
    pm.expect(user.phone).to.eql(pm.environment.get("crud_updated_phone"));
    
    console.log("User updated successfully");
    console.log("New name:", user.name);
    console.log("New phone:", user.phone);
    console.log("Next: Partially update the user with PATCH");
});
```

5. Click en "Save" y "Send"

---

### Request 4: PATCH Partial Update

**Pasos:**

1. Crea:
   - **Name**: "4. PATCH Partial Update User"
   - **Method**: PATCH
   - **URL**: `{{base_url}}/users/{{crud_user_id}}`

2. Pre-request Script:

```javascript
console.log("=== CRUD FLOW: PARTIAL UPDATE ===");

const newEmail = `updated_${Date.now()}@test.com`;
pm.environment.set("crud_final_email", newEmail);

console.log("Patching user ID:", pm.environment.get("crud_user_id"));
console.log("New email:", newEmail);
```

3. Body (raw - JSON):

```json
{
  "email": "{{crud_final_email}}",
  "phone": "555-FINAL"
}
```

4. Tests:

```javascript
pm.test("User partially updated", function () {
    pm.response.to.have.status(200);
});

pm.test("Verify patched fields", function () {
    const user = pm.response.json();
    
    pm.expect(user.email).to.eql(pm.environment.get("crud_final_email"));
    
    console.log("User patched successfully");
    console.log("Final email:", user.email);
    console.log("Next: Delete the user");
});
```

5. Click en "Save" y "Send"

---

### Request 5: DELETE User

**Pasos:**

1. Crea:
   - **Name**: "5. DELETE User"
   - **Method**: DELETE
   - **URL**: `{{base_url}}/users/{{crud_user_id}}`

2. Tests:

```javascript
console.log("=== CRUD FLOW: DELETE USER ===");
console.log("Deleting user ID:", pm.environment.get("crud_user_id"));

pm.test("User deleted successfully", function () {
    pm.response.to.have.status(200);
});

pm.test("Cleanup variables", function () {
    // Limpiar todas las variables del flujo CRUD
    pm.environment.unset("crud_user_id");
    pm.environment.unset("crud_name");
    pm.environment.unset("crud_username");
    pm.environment.unset("crud_email");
    pm.environment.unset("crud_phone");
    pm.environment.unset("crud_updated_name");
    pm.environment.unset("crud_updated_phone");
    pm.environment.unset("crud_final_email");
    
    console.log("User deleted");
    console.log("Variables cleaned up");
    console.log("=== CRUD FLOW COMPLETED ===");
});
```

3. Click en "Save" y "Send"

---

### Request 6: GET Verify Deletion (Opcional)

**Pasos:**

1. Crea:
   - **Name**: "6. GET Verify Deletion (404 Expected)"
   - **Method**: GET
   - **URL**: `{{base_url}}/users/{{crud_user_id}}`

2. Tests:

```javascript
console.log("=== VERIFY DELETION ===");

pm.test("User not found (404)", function () {
    // En JSONPlaceholder, esto retorna 200 con datos porque no elimina realmente
    // En una API real, esperarías 404
    
    if (pm.response.code === 404) {
        console.log("User successfully deleted (404 confirmed)");
    } else {
        console.log("Note: JSONPlaceholder doesn't actually delete resources");
        console.log("In a real API, this should return 404");
    }
});
```

---

**Para ejecutar el flujo CRUD completo:**
1. Ejecuta los 5 requests en orden (1 → 2 → 3 → 4 → 5)
2. Revisa el Console para ver el flujo completo documentado
3. Verifica que las variables se crearon y limpiaron correctamente

**Resultado esperado:**
- Flujo CRUD completo funcional
- Datos fluyen correctamente entre todos los requests
- Variables se limpian al final
- Logging claro de cada paso

---

## Autoevaluación

### Preguntas Teóricas

1. **¿Cuándo se ejecutan los Pre-request Scripts?**
   - [ ] Después de recibir el response
   - [ ] Antes de enviar el request
   - [ ] Durante el envío del request
   - [ ] Solo cuando hay errores

2. **¿Qué diferencia hay entre `.equal()` y `.eql()` en Chai?**
   
   Respuesta: _______________________________________________________

3. **¿Qué scope de variable usarías para datos que solo necesitas en un flujo temporal?**
   - [ ] Variables globales
   - [ ] Variables de colección
   - [ ] Variables de entorno
   - [ ] Cualquiera, no importa

4. **¿Por qué es importante verificar que un array no está vacío antes de acceder a `array[0]`?**
   
   Respuesta: _______________________________________________________

### Preguntas Prácticas

5. **Escribe el código para extraer y guardar el email del primer usuario de un array:**

```javascript
const users = pm.response.json();

// Tu código aquí




```

6. **Escribe un test que valide que todos los usuarios tienen un email válido:**

```javascript
pm.test("All users have valid email", function () {
    const users = pm.response.json();
    
    // Tu código aquí
    
    
});
```

---

## Soluciones de Autoevaluación

### Respuestas

1. **Respuesta correcta:** Antes de enviar el request

2. **`.equal()` vs `.eql()`:**  
   `.equal()` compara valores primitivos con igualdad estricta (===). `.eql()` compara objetos y arrays por sus valores, no por referencia (igualdad profunda).

3. **Respuesta correcta:** Variables de entorno (se pueden limpiar fácilmente al final del flujo)

4. **Razón:**  
   Si el array está vacío, acceder a `array[0]` retorna `undefined`, lo que causará errores al intentar acceder a propiedades de `undefined` (ej: `undefined.id`).

5. **Solución:**
```javascript
const users = pm.response.json();

if (users && users.length > 0) {
    const firstUserEmail = users[0].email;
    pm.environment.set("first_user_email", firstUserEmail);
    console.log("First user email:", firstUserEmail);
}
```

6. **Solución:**
```javascript
pm.test("All users have valid email", function () {
    const users = pm.response.json();
    const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    
    users.forEach(function(user) {
        pm.expect(user.email).to.match(emailRegex);
    });
});
```

---

## Desafío Final: Flujo Completo de Posts (Opcional)

### Objetivo
Crear un flujo completo que:
1. Liste usuarios y seleccione uno
2. Cree un post para ese usuario
3. Obtenga el post creado
4. Actualice el post
5. Obtenga los comments del post
6. Elimine el post
7. Limpie las variables

### Requisitos

**Carpeta:** "Challenge: Complete Posts Flow"

**Requests a crear:**
1. GET Users → Extraer `user_id`
2. POST Create Post → Usar `user_id`, guardar `post_id`
3. GET Retrieve Post → Usar `post_id`
4. PUT Update Post → Actualizar título y body
5. GET Post Comments → Validar estructura
6. DELETE Post → Limpiar variables

**Validaciones requeridas:**
- Todos los requests deben tener status code correcto
- Validar tipos de datos en cada response
- Verificar que el `userId` es correcto en el post
- Validar que los comments tienen `postId` correcto
- Logging claro en cada paso

**Bonus:**
- Generar títulos y body aleatorios para el post
- Validar que el post actualizado tiene los nuevos datos
- Crear estadísticas del flujo al final

---

## Resumen del Laboratorio

En este laboratorio has aprendido:

✅ Generar datos dinámicos con Pre-request Scripts  
✅ Usar condicionales según el environment activo  
✅ Implementar validaciones avanzadas con Chai  
✅ Extraer datos del response (valores simples, anidados, arrays)  
✅ Guardar datos en variables de diferentes scopes  
✅ Encadenar requests para crear flujos complejos  
✅ Implementar flujos CRUD completos  
✅ Limpiar variables al finalizar flujos  
✅ Documentar flujos con logging efectivo  

**Habilidades clave adquiridas:**
- Scripting avanzado en Postman
- Validaciones exhaustivas
- Gestión de flujos de datos
- Debugging efectivo
- Buenas prácticas de testing

---

## Próximos Pasos

Una vez completado este laboratorio, estás preparado para el tema final:

- **Tema 4**: "Testing con Postman - Automatización de Pruebas"
  - Collection Runner
  - Newman CLI
  - Integración con CI/CD
  - Scheduled runs

---

## Recursos Adicionales

- **Postman Scripting Guide**: https://learning.postman.com/docs/writing-scripts/intro-to-scripts/
- **Chai Assertion Library**: https://www.chaijs.com/api/bdd/
- **JavaScript Array Methods**: https://developer.mozilla.org/es/docs/Web/JavaScript/Reference/Global_Objects/Array
- **Postman Community Examples**: https://community.postman.com/

---

**¡Excelente trabajo completando este laboratorio avanzado!**

*Laboratorio generado para el curso de API Testing - Postman y SoapUI*
