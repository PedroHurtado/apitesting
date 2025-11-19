# Tests de Postman para UpdatePizza

## 📋 Descripción

Esta colección contiene **12 tests automatizados** para el endpoint `PUT /pizzas/{id}` que actualiza pizzas existentes.

## 🎯 Cobertura de Tests

### ✅ Casos de Éxito
- **Test 01**: Actualización exitosa (200 OK)

### ❌ Validaciones de Campo - Name
- **Test 02**: Name requerido (vacío)
- **Test 03**: Name máximo 100 caracteres

### ❌ Validaciones de Campo - Description
- **Test 04**: Description requerida (vacía)
- **Test 05**: Description máximo 250 caracteres

### ❌ Validaciones de Campo - Url
- **Test 06**: Url requerida (vacía)
- **Test 07**: Url formato inválido
- **Test 08**: Url debe ser http/https (no ftp, file, etc)
- **Test 09**: Url máximo 500 caracteres

### ❌ Errores de Entidad
- **Test 10**: Pizza no encontrada (404)
- **Test 11**: GUID con formato inválido

### 🔥 Casos Complejos
- **Test 12**: Múltiples errores de validación simultáneos

## 🚀 Configuración

### 1. Importar la Colección

1. Abre Postman
2. Click en **Import**
3. Selecciona el archivo `UpdatePizza_Tests.postman_collection.json`
4. Click en **Import**

### 2. Configurar Variables

La colección usa dos variables que debes configurar:

#### Variables de Colección:

| Variable | Valor por Defecto | Descripción |
|----------|-------------------|-------------|
| `baseUrl` | `https://localhost:7000` | URL base de tu API |
| `existingPizzaId` | `00000000-0000-0000-0000-000000000001` | ID de una pizza existente en tu BD |

#### Cómo Cambiar las Variables:

**Opción A: Editar Variables de Colección**
1. Click derecho en la colección → **Edit**
2. Ve a la pestaña **Variables**
3. Actualiza los valores:
   - `baseUrl`: Cambia a tu dominio (ej: `https://api.mipizzeria.com`)
   - `existingPizzaId`: Pon el GUID de una pizza real de tu base de datos
4. Click **Save**

**Opción B: Crear un Environment**
1. Click en el icono de ⚙️ (arriba derecha)
2. Click en **Add** para crear nuevo entorno
3. Añade las variables:
   ```
   baseUrl: tu-dominio
   existingPizzaId: tu-guid-real
   ```
4. Selecciona el entorno creado en el dropdown

### 3. Preparar los Datos

**⚠️ IMPORTANTE**: Para que el **Test 01** (actualización exitosa) funcione, necesitas:

1. Tener una pizza creada en tu base de datos
2. Copiar su GUID
3. Ponerlo en la variable `existingPizzaId`

**Ejemplo de cómo obtener un GUID válido:**

```bash
# Opción 1: Crear una pizza primero (endpoint POST)
POST /pizzas
{
  "name": "Pizza Test",
  "description": "Pizza para testing",
  "url": "https://example.com/test.jpg"
}

# Copiar el ID de la respuesta y usarlo como existingPizzaId
```

## ▶️ Ejecutar los Tests

### Ejecución Individual

1. Abre la colección
2. Click en cualquier request
3. Click en **Send**
4. Revisa los resultados en la pestaña **Test Results**

### Ejecución Completa (Collection Runner)

1. Click derecho en la colección
2. Selecciona **Run collection**
3. Asegúrate de que todos los tests están seleccionados
4. Click en **Run Update Pizza - Tests**
5. Verás los resultados de los 12 tests:
   - ✅ Tests pasados
   - ❌ Tests fallidos
   - Tiempos de respuesta

## 📊 Resultados Esperados

### Ejecución Exitosa

Cuando todo está bien configurado:

```
✅ 01 - Update Pizza - Success (3 tests)
✅ 02 - Update Pizza - Name Required (3 tests)
✅ 03 - Update Pizza - Name Max Length (3 tests)
✅ 04 - Update Pizza - Description Required (3 tests)
✅ 05 - Update Pizza - Description Max Length (3 tests)
✅ 06 - Update Pizza - Url Required (3 tests)
✅ 07 - Update Pizza - Invalid Url Format (3 tests)
✅ 08 - Update Pizza - Url Without Http Scheme (3 tests)
✅ 09 - Update Pizza - Url Max Length (3 tests)
✅ 10 - Update Pizza - Not Found (2 tests)
✅ 11 - Update Pizza - Invalid GUID Format (2 tests)
✅ 12 - Update Pizza - Multiple Validation Errors (2 tests)

Total: 31 assertions passed
```

## 🔍 Detalles de las Validaciones

### Reglas del Modelo Pizza

Según el código del dominio:

```csharp
Name:
  - ✅ Requerido (NotEmpty)
  - ✅ Máximo 100 caracteres

Description:
  - ✅ Requerida (NotEmpty)
  - ✅ Máximo 250 caracteres

Url:
  - ✅ Requerida (NotEmpty)
  - ✅ Máximo 500 caracteres
  - ✅ Formato válido (http/https)
```

### Códigos HTTP Esperados

- **200 OK**: Actualización exitosa
- **404 Not Found**: Pizza no existe
- **422 Unprocessable Entity**: Error de validación
- **400 Bad Request**: GUID inválido

## 🐛 Troubleshooting

### Test 01 falla con 404

**Problema**: La pizza con el ID especificado no existe

**Solución**: 
1. Crea una pizza usando el endpoint POST
2. Actualiza `existingPizzaId` con el ID real
3. O consulta tu base de datos para obtener un GUID válido

### Todos los tests fallan con error de conexión

**Problema**: No se puede conectar al servidor

**Solución**:
1. Verifica que tu API está corriendo
2. Revisa que `baseUrl` esté correcto
3. Si usas HTTPS local, acepta el certificado en el navegador primero

### Tests de validación pasan cuando deberían fallar

**Problema**: Las validaciones no están funcionando correctamente

**Solución**:
1. Verifica que FluentValidation esté configurado en tu API
2. Revisa que el middleware de validación esté activo
3. Asegúrate de que `SuccessOrThrow()` está lanzando excepciones correctamente

### Tests fallan con error 500

**Problema**: Error interno del servidor

**Solución**:
1. Revisa los logs de tu aplicación
2. Verifica que la base de datos esté accesible
3. Confirma que todas las dependencias estén correctamente inyectadas

## 📝 Notas Adicionales

- **Todos los tests son idempotentes**: Puedes ejecutarlos múltiples veces
- **Tests 02-12 no requieren datos específicos**: Usan datos de prueba
- **Test 01 sí modifica datos**: Actualiza la pizza existente
- **Test 10 usa un GUID inexistente**: No afecta tus datos
- **Ordenados por complejidad**: Primero éxito, luego validaciones, luego errores

## 🎓 Uso en Curso

Esta colección es ideal para:

- ✅ Demostrar testing de APIs
- ✅ Validar comportamiento de endpoints
- ✅ Enseñar buenas prácticas de validación
- ✅ Documentar casos de uso y edge cases
- ✅ Automatizar pruebas de regresión

## 📞 Contacto

Si tienes preguntas sobre los tests, revisa la documentación del endpoint o consulta el código fuente del comando `UpdatePizza`.
