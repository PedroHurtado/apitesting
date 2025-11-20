# Testing con SoapUI - Material Completo

## Información del Curso

**Módulo**: Testing con SoapUI  
**Código**: APT-101 (Testing de APIs con Postman y SoapUI)  
**Duración**: 5 horas  
**Nivel**: Intermedio  

## Descripción

Este material cubre de forma exhaustiva el testing de servicios web utilizando SoapUI. El contenido está organizado en 7 temas principales, cada uno con un documento de teoría y un laboratorio práctico.

## Objetivos de Aprendizaje

Al finalizar este módulo, el alumno será capaz de:

- Instalar y configurar SoapUI para testing de APIs
- Crear y organizar Test Suites y Test Cases estructurados
- Implementar Test Steps de diferentes tipos
- Configurar y utilizar propiedades multinivel
- Aplicar aserciones para validar respuestas
- Ejecutar y depurar planes de prueba
- Generar reportes de testing automatizado

## Estructura del Material

### Teoría

Cada documento de teoría incluye:
- Objetivos de aprendizaje específicos
- Duración estimada
- Prerrequisitos
- Contenido teórico con ejemplos sencillos
- Referencias externas de calidad
- Resumen ejecutivo

### Laboratorios

Cada documento de laboratorio incluye:
- Objetivos prácticos
- Duración estimada
- Un ejercicio práctico paso a paso
- Resultados esperados
- Autoevaluación con preguntas y respuestas
- Ejercicio adicional opcional
- Notas importantes

## Contenidos del Curso

### 1. Empezando a trabajar con SoapUI (50 minutos)

**Teoría** (25 min): `teoria/01_empezando_soapui_teoria.md`
- Qué es SoapUI y sus características
- Instalación y configuración
- Interfaz de usuario
- Creación del primer proyecto
- Importar especificaciones WSDL/OpenAPI

**Laboratorio** (25 min): `laboratorio/01_empezando_soapui_lab.md`
- Instalación de SoapUI
- Crear proyecto REST con JSONPlaceholder
- Primera petición y análisis de respuesta

---

### 2. Creación de Planes de Pruebas (Test Suites) (40 minutos)

**Teoría** (20 min): `teoria/02_test_suites_teoria.md`
- Qué es un Test Suite
- Estructura jerárquica
- Propiedades y configuración
- Estrategias de organización
- Scripts de Setup/TearDown

**Laboratorio** (20 min): `laboratorio/02_test_suites_lab.md`
- Crear Test Suite organizado
- Configurar propiedades personalizadas
- Implementar scripts de inicialización y finalización

---

### 3. Creación/Edición de Test Cases y Test Steps (50 minutos)

**Teoría** (25 min): `teoria/03_test_cases_steps_teoria.md`
- Qué son Test Cases y Test Steps
- Tipos de Test Steps disponibles
- Configuración de REST Request Steps
- Secuencia de ejecución
- Buenas prácticas

**Laboratorio** (25 min): `laboratorio/03_test_cases_steps_lab.md`
- Crear Test Case con múltiples steps
- REST Request y Groovy Script Steps
- Flujo de datos entre steps

---

### 4. Propiedades de un Test Case (35 minutos)

**Teoría** (15 min): `teoria/04_propiedades_teoria.md`
- Niveles de propiedades (Global, Project, Suite, Case)
- Crear y leer propiedades
- Expansión de propiedades con ${}
- Property Transfer
- Data-driven testing

**Laboratorio** (20 min): `laboratorio/04_propiedades_lab.md`
- Propiedades multinivel
- Expansión en REST requests
- Property Transfer para extraer datos
- Validación con propiedades

---

### 5. Tipos de Aserciones (45 minutos)

**Teoría** (20 min): `teoria/05_aserciones_teoria.md`
- Qué son las aserciones y su importancia
- Valid HTTP Status Codes
- Contains / Not Contains
- JSONPath Match
- Response SLA
- Script Assertions
- Combinación de aserciones

**Laboratorio** (25 min): `laboratorio/05_aserciones_lab.md`
- Configurar 11 tipos de aserciones
- Validación de status, contenido y rendimiento
- Script Assertion personalizada
- Manejo de errores

---

### 6. Ejecución y Depuración de Planes de Prueba (40 minutos)

**Teoría** (20 min): `teoria/06_ejecucion_depuracion_teoria.md`
- Modos de ejecución
- Interpretar resultados
- Panel TestCase Log
- Breakpoints y depuración
- Análisis de fallos
- Reportes de ejecución
- Ejecución desde línea de comandos

**Laboratorio** (20 min): `laboratorio/06_ejecucion_depuracion_lab.md`
- Ejecutar Test Suite completo
- Usar breakpoints
- Analizar logs detallados
- Provocar y diagnosticar errores

