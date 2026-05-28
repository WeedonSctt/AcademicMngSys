# Proyecto Grande de Java: Sistema de Gestión Académica

## Objetivo General

Construir un sistema de consola relativamente grande y modular para administrar una pequeña institución educativa.

El proyecto debe obligarte a:

- Diseñar correctamente modelos e invariantes.
- Modularizar el código en varios archivos `.java`.
- Evitar repetición.
- Trabajar intensivamente con listas (`ArrayList`).
- Leer y escribir archivos de texto.
- Diseñar menús y flujo de navegación.
- Empezar a pensar en persistencia de datos.
- Prepararte para POO más avanzada.

Este proyecto está deliberadamente pensado para quedar justo por encima de tu nivel actual.

---

# Idea General

El sistema administra:

- Estudiantes
- Cursos
- Profesores
- Inscripciones
- Calificaciones
- Reportes

Todo desde consola.

No necesitas interfaces gráficas.

---

# Restricciones Técnicas

## Debes usar

- Java puro
- `ArrayList`
- Múltiples archivos `.java`
- Métodos reutilizables
- Archivos `.txt`
- Menús en consola
- Validaciones
- Búsquedas
- Ordenamientos manuales

## No debes usar todavía

- Frameworks
- Bases de datos reales
- Streams avanzados
- Threads
- JavaFX/Swing
- Librerías externas grandes

---

# Concepto Nuevo Recomendado

## Introducir: CSV

Aprenderás un concepto extremadamente útil:

## ¿Qué es?

Un archivo de texto estructurado:

```txt
1,Juan Perez,20
2,Ana Lopez,22
```

Esto te permitirá:

- Guardar objetos
- Reconstruir objetos desde archivos
- Pensar como una base de datos simple
- Prepararte para SQL después

---

# Arquitectura Esperada

## Paquetes sugeridos

```txt
app/
model/
service/
repository/
util/
```

---

# Modelos Principales

## Estudiante

Campos sugeridos:

```java
id
nombre
edad
correo
promedio
activo
```

## Profesor

```java
id
nombre
especialidad
correo
```

## Curso

```java
id
nombre
cupoMaximo
profesorAsignado
```

## Inscripcion

```java
idEstudiante
idCurso
calificacion
```

---

# Invariantes Importantes

Aquí es donde realmente mejorarás como programador.

## Ejemplos

### Estudiante

- ID nunca negativo.
- Correo no vacío.
- Promedio entre 0 y 100.

### Curso

- Cupo máximo > 0.
- No exceder capacidad.
- No duplicar estudiantes.

### Inscripción

- No inscribir estudiante inexistente.
- No inscribir dos veces al mismo estudiante en el mismo curso.

---

# Funcionalidades Obligatorias

# Módulo Estudiantes

## Debe permitir

- Crear estudiante
- Editar estudiante
- Eliminar estudiante
- Buscar por nombre
- Buscar por ID
- Mostrar todos
- Ordenar por promedio
- Ordenar alfabéticamente

---

# Módulo Profesores

## Debe permitir

- Crear
- Editar
- Eliminar
- Buscar
- Mostrar cursos asignados

---

# Módulo Cursos

## Debe permitir

- Crear curso
- Asignar profesor
- Mostrar estudiantes inscritos
- Mostrar cupo restante
- Eliminar curso

---

# Módulo Inscripciones

## Debe permitir

- Inscribir estudiante
- Cancelar inscripción
- Registrar calificaciones
- Ver historial académico

---

# Persistencia

## Debes guardar información en archivos

Archivos sugeridos:

```txt
estudiantes.txt
profesores.txt
cursos.txt
inscripciones.txt
```

---

# Ejemplo de formato CSV

## estudiantes.txt

```txt
1,Juan Perez,20,juan@gmail.com,87.5,true
2,Ana Lopez,22,ana@gmail.com,91.0,true
```

---

# Funciones de Persistencia

Necesitarás cosas como:

```java
guardarEstudiantes()
leerEstudiantes()
serializarEstudiante()
parsearEstudiante()
```

Aquí aprenderás muchísimo.

---

# Utilidades Recomendadas

## Clase InputHelper

Centralizar:

```java
leerInt()
leerDouble()
leerString()
leerBoolean()
```

Esto reduce repetición.

---

## Clase Validator

```java
correoValido()
rangoValido()
textoVacio()
```

---

## Clase FileManager

```java
leerLineas()
escribirLineas()
agregarLinea()
```

---

# Nivel Extra (Muy Recomendado)

## Implementar IDs automáticos

En lugar de pedir el ID:

```txt
último ID + 1
```

Esto te obliga a:

- Pensar en estado global.
- Diseñar correctamente.
- Evitar colisiones.

---

# Nivel Extra 2

## Sistema de Reportes

Generar:

- Mejor estudiante
- Promedio global
- Curso con más alumnos
- Profesor con más cursos
- Estudiantes reprobados

---

# Nivel Extra 3

## Menú Administrativo Real

```txt
1. Estudiantes
2. Profesores
3. Cursos
4. Inscripciones
5. Reportes
6. Guardar
7. Salir
```

Cada sección debe tener submenús.

---

# Nivel Extra 4 (El MÁS importante)

## Separar lógica y datos

NO hacer:

```java
System.out.println("Nombre:");
```

Dentro de clases de negocio.

La lógica del sistema debe estar separada de la interfaz.

Ejemplo:

- `StudentService` → lógica.
- `Main` → interacción con usuario.

Este paso es enorme para madurar.

---

# Conceptos que Aprenderás Sin Darte Cuenta

## Diseño

- Cohesión
- Acoplamiento
- Responsabilidades
- Organización modular

## Algoritmos

- Búsquedas
- Filtrado
- Ordenamiento
- Validación

## Persistencia

- Parseo
- Serialización manual
- Integridad de datos

## Arquitectura

- Separación de capas
- Flujo de datos
- Reutilización

---

# Qué Voy a Evaluar Cuando Me Enseñes el Código

## Diseño

- ¿Tus clases tienen responsabilidades claras?
- ¿Evitas repetición?
- ¿Tus nombres son buenos?

## Modelado

- ¿Elegiste bien las estructuras?
- ¿Tus invariantes están protegidas?

## Calidad

- ¿Tu código es legible?
- ¿Está modularizado?
- ¿Los métodos son razonables?

## Persistencia

- ¿Tu sistema rompe datos fácilmente?
- ¿El parseo es robusto?

## Escalabilidad

- ¿Agregar nuevas funciones sería fácil?
- ¿O tu código ya está muy acoplado?

---

# Qué NO me importará demasiado

- Que el código sea perfecto.
- Que uses patrones avanzados.
- Que el sistema sea visualmente bonito.

Lo importante será:

- Cómo piensas.
- Cómo modelas.
- Cómo organizas.
- Qué tan mantenible es.

---

# Sugerencia de Desarrollo

## Fase 1

Modelos simples.

## Fase 2

CRUD básico.

## Fase 3

Persistencia.

## Fase 4

Inscripciones y relaciones.

## Fase 5

Reportes.

## Fase 6

Refactorización fuerte.

---

# Señales de que lo estás haciendo bien

- Empiezas a mover código entre clases.
- Descubres repetición y la eliminas.
- Empiezas a pensar en invariantes antes de programar.
- Empiezas a notar acoplamiento.
- Cambiar algo deja de romper todo.

Ese es el objetivo real del proyecto.

