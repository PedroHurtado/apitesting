# Laboratorio 1.3: Protocolo HTTP

## Objetivo
Practicar el uso de métodos HTTP, analizar códigos de estado y trabajar con headers en el contexto de APIs RESTful.

## Duración Estimada
60 minutos

## Requisitos Previos
- Postman instalado
- Conocimientos del laboratorio anterior
- Conexión a Internet

## Preparación

Crea una nueva colección en Postman llamada "Laboratorio HTTP - Métodos y Códigos".

## Ejercicio 1: Métodos HTTP CRUD

Utilizaremos JSONPlaceholder para simular operaciones CRUD completas.

### Actividad 1.1: GET - Lectura de Recursos

**Objetivo**: Comprender el método GET y sus respuestas.

1. **Petición: Obtener un post**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts/1`
   - Envía la petición

2. **Analizar la respuesta**:
   - Ve a la pestaña "Headers" en la respuesta
   - Busca el header `Content-Type`
   - Observa el código de estado: **200 OK**

3. **Observa las características de GET**:
   - No tiene cuerpo (body) en la petición
   - Es seguro (no modifica datos)
   - Puedes enviarla múltiples veces sin problemas

**Pregunta**: ¿Por qué el código es 200 y no 201?

### Actividad 1.2: POST - Creación de Recursos

**Objetivo**: Crear un nuevo recurso usando POST.

1. **Crear petición POST**:
   - Método: POST
   - URL: `https://jsonplaceholder.typicode.com/posts`
   
2. **Configurar el cuerpo**:
   - Ve a la pestaña "Body"
   - Selecciona "raw" y "JSON"
   - Ingresa:
   ```json
   {
     "title": "Mi primer post de prueba",
     "body": "Este es el contenido del post",
     "userId": 1
   }
   ```

3. **Configurar headers**:
   - Ve a la pestaña "Headers"
   - Asegúrate de que esté: `Content-Type: application/json`

4. **Enviar y analizar**:
   - Código de estado: **201 Created**
   - La respuesta incluye el nuevo recurso con un `id`

**Nota importante**: JSONPlaceholder simula la creación pero no persiste los datos realmente.

**Pregunta**: ¿Qué diferencia hay entre el código 200 y 201?

### Actividad 1.3: PUT - Actualización Completa

**Objetivo**: Reemplazar completamente un recurso.

1. **Crear petición PUT**:
   - Método: PUT
   - URL: `https://jsonplaceholder.typicode.com/posts/1`
   
2. **Configurar el cuerpo** (todos los campos):
   ```json
   {
     "id": 1,
     "title": "Título completamente nuevo",
     "body": "Cuerpo completamente nuevo",
     "userId": 1
   }
   ```

3. **Enviar y analizar**:
   - Código de estado: **200 OK**
   - La respuesta muestra el recurso actualizado

4. **Probar idempotencia**:
   - Envía la misma petición 3 veces
   - Observa que el resultado es siempre el mismo

**Pregunta**: ¿Por qué PUT es idempotente pero POST no?

### Actividad 1.4: PATCH - Actualización Parcial

**Objetivo**: Modificar solo algunos campos de un recurso.

1. **Crear petición PATCH**:
   - Método: PATCH
   - URL: `https://jsonplaceholder.typicode.com/posts/1`
   
2. **Configurar el cuerpo** (solo el campo a modificar):
   ```json
   {
     "title": "Solo cambio el título"
   }
   ```

3. **Enviar y comparar**:
   - Observa que solo cambió el campo especificado
   - Los demás campos permanecen igual

**Pregunta**: ¿Cuándo usarías PATCH en lugar de PUT?

### Actividad 1.5: DELETE - Eliminación de Recursos

**Objetivo**: Eliminar un recurso.

1. **Crear petición DELETE**:
   - Método: DELETE
   - URL: `https://jsonplaceholder.typicode.com/posts/1`

2. **Enviar y analizar**:
   - Código de estado: **200 OK** (en esta API)
   - Normalmente sería **204 No Content**
   - La respuesta puede estar vacía

3. **Verificar eliminación**:
   - Intenta hacer GET al mismo recurso
   - En una API real, recibirías **404 Not Found**

## Ejercicio 2: Códigos de Estado HTTP

### Actividad 2.1: Éxito (2xx)

**Ya los viste en el ejercicio anterior**:
- **200 OK**: GET, PUT, PATCH exitosos
- **201 Created**: POST exitoso

### Actividad 2.2: Error del Cliente (4xx)

**Objetivo**: Provocar y entender errores 4xx.

