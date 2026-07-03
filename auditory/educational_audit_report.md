# Auditoría Técnica Revisada — Academic Management System

## Contexto de Evaluación

Este proyecto no debe evaluarse como software profesional ni como producto listo para producción.

Debe evaluarse como un proyecto de aprendizaje cuyo objetivo era desarrollar:

* Modularidad.
* Separación de responsabilidades.
* Persistencia en archivos.
* Uso intensivo de colecciones.
* Diseño de invariantes.
* Organización de código en múltiples clases.
* Pensamiento arquitectónico.

Durante el desarrollo aparecieron problemas no previstos originalmente que llevaron al desarrollador a aprender conceptos adicionales como:

* Dependency Injection.
* Dependencias circulares.
* Acoplamiento entre servicios.
* Organización por paquetes.
* Servicios de orquestación (conceptualmente).
* Diseño de capas.

Estos aprendizajes surgieron como consecuencia directa de problemas reales encontrados durante la implementación.

---

# Estado General del Proyecto

## Objetivos Completados

### Gestión de estudiantes

Completado.

Incluye:

* Alta.
* Edición.
* Eliminación.
* Búsqueda.
* Ordenamiento.
* Persistencia.

---

### Gestión de profesores

Completado.

Incluye:

* Alta.
* Edición.
* Eliminación.
* Búsqueda.
* Persistencia.

---

### Gestión de cursos

Completado.

Incluye:

* Alta.
* Edición.
* Eliminación.
* Asignación de profesor.
* Control de cupos.
* Persistencia.

---

### Gestión de inscripciones

Completado.

Incluye:

* Inscripción.
* Cancelación.
* Calificaciones.
* Historial académico.
* Actualización de promedios.

---

### Persistencia

Completada.

El sistema puede:

* Guardar entidades.
* Recuperarlas.
* Reconstruir relaciones mediante IDs.
* Mantener continuidad entre ejecuciones.

---

### Modularidad

Completada.

El proyecto fue dividido correctamente en:

* model
* repository
* service
* util
* app

La organización es coherente.

---

### Separación de responsabilidades

Mayormente completada.

Los servicios contienen lógica.

Los repositorios contienen almacenamiento.

Los modelos contienen datos.

Main funciona principalmente como capa de presentación.

---

## Objetivos Pendientes

### Sistema de reportes

Incompleto.

El menú existe pero la funcionalidad no está implementada.

Era parte explícita de los requisitos.

---

### Robustez frente a errores extremos

Parcialmente completado.

El sistema funciona correctamente bajo uso normal.

Sin embargo aún existen escenarios límite que pueden provocar:

* NullPointerException.
* Datos huérfanos.
* Estados inconsistentes.

---

# Arquitectura

## Evaluación

La arquitectura es uno de los puntos más fuertes del proyecto.

El desarrollador comenzó intentando aislar completamente los servicios.

Ese enfoque generó dificultades:

* Dependencias circulares.
* Duplicación de lógica.
* Aparición de un Helper excesivamente grande.

A partir de esos problemas se introdujo Dependency Injection manual.

Las instancias se crean una vez en Main y son compartidas.

Esto permitió:

* Reducir acoplamiento accidental.
* Evitar recrear servicios constantemente.
* Resolver problemas de dependencia.

La solución adoptada es razonable para el nivel actual.

---

# Modelado del Dominio

El dominio está correctamente representado.

Entidades:

* Student
* Teacher
* Course
* Registration

La decisión de utilizar IDs en lugar de referencias directas entre objetos fue apropiada.

Beneficios:

* Persistencia sencilla.
* Menor riesgo de referencias circulares.
* Menor complejidad conceptual.

Costo:

* Navegación más verbosa.
* Búsquedas frecuentes por ID.

Para este proyecto el intercambio es aceptable.

---

# Uso de Servicios

Los servicios contienen lógica real.

No son simples envoltorios de repositorios.

Ejemplos:

* Validaciones.
* Verificación de cupos.
* Protección de invariantes.
* Gestión de inscripciones.

Esto demuestra comprensión de responsabilidades.

---

# Uso de Dependency Injection

Este fue uno de los aprendizajes más importantes del proyecto.

Originalmente se intentó crear servicios donde eran necesarios.

Posteriormente se descubrió que:

* Las instancias podían compartirse.
* Las dependencias podían inyectarse.
* Main podía actuar como punto de composición.

La implementación es manual pero conceptualmente correcta.

---

# Helper

Helper representa una solución intermedia.

