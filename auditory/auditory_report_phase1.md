# Informe de Auditoría Técnica — Migración a Interfaces (Etapa 1 POO)

**Proyecto:** Academic Management System  
**Fecha de revisión:** 2026-07-03  
**Alcance:** Evaluación exclusiva de la migración a interfaces.

---

## 1. Organización Arquitectónica

### Estructura de paquetes obtenida

```
org.institution.app
├── Main.java                          ← Punto de composición
├── model/
│   ├── Student.java
│   ├── Teacher.java
│   ├── Course.java
│   └── Registration.java
├── repository/
│   ├── StudentRepository.java         ← Interfaz
│   ├── TeacherRepository.java         ← Interfaz
│   ├── CourseRepository.java          ← Interfaz
│   ├── RegistrationRepository.java    ← Interfaz
│   └── csv/
│       ├── CsvStudentRepository.java  ← Implementación
│       ├── CsvTeacherRepository.java  ← Implementación
│       ├── CsvCourseRepository.java   ← Implementación
│       └── CsvRegistrationRepository.java ← Implementación
├── service/
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── CourseService.java
│   └── RegistrationService.java
└── util/
    ├── Enum.java
    ├── FileManager.java
    ├── Helper.java
    ├── InputHelper.java
    └── Validator.java
```

### Evaluación

La separación entre interfaces (`repository/`) e implementaciones (`repository/csv/`) es **clara y correcta**. Los nombres siguen una convención consistente: la interfaz recibe el nombre del contrato genérico (`StudentRepository`) y la implementación lo prefija con la tecnología (`CsvStudentRepository`). Esta estructura facilita agregar implementaciones alternativas (MySQL, JSON, API remota) sin modificar los servicios.

**Resultado: ✅ Satisfactorio**

---

## 2. Diseño de los Contratos

### StudentRepository

```java
void newStudent(int id, String name, int age, String email);
Student getStudentByID(int id);
Student getStudentByEmail(String email);
ArrayList<Student> getStudentsByName(String name);
ArrayList<Student> getStudents();
boolean save();
boolean load();
int getLastID();
void deleteStudent(int id);
```

**Problema — `save()` y `load()` en el contrato:**  
Estos métodos pertenecen a la mecánica de persistencia específica de una tecnología. Representan una implementación CSV en la interfaz. En un diseño más desacoplado, la inicialización y el volcado de datos son responsabilidades de la implementación, no contratos que todos los repositorios deben exponer. Actualmente, cualquier implementación futura (ej. MySQL) deberá implementar `save()` y `load()` aunque no tenga sentido semántico para ella.

**Problema — `getLastID()`:**  
Este método expone un detalle de implementación. La generación del siguiente identificador es una responsabilidad interna del repositorio o de una capa de identidad. Incluirlo en el contrato obliga a todas las implementaciones futuras a gestionar un contador de ID, cuando una base de datos podría usar `AUTO_INCREMENT`.

**Problema — `newStudent(int id, ...)` recibe el ID como parámetro:**  
El servicio llama `repository.getLastID() + 1` y se lo pasa al repositorio. La asignación del ID debería ser interna al repositorio. El contrato mezcla responsabilidades del servicio y del repositorio.

### CourseRepository

Mismo análisis que `StudentRepository`. Se repiten los mismos tres problemas: `save()`, `load()`, `getLastID()`, y recepción del ID en `newCourse(int id, ...)`.

**Problema adicional — `deleteCourse(Course c)` vs `deleteStudent(int id)`:**  
La firma del método de eliminación es inconsistente entre interfaces. `StudentRepository` y `TeacherRepository` reciben un `int id`, mientras que `CourseRepository` recibe un objeto `Course c`. Esta inconsistencia dificulta el razonamiento sobre el sistema.

### TeacherRepository

Mismo análisis general. Se agrega:

**Problema menor — visibilidad de `lastID`:**  
En `CsvTeacherRepository`, el campo `int lastID` (línea 15) es package-private en lugar de `private`. Esto no pertenece al contrato, pero es un defecto de encapsulación en la implementación.

