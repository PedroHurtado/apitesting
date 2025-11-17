# 1.5 Servicios CRUD

## Introducción

CRUD es el acrónimo de las cuatro operaciones básicas que se pueden realizar sobre datos persistentes: **Create** (Crear), **Read** (Leer), **Update** (Actualizar) y **Delete** (Eliminar). En el contexto de REST, estas operaciones se mapean naturalmente a los métodos HTTP, creando una interfaz uniforme y predecible para manipular recursos.

## Mapeo CRUD a HTTP

La elegancia de REST radica en cómo las operaciones CRUD se alinean perfectamente con los métodos HTTP:

| Operación CRUD | Método HTTP | Descripción |
|----------------|-------------|-------------|
| **Create** | POST | Crear un nuevo recurso |
| **Read** | GET | Leer/obtener un recurso existente |
| **Update** | PUT / PATCH | Actualizar un recurso existente |
| **Delete** | DELETE | Eliminar un recurso |

## Create - Crear Recursos (POST)

### Propósito
Crear un nuevo recurso dentro de una colección.

### Características
- Se envía a la URI de la **colección**, no del recurso individual
- El servidor asigna el identificador del nuevo recurso
- Devuelve código **201 Created**
- Incluye header **Location** con la URI del recurso creado

### Ejemplo Sencillo

**Petición**:
```http
POST /api/productos HTTP/1.1
Host: tienda.com
Content-Type: application/json

{
  "nombre": "Teclado Mecánico",
  "precio": 89.99,
  "categoria": "Periféricos",
  "stock": 50
}
```

**Respuesta**:
```http
HTTP/1.1 201 Created
Location: https://tienda.com/api/productos/789
Content-Type: application/json

{
  "id": 789,
  "nombre": "Teclado Mecánico",
  "precio": 89.99,
  "categoria": "Periféricos",
  "stock": 50,
  "fecha_creacion": "2024-11-17T10:30:00Z"
}
```

### Puntos Clave
- URI de la colección: `/productos`
- El servidor genera el ID: `789`
- Header Location indica dónde encontrar el recurso creado

## Read - Leer Recursos (GET)

### Propósito
Obtener la representación de uno o más recursos sin modificarlos.

### Dos Variantes

#### 1. Obtener Colección
```http
GET /api/productos HTTP/1.1
Host: tienda.com
```

**Respuesta**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 789,
    "nombre": "Teclado Mecánico",
    "precio": 89.99
  },
  {
    "id": 790,
    "nombre": "Mouse Inalámbrico",
    "precio": 45.50
  }
]
```

#### 2. Obtener Recurso Individual
```http
GET /api/productos/789 HTTP/1.1
Host: tienda.com
```

**Respuesta**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 789,
  "nombre": "Teclado Mecánico",
  "precio": 89.99,
  "categoria": "Periféricos",
  "stock": 50
}
```

### Filtrado y Paginación

Para colecciones grandes, usa query parameters:

```http
GET /api/productos?categoria=Periféricos&precio_max=100&pagina=1&limite=20
```

### Puntos Clave
- GET no modifica datos (método seguro)
- Es idempotente
- Puede ser cacheado
- Código de éxito: **200 OK**

## Update - Actualizar Recursos

### PUT - Reemplazo Completo

**Propósito**: Reemplazar completamente un recurso existente.

**Petición**:
```http
PUT /api/productos/789 HTTP/1.1
Host: tienda.com
Content-Type: application/json

{
  "nombre": "Teclado Mecánico RGB",
  "precio": 99.99,
  "categoria": "Periféricos",
  "stock": 45,
  "descripcion": "Teclado con iluminación RGB"
}
```

**Respuesta**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 789,
  "nombre": "Teclado Mecánico RGB",
  "precio": 99.99,
  "categoria": "Periféricos",
  "stock": 45,
  "descripcion": "Teclado con iluminación RGB",
  "fecha_modificacion": "2024-11-17T11:00:00Z"
}
```

**Características**:
- Debes enviar **todos los campos** del recurso
- Es **idempotente**
- Puede crear el recurso si no existe (aunque es poco común)

### PATCH - Actualización Parcial

**Propósito**: Modificar solo algunos campos de un recurso.

**Petición**:
```http
PATCH /api/productos/789 HTTP/1.1
Host: tienda.com
Content-Type: application/json

{
  "precio": 94.99,
  "stock": 42
}
```

**Respuesta**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 789,
  "nombre": "Teclado Mecánico RGB",
  "precio": 94.99,
  "categoria": "Periféricos",
  "stock": 42,
  "descripcion": "Teclado con iluminación RGB"
}
```

**Características**:
- Solo envías los campos a modificar
- Más eficiente para cambios pequeños
- Menos estricto que PUT

## Delete - Eliminar Recursos (DELETE)

### Propósito
Eliminar un recurso específico.

