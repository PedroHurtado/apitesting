# 1.3 Protocolo HTTP

## Introducción

HTTP (HyperText Transfer Protocol) es el protocolo de comunicación que utiliza REST. Comprender HTTP es fundamental para diseñar y consumir APIs RESTful correctamente. HTTP proporciona un conjunto rico de métodos, códigos de estado y headers que permiten expresar operaciones sobre recursos de forma clara y estandarizada.

## Métodos HTTP Principales

HTTP define varios métodos (también llamados verbos) que indican la acción a realizar sobre un recurso. En REST, estos métodos tienen significados específicos.

### GET - Obtener Recursos

**Propósito**: Recuperar la representación de un recurso sin modificarlo.

**Características**:
- **Seguro**: No modifica el estado del servidor
- **Idempotente**: Múltiples peticiones idénticas tienen el mismo efecto que una sola
- **Cacheable**: Las respuestas pueden ser almacenadas en caché

**Ejemplo sencillo**:
```
GET /libros/123
```
Devuelve la información del libro con ID 123.

### POST - Crear Recursos

**Propósito**: Crear un nuevo recurso subordinado a la URI especificada.

**Características**:
- **No seguro**: Modifica el estado del servidor
- **No idempotente**: Cada petición puede crear un nuevo recurso
- **No cacheable** (generalmente)

**Ejemplo sencillo**:
```
POST /libros
Content-Type: application/json

{
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "isbn": "978-0307474728"
}
```
Crea un nuevo libro y devuelve su URI (ej: `/libros/124`).

### PUT - Actualizar o Crear Recursos

**Propósito**: Reemplazar completamente un recurso existente o crearlo si no existe.

**Características**:
- **No seguro**: Modifica el estado del servidor
- **Idempotente**: Múltiples peticiones idénticas tienen el mismo efecto
- **No cacheable**

**Ejemplo sencillo**:
```
PUT /libros/123
Content-Type: application/json

{
  "titulo": "Cien años de soledad (Edición especial)",
  "autor": "Gabriel García Márquez",
  "isbn": "978-0307474728",
  "precio": 29.99
}
```
Reemplaza completamente el recurso libro 123.

### PATCH - Actualización Parcial

**Propósito**: Modificar parcialmente un recurso existente.

**Características**:
- **No seguro**: Modifica el estado del servidor
- **No idempotente** (depende de la implementación)
- **No cacheable**

**Ejemplo sencillo**:
```
PATCH /libros/123
Content-Type: application/json

{
  "precio": 24.99
}
```
Solo actualiza el precio del libro 123, dejando los demás campos intactos.

### DELETE - Eliminar Recursos

**Propósito**: Eliminar un recurso.

**Características**:
- **No seguro**: Modifica el estado del servidor
- **Idempotente**: Eliminar el mismo recurso múltiples veces tiene el mismo efecto
- **No cacheable**

**Ejemplo sencillo**:
```
DELETE /libros/123
```
Elimina el libro con ID 123.

## Códigos de Estado HTTP

Los códigos de estado indican el resultado de la petición HTTP. Se agrupan en cinco categorías:

### 2xx - Éxito

- **200 OK**: La petición fue exitosa (GET, PUT, PATCH)
- **201 Created**: Recurso creado exitosamente (POST)
- **204 No Content**: Éxito sin contenido en la respuesta (DELETE)

**Ejemplo**:
```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 123,
  "titulo": "Cien años de soledad"
}
```

### 3xx - Redirección

- **301 Moved Permanently**: El recurso se ha movido permanentemente
- **304 Not Modified**: El recurso no ha cambiado (usado con caché)

### 4xx - Errores del Cliente

- **400 Bad Request**: La petición está mal formada
- **401 Unauthorized**: Autenticación requerida
- **403 Forbidden**: Sin permisos para acceder al recurso
- **404 Not Found**: Recurso no encontrado
- **405 Method Not Allowed**: Método HTTP no permitido para este recurso

**Ejemplo**:
```
HTTP/1.1 404 Not Found
Content-Type: application/json

{
  "error": "Libro no encontrado",
  "mensaje": "No existe un libro con ID 999"
}
```

