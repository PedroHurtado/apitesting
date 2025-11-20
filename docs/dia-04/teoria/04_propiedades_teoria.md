# Propiedades de un Test Case - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Comprender qué son las propiedades en SoapUI
- Crear y gestionar propiedades de Test Cases
- Utilizar diferentes niveles de propiedades (Project, Suite, Case, Step)
- Expandir propiedades en requests y scripts
- Implementar data-driven testing con propiedades

## Duración estimada

15 minutos

## Prerrequisitos

- Comprender la estructura de Test Cases
- Conocimientos básicos de variables y datos
- Familiaridad con Test Steps

## Contenido

### ¿Qué son las propiedades?

Las **propiedades** (properties) en SoapUI son pares clave-valor que permiten almacenar y compartir datos entre diferentes elementos del proyecto. Funcionan como variables que pueden ser referenciadas y modificadas durante la ejecución de las pruebas.

**Analogía simple:**

```
Propiedad = Cajón etiquetado
  ├── Etiqueta (nombre): "direccionEmail"
  └── Contenido (valor): "test@ejemplo.com"
```

### Niveles de propiedades

SoapUI organiza las propiedades en una jerarquía de cuatro niveles:

```
1. Global Properties (Aplicación)
   └── 2. Project Properties (Proyecto)
       └── 3. TestSuite Properties (Test Suite)
           └── 4. TestCase Properties (Test Case)
```

**Ejemplo de jerarquía:**

```
Global:
  └── environment = "production"
      └── Project: API Usuarios
          └── baseURL = "https://api.ejemplo.com"
              └── TestSuite: Pruebas Usuarios
                  └── timeout = "5000"
                      └── TestCase: Crear Usuario
                          └── userEmail = "test@ejemplo.com"
```

### Alcance y visibilidad

Cada nivel tiene un alcance específico:

**1. Global Properties**
- Disponibles para todos los proyectos
- Se configuran en: File → Preferences → Global Properties
- Útiles para configuración del entorno de SoapUI

**2. Project Properties**
- Disponibles para todo el proyecto
- Típicamente: URLs base, credenciales, configuración común

**Ejemplo:**
```
baseURL = https://jsonplaceholder.typicode.com
apiVersion = v1
defaultTimeout = 30000
```

**3. TestSuite Properties**
- Disponibles para todos los Test Cases del Suite
- Útiles para datos compartidos entre casos de prueba

**Ejemplo:**
```
testDataPath = ./data/users.csv
testEnvironment = Development
```

**4. TestCase Properties**
- Disponibles solo dentro del Test Case
- Datos específicos del escenario de prueba

**Ejemplo:**
```
userId = 1
userName = Juan Pérez
userEmail = juan@ejemplo.com
```

### Crear propiedades manualmente

**Desde la interfaz:**

```
1. Seleccionar el nivel (Project/TestSuite/TestCase)
2. En el panel de propiedades, clic en ➕ (Add)
3. Configurar:
   - Name: nombre de la propiedad
   - Value: valor inicial
```

**Ejemplo de creación:**

```
Name: authToken
Value: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Crear propiedades desde Groovy

Las propiedades pueden crearse y modificarse dinámicamente con código Groovy:

**Establecer propiedad del Test Case:**
```groovy
// Crear/modificar propiedad
testRunner.testCase.setPropertyValue("userId", "123")
```

**Establecer propiedad del Test Suite:**
```groovy
// Acceder al Test Suite padre
testRunner.testCase.testSuite.setPropertyValue("timestamp", new Date().toString())
```

**Establecer propiedad del Project:**
```groovy
// Acceder al proyecto
testRunner.testCase.testSuite.project.setPropertyValue("lastRun", "2025-11-20")
```

### Leer propiedades

**Desde Groovy:**

```groovy
// Leer propiedad del Test Case
def userId = testRunner.testCase.getPropertyValue("userId")
log.info("User ID: " + userId)

// Leer propiedad del Test Suite
def environment = testRunner.testCase.testSuite.getPropertyValue("testEnvironment")

