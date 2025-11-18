# Testing con Postman - Probando

## Objetivos de Aprendizaje

Al finalizar este tema, el estudiante será capaz de:

1. Comprender y utilizar correctamente los métodos HTTP (GET, POST, PUT, PATCH, DELETE)
2. Configurar diferentes tipos de autenticación en Postman
3. Trabajar con headers personalizados y estándares
4. Manejar query parameters y path variables
5. Enviar datos en diferentes formatos de body (JSON, form-data, x-www-form-urlencoded)
6. Implementar validaciones básicas de responses
7. Utilizar la consola de Postman para debugging
8. Crear un CRUD completo contra una API REST

---

## 1. Métodos HTTP

### 1.1 Introducción a los métodos HTTP

Los métodos HTTP definen la acción que se desea realizar sobre un recurso. Son la base del protocolo REST.

**Métodos principales:**

| Método | Propósito | ¿Modifica el servidor? | Idempotente |
|--------|-----------|------------------------|-------------|
| GET | Obtener/Leer recursos | No | Sí |
| POST | Crear recursos | Sí | No |
| PUT | Actualizar recurso completo | Sí | Sí |
| PATCH | Actualizar parcialmente | Sí | No* |
| DELETE | Eliminar recursos | Sí | Sí |

*PATCH puede ser idempotente dependiendo de la implementación

### 1.2 GET - Obtener recursos

El método GET se utiliza para recuperar información del servidor sin modificarla.

**Características:**
- No debe tener efectos secundarios
- Los parámetros se envían en la URL (query string)
- Puede ser cacheado
- Idempotente: múltiples llamadas idénticas producen el mismo resultado

**Ejemplo 1: Listar todos los usuarios**
```
GET https://jsonplaceholder.typicode.com/users
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz"
  },
  ...
]
```

**Ejemplo 2: Obtener un usuario específico**
```
GET https://jsonplaceholder.typicode.com/users/1
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "Bret",
  "email": "Sincere@april.biz",
  "address": {
    "street": "Kulas Light",
    "city": "Gwenborough"
  }
}
```

### 1.3 POST - Crear recursos

El método POST se utiliza para crear nuevos recursos en el servidor.

**Características:**
- Modifica el estado del servidor
- Los datos se envían en el body del request
- No es idempotente: múltiples llamadas crean múltiples recursos
- El servidor suele retornar el recurso creado con su ID

**Ejemplo: Crear un nuevo usuario**
```
POST https://jsonplaceholder.typicode.com/users
Content-Type: application/json

{
  "name": "Juan Pérez",
  "username": "juanp",
  "email": "juan@example.com"
}
```

**Respuesta esperada:**
```json
{
  "id": 11,
  "name": "Juan Pérez",
  "username": "juanp",
  "email": "juan@example.com"
}
```

**Status code típico**: 201 Created

### 1.4 PUT - Actualizar recurso completo

El método PUT reemplaza completamente un recurso existente.

**Características:**
- Actualiza TODO el recurso
- Si envías solo algunos campos, los demás se pueden perder
- Es idempotente: múltiples llamadas con los mismos datos producen el mismo resultado
- Requiere conocer el ID del recurso

**Ejemplo: Actualizar un usuario completo**
```
PUT https://jsonplaceholder.typicode.com/users/1
Content-Type: application/json

{
  "id": 1,
  "name": "Juan Pérez Actualizado",
  "username": "juanp_nuevo",
  "email": "juan.nuevo@example.com",
  "address": {
    "street": "Nueva Calle 123",
    "city": "Madrid"
  }
}
```

**Status code típico**: 200 OK

### 1.5 PATCH - Actualizar parcialmente

El método PATCH actualiza solo los campos especificados de un recurso.

**Características:**
- Actualiza solo los campos enviados
- Más eficiente que PUT para cambios pequeños
- Los campos no enviados permanecen sin cambios
- Puede ser o no idempotente según implementación

