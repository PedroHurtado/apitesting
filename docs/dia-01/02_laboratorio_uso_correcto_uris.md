# Laboratorio 1.2: Uso Correcto de URIs

## Objetivo
Practicar el diseño y uso correcto de URIs en APIs RESTful, identificando buenas y malas prácticas mediante peticiones reales.

## Duración Estimada
45 minutos

## Requisitos Previos
- Postman instalado
- Colección del laboratorio anterior
- Conexión a Internet

## Preparación

### Crear Nueva Colección
1. En Postman, crea una nueva colección llamada "Laboratorio URIs REST"
2. Organizaremos las peticiones por categorías

## Ejercicio 1: Analizar URIs Correctas vs Incorrectas

### Actividad 1.1: URIs con Recursos (Correcto)

**Objetivo**: Observar cómo las URIs bien diseñadas identifican recursos claramente.

1. **Petición: Obtener colección de usuarios**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users`
   - Envía la petición
   
   **Análisis**: 
   - La URI usa un sustantivo en plural (`users`)
   - No contiene verbos ni acciones
   - Es limpia y predecible

2. **Petición: Obtener usuario específico**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users/3`
   - Envía la petición
   
   **Análisis**:
   - El ID del recurso está en la URI
   - Mantiene la consistencia con la colección

**Pregunta**: ¿Qué diferencia hay entre `/users` y `/users/3` en términos de lo que representan?

### Actividad 1.2: Jerarquía de Recursos

**Objetivo**: Comprender cómo expresar relaciones entre recursos mediante URIs.

1. **Petición: Posts de un usuario**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users/5/posts`
   - Envía la petición
   
   **Análisis**:
   - La URI refleja la relación: "posts del usuario 5"
   - Es intuitiva y autodescriptiva

2. **Petición alternativa: Filtrar con query parameters**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts?userId=5`
   - Envía la petición
   
   **Análisis**:
   - Mismo resultado, diferente enfoque
   - Los query parameters se usan para filtrar

**Pregunta**: ¿Cuál de las dos URIs prefieres? ¿Por qué?

### Actividad 1.3: Sub-recursos Anidados

**Objetivo**: Trabajar con recursos que tienen múltiples niveles de jerarquía.

1. **Petición: Comentarios de un post específico**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts/1/comments`
   - Envía la petición
   
2. **Observa la estructura**:
   - `/posts/1/comments` = "comentarios del post 1"
   - La jerarquía es clara: post → comentarios

**Tarea**: Crea otras dos peticiones similares explorando diferentes posts.

## Ejercicio 2: Uso de Query Parameters

### Actividad 2.1: Filtrado con Parámetros

**Objetivo**: Usar query parameters correctamente para filtrar colecciones.

1. **Petición: Filtrar posts por usuario**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts?userId=1`
   
2. **Petición: Obtener un post específico por ID**
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/posts?id=5`

**Análisis**:
- Los query parameters (`?userId=1`) no identifican el recurso
- Se usan para filtrar o buscar dentro de una colección

**Pregunta**: ¿Cuál es la diferencia entre `/posts/5` y `/posts?id=5`?

### Actividad 2.2: Múltiples Parámetros

**Objetivo**: Combinar varios criterios de filtrado.

1. **Crear petición con múltiples parámetros**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/comments?postId=1&email=Eliseo@gardner.biz`
   
   (Nota: Esta API particular puede no soportar todos los filtros, pero el concepto es válido)

**Análisis**:
- Usa `&` para separar parámetros
- Mantén nombres descriptivos
- Usa minúsculas o camelCase consistentemente

## Ejercicio 3: Identificar Anti-patrones

### Actividad 3.1: Comparación de Diseños

**Instrucciones**: Para cada escenario, identifica cuál es el diseño correcto.

**Escenario 1: Obtener información de un usuario**

Opción A: `GET /obtenerUsuario?id=10`  
Opción B: `GET /users/10`

**Respuesta correcta**: B (la URI no debe contener verbos)

---

**Escenario 2: Listar productos de una categoría**

Opción A: `GET /categorias/5/productos`  
Opción B: `GET /listarProductosPorCategoria/5`

**Respuesta correcta**: A (usa sustantivos y refleja jerarquía)

---

**Escenario 3: Buscar productos por precio**

Opción A: `GET /productos/buscar?precio_max=100`  
Opción B: `GET /productos?precio_max=100`

**Respuesta correcta**: B (no añadas "buscar" a la URI)

---

**Tarea**: Escribe la URI correcta para estos casos:
1. Obtener todos los álbumes de un usuario
2. Obtener la foto número 15 del álbum 3
3. Listar todos los posts ordenados por título

## Ejercicio 4: Diseñar URIs para un Sistema Real

### Actividad 4.1: Sistema de Gestión Escolar

**Contexto**: Estás diseñando una API para una escuela con estos recursos:
- Estudiantes
- Cursos
- Profesores
- Calificaciones

**Tarea**: Diseña las URIs para estas operaciones:

1. Listar todos los estudiantes
   - Tu respuesta: _________________

2. Obtener información del estudiante con ID 25
   - Tu respuesta: _________________

3. Listar los cursos del estudiante 25
   - Tu respuesta: _________________

4. Obtener las calificaciones del estudiante 25 en el curso 8
   - Tu respuesta: _________________

5. Listar todos los profesores del curso 8
   - Tu respuesta: _________________

**Respuestas sugeridas**:
1. `GET /estudiantes`
2. `GET /estudiantes/25`
3. `GET /estudiantes/25/cursos`
4. `GET /estudiantes/25/cursos/8/calificaciones`
5. `GET /cursos/8/profesores`

## Resultados Esperados

Al finalizar este laboratorio, deberías:

1. Distinguir entre URIs bien y mal diseñadas
2. Usar sustantivos en plural para colecciones
3. Representar jerarquías de recursos en URIs
4. Aplicar query parameters para filtrado
5. Evitar verbos en las URIs
6. Diseñar URIs intuitivas y consistentes

## Preguntas de Autoevaluación

1. ¿Por qué es importante usar sustantivos en lugar de verbos en las URIs?
2. ¿Cuándo deberías usar jerarquía en URIs (`/recurso1/id/recurso2`) vs query parameters (`/recurso2?recurso1Id=id`)?
3. ¿Qué problemas puede causar una URI mal diseñada?
4. Da tres ejemplos de URIs incorrectas y corrígelas

## Documentación

Crea un documento en tu colección de Postman documentando:
- Las 5 mejores prácticas que aprendiste
- 3 anti-patrones que debes evitar
- Ejemplos de tu propio diseño de URIs

## Conclusión

El diseño de URIs es un arte que requiere pensar en los recursos como entidades, no como acciones. Una API con URIs bien diseñadas es más fácil de entender, usar y mantener.

---

**Consejo**: Antes de implementar una API, dedica tiempo a diseñar tus URIs. Un buen diseño desde el principio ahorrará muchos problemas futuros.
