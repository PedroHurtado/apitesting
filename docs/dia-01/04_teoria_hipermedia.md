# 1.4 Hipermedia

## Introducción

Hipermedia es uno de los conceptos más importantes y a menudo malentendidos de REST. El término "hipermedia" se refiere a la inclusión de enlaces (links) dentro de las representaciones de recursos que guían al cliente sobre qué acciones puede realizar a continuación. Este concepto convierte una API REST en una API verdaderamente RESTful.

## ¿Qué es Hipermedia?

Hipermedia, en el contexto de REST, significa que las respuestas del servidor incluyen no solo datos, sino también enlaces a recursos relacionados y acciones disponibles. Es similar a cómo funciona la web: cuando visitas una página, los enlaces te dicen a dónde puedes navegar desde allí.

## El Principio HATEOAS

**HATEOAS** (Hypermedia As The Engine Of Application State) es una restricción de REST que establece que las interacciones con la API deben ser dirigidas por hipermedia incluida en las respuestas del servidor.

### Concepto Fundamental

En lugar de que el cliente tenga conocimiento preconstruido de cómo navegar la API, el servidor le proporciona enlaces dinámicamente que le indican qué puede hacer a continuación.

## Ejemplo Sencillo sin Hipermedia

Una API tradicional devolvería solo datos:

```json
{
  "id": 123,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "precio": 25.99,
  "disponible": true
}
```

El cliente necesitaría saber de antemano cómo:
- Obtener más información del autor
- Ver libros relacionados
- Comprar el libro
- Etc.

## Ejemplo Sencillo con Hipermedia

Una API con hipermedia incluye enlaces:

```json
{
  "id": 123,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "precio": 25.99,
  "disponible": true,
  "_links": {
    "self": {
      "href": "https://api.libreria.com/libros/123"
    },
    "autor": {
      "href": "https://api.libreria.com/autores/456"
    },
    "relacionados": {
      "href": "https://api.libreria.com/libros/123/relacionados"
    },
    "comprar": {
      "href": "https://api.libreria.com/carrito/items",
      "method": "POST"
    },
    "reviews": {
      "href": "https://api.libreria.com/libros/123/reviews"
    }
  }
}
```

## Beneficios de Hipermedia

### 1. Desacoplamiento
El cliente no necesita conocer la estructura de URIs de antemano. El servidor controla la navegación.

### 2. Evolución de la API
Puedes cambiar URIs sin romper clientes existentes, siempre que mantengas los nombres de relaciones.

### 3. Descubribilidad
Los clientes pueden descubrir funcionalidades disponibles dinámicamente.

### 4. Estado de la Aplicación
Los enlaces pueden cambiar según el estado actual del recurso.

## Hipermedia Condicional

Los enlaces disponibles pueden depender del estado del recurso:

### Libro Disponible
```json
{
  "id": 123,
  "titulo": "Don Quijote",
  "disponible": true,
  "_links": {
    "self": {"href": "/libros/123"},
    "comprar": {"href": "/carrito/items", "method": "POST"},
    "reservar": {"href": "/reservas", "method": "POST"}
  }
}
```

### Libro No Disponible
```json
{
  "id": 123,
  "titulo": "Don Quijote",
  "disponible": false,
  "_links": {
    "self": {"href": "/libros/123"},
    "notificar-disponibilidad": {"href": "/notificaciones", "method": "POST"}
  }
}
```

Observa cómo los enlaces cambian según la disponibilidad.

## Formatos de Hipermedia

Existen varios estándares para representar hipermedia:

### HAL (Hypertext Application Language)

Uno de los formatos más populares:

```json
{
  "id": 789,
  "nombre": "Juan Pérez",
  "_links": {
    "self": {"href": "/usuarios/789"},
    "pedidos": {"href": "/usuarios/789/pedidos"}
  },
  "_embedded": {
    "direccion": {
      "calle": "Gran Vía 123",
      "ciudad": "Madrid",
      "_links": {
        "self": {"href": "/direcciones/45"}
      }
    }
  }
}
```

### JSON:API

Otro estándar con soporte de hipermedia:

```json
{
  "data": {
    "type": "libros",
    "id": "123",
    "attributes": {
      "titulo": "El Quijote"
    },
    "relationships": {
      "autor": {
        "links": {
          "related": "/libros/123/autor"
        }
      }
    }
  },
  "links": {
    "self": "/libros/123"
  }
}
```

## Tipos de Enlaces

### self
Enlace al recurso actual:
```json
"_links": {
  "self": {"href": "/productos/123"}
}
```

### related
Enlaces a recursos relacionados:
```json
"_links": {
  "categoria": {"href": "/categorias/10"},
  "fabricante": {"href": "/fabricantes/5"}
}
```