**Ejemplo: Actualizar solo el email**
```
PATCH https://jsonplaceholder.typicode.com/users/1
Content-Type: application/json

{
  "email": "nuevo.email@example.com"
}
```

**Comparación PUT vs PATCH:**

**Con PUT**: Si solo envías el email, podrías perder el name, username, etc.  
**Con PATCH**: Solo se actualiza el email, el resto permanece igual.

### 1.6 DELETE - Eliminar recursos

El método DELETE se utiliza para eliminar recursos del servidor.

**Características:**
- Elimina el recurso especificado
- Es idempotente: eliminar múltiples veces produce el mismo resultado
- Puede retornar 204 No Content (sin body) o 200 OK con información

**Ejemplo: Eliminar un usuario**
```
DELETE https://jsonplaceholder.typicode.com/users/1
```

**Status codes típicos**:
- 204 No Content (sin body en respuesta)
- 200 OK (con información del recurso eliminado)

---

## 2. Autenticación

### 2.1 Tipos de autenticación en Postman

La autenticación verifica la identidad del cliente que hace la petición.

**Postman soporta múltiples tipos:**
- No Auth (sin autenticación)
- API Key
- Bearer Token
- Basic Auth
- OAuth 1.0
- OAuth 2.0
- Digest Auth
- AWS Signature

### 2.2 API Key

La autenticación por API Key envía una clave única que identifica al cliente.

**Ubicación de la API Key:**
- En Header
- En Query Parameters

**Ejemplo con Header:**
```
GET https://api.example.com/users
X-API-Key: abc123xyz789
```

**Configuración en Postman:**
1. Ve al tab "Authorization"
2. Selecciona "API Key" en el dropdown
3. Configura:
   - **Key**: `X-API-Key` (nombre del header)
   - **Value**: `{{api_key}}` (usar variable)
   - **Add to**: Header

**Ejemplo con Query Parameter:**
```
GET https://api.example.com/users?apikey=abc123xyz789
```

### 2.3 Bearer Token

Bearer Token es el método más común para APIs REST modernas. Generalmente usa JWT (JSON Web Tokens).

**Formato del header:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Ejemplo de uso:**
```
GET https://api.example.com/users
Authorization: Bearer {{access_token}}
```

**Configuración en Postman:**
1. Ve al tab "Authorization"
2. Selecciona "Bearer Token"
3. En "Token" ingresa: `{{access_token}}`

**Flujo típico:**
1. POST /login → Recibe access_token
2. Guardar token en variable de entorno
3. Usar token en requests subsiguientes

### 2.4 Basic Auth

Basic Authentication envía usuario y contraseña codificados en Base64.

**Formato del header:**
```
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=
```

**Configuración en Postman:**
1. Ve al tab "Authorization"
2. Selecciona "Basic Auth"
3. Ingresa:
   - **Username**: tu usuario
   - **Password**: tu contraseña

Postman automáticamente genera el header Authorization con el valor codificado.

**Ejemplo:**
```
Usuario: admin
Password: secret123
Resultado: Authorization: Basic YWRtaW46c2VjcmV0MTIz
```

**Nota de seguridad**: Basic Auth debe usarse siempre sobre HTTPS para proteger las credenciales.

### 2.5 OAuth 2.0 (Introducción)

OAuth 2.0 es el estándar de autenticación más robusto para APIs modernas.

**Flujo básico:**
1. Aplicación solicita autorización
2. Usuario otorga permiso
3. Aplicación recibe access token
4. Aplicación usa el token en cada request

**Componentes:**
- Authorization Server: Emite tokens
- Resource Server: API protegida
- Client: Tu aplicación
- Access Token: Credencial temporal

Postman puede automatizar el flujo OAuth 2.0 completo.

---

## 3. Headers

### 3.1 ¿Qué son los Headers?

Los headers HTTP proporcionan información adicional sobre el request o el response.