### 5xx - Errores del Servidor

- **500 Internal Server Error**: Error genérico del servidor
- **503 Service Unavailable**: Servicio temporalmente no disponible

## Headers HTTP Importantes

Los headers proporcionan metadatos sobre la petición o respuesta.

### Headers de Petición

**Content-Type**: Indica el formato del cuerpo de la petición
```
Content-Type: application/json
```

**Accept**: Indica qué formatos acepta el cliente
```
Accept: application/json, application/xml
```

**Authorization**: Credenciales de autenticación
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

### Headers de Respuesta

**Content-Type**: Formato del cuerpo de la respuesta
```
Content-Type: application/json; charset=utf-8
```

**Location**: URI del recurso creado (con código 201)
```
Location: https://api.ejemplo.com/libros/124
```

**Cache-Control**: Directivas de caché
```
Cache-Control: max-age=3600
```

## Ejemplo Completo de Flujo HTTP

Veamos un caso completo de creación y consulta de un recurso:

### 1. Crear un libro (POST)

**Petición**:
```
POST /api/v1/libros HTTP/1.1
Host: biblioteca.com
Content-Type: application/json
Accept: application/json

{
  "titulo": "Don Quijote",
  "autor": "Miguel de Cervantes"
}
```

**Respuesta**:
```
HTTP/1.1 201 Created
Content-Type: application/json
Location: https://biblioteca.com/api/v1/libros/456

{
  "id": 456,
  "titulo": "Don Quijote",
  "autor": "Miguel de Cervantes",
  "fecha_creacion": "2024-11-17T10:30:00Z"
}
```

### 2. Consultar el libro creado (GET)

**Petición**:
```
GET /api/v1/libros/456 HTTP/1.1
Host: biblioteca.com
Accept: application/json
```

**Respuesta**:
```
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: max-age=3600

{
  "id": 456,
  "titulo": "Don Quijote",
  "autor": "Miguel de Cervantes",
  "fecha_creacion": "2024-11-17T10:30:00Z"
}
```

## Propiedades de Seguridad

### Métodos Seguros
Métodos que no modifican el estado del servidor:
- GET
- HEAD
- OPTIONS

### Métodos Idempotentes
Métodos que, al ejecutarse múltiples veces, tienen el mismo efecto que ejecutarse una vez:
- GET
- PUT
- DELETE
- HEAD
- OPTIONS

**Ejemplo de idempotencia**:
```
DELETE /libros/123  # Primera vez: elimina el libro
DELETE /libros/123  # Segunda vez: ya estaba eliminado (mismo resultado)
```

## Negociación de Contenido

HTTP permite que cliente y servidor negocien el formato de los datos mediante headers.

**Cliente solicita JSON**:
```
GET /libros/123
Accept: application/json
```

**Servidor responde en JSON**:
```
HTTP/1.1 200 OK
Content-Type: application/json

{"id": 123, "titulo": "..."}
```

## Buenas Prácticas

1. **Usa el método HTTP correcto** para cada operación
2. **Devuelve códigos de estado apropiados** que reflejen el resultado
3. **Incluye headers relevantes** (Content-Type, Location, etc.)
4. **Respeta la idempotencia** de los métodos que deben serlo
5. **Proporciona mensajes de error claros** en respuestas 4xx y 5xx

## Referencias

1. **RFC 7231 - HTTP/1.1 Semantics and Content**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc7231

2. **RFC 7230 - HTTP/1.1 Message Syntax and Routing**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc7230

3. **Mozilla Developer Network (MDN) - HTTP Methods**.  
   Disponible en: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods

4. **Mozilla Developer Network (MDN) - HTTP Status Codes**.  
   Disponible en: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status

5. **RFC 5789 - PATCH Method for HTTP**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc5789

6. **HTTP: The Definitive Guide** by David Gourley & Brian Totty. O'Reilly Media.

---

**Nota**: HTTP es mucho más que un simple protocolo de transporte; es el lenguaje que habla REST. Dominar HTTP te permitirá crear APIs más robustas y profesionales.