// Leer propiedad del Project
def baseURL = testRunner.testCase.testSuite.project.getPropertyValue("baseURL")
```

### Expansión de propiedades

Las propiedades pueden ser expandidas (referenciadas) usando la sintaxis `${}`:

**Sintaxis de expansión:**

```
${#nivel#nombrePropiedad}

Niveles:
  #Project# → Propiedad del proyecto
  #TestSuite# → Propiedad del test suite
  #TestCase# → Propiedad del test case
  #nombreStep# → Propiedad o respuesta de un step
```

**Ejemplos de uso:**

**1. En un REST Request:**
```
URL: ${#Project#baseURL}/users/${#TestCase#userId}
```
Resultado expandido:
```
URL: https://jsonplaceholder.typicode.com/users/123
```

**2. En un Header:**
```
Authorization: Bearer ${#TestCase#authToken}
```

**3. En el body de un request:**
```json
{
  "name": "${#TestCase#userName}",
  "email": "${#TestCase#userEmail}"
}
```

**4. En un Groovy Script:**
```groovy
// Expansión mediante context
def userId = context.expand('${#TestCase#userId}')
def baseURL = context.expand('${#Project#baseURL}')
```

### Transferencia de propiedades

**Property Transfer Step** permite extraer datos de respuestas y guardarlos como propiedades:

**Ejemplo de configuración:**

```
Source: Response del step "Login"
Source Property: $.token (JSONPath)
Target: TestCase
Target Property: authToken
```

**Escenario completo:**

```
Test Step 1: POST /login
  Response: {"token": "abc123xyz", "userId": 42}

Property Transfer:
  Extraer: $.token → guardarlo en ${#TestCase#authToken}
  Extraer: $.userId → guardarlo en ${#TestCase#userId}

Test Step 2: GET /profile
  Header: Authorization: Bearer ${#TestCase#authToken}
```

### Data-driven testing con propiedades

Las propiedades permiten parametrizar pruebas para ejecutarlas con diferentes datos:

**Ejemplo conceptual:**

```
TestCase Properties:
  testData = [
    {username: "user1", password: "pass1"},
    {username: "user2", password: "pass2"}
  ]

Groovy Script Loop:
  Para cada conjunto de datos:
    - Establecer propiedades
    - Ejecutar test steps
    - Validar resultados
```

### Propiedades dinámicas

Las propiedades pueden generarse dinámicamente durante la ejecución:

**Ejemplo: Generar datos únicos**

```groovy
// Timestamp único
def timestamp = System.currentTimeMillis()
testRunner.testCase.setPropertyValue("timestamp", timestamp.toString())

// Email aleatorio
def random = new Random().nextInt(10000)
def email = "user${random}@test.com"
testRunner.testCase.setPropertyValue("userEmail", email)

// UUID
def uuid = UUID.randomUUID().toString()
testRunner.testCase.setPropertyValue("requestId", uuid)
```

### Buenas prácticas

**1. Nomenclatura clara y consistente**
```
✓ baseURL, apiVersion, authToken
✗ url, ver, token (nombres ambiguos)
```

**2. Nivel apropiado**
```
✓ URLs base → Project Properties
✓ Datos de prueba específicos → TestCase Properties
✗ Todo en Global Properties (dificulta mantenimiento)
```

**3. Valores por defecto**
```groovy
// Usar valor por defecto si la propiedad no existe
def timeout = testRunner.testCase.getPropertyValue("timeout") ?: "5000"
```

**4. Documentación**
```
Property Name: authToken
Purpose: JWT token para autenticación de requests posteriores
Source: Extraído de response del step "Login"
Format: String (JWT)
```

**5. Limpieza**
```groovy
// En TearDown Script: limpiar propiedades temporales
testRunner.testCase.removeProperty("tempUserId")
```

### Debugging de propiedades

**Ver todas las propiedades:**

```groovy
// Listar propiedades del Test Case
testRunner.testCase.getPropertyNames().each { name ->
    def value = testRunner.testCase.getPropertyValue(name)
    log.info("${name} = ${value}")
}
```

**Verificar expansión:**

```groovy
// Ver cómo se expande una expresión
def expanded = context.expand('${#TestCase#userId}')
log.info("Valor expandido: " + expanded)
```

## Referencias externas

- **SoapUI Properties Documentation**: https://www.soapui.org/scripting-properties/property-expansion/
- **Property Transfer Guide**: https://www.soapui.org/functional-testing/transferring-property-values/
- **Groovy Scripting Reference**: https://www.soapui.org/scripting-properties/scripting/
- **JSONPath Syntax**: https://goessner.net/articles/JsonPath/
- **Data-Driven Testing**: https://www.soapui.org/functional-testing/data-driven-testing/

## Resumen

Las propiedades son el mecanismo fundamental en SoapUI para almacenar y compartir datos entre diferentes elementos de las pruebas. Organizadas en cuatro niveles jerárquicos (Global, Project, TestSuite, TestCase), permiten parametrizar requests, compartir resultados entre steps y implementar data-driven testing. La expansión de propiedades con la sintaxis `${}` facilita su uso en requests, headers y scripts. Dominar el uso de propiedades es esencial para crear pruebas flexibles, mantenibles y reutilizables.
