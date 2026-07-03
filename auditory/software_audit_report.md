# Auditoría Técnica Completa — Academic Management System

> **Fecha:** 2026-06-18 | **Revisado por:** Antigravity (AI Audit)
> **Base:** `src/org/institution/app/` + `data/csv/`

---

## 1. Resumen General

### Objetivo del proyecto
Sistema de consola modular para administrar una institución educativa: estudiantes, profesores, cursos, inscripciones, calificaciones y reportes, usando Java puro con archivos CSV como persistencia.

### Funcionalidades implementadas

| Módulo | Funcionalidad | Estado |
|---|---|---|
| Estudiantes | Crear, Editar, Eliminar, Buscar nombre/ID, Mostrar todos | ✅ |
| Estudiantes | Ordenar por promedio, Ordenar alfabéticamente | ✅ |
| Profesores | Crear, Editar, Eliminar, Buscar nombre/ID, Mostrar todos | ✅ |
| Profesores | Mostrar cursos asignados | ✅ |
| Cursos | Crear, Editar, Eliminar, Asignar profesor | ✅ |
| Cursos | Mostrar inscritos, Mostrar cupo restante | ✅ |
| Inscripciones | Crear, Cancelar, Calificar, Ver historial académico | ✅ |
| Persistencia | Guardar y cargar las 4 entidades desde CSV | ✅ |
| Reportes | Menú presente | ❌ (vacío — `reportsMenu()` sin implementar) |

### Funcionalidades faltantes
- **Módulo de reportes completo** (mejor estudiante, promedio global, curso con más alumnos, etc.)
- **Búsqueda parcial** por nombre: actualmente es exacta en profesores y estudiantes.
- **Ordenamiento manual** (doc.md sugiere sin `Comparator`; el proyecto usa `ArrayList.sort`).
- **Eliminación de estudiante activo bloqueada** sin forma de desactivarlo directamente.

### Estado general y estimación de completitud
El núcleo funciona correctamente. La arquitectura supera ampliamente lo esperado para el nivel.
**Estimación: ~82% completo.**

---

## 2. Estructura del Proyecto

```
AcademicMngSys/
├── data/csv/
│   ├── courses.txt
│   ├── registrations.txt
│   ├── students.txt
│   └── teachers.txt
└── src/org/institution/app/
    ├── Main.java
    ├── model/          Course, Registration, Student, Teacher
    ├── repository/     CourseRepository, RegistrationRepository,
    │                   StudentRepository, TeacherRepository
    ├── service/        CourseService, RegistrationService,
    │                   StudentService, TeacherService
    └── util/           Enum, FileManager, Helper,
                        InputHelper, Validator
```

| Paquete | Responsabilidad |
|---|---|
| `model` | POJOs puros: campos, getters, setters |
| `repository` | Colecciones en memoria + lectura/escritura CSV |
| `service` | Lógica de negocio, validaciones e invariantes |
| `util` | FileManager, Helper, InputHelper, Validator, Enum |
| `Main.java` | Menús, interacción con el usuario, orquestación |

**Evaluación:** Coherente. Sigue exactamente la arquitectura sugerida en `doc.md`. Organización lógica y clara.

---

## 3. Arquitectura

```
[ Main.java ]           <- Presentación / UI
      |
[ Service ]             <- Lógica de negocio
      |
[ Repository ]          <- Datos en memoria
      |
[ Model ]               <- Entidades del dominio
      |
[ FileManager/Helper ]  <- Persistencia CSV
```

**Separación lograda:** Los `Service` ejecutan validaciones. El `Main` nunca valida directamente — delega y solo interpreta `Enum.Error` o `null`. Los modelos son POJOs sin lógica.

**Acoplamiento notable:**
- `RegistrationService` depende de `StudentService`, `CourseService` y `TeacherService` simultáneamente.
- `CourseService` depende de `TeacherService` — dependencia cruzada entre servicios.

**¿Es mantenible?** Sí, para el tamaño actual. Agregar un nuevo módulo requiere crear Model + Repository + Service + sección en Main, sin tocar lo existente.

---

## 4. Clases