1. **404 Not Found**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts/999999`
   - Envía la petición
   - Observa el código **404**

2. **Analiza el significado**:
   - El recurso solicitado no existe
   - Es un error del cliente (URI incorrecta)

**Tarea**: Intenta acceder a una URI completamente incorrecta y observa el resultado.

### Actividad 2.3: Simular otros códigos

Aunque JSONPlaceholder no implementa todos los códigos, conoce cuándo se usan:

- **400 Bad Request**: Cuando envías JSON mal formado
- **401 Unauthorized**: Sin autenticación
- **403 Forbidden**: Sin permisos suficientes
- **405 Method Not Allowed**: Método incorrecto para ese recurso

## Ejercicio 3: Trabajar con Headers

### Actividad 3.1: Headers de Petición

**Objetivo**: Configurar y usar headers personalizados.

1. **Petición con headers personalizados**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts/1`
   
2. **Añadir headers**:
   - Ve a la pestaña "Headers"
   - Añade: `Accept: application/json`
   - Añade: `User-Agent: MiAplicacion/1.0`

3. **Observa los headers de respuesta**:
   - `Content-Type`: Tipo de contenido devuelto
   - `Date`: Fecha de la respuesta
   - `Cache-Control`: Directivas de caché

### Actividad 3.2: Content-Type

**Objetivo**: Entender la importancia del Content-Type.

1. **Petición POST sin Content-Type explícito**:
   - Observa que Postman lo añade automáticamente
   
2. **Verifica en Headers de la petición**:
   - Debe aparecer `Content-Type: application/json`

**Pregunta**: ¿Qué pasaría si envías JSON pero indicas `Content-Type: text/plain`?

## Ejercicio 4: Tabla Comparativa de Métodos

Completa esta tabla basándote en tus observaciones:

| Método | ¿Seguro? | ¿Idempotente? | Código Éxito | Tiene Body? |
|--------|----------|---------------|--------------|-------------|
| GET    | ✓        | ✓             | 200          | No          |
| POST   | ✗        | ✗             | 201          | Sí          |
| PUT    | ?        | ?             | ?            | ?           |
| PATCH  | ?        | ?             | ?            | ?           |
| DELETE | ?        | ?             | ?            | ?           |

**Respuestas**:
- PUT: No seguro, Idempotente, 200/204, Sí
- PATCH: No seguro, No idempotente, 200, Sí
- DELETE: No seguro, Idempotente, 200/204, No

## Ejercicio 5: Caso Práctico Completo

### Actividad 5.1: Flujo CRUD Completo

**Escenario**: Gestionar una lista de tareas (todos).

1. **Listar todas las tareas**:
   - GET `https://jsonplaceholder.typicode.com/todos`
   - Anota cuántas tareas hay

2. **Obtener una tarea específica**:
   - GET `https://jsonplaceholder.typicode.com/todos/5`
   - Observa su estructura

3. **Crear una nueva tarea**:
   - POST `https://jsonplaceholder.typicode.com/todos`
   - Body:
   ```json
   {
     "title": "Completar laboratorio HTTP",
     "completed": false,
     "userId": 1
   }
   ```
   - Verifica código 201

4. **Actualizar la tarea**:
   - PUT `https://jsonplaceholder.typicode.com/todos/5`
   - Cambia `completed` a `true`

5. **Eliminar la tarea**:
   - DELETE `https://jsonplaceholder.typicode.com/todos/5`

## Resultados Esperados

Al finalizar este laboratorio, deberías:

1. Usar correctamente los 5 métodos HTTP principales
2. Interpretar códigos de estado 2xx, 4xx
3. Configurar y leer headers HTTP
4. Distinguir entre métodos seguros e idempotentes
5. Realizar flujos CRUD completos

## Preguntas de Autoevaluación

1. ¿Por qué GET debe ser idempotente?
2. ¿Cuál es la diferencia práctica entre PUT y PATCH?
3. ¿Por qué POST no es idempotente?
4. ¿Qué código de estado usarías para "recurso creado"?
5. ¿Qué header indica el formato del contenido que envías?

## Documentación de Tu API

Crea un documento en Postman que especifique:
- Qué método usar para cada operación
- Qué código de estado esperar en cada caso
- Qué headers son obligatorios

## Conclusión

HTTP proporciona un lenguaje rico y estandarizado para interactuar con APIs RESTful. Dominar los métodos HTTP, códigos de estado y headers es esencial para crear y consumir APIs de calidad profesional.

---

**Recordatorio**: Los métodos HTTP no son arbitrarios - cada uno tiene un propósito específico y características definidas. Úsalos correctamente para crear APIs predecibles y estándar.
