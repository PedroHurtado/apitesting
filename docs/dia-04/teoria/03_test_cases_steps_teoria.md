# Creación/Edición de Test Cases y Test Steps - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Comprender qué son Test Cases y Test Steps
- Crear Test Cases dentro de Test Suites
- Añadir y configurar diferentes tipos de Test Steps
- Organizar la secuencia de ejecución de Test Steps
- Implementar flujos de prueba complejos

## Duración estimada

25 minutos

## Prerrequisitos

- Comprender la estructura de Test Suites
- Familiaridad con servicios REST/SOAP
- Conocimientos básicos de HTTP

## Contenido

### ¿Qué es un Test Case?

Un **Test Case** (caso de prueba) es un escenario de prueba específico que valida un comportamiento particular del servicio web. Contiene una secuencia ordenada de Test Steps que se ejecutan para verificar una funcionalidad concreta.

**Analogía simple:**

```
Test Case = Receta de cocina
  ├── Test Step 1 = Precalentar horno
  ├── Test Step 2 = Mezclar ingredientes
  ├── Test Step 3 = Hornear 30 minutos
  └── Test Step 4 = Verificar que está cocido
```

### ¿Qué es un Test Step?

Un **Test Step** (paso de prueba) es la unidad mínima de ejecución en SoapUI. Representa una acción específica como:

- Enviar una petición HTTP
- Validar una respuesta
- Ejecutar código Groovy
- Esperar un tiempo determinado
- Transferir datos entre steps

**Ejemplo de Test Case completo:**

```
Test Case: Crear y validar usuario nuevo
├── Test Step 1: REST Request - POST /users (crear usuario)
├── Test Step 2: Assertion - Validar código 201
├── Test Step 3: Property Transfer - Extraer ID del usuario creado
├── Test Step 4: REST Request - GET /users/{id} (obtener usuario)
└── Test Step 5: Assertion - Verificar datos del usuario
```

### Jerarquía completa en SoapUI

```
Proyecto
└── Test Suite: Pruebas de Usuarios
    └── Test Case: Crear Usuario
        ├── Test Step: POST /users
        │   ├── Request (petición)
        │   ├── Response (respuesta)
        │   └── Assertions (validaciones)
        ├── Test Step: Groovy Script
        └── Test Step: GET /users/{id}
```

### Creación de Test Cases

**Método 1: Desde Test Suite**

```
1. Clic derecho sobre el Test Suite
2. Seleccionar "New TestCase"
3. Introducir nombre descriptivo
4. Opcionalmente añadir descripción
```

**Método 2: Generar desde request existente**

```
1. Clic derecho sobre un Request
2. Seleccionar "Add to TestCase"
3. Elegir Test Suite y Test Case (nuevo o existente)
```

**Ejemplo de nombres descriptivos:**

- ✓ `TC01 - Crear usuario con datos válidos`
- ✓ `Validar login con credenciales correctas`
- ✓ `Error 404 al buscar usuario inexistente`
- ✗ `Test1` (poco descriptivo)

### Propiedades de Test Cases

**1. Name (Nombre)**
- Identificador único dentro del Test Suite

**2. Description (Descripción)**
- Detalle del escenario de prueba
- Condiciones previas y resultados esperados

**Ejemplo:**
```
Nombre: TC01 - Crear Usuario Completo
Descripción:
  Precondiciones: Sistema inicializado, base de datos limpia
  Pasos: Enviar POST con datos válidos de usuario
  Resultado esperado: Código 201, usuario creado con ID asignado
```

**3. Fail on Error**
- Detiene el Test Case si un step falla

**4. Fail TestCase on Error**
- Marca el Test Case completo como fallido

**5. KeepSession**
- Mantiene cookies y sesión HTTP entre steps

### Tipos de Test Steps

SoapUI ofrece múltiples tipos de Test Steps:

**1. REST Request**
- Envía petición a un servicio REST
- Configurable (método, parámetros, headers, body)

**Ejemplo:**
```
Step: Obtener lista de usuarios
Tipo: REST Request
Método: GET
Endpoint: /users
```

**2. SOAP Request**
- Envía petición a un servicio SOAP
- Genera automáticamente el XML desde WSDL

**3. Groovy Script**
- Ejecuta código Groovy personalizado
- Lógica condicional, manipulación de datos, logging

**Ejemplo simple:**
```groovy
// Generar email aleatorio para test
def random = new Random().nextInt(10000)
def email = "test${random}@example.com"
testRunner.testCase.setPropertyValue("userEmail", email)
log.info("Email generado: " + email)
```

**4. Property Transfer**
- Extrae datos de una respuesta
- Los transfiere a propiedades o requests siguientes

**Ejemplo:**
```
Source: Response del POST /users
Property: $.id (extraer ID del JSON)
Target: Request GET /users/{id}
```