### Modelos
| Clase | Campos principales | Evaluación |
|---|---|---|
| `Student` | `id(final)`, `name`, `age`, `email`, `averageGrade`, `isActive` | Limpio, 2 constructores (creación/carga CSV) |
| `Teacher` | `id(final)`, `name`, `department`, `email` | Mínimo y correcto |
| `Course` | `id(final)`, `name`, `description`, `maximumStudents`, `teacherId` | `teacherId` (int) evita referencias circulares |
| `Registration` | `studentId(final)`, `courseId(final)`, `grade` | Tabla de relación clásica |

### Repositorios
Estructura idéntica en los 4: `ArrayList<T>`, `FileManager` (×2, innecesario), `int lastID`.
- `StudentRepository`: import sin uso (`java.lang.reflect.Array`).
- `TeacherRepository`: `lastID` no es `private`.

### Servicios
- `StudentService`, `TeacherService`: Bien definidos. Duplican búsqueda por ID del repositorio.
- `CourseService`: Bien estructurado. `replaceTeacherIDWithName` (privado) buen ejemplo de encapsulamiento. Hardcodea `50` sin constante.
- `RegistrationService`: La más compleja (4 dependencias). `getAcademicHistory` construye "APPROVED"/"FAILED" — roza mezcla de lógica y presentación.

### Main (699 líneas)
No es God Object estricto (sin lógica de negocio), pero concentra toda la UI. Aceptable para el nivel.

### Utilidades
| Clase | Evaluación |
|---|---|
| `Helper` | Mezcla serialización de 4 entidades + ordenamientos. Aceptable. |
| `FileManager` | Compacto y reutilizable. Ruta `"data/csv/"` hardcodeada. |
| `InputHelper` | Bien diseñado. Cubre int, double, String, boolean. |
| `Validator` | Funcional. `emailHasExtension` básico pero suficiente. |
| `Enum` | Buen patrón de error-by-value. Nombre colisiona con clase base de Java. |

---

## 5. Modelado del Dominio

| Entidad | Campos presentes | vs. doc.md | Diferencias |
|---|---|---|---|
| Student | `id, name, age, email, averageGrade, isActive` | ✅ | Ninguna |
| Teacher | `id, name, department, email` | ✅ | `department` ≈ `especialidad` |
| Course | `id, name, description, maximumStudents, teacherId` | ✅+ | Agrega `description` (mejora) |
| Registration | `studentId, courseId, grade` | ✅ | Ninguna |

**Calidad:** Excelente para el nivel. Los modelos representan fielmente el dominio.

**Atributos cuestionables:**
- `averageGrade = -0.1` como centinela de "sin calificación" es un hack. Mejor: `Double` nullable.
- Nota de aprobación hardcodeada: `grade >= 6.0`. Sin umbral configurable.

---

## 6. Relaciones Entre Objetos

```
Teacher (1) <---- teacherId ---- Course (N)
                                     |
                               courseId ---- Registration (N)
                                             |
Student (1) <---- studentId -----------------+
```

- **Curso ↔ Profesor:** `Course.teacherId` (int). Sin profesor: `teacherId = -1`.
- **Inscripción:** Tabla de relación clásica con `studentId` y `courseId`.
- **Sin listas de IDs en objetos:** Toda navegación itera colecciones. Consistente.

**Diseño correcto** para el contexto sin base de datos. La integridad referencial se protege en servicios.

> [!WARNING]
> Al eliminar un profesor, los cursos con ese `teacherId` quedan con referencia huérfana. Al mostrarlos, `replaceTeacherIDWithName` llama `.getName()` sobre `null` → **NPE**. Bug más grave del sistema.

---

## 7. Uso de ArrayList y Colecciones

| Colección | Clase | Propósito |
|---|---|---|
| `ArrayList<Student/Teacher/Course/Registration>` | Repositorios | Almacenes principales |
| `ArrayList<ArrayList<String>>` | Servicios/Main | DTO hacia la UI (frágil) |
| `ArrayList<Double>` | `RegistrationRepository` | Calificaciones de un estudiante |
| `ArrayList<Integer>` | `RegistrationService` | Índices para eliminar en batch |
| `ArrayList<String>` | `Helper`, `FileManager` | Serialización/parseo CSV |