**Headers comunes en requests:**

| Header | Propósito | Ejemplo |
|--------|-----------|---------|
| Content-Type | Formato del body | application/json |
| Accept | Formatos aceptados en respuesta | application/json |
| Authorization | Credenciales de autenticación | Bearer token123 |
| User-Agent | Información del cliente | Postman/10.0 |
| Accept-Language | Idioma preferido | es-ES, en |

**Ejemplo de headers en un request:**
```
POST https://api.example.com/users
Content-Type: application/json
Accept: application/json
Authorization: Bearer abc123
User-Agent: MyApp/1.0
Accept-Language: es-ES
```

### 3.2 Headers personalizados

Puedes agregar headers personalizados según las necesidades de tu API.

**Ejemplos comunes:**
```
X-Request-ID: uuid-1234-5678
X-Client-Version: 2.5.0
X-Custom-Header: custom-value
```

**Configuración en Postman:**
1. Ve al tab "Headers"
2. Agrega una fila nueva
3. Key: nombre del header
4. Value: valor (puede usar variables: `{{header_value}}`)

---

## 4. Query Parameters y Path Variables

### 4.1 Query Parameters

Los query parameters son pares clave-valor que se agregan al final de la URL después del símbolo `?`.

**Formato:**
```
https://api.example.com/users?page=2&limit=10&sort=name
```

**Usos comunes:**
- Filtrado: `?status=active`
- Paginación: `?page=1&limit=20`
- Ordenamiento: `?sort=name&order=asc`
- Búsqueda: `?search=juan`

**Ejemplo práctico:**
```
GET https://jsonplaceholder.typicode.com/posts?userId=1
```
Retorna todos los posts del usuario con ID 1.

**Configuración en Postman:**
1. En la URL, después del path, agrega `?`
2. O usa el tab "Params" (más visual)
3. Agrega pares Key-Value
4. Postman construye automáticamente la URL

**Múltiples parámetros:**
```
Key: userId     Value: 1
Key: _limit     Value: 10
Key: _sort      Value: id
Key: _order     Value: desc
```

Genera: `https://jsonplaceholder.typicode.com/posts?userId=1&_limit=10&_sort=id&_order=desc`

### 4.2 Path Variables

Los path variables son valores dinámicos dentro del path de la URL.

**Formato:**
```
https://api.example.com/users/:userId/posts/:postId
```

**Ejemplo:**
```
GET https://jsonplaceholder.typicode.com/users/1/posts
```

Obtiene todos los posts del usuario con ID 1.

**Configuración en Postman:**
1. En la URL, usa `:` para indicar una variable: `/users/:id`
2. Postman detecta automáticamente la variable
3. Ve al tab "Params" → sección "Path Variables"
4. Asigna un valor: `id = 1`
5. O usa una variable: `id = {{user_id}}`

**Ejemplo con variable:**
```
URL: {{base_url}}/users/:userId/posts
Path Variable: userId = {{user_id}}
```

---

## 5. Tipos de Body

### 5.1 Introducción

El body del request contiene los datos que envías al servidor. Es usado principalmente en POST, PUT y PATCH.

**Formatos disponibles en Postman:**
- none (sin body)
- form-data
- x-www-form-urlencoded
- raw (JSON, XML, Text, HTML, JavaScript)
- binary
- GraphQL

### 5.2 JSON (raw)

JSON es el formato más utilizado en APIs REST modernas.

**Configuración:**
1. Tab "Body" → selecciona "raw"
2. Dropdown de la derecha → selecciona "JSON"
3. Postman automáticamente agrega: `Content-Type: application/json`

**Ejemplo:**
```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "age": 30,
  "active": true,
  "roles": ["admin", "user"],
  "address": {
    "city": "Madrid",
    "country": "España"
  }
}
```

**Ventajas:**
- Estructura jerárquica
- Soporte nativo en JavaScript
- Legible para humanos
- Tipos de datos (string, number, boolean, array, object)

