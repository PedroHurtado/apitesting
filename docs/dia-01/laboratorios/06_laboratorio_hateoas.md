# Laboratorio 1.6: HATEOAS

## Objetivo
Comprender y aplicar el principio HATEOAS (Hypermedia As The Engine Of Application State), diseñando APIs que guían al cliente mediante enlaces hipermedia y experimentando con transiciones de estado.

## Duración Estimada
60 minutos

## Requisitos Previos
- Postman instalado
- Comprensión de hipermedia (Laboratorio 1.4)
- Conocimientos de servicios CRUD
- Conexión a Internet

## Preparación

Crea una nueva colección en Postman llamada "Laboratorio HATEOAS - Transiciones de Estado".

## Ejercicio 1: Comparar Niveles de Madurez

### Actividad 1.1: API Nivel 2 (Sin HATEOAS)

**Objetivo**: Observar una API tradicional sin guía hipermedia.

1. **Obtener un usuario**:
   - Método: GET
   - URL: `https://jsonplaceholder.typicode.com/users/1`

2. **Analizar respuesta**:
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "Bret",
  "email": "Sincere@april.biz",
  "address": {...},
  "phone": "1-770-736-8031",
  "website": "hildegard.org"
}
```

3. **Observa**:
   - No hay enlaces a acciones disponibles
   - No hay guía sobre qué puedes hacer con este usuario
   - Debes saber que existe `/users/1/posts` por documentación

**Pregunta**: ¿Cómo sabrías que puedes ver los posts de este usuario?

### Actividad 1.2: Simular API Nivel 3 (Con HATEOAS)

**Objetivo**: Diseñar cómo debería ser la misma respuesta con HATEOAS.

1. **Crear petición documentada**:
   - Nombre: "Usuario con HATEOAS"
   - Método: GET
   - URL: `https://api-hateoas.ejemplo.com/users/1` (ficticia)