**Evaluación:**
- `ArrayList` adecuado para acceso secuencial frecuente.
- Para búsquedas por ID: `HashMap<Integer, T>` sería O(1) vs. O(n) actual.
- `ArrayList<ArrayList<String>>` como DTO frágil: acceso por índice (`get(0)`, `get(1)`) no es autodocumentado.
- Todas las búsquedas por ID son O(n) y se duplican entre repositorio y servicio.

---

## 8. Persistencia

| Archivo | Campos CSV | Ejemplo real |
|---|---|---|
| `students.txt` | `id,name,age,email,averageGrade,isActive,` | `4,David Elias...,18,...,9.93,true,` |
| `teachers.txt` | `id,name,department,email,` | `3,Arturo,Mathematics,arturo@gmail.com,` |
| `courses.txt` | `id,name,description,maximumStudents,teacherId,` | `2,Math IV,...,15,3,` |
| `registrations.txt` | `studentId,courseId,grade,` | `4,2,9.9,` |

**Ciclo completo:** Objeto → `Helper.xToStringArray()` → `FileManager.writeToFile()` → CSV.
**Carga:** CSV → `FileManager.readFromFile()` → parse → constructor de carga → `lastID` recalculado por máximo.

**Casos de falla:**
1. Archivo vacío → lista vacía sin error. OK.
2. Archivo no existe → `FileNotFoundException` capturada → `false`. Sistema sigue con listas vacías.
3. Trailing comma → campo vacío extra, ignorado por parseo por índice. Ruido silencioso.
4. Datos corruptos → `NumberFormatException` **no capturada** → crash en carga. ❌

---

## 9. Formato CSV

**Serialización:** `Helper.stringArrayToCSV()` hace `append(s) + append(",")` — genera trailing comma siempre.

**Parseo:** `FileManager.readFromFile()` itera carácter a carácter, divide por `','`. Parser manual básico.

| Problema | Impacto |
|---|---|
| No soporta comas en campos | "García, Juan" rompe el parseo de todos los campos siguientes |
| No soporta `\n` en campos | Rompe la lectura de filas |
| Trailing comma | Campo vacío extra al final, ignorado silenciosamente |
| Datos corruptos | `NumberFormatException` no capturada — crash |
| Encoding no fijo | `FileWriter` usa encoding del sistema. Evidencia: `prop?sito` en `courses.txt` |

---

## 10. Validaciones

| Validación | Dónde | Estado |
|---|---|---|
| Email con `@` y `.` | `Validator.emailHasExtension` | ✅ básica |
| Edad mínima 18 | `Validator.studentInputData` | ✅ |
| Email no duplicado | `StudentService`, `TeacherService` | ✅ |
| Cupo máximo ≤ 50 | `CourseService.newCourse` | ⚠️ permite 0 |
| Curso no duplicado por nombre | `CourseService` | ✅ |
| Estudiante+Curso existen al inscribir | `RegistrationService` | ✅ |
| Cupo no excedido | `RegistrationService` | ✅ |
| Estudiante activo al inscribir | `RegistrationService` | ✅ |
| Curso con profesor al inscribir | `RegistrationService` | ✅ |
| Sin inscripción duplicada | `RegistrationService` | ✅ |
| Calificación 0-100 | `RegistrationService.grade` | ✅ |
| Formato de input | `InputHelper` | ✅ |

**Faltantes o incorrectas:**
- `maximumStudents < 0` debería ser `<= 0` (permite cupo 0).
- Sin límite superior de edad.
- Nombre solo valida primer carácter como letra.
- Al eliminar profesor: no verifica si tiene cursos asignados.
- Al editar email: `existEmail` detecta el propio email como "duplicado" — el usuario no puede mantener su email al editar.

---

## 11. Invariantes

### Protegidas ✅

