# Guía Rápida para el Instructor - Testing con SoapUI

## 📋 Información General

- **Duración total**: 5 horas (300 minutos)
- **Formato**: 7 temas con teoría + laboratorio cada uno
- **Nivel**: Intermedio
- **Requisito previo**: Conocimientos de JavaScript, HTTP y servicios web

## ⏱️ Planificación de Tiempo

### Sesión Completa (5 horas con descanso)

```
09:00 - 09:50  Tema 1: Empezando con SoapUI (50')
09:50 - 10:30  Tema 2: Test Suites (40')
10:30 - 10:45  ☕ DESCANSO (15')
10:45 - 11:35  Tema 3: Test Cases y Steps (50')
11:35 - 12:10  Tema 4: Propiedades (35')
12:10 - 12:55  Tema 5: Aserciones (45')
12:55 - 13:00  Cierre de mañana
```

**Pausa para almuerzo o continuar:**

```
13:00 - 13:40  Tema 6: Ejecución y Depuración (40')
13:40 - 14:20  Tema 7: Tipos de Pasos (40')
14:20 - 14:30  Cierre final y Q&A
```

## 🎯 Objetivos por Tema

### Tema 1: Empezando con SoapUI
**Objetivo clave**: El alumno debe tener SoapUI instalado y funcionando, con su primer request exitoso.

**Verificación**: ¿Todos pueden ejecutar GET /users y ver respuesta?

### Tema 2: Test Suites
**Objetivo clave**: Comprender la organización jerárquica y crear su primer Test Suite.

**Verificación**: ¿Todos tienen un Test Suite con propiedades y scripts?

### Tema 3: Test Cases y Steps
**Objetivo clave**: Crear flujos de múltiples pasos con transferencia de datos.

**Verificación**: ¿Pueden extraer un ID y usarlo en el siguiente request?

### Tema 4: Propiedades
**Objetivo clave**: Dominar el uso de propiedades en diferentes niveles.