### RegistrationRepository

```java
void newRegistration(int studentID, int courseID);
Registration getRegistration(int stID, int cID);
ArrayList<Double> getStudentGrades(int id);
ArrayList<Registration> getRegistrations();
boolean save();
boolean load();
void deleteRegistration(int studentID, int courseID);
void deleteRegistrationsIndexes(ArrayList<Integer> indexes);
```

**Problema crítico — `deleteRegistrationsIndexes(ArrayList<Integer> indexes)`:**  
Este método expone un detalle de implementación interno. Los "índices" en un `ArrayList` son un concepto específico de la implementación en memoria. Una futura implementación (ej. SQL) no tendría índices posicionales. Este método rompe la abstracción de la interfaz y hace el contrato frágil.

**Problema — `getStudentGrades(int id)` en el repositorio:**  
Este método realiza una consulta de negocio (filtrado de calificaciones por alumno), lo cual podría pertenecer al servicio. Sin embargo, al nivel actual del proyecto, es aceptable mantenerlo en el repositorio como operación de consulta.

**Resultado: ⚠️ Aceptable con problemas — los contratos exponen detalles de implementación**

---

## 3. Acoplamiento

### Verificación de dependencias en servicios

| Servicio | Campo de repositorio | Tipo declarado | ¿Correcto? |
|---|---|---|---|
| `StudentService` | `repository` | `StudentRepository` | ✅ |
| `TeacherService` | `repository` | `TeacherRepository` | ✅ |
| `CourseService` | `repository` | `CourseRepository` | ✅ |
| `RegistrationService` | `repository` | `RegistrationRepository` | ✅ |

No se encontraron referencias directas a `CsvStudentRepository`, `CsvCourseRepository`, `CsvTeacherRepository` ni `CsvRegistrationRepository` dentro de ningún servicio.

**Resultado: ✅ Correcto — los servicios dependen únicamente de interfaces**

---

## 4. Inyección de Dependencias

### Servicios

Todos los servicios reciben sus repositorios mediante constructor:

```java
// StudentService.java
public StudentService(StudentRepository repo) {
    this.repository = repo;
}

// TeacherService.java
public TeacherService(TeacherRepository repo) {
    this.repository = repo;
}

// CourseService.java
public CourseService(CourseRepository repo, TeacherService teacherService) {
    this.repository = repo;
    this.teacherService = teacherService;
}

// RegistrationService.java
public RegistrationService(RegistrationRepository repo, CourseService cSer, StudentService sSer, TeacherService tSer) {
    ...
}
```

No se encontró ningún `new CsvXxxRepository()` dentro de ningún servicio.

### Punto de composición — Main.java

```java
// Main.java — líneas 15-24
final private static StudentRepository studentRepository = new CsvStudentRepository();
final private static TeacherRepository teacherRepository = new CsvTeacherRepository();
final private static CourseRepository courseRepository = new CsvCourseRepository();
final private static RegistrationRepository registrationRepository = new CsvRegistrationRepository();

final private static StudentService studentService = new StudentService(studentRepository);
final private static TeacherService teacherService = new TeacherService(teacherRepository);
final private static CourseService courseService = new CourseService(courseRepository, teacherService);
final private static RegistrationService registrationService = new RegistrationService(...);
```

La decisión de qué implementación utilizar ocurre exclusivamente en `Main.java`, que actúa como punto de composición. Los repositorios se declaran con el tipo de la interfaz, lo cual es correcto.

**Resultado: ✅ Excelente — inyección por constructor, punto de composición único y correcto**

---

## 5. Implementaciones

### Verificación de contrato completo

| Implementación | Implementa todos los métodos de la interfaz | `@Override` presente |
|---|---|---|
| `CsvStudentRepository` | ✅ | ✅ |
| `CsvCourseRepository` | ✅ | ✅ |
| `CsvTeacherRepository` | ✅ — ver nota | ⚠️ `load()` sin `@Override` |
| `CsvRegistrationRepository` | ✅ | ✅ |