2. **En Examples, documentar respuesta ideal**:
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "Bret",
  "email": "Sincere@april.biz",
  "phone": "1-770-736-8031",
  "_links": {
    "self": {
      "href": "/users/1",
      "method": "GET"
    },
    "posts": {
      "href": "/users/1/posts",
      "method": "GET",
      "title": "Ver posts del usuario"
    },
    "albums": {
      "href": "/users/1/albums",
      "method": "GET",
      "title": "Ver álbumes del usuario"
    },
    "todos": {
      "href": "/users/1/todos",
      "method": "GET",
      "title": "Ver tareas del usuario"
    },
    "edit": {
      "href": "/users/1",
      "method": "PUT",
      "title": "Editar usuario"
    },
    "delete": {
      "href": "/users/1",
      "method": "DELETE",
      "title": "Eliminar usuario"
    }
  }
}
```

**Análisis**:
- Cada enlace muestra una acción disponible
- El cliente descubre funcionalidades sin documentación
- Los enlaces guían la navegación

## Ejercicio 2: Transiciones de Estado - Sistema de Pedidos

### Actividad 2.1: Estado 1 - Carrito Vacío

**Objetivo**: Diseñar enlaces para un carrito sin items.

1. **Crear petición**: "Carrito Vacío"
2. **Documentar en Examples**:

```json
{
  "carrito_id": "cart-abc123",
  "usuario_id": 1,
  "items": [],
  "total": 0.00,
  "estado": "vacio",
  "_links": {
    "self": {
      "href": "/carritos/cart-abc123"
    },
    "productos": {
      "href": "/productos",
      "method": "GET",
      "title": "Explorar productos"
    },
    "añadir-item": {
      "href": "/carritos/cart-abc123/items",
      "method": "POST",
      "title": "Añadir producto al carrito"
    },
    "ofertas": {
      "href": "/productos?ofertas=true",
      "method": "GET",
      "title": "Ver productos en oferta"
    }
  }
}
```

**Observa**: Solo están disponibles acciones para añadir productos. No hay "proceder al pago" porque el carrito está vacío.

### Actividad 2.2: Estado 2 - Carrito con Items

**Objetivo**: Mostrar cómo cambian los enlaces cuando hay productos.

1. **Crear petición**: "Carrito con Productos"
2. **Documentar respuesta**:

```json
{
  "carrito_id": "cart-abc123",
  "usuario_id": 1,
  "items": [
    {
      "id": "item-1",
      "producto_id": 101,
      "nombre": "Laptop Dell",
      "cantidad": 1,
      "precio_unitario": 899.99,
      "subtotal": 899.99
    },
    {
      "id": "item-2",
      "producto_id": 205,
      "nombre": "Mouse Logitech",
      "cantidad": 2,
      "precio_unitario": 29.99,
      "subtotal": 59.98
    }
  ],
  "total": 959.97,
  "estado": "activo",
  "_links": {
    "self": {"href": "/carritos/cart-abc123"},
    "productos": {"href": "/productos", "title": "Seguir comprando"},
    "añadir-item": {"href": "/carritos/cart-abc123/items", "method": "POST"},
    "modificar-item": {
      "href": "/carritos/cart-abc123/items/{item_id}",
      "method": "PATCH",
      "title": "Modificar cantidad de un item"
    },
    "eliminar-item": {
      "href": "/carritos/cart-abc123/items/{item_id}",
      "method": "DELETE",
      "title": "Eliminar item del carrito"
    },
    "aplicar-cupon": {
      "href": "/carritos/cart-abc123/cupon",
      "method": "POST",
      "title": "Aplicar código de descuento"
    },
    "checkout": {
      "href": "/checkout",
      "method": "POST",
      "title": "Proceder al pago"
    },
    "vaciar": {
      "href": "/carritos/cart-abc123",
      "method": "DELETE",
      "title": "Vaciar carrito"
    }
  }
}
```

**Observa**: 
- Ahora hay enlace "checkout" (antes no)
- Hay opciones para modificar/eliminar items
- Más acciones disponibles

**Pregunta**: ¿Por qué el enlace "checkout" solo aparece cuando hay items?

### Actividad 2.3: Estado 3 - Pedido en Proceso de Pago

**Objetivo**: Mostrar transición a otro estado.

1. **Crear petición**: "Pedido - Esperando Pago"
2. **Documentar respuesta**:

```json
{
  "pedido_id": "order-789",
  "usuario_id": 1,
  "total": 959.97,
  "estado": "pendiente_pago",
  "fecha_creacion": "2024-11-17T10:30:00Z",
  "_links": {
    "self": {"href": "/pedidos/order-789"},
    "pagar-tarjeta": {
      "href": "/pagos",
      "method": "POST",
      "title": "Pagar con tarjeta de crédito",
      "body_schema": {
        "pedido_id": "string",
        "metodo": "tarjeta",
        "tarjeta": {...}
      }
    },
    "pagar-paypal": {
      "href": "/pagos/paypal",
      "method": "POST",
      "title": "Pagar con PayPal"
    },
    "pagar-transferencia": {
      "href": "/pagos/transferencia",
      "method": "POST",
      "title": "Pagar por transferencia bancaria"
    },
    "cancelar": {
      "href": "/pedidos/order-789",
      "method": "DELETE",
      "title": "Cancelar pedido"
    },
    "items": {
      "href": "/pedidos/order-789/items",
      "method": "GET",
      "title": "Ver items del pedido"
    }
  }
}
```

**Observa**:
- El carrito se transformó en pedido
- Ahora solo puedes pagar o cancelar
- No puedes modificar items (el pedido está "congelado")

### Actividad 2.4: Estado 4 - Pedido Pagado

1. **Crear petición**: "Pedido Pagado"
2. **Documentar respuesta**:

```json
{
  "pedido_id": "order-789",
  "usuario_id": 1,
  "total": 959.97,
  "estado": "pagado",
  "fecha_pago": "2024-11-17T10:45:00Z",
  "metodo_pago": "tarjeta",
  "_links": {
    "self": {"href": "/pedidos/order-789"},
    "factura": {
      "href": "/pedidos/order-789/factura",
      "method": "GET",
      "title": "Descargar factura (PDF)"
    },
    "seguimiento": {
      "href": "/pedidos/order-789/seguimiento",
      "method": "GET",
      "title": "Rastrear envío"
    },
    "items": {
      "href": "/pedidos/order-789/items",
      "method": "GET",
      "title": "Ver items del pedido"
    },
    "soporte": {
      "href": "/soporte/contacto",
      "method": "POST",
      "title": "Contactar soporte"
    }
  }
}
```

**Observa**:
- Ya no puedes pagar ni cancelar
- Aparecen nuevas opciones: factura, seguimiento
- El estado determina las acciones disponibles

## Ejercicio 3: Flujo Completo de Transiciones

### Actividad 3.1: Diagrama de Estados

**Tarea**: Documenta el flujo completo de estados y transiciones.

```
[Carrito Vacío]
    |
    | POST /carritos/{id}/items (añadir producto)
    v
[Carrito con Items]
    |
    | POST /checkout
    v
[Pedido - Pendiente Pago]
    |
    | POST /pagos (pagar)
    v
[Pedido Pagado]
    |
    | (automático después de preparación)
    v
[Pedido Enviado]
    |
    | (automático al entregar)
    v