### action
Enlaces a acciones disponibles:
```json
"_links": {
  "editar": {"href": "/productos/123", "method": "PUT"},
  "eliminar": {"href": "/productos/123", "method": "DELETE"}
}
```

### collection
Enlace a la colección padre:
```json
"_links": {
  "collection": {"href": "/productos"}
}
```

## Ejemplo Práctico: Sistema de Pedidos

### Estado: Pedido Recién Creado
```json
{
  "id": 1001,
  "fecha": "2024-11-17",
  "total": 150.00,
  "estado": "pendiente",
  "_links": {
    "self": {"href": "/pedidos/1001"},
    "pagar": {"href": "/pagos", "method": "POST"},
    "cancelar": {"href": "/pedidos/1001", "method": "DELETE"},
    "items": {"href": "/pedidos/1001/items"}
  }
}
```

### Estado: Pedido Pagado
```json
{
  "id": 1001,
  "fecha": "2024-11-17",
  "total": 150.00,
  "estado": "pagado",
  "_links": {
    "self": {"href": "/pedidos/1001"},
    "factura": {"href": "/pedidos/1001/factura"},
    "seguimiento": {"href": "/pedidos/1001/seguimiento"},
    "items": {"href": "/pedidos/1001/items"}
  }
}
```

Observa cómo las acciones disponibles cambian según el estado.

## Navegación con Hipermedia

El cliente puede "navegar" la API siguiendo enlaces:

1. **Obtener usuario**:
   - GET `/usuarios/123`
   - Respuesta incluye enlace a pedidos

2. **Seguir enlace a pedidos**:
   - GET `/usuarios/123/pedidos`
   - Respuesta incluye enlaces a cada pedido individual

3. **Seguir enlace a pedido específico**:
   - GET `/pedidos/1001`
   - Respuesta incluye enlaces a acciones disponibles

## Niveles de Madurez Richardson

Leonard Richardson definió 4 niveles de madurez para APIs REST:

- **Nivel 0**: Una sola URI, un método (SOAP-like)
- **Nivel 1**: Múltiples URIs, recursos
- **Nivel 2**: Uso correcto de métodos HTTP
- **Nivel 3**: **Hipermedia** (REST verdadero)

La mayoría de las "APIs REST" están en nivel 2. El nivel 3 requiere hipermedia.

## Desafíos de Implementar Hipermedia

### Complejidad Inicial
Requiere más trabajo de diseño y implementación.

### Clientes Más Complejos
Los clientes deben ser capaces de interpretar y seguir enlaces.

### Adopción Limitada
Muchas APIs populares no implementan hipermedia completa.

### Beneficio a Largo Plazo
A pesar de los desafíos iniciales, hipermedia proporciona flexibilidad y evolucionabilidad.

## Cuándo Usar Hipermedia

**Usa hipermedia cuando**:
- La API tendrá muchos clientes diversos
- Necesitas flexibilidad para cambiar URIs
- Quieres crear una API verdaderamente RESTful
- Los workflows son complejos y dinámicos

**Puedes omitir hipermedia cuando**:
- Tienes control total sobre clientes y servidor
- La API es muy simple
- El rendimiento es crítico (hipermedia añade overhead)

## Buenas Prácticas

1. **Usa nombres de relación estándar** cuando sea posible (self, collection, etc.)
2. **Incluye el método HTTP** cuando el enlace no sea GET
3. **Proporciona enlaces dinámicamente** según el estado del recurso
4. **Documenta los tipos de relaciones** disponibles en tu API
5. **Considera usar un formato estándar** como HAL o JSON:API

## Referencias

1. **Fielding, R. T. (2008)**. "REST APIs must be hypertext-driven".  
   Disponible en: https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven

2. **Kelly, M. (2013)**. "JSON Hypertext Application Language" (HAL Specification).  
   Disponible en: https://stateless.group/hal_specification.html

3. **Richardson, L., Amundsen, M., & Ruby, S. (2013)**. "RESTful Web APIs". O'Reilly Media.

4. **JSON:API Specification**.  
   Disponible en: https://jsonapi.org/

5. **Amundsen, M. (2020)**. "Design and Build Great Web APIs". Pragmatic Bookshelf.

6. **Mozilla Developer Network**. "HATEOAS".  
   Disponible en: https://developer.mozilla.org/en-US/docs/Glossary/HATEOAS

---

**Nota**: Hipermedia es lo que distingue una verdadera API RESTful de una simple API HTTP. Aunque requiere más esfuerzo inicial, proporciona flexibilidad y desacoplamiento a largo plazo.