### 5.3 form-data

Form-data permite enviar datos como formularios multipart, ideal para archivos.

**Usos:**
- Subir archivos (imágenes, PDFs, etc.)
- Formularios HTML tradicionales
- Datos mixtos (texto + archivos)

**Configuración:**
1. Tab "Body" → selecciona "form-data"
2. Agrega pares Key-Value
3. Para archivos: cambia el tipo de "Text" a "File"

**Ejemplo:**
```
Key: name          Value: Juan Pérez         Type: Text
Key: email         Value: juan@example.com   Type: Text
Key: avatar        Value: [Select File]      Type: File
```

**Content-Type generado:**
```
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
```

### 5.4 x-www-form-urlencoded

Similar a form-data pero codifica los datos como query string en el body.

**Usos:**
- Formularios HTML simples
- APIs que requieren este formato
- No soporta archivos

**Configuración:**
1. Tab "Body" → selecciona "x-www-form-urlencoded"
2. Agrega pares Key-Value

**Ejemplo:**
```
Key: username      Value: admin
Key: password      Value: secret123
Key: grant_type    Value: password
```

**Body generado:**
```
username=admin&password=secret123&grant_type=password
```

**Content-Type:**
```
Content-Type: application/x-www-form-urlencoded
```

### 5.5 binary

Binary permite enviar archivos binarios directamente.

**Usos:**
- Subir una imagen
- Subir un PDF
- Cualquier archivo binario

**Configuración:**
1. Tab "Body" → selecciona "binary"
2. Click en "Select File"
3. Elige el archivo a enviar

El archivo se envía como el body completo del request.

---

## 6. Validaciones Básicas de Responses

### 6.1 Introducción al tab Tests

Postman permite escribir validaciones automáticas en el tab "Tests".

**Validaciones comunes:**
- Status code correcto
- Tiempo de respuesta aceptable
- Estructura del response
- Valores específicos en el response
- Tipos de datos correctos

### 6.2 Validación de Status Codes