**Petición**:
```http
DELETE /api/productos/789 HTTP/1.1
Host: tienda.com
```

**Respuesta (Opción 1 - Sin contenido)**:
```http
HTTP/1.1 204 No Content
```

**Respuesta (Opción 2 - Con confirmación)**:
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "mensaje": "Producto eliminado exitosamente",
  "id": 789
}
```

### Idempotencia
DELETE es idempotente: eliminar un recurso ya eliminado no causa error, simplemente no hace nada (o devuelve 404).

### Eliminación Lógica vs Física

**Física** (realmente elimina):
```http
DELETE /api/productos/789
→ El producto se elimina de la base de datos
```

**Lógica** (marca como eliminado):
```http
PATCH /api/productos/789
{
  "activo": false
}
→ El producto sigue en la BD pero marcado como inactivo
```

## Ejemplo Completo: Gestión de Empleados

### Create - Contratar Empleado
```http
POST /api/empleados
{
  "nombre": "Ana Martínez",
  "puesto": "Desarrolladora",
  "salario": 45000
}

→ 201 Created
   Location: /api/empleados/101
```

### Read - Consultar Empleado
```http
GET /api/empleados/101

→ 200 OK
{
  "id": 101,
  "nombre": "Ana Martínez",
  "puesto": "Desarrolladora",
  "salario": 45000,
  "fecha_contratacion": "2024-11-17"
}
```

### Update - Promocionar Empleado
```http
PATCH /api/empleados/101
{
  "puesto": "Desarrolladora Senior",
  "salario": 55000
}

→ 200 OK
```

### Delete - Despedir Empleado
```http
DELETE /api/empleados/101

→ 204 No Content
```

## Códigos de Estado en CRUD

### Operaciones Exitosas
- **200 OK**: GET, PUT, PATCH, DELETE (con cuerpo)
- **201 Created**: POST
- **204 No Content**: DELETE (sin cuerpo)

### Errores Comunes
- **400 Bad Request**: Datos inválidos en POST/PUT/PATCH
- **404 Not Found**: Recurso no existe (GET, PUT, PATCH, DELETE)
- **409 Conflict**: Conflicto (ej: duplicado en POST)
- **422 Unprocessable Entity**: Validación de negocio fallida

## Validación en CRUD

### En POST y PUT
```http
POST /api/productos
{
  "nombre": "",
  "precio": -10
}

→ 400 Bad Request
{
  "errores": [
    {"campo": "nombre", "mensaje": "El nombre es obligatorio"},
    {"campo": "precio", "mensaje": "El precio debe ser positivo"}
  ]
}
```

## Operaciones en Lote (Batch)

Algunas APIs permiten operaciones CRUD múltiples:

```http
POST /api/productos/batch
{
  "operaciones": [
    {
      "accion": "crear",
      "datos": {"nombre": "Producto A", "precio": 10}
    },
    {
      "accion": "actualizar",
      "id": 123,
      "datos": {"precio": 15}
    },
    {
      "accion": "eliminar",
      "id": 456
    }
  ]
}
```

## Buenas Prácticas

1. **Usa el método HTTP correcto** para cada operación CRUD
2. **Devuelve códigos de estado apropiados**
3. **Proporciona el recurso completo en respuestas** (incluido el ID)
4. **Valida datos antes de persistir**
5. **Incluye Location header** en respuestas 201
6. **Documenta qué campos son obligatorios**
7. **Implementa versionado** para cambios en la estructura
8. **Considera soft deletes** para datos sensibles
9. **Implementa paginación** para colecciones grandes
10. **Usa transacciones** cuando sea necesario

## Tabla Resumen

| Operación | Método | URI | Códigos Éxito | Idempotente |
|-----------|--------|-----|---------------|-------------|
| Crear | POST | /recursos | 201 | No |
| Leer todos | GET | /recursos | 200 | Sí |
| Leer uno | GET | /recursos/:id | 200 | Sí |
| Actualizar completo | PUT | /recursos/:id | 200, 204 | Sí |
| Actualizar parcial | PATCH | /recursos/:id | 200 | No* |
| Eliminar | DELETE | /recursos/:id | 200, 204 | Sí |

*PATCH puede ser idempotente dependiendo de la implementación.

## Referencias

1. **Fielding, R. T. (2000)**. "Architectural Styles and the Design of Network-based Software Architectures".  
   Disponible en: https://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm

2. **RFC 7231 - HTTP/1.1 Semantics: Methods**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc7231#section-4

3. **RFC 5789 - PATCH Method for HTTP**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc5789

4. **Masse, M. (2011)**. "REST API Design Rulebook". O'Reilly Media.

5. **Microsoft REST API Guidelines - CRUD Operations**.  
   Disponible en: https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md

---

**Nota**: Los servicios CRUD son el pan de cada día en desarrollo de APIs. Dominar estas operaciones básicas es fundamental para construir cualquier API RESTful funcional.
