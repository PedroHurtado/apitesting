# 1.6 HATEOAS

## Introducción

HATEOAS (Hypermedia As The Engine Of Application State) es la restricción final y más avanzada de REST, definida por Roy Fielding. Representa el nivel más alto de madurez en el diseño de APIs RESTful y es lo que distingue una verdadera API REST de una simple API HTTP.

## ¿Qué es HATEOAS?

HATEOAS es un principio que establece que las interacciones del cliente con la aplicación deben ser guiadas enteramente por hipermedia proporcionada dinámicamente por el servidor. En otras palabras, el cliente no necesita conocimiento previo de cómo navegar o interactuar con la aplicación más allá del punto de entrada inicial.

## Concepto Fundamental

La idea central de HATEOAS es similar a cómo navegas en la web:

- Entras a una página web (punto de entrada)
- La página contiene enlaces que te muestran a dónde puedes ir
- Haces clic en enlaces sin conocer las URLs de antemano
- La navegación es guiada por los enlaces que ves

## Ejemplo sin HATEOAS

En una API tradicional, el cliente necesita saber todo de antemano:

```json
GET /api/cuentas/12345

{
  "numero_cuenta": "12345",
  "saldo": 1500.00,
  "titular": "Juan Pérez",
  "estado": "activa"
}
```

El cliente debe tener "cableado" en su código:
- "Para transferir dinero, hago POST a /api/transferencias"
- "Para ver movimientos, hago GET a /api/cuentas/12345/movimientos"
- "Para cerrar cuenta, hago DELETE a /api/cuentas/12345"

## Ejemplo con HATEOAS

Con HATEOAS, el servidor guía al cliente:

```json
GET /api/cuentas/12345

{
  "numero_cuenta": "12345",
  "saldo": 1500.00,
  "titular": "Juan Pérez",
  "estado": "activa",
  "_links": {
    "self": {
      "href": "/api/cuentas/12345"
    },
    "transferir": {
      "href": "/api/transferencias",
      "method": "POST",
      "title": "Realizar transferencia"
    },
    "movimientos": {
      "href": "/api/cuentas/12345/movimientos",
      "method": "GET",
      "title": "Ver historial de movimientos"
    },
    "depositar": {
      "href": "/api/cuentas/12345/depositos",
      "method": "POST",
      "title": "Realizar depósito"
    },
    "cerrar": {
      "href": "/api/cuentas/12345",
      "method": "DELETE",
      "title": "Cerrar cuenta"
    }
  }
}
```

## Estados de la Aplicación

"Application State" en HATEOAS se refiere al estado del flujo de trabajo del cliente, no al estado de los recursos en el servidor. Los enlaces disponibles representan las transiciones de estado posibles.

### Ejemplo: Proceso de Pedido

**Estado 1: Carrito Vacío**
```json
{
  "carrito_id": "abc123",
  "items": [],
  "total": 0,
  "_links": {
    "self": {"href": "/carritos/abc123"},
    "añadir-producto": {
      "href": "/carritos/abc123/items",
      "method": "POST"
    },
    "buscar-productos": {
      "href": "/productos"
    }
  }
}
```

**Estado 2: Carrito con Items**
```json
{
  "carrito_id": "abc123",
  "items": [
    {"producto": "Libro", "cantidad": 2, "precio": 25.00}
  ],
  "total": 50.00,
  "_links": {
    "self": {"href": "/carritos/abc123"},
    "añadir-producto": {"href": "/carritos/abc123/items", "method": "POST"},
    "modificar-item": {"href": "/carritos/abc123/items/{id}", "method": "PATCH"},
    "eliminar-item": {"href": "/carritos/abc123/items/{id}", "method": "DELETE"},
    "proceder-pago": {"href": "/checkout", "method": "POST"},
    "vaciar": {"href": "/carritos/abc123", "method": "DELETE"}
  }
}
```

**Estado 3: Pedido Confirmado**
```json
{
  "pedido_id": "ord-789",
  "estado": "confirmado",
  "total": 50.00,
  "_links": {
    "self": {"href": "/pedidos/ord-789"},
    "factura": {"href": "/pedidos/ord-789/factura", "method": "GET"},
    "seguimiento": {"href": "/pedidos/ord-789/seguimiento", "method": "GET"},
    "cancelar": {"href": "/pedidos/ord-789", "method": "DELETE"}
  }
}
```