**Ejemplo: Verificar status 200**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});
```

**Ejemplo: Verificar status 201 (Created)**
```javascript
pm.test("User created successfully", function () {
    pm.response.to.have.status(201);
});
```

**Ejemplo: Verificar rango de status (2xx)**
```javascript
pm.test("Successful response", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201, 204]);
});
```

### 6.3 Validación de Response Time

**Ejemplo: Response menor a 500ms**
```javascript
pm.test("Response time is less than 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});
```

### 6.4 Validación de Headers

**Ejemplo: Verificar Content-Type**
```javascript
pm.test("Content-Type is JSON", function () {
    pm.response.to.have.header("Content-Type", "application/json; charset=utf-8");
});
```

### 6.5 Validación de Body - Estructura

**Ejemplo: Verificar que el response contiene campos específicos**
```javascript
pm.test("Response has required fields", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('name');
    pm.expect(jsonData).to.have.property('email');
});
```

### 6.6 Validación de Body - Tipos de Datos

**Ejemplo: Verificar tipos**
```javascript
pm.test("Field types are correct", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
    pm.expect(jsonData.active).to.be.a('boolean');
});
```

### 6.7 Validación de Body - Valores Específicos

**Ejemplo: Verificar valores concretos**
```javascript
pm.test("User email is correct", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.email).to.eql("juan@example.com");
});
```

### 6.8 Validaciones Múltiples

**Ejemplo completo:**
```javascript
pm.test("Complete validation for GET user", function () {
    // Status code
    pm.response.to.have.status(200);
    
    // Response time
    pm.expect(pm.response.responseTime).to.be.below(1000);
    
    // Header
    pm.response.to.have.header("Content-Type");
    
    // Body structure and types
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('name');
    pm.expect(jsonData.id).to.be.a('number');
    pm.expect(jsonData.name).to.be.a('string');
});
```

---

## 7. Postman Console para Debugging

### 7.1 ¿Qué es la Consola?

La Postman Console muestra información detallada sobre los requests y responses.

**Información que muestra:**
- Request completo (headers, body)
- Response completo
- Tiempos de ejecución
- Logs de consola (`console.log()`)
- Errores

**Abrir la consola:**
- Atajo: `Ctrl + Alt + C` (Windows/Linux) o `Cmd + Alt + C` (Mac)
- O: View → Show Postman Console

### 7.2 Uso de console.log()

Puedes usar `console.log()` en tus scripts de Pre-request o Tests.

**Ejemplo:**
```javascript
// En el tab Tests
console.log("Ejecutando test para usuario");
var jsonData = pm.response.json();
console.log("User ID:", jsonData.id);
console.log("User name:", jsonData.name);
console.log("Complete response:", jsonData);
```

**Utilidad:**
- Debugging de scripts
- Verificar valores de variables
- Entender el flujo de ejecución

### 7.3 Depuración de Problemas

**Problemas comunes y cómo debuggear:**

**1. Request no funciona:**
- Revisar en Console el request exacto que se envió
- Verificar URL completa generada
- Verificar headers enviados
- Verificar body enviado

**2. Variables no se reemplazan:**
- Usar `console.log(pm.environment.get("variable_name"))`
- Verificar que el environment está activo
- Verificar el scope de la variable

**3. Tests fallan:**
- Usar `console.log()` para ver los valores recibidos
- Verificar la estructura del response en Console

---

## 8. Referencias y Recursos

### 8.1 Documentación oficial
- **Postman Learning Center - Requests**: https://learning.postman.com/docs/sending-requests/requests/
- **Postman Learning Center - Authorization**: https://learning.postman.com/docs/sending-requests/authorization/
- **Postman Learning Center - Tests**: https://learning.postman.com/docs/writing-scripts/test-scripts/

### 8.2 Referencias HTTP
- **HTTP Methods - MDN**: https://developer.mozilla.org/es/docs/Web/HTTP/Methods
- **HTTP Status Codes - MDN**: https://developer.mozilla.org/es/docs/Web/HTTP/Status
- **HTTP Headers - MDN**: https://developer.mozilla.org/es/docs/Web/HTTP/Headers

### 8.3 APIs para practicar
- **JSONPlaceholder**: https://jsonplaceholder.typicode.com/ (CRUD completo)
- **ReqRes**: https://reqres.in/ (Incluye autenticación simulada)
- **Postman Echo**: https://postman-echo.com/ (Testing de requests)

---

## 9. Preguntas de Autoevaluación

1. ¿Cuál es la diferencia entre PUT y PATCH?
2. ¿Qué significa que un método HTTP sea "idempotente"?
3. ¿Cuál es el método HTTP apropiado para crear un nuevo recurso?
4. ¿En qué parte del request se envían los query parameters?
5. ¿Qué header se usa para indicar el formato del body que estás enviando?
6. ¿Cuál es el formato de body más común en APIs REST modernas?
7. ¿Para qué sirve Bearer Token?
8. ¿Qué status code esperas recibir después de un POST exitoso?
9. ¿Cómo se referencian las path variables en Postman?
10. ¿Para qué sirve la Postman Console?

---

## Resumen

En este tema hemos aprendido:

- Los **métodos HTTP** (GET, POST, PUT, PATCH, DELETE) y sus casos de uso
- Diferentes tipos de **autenticación** (API Key, Bearer Token, Basic Auth, OAuth 2.0)
- Cómo trabajar con **headers** estándares y personalizados
- La diferencia entre **query parameters** y **path variables**
- Los diferentes **formatos de body** (JSON, form-data, x-www-form-urlencoded, binary)
- Cómo implementar **validaciones básicas** en el tab Tests
- El uso de la **Postman Console** para debugging
- Las bases para implementar un **CRUD completo**

---