| Invariante | Mecanismo |
|---|---|
| No inscribir estudiante inexistente | `existStudent(id)` en `newRegistration` |
| No superar cupo | `getCourseRemainingQuota() <= 0` bloquea |
| No inscribir inactivo | `!student.isActive()` bloquea |
| Sin inscripción duplicada | Loop de verificación en `newRegistration` |
| Sin inscribir en curso sin profesor | `teacherId == -1` bloquea |
| No eliminar estudiante activo | `deleteStudent` verifica `isActive()` |
| IDs sin colisión | `lastID + 1` en todos los repositorios |

### Que pueden romperse ❌

1. **Referencia a profesor eliminado** → NPE en `replaceTeacherIDWithName`. Bug más grave.
2. **División por cero** al cancelar única inscripción → `setAverageGrade(id, [])` → `sum/0`.
3. **NPE en `editCourse`** → `getCourseByID(id)` devuelve null sin verificación previa.
4. **NPE en `searchTeacherByID`** → `Helper.teacherToStringArray(null)` cuando ID no existe.
5. **Off-by-one** en `removeRegistrationsIndexes`: `i > 0` debería ser `i >= 0`. El primer índice nunca se elimina.

---

## 12. Flujo de Menús

```
Main Menu
├── 1. Students         (9 opciones + Back) ✅
├── 2. Teachers         (8 opciones + Back) ✅
├── 3. Courses          (8 opciones + Back) ✅
├── 4. Registrations    (5 opciones + Back) ✅
├── 5. Reports          <- VACÍO, regresa sin feedback ❌
├── 6. Save             ✅
└── 7. Exit             (guarda automáticamente) ✅
```

**Problemas:**
- Opción 5 (Reports) está vacía y regresa sin mensaje al usuario.
- `Main` usa `|` (OR bitwise) en lugar de `||` (OR lógico) al cargar/guardar — no hay short-circuit. Probablemente involuntario.
- El flujo de navegación "Back" es correcto: `return` evita doble `pressToContinue`.

---

## 13. Entrada de Datos

**Puntos fuertes:**
- Un único `Scanner` estático en `Main`.
- `InputHelper` centraliza toda la lectura.
- Todos los métodos usan `sc.nextLine()` — evita problemas de buffer.

| Situación | Comportamiento |
|---|---|
| Texto donde se espera número | Loop solicitando de nuevo. ✅ |
| Vacío en campo obligatorio | Loop solicitando de nuevo. ✅ |
| Vacío en campo skippable | Devuelve `null` / `-1`. ✅ |
| `inputDouble` con vacío | No hace `continue` — cae al `try/catch`. Funciona por accidente. |
| ID negativo | Acepta; fallará limpiamente en búsqueda. |

---

## 14. Manejo de Errores

| Ubicación | Excepción capturada | Acción |
|---|---|---|
| `FileManager.writeToFile` | `IOException` | Retorna `Enum.Error` |
| Repositorios `saveX` | `IOException` | Retorna `false` |
| Repositorios `loadX` | `IOException` | Retorna `false` |
| `InputHelper.inputInteger/Double` | `NumberFormatException` | Loop, pide de nuevo |

**Errores que tumban el sistema:**
1. NPE en `replaceTeacherIDWithName` — profesor eliminado con cursos.
2. NPE en `editCourse` — curso ID inexistente sin verificación previa.
3. NPE en `searchTeacherByID` — teacher ID inexistente sin null-check.
4. División por cero en `setAverageGrade` — lista de calificaciones vacía.
5. `NumberFormatException` no capturada al cargar CSV corrupto.

---

## 15. Reutilización de Código

| Código duplicado | Dónde |
|---|---|
| `getByID` por iteración | En repositorio Y en servicio (Student, Teacher, Course) |
| `existX(id)` | En repositorio Y en servicio |
| Inicialización de `lastID` al cargar CSV | Bloque idéntico en 3 repositorios |
| Dos instancias de `FileManager` por repositorio | Los 4 repositorios |

**Soluciones:**
- Servicios deben delegar búsqueda por ID al repositorio, no reimplementarla.
- Una clase base `BaseRepository<T>` eliminaría el código repetido de `lastID`.
- Un único `FileManager` por repositorio.

---

## 16. Calidad de Nombres