**Estado 4: Pedido Enviado**
```json
{
  "pedido_id": "ord-789",
  "estado": "enviado",
  "numero_seguimiento": "TRACK12345",
  "_links": {
    "self": {"href": "/pedidos/ord-789"},
    "factura": {"href": "/pedidos/ord-789/factura"},
    "seguimiento": {"href": "/pedidos/ord-789/seguimiento"},
    "soporte": {"href": "/soporte/contacto"}
  }
}
```

Observa cómo los enlaces cambian según el estado del pedido.

## Beneficios de HATEOAS

### 1. Desacoplamiento Total
El cliente no necesita conocimiento previo de URIs. Todo se descubre dinámicamente.

### 2. Evolucionabilidad
Puedes cambiar URIs, añadir nuevas operaciones o modificar workflows sin romper clientes.

### 3. Auto-documentación
Los enlaces actúan como documentación en tiempo real de qué se puede hacer.

### 4. Lógica de Negocio en el Servidor
Las reglas de negocio (qué operaciones están disponibles) se centralizan en el servidor.

### 5. Clientes Más Robustos
Los clientes siguen enlaces en lugar de construir URIs, haciéndolos más resilientes a cambios.

## Control de Flujo con HATEOAS

### Ejemplo: Sistema de Préstamos Bancarios

**Solicitud Nueva**
```json
{
  "solicitud_id": "loan-123",
  "monto": 50000,
  "estado": "borrador",
  "_links": {
    "self": {"href": "/prestamos/loan-123"},
    "editar": {"href": "/prestamos/loan-123", "method": "PATCH"},
    "enviar": {"href": "/prestamos/loan-123/enviar", "method": "POST"},
    "eliminar": {"href": "/prestamos/loan-123", "method": "DELETE"}
  }
}
```

**Solicitud Enviada (En Revisión)**
```json
{
  "solicitud_id": "loan-123",
  "monto": 50000,
  "estado": "en_revision",
  "_links": {
    "self": {"href": "/prestamos/loan-123"},
    "estado": {"href": "/prestamos/loan-123/estado"},
    "documentos": {"href": "/prestamos/loan-123/documentos"}
  }
}
```

El cliente no puede editar o eliminar porque esos enlaces ya no están presentes.

**Solicitud Aprobada**
```json
{
  "solicitud_id": "loan-123",
  "monto": 50000,
  "estado": "aprobado",
  "_links": {
    "self": {"href": "/prestamos/loan-123"},
    "aceptar": {"href": "/prestamos/loan-123/aceptar", "method": "POST"},
    "rechazar": {"href": "/prestamos/loan-123/rechazar", "method": "POST"},
    "contrato": {"href": "/prestamos/loan-123/contrato", "method": "GET"}
  }
}
```

## Implementación Práctica

### Formato HAL (Hypertext Application Language)

HAL es uno de los formatos más utilizados para HATEOAS:

```json
{
  "id": 456,
  "nombre": "María García",
  "email": "maria@ejemplo.com",
  "_links": {
    "self": {
      "href": "/usuarios/456"
    },
    "pedidos": {
      "href": "/usuarios/456/pedidos"
    },
    "direcciones": {
      "href": "/usuarios/456/direcciones"
    }
  },
  "_embedded": {
    "direccion_principal": {
      "calle": "Calle Mayor 10",
      "ciudad": "Madrid",
      "_links": {
        "self": {"href": "/direcciones/78"}
      }
    }
  }
}
```

### Tipos de Relaciones (Link Relations)

Usa nombres estándar cuando sea posible:

- **self**: El recurso actual
- **next**: Siguiente recurso en una secuencia
- **prev**: Recurso anterior
- **first**: Primer recurso de una colección
- **last**: Último recurso
- **collection**: Colección padre
- **item**: Elemento de una colección

### Relaciones Personalizadas