**Verificación**: ¿Entienden la expansión ${#TestCase#propiedad}?

### Tema 5: Aserciones
**Objetivo clave**: Aplicar múltiples tipos de aserciones para validación completa.

**Verificación**: ¿Tienen al menos 5 aserciones diferentes funcionando?

### Tema 6: Ejecución y Depuración
**Objetivo clave**: Ejecutar suites completas y diagnosticar problemas.

**Verificación**: ¿Pueden usar breakpoints y leer el log efectivamente?

### Tema 7: Tipos de Pasos
**Objetivo clave**: Implementar flujos complejos con diferentes tipos de steps.

**Verificación**: ¿Funciona su data-driven test con DataSource?

## 🔧 Preparación Pre-Clase

### 1 Semana Antes
- [ ] Enviar requisitos de instalación a los alumnos
- [ ] Proporcionar enlace de descarga de SoapUI
- [ ] Verificar que JSONPlaceholder API esté operativa

### 1 Día Antes
- [ ] Probar todos los laboratorios en orden
- [ ] Preparar proyectos SoapUI de respaldo
- [ ] Verificar conectividad de red del aula

### Día de la Clase
- [ ] Llegar 15 minutos antes
- [ ] Verificar proyector/pantalla compartida
- [ ] Probar acceso a Internet
- [ ] Tener proyectos de ejemplo abiertos

## 💡 Consejos de Enseñanza

### Durante la Teoría
1. **Usa analogías simples**: Las incluidas en los documentos funcionan bien
2. **Muestra en vivo**: No solo PowerPoint, abre SoapUI
3. **Pregunta frecuentemente**: Verifica comprensión antes de continuar
4. **Relaciona con experiencia previa**: Conecta con Postman si ya lo conocen

### Durante los Laboratorios
1. **Sigue el ritmo del más lento**: Nadie debe quedarse atrás
2. **Circula por el aula**: Observa pantallas, detecta problemas
3. **Parejas de apoyo**: Alumnos avanzados ayudan a otros
4. **Muestra resultados esperados**: En pantalla grande para que comparen

### Manejo de Problemas Comunes

**"SoapUI no inicia"**
- Verificar Java instalado: `java -version`
- Reinstalar SoapUI con instalador completo
- Tener instalador portable de respaldo

**"No puedo acceder a JSONPlaceholder"**
- Verificar conectividad: `ping jsonplaceholder.typicode.com`
- Usar hotspot móvil como backup
- Tener respuestas de ejemplo guardadas

**"Mi Groovy Script no funciona"**
- Revisar sintaxis básica: punto y coma, comillas
- Verificar nombres de propiedades (case-sensitive)
- Mostrar ejemplo funcionando

**"Las aserciones fallan sin razón"**
- Revisar que el request se ejecutó primero
- Verificar expansión de propiedades
- Mostrar response raw para comparar

## 📊 Evaluación Continua

Durante la clase, verifica:

**Después de cada laboratorio:**
- ¿Cuántos completaron exitosamente? (>80% = bien)
- ¿Alguien está muy rezagado? (ayuda individual)
- ¿Dudas recurrentes? (explicar nuevamente)

**Señales de alerta:**
- Múltiples alumnos con el mismo error → problema común
- Silencio total en laboratorio → pueden estar atascados
- Terminan muy rápido → pueden estar saltando pasos

## 🎓 Evaluación Final (Opcional)

Si se requiere evaluación formal:

**Ejercicio práctico (1 hora):**
```
Crear un Test Suite completo para la API JSONPlaceholder
que incluya:

1. Test Case: Crear post
   - POST /posts con datos
   - Validar respuesta 201
   - Extraer ID del post

2. Test Case: Validar post creado
   - GET /posts/{id}
   - Múltiples aserciones
   - Comparar datos

3. Usar propiedades multinivel
4. Mínimo 5 aserciones diferentes
5. Logging apropiado

Criterios:
- Funcionalidad (50%): ¿Ejecuta correctamente?
- Organización (20%): ¿Estructura clara?
- Validación (20%): ¿Aserciones apropiadas?
- Buenas prácticas (10%): ¿Nombres, logs, etc.?
```

## 📚 Recursos de Apoyo

### Para Proyectar
- Documentos markdown (bien formateados)
- SoapUI en vivo
- Navegador con JSONPlaceholder

### Para Compartir
- Enlaces a documentación oficial
- Proyectos SoapUI de ejemplo
- Cheat sheet de JSONPath/Groovy

### Para Emergencias
- Proyectos pre-configurados
- Respuestas JSON de ejemplo
- Instalador SoapUI offline

## ✅ Checklist Final de Clase

Al terminar, los alumnos deben:
- [ ] Tener SoapUI configurado y funcionando
- [ ] Comprender la estructura jerárquica (Project → Suite → Case → Step)
- [ ] Saber crear requests y validarlos con aserciones
- [ ] Poder usar propiedades para compartir datos
- [ ] Entender cómo ejecutar y depurar tests
- [ ] Tener un proyecto completo funcional guardado

## 🔄 Retroalimentación Post-Clase

**Recopilar:**
- ¿Qué tema fue más difícil?
- ¿Qué laboratorio tomó más tiempo?
- ¿Qué mejorarías del material?
- ¿El ritmo fue apropiado?

**Ajustar para próxima sesión:**
- Tiempos por tema
- Nivel de detalle en explicaciones
- Complejidad de ejercicios

## 📞 Contacto y Soporte

Si los alumnos necesitan ayuda post-clase:
- Comunidad SoapUI: https://community.smartbear.com/
- Stack Overflow: tag [soapui]
- Documentación oficial

---

**Recuerda**: El objetivo es que terminen con confianza para usar SoapUI en proyectos reales. Prioriza la comprensión sobre la velocidad.

¡Buena clase! 🚀