| Elemento | Ejemplos | Calidad |
|---|---|---|
| Clases | `StudentService`, `CourseRepository`, `InputHelper` | ✅ Excelente |
| Métodos | `newStudent`, `editStudentData`, `saveStudentsToCSV` | ✅ Buena |
| Variables | `studentsData`, `assignedCoursesData`, `academicHistory` | ✅ Descriptivas |
| Constantes | No hay (magic numbers: `50`, `6.0`, `-0.1`) | ⚠️ Faltan |

**Problemáticos:**
- `Enum` — colisiona con clase base de Java.
- `manageError` — en realidad "imprime error y retorna si hubo éxito".
- `loadRepo()` — vago; `loadFromFile()` sería mejor.
- `reader`/`writer` — dos instancias idénticas del mismo tipo.

---

## 17. Cohesión

| Clase | Cohesión | Justificación |
|---|---|---|
| Modelos | **Alta** | Solo datos del modelo |
| Repositorios | **Alta** | Giran en torno a su colección específica |
| `StudentService`, `TeacherService` | **Alta** | Lógica exclusiva de su entidad |
| `CourseService` | **Alta** | `replaceTeacherIDWithName` privado y cohesivo |
| `RegistrationService` | **Media-Alta** | Inscripciones, calificaciones, historial — relacionados |
| `Helper` | **Media** | Mezcla serialización de 4 entidades + 3 ordenamientos |
| `Main` | **Media** | 699 líneas de UI — cohesivo en propósito, difícil de leer |
| `Enum`, `FileManager`, `InputHelper`, `Validator` | **Alta** | Responsabilidad única y clara |

---

## 18. Acoplamiento

```
RegistrationService
    +-- -> StudentService
    +-- -> CourseService ----> TeacherService
    +-- -> TeacherService
```

**Riesgos de mantenimiento:**
- Si `TeacherService` cambia su API, `CourseService` y `RegistrationService` deben actualizarse.
- `ArrayList<ArrayList<String>>` como DTO: cambiar el orden de campos en `Helper.studentToStringArray` rompe `showStudent` en `Main` silenciosamente.

---

## 19. Complejidad

| Método | Líneas aprox. | Complejidad |
|---|---|---|
| `Main.studentsMenu()` | ~125 | Alta (switch + N casos) |
| `Main.teachersMenu()` | ~125 | Alta |
| `Main.coursesMenu()` | ~125 | Alta |
| `Main.registrationsMenu()` | ~90 | Media |
| `RegistrationService.getAcademicHistory()` | ~45 | Media |
| `FileManager.readFromFile()` | ~31 | Media (doble bucle) |

**Bug crítico de lógica:**
```java
// RegistrationRepository.removeRegistrationsIndexes()
for (int i = indexes.size()-1; i > 0; i--)  // BUG: omite índice 0
// Corrección:
for (int i = indexes.size()-1; i >= 0; i--)
```

---

## 20. Escalabilidad

| Extensión | Dificultad | Justificación |
|---|---|---|
| Nuevo tipo de usuario | Media | Model + Repository + Service + Main. No rompe lo existente. |
| Nuevos reportes | Baja | Llenar `reportsMenu()` |
| Nuevo módulo | Media | Misma estructura que los demás |
| Nuevo formato de almacenamiento | Media-Alta | Sin interfaz `Repository<T>`, el cambio afecta todos los repositorios y Helper |
| Nuevos campos en modelo | Baja-Media | Constructor + getters/setters + `Helper.xToStringArray` + parser |

La arquitectura facilita extensión horizontal. La mayor rigidez está en la persistencia.

---

## 21. Código Muerto

| Elemento | Ubicación | Motivo |
|---|---|---|
| `import java.lang.reflect.Array;` | `StudentRepository.java:4` | Nunca usado |
| `return;` al final de `newTeacher` | `TeacherRepository.java:23` | `void` no lo necesita |
| `return;` al final de `newCourse` | `CourseRepository.java:28` | Ídem |
| `Enum.Error.PLACEHOLDER_VALUE` | `Enum.java:39` | Nunca referenciado |
| `Enum.Error.FILE_NOT_FOUND` | `Enum.java:33` | Nunca referenciado |
| `Enum.Error.INVALID_INPUT_DATA` | `Enum.java:37` | Nunca referenciado |
| `reportsMenu()` | `Main.java:562` | Método vacío |
| Dos instancias de `FileManager` | Todos los repositorios | Solo se necesita una |