**5. Delay**
- Pausa la ejecución durante un tiempo especificado
- Útil para esperar procesamiento asíncrono

**Ejemplo:**
```
Delay: 2000 ms (2 segundos)
```

**6. Conditional Goto**
- Salto condicional entre steps
- Implementa lógica de flujo

**7. Run TestCase**
- Ejecuta otro Test Case
- Permite reutilización y modularidad

### Creación de Test Steps

**Para añadir un REST Request Step:**

```
1. Clic derecho en el Test Case
2. Add Step → REST Request
3. Configurar:
   - Nombre del step
   - Seleccionar servicio REST
   - Elegir método (GET, POST, etc.)
   - Configurar parámetros
```

**Para añadir un Groovy Script Step:**

```
1. Clic derecho en el Test Case
2. Add Step → Groovy Script
3. Escribir código en el editor
```

### Configuración de REST Request Steps

Un REST Request Step incluye:

**1. Request (Petición)**

```
Method: POST
Endpoint: /users
Headers:
  Content-Type: application/json
Body:
{
  "name": "Juan Pérez",
  "email": "juan@example.com"
}
```

**2. Response (Respuesta)**
- Visualización de la respuesta recibida
- Headers de respuesta
- Tiempo de respuesta

**3. Assertions (Validaciones)**
- Verificaciones sobre la respuesta
- Se verán en detalle en el tema de aserciones

### Secuencia de ejecución

Los Test Steps se ejecutan en orden secuencial de arriba hacia abajo:

```
Test Case: Flujo Completo Usuario
  1. Groovy Script: Generar datos de prueba ← Ejecuta primero
  2. REST Request: POST /users       ← Ejecuta segundo
  3. Property Transfer: Extraer ID   ← Ejecuta tercero
  4. REST Request: GET /users/{id}   ← Ejecuta cuarto
  5. Assertions: Validar datos       ← Ejecuta último
```

**Reordenar Steps:**

```
1. Seleccionar el Test Step
2. Usar botones ↑↓ o arrastrar
3. El orden afecta la ejecución
```

### Deshabilitar Test Steps

Puedes deshabilitar steps sin eliminarlos:

```
1. Clic derecho sobre el Test Step
2. Seleccionar "Disable"
3. El step aparecerá en gris y no se ejecutará
```

Útil para:
- Debugging temporal
- Pruebas selectivas
- Mantener steps de respaldo

### Copiar y duplicar Test Steps

```
1. Clic derecho sobre el Test Step
2. Clone TestStep (duplicar en mismo Test Case)
   o Copy TestStep (copiar a otro Test Case)
```

### Buenas prácticas para Test Cases

1. **Un objetivo por Test Case**: Cada Test Case debe validar un escenario específico
2. **Nombres descriptivos**: Indica claramente qué se está probando
3. **Independencia**: Los Test Cases no deben depender unos de otros
4. **Datos de prueba limpios**: Usar setup scripts para preparar datos
5. **Aserciones completas**: Validar todos los aspectos importantes
6. **Documentación**: Añadir descripciones claras

**Ejemplo de mal diseño:**

```
❌ Test Case: Pruebas Varias
  ├── POST /users
  ├── POST /products
  ├── GET /orders
  └── DELETE /cart
(Mezcla múltiples funcionalidades)
```

**Ejemplo de buen diseño:**

```
✓ Test Case: Crear usuario con datos válidos
  ├── Groovy: Generar datos únicos
  ├── POST /users
  ├── Assert: Código 201
  └── Assert: Respuesta contiene ID
(Objetivo claro y enfocado)
```

### Flujos complejos con Test Steps

**Ejemplo de flujo con lógica condicional:**

```
Test Case: Login y acceso condicional
├── POST /login (autenticar)
├── Property Transfer (extraer token)
├── Groovy Script (validar token)
├── Conditional Goto (si token válido → continuar)
├── GET /profile (con token)
└── Assertion (validar datos perfil)
```

## Referencias externas

- **SoapUI Test Case Documentation**: https://www.soapui.org/functional-testing/working-with-testcases/
- **Test Steps Reference**: https://www.soapui.org/functional-testing/teststep-reference/
- **Groovy Scripting Guide**: https://www.soapui.org/scripting-properties/scripting/
- **Property Transfer Tutorial**: https://www.soapui.org/functional-testing/transferring-property-values/
- **REST Testing Best Practices**: https://smartbear.com/learn/api-testing/rest-testing/

## Resumen

Los Test Cases son escenarios de prueba específicos compuestos por secuencias de Test Steps. Cada Test Step representa una acción atómica como enviar una petición, validar una respuesta o ejecutar lógica personalizada. La correcta organización y configuración de Test Cases y Test Steps es fundamental para crear pruebas efectivas, mantenibles y escalables. SoapUI ofrece múltiples tipos de Test Steps que permiten implementar flujos complejos, validaciones exhaustivas y reutilización de código.
