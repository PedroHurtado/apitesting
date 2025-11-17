# Laboratorio 1.5: Servicios CRUD

## Objetivo
Practicar las cuatro operaciones CRUD (Create, Read, Update, Delete) usando Postman, comprendiendo cómo se mapean a los métodos HTTP y dominando el flujo completo de gestión de recursos.

## Duración Estimada
60 minutos

## Requisitos Previos
- Postman instalado
- Conocimientos de métodos HTTP
- Conexión a Internet

## Preparación

Crea una nueva colección en Postman llamada "Laboratorio CRUD Completo".

## Ejercicio 1: CRUD Completo de Posts

Realizaremos un ciclo CRUD completo usando JSONPlaceholder.

### Actividad 1.1: CREATE - Crear un Nuevo Post

**Objetivo**: Crear un recurso nuevo usando POST.

1. **Configurar la petición**:
   - Nombre: "Crear Post"
   - Método: POST
   - URL: `https://jsonplaceholder.typicode.com/posts`

2. **Configurar Headers**:
   - Ve a la pestaña "Headers"
   - Verifica que esté: `Content-Type: application/json`

3. **Configurar el Body**:
   - Pestaña "Body"
   - Selecciona "raw" y "JSON"
   - Ingresa:
```json
{
  "title": "Mi artículo sobre REST",
  "body": "REST es un estilo arquitectónico para diseñar APIs web. Utiliza HTTP de forma eficiente y proporciona una interfaz uniforme.",
  "userId": 1
}
```

4. **Enviar y analizar**:
   - Clic en "Send"
   - **Código esperado**: 201 Created
   - **Observa la respuesta**: Incluye un `id` (ej: 101)
   - **Nota el ID** para usarlo en los siguientes pasos

**Preguntas**:
- ¿Por qué el código es 201 y no 200?
- ¿Quién asignó el ID del recurso?
- ¿Debería haber un header Location? (Esta API no lo incluye, pero debería)

### Actividad 1.2: READ - Leer el Post Creado

**Objetivo**: Recuperar el recurso creado.

1. **Configurar la petición**:
   - Nombre: "Leer Post"
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts/1`
   (Usa ID 1 porque JSONPlaceholder no persiste los datos realmente)

2. **Enviar y analizar**:
   - Clic en "Send"
   - **Código esperado**: 200 OK
   - Observa toda la información del post

3. **Probar con ID inexistente**:
   - Cambia la URL a: `https://jsonplaceholder.typicode.com/posts/9999`
   - **Código esperado**: 404 Not Found

**Preguntas**:
- ¿GET modifica el recurso?
- ¿Puedes enviar esta petición múltiples veces? ¿Tiene algún efecto secundario?

### Actividad 1.3: READ - Leer Colección con Filtros

**Objetivo**: Obtener múltiples recursos con criterios de búsqueda.

1. **Obtener todos los posts**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts`
   - Observa cuántos posts se devuelven

2. **Filtrar por usuario**:
   - URL: `https://jsonplaceholder.typicode.com/posts?userId=1`
   - Solo devuelve posts del usuario 1

3. **Filtrar por múltiples criterios**:
   - URL: `https://jsonplaceholder.typicode.com/posts?userId=1&id=5`

**Observación**: Los query parameters filtran la colección sin cambiar la URI base.

### Actividad 1.4: UPDATE - Actualización Completa (PUT)

**Objetivo**: Reemplazar completamente un recurso.

1. **Configurar la petición**:
   - Nombre: "Actualizar Post (PUT)"
   - Método: PUT
   - URL: `https://jsonplaceholder.typicode.com/posts/1`

2. **Body completo** (todos los campos):
```json
{
  "id": 1,
  "title": "Título Completamente Actualizado",
  "body": "Este es el nuevo contenido completo del post. PUT reemplaza todo el recurso.",
  "userId": 1
}
```

3. **Enviar y analizar**:
   - **Código esperado**: 200 OK
   - La respuesta muestra el recurso actualizado

