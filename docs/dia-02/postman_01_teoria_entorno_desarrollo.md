# Testing con Postman - Preparando el Entorno de Desarrollo

## Objetivos de Aprendizaje

Al finalizar este tema, el estudiante será capaz de:

1. Comprender qué es Postman y sus principales casos de uso en el testing de APIs
2. Instalar y configurar Postman correctamente en su entorno de trabajo
3. Navegar eficientemente por la interfaz de usuario de Postman
4. Crear y organizar workspaces y colecciones de forma efectiva
5. Configurar y gestionar environments para diferentes entornos (desarrollo, staging, producción)
6. Utilizar variables de entorno, globales y de colección apropiadamente
7. Importar, exportar y compartir colecciones con el equipo

---

## 1. Introducción a Postman

### 1.1 ¿Qué es Postman?

Postman es una plataforma de colaboración para el desarrollo de APIs que simplifica cada paso del ciclo de vida de una API. Es la herramienta más utilizada en la industria para el testing de APIs REST.

**Funcionalidades principales:**
- Crear y ejecutar peticiones HTTP
- Automatizar pruebas de APIs
- Documentar APIs
- Simular APIs con Mock Servers
- Monitorear APIs en producción
- Colaborar en equipos

### 1.2 Casos de uso principales

**Desarrollo:**
- Probar endpoints durante el desarrollo
- Validar respuestas de la API
- Depurar problemas de integración

**Testing:**
- Crear suites de pruebas automatizadas
- Validar contratos de API
- Realizar pruebas de regresión

**Documentación:**
- Generar documentación automática
- Compartir ejemplos de uso
- Mantener especificaciones actualizadas

### 1.3 Instalación

Postman está disponible en múltiples plataformas:

1. **Aplicación de escritorio** (recomendado)
   - Windows, macOS, Linux
   - Descarga desde: https://www.postman.com/downloads/

2. **Versión web**
   - Acceso desde navegador
   - URL: https://web.postman.co/

**Ejemplo de instalación en Windows:**
1. Descargar el instalador desde la web oficial
2. Ejecutar el archivo .exe descargado
3. Seguir el asistente de instalación
4. Crear cuenta o iniciar sesión

---

## 2. Interfaz de Usuario de Postman

### 2.1 Componentes principales

La interfaz de Postman se divide en varias áreas clave:

**Barra lateral izquierda:**
- **Collections**: Organización de requests agrupados
- **Environments**: Conjuntos de variables
- **History**: Historial de requests ejecutados

**Área central:**
- **Request builder**: Constructor de peticiones HTTP
- **Response viewer**: Visualizador de respuestas
- **Tabs**: Múltiples requests abiertos simultáneamente

**Panel inferior:**
- **Console**: Logs y debugging
- **Test Results**: Resultados de pruebas

### 2.2 Conceptos fundamentales

**Request (Petición):**
Una solicitud HTTP individual que incluye método, URL, headers, body y configuraciones de prueba.

**Collection (Colección):**
Agrupación lógica de requests relacionados. Permite organizar y ejecutar múltiples peticiones como una suite.

**Environment (Entorno):**
Conjunto de variables clave-valor que permiten trabajar con diferentes configuraciones (desarrollo, staging, producción).

**Workspace (Espacio de trabajo):**
Contenedor que agrupa colecciones, environments, mocks y monitors. Facilita la colaboración en equipo.

**Ejemplo conceptual:**
```
Workspace: "Proyecto E-commerce"
├── Collection: "API de Usuarios"
│   ├── Request: GET Listar usuarios
│   ├── Request: POST Crear usuario
│   └── Request: DELETE Eliminar usuario
├── Collection: "API de Productos"
│   ├── Request: GET Listar productos
│   └── Request: POST Crear producto
└── Environments:
    ├── Development
    ├── Staging
    └── Production
```

---

## 3. Workspaces y Colecciones

### 3.1 Workspaces

Los workspaces permiten organizar el trabajo por proyectos o equipos.

**Tipos de workspaces:**

1. **Personal**: Solo visible para ti
2. **Team**: Compartido con miembros del equipo
3. **Public**: Visible para toda la comunidad

**Buenas prácticas:**
- Un workspace por proyecto o producto
- Nombres descriptivos y claros
- Documentación del propósito del workspace

**Ejemplo de creación:**
1. Click en "Workspaces" en la barra superior
2. Seleccionar "Create Workspace"
3. Ingresar nombre: "API Banking Project"
4. Seleccionar tipo: "Team"
5. Agregar descripción y miembros del equipo

### 3.2 Colecciones

Las colecciones son el núcleo organizativo de Postman.

**Estructura de una colección:**
```
Collection
├── Folder (carpeta)
│   ├── Request 1
│   └── Request 2
├── Request 3
└── Documentation
```

