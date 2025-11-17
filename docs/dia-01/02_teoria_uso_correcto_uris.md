# 1.2 Uso Correcto de URIs

## Introducción

Las URIs (Uniform Resource Identifiers) son la piedra angular de cualquier API RESTful. Una URI bien diseñada hace que la API sea intuitiva, fácil de usar y mantenible. El diseño de URIs es tan importante que puede determinar el éxito o fracaso de una API.

## Principios Fundamentales

### 1. Las URIs Deben Identificar Recursos, No Acciones

Una URI debe representar un **sustantivo** (el recurso), no un **verbo** (la acción). Las acciones se expresan mediante los métodos HTTP (GET, POST, PUT, DELETE).

**Correcto**:
```
GET /usuarios/123
DELETE /productos/456
```

**Incorrecto**:
```
GET /obtenerUsuario?id=123
POST /eliminarProducto/456
```

### 2. Usar Sustantivos en Plural

Por consistencia, se recomienda usar nombres en plural para las colecciones de recursos.

**Correcto**:
```
/usuarios
/productos
/pedidos
```

**Incorrecto**:
```
/usuario
/producto
/pedido
```

### 3. Jerarquía de Recursos

Las URIs deben reflejar las relaciones jerárquicas entre recursos.

**Ejemplo**:
```
/usuarios/123/pedidos          # Todos los pedidos del usuario 123
/usuarios/123/pedidos/789      # Pedido específico 789 del usuario 123
/categorias/10/productos       # Productos de la categoría 10
```

### 4. Usar Minúsculas y Guiones

Para mantener la consistencia y evitar problemas, usa minúsculas y guiones para separar palabras.

**Correcto**:
```
/historiales-medicos
/datos-personales
```

**Incorrecto**:
```
/HistorialesMedicos
/datos_personales
/DatosPersonales
```

## Convenciones de Nomenclatura

### Colecciones vs Elementos Individuales

```
GET /productos              # Colección: devuelve lista de productos
GET /productos/123          # Elemento: devuelve un producto específico
```

### Sub-recursos

Cuando un recurso pertenece a otro, la URI debe reflejarlo:

```
/autores/45/libros          # Libros del autor 45
/empresas/89/empleados/12   # Empleado 12 de la empresa 89
```

## Ejemplo Sencillo: API de Biblioteca

Veamos cómo diseñar URIs para una biblioteca:

```
# Gestión de libros
GET    /libros                    # Obtener todos los libros
GET    /libros/234                # Obtener libro específico
POST   /libros                    # Crear nuevo libro
PUT    /libros/234                # Actualizar libro 234
DELETE /libros/234                # Eliminar libro 234

# Relación libro-autor
GET    /libros/234/autores        # Autores del libro 234
GET    /autores/56/libros         # Libros del autor 56

# Préstamos
GET    /usuarios/12/prestamos     # Préstamos del usuario 12
POST   /prestamos                 # Crear nuevo préstamo
```

## Uso de Query Parameters

Los parámetros de consulta se utilizan para **filtrar, ordenar o paginar** colecciones, no para identificar recursos.

**Correcto**:
```
GET /productos?categoria=electronica&precio_max=500
GET /usuarios?ciudad=madrid&ordenar=nombre
GET /articulos?pagina=2&limite=20
```

**Incorrecto**:
```
GET /productos?accion=listar
GET /usuarios?operacion=buscar&id=123
```

## Versionado de APIs

Incluir la versión en la URI ayuda a mantener retrocompatibilidad:

```
/v1/productos
/v2/productos
```

O mediante headers:
```
Accept: application/vnd.miapi.v1+json
```

## Patrones Comunes a Evitar

### ❌ Acciones en la URI
```
/crearUsuario
/eliminarProducto
/actualizarPedido
```

### ❌ Verbos HTTP en la URI
```
/get/usuarios
/post/productos
```

### ❌ Extensiones de archivo
```
/usuarios/123.json
/productos/456.xml
```
(Usar header Content-Type en su lugar)

### ❌ URIs excesivamente largas
```
/departamentos/10/subdepartamentos/5/secciones/3/empleados/123/tareas/789
```
(Considerar simplificar: `/tareas/789`)

## Buenas Prácticas

1. **Mantén las URIs simples y predecibles**
2. **Usa sustantivos, no verbos**
3. **Refleja la jerarquía lógica**
4. **Sé consistente en toda la API**
5. **Documenta las URIs claramente**
6. **Piensa en el usuario de la API**

## Ejemplo Completo: API de E-commerce

```
# Productos
GET    /productos
GET    /productos/789
POST   /productos
PUT    /productos/789
DELETE /productos/789

# Categorías y sus productos
GET    /categorias
GET    /categorias/12/productos

# Carrito de compra
GET    /usuarios/34/carrito
POST   /usuarios/34/carrito/items
DELETE /usuarios/34/carrito/items/567

# Pedidos
GET    /usuarios/34/pedidos
GET    /pedidos/890
POST   /pedidos

# Búsqueda y filtros
GET    /productos?buscar=laptop&precio_min=500&precio_max=1500
GET    /productos?categoria=electronica&ordenar=precio&direccion=desc
```

## Referencias

1. **RFC 3986 - Uniform Resource Identifier (URI): Generic Syntax**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc3986

2. **Fielding, R. T. (2000)**. "REST APIs must be hypertext-driven".  
   Disponible en: https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven

3. **Masse, M. (2011)**. "REST API Design Rulebook". O'Reilly Media.

4. **Google Cloud - API Design Guide**.  
   Disponible en: https://cloud.google.com/apis/design/resources

5. **Microsoft REST API Guidelines**.  
   Disponible en: https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md

6. **RESTful API Designing guidelines**.  
   Disponible en: https://restfulapi.net/resource-naming/

---

**Nota**: Un buen diseño de URIs no solo mejora la usabilidad de tu API, sino que también facilita su mantenimiento y evolución a largo plazo.