4. **Probar idempotencia**:
   - Envía la misma petición 3 veces
   - El resultado es siempre el mismo

**Pregunta**: ¿Qué pasaría si omites un campo obligatorio en PUT?

### Actividad 1.5: UPDATE - Actualización Parcial (PATCH)

**Objetivo**: Modificar solo algunos campos del recurso.

1. **Configurar la petición**:
   - Nombre: "Actualizar Post (PATCH)"
   - Método: PATCH
   - URL: `https://jsonplaceholder.typicode.com/posts/1`

2. **Body parcial** (solo campos a cambiar):
```json
{
  "title": "Solo cambio el título con PATCH"
}
```

3. **Enviar y analizar**:
   - **Código esperado**: 200 OK
   - Solo el título cambió, los demás campos permanecen

**Comparación PUT vs PATCH**:
- PUT: Debes enviar el recurso completo
- PATCH: Solo envías lo que quieres cambiar

### Actividad 1.6: DELETE - Eliminar el Recurso

**Objetivo**: Eliminar un recurso.

1. **Configurar la petición**:
   - Nombre: "Eliminar Post"
   - Método: DELETE
   - URL: `https://jsonplaceholder.typicode.com/posts/1`

2. **Enviar y analizar**:
   - **Código esperado**: 200 OK (debería ser 204 No Content)
   - La respuesta puede estar vacía

3. **Verificar idempotencia**:
   - Envía DELETE de nuevo
   - En una API real, seguiría siendo exitoso (recurso ya eliminado)

4. **Intentar leer el recurso eliminado**:
   - Haz GET a `/posts/1`
   - En una API real: 404 Not Found
   - (JSONPlaceholder no elimina realmente)

## Ejercicio 2: CRUD de Usuarios

### Actividad 2.1: Ciclo Completo Guiado

Realiza el ciclo CRUD completo para usuarios:

1. **CREATE**: Crear nuevo usuario
   - POST `https://jsonplaceholder.typicode.com/users`
   - Body:
```json
{
  "name": "Ana López",
  "username": "analopez",
  "email": "ana@ejemplo.com",
  "phone": "600-123-456"
}
```

2. **READ**: Leer usuario
   - GET `https://jsonplaceholder.typicode.com/users/1`

3. **UPDATE**: Actualizar teléfono (PATCH)
   - PATCH `https://jsonplaceholder.typicode.com/users/1`
   - Body: `{"phone": "600-999-888"}`

4. **DELETE**: Eliminar usuario
   - DELETE `https://jsonplaceholder.typicode.com/users/1`

## Ejercicio 3: Manejo de Errores CRUD

### Actividad 3.1: Provocar y Manejar Errores

**Objetivo**: Entender los códigos de error en operaciones CRUD.

1. **404 Not Found** (recurso inexistente):
   - GET `https://jsonplaceholder.typicode.com/posts/99999`
   - DELETE `https://jsonplaceholder.typicode.com/posts/99999`

2. **400 Bad Request** (datos inválidos):
   - POST `https://jsonplaceholder.typicode.com/posts`
   - Body: `{"invalid": "data"}`
   (JSONPlaceholder es permisivo, pero una API real rechazaría esto)

3. **Simular validación**:
   Documenta qué validaciones deberían existir:
   - Título: obligatorio, mínimo 5 caracteres
   - Body: obligatorio
   - UserId: debe existir

## Ejercicio 4: Operaciones CRUD Anidadas

### Actividad 4.1: Recursos Relacionados

**Objetivo**: Trabajar con sub-recursos.

1. **Leer comentarios de un post**:
   - GET `https://jsonplaceholder.typicode.com/posts/1/comments`
   - Observa la jerarquía: post → comentarios

2. **Filtrar comentarios**:
   - GET `https://jsonplaceholder.typicode.com/comments?postId=1`
   - Mismos resultados, diferente URI

3. **Leer álbumes de un usuario**:
   - GET `https://jsonplaceholder.typicode.com/users/1/albums`