**Ejemplo práctico: Colección de API de Usuarios**
```
Collection: "User Management API"
├── Folder: "Authentication"
│   ├── POST Login
│   └── POST Logout
├── Folder: "CRUD Operations"
│   ├── GET List all users
│   ├── GET Get user by ID
│   ├── POST Create user
│   ├── PUT Update user
│   └── DELETE Delete user
└── Folder: "User Validation"
    └── GET Validate email
```

**Características de las colecciones:**
- Authorization compartida entre todos los requests
- Pre-request scripts a nivel de colección
- Tests comunes para todos los requests
- Variables específicas de la colección

---

## 4. Environments y Variables

### 4.1 ¿Qué son los Environments?

Los environments permiten trabajar con diferentes configuraciones sin modificar los requests.

**Ejemplo de escenario:**
Tienes una API que funciona en tres entornos:
- Desarrollo: `http://localhost:3000`
- Staging: `https://staging.api.example.com`
- Producción: `https://api.example.com`

En lugar de cambiar manualmente la URL en cada request, defines una variable `{{base_url}}` y cambias de environment.

### 4.2 Tipos de variables

Postman maneja variables con diferentes alcances (scopes):

**1. Variables globales:**
- Disponibles en todos los workspaces y colecciones
- Uso: Configuraciones que raramente cambian

**2. Variables de entorno:**
- Específicas de un environment
- Uso: URLs, API keys, configuraciones por entorno

**3. Variables de colección:**
- Disponibles solo dentro de una colección
- Uso: Constantes específicas del proyecto

**4. Variables locales:**
- Temporales, solo durante la ejecución
- Uso: Valores calculados en scripts

**Jerarquía de prioridad (mayor a menor):**
```
Local > Environment > Collection > Global
```

### 4.3 Creación de Environments

**Pasos para crear un environment:**

1. Click en el icono de engranaje (⚙️) en la esquina superior derecha
2. Seleccionar "Environments"
3. Click en "+" para crear nuevo environment
4. Nombrar el environment: "Development"
5. Agregar variables

**Ejemplo de Environment: Development**

| Variable | Initial Value | Current Value |
|----------|--------------|---------------|
| base_url | http://localhost:3000 | http://localhost:3000 |
| api_key | dev_key_12345 | dev_key_12345 |
| timeout | 5000 | 5000 |
| user_email | test@example.com | test@example.com |

**Ejemplo de Environment: Production**

| Variable | Initial Value | Current Value |
|----------|--------------|---------------|
| base_url | https://api.example.com | https://api.example.com |
| api_key | prod_key_67890 | prod_key_67890 |
| timeout | 30000 | 30000 |
| user_email | admin@example.com | admin@example.com |

### 4.4 Uso de variables en requests

**Sintaxis:**
```
{{variable_name}}
```

**Ejemplo en URL:**
```
GET {{base_url}}/api/v1/users
```

**Ejemplo en Headers:**
```
Authorization: Bearer {{auth_token}}
X-API-Key: {{api_key}}
```

**Ejemplo en Body:**
```json
{
  "email": "{{user_email}}",
  "timeout": {{timeout}}
}
```

### 4.5 Variables Initial Value vs Current Value

**Initial Value:**
- Se comparte cuando exportas el environment
- Visible para todo el equipo
- **NO incluir datos sensibles** (passwords, tokens reales)

**Current Value:**
- Solo en tu instancia local
- No se comparte al exportar
- Usar para datos sensibles

**Ejemplo:**
```
Variable: database_password
Initial Value: <password>
Current Value: MyS3cr3tP@ssw0rd!
```

---

## 5. Importar, Exportar y Compartir

### 5.1 Exportar colecciones

**Formato de exportación:**
- JSON (Collection v2.1 - recomendado)
- JSON (Collection v2.0 - compatibilidad)

**Pasos:**
1. Click derecho en la colección
2. Seleccionar "Export"
3. Elegir versión: Collection v2.1
4. Guardar archivo JSON

**Ejemplo de uso:**
- Backup de colecciones
- Compartir con equipo
- Versionado en Git

### 5.2 Importar colecciones

**Métodos de importación:**

1. **Desde archivo:**
   - Click en "Import" (botón superior izquierdo)
   - Seleccionar archivo .json
   - Confirmar importación

2. **Desde URL:**
   - Pegar URL del archivo JSON
   - Útil para colecciones públicas

3. **Desde texto:**
   - Pegar contenido JSON directamente

**Ejemplo práctico:**
```bash
# URL de colección pública de JSONPlaceholder
https://www.postman.com/postman/workspace/published-postman-templates/collection/631643-f695cab7-6878-eb55-7943-ad88e1ccfd65
```

