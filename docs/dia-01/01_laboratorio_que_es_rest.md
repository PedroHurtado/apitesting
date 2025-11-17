# Laboratorio 1.1: ¿Qué es REST? - Primeros Pasos

## Objetivo
Familiarizarse con los conceptos básicos de REST realizando peticiones HTTP a una API pública y observando cómo funciona el intercambio de recursos y representaciones.

## Duración Estimada
30 minutos

## Requisitos Previos
- Postman instalado en tu equipo
- Conexión a Internet

## Preparación del Entorno

### Paso 1: Instalar Postman
1. Descarga Postman desde https://www.postman.com/downloads/
2. Instala la aplicación en tu sistema operativo
3. Crea una cuenta o continúa como invitado

### Paso 2: Crear una Nueva Colección
1. Abre Postman
2. En el panel izquierdo, haz clic en "Collections"
3. Clic en el botón "+" para crear una nueva colección
4. Nombra la colección como "Laboratorio REST - Conceptos Básicos"

## Ejercicio: Explorando una API REST Pública

Utilizaremos la API pública JSONPlaceholder (https://jsonplaceholder.typicode.com/), que simula una API REST para practicar.

### Actividad 1: Obtener un Recurso (GET)

**Objetivo**: Entender cómo se solicita la representación de un recurso.

1. **Crear una nueva petición**:
   - Clic en "Add request" dentro de tu colección
   - Nombre: "Obtener Usuario 1"
   
2. **Configurar la petición**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users/1`
   
3. **Enviar la petición**:
   - Clic en el botón "Send"
   
4. **Analizar la respuesta**:
   - Observa el código de estado HTTP (debería ser 200 OK)
   - Examina el cuerpo de la respuesta en formato JSON
   - Identifica los campos del recurso "usuario"

**Pregunta de reflexión**: ¿Qué representa la URI en esta petición? ¿Qué representa el JSON recibido?

### Actividad 2: Obtener una Colección de Recursos

**Objetivo**: Observar cómo REST maneja múltiples recursos.

1. **Crear una nueva petición**:
   - Nombre: "Obtener Todos los Posts"
   
2. **Configurar la petición**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts`
   
3. **Enviar y analizar**:
   - Observa que recibes un array de recursos
   - Cuenta cuántos posts se devuelven
   - Examina la estructura de cada post

**Pregunta de reflexión**: ¿Por qué la URI no incluye un ID específico en este caso?

### Actividad 3: Verificar el Principio Sin Estado (Stateless)

**Objetivo**: Comprobar que cada petición es independiente.

1. **Enviar múltiples peticiones**:
   - Envía la petición "Obtener Usuario 1" tres veces seguidas
   - Observa que cada respuesta es idéntica
   
2. **Análisis**:
   - Cada petición contiene toda la información necesaria (la URL completa)
   - El servidor no "recuerda" peticiones anteriores
   - No existe concepto de "sesión" en REST puro

### Actividad 4: Explorar Headers de Respuesta

**Objetivo**: Entender los metadatos que acompañan a los recursos.

1. **En cualquier petición anterior**:
   - Haz clic en la pestaña "Headers" en la sección de respuesta
   
2. **Identificar headers importantes**:
   - `Content-Type`: Indica el formato de la representación (application/json)
   - `Cache-Control`: Información sobre caché
   - `Date`: Timestamp de la respuesta
   
**Pregunta de reflexión**: ¿Qué te indica el header Content-Type sobre la representación del recurso?

## Resultados Esperados

Al completar este laboratorio, deberías:

1. Haber realizado exitosamente 3 peticiones GET diferentes
2. Comprender que una URI identifica un recurso
3. Entender que JSON es una representación del recurso
4. Observar el principio sin estado en acción
5. Identificar headers HTTP básicos

## Preguntas de Autoevaluación

1. ¿Qué diferencia hay entre un recurso y su representación?
2. ¿Por qué es importante que REST sea "sin estado"?
3. ¿Qué información proporciona el código de estado HTTP 200?
4. ¿Podría un mismo recurso tener múltiples representaciones? ¿Cómo?

## Limpieza

Guarda tu colección de Postman para futuras referencias:
- File → Export → Elige formato "Collection v2.1"
- Guarda el archivo JSON en tu carpeta de trabajo

## Conclusión

Has experimentado con los conceptos fundamentales de REST: recursos identificados por URIs, representaciones en formato JSON, y el principio sin estado. Estos son los pilares sobre los que se construyen todas las APIs RESTful.

---

**Nota**: JSONPlaceholder es una API de prueba gratuita ideal para aprender. No requiere autenticación y está diseñada específicamente para fines educativos.
