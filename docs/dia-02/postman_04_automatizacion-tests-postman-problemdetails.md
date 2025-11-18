# Automatización de Tests con ProblemDetails en Postman

## 📋 Introducción

Cuando trabajamos con APIs que implementan el estándar **RFC 7807 (Problem Details)**, podemos aprovechar los **scripts a nivel de carpeta** en Postman para evitar duplicar tests y mantener nuestro código DRY (Don't Repeat Yourself).

---

## 🎯 Ventajas de Este Enfoque

- ✅ **Sin duplicación**: Las validaciones comunes se escriben una sola vez
- ✅ **Mantenible**: Los cambios se realizan en un único lugar
- ✅ **Escalable**: Los nuevos endpoints heredan automáticamente los tests
- ✅ **Específico**: Cada request solo contiene sus validaciones únicas

---

## 📁 Estructura de Carpetas Recomendada

```
📁 Users API
  📁 Success Cases (2xx)
    ├─ GET All Users
    ├─ GET User by ID
    ├─ POST Create User
    └─ PUT Update User
  
  📁 Error Cases (4xx)
    ├─ GET User Not Found (404)
    ├─ POST Invalid Data (400)
    ├─ POST Duplicate Email (409)
    ├─ DELETE Validation Error (422)
    ├─ GET Unauthorized (401)
    └─ PUT Forbidden (403)
```

---

## 🔧 Script a Nivel de Carpeta: Error Cases (4xx)

Este script se aplica automáticamente a **todos los requests** dentro de la carpeta.

### Configuración

1. Click derecho en la carpeta **"Error Cases (4xx)"**
2. Selecciona **"Edit"**
3. Ve a la pestaña **"Tests"**
4. Copia el siguiente código:

### Tests Comunes para Errores

```javascript
// ============================================
// TESTS COMUNES PARA TODOS LOS ERRORES (4xx)
// ============================================

pm.test("Response time is less than 2000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

pm.test("Content-Type is application/problem+json", function () {
    pm.response.to.have.header("Content-Type", /application\/problem\+json/);
});

pm.test("Response follows ProblemDetails RFC 7807 structure", function () {
    const jsonData = pm.response.json();
    
    // Propiedades requeridas por RFC 7807
    pm.expect(jsonData).to.have.property('type');
    pm.expect(jsonData).to.have.property('title');
    pm.expect(jsonData).to.have.property('status');
    pm.expect(jsonData).to.have.property('detail');
    pm.expect(jsonData).to.have.property('instance');
});

pm.test("Status in body matches HTTP status code", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.status).to.eql(pm.response.code);
});

pm.test("Type is a valid URI", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.type).to.match(/^https?:\/\/.+/);
});
```

### Validaciones Específicas por Código de Estado

```javascript
// ============================================
// VALIDACIONES ESPECÍFICAS POR STATUS CODE
// ============================================

const statusCode = pm.response.code;
const jsonData = pm.response.json();

switch (statusCode) {
    case 400: // Bad Request
        pm.test("400 - Bad Request: Has validation details", function () {
            pm.expect(jsonData.title).to.include("Bad Request");
            if (jsonData.errors) {
                pm.expect(jsonData.errors).to.be.an('object');
            }
        });
        break;

    case 401: // Unauthorized
        pm.test("401 - Unauthorized: Has authentication guidance", function () {
            pm.expect(jsonData.title).to.include("Unauthorized");
            pm.expect(jsonData.detail).to.exist;
        });
        
        pm.test("401 - Has WWW-Authenticate header", function () {
            pm.response.to.have.header("WWW-Authenticate");
        });
        break;

    case 403: // Forbidden
        pm.test("403 - Forbidden: Indicates insufficient permissions", function () {
            pm.expect(jsonData.title).to.include("Forbidden");
            pm.expect(jsonData.detail).to.exist;
        });
        break;

    case 404: // Not Found
        pm.test("404 - Not Found: Resource identifier mentioned", function () {
            pm.expect(jsonData.title).to.include("Not Found");
            pm.expect(jsonData.detail).to.exist;
        });
        break;

    case 409: // Conflict
        pm.test("409 - Conflict: Describes the conflict", function () {
            pm.expect(jsonData.title).to.include("Conflict");
            pm.expect(jsonData.detail).to.exist;
        });
        break;

    case 422: // Unprocessable Entity
        pm.test("422 - Validation failed: Has validation errors", function () {
            pm.expect(jsonData.title).to.include("Validation");
            pm.expect(jsonData.errors).to.exist;
            pm.expect(jsonData.errors).to.be.an('object');
            
            // Validar estructura de errores de validación
            Object.keys(jsonData.errors).forEach(field => {
                pm.expect(jsonData.errors[field]).to.be.an('array');
            });
        });
        break;

    default:
        pm.test(`Unexpected status code: ${statusCode}`, function () {
            pm.expect.fail(`Status code ${statusCode} not handled`);
        });
}
```

---

## ✅ Script a Nivel de Carpeta: Success Cases (2xx)

### Configuración

1. Click derecho en la carpeta **"Success Cases (2xx)"**
2. Selecciona **"Edit"**
3. Ve a la pestaña **"Tests"**
4. Copia el siguiente código:

```javascript
// ============================================
// TESTS COMUNES PARA CASOS DE ÉXITO (2xx)
// ============================================

pm.test("Response time is less than 1000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(1000);
});

pm.test("Status code is 2xx (Success)", function () {
    pm.expect(pm.response.code).to.be.within(200, 299);
});

pm.test("Content-Type is application/json", function () {
    pm.response.to.have.header("Content-Type", /application\/json/);
});

const statusCode = pm.response.code;

switch (statusCode) {
    case 200: // OK
        pm.test("200 - Response has data", function () {
            const jsonData = pm.response.json();
            pm.expect(jsonData).to.exist;
        });
        break;

    case 201: // Created
        pm.test("201 - Has Location header", function () {
            pm.response.to.have.header("Location");
        });
        
        pm.test("201 - Response has created resource", function () {
            const jsonData = pm.response.json();
            pm.expect(jsonData).to.have.property('id');
        });
        break;

    case 204: // No Content
        pm.test("204 - Response body is empty", function () {
            pm.expect(pm.response.text()).to.be.empty;
        });
        break;
}
```

---

## 🎯 Tests Específicos en Cada Request

Una vez configurados los scripts de carpeta, cada request individual **solo necesita sus validaciones específicas**.

### Ejemplo 1: GET User by ID (404)

```javascript
// Solo validaciones específicas de este endpoint
pm.test("User ID in error message matches requested ID", function () {
    const jsonData = pm.response.json();
    const requestedId = pm.request.url.path[pm.request.url.path.length - 1];
    pm.expect(jsonData.detail).to.include(requestedId);
});
```

### Ejemplo 2: POST Create User (409 - Duplicate Email)

```javascript
pm.test("Conflict detail mentions email duplication", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.detail.toLowerCase()).to.include('email');
    pm.expect(jsonData.detail.toLowerCase()).to.include('already exists');
});
```

### Ejemplo 3: POST Invalid Data (400)

```javascript
pm.test("Validation errors include expected fields", function () {
    const jsonData = pm.response.json();
    const expectedFields = ['Email', 'Name', 'Password'];
    
    expectedFields.forEach(field => {
        pm.expect(jsonData.errors).to.have.property(field);
    });
});
```

### Ejemplo 4: GET All Users (200)

```javascript
pm.test("Response is an array", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Array has at least 1 user", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData.length).to.be.above(0);
});

pm.test("Each user has required properties", function () {
    const jsonData = pm.response.json();
    jsonData.forEach(user => {
        pm.expect(user).to.have.property('id');
        pm.expect(user).to.have.property('name');
        pm.expect(user).to.have.property('email');
        pm.expect(user).to.have.property('username');
    });
});
```

---

## 🌍 Variables de Entorno (Opcional)

Para hacer tus tests más configurables, puedes usar variables de entorno.

### Configurar Variables

1. Click en **"Environments"** (icono de ojo en la esquina superior derecha)
2. Crea un nuevo environment llamado **"Development"**
3. Agrega las siguientes variables:

```json
{
    "baseUrl": "https://api.example.com",
    "maxResponseTime": 1000,
    "maxResponseTimeErrors": 2000,
    "problemDetailsTypeBase": "https://api.example.com/problems/"
}
```

### Usar Variables en los Tests

```javascript
pm.test("Response time is acceptable", function () {
    const maxTime = pm.response.code >= 400 
        ? pm.environment.get("maxResponseTimeErrors")
        : pm.environment.get("maxResponseTime");
    
    pm.expect(pm.response.responseTime).to.be.below(maxTime);
});

pm.test("Type URI follows standard format", function () {
    const jsonData = pm.response.json();
    const baseType = pm.environment.get("problemDetailsTypeBase");
    pm.expect(jsonData.type).to.include(baseType);
});
```

---

## 🚀 Pre-request Script a Nivel de Colección (Opcional)

Para configuración global que se aplica a **todos** los requests.

### Configuración

1. Click derecho en tu **Collection**
2. Selecciona **"Edit"**
3. Ve a la pestaña **"Pre-request Scripts"**
4. Agrega el siguiente código:

```javascript
// Agregar headers comunes
pm.request.headers.add({
    key: 'Accept',
    value: 'application/json, application/problem+json'
});

// Si usas autenticación JWT
const token = pm.environment.get("authToken");
if (token) {
    pm.request.headers.add({
        key: 'Authorization',
        value: `Bearer ${token}`
    });
}

// Log para debugging (opcional)
console.log(`Request: ${pm.request.method} ${pm.request.url}`);
```

---

## 📊 Ejemplo de Estructura ProblemDetails

Tu API debería devolver respuestas con esta estructura:

### Ejemplo 404 - Not Found

```json
{
    "type": "https://api.example.com/problems/not-found",
    "title": "Not Found",
    "status": 404,
    "detail": "User with ID 12345 was not found",
    "instance": "/api/users/12345",
    "traceId": "00-abc123..."
}
```

### Ejemplo 400 - Bad Request

```json
{
    "type": "https://api.example.com/problems/validation-error",
    "title": "One or more validation errors occurred",
    "status": 400,
    "detail": "The request contains invalid data",
    "instance": "/api/users",
    "errors": {
        "Email": ["The Email field is required."],
        "Name": ["The Name field must be at least 3 characters."],
        "Age": ["The Age field must be between 18 and 120."]
    }
}
```

### Ejemplo 409 - Conflict

```json
{
    "type": "https://api.example.com/problems/conflict",
    "title": "Conflict",
    "status": 409,
    "detail": "A user with email 'john@example.com' already exists",
    "instance": "/api/users"
}
```

---

## 🔍 Cómo Ejecutar los Tests

### Ejecutar una Carpeta Completa

1. Click derecho en la carpeta (ej: "Error Cases")
2. Selecciona **"Run folder"**
3. Se ejecutarán todos los requests con sus tests heredados

### Ejecutar toda la Colección

1. Click derecho en la colección
2. Selecciona **"Run collection"**
3. Postman ejecutará todos los requests en orden

### Ver Resultados

Los resultados mostrarán:
- ✅ Tests heredados de la carpeta (automáticos)
- ✅ Tests específicos del request
- 📊 Tiempo de respuesta
- 📈 Estadísticas generales

---

## 💡 Consejos Adicionales

### 1. Nombrado de Requests

Usa nombres descriptivos que incluyan el código de estado esperado:

```
✅ GET User by ID - Success (200)
✅ GET User by ID - Not Found (404)
✅ POST Create User - Validation Error (400)
```

### 2. Orden de Ejecución

Organiza tus requests en orden lógico:

1. Casos de éxito primero (happy path)
2. Casos de error después
3. Casos edge cases al final

### 3. Documentación

Agrega descripciones a tus requests y carpetas:

1. Click en el request
2. Ve a la descripción
3. Explica qué valida este test

### 4. Mantén los Tests Simples

- Cada test debe validar **una sola cosa**
- Usa nombres descriptivos para los tests
- Evita lógica compleja en los tests

---

## 🎓 Ejercicio Práctico

### Tarea

Crea una colección en Postman con la siguiente estructura:

1. **Carpeta "Products API"**
   - Subcarpeta "Success Cases"
     - GET All Products (200)
     - POST Create Product (201)
     - DELETE Product (204)
   
   - Subcarpeta "Error Cases"
     - GET Product Not Found (404)
     - POST Invalid Product (400)
     - POST Duplicate SKU (409)

2. Implementa los scripts de carpeta como se muestra en esta guía

3. Crea tests específicos para cada endpoint

4. Ejecuta la colección completa y verifica que todos los tests pasen

---

## 📚 Referencias

- [RFC 7807 - Problem Details for HTTP APIs](https://tools.ietf.org/html/rfc7807)
- [Postman Documentation - Writing Tests](https://learning.postman.com/docs/writing-scripts/test-scripts/)
- [ASP.NET Core - ProblemDetails](https://learn.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.mvc.problemdetails)

---

## ✨ Resumen

Con este enfoque:

- ✅ **Escribes menos código**: Los tests comunes se heredan automáticamente
- ✅ **Mantienes consistencia**: Todos los endpoints siguen las mismas validaciones
- ✅ **Facilitas el mantenimiento**: Los cambios se hacen en un solo lugar
- ✅ **Escalas fácilmente**: Nuevos endpoints se benefician automáticamente

**¡Ahora tus tests son más profesionales, mantenibles y escalables!**