```json
{
  "_links": {
    "self": {"href": "/productos/123"},
    "http://api.ejemplo.com/rels/fabricante": {
      "href": "/fabricantes/45"
    },
    "http://api.ejemplo.com/rels/reseñas": {
      "href": "/productos/123/reseñas"
    }
  }
}
```

## Desafíos de HATEOAS

### 1. Complejidad
Implementar HATEOAS correctamente requiere más trabajo de diseño.

### 2. Overhead
Los enlaces añaden tamaño a las respuestas.

### 3. Adopción Limitada
Muchas APIs "REST" no implementan HATEOAS (nivel 2 de Richardson).

### 4. Curva de Aprendizaje
Tanto desarrolladores de servidor como de cliente necesitan entender el concepto.

### 5. Herramientas
No todas las herramientas y frameworks soportan HATEOAS fácilmente.

## Cuándo Usar HATEOAS

**Usa HATEOAS cuando**:
- Necesitas máxima flexibilidad y evolucionabilidad
- Tienes workflows complejos con múltiples estados
- Quieres APIs verdaderamente RESTful
- Los clientes son diversos y no controlas todos

**Puedes omitir HATEOAS cuando**:
- Controlas tanto cliente como servidor
- La API es muy simple
- El rendimiento es crítico
- El equipo no tiene experiencia con HATEOAS

## Nivel de Madurez Richardson

HATEOAS representa el **Nivel 3** (el más alto):

- **Nivel 0**: Un endpoint, todo vía POST (RPC)
- **Nivel 1**: Múltiples recursos con URIs
- **Nivel 2**: Uso correcto de métodos HTTP
- **Nivel 3**: HATEOAS - Hipermedia controla el estado

## Ejemplo Completo: API de E-learning

```json
GET /api/cursos/curso-rest-101

{
  "id": "curso-rest-101",
  "titulo": "Introducción a REST",
  "progreso": 45,
  "leccion_actual": 3,
  "_links": {
    "self": {"href": "/cursos/curso-rest-101"},
    "continuar": {
      "href": "/cursos/curso-rest-101/lecciones/4",
      "title": "Continuar con Lección 4"
    },
    "leccion-anterior": {
      "href": "/cursos/curso-rest-101/lecciones/3"
    },
    "inicio-curso": {
      "href": "/cursos/curso-rest-101/lecciones/1"
    },
    "certificado": {
      "href": "/cursos/curso-rest-101/certificado",
      "available": false,
      "title": "Disponible al completar 100%"
    },
    "foro": {
      "href": "/cursos/curso-rest-101/foro"
    }
  }
}
```

## Buenas Prácticas

1. **Proporciona enlaces significativos** con títulos descriptivos
2. **Incluye el método HTTP** cuando no sea GET
3. **Usa relaciones estándar** cuando existan
4. **Indica disponibilidad** de acciones (available: true/false)
5. **Documenta tus tipos de relaciones** personalizadas
6. **Mantén consistencia** en toda la API
7. **Considera el overhead** vs beneficios

## Referencias

1. **Fielding, R. T. (2000)**. "Architectural Styles and the Design of Network-based Software Architectures" (Capítulo 5 sobre REST).  
   Disponible en: https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm

2. **Fielding, R. T. (2008)**. "REST APIs must be hypertext-driven".  
   Disponible en: https://roy.gbiv.com/untangled/2008/rest-apis-must-be-hypertext-driven

3. **Kelly, M. (2013)**. "JSON Hypertext Application Language (HAL)".  
   Disponible en: https://stateless.group/hal_specification.html

4. **Richardson, L., & Amundsen, M. (2013)**. "RESTful Web APIs: Services for a Changing World". O'Reilly Media.

5. **RFC 8288 - Web Linking**.  
   Disponible en: https://www.rfc-editor.org/rfc/rfc8288

6. **Spring HATEOAS Documentation**.  
   Disponible en: https://spring.io/projects/spring-hateoas

---

**Nota**: HATEOAS es el ideal de REST, pero es el nivel menos implementado en la práctica. Sin embargo, conocerlo te permite tomar decisiones informadas sobre cuándo vale la pena el esfuerzo adicional y cuándo un enfoque más simple es suficiente.