[Pedido Entregado]
```

**Pregunta**: ¿Qué enlaces estarían disponibles en "Pedido Entregado"?

### Actividad 3.2: Estado Final - Pedido Entregado

Diseña la respuesta para un pedido entregado:

```json
{
  "pedido_id": "order-789",
  "estado": "entregado",
  "fecha_entrega": "2024-11-20T14:30:00Z",
  "_links": {
    "self": {"href": "/pedidos/order-789"},
    "factura": {"href": "/pedidos/order-789/factura"},
    "dejar-reseña": {
      "href": "/pedidos/order-789/reseña",
      "method": "POST",
      "title": "Dejar reseña del producto"
    },
    "devolver": {
      "href": "/devoluciones",
      "method": "POST",
      "title": "Iniciar devolución (30 días)",
      "available_until": "2024-12-20"
    },
    "recomprar": {
      "href": "/carritos/cart-abc123/items",
      "method": "POST",
      "title": "Comprar de nuevo"
    }
  }
}
```

## Ejercicio 4: Control de Acceso con HATEOAS

### Actividad 4.1: Permisos de Usuario

**Contexto**: Un usuario regular vs un administrador ven diferentes enlaces.

**Usuario Regular**:
```json
{
  "id": 1,
  "nombre": "Juan Usuario",
  "rol": "cliente",
  "_links": {
    "self": {"href": "/usuarios/1"},
    "mis-pedidos": {"href": "/usuarios/1/pedidos"},
    "editar-perfil": {"href": "/usuarios/1", "method": "PATCH"}
  }
}
```

**Administrador**:
```json
{
  "id": 100,
  "nombre": "Admin Principal",
  "rol": "admin",
  "_links": {
    "self": {"href": "/usuarios/100"},
    "mis-pedidos": {"href": "/usuarios/100/pedidos"},
    "editar-perfil": {"href": "/usuarios/100", "method": "PATCH"},
    "todos-usuarios": {"href": "/usuarios", "title": "Gestionar usuarios"},
    "todos-pedidos": {"href": "/pedidos", "title": "Ver todos los pedidos"},
    "reportes": {"href": "/reportes", "title": "Ver reportes"},
    "configuracion": {"href": "/configuracion", "title": "Configuración del sistema"}
  }
}
```

**Observa**: Los enlaces reflejan permisos. Un usuario regular nunca ve enlaces de admin.

## Ejercicio 5: Diseña Tu Sistema con HATEOAS

### Actividad 5.1: Sistema de Biblioteca

**Contexto**: Diseña un sistema de préstamo de libros con HATEOAS.

**Estados posibles**:
1. Libro disponible
2. Libro prestado
3. Libro reservado
4. Libro en mantenimiento

**Tarea**: Diseña la respuesta para cada estado con los enlaces apropiados.

**Libro Disponible**:
```json
{
  "libro_id": 123,
  "titulo": "Cien años de soledad",
  "estado": "disponible",
  "copias_disponibles": 3,
  "_links": {
    // Añade los enlaces apropiados
    "self": {"href": "/libros/123"},
    // ¿Qué más?
  }
}
```

**Solución sugerida**:
- `prestar`: POST /prestamos
- `reservar`: POST /reservas
- `reseñas`: GET /libros/123/reseñas
- `similares`: GET /libros/123/similares

**Libro Prestado** (para el usuario que lo tiene):
```json
{
  "libro_id": 123,
  "titulo": "Cien años de soledad",
  "estado": "prestado_a_mi",
  "fecha_devolucion": "2024-11-30",
  "_links": {
    // ¿Qué enlaces corresponden?
  }
}
```

**Solución sugerida**:
- `renovar`: POST /prestamos/456/renovar
- `devolver`: DELETE /prestamos/456
- `reportar-problema`: POST /soporte/libro-dañado

## Resultados Esperados

Al finalizar este laboratorio, deberías:

1. Comprender qué significa "Application State"
2. Diseñar APIs que guían mediante enlaces
3. Modelar transiciones de estado con hipermedia
4. Entender cómo HATEOAS mejora el desacoplamiento
5. Aplicar control de acceso mediante enlaces
6. Distinguir APIs Nivel 2 vs Nivel 3

## Preguntas de Autoevaluación

1. ¿Qué significa "Application State" en HATEOAS?
2. ¿Por qué los enlaces deben cambiar según el estado del recurso?
3. ¿Cómo HATEOAS ayuda con el control de acceso?
4. ¿Qué ventaja tiene que el cliente no construya URIs?
5. ¿Cuál es la diferencia principal entre Nivel 2 y Nivel 3 de Richardson?

## Exportar y Documentar

1. **Exporta tu colección** con todos los ejemplos
2. **Crea un documento** que explique:
   - Los estados identificados
   - Las transiciones posibles
   - Los enlaces en cada estado

## Conclusión

HATEOAS representa el nivel más alto de madurez REST. Aunque añade complejidad, proporciona desacoplamiento total, auto-documentación y flexibilidad sin igual. El servidor controla completamente el flujo de la aplicación mediante enlaces dinámicos.

---

**Reflexión Final**: La mayoría de las APIs modernas no implementan HATEOAS completo. Sin embargo, entender este concepto te permite diseñar APIs más flexibles y tomar decisiones informadas sobre cuándo vale la pena la inversión adicional.