**Problema — `CsvTeacherRepository.load()` (línea 82):**  
El método `load()` en `CsvTeacherRepository` no tiene la anotación `@Override`. Aunque compila correctamente, esto es una omisión que puede ocultar errores de firma en el futuro.

### Métodos públicos fuera del contrato

No se encontraron métodos públicos que no pertenezcan a la interfaz. Todas las implementaciones se limitan a los métodos definidos en sus contratos.

### Sustituibilidad

Las cuatro implementaciones son sustituibles entre sí dentro de su contrato respectivo.

**Resultado: ✅ Correcto con observación menor en `CsvTeacherRepository`**

---

## 6. Persistencia

### Organización de la persistencia

- Las colecciones en memoria (`ArrayList<Student>`, etc.) viven dentro de cada implementación concreta.
- Los datos se cargan al inicio del programa mediante `loadRepo()` en `Main.java`.
- Los datos se escriben mediante `save()` llamado explícitamente por el usuario o al salir.

La estrategia de carga única al inicio y escritura explícita es coherente con una implementación CSV.

### Problema — `FileManager` instanciado dos veces

En todas las implementaciones CSV se crean dos instancias de `FileManager` separadas para lectura y escritura:

```java
private FileManager reader = new FileManager();
private FileManager writer = new FileManager();
```

Si `FileManager` es stateless (sin estado interno), estas dos instancias son equivalentes y una sola es suficiente. Esto genera cuatro duplicaciones innecesarias.

**Resultado: ✅ Correcto — la persistencia está bien organizada, con redundancia menor**

---

## 7. Separación de Responsabilidades

### Servicios

Los servicios contienen lógica de negocio y coordinación. Sin embargo se observan dos casos donde la lógica de acceso a datos se duplica entre el servicio y el repositorio.

**Ejemplo — `StudentService.existStudent()` y `StudentService.getStudentByID()`:**  
Ambos métodos iteran directamente sobre `repository.getStudents()` en lugar de usar `repository.getStudentByID(id)` (que ya existe en la interfaz). Esto duplica la búsqueda lineal por ID.

```java
// StudentService.java — líneas 167-175: duplica lógica de búsqueda ya en el repositorio
public boolean existStudent(int id) {
    for (Student s : repository.getStudents()) {
        if (s.getId() == id) { return true; }
    }
    return false;
}

// StudentService.java — líneas 177-187: igualmente duplica búsqueda por ID
public Student getStudentByID(int id) {
    for (Student s : repository.getStudents()) {
        if (s.getId() == id) { return s; }
    }
    return null;
}
```

Equivalente correcto que ya existe en la interfaz:
```java
public boolean existStudent(int id) {
    return repository.getStudentByID(id) != null;
}
```

El mismo patrón se repite en `TeacherService.existTeacher()` y `TeacherService.getTeacherByID()`.

### Modelos

Los modelos (`Student`, `Teacher`, `Course`, `Registration`) representan únicamente entidades. No se detectó lógica de negocio o persistencia dentro de ellos.

### Utilidades

`Helper`, `Validator`, `FileManager`, `InputHelper` y `Enum` permanecen independientes del dominio de negocio. `Helper` sí conoce los modelos pero su rol de transformación es aceptable.

**Resultado: ⚠️ Aceptable — lógica de búsqueda duplicada entre servicio y repositorio**

---

## 8. Calidad de las Interfaces

Las cuatro interfaces tienen un tamaño razonable y cohesivo para el nivel del proyecto. No existen interfaces vacías ni contratos excesivamente grandes.

El único caso que merece análisis de necesidad: todas las interfaces poseen exactamente una implementación hoy. Sin embargo, el objetivo declarado del proyecto es facilitar futuras implementaciones (MySQL, SQLite, JSON), por lo que la existencia de estas interfaces **sí aporta desacoplamiento con valor real**. No se recomienda eliminarlas.

**Resultado: ✅ Satisfactorio**

---

