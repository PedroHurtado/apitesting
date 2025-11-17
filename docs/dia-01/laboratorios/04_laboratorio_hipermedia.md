# Laboratorio 1.4: Hipermedia

## Objetivo
Comprender y experimentar con APIs que implementan hipermedia, aprendiendo a navegar APIs mediante enlaces y entendiendo el concepto HATEOAS.

## Duración Estimada
50 minutos

## Requisitos Previos
- Postman instalado
- Conocimientos de laboratorios anteriores
- Conexión a Internet

## Preparación

Crea una nueva colección en Postman llamada "Laboratorio Hipermedia".

## Ejercicio 1: Comparar API sin y con Hipermedia

### Actividad 1.1: API sin Hipermedia (JSONPlaceholder)

**Objetivo**: Observar una API tradicional sin enlaces hipermedia.

1. **Obtener un usuario**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users/1`
   - Envía la petición

2. **Analiza la respuesta**:
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "Bret",
  "email": "Sincere@april.biz",
  ...
}
```

3. **Observa**:
   - La respuesta contiene solo datos
   - No hay enlaces a recursos relacionados
   - Debes saber de antemano cómo acceder a los posts del usuario

**Pregunta**: ¿Cómo obtendrías los posts de este usuario? ¿Cómo lo sabrías sin documentación?

### Actividad 1.2: Construir Navegación Manual

1. **Obtener posts del usuario**:
   - Debes construir tú la URI: `https://jsonplaceholder.typicode.com/posts?userId=1`
   - Método: GET
   - Envía la petición

2. **Reflexión**:
   - Tuviste que saber la estructura de la API
   - Si cambia la URI, tu cliente se rompe
   - No hay "descubrimiento" de funcionalidades

## Ejercicio 2: Simulación de API con Hipermedia

Vamos a crear peticiones que simulan una API con hipermedia usando Postman Mock Server.

### Actividad 2.1: Configurar Mock Server

1. **Crear un Mock Server básico**:
   - En tu colección, crea una nueva carpeta "API con Hipermedia"
   
2. **Crear petición simulada de libro**:
   - Nombre: "Obtener Libro con Enlaces"
   - Método: GET
   - URL: `https://api-hipermedia.example.com/libros/123` (ficticia para documentación)

3. **Documentar respuesta esperada con hipermedia**:
   - En la pestaña "Examples" de la petición, crea un ejemplo
   - Status Code: 200
   - Body (simula la respuesta):

```json
{
  "id": 123,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "precio": 25.99,
  "disponible": true,
  "isbn": "978-0307474728",
  "_links": {
    "self": {
      "href": "https://api-hipermedia.example.com/libros/123",
      "type": "GET"
    },
    "autor": {
      "href": "https://api-hipermedia.example.com/autores/456",
      "type": "GET",
      "title": "Información del autor"
    },
    "relacionados": {
      "href": "https://api-hipermedia.example.com/libros/123/relacionados",
      "type": "GET",
      "title": "Libros relacionados"
    },
    "comprar": {
      "href": "https://api-hipermedia.example.com/carrito/items",
      "type": "POST",
      "title": "Añadir al carrito"
    },
    "reviews": {
      "href": "https://api-hipermedia.example.com/libros/123/reviews",
      "type": "GET",
      "title": "Ver reseñas"
    }
  }
}
```

**Análisis**:
- `_links`: Sección que contiene todos los enlaces
- `self`: Enlace al recurso actual
- Cada enlace tiene `href` (URL) y opcionalmente `type` (método) y `title` (descripción)

## Ejercicio 3: Hipermedia Condicional

### Actividad 3.1: Estados Diferentes, Enlaces Diferentes

**Objetivo**: Comprender cómo los enlaces cambian según el estado del recurso.

1. **Crear ejemplo: Libro Disponible**:
   - Nombre: "Libro Disponible"
   - Documenta en Examples:

```json
{
  "id": 124,
  "titulo": "Don Quijote",
  "disponible": true,
  "stock": 15,
  "_links": {
    "self": {"href": "/libros/124"},
    "comprar": {
      "href": "/carrito/items",
      "method": "POST",
      "title": "Añadir al carrito"
    },
    "reservar": {
      "href": "/reservas",
      "method": "POST",
      "title": "Reservar ejemplar"
    }
  }
}
```

2. **Crear ejemplo: Libro No Disponible**:
   - Nombre: "Libro No Disponible"
   - Documenta en Examples:

```json
{
  "id": 124,
  "titulo": "Don Quijote",
  "disponible": false,
  "stock": 0,
  "_links": {
    "self": {"href": "/libros/124"},
    "notificar": {
      "href": "/notificaciones",
      "method": "POST",
      "title": "Notificarme cuando esté disponible"
    },
    "alternativas": {
      "href": "/libros/124/alternativas",
      "method": "GET",
      "title": "Ver libros similares"
    }
  }
}
```

**Pregunta**: ¿Qué ventaja tiene que los enlaces cambien dinámicamente según el estado?

## Ejercicio 4: Formato HAL (Hypertext Application Language)

### Actividad 4.1: Crear Respuesta en Formato HAL

**Objetivo**: Familiarizarse con el formato HAL estándar.

1. **Crear petición**: "Usuario con HAL"

2. **Documentar respuesta HAL**:

```json
{
  "id": 789,
  "nombre": "María García",
  "email": "maria@ejemplo.com",
  "telefono": "+34 600 123 456",
  "_links": {
    "self": {
      "href": "/usuarios/789"
    },
    "pedidos": {
      "href": "/usuarios/789/pedidos"
    },
    "direcciones": {
      "href": "/usuarios/789/direcciones"
    }
  },
  "_embedded": {
    "direccion_principal": {
      "id": 45,
      "calle": "Gran Vía 123",
      "ciudad": "Madrid",
      "codigo_postal": "28013",
      "_links": {
        "self": {"href": "/direcciones/45"}
      }
    }
  }
}
```

**Características de HAL**:
- `_links`: Enlaces a recursos relacionados
- `_embedded`: Recursos incrustados (para reducir peticiones)

## Ejercicio 5: Navegación Guiada por Hipermedia

### Actividad 5.1: Flujo de Compra con Enlaces

**Objetivo**: Simular un flujo completo guiado por hipermedia.

**Paso 1: Obtener producto**
```json
{
  "id": 501,
  "nombre": "Laptop HP",
  "precio": 899.99,
  "_links": {
    "self": {"href": "/productos/501"},
    "añadir-carrito": {"href": "/carrito/items", "method": "POST"}
  }
}
```

**Paso 2: Añadir al carrito**
Respuesta después de POST:
```json
{
  "carrito_id": "abc123",
  "items": [
    {
      "producto_id": 501,
      "cantidad": 1,
      "subtotal": 899.99
    }
  ],
  "total": 899.99,
  "_links": {
    "self": {"href": "/carrito/abc123"},
    "continuar-comprando": {"href": "/productos"},
    "proceder-pago": {"href": "/checkout", "method": "POST"}
  }
}
```

**Paso 3: Proceder al pago**
Respuesta después de iniciar checkout:
```json
{
  "pedido_id": 1001,
  "estado": "pendiente_pago",
  "total": 899.99,
  "_links": {
    "self": {"href": "/pedidos/1001"},
    "pagar-tarjeta": {"href": "/pagos/tarjeta", "method": "POST"},
    "pagar-paypal": {"href": "/pagos/paypal", "method": "POST"},
    "cancelar": {"href": "/pedidos/1001", "method": "DELETE"}
  }
}
```

**Observación**: El cliente solo necesita seguir los enlaces; no construye URIs.

## Ejercicio 6: Análisis Comparativo

### Actividad 6.1: Tabla de Ventajas/Desventajas

Completa esta tabla basándote en lo aprendido:

| Aspecto | Sin Hipermedia | Con Hipermedia |
|---------|----------------|----------------|
| **Desacoplamiento** | Cliente debe conocer URIs | Servidor controla navegación |
| **Evolución** | Cambios rompen clientes | URIs pueden cambiar sin impacto |
| **Descubribilidad** | Requiere documentación externa | Enlaces muestran opciones |
| **Complejidad cliente** | Más simple | Debe interpretar enlaces |
| **Tamaño respuesta** | Menor | Mayor (incluye enlaces) |

## Ejercicio 7: Diseño de Tu Propia API con Hipermedia

### Actividad 7.1: Sistema de Biblioteca

**Contexto**: Diseña las respuestas hipermedia para una biblioteca.

**Tarea 1**: Diseña la respuesta para un libro prestado:
```json
{
  "id": 200,
  "titulo": "1984",
  "estado": "prestado",
  "fecha_devolucion": "2024-11-30",
  "_links": {
    // Añade los enlaces apropiados
    "self": {"href": "???"},
    // ¿Qué otras acciones son posibles?
  }
}
```

**Tarea 2**: Diseña la respuesta para un libro disponible:
```json
{
  "id": 200,
  "titulo": "1984",
  "estado": "disponible",
  "_links": {
    // ¿Qué acciones son posibles ahora?
  }
}
```

**Solución sugerida**:
- Prestado: `renovar`, `devolver`, `ver-historial`
- Disponible: `prestar`, `reservar`, `ver-disponibilidad`

## Resultados Esperados

Al finalizar este laboratorio, deberías:

1. Entender qué es hipermedia y HATEOAS
2. Distinguir APIs con y sin hipermedia
3. Interpretar enlaces en formato HAL
4. Comprender la navegación guiada por enlaces
5. Diseñar respuestas con hipermedia apropiada

## Preguntas de Autoevaluación

1. ¿Qué significa HATEOAS?
2. ¿Qué ventaja tiene que los enlaces cambien según el estado del recurso?
3. ¿Por qué hipermedia hace las APIs más flexibles?
4. ¿Cuál es el nivel de madurez Richardson que incluye hipermedia?
5. ¿Cuándo podrías omitir hipermedia en tu API?

## Recursos Adicionales

Para explorar APIs reales con hipermedia:
- GitHub API (usa HAL parcialmente)
- PayPal API (implementa HATEOAS)

## Conclusión

Hipermedia transforma tu API de una simple interfaz HTTP a una verdadera aplicación RESTful. Aunque añade complejidad, proporciona flexibilidad, descubribilidad y desacoplamiento que son invaluables en APIs de larga duración.

---

**Recordatorio**: La mayoría de las "APIs REST" del mundo real no implementan hipermedia completa (están en nivel 2 de Richardson). Sin embargo, conocer estos conceptos te permite diseñar APIs más robustas y evolutivas.
