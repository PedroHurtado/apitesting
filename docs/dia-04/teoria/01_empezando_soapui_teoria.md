# Empezando a trabajar con SoapUI - Teoría

## Objetivos de aprendizaje

Al finalizar este tema, el alumno será capaz de:

- Comprender qué es SoapUI y sus casos de uso principales
- Instalar y configurar SoapUI en su entorno de desarrollo
- Navegar por la interfaz de usuario de SoapUI
- Crear su primer proyecto SOAP/REST en SoapUI
- Importar una especificación de servicio (WSDL/Swagger)

## Duración estimada

25 minutos

## Prerrequisitos

- Conocimientos básicos de servicios web SOAP y REST
- Comprensión de XML y JSON
- Familiaridad con conceptos de testing

## Contenido

### ¿Qué es SoapUI?

SoapUI es una herramienta open-source líder en la industria para el testing funcional de servicios web SOAP y REST. Permite crear, gestionar y ejecutar pruebas automatizadas de APIs de forma sencilla y eficiente.

**Características principales:**

- Testing funcional de servicios SOAP y REST
- Validación de mensajes mediante aserciones
- Creación de tests automatizados
- Simulación de servicios (MockServices)
- Generación de carga y pruebas de rendimiento
- Integración continua (CI/CD)

### Instalación de SoapUI

SoapUI está disponible en dos versiones:

1. **SoapUI Open Source**: Versión gratuita con funcionalidades básicas
2. **SoapUI Pro**: Versión comercial con características avanzadas

**Ejemplo de instalación en Windows:**

```
1. Descargar instalador desde https://www.soapui.org/downloads/soapui/
2. Ejecutar SoapUI-x64-5.7.0.exe
3. Seguir el asistente de instalación
4. Seleccionar componentes (Core + Tutorials recomendado)
5. Finalizar instalación
```

Para Linux/Mac, la instalación es similar utilizando los paquetes específicos para cada plataforma.

### Interfaz de usuario de SoapUI

La interfaz de SoapUI se organiza en varios paneles principales:

**1. Navigator (Panel izquierdo)**
- Muestra la estructura jerárquica de proyectos
- Permite navegar entre Test Suites, Test Cases y Test Steps
- Incluye acceso rápido a MockServices y LoadTests

**2. Editor (Panel central)**
- Área de trabajo principal
- Muestra requests, responses y configuraciones
- Permite editar XML/JSON y configurar pruebas

**3. Properties (Panel inferior derecho)**
- Muestra propiedades del elemento seleccionado
- Permite configurar parámetros y opciones
- Edición de custom properties

**4. Log (Panel inferior izquierdo)**
- Muestra mensajes del sistema
- Registros de ejecución de pruebas
- Información de depuración

### Creación del primer proyecto

SoapUI organiza el trabajo en **proyectos**, que contienen todos los elementos relacionados con el testing de uno o varios servicios.

**Ejemplo de estructura de un proyecto:**

```
Mi Proyecto SOAP
├── TestSuites
│   └── Suite de Pruebas Funcionales
│       └── TestCase: Validar Usuario
│           ├── Request: ObtenerUsuario
│           └── Assertions: Código 200
├── MockServices
└── LoadTests
```

### Tipos de proyectos en SoapUI

**1. Proyecto SOAP**

Se crea a partir de un archivo WSDL (Web Services Description Language):

```
Archivo → Nuevo Proyecto SOAP
Nombre: ServicioUsuarios
WSDL: http://ejemplo.com/usuarios?wsdl
```

**2. Proyecto REST**

Se crea manualmente o desde una especificación OpenAPI/Swagger:

```
Archivo → Nuevo Proyecto REST
Nombre: API Usuarios
URI: http://api.ejemplo.com/v1
```

### Importar especificaciones de servicio

**Importar WSDL para servicios SOAP:**

Un WSDL describe las operaciones, mensajes y tipos de datos de un servicio SOAP.

Ejemplo de URL WSDL:
```
http://www.dneonline.com/calculator.asmx?WSDL
```

SoapUI analiza automáticamente el WSDL y genera:
- Requests de ejemplo para cada operación
- Estructura XML correcta
- Namespaces necesarios

**Importar OpenAPI/Swagger para servicios REST:**

Las especificaciones OpenAPI describen endpoints REST, parámetros y respuestas.

Ejemplo de URL OpenAPI:
```
https://petstore.swagger.io/v2/swagger.json
```

SoapUI crea automáticamente:
- Recursos REST
- Métodos (GET, POST, PUT, DELETE)
- Parámetros de ruta y query
- Ejemplos de requests/responses

### Configuración inicial recomendada

**Preferencias básicas:**

```
File → Preferences → HTTP Settings
  - Timeout: 60000 ms
  - Proxy settings (si es necesario)

File → Preferences → Editor Settings
  - Validación XML habilitada
  - Formato automático activado
```

### Workspace y organización

SoapUI utiliza un **workspace** para gestionar múltiples proyectos:

- Por defecto: `default-soapui-workspace.xml`
- Ubicación: directorio de usuario de SoapUI
- Contiene referencias a todos los proyectos abiertos

**Ejemplo de buenas prácticas:**

1. Un proyecto por servicio o API
2. Nombrar proyectos descriptivamente
3. Guardar proyectos en control de versiones
4. Exportar/importar proyectos para compartir con el equipo

## Referencias externas

- **Documentación oficial de SoapUI**: https://www.soapui.org/docs/
- **SoapUI Getting Started Guide**: https://www.soapui.org/getting-started/
- **SmartBear Academy - SoapUI Fundamentals**: https://smartbear.com/resources/
- **WSDL 1.1 Specification**: https://www.w3.org/TR/wsdl
- **OpenAPI Specification**: https://swagger.io/specification/

## Resumen

SoapUI es una herramienta potente y versátil para el testing de servicios web SOAP y REST. Su interfaz intuitiva permite crear proyectos rápidamente a partir de especificaciones WSDL o OpenAPI. La organización jerárquica en proyectos, test suites y test cases facilita la gestión de pruebas complejas. Dominar la interfaz y la creación básica de proyectos es fundamental para aprovechar todas las capacidades de testing que ofrece la herramienta.