### 5.3 Compartir con el equipo

**Métodos de colaboración:**

**1. Team Workspace:**
- Automáticamente sincronizado
- Cambios en tiempo real
- Control de permisos (Editor, Viewer)

**2. Generar enlace público:**
- Click derecho en colección
- "Share collection"
- Generar link público o privado

**3. Publicar en Postman:**
- Crear documentación pública
- Disponible para la comunidad

### 5.4 Sincronización

Postman sincroniza automáticamente:
- Colecciones
- Environments
- Globals
- Historia de requests

**Requisitos:**
- Cuenta de Postman activa
- Conexión a internet
- Workspace tipo Team o Personal con sync habilitado

---

## 6. Organización y Buenas Prácticas

### 6.1 Estructura recomendada

**Por funcionalidad:**
```
Workspace: "E-commerce Platform"
├── Collection: "Authentication API"
├── Collection: "User Management"
├── Collection: "Product Catalog"
├── Collection: "Shopping Cart"
└── Collection: "Payment Processing"
```

**Por flujo de usuario:**
```
Collection: "User Journey - Purchase Flow"
├── Folder: "1. Registration"
├── Folder: "2. Browse Products"
├── Folder: "3. Add to Cart"
├── Folder: "4. Checkout"
└── Folder: "5. Payment"
```

### 6.2 Nomenclatura

**Colecciones:**
- Usar nombres descriptivos
- Incluir versión si aplica
- Ejemplo: "User API v2.0"

**Requests:**
- Incluir método HTTP
- Descripción clara
- Ejemplo: "GET List active users"

**Variables:**
- snake_case o camelCase consistente
- Prefijos por tipo si es necesario
- Ejemplos: `base_url`, `authToken`, `max_retries`

### 6.3 Documentación

**Elementos a documentar:**

**En colecciones:**
- Propósito general
- Autenticación requerida
- Prerrequisitos
- Ejemplos de uso

**En requests:**
- Descripción de la operación
- Parámetros requeridos y opcionales
- Ejemplos de respuestas exitosas y errores
- Códigos de estado esperados

**Ejemplo de documentación de request:**
```markdown
# Get User by ID

Recupera la información detallada de un usuario específico.

## Endpoint
GET /api/v1/users/:id

## Parámetros
- `id` (path parameter): ID único del usuario

## Headers
- `Authorization`: Bearer token requerido

## Respuestas
- 200: Usuario encontrado
- 404: Usuario no existe
- 401: No autenticado

## Ejemplo de respuesta exitosa
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

## 7. Referencias y Recursos

### 7.1 Documentación oficial
- **Postman Learning Center**: https://learning.postman.com/
- **Documentación de API**: https://www.postman.com/postman/workspace/postman-public-workspace/documentation/12959542-c8142d51-e97c-46b6-bd77-52bb66712c9a
- **Postman Blog**: https://blog.postman.com/

### 7.2 Recursos adicionales
- **Postman Community**: https://community.postman.com/
- **YouTube - Postman**: https://www.youtube.com/c/Postman
- **Postman Galaxy** (conferencia anual): https://www.postman.com/postman-galaxy/

### 7.3 APIs para practicar
- **JSONPlaceholder**: https://jsonplaceholder.typicode.com/ (sin autenticación)
- **ReqRes**: https://reqres.in/ (simulación CRUD)
- **Postman Echo**: https://postman-echo.com/ (testing de requests)

---

## 8. Preguntas de Autoevaluación

1. ¿Cuál es la diferencia entre una colección y un workspace en Postman?
2. ¿Qué tipo de variable usarías para almacenar una URL que cambia entre desarrollo y producción?
3. ¿En qué se diferencian "Initial Value" y "Current Value" en las variables de entorno?
4. ¿Cuál es la jerarquía de prioridad entre los diferentes tipos de variables?
5. ¿Por qué es importante exportar colecciones regularmente?
6. ¿Qué método de compartir colecciones es más apropiado para un equipo de desarrollo?
7. ¿Cómo organizarías una colección para una API REST con operaciones CRUD en múltiples recursos?

---

## Resumen

En este tema hemos aprendido:

- **Postman** es la herramienta líder para el testing de APIs REST
- La **interfaz** se organiza en barra lateral, área central y paneles inferiores
- Los **workspaces** agrupan colecciones y environments por proyecto o equipo
- Las **colecciones** organizan requests relacionados en una estructura jerárquica
- Los **environments** permiten trabajar con diferentes configuraciones (dev, staging, prod)
- Las **variables** tienen diferentes alcances: globales, de entorno, de colección y locales
- La **colaboración** se facilita mediante team workspaces, exportación e importación
- Las **buenas prácticas** incluyen nomenclatura clara, estructura lógica y documentación completa

---