---

## 22. Posibles Bugs (Priorizado)

### Críticos (pueden crashear el sistema)

1. **NPE — profesor eliminado con cursos asignados**
   `CourseService.replaceTeacherIDWithName:174` → `teacherService.getTeacherByID(teacherID).getName()` cuando el profesor fue eliminado.

2. **Off-by-one — `removeRegistrationsIndexes`**
   `RegistrationRepository:54` → `i > 0` debería ser `i >= 0`. El primer índice nunca se elimina; quedan inscripciones huérfanas.

3. **División por cero — `setAverageGrade`**
   `RegistrationService:56` → `sum/grades.size()` cuando `grades` está vacío (al cancelar la única inscripción).

4. **NPE — `editCourse` sin verificar existencia**
   `CourseService:62` → `getCourseByID(id)` puede devolver `null`; sin comprobación previa.

5. **`NumberFormatException` no capturada en carga de CSV**
   Todos los `loadXFromCSV` crashean si un campo numérico tiene texto corrupto.

### Importantes

6. **Cursos con `teacherId` inválido tras eliminar profesor** — sin protección al eliminar.

7. **Editar email propio rechazado** — `existEmail` detecta el propio email del estudiante como "duplicado".

8. **NPE — `searchTeacherByID` sin null-check**
   `TeacherService:72` → `Helper.teacherToStringArray(null)` cuando el ID no existe.

9. **`inputDouble` sin `continue`** — funciona por accidente a través del `catch`.

10. **`getCoursesEnrolledByStudent` muestra `teacherId` crudo** — inconsistente con `getCourses()`.

### Menores

11. Trailing comma en CSV genera campo vacío extra.
12. Encoding no fijo en `FileWriter` — `prop?sito` en `courses.txt`.
13. `-0.1` como centinela no documentado.
14. `|` bitwise en lugar de `||` lógico en `Main` al cargar/guardar.

---

## 23. Refactorizaciones Recomendadas

### Críticas (corregir antes de continuar)

**1. Null-check en `replaceTeacherIDWithName`:**
```java
// Cambiar:
course.add(teacherService.getTeacherByID(teacherID).getName());
// Por:
Teacher t = teacherService.getTeacherByID(teacherID);
course.add(t != null ? t.getName() : "UNKNOWN");
```

**2. Corregir off-by-one:**
```java
// Cambiar:
for (int i = indexes.size()-1; i > 0; i--)
// Por:
for (int i = indexes.size()-1; i >= 0; i--)
```

**3. Verificar lista vacía antes de calcular promedio:**
```java
if (grades.isEmpty()) {
    studentService.setAverageGrade(studentID, -0.1);
    return;
}
```

**4.** Verificar existencia del curso en `editCourse`.

**5.** Capturar `NumberFormatException` en `loadXFromCSV`.

### Importantes

**6.** Bloquear eliminación de profesor con cursos asignados o desvincularlos.

**7.** Excluir el email actual al validar duplicados en edición.

**8.** Null-check en `searchTeacherByID`.

**9.** Reemplazar `|` por `||` en `Main`.

**10.** UTF-8 explícito: `new FileWriter(f, StandardCharsets.UTF_8)`.

**11.** Eliminar import `java.lang.reflect.Array`.

### Opcionales (calidad)

**12.** Renombrar `Enum` a `AppErrors` o `ErrorCode`.

**13.** Extraer clase `Display` con los métodos `showStudent`, `showTeacher`, etc.

**14.** Un único `FileManager` por repositorio.

**15.** `HashMap<Integer, T>` para búsquedas O(1).

**16.** Constantes nombradas:
```java
private static final int MAX_COURSE_QUOTA = 50;
private static final double NO_GRADE_SENTINEL = -0.1;
private static final double PASS_GRADE = 6.0;
```

