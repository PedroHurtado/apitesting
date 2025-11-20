# Tipos de Pasos de Pruebas (Test Steps) - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Identificar todos los tipos de Test Steps disponibles en SoapUI
- Seleccionar el tipo de Test Step apropiado según el escenario
- Implementar flujos complejos con Test Steps especializados
- Combinar diferentes tipos de Test Steps efectivamente
- Comprender casos de uso avanzados

## Duración estimada

20 minutos

## Prerrequisitos

- Comprensión básica de Test Cases
- Familiaridad con REST y SOAP
- Conocimientos de Groovy (básico)

## Contenido

### Clasificación de Test Steps

SoapUI ofrece múltiples tipos de Test Steps organizados en categorías:

```
Test Steps
├── Request Steps (Peticiones)
│   ├── REST Request
│   ├── SOAP Request
│   └── HTTP Request
├── Logic Steps (Lógica)
│   ├── Groovy Script
│   ├── Conditional Goto
│   ├── Delay
│   └── Run TestCase
├── Data Steps (Datos)
│   ├── Property Transfer
│   ├── DataSource
│   └── DataSink
└── Utility Steps (Utilidades)
    ├── Manual TestStep
    └── JDBC Request
```

### Request Steps

#### 1. REST Request

El tipo más utilizado para APIs REST modernas.

**Características:**
- Soporta todos los métodos HTTP (GET, POST, PUT, DELETE, PATCH)
- Manejo de parámetros (path, query, header, body)
- Formatos: JSON, XML, form-data, etc.
- Configuración de autenticación

**Ejemplo de configuración:**
```
Method: POST
Endpoint: /api/users
Headers:
  Content-Type: application/json
  Authorization: Bearer ${#TestCase#token}
Body:
{
  "name": "Juan Pérez",
  "email": "juan@ejemplo.com"
}
```

**Casos de uso:**
- Testing de APIs RESTful
- Integración con servicios modernos
- Microservicios

#### 2. SOAP Request

Para servicios web basados en SOAP/WSDL.

**Características:**
- Genera automáticamente XML desde WSDL
- Manejo de namespaces
- Soporte para WS-Security
- Validación de esquemas

**Ejemplo conceptual:**
```xml
<soap:Envelope>
  <soap:Body>
    <GetUser>
      <userId>123</userId>
    </GetUser>
  </soap:Body>
</soap:Envelope>
```

**Casos de uso:**
- Servicios empresariales legacy
- Integración con sistemas SAP, Oracle
- Web Services tradicionales

#### 3. HTTP Request

Request HTTP genérico sin estructura REST/SOAP predefinida.

**Características:**
- Control total sobre la petición
- Útil para endpoints no estándar
- Configuración manual completa

**Casos de uso:**
- Endpoints que no siguen REST estricto
- Servicios propietarios
- Testing de bajo nivel

### Logic Steps

#### 4. Groovy Script

Permite ejecutar código Groovy personalizado.

**Capacidades:**
- Lógica condicional compleja
- Manipulación de datos
- Cálculos matemáticos
- Acceso a bases de datos
- Llamadas a APIs externas
- Generación de datos aleatorios

**Ejemplo de manipulación de datos:**
```groovy
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

// Leer respuesta anterior
def response = context.expand('${PreviousStep#Response}')
def json = new JsonSlurper().parseText(response)

// Transformar datos
def usuarios = json.collect { user ->
    [
        id: user.id,
        fullName: "${user.name} (${user.username})",
        contact: user.email
    ]
}

// Guardar resultado procesado
def processed = JsonOutput.toJson(usuarios)
testRunner.testCase.setPropertyValue("processedData", processed)
log.info("Procesados ${usuarios.size()} usuarios")
```

**Casos de uso:**
- Validaciones complejas que no se pueden hacer con aserciones
- Preparación de datos de prueba
- Integración con sistemas externos
- Lógica de negocio personalizada

#### 5. Conditional Goto

Implementa lógica condicional en el flujo de test steps.

