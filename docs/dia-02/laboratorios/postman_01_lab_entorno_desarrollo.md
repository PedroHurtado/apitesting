# Testing con Postman - Laboratorio: Preparando el Entorno de Desarrollo

## Información del Laboratorio

**Duración estimada**: 50 minutos  
**Nivel**: Principiante  
**Prerequisitos**: 
- Postman instalado en el equipo
- Cuenta de Postman creada
- Conexión a internet

---

## Objetivos del Laboratorio

Al completar este laboratorio, serás capaz de:

1. Crear y configurar un workspace personal
2. Crear colecciones organizadas con carpetas
3. Configurar múltiples environments (Development, Staging, Production)
4. Definir y utilizar variables de diferentes alcances
5. Importar y exportar colecciones
6. Aplicar buenas prácticas de organización

---

## Ejercicio 1: Configuración Inicial del Workspace (10 minutos)

### Descripción
Crearás un workspace organizado para trabajar con la API de JSONPlaceholder.

### Pasos a seguir

**1.1 Crear un nuevo workspace**

1. Abre Postman
2. Click en el menú "Workspaces" (esquina superior izquierda)
3. Selecciona "Create Workspace"
4. Configura el workspace:
   - **Name**: "API Testing Lab"
   - **Summary**: "Workspace para prácticas de testing de APIs REST"
   - **Visibility**: Personal
5. Click en "Create Workspace"

**1.2 Crear la estructura de colecciones**

1. En el nuevo workspace, click en "Collections" en la barra lateral
2. Click en "+" o "Create a collection"
3. Nombra la colección: "JSONPlaceholder API"
4. Click derecho en la colección → "Add folder"
5. Crea las siguientes carpetas:
   - `Users Management`
   - `Posts Management`
   - `Comments Management`

**Resultado esperado:**
```
Workspace: API Testing Lab
└── Collection: JSONPlaceholder API
    ├── Folder: Users Management
    ├── Folder: Posts Management
    └── Folder: Comments Management
```

---

## Ejercicio 2: Configuración de Environments (15 minutos)

### Descripción
Configurarás tres environments diferentes para simular los entornos de desarrollo, staging y producción.

### Pasos a seguir

**2.1 Crear Environment: Development**

1. Click en el icono de "Environments" en la barra lateral (ícono de engranaje)
2. Click en "+" para crear un nuevo environment
3. Nombra el environment: "Development"
4. Agrega las siguientes variables:

| VARIABLE | INITIAL VALUE | CURRENT VALUE |
|----------|--------------|---------------|
| base_url | https://jsonplaceholder.typicode.com | https://jsonplaceholder.typicode.com |
| timeout | 5000 | 5000 |
| max_users | 10 | 10 |
| environment_name | development | development |

5. Click en "Save"

**2.2 Crear Environment: Staging**

1. Repite el proceso anterior
2. Nombra el environment: "Staging"
3. Agrega las variables:

| VARIABLE | INITIAL VALUE | CURRENT VALUE |
|----------|--------------|---------------|
| base_url | https://jsonplaceholder.typicode.com | https://jsonplaceholder.typicode.com |
| timeout | 10000 | 10000 |
| max_users | 50 | 50 |
| environment_name | staging | staging |

**2.3 Crear Environment: Production**

1. Repite el proceso anterior
2. Nombra el environment: "Production"
3. Agrega las variables:

| VARIABLE | INITIAL VALUE | CURRENT VALUE |
|----------|--------------|---------------|
| base_url | https://jsonplaceholder.typicode.com | https://jsonplaceholder.typicode.com |
| timeout | 30000 | 30000 |
| max_users | 100 | 100 |
| environment_name | production | production |

**2.4 Seleccionar environment activo**

1. En la esquina superior derecha, verás un dropdown con "No Environment"
2. Haz click y selecciona "Development"
3. Verifica que aparezca "Development" seleccionado

**Resultado esperado:**
Tres environments configurados con variables específicas para cada entorno.

---

## Ejercicio 3: Crear Requests con Variables (15 minutos)

### Descripción
Crearás requests HTTP utilizando las variables definidas en los environments.

### Pasos a seguir

**3.1 Request: GET List Users**

1. Dentro de la carpeta "Users Management", click en "Add request"
2. Configura el request:
   - **Method**: GET
   - **Name**: "GET List Users"
   - **URL**: `{{base_url}}/users`
3. Click en "Save"