**Pregunta**: ¿Qué URI prefieres para sub-recursos: jerárquica o con filtros?

## Ejercicio 5: Documentar API CRUD

### Actividad 5.1: Tabla de Endpoints

Crea una tabla documentando todos los endpoints CRUD para "productos":

| Operación | Método | URI | Body | Código Éxito |
|-----------|--------|-----|------|--------------|
| Listar todos | GET | /productos | No | 200 |
| Obtener uno | GET | /productos/:id | No | 200 |
| Crear | POST | /productos | Sí | 201 |
| Actualizar completo | PUT | /productos/:id | Sí | 200 |
| Actualizar parcial | PATCH | /productos/:id | Sí | 200 |
| Eliminar | DELETE | /productos/:id | No | 204 |

## Ejercicio 6: Secuencia de Pruebas CRUD

### Actividad 6.1: Crear Test Suite

**Objetivo**: Organizar tus peticiones CRUD en una secuencia lógica.

1. **En tu colección, organiza las peticiones**:
   ```
   📁 Laboratorio CRUD Completo
      📁 Posts
         ➤ 1. Crear Post
         ➤ 2. Leer Post Creado
         ➤ 3. Actualizar Post (PUT)
         ➤ 4. Actualizar Post (PATCH)
         ➤ 5. Eliminar Post
         ➤ 6. Verificar Eliminación (debería fallar)
      📁 Usuarios
         ➤ ...
   ```

2. **Usar variables de colección**:
   - En Collection → Variables
   - Crea variable: `base_url` = `https://jsonplaceholder.typicode.com`
   - Usa en URLs: `{{base_url}}/posts`

## Ejercicio 7: Comparativa de Métodos

### Actividad 7.1: Tabla Comparativa

Completa esta tabla basándote en tu experiencia:

| Característica | POST | GET | PUT | PATCH | DELETE |
|----------------|------|-----|-----|-------|--------|
| Crea recursos | ✓ | ✗ | Raro | ✗ | ✗ |
| Lee recursos | ✗ | ✓ | ✗ | ✗ | ✗ |
| Modifica recursos | ✗ | ✗ | ✓ | ✓ | ✗ |
| Elimina recursos | ✗ | ✗ | ✗ | ✗ | ✓ |
| Idempotente | ✗ | ✓ | ✓ | ✗ | ✓ |
| Seguro | ✗ | ✓ | ✗ | ✗ | ✗ |
| Body requerido | ✓ | ✗ | ✓ | ✓ | ✗ |

## Resultados Esperados

Al finalizar este laboratorio, deberías:

1. Dominar las 4 operaciones CRUD
2. Mapear CRUD a métodos HTTP correctamente
3. Usar códigos de estado apropiados
4. Distinguir entre PUT y PATCH
5. Manejar sub-recursos y jerarquías
6. Organizar peticiones en colecciones lógicas

## Preguntas de Autoevaluación

1. ¿Qué método HTTP usas para crear un recurso?
2. ¿Cuál es la diferencia entre PUT y PATCH?
3. ¿Por qué POST no es idempotente?
4. ¿Qué código de estado devuelve un CREATE exitoso?
5. ¿GET modifica el estado del servidor?
6. ¿DELETE es idempotente? ¿Por qué?

## Exportar Tu Trabajo

Exporta tu colección:
1. Clic derecho en la colección
2. "Export"
3. Guarda el archivo JSON
4. Esto te permite compartir o respaldar tu trabajo

## Conclusión

CRUD es el corazón de la mayoría de las APIs RESTful. Dominar estas operaciones y su mapeo correcto a HTTP es fundamental para cualquier desarrollador que trabaje con APIs web. La consistencia en cómo implementas CRUD hace que tus APIs sean predecibles y fáciles de usar.

---

**Consejo Profesional**: En producción, siempre implementa validación robusta, manejo de errores detallado, y considera aspectos como autenticación, autorización y rate limiting para operaciones CRUD.