**Sintaxis:**
```
Target Step: [nombre del step destino]
Condition: ${#TestCase#property} == "value"
```

**Ejemplo de flujo condicional:**
```
Test Case: Login y acceso
├── Step 1: POST /login
├── Step 2: Groovy Script (verificar token)
├── Step 3: Conditional Goto
│   └── If ${#TestCase#tokenValid} == "true" → Goto "Acceder a perfil"
│   └── Else: continuar
├── Step 4: Log error
└── Step 5: Acceder a perfil [Target]
```

**Casos de uso:**
- Flujos con múltiples caminos
- Skip de steps según condiciones
- Implementar loops básicos
- Manejo de errores

#### 6. Delay

Pausa la ejecución durante un tiempo específico.

**Configuración:**
```
Delay: 2000 ms (2 segundos)
```

**Casos de uso:**
- Esperar procesamiento asíncrono
- Rate limiting entre requests
- Simular comportamiento de usuario real
- Dar tiempo a sistemas externos

**Ejemplo de uso:**
```
Test Case: Crear y verificar recurso
├── Step 1: POST /orders (crear pedido)
├── Step 2: Delay 3000ms (esperar procesamiento)
└── Step 3: GET /orders/{id} (verificar estado)
```

#### 7. Run TestCase

Ejecuta otro Test Case como sub-rutina.

**Configuración:**
```
Target TestCase: TC-Autenticacion
Copy HTTP Session: true
Copy Properties: false
```

**Casos de uso:**
- Reutilización de flujos comunes
- Modularización de tests
- Setup/teardown compartido
- Tests compuestos

**Ejemplo de reutilización:**
```
Test Case: Crear Orden con Auth
├── Step 1: Run TestCase [TC-Login] (obtener token)
├── Step 2: POST /orders (usar token de TC-Login)
└── Step 3: Validar respuesta
```

### Data Steps

#### 8. Property Transfer

Extrae datos de una respuesta y los almacena en propiedades.

**Configuración:**
```
Source: Response del step "Login"
Property Path (JSONPath): $.token
Target: TestCase Property "authToken"
```

**Ejemplo completo:**
```
Source Step: POST /login
Response: {"token": "abc123", "userId": 42, "expires": "2025-12-31"}

Transfer 1:
  Path: $.token → Property: authToken
Transfer 2:
  Path: $.userId → Property: currentUserId
Transfer 3:
  Path: $.expires → Property: tokenExpiry
```

**Casos de uso:**
- Extraer IDs generados
- Capturar tokens de autenticación
- Pasar datos entre steps
- Data-driven testing

#### 9. DataSource

Lee datos de fuentes externas para data-driven testing.

**Fuentes soportadas:**
- Archivos Excel (.xls, .xlsx)
- CSV files
- XML files
- Bases de datos (JDBC)
- Grids (datos inline)

**Ejemplo conceptual de CSV:**
```csv
username,password,expectedResult
admin,pass123,success
user1,wrongpass,error
guest,guest,success
```

**Casos de uso:**
- Ejecutar mismo test con múltiples datasets
- Testing parametrizado
- Pruebas con datos realistas
- Validación masiva

#### 10. DataSink

Guarda datos de las pruebas en archivos externos.

**Destinos soportados:**
- Archivos Excel
- CSV
- XML

**Ejemplo de uso:**
```
Test Loop:
  Para cada usuario en DataSource:
    ├── Ejecutar prueba
    ├── Guardar resultado en DataSink
    └── {username, status, responseTime}
```

**Casos de uso:**
- Exportar resultados para análisis
- Generar datos para reportes
- Auditoría de pruebas

### Utility Steps

#### 11. JDBC Request

Ejecuta consultas SQL contra bases de datos.

**Configuración:**
```
Driver: com.mysql.jdbc.Driver
Connection String: jdbc:mysql://localhost:3306/testdb
SQL Query: SELECT * FROM users WHERE id = ${#TestCase#userId}
```