## 9. Posibles Refactorizaciones (compatibles con el nivel actual)

Las siguientes refactorizaciones son aplicables sin requerir herencia, generics, Optional ni patrones GoF.

### R1 — Eliminar `save()` y `load()` de las interfaces de repositorio

Mover la responsabilidad de inicialización y volcado a métodos separados fuera del contrato, o aceptar que `save()`/`load()` son parte del ciclo de vida del repositorio pero documentar explícitamente que representan una convención de la implementación CSV.

Como mínimo, renombrar a términos más neutrales:
```java
boolean persist();   // en lugar de save()
boolean initialize(); // en lugar de load()
```

### R2 — Eliminar `getLastID()` del contrato

La generación del ID debe ser interna al repositorio. Cambiar las firmas de creación:

```java
// Interfaz: sin ID como parámetro
void newStudent(String name, int age, String email);

// Implementación: gestiona el ID internamente
@Override
public void newStudent(String name, int age, String email) {
    lastID++;
    students.add(new Student(lastID, name, age, email));
}
```

### R3 — Eliminar `deleteRegistrationsIndexes` de la interfaz

Reemplazarlo por un método semánticamente correcto:
```java
void deleteRegistrationsByStudentID(int studentID);
void deleteRegistrationsByCourseID(int courseID);
```

El servicio dejaría de construir listas de índices y delegaría la eliminación completa al repositorio.

### R4 — Usar `repository.getStudentByID()` en los servicios

```java
// En lugar de iterar manualmente:
public boolean existStudent(int id) {
    return repository.getStudentByID(id) != null;
}

public Student getStudentByID(int id) {
    return repository.getStudentByID(id);
}
```

### R5 — Agregar `@Override` a `CsvTeacherRepository.load()`

Corrección trivial de omisión.

### R6 — Unificar firma de eliminación en las interfaces

Decidir una convención consistente: eliminar siempre por ID o siempre por objeto.

```java
// Opción recomendada: siempre por ID
void deleteCourse(int id);   // en lugar de deleteCourse(Course c)
```

### R7 — Reducir instancias de `FileManager` a una por repositorio

```java
private FileManager fileManager = new FileManager();
// usar fileManager tanto para lectura como para escritura
```

---

## 10. Excepciones Arquitectónicas

### Dependencias prohibidas (servicios → implementaciones concretas)

**No encontradas.**

### Construcción interna de implementaciones (`new CsvXxx()` en servicios)

**No encontradas.**

### Uso de `instanceof`

**No encontrado.**

### Switch o if para distinguir implementaciones

**No encontrado en servicios ni repositorios.**  
El `switch` en `Main.showResume()` opera sobre strings de presentación, no sobre tipos de repositorio. Es aceptable.

### Casts innecesarios `(CsvXxx) repository`

**No encontrados.**

### Contratos que exponen detalles internos

**Encontrados:**
- `save()` / `load()`: terminología CSV.
- `getLastID()`: gestión interna de identidad.
- `deleteRegistrationsIndexes(ArrayList<Integer>)`: índices posicionales de `ArrayList`.

### Duplicación provocada por la migración

La migración introdujo cuatro bloques de lógica de búsqueda duplicada entre los servicios y los repositorios (ver sección 7). No se propone herencia como solución.

### Interfaces sin utilidad

Todas las interfaces tienen valor arquitectónico real como preparación para futuras implementaciones. No se recomienda eliminar ninguna.

---

## 11. Resultado Final

**Clasificación: Muy buena**

**Justificación:**

La migración logró su objetivo principal: los servicios dependen únicamente de abstracciones, la inyección de dependencias ocurre por constructor, y el punto de composición está correctamente aislado en `Main.java`. La estructura de paquetes es clara y coherente.

Los defectos encontrados son reales pero no invalidan la arquitectura:
- Los contratos exponen tres detalles de implementación (`save`, `load`, `getLastID`, `deleteRegistrationsIndexes`) que reducen la portabilidad futura.
- Existe duplicación de lógica de búsqueda entre servicios y repositorios.
- Una omisión menor de `@Override` y una inconsistencia en las firmas de eliminación.

