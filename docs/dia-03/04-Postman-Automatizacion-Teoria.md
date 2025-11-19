# Automatización de Pruebas en Postman

## Objetivos de Aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Crear y gestionar colecciones de pruebas automatizadas en Postman
- Implementar flujos de trabajo automatizados utilizando variables y scripts
- Ejecutar baterías de pruebas mediante Collection Runner
- Integrar pruebas de API en pipelines de CI/CD utilizando Newman
- Generar reportes automáticos de ejecución de pruebas

## Duración Estimada

3 horas (teoría y práctica combinadas)

## Contenidos

### 1. Introducción a la Automatización en Postman

La automatización de pruebas permite ejecutar conjuntos de peticiones de forma secuencial, validar respuestas de manera programática y repetir pruebas sin intervención manual. Esto resulta esencial para:

- **Regresión**: Verificar que cambios en la API no rompen funcionalidades existentes
- **Integración continua**: Ejecutar pruebas automáticamente en cada despliegue
- **Eficiencia**: Reducir tiempo de testing manual repetitivo
- **Consistencia**: Garantizar que las pruebas se ejecutan de la misma manera cada vez

**Ejemplo sencillo**: Imagina que tienes una API de gestión de usuarios. En lugar de probar manualmente crear un usuario, verificar que existe, actualizarlo y borrarlo cada vez que hay un cambio, puedes automatizar estas 4 peticiones para que se ejecuten en secuencia y validen automáticamente cada paso.

### 2. Colecciones de Postman

Una colección es un grupo organizado de peticiones HTTP que pueden ejecutarse juntas. Las colecciones son la base de la automatización en Postman.

**Estructura de una colección**:
- **Carpetas**: Organizan peticiones por funcionalidad (ej: "Autenticación", "Usuarios", "Productos")
- **Peticiones**: Las llamadas HTTP individuales
- **Tests**: Scripts de validación asociados a cada petición
- **Variables**: Datos reutilizables dentro de la colección

**Ejemplo sencillo**: Una colección de "Gestión de Productos" podría contener:
```
📁 Gestión de Productos
  📁 Autenticación
    - POST Login
  📁 Productos
    - GET Listar todos los productos
    - POST Crear producto
    - GET Obtener producto por ID
    - PUT Actualizar producto
    - DELETE Eliminar producto
```

**Creación de una colección**:
1. Clic en "New" > "Collection"
2. Nombrar la colección descriptivamente
3. Añadir descripción y documentación
4. Agregar peticiones arrastrándolas o creándolas dentro

### 3. Variables en Automatización

Las variables permiten reutilizar valores y crear flujos dinámicos. Existen varios niveles de alcance:

**Tipos de variables por alcance**:
- **Global**: Disponibles en todo Postman
- **Collection**: Solo en la colección actual
- **Environment**: Específicas del entorno (desarrollo, pruebas, producción)
- **Data**: Desde archivos CSV/JSON para data-driven testing
- **Local**: Solo durante la ejecución actual

**Ejemplo sencillo**: Si necesitas el mismo token de autenticación en 10 peticiones diferentes:

```javascript
// En lugar de copiar el token en cada petición:
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

// Usas una variable:
Authorization: Bearer {{authToken}}
```

**Definir variables en scripts**:
```javascript
// En el test de la petición de login:
pm.collectionVariables.set("authToken", pm.response.json().token);
pm.collectionVariables.set("userId", pm.response.json().userId);
```

**Usar variables en peticiones**:
```
GET https://api.example.com/users/{{userId}}
Authorization: Bearer {{authToken}}
```

### 4. Flujos de Trabajo Automatizados

Los flujos automáticos encadenan peticiones donde el resultado de una se usa como entrada de la siguiente.

**Patrón común**: Autenticación → Operación → Verificación

**Ejemplo sencillo de flujo**:

1. **Petición 1 - Login**: Obtener token de autenticación
```javascript
// Test
pm.test("Login exitoso", function() {
    pm.response.to.have.status(200);
    const jsonData = pm.response.json();
    pm.collectionVariables.set("authToken", jsonData.token);
});
```