**Casos de uso:**
- Validar datos en base de datos
- Setup de datos de prueba
- Cleanup después de tests
- Verificar side-effects de API

**Ejemplo de flujo:**
```
Test Case: Crear usuario con validación DB
├── Step 1: POST /users (crear en API)
├── Step 2: Property Transfer (extraer userId)
├── Step 3: JDBC Request (SELECT * FROM users WHERE id=?)
└── Step 4: Groovy Script (comparar API vs DB)
```

#### 12. Manual TestStep

Requiere intervención manual del tester.

**Configuración:**
```
Instructions: "Verificar manualmente que el email fue enviado a test@ejemplo.com"
Expected Result: "Email recibido con asunto correcto"
```

**Casos de uso:**
- Verificaciones que no pueden automatizarse
- Pruebas de UI que requieren interacción
- Validaciones visuales
- Testing exploratorio guiado

### Combinaciones comunes de Test Steps

**Patrón 1: Autenticación reutilizable**
```
Test Case: TC-Auth-Setup
├── REST Request: POST /login
├── Property Transfer: Extraer token
└── [Token disponible en propiedades]

Otros Test Cases:
├── Run TestCase: TC-Auth-Setup
└── REST Request: usar ${#TestCase#authToken}
```

**Patrón 2: Validación completa**
```
Test Case: Validar creación usuario
├── Groovy Script: Generar datos únicos
├── REST Request: POST /users
├── Property Transfer: Extraer userId
├── REST Request: GET /users/${userId}
├── JDBC Request: SELECT de DB
└── Groovy Script: Comparar API vs DB
```

**Patrón 3: Data-driven testing**
```
Test Case: Login múltiples usuarios
├── DataSource: usuarios.csv
├── [Loop Start]
├──── REST Request: POST /login
├──── Assertions: Validar resultado esperado
├──── DataSink: Guardar resultado
└── [Loop End]
```

**Patrón 4: Retry logic**
```
Test Case: Polling de estado
├── REST Request: GET /job/${jobId}
├── Groovy Script: Verificar si completó
├── Conditional Goto: Si no completó → Delay
├── Delay: 5000ms
├── Conditional Goto: Goto "REST Request" (retry)
└── [Continuar cuando completó]
```

### Selección del Test Step apropiado

**Guía de decisión:**

```
¿Necesitas hacer una petición?
  └→ REST Request / SOAP Request

¿Necesitas lógica personalizada?
  └→ Groovy Script

¿Necesitas extraer datos de respuesta?
  └→ Property Transfer

¿Necesitas flujo condicional?
  └→ Conditional Goto

¿Necesitas esperar?
  └→ Delay

¿Necesitas reutilizar otro test?
  └→ Run TestCase

¿Necesitas validar base de datos?
  └→ JDBC Request

¿Necesitas múltiples datasets?
  └→ DataSource + loop
```

## Referencias externas

- **Test Steps Reference**: https://www.soapui.org/functional-testing/teststep-reference/
- **Groovy Scripting**: https://www.soapui.org/scripting-properties/scripting/
- **Property Transfer Guide**: https://www.soapui.org/functional-testing/transferring-property-values/
- **Data-Driven Testing**: https://www.soapui.org/functional-testing/data-driven-testing/
- **Conditional Execution**: https://www.soapui.org/functional-testing/conditional-execution/

## Resumen

SoapUI ofrece una amplia variedad de Test Steps especializados que permiten implementar flujos de prueba complejos y completos. Los Request Steps (REST, SOAP, HTTP) son fundamentales para interactuar con servicios, mientras que Logic Steps (Groovy Script, Conditional Goto, Delay) permiten implementar lógica personalizada y flujos condicionales. Data Steps (Property Transfer, DataSource, DataSink) facilitan el manejo de datos y testing parametrizado. Utility Steps (JDBC, Manual) cubren casos especiales. La selección apropiada y combinación efectiva de estos Test Steps es clave para crear suites de prueba robustas, mantenibles y escalables.