Ninguno de estos defectos representa un error arquitectónico grave. La base es sólida y corregible antes de comenzar la siguiente etapa.

---

## 12. Informe Final

### Fortalezas

- **Desacoplamiento real entre servicios e interfaces.** Los cuatro servicios dependen exclusivamente de sus interfaces de repositorio. No existe ninguna referencia a implementaciones concretas dentro de la capa de servicios.
- **Punto de composición único y explícito.** `Main.java` centraliza la construcción de todas las dependencias. La decisión de qué implementación usar ocurre en un solo lugar.
- **Inyección por constructor en todos los servicios.** Patrón correcto, consistente y testeable.
- **Paquete `csv/` correctamente aislado.** Las implementaciones concretas están separadas del contrato.
- **Convención de nomenclatura coherente.** `XxxRepository` para interfaces, `CsvXxxRepository` para implementaciones.
- **Implementaciones completas.** Ningún método del contrato queda sin implementar.

---

### Debilidades

| # | Debilidad | Archivo(s) afectado(s) | Severidad |
|---|---|---|---|
| D1 | `save()` y `load()` en las interfaces exponen terminología de persistencia | Todas las interfaces | Media |
| D2 | `getLastID()` expone gestión interna de identidad | Todas las interfaces | Media |
| D3 | `deleteRegistrationsIndexes(ArrayList<Integer>)` expone índices posicionales internos | `RegistrationRepository` | Alta |
| D4 | Duplicación de lógica de búsqueda por ID en servicios (ignorando métodos del repositorio) | `StudentService`, `TeacherService` | Baja |
| D5 | Firma de eliminación inconsistente (`int id` vs `Course c`) | `CourseRepository` vs otras interfaces | Baja |
| D6 | `@Override` ausente en `CsvTeacherRepository.load()` | `CsvTeacherRepository` | Baja |
| D7 | Doble instancia innecesaria de `FileManager` en cada repositorio | Todas las implementaciones CSV | Muy baja |

---

### Riesgos

- **R-1 — `deleteRegistrationsIndexes` bloqueará la sustitución del repositorio.** Si en una etapa posterior se intenta agregar una implementación SQL o JSON, este método hará que el contrato sea imposible de implementar de forma natural. Es el riesgo más alto de cara a la siguiente etapa.

- **R-2 — `save()` y `load()` como contrato complicarán implementaciones futuras.** Un repositorio MySQL no tiene un ciclo de vida de "guardar todo al final". Implementar estos métodos como no-ops (vacíos) sería técnicamente incorrecto y engañoso.

- **R-3 — `getLastID()` acoplará futuras implementaciones a gestión manual de IDs.** Una base de datos con clave autogenerada no puede respetar este contrato naturalmente.

---

### Recomendaciones

Ordenadas por prioridad antes de comenzar la etapa de Polimorfismo:

1. **[Alta]** Reemplazar `deleteRegistrationsIndexes(ArrayList<Integer>)` por métodos semánticos en `RegistrationRepository` (`deleteRegistrationsByStudentID`, `deleteRegistrationsByCourseID`).

2. **[Media]** Revisar si `save()` y `load()` deben permanecer en las interfaces o gestionarse como parte del ciclo de vida interno de cada implementación. Si se mantienen, documentar explícitamente que son convenciones de persistencia por lotes.

3. **[Media]** Mover la generación de ID al interior de cada implementación y eliminar `getLastID()` del contrato.

4. **[Baja]** Usar `repository.getStudentByID(id)` en `StudentService.existStudent()` y `StudentService.getStudentByID()` en lugar de iterar manualmente.

5. **[Baja]** Unificar la firma de eliminación en todas las interfaces (preferir `deleteXxx(int id)`).

6. **[Baja]** Agregar `@Override` a `CsvTeacherRepository.load()`.

7. **[Muy baja]** Reducir a una sola instancia de `FileManager` por repositorio.