---

### 7. Tipos de Pasos de Pruebas (40 minutos)

**Teoría** (20 min): `teoria/07_tipos_pasos_teoria.md`
- Request Steps (REST, SOAP, HTTP)
- Logic Steps (Groovy, Conditional Goto, Delay, Run TestCase)
- Data Steps (Property Transfer, DataSource, DataSink)
- Utility Steps (JDBC, Manual)
- Combinaciones comunes
- Selección del step apropiado

**Laboratorio** (20 min): `laboratorio/07_tipos_pasos_lab.md`
- Flujo con Conditional Goto y retry logic
- Data-driven testing con DataSource Grid
- Loop de ejecución con múltiples datasets

---

## Distribución de Tiempo

| Tema | Teoría | Laboratorio | Total |
|------|--------|-------------|-------|
| 1. Empezando con SoapUI | 25 min | 25 min | 50 min |
| 2. Test Suites | 20 min | 20 min | 40 min |
| 3. Test Cases y Steps | 25 min | 25 min | 50 min |
| 4. Propiedades | 15 min | 20 min | 35 min |
| 5. Aserciones | 20 min | 25 min | 45 min |
| 6. Ejecución y Depuración | 20 min | 20 min | 40 min |
| 7. Tipos de Pasos | 20 min | 20 min | 40 min |
| **TOTAL** | **145 min** | **155 min** | **300 min (5 horas)** |

## Requisitos Previos

### Conocimientos del Alumno
- Programación JavaScript consolidada
- Tecnología Web
- Servicios Web (REST/SOAP)
- Conceptos básicos de testing

### Software Necesario
- SoapUI Open Source (última versión)
- Conexión a Internet (para usar JSONPlaceholder API)
- Java Runtime Environment (JRE) 8 o superior

## API de Prueba Utilizada

Los laboratorios utilizan **JSONPlaceholder** (https://jsonplaceholder.typicode.com), una API REST gratuita y pública que:
- No requiere autenticación
- Proporciona datos de prueba consistentes
- Es ideal para aprendizaje y testing

## Metodología

Cada tema sigue una metodología dual:

1. **Teoría**: Explicación conceptual con ejemplos sencillos
2. **Laboratorio**: Práctica guiada paso a paso con un solo ejemplo completo

Esta estructura permite:
- Comprensión teórica sólida
- Aplicación práctica inmediata
- Aprendizaje progresivo y acumulativo

## Referencias Externas Principales

- **SoapUI Documentación Oficial**: https://www.soapui.org/docs/
- **SmartBear Academy**: https://smartbear.com/resources/
- **JSONPath Syntax**: https://goessner.net/articles/JsonPath/
- **HTTP Status Codes**: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
- **Groovy Language**: http://groovy-lang.org/documentation.html

## Navegación del Material

```
soapui/
├── teoria/
│   ├── 01_empezando_soapui_teoria.md
│   ├── 02_test_suites_teoria.md
│   ├── 03_test_cases_steps_teoria.md
│   ├── 04_propiedades_teoria.md
│   ├── 05_aserciones_teoria.md
│   ├── 06_ejecucion_depuracion_teoria.md
│   └── 07_tipos_pasos_teoria.md
└── laboratorio/
    ├── 01_empezando_soapui_lab.md
    ├── 02_test_suites_lab.md
    ├── 03_test_cases_steps_lab.md
    ├── 04_propiedades_lab.md
    ├── 05_aserciones_lab.md
    ├── 06_ejecucion_depuracion_lab.md
    └── 07_tipos_pasos_lab.md
```

## Recomendaciones para el Instructor

1. **Seguir el orden secuencial**: Los temas construyen sobre conocimientos previos
2. **Alternar teoría y práctica**: No acumular toda la teoría antes de los laboratorios
3. **Permitir tiempo extra**: Los laboratorios pueden requerir más tiempo si surgen dudas
4. **Fomentar experimentación**: Animar a los alumnos a probar variaciones
5. **Guardar proyectos**: Los alumnos deben guardar su trabajo para referencia futura

## Evaluación

Se recomienda evaluar mediante:
- Realización correcta de los laboratorios
- Respuestas a las autoevaluaciones
- Proyecto final: crear un Test Suite completo para una API real
- Presentación de resultados con reportes generados

## Soporte y Recursos Adicionales

- Comunidad SoapUI: https://community.smartbear.com/
- Stack Overflow: tag [soapui]
- GitHub: proyectos ejemplo de SoapUI

## Licencia del Material

Este material educativo ha sido desarrollado para uso en formación técnica sobre testing de APIs con SoapUI.

---

**Fecha de última actualización**: Noviembre 2025  
**Versión**: 1.0
