# Empezando a trabajar con SoapUI - Laboratorio

## Objetivos

- Instalar y configurar SoapUI
- Crear un proyecto REST desde una especificación OpenAPI
- Realizar la primera petición de prueba
- Familiarizarse con la interfaz de SoapUI

## Duración estimada

25 minutos

## Prerrequisitos

- Tener permisos de instalación en el equipo
- Conexión a Internet

## Ejercicio práctico: Primer proyecto con JSONPlaceholder API

### Paso 1: Instalación de SoapUI (10 minutos)

1. Accede a https://www.soapui.org/downloads/soapui/
2. Descarga **SoapUI Open Source** para tu sistema operativo
3. Ejecuta el instalador:
   - Windows: `SoapUI-x64-5.7.0.exe`
   - Linux: `SoapUI-x64-5.7.0.sh`
   - Mac: `SoapUI-5.7.0.dmg`
4. Acepta los términos de licencia
5. Selecciona instalación completa (incluye tutoriales)
6. Completa la instalación
7. Inicia SoapUI desde el acceso directo creado

### Paso 2: Crear un proyecto REST (5 minutos)

1. En SoapUI, ve a **File → New REST Project**
2. Configura el proyecto:
   - **Project Name**: `JSONPlaceholder API`
   - **URI**: `https://jsonplaceholder.typicode.com`
3. Haz clic en **OK**

### Paso 3: Crear tu primer request (5 minutos)

1. Haz clic derecho sobre el proyecto `JSONPlaceholder API`
2. Selecciona **New REST Service from URI**
3. Introduce la URI completa: `https://jsonplaceholder.typicode.com/users`
4. Haz clic en **OK**
5. SoapUI creará automáticamente:
   - Un servicio REST
   - Un recurso `/users`
   - Un método GET

### Paso 4: Ejecutar la petición (3 minutos)

1. En el navegador de proyectos (panel izquierdo), expande:
   ```
   JSONPlaceholder API
   └── https://jsonplaceholder.typicode.com
       └── /users
           └── Method 1 [GET]
               └── Request 1
   ```
2. Haz doble clic en **Request 1**
3. Se abrirá el editor en el panel central
4. Haz clic en el botón **▶ (Play/Submit Request)** en la barra superior
5. Observa la respuesta en el panel derecho

### Paso 5: Explorar la respuesta (2 minutos)

En el panel de respuesta verás:

- **Status**: `HTTP/1.1 200 OK`
- **Time**: Tiempo de respuesta en ms
- **Size**: Tamaño de la respuesta

**Ejemplo de respuesta JSON:**
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    ...
  },
  ...
]
```

### Resultados esperados

Al finalizar este ejercicio deberías:

✓ Tener SoapUI instalado y funcionando
✓ Un proyecto REST creado llamado "JSONPlaceholder API"
✓ Una petición GET exitosa a `/users`
✓ Respuesta HTTP 200 con un array JSON de 10 usuarios
✓ Tiempo de respuesta menor a 1000ms

## Autoevaluación

**Pregunta 1:** ¿Qué panel de SoapUI muestra la estructura jerárquica de proyectos?
<details>
<summary>Respuesta</summary>
El Navigator (panel izquierdo) muestra la estructura de proyectos, test suites, test cases y test steps en forma de árbol jerárquico.
</details>

**Pregunta 2:** ¿Qué información básica se necesita para crear un proyecto REST en SoapUI?
<details>
<summary>Respuesta</summary>
Se necesita el nombre del proyecto y la URI base del servicio REST. Opcionalmente se puede importar una especificación OpenAPI/Swagger.
</details>

**Pregunta 3:** ¿Qué código de estado HTTP indica una petición exitosa?
<details>
<summary>Respuesta</summary>
El código 200 OK indica que la petición fue exitosa y el servidor devolvió la respuesta esperada.
</details>

**Pregunta 4:** ¿En qué formato devuelve los datos el endpoint `/users` de JSONPlaceholder?
<details>
<summary>Respuesta</summary>
Devuelve los datos en formato JSON (JavaScript Object Notation), específicamente un array de objetos usuario.
</details>

## Ejercicio adicional (opcional)

Crea una segunda petición para obtener un usuario específico:

1. Crea un nuevo recurso: `/users/{id}`
2. Crea un método GET
3. Sustituye `{id}` por `1`
4. Ejecuta la petición
5. Verifica que obtienes un único usuario en lugar de un array

**URI esperada**: `https://jsonplaceholder.typicode.com/users/1`

## Notas importantes

- Guarda tu proyecto regularmente con **File → Save Project**
- Los proyectos se guardan en formato XML
- Puedes exportar el proyecto para compartirlo con tu equipo
- SoapUI mantiene un historial de requests ejecutados