2. **Petición 2 - Crear Recurso**: Crear un producto usando el token
```javascript
// Pre-request Script
pm.request.headers.add({
    key: "Authorization",
    value: "Bearer " + pm.collectionVariables.get("authToken")
});

// Test
pm.test("Producto creado", function() {
    pm.response.to.have.status(201);
    const producto = pm.response.json();
    pm.collectionVariables.set("productoId", producto.id);
});
```

3. **Petición 3 - Verificar Recurso**: Comprobar que el producto existe
```javascript
// URL: GET /products/{{productoId}}
pm.test("Producto existe", function() {
    pm.response.to.have.status(200);
    pm.expect(pm.response.json().id).to.eql(pm.collectionVariables.get("productoId"));
});
```

### 5. Collection Runner

El Collection Runner ejecuta todas las peticiones de una colección en secuencia de forma automatizada.

**Características principales**:
- Ejecuta peticiones en orden o con iteraciones múltiples
- Permite configurar delays entre peticiones
- Soporta archivos de datos externos (CSV, JSON)
- Genera reportes de ejecución
- Puede guardar respuestas para análisis

**Ejemplo sencillo de uso**:

1. Seleccionar la colección en el menú lateral
2. Clic en "Run" (botón de play)
3. Configurar opciones:
   - Número de iteraciones: 1
   - Delay entre peticiones: 0ms
   - Seleccionar peticiones a ejecutar
4. Clic en "Run [Nombre Colección]"

**Interpretación de resultados**:
- **Verde**: Test pasó correctamente
- **Rojo**: Test falló
- **Gris**: Petición sin tests

**Data-driven testing**: Ejecutar la misma colección con diferentes conjuntos de datos.

Archivo `usuarios.csv`:
```csv
nombre,email,edad
Juan,juan@example.com,25
María,maria@example.com,30
Pedro,pedro@example.com,28
```

En Collection Runner:
- Seleccionar archivo de datos
- Las variables `{{nombre}}`, `{{email}}`, `{{edad}}` se reemplazan en cada iteración

### 6. Newman - CLI de Postman

Newman es la herramienta de línea de comandos que ejecuta colecciones de Postman fuera de la interfaz gráfica. Es fundamental para integración con CI/CD.

**Instalación**:
```bash
npm install -g newman
```

**Uso básico**:
```bash
newman run mi-coleccion.json
```

**Ejemplo sencillo con opciones**:
```bash
newman run api-tests.json \
  --environment produccion.json \
  --reporters cli,html \
  --reporter-html-export reporte.html \
  --timeout-request 10000
```

**Opciones importantes**:
- `--environment`: Archivo de entorno
- `--globals`: Variables globales
- `--reporters`: Formato de salida (cli, html, json, junit)
- `--bail`: Detener ejecución al primer error
- `--timeout-request`: Timeout en milisegundos
- `--iteration-count`: Número de iteraciones

**Exportar colección para Newman**:
1. En Postman, clic derecho en la colección
2. "Export"
3. Seleccionar formato "Collection v2.1"
4. Guardar archivo JSON

### 7. Integración con CI/CD

La automatización completa incluye ejecutar pruebas en pipelines de integración continua.

**Ejemplo sencillo con GitHub Actions**:

Archivo `.github/workflows/api-tests.yml`:
```yaml
name: API Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout código
        uses: actions/checkout@v2
      
      - name: Instalar Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '16'
      
      - name: Instalar Newman
        run: npm install -g newman
      
      - name: Ejecutar pruebas
        run: newman run coleccion.json --environment env.json
```

**Flujo típico CI/CD**:
1. Desarrollador hace push de código
2. Pipeline se activa automáticamente
3. Newman ejecuta colección de pruebas
4. Si alguna prueba falla, el pipeline falla
5. Se notifica al equipo del resultado

### 8. Reportes y Monitorización

**Reportes HTML con Newman**:
```bash
newman run coleccion.json --reporters html --reporter-html-export reporte.html
```

El reporte incluye:
- Resumen de ejecución (total, exitosas, fallidas)
- Detalle de cada petición
- Tiempos de respuesta
- Tests ejecutados y resultados
- Gráficos de rendimiento

**Postman Monitors** (requiere cuenta Postman):
- Ejecutan colecciones automáticamente en intervalos programados
- Notifican por email si hay fallos
- Mantienen historial de ejecuciones
- Útil para monitorización continua de APIs en producción

