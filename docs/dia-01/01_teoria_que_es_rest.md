# 1.1 ¿Qué es REST?

## Introducción

REST (Representational State Transfer) es un estilo arquitectónico para diseñar servicios web que fue definido por Roy Fielding en su tesis doctoral en el año 2000. No es un protocolo ni un estándar, sino un conjunto de principios y restricciones arquitectónicas que, cuando se aplican correctamente, crean servicios web escalables, mantenibles y eficientes.

## Concepto Fundamental

REST se basa en la idea de **recursos** y **representaciones**. Un recurso es cualquier entidad que puede ser identificada mediante una URI (Uniform Resource Identifier), como un usuario, un producto, un documento, etc. La representación es la forma en que se presenta ese recurso al cliente (generalmente en formato JSON o XML).

## Principios Clave de REST

REST se fundamenta en seis restricciones arquitectónicas principales:

### 1. Cliente-Servidor
La separación entre cliente y servidor permite que ambos evolucionen de forma independiente. El cliente se encarga de la interfaz de usuario y el servidor gestiona los datos y la lógica de negocio.

### 2. Sin Estado (Stateless)
Cada petición del cliente al servidor debe contener toda la información necesaria para ser entendida. El servidor no almacena el estado de las sesiones de los clientes entre peticiones.

### 3. Cacheable
Las respuestas deben definir explícitamente si pueden ser almacenadas en caché o no, para mejorar el rendimiento y la escalabilidad.

### 4. Sistema de Capas
La arquitectura puede estar compuesta por capas jerárquicas, donde cada componente no puede ver más allá de la capa con la que interactúa directamente.

### 5. Interfaz Uniforme
Todos los recursos se acceden mediante una interfaz genérica y uniforme, lo que simplifica la arquitectura y mejora la visibilidad de las interacciones.

### 6. Código bajo Demanda (Opcional)
Los servidores pueden extender temporalmente la funcionalidad del cliente transfiriendo código ejecutable (por ejemplo, JavaScript).

## Ejemplo Sencillo

Imaginemos una tienda online que gestiona productos:

**Recurso**: Un producto con ID 123

**URI del recurso**: `https://api.tienda.com/productos/123`

**Representación JSON del recurso**:
```json
{
  "id": 123,
  "nombre": "Laptop HP",
  "precio": 699.99,
  "stock": 15
}
```

Cuando un cliente hace una petición GET a la URI, el servidor responde con la representación actual del recurso (el producto). Si el cliente quiere actualizar el precio, enviaría una petición PUT con la nueva representación.

## Ventajas de REST

- **Escalabilidad**: Gracias a su naturaleza sin estado y capacidad de caché
- **Flexibilidad**: Separación entre cliente y servidor
- **Simplicidad**: Uso del protocolo HTTP estándar
- **Independencia**: Cliente y servidor pueden evolucionar independientemente
- **Visibilidad**: Las operaciones son fáciles de monitorizar y depurar

## REST vs SOAP

A diferencia de SOAP (que es un protocolo con especificaciones estrictas), REST es más flexible y ligero. REST utiliza los métodos HTTP estándar (GET, POST, PUT, DELETE), mientras que SOAP utiliza principalmente POST y requiere más overhead en forma de XML.

## Referencias

1. **Fielding, R. T. (2000)**. "Architectural Styles and the Design of Network-based Software Architectures" (Tesis Doctoral). Universidad de California, Irvine.  
   Disponible en: https://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm

2. **Richardson, L., & Ruby, S. (2007)**. "RESTful Web Services". O'Reilly Media.

3. **Mozilla Developer Network (MDN)**. "HTTP Methods".  
   Disponible en: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods

4. **REST API Tutorial**. "What is REST".  
   Disponible en: https://restfulapi.net/

5. **Microsoft Azure Architecture Center**. "RESTful web API design".  
   Disponible en: https://docs.microsoft.com/en-us/azure/architecture/best-practices/api-design

---

**Nota**: REST es la base de la mayoría de las APIs modernas. Comprender sus principios es fundamental para diseñar servicios web robustos y escalables.