**17.** Implementar `reportsMenu()`.

---

## 24. Comparación Contra Objetivos del Proyecto

| Criterio (doc.md) | Calificación | Evidencia |
|---|---|---|
| **Modularidad** | **9/10** | 4 paquetes bien definidos, 18 archivos Java. Main muy grande. |
| **Separación de responsabilidades** | **8/10** | UI en Main, lógica en Services, datos en Repositories. RegistrationService roza presentación. |
| **Uso correcto de ArrayList** | **7/10** | Correcto y extensivo. Duplica búsquedas por ID. DTO por `ArrayList<ArrayList<String>>` frágil. |
| **Persistencia** | **8/10** | CSV completo. Encoding no fijo, sin manejo de corrupción, trailing comma. |
| **Diseño de invariantes** | **8/10** | Mayoría protegidas. Bugs críticos en NPE de profesor y off-by-one. |
| **Organización en múltiples archivos** | **10/10** | 18 archivos, bien nombrados, bien ubicados. |
| **Reutilización de código** | **6/10** | Buena en general. Búsquedas por ID duplicadas. Helper centraliza serialización. |

---

## 25. Evaluación Final

### Puntuación Técnica Global

| Dimensión | Puntos |
|---|---|
| Arquitectura y organización | 22/25 |
| Corrección funcional | 17/25 |
| Calidad de código | 16/25 |
| Manejo de errores y robustez | 13/25 |
| **Total** | **68/100** |

---

### Fortalezas principales

1. **Arquitectura excelente para el nivel:** implementó separación en capas de forma correcta y consistente.
2. **Inyección de dependencias manual:** servicios reciben repositorios y dependencias por constructor.
3. **Patrón de error por valor (`Enum.Error`):** produce código limpio en `Main` sin lanzar excepciones.
4. **IDs automáticos:** `lastID + 1` correctamente en todos los repositorios.
5. **Persistencia completa:** todas las entidades se leen y guardan con reconstrucción correcta.
6. **InputHelper:** centraliza lectura de datos de forma reutilizable y robusta.
7. **Invariantes cubiertos en su mayoría:** la gran mayoría de reglas de negocio críticas protegidas en servicios.

---

### Debilidades principales

1. **Bugs críticos no detectados:** NPE al eliminar profesor, off-by-one en remoción de registros, división por cero al cancelar inscripción.
2. **Módulo de reportes completamente vacío.**
3. **CSV frágil:** sin soporte de comas en texto, sin manejo de corrupción, encoding no fijo.
4. **Duplicación de lógica:** búsquedas por ID en repositorio y servicio.
5. **Bug de edición de email:** el usuario no puede mantener su propio email al editar.
6. **Sin constantes nombradas:** magic numbers `50`, `6.0`, `-0.1` dispersos.

---

### Nivel aproximado del programador

> **Junior en transición hacia Junior-Intermedio.**

**Comprensión sólida de:**
- Diseño OO básico (encapsulamiento, separación de clases)
- Arquitectura en capas (superó al programador que todo lo hace en `main`)
- Inyección de dependencias manual
- Persistencia básica con CSV

**Aún por desarrollar:**
- Pensamiento en casos borde y pruebas mentales de flujos de error
- Manejo robusto de excepciones
- Abstracción de patrones repetidos (DRY avanzado)
- Pruebas unitarias

---

### Próximos conceptos recomendados (en orden)

1. **Interfaces y polimorfismo** — `Repository<T>` para eliminar duplicación masiva.
2. **Excepciones personalizadas** — `StudentNotFoundException extends RuntimeException`.
3. **Generics** — métodos reutilizables entre repositorios.
4. **`Optional<T>`** — manejar `null` de forma explícita y segura.
5. **Unit Testing con JUnit** — detectar automáticamente bugs como los encontrados.
6. **Builder pattern o Records** — DTOs tipados vs. `ArrayList<ArrayList<String>>`.
7. **Streams básicos** — `list.stream().filter().findFirst()` vs. loops manuales.

---

*Fin del informe de auditoría.*