**3.2 Request: GET User by ID**

1. En la misma carpeta, crea otro request:
   - **Method**: GET
   - **Name**: "GET User by ID"
   - **URL**: `{{base_url}}/users/1`
2. Click en "Save"

**3.3 Request: POST Create User**

1. Crea un nuevo request:
   - **Method**: POST
   - **Name**: "POST Create User"
   - **URL**: `{{base_url}}/users`
2. Ve al tab "Body"
3. Selecciona "raw" y "JSON"
4. Agrega el siguiente body:

```json
{
  "name": "Test User",
  "username": "testuser",
  "email": "test@example.com",
  "address": {
    "street": "Test Street",
    "city": "Test City"
  }
}
```

5. Click en "Save"

**3.4 Probar los requests**

1. Asegúrate de que "Development" está seleccionado como environment activo
2. Ejecuta "GET List Users" → Click en "Send"
3. Verifica que recibes un status 200 y una lista de usuarios en formato JSON
4. Ejecuta "GET User by ID" → Click en "Send"
5. Verifica que recibes los datos del usuario con ID 1
6. Ejecuta "POST Create User" → Click en "Send"
7. Verifica que recibes un status 201 y el usuario creado con un ID asignado

**Resultado esperado:**
Tres requests funcionales que utilizan la variable `{{base_url}}` del environment activo.

---

## Ejercicio 4: Variables de Colección (10 minutos)

### Descripción
Configurarás variables a nivel de colección para valores compartidos.

### Pasos a seguir

**4.1 Definir variables de colección**

1. Click derecho en la colección "JSONPlaceholder API"
2. Selecciona "Edit"
3. Ve al tab "Variables"
4. Agrega las siguientes variables:

| VARIABLE | INITIAL VALUE | CURRENT VALUE |
|----------|--------------|---------------|
| api_version | v1 | v1 |
| content_type | application/json | application/json |
| test_user_id | 1 | 1 |

5. Click en "Update"

**4.2 Usar variables de colección**

1. Crea un nuevo request en "Users Management":
   - **Method**: GET
   - **Name**: "GET Test User"
   - **URL**: `{{base_url}}/users/{{test_user_id}}`
2. Ve al tab "Headers"
3. Agrega un header:
   - **Key**: `Content-Type`
   - **Value**: `{{content_type}}`
4. Click en "Save"
5. Click en "Send" para probar

**Resultado esperado:**
El request debe usar tanto variables de environment (`base_url`) como de colección (`test_user_id`, `content_type`).

---

## Ejercicio 5: Importar y Exportar Colecciones (10 minutos)

### Descripción
Aprenderás a exportar tu colección y a importar colecciones externas.

### Pasos a seguir

**5.1 Exportar tu colección**

1. Click derecho en la colección "JSONPlaceholder API"
2. Selecciona "Export"
3. Selecciona "Collection v2.1 (recommended)"
4. Click en "Export"
5. Guarda el archivo con el nombre: `jsonplaceholder-api-collection.json`
6. Anota la ubicación del archivo guardado

**5.2 Exportar un environment**

1. Click en "Environments" en la barra lateral
2. Click en los tres puntos (...) junto al environment "Development"
3. Selecciona "Export"
4. Guarda el archivo con el nombre: `development-environment.json`

**5.3 Importar una colección (simulación)**

1. Click en "Import" (botón en la esquina superior izquierda)
2. Selecciona "File"
3. Busca y selecciona el archivo `jsonplaceholder-api-collection.json` que exportaste
4. Click en "Import"
5. Nota: Esto creará una copia de tu colección
6. Puedes eliminar la colección duplicada después de verificar la importación

**Resultado esperado:**
- Archivo JSON de la colección guardado en tu sistema
- Archivo JSON del environment guardado en tu sistema
- Comprensión del proceso de importación/exportación

---

## Autoevaluación

Responde las siguientes preguntas para verificar tu comprensión:

### Preguntas Teóricas

1. **¿Qué diferencia hay entre un workspace personal y un workspace de equipo?**
   - [ ] No hay diferencia
   - [ ] El workspace personal solo lo puedes ver tú, el de equipo se comparte con otros miembros
   - [ ] El workspace de equipo es de pago
   - [ ] El workspace personal no permite crear colecciones

2. **¿Cuál es la jerarquía correcta de prioridad de variables?**
   - [ ] Global > Collection > Environment > Local
   - [ ] Local > Environment > Collection > Global
   - [ ] Environment > Local > Global > Collection
   - [ ] Global > Environment > Local > Collection