**Ejemplo sencillo**: Monitorizar cada hora que el endpoint de salud responde correctamente:
```
GET /health
pm.test("API disponible", function() {
    pm.response.to.have.status(200);
});
```

### 9. Buenas Prácticas en Automatización

**Organización**:
- Agrupar peticiones lógicamente en carpetas
- Nombrar peticiones y tests descriptivamente
- Documentar el propósito de cada colección

**Variables**:
- Usar variables de colección para datos específicos del test
- Usar variables de entorno para configuración por ambiente
- Limpiar variables temporales al finalizar

**Tests**:
- Validar códigos de estado HTTP
- Verificar estructura de respuestas
- Comprobar valores críticos de negocio
- Mantener tests simples y enfocados

**Mantenimiento**:
- Revisar y actualizar colecciones regularmente
- Eliminar peticiones obsoletas
- Mantener sincronizados entornos con la realidad

**Ejemplo de test completo y bien estructurado**:
```javascript
pm.test("Status code es 200", function() {
    pm.response.to.have.status(200);
});

pm.test("Response es JSON válido", function() {
    pm.response.to.be.json;
});

pm.test("Contiene campos requeridos", function() {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
    pm.expect(jsonData).to.have.property('nombre');
    pm.expect(jsonData).to.have.property('email');
});

pm.test("Email tiene formato válido", function() {
    const email = pm.response.json().email;
    pm.expect(email).to.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/);
});
```

## Referencias Externas

### Documentación Oficial
- **Postman Learning Center - Running Collections**: [https://learning.postman.com/docs/running-collections/intro-to-collection-runs/](https://learning.postman.com/docs/running-collections/intro-to-collection-runs/)
- **Postman - Variables**: [https://learning.postman.com/docs/sending-requests/variables/](https://learning.postman.com/docs/sending-requests/variables/)
- **Newman Documentation**: [https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/)
- **Postman Scripts Reference**: [https://learning.postman.com/docs/writing-scripts/script-references/postman-sandbox-api-reference/](https://learning.postman.com/docs/writing-scripts/script-references/postman-sandbox-api-reference/)

### Herramientas y Recursos
- **Newman en npm**: [https://www.npmjs.com/package/newman](https://www.npmjs.com/package/newman)
- **Newman Reporters**: [https://www.npmjs.com/search?q=newman-reporter](https://www.npmjs.com/search?q=newman-reporter)
- **Postman Examples**: [https://www.postman.com/postman/workspace/postman-team-collections/](https://www.postman.com/postman/workspace/postman-team-collections/)

### Integración CI/CD
- **GitHub Actions con Newman**: [https://blog.postman.com/using-github-actions-to-run-postman-collections/](https://blog.postman.com/using-github-actions-to-run-postman-collections/)
- **Jenkins Integration**: [https://learning.postman.com/docs/running-collections/using-newman-cli/integration-with-jenkins/](https://learning.postman.com/docs/running-collections/using-newman-cli/integration-with-jenkins/)
- **GitLab CI with Newman**: [https://docs.gitlab.com/ee/ci/examples/test_api.html](https://docs.gitlab.com/ee/ci/examples/test_api.html)

### Testing y Mejores Prácticas
- **API Testing Best Practices**: [https://www.postman.com/api-platform/api-testing-best-practices/](https://www.postman.com/api-platform/api-testing-best-practices/)
- **Data-driven Testing**: [https://learning.postman.com/docs/running-collections/working-with-data-files/](https://learning.postman.com/docs/running-collections/working-with-data-files/)
- **Test Script Examples**: [https://learning.postman.com/docs/writing-scripts/test-examples/](https://learning.postman.com/docs/writing-scripts/test-examples/)

## Resumen

La automatización de pruebas en Postman permite ejecutar conjuntos de peticiones de forma secuencial, validar respuestas programáticamente y integrar testing en pipelines de CI/CD. Los elementos clave son las colecciones (agrupaciones de peticiones), las variables (para datos reutilizables), el Collection Runner (ejecución visual), y Newman (ejecución por línea de comandos). La automatización efectiva requiere organización clara, uso apropiado de variables, tests bien estructurados y mantenimiento regular de las colecciones.
