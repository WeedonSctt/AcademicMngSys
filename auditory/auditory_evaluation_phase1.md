# Auditoría Técnica — Migración a Interfaces (Etapa 1 POO)

## Objetivo

Esta auditoría evalúa exclusivamente la migración realizada para incorporar interfaces al proyecto académico.

No deben evaluarse aspectos relacionados con:

* Estilo de código.
* Interfaz de usuario.
* Rendimiento.
* Nuevas funcionalidades.
* Optimizaciones no relacionadas con interfaces.

La revisión debe centrarse únicamente en la arquitectura resultante tras introducir interfaces y desacoplar los servicios de las implementaciones concretas.

---

# Contexto

El proyecto originalmente utilizaba repositorios concretos.

Después de la migración se espera una arquitectura similar a:

```
UI
↓
Services
↓
Repository Interfaces
↓
CSV Implementations
↓
Persistencia
```

La intención principal fue aplicar el concepto de:

> Programar contra abstracciones y no contra implementaciones.

---

# Objetivos de la Auditoría

Verificar que la migración realmente produjo un desacoplamiento arquitectónico y que las interfaces representan contratos útiles, sin introducir complejidad innecesaria.

---

# 1. Organización Arquitectónica

Verificar:

* Que existan interfaces separadas de sus implementaciones.
* Que las implementaciones concretas se encuentren claramente diferenciadas.
* Que los nombres de interfaces e implementaciones sean consistentes.
* Que la organización de paquetes refleje correctamente la separación entre contrato e implementación.

Determinar si la estructura obtenida facilita agregar futuras implementaciones como:

* MySQL
* SQLite
* JSON
* API remota
* Memoria

sin modificar la lógica de negocio.

---

# 2. Diseño de los Contratos

Para cada interfaz de repositorio analizar:

* Si representa realmente un contrato.
* Si los métodos pertenecen naturalmente al repositorio.
* Si existen métodos que deberían pertenecer al servicio y no al repositorio.
* Si faltan operaciones importantes.
* Si existen operaciones redundantes.
* Si el contrato revela detalles internos de la implementación.

Analizar especialmente:

* nombres de métodos
* parámetros
* tipos de retorno
* cohesión del contrato

No limitarse únicamente a verificar compilación.

---

# 3. Acoplamiento

Verificar que los servicios dependan únicamente de interfaces.

Buscar referencias directas hacia implementaciones concretas como:

```
CsvStudentRepository
CsvCourseRepository
CsvProfessorRepository
CsvEnrollmentRepository
```

Determinar si existen dependencias innecesarias.

Indicar exactamente dónde aparecen.

---

# 4. Inyección de Dependencias

Verificar:

* cómo reciben los servicios sus repositorios.
* si la inyección ocurre mediante constructor.
* si existen instancias creadas directamente mediante `new` dentro de los servicios.

No deberían existir casos donde un servicio construya su propia implementación.

Ejemplo NO esperado:

```java
private StudentRepository repo =
    new CsvStudentRepository();
```

La decisión de qué implementación utilizar debería ocurrir únicamente en el punto de composición del programa (Main o equivalente).

---

# 5. Implementaciones

Revisar cada implementación concreta.

Determinar:

* si implementa correctamente el contrato.
* si aparecen métodos públicos que no pertenecen a la interfaz.
* si dichos métodos adicionales están justificados.
* si existen implementaciones vacías o artificiales.

Verificar que cada implementación pueda sustituir completamente a cualquier otra implementación del mismo contrato.

---

# 6. Persistencia

Analizar cómo quedó organizada la persistencia.

Verificar especialmente:

* dónde viven las colecciones en memoria.
* cuándo se cargan los datos.
* cuándo se escriben los datos.
* si el repositorio conserva correctamente su responsabilidad.

Determinar si la implementación elegida resulta coherente para una implementación CSV.

---

# 7. Separación de Responsabilidades

Determinar si después de la migración:

Servicios:

* contienen únicamente lógica de negocio.

Repositorios:

* contienen únicamente persistencia.

Modelos:

* representan únicamente entidades.

Utilidades:

* permanecen independientes del dominio.

Buscar responsabilidades mezcladas.

---

# 8. Calidad de las Interfaces

Determinar si existen:

* interfaces innecesarias.
* interfaces excesivamente pequeñas.
* contratos demasiado grandes.
* métodos que rompan el principio de responsabilidad única.

Evaluar si la abstracción aporta valor real o únicamente añade capas.

---

# 9. Posibles Refactorizaciones

Proponer únicamente refactorizaciones relacionadas con interfaces.

No sugerir todavía:

* herencia
* clases abstractas
* generics
* Optional
* HashMap
* patrones GoF

Estas tecnologías pertenecen a etapas posteriores del roadmap.

Las recomendaciones deben ser compatibles únicamente con el nivel actual del proyecto.

---

# 10. Excepciones Arquitectónicas (No deberían existir)

La auditoría debe buscar explícitamente los siguientes casos.

## Dependencias prohibidas

Servicios dependiendo de implementaciones concretas.

Ejemplo:

```java
private CsvStudentRepository repository;
```

---

## Construcción interna de implementaciones

Servicios creando sus propios repositorios mediante:

```java
new CsvStudentRepository(...)
```

---

## Uso innecesario de instanceof

No deberían existir comprobaciones de implementación concreta.

Ejemplo:

```java
if (repository instanceof CsvStudentRepository)
```

---

## Switch o if para distinguir implementaciones

No deberían existir decisiones del tipo:

```java
if (repositoryType.equals(...))
```

o

```java
switch(repositoryType)
```

dentro de servicios o repositorios.

La implementación debe decidirse mediante polimorfismo de interfaces y composición del programa.

---

## Casts innecesarios

No deberían aparecer conversiones como:

```java
(CsvStudentRepository) repository
```

para acceder a métodos específicos.

Si existen, indicar:

* ubicación
* motivo
* alternativa recomendada

---

## Contratos que exponen detalles internos

Buscar interfaces que revelen implementación concreta.

Ejemplos:

* nombres relacionados con CSV
* tipos internos innecesarios
* dependencias hacia utilidades específicas

---

## Duplicación provocada por la migración

Determinar si la incorporación de interfaces generó duplicación significativa.

No proponer herencia como solución.

Únicamente documentar la duplicación encontrada.

---

## Interfaces sin utilidad

Buscar interfaces que posean exactamente una implementación y cuya existencia no aporte desacoplamiento ni facilite futuras extensiones.

Si existen, justificar técnicamente si conviene mantenerlas o eliminarlas.

---

# 11. Resultado Final

Clasificar la migración utilizando una de las siguientes categorías.

* Excelente
* Muy buena
* Correcta
* Aceptable con problemas
* Requiere rediseño

Justificar la clasificación.

---

# 12. Informe Final

El informe debe contener:

## Fortalezas

Aspectos arquitectónicos positivos introducidos por la migración.

---

## Debilidades

Problemas detectados relacionados únicamente con interfaces.

---

## Riesgos

Diseños que podrían dificultar futuras etapas del roadmap.

---

## Recomendaciones

Cambios sugeridos antes de comenzar la siguiente etapa (Polimorfismo).

Las recomendaciones deben respetar el nivel técnico actual del proyecto y no adelantarse a conceptos que todavía no forman parte del plan de estudio.