3. **¿Para qué sirve el "Initial Value" de una variable de environment?**
   - [ ] Para valores que no cambian
   - [ ] Es el valor que se comparte al exportar el environment
   - [ ] Es el valor que se usa en producción
   - [ ] No sirve para nada, es opcional

4. **¿Qué ventaja tiene usar variables en lugar de valores hardcodeados?**
   - [ ] Facilita cambiar entre diferentes entornos sin modificar los requests
   - [ ] Hace que los requests se ejecuten más rápido
   - [ ] Es obligatorio en Postman
   - [ ] Ninguna ventaja particular

### Preguntas Prácticas

5. **Si tienes una variable `base_url` definida tanto en el environment como en las variables globales, ¿cuál se usará?**
   
   Respuesta: _______________

6. **¿Cómo referenciarías una variable llamada `api_token` en un header de tu request?**
   
   Respuesta: _______________

7. **¿Qué formato de archivo se usa para exportar colecciones?**
   
   Respuesta: _______________

---

## Ejercicio de Desafío (Opcional)

### Desafío: Colección Completa de Posts

**Objetivo**: Crear una colección completa para gestionar posts en JSONPlaceholder.

**Requisitos:**

1. Crear una nueva carpeta llamada "Posts Management Complete"
2. Implementar los siguientes requests:
   - GET List All Posts: `{{base_url}}/posts`
   - GET Post by ID: `{{base_url}}/posts/{{post_id}}`
   - GET Posts by User: `{{base_url}}/posts?userId={{user_id}}`
   - POST Create Post: `{{base_url}}/posts`
   - PUT Update Post: `{{base_url}}/posts/{{post_id}}`
   - DELETE Delete Post: `{{base_url}}/posts/{{post_id}}`

3. Crear variables de colección:
   - `post_id` con valor `1`
   - `user_id` con valor `1`

4. Para el POST Create Post, usar este body:
```json
{
  "title": "Test Post",
  "body": "This is a test post created from Postman",
  "userId": {{user_id}}
}
```

5. Documentar cada request con una descripción clara de su funcionalidad

6. Probar todos los requests y verificar que funcionan correctamente

**Entregable**: Exportar la colección completa y el environment utilizado.

---

## Soluciones de Autoevaluación

### Respuestas

1. **Respuesta correcta**: El workspace personal solo lo puedes ver tú, el de equipo se comparte con otros miembros

2. **Respuesta correcta**: Local > Environment > Collection > Global

3. **Respuesta correcta**: Es el valor que se comparte al exportar el environment

4. **Respuesta correcta**: Facilita cambiar entre diferentes entornos sin modificar los requests

5. **Respuesta**: Se usará el valor del environment (tiene mayor prioridad que las variables globales)

6. **Respuesta**: `{{api_token}}`

7. **Respuesta**: JSON (formato Collection v2.1)

---

## Resumen del Laboratorio

En este laboratorio has aprendido a:

✅ Crear y configurar un workspace personal  
✅ Organizar colecciones con carpetas lógicas  
✅ Configurar múltiples environments (Development, Staging, Production)  
✅ Definir variables de environment y de colección  
✅ Utilizar variables en requests (URL, headers, body)  
✅ Exportar e importar colecciones y environments  
✅ Aplicar buenas prácticas de organización  

**Habilidades clave adquiridas:**
- Gestión de workspaces y colecciones
- Configuración de environments
- Uso efectivo de variables
- Portabilidad de configuraciones

---

## Próximos Pasos

Una vez completado este laboratorio, estarás preparado para:

- **Laboratorio 2**: "Testing con Postman - Probando"
  - Aprender los métodos HTTP (GET, POST, PUT, DELETE)
  - Configurar autenticación
  - Trabajar con diferentes tipos de body
  - Implementar un CRUD completo

---

## Recursos Adicionales

- **Documentación de Postman sobre Variables**: https://learning.postman.com/docs/sending-requests/variables/
- **Documentación sobre Environments**: https://learning.postman.com/docs/sending-requests/managing-environments/
- **JSONPlaceholder Guide**: https://jsonplaceholder.typicode.com/guide/
- **Postman Community**: https://community.postman.com/

---

**¡Felicitaciones por completar el laboratorio!**

*Laboratorio generado para el curso de API Testing - Postman y SoapUI*