Originalmente absorbió responsabilidades de múltiples partes del sistema.

Posteriormente se identificó el riesgo de convertirlo en un God Object.

No se eliminó completamente, pero el desarrollador detectó correctamente el problema.

Eso es más importante que haberlo resuelto de forma perfecta.

---

# DTOs y Transferencia de Datos

El sistema utiliza:

ArrayList<ArrayList<String>>

como estructura de transferencia hacia Main.

Esta decisión no es ideal.

Problemas:

* Dependencia de posiciones.
* Ausencia de nombres semánticos.
* Fragilidad ante cambios de estructura.

Sin embargo la implementación está parcialmente mitigada por:

* showStudent()
* showStudents()
* showResume()

que centralizan el conocimiento de los índices.

Por lo tanto:

No es una buena solución.

Pero tampoco es el desastre arquitectónico que suele producir este enfoque.

Representa una solución funcional surgida antes de conocer DTOs reales.

---

# Persistencia

La persistencia cumple su objetivo.

Aspectos positivos:

* Implementación completa.
* Reconstrucción correcta de entidades.
* IDs automáticos.

Aspectos negativos:

* CSV manual frágil.
* Sin manejo robusto de corrupción.
* Sin soporte para comas dentro de campos.
* Encoding dependiente del sistema.

La persistencia es adecuada para aprendizaje.

No sería adecuada para producción.

---

# Invariantes

La mayoría de las invariantes importantes están protegidas.

Ejemplos:

* No duplicar inscripciones.
* No exceder cupos.
* No inscribir estudiantes inactivos.
* No inscribir en cursos sin profesor.
* Correos únicos.
* IDs únicos.

Sin embargo existen errores reales detectados.

---

# Errores Reales Confirmados

## División por cero al recalcular promedio

Confirmado.

Ocurre al cancelar la única inscripción de un estudiante.

Debe corregirse.

---

## Profesor eliminado con cursos asignados

Confirmado.

Los cursos conservan un teacherId inválido.

La solución propuesta de reemplazarlo por -1 es consistente con el resto del diseño.

---

## NullPointerException en edición de cursos

Confirmado y corregido.

---

# Falsos Positivos o Casos Exagerados

## Error de correo duplicado

La auditoría original exageró parcialmente este problema.

Existe una limitación durante edición.

Sin embargo el sistema implementa campos opcionales (skippable).

La solución necesaria es pequeña y localizada.

No representa una falla estructural.

---

# Calidad del Código

## Fortalezas

* Nombres claros.
* Organización coherente.
* Responsabilidades relativamente bien distribuidas.
* Uso consistente de servicios.
* Uso consistente de repositorios.

---

## Debilidades

* Uso excesivo de null.
* Algunos métodos duplican búsquedas.
* Ausencia de constantes simbólicas.
* DTOs débiles.
* Manejo de errores todavía inmaduro.

---

# Nivel Técnico del Desarrollador

## Lo que demuestra dominar

* Java básico e intermedio.
* Modularización.
* Colecciones.
* Persistencia simple.
* Encapsulación básica.
* Arquitectura en capas.
* Inyección de dependencias manual.
* Diseño de invariantes.
* Organización por paquetes.

---

## Lo que todavía no domina

* Manejo sistemático de errores.
* Diseño de APIs consistentes.
* DTOs tipados.
* Generics.
* Interfaces.
* Polimorfismo.
* Testing automatizado.
* Persistencia robusta.
* Diseño orientado a extensibilidad.

---

# Evaluación Final

## Cumplimiento de Objetivos

| Objetivo                        | Estado              |
| ------------------------------- | ------------------- |
| Modularidad                     | Cumplido            |
| Separación de responsabilidades | Cumplido            |
| Uso de listas                   | Cumplido            |
| Persistencia                    | Cumplido            |
| Invariantes                     | Mayormente cumplido |
| Arquitectura básica             | Cumplido            |
| Reportes                        | Pendiente           |
| Robustez                        | Parcial             |

---

## Puntuación Técnica

Como software general:

72/100

Como proyecto educativo:

85/100

La diferencia existe porque el objetivo principal no era construir un sistema listo para producción, sino obligar al desarrollador a enfrentarse a problemas reales de diseño.

El proyecto cumple ese objetivo de forma satisfactoria.

La principal evidencia es que durante el desarrollo aparecieron problemas arquitectónicos genuinos (dependencias circulares, God Objects, compartición de instancias, responsabilidades entre capas) y el desarrollador fue capaz de identificarlos y encontrar soluciones funcionales para ellos.
