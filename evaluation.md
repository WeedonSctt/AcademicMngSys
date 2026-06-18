
# Auditoría Completa del Proyecto Java

Analiza el repositorio completo y genera un informe técnico exhaustivo.

NO te limites a describir el código. Debes inspeccionar la implementación real y determinar qué tan completo está el proyecto.

---

# 1. Resumen General

Describe:

* Objetivo del proyecto.
* Funcionalidades implementadas.
* Funcionalidades faltantes.
* Estado general de completitud.
* Estimación de porcentaje completado.

---

# 2. Estructura del Proyecto

Muestra:

* Árbol de directorios.
* Paquetes utilizados.
* Archivos Java existentes.
* Responsabilidad aparente de cada paquete.

Determina si la organización es coherente o caótica.

---

# 3. Arquitectura

Identifica:

* Capas existentes.
* Separación entre interfaz, lógica y persistencia.
* Dependencias entre paquetes.
* Flujo general de datos.

Determina:

* Si existe separación de responsabilidades.
* Si el diseño es mantenible.
* Si hay acoplamiento excesivo.

Explica con ejemplos concretos.

---

# 4. Clases

Para cada clase:

Indica:

* Nombre.
* Responsabilidad.
* Campos.
* Métodos públicos.
* Dependencias.

Determina:

* Si la clase tiene una única responsabilidad.
* Si está demasiado grande.
* Si concentra lógica que debería estar en otro lugar.

Identifica posibles clases "Dios" (God Objects).

---

# 5. Modelado del Dominio

Analiza:

* Estudiantes.
* Profesores.
* Cursos.
* Inscripciones.
* Cualquier otra entidad.

Determina:

* Si los modelos representan correctamente el dominio.
* Si faltan atributos importantes.
* Si existen atributos innecesarios.

Explica la calidad del modelado.

---

# 6. Relaciones Entre Objetos

Describe:

* Cómo se relacionan las entidades.
* Cómo se representan las relaciones.

Por ejemplo:

* Curso ↔ Profesor.
* Curso ↔ Estudiante.
* Inscripción ↔ Curso.
* Inscripción ↔ Estudiante.

Evalúa:

* Si el diseño es correcto.
* Si hay duplicación de información.
* Si existen inconsistencias potenciales.

---

# 7. Uso de ArrayList y Colecciones

Identifica:

* Todas las colecciones utilizadas.
* Dónde se usan.
* Para qué se usan.

Evalúa:

* Si fueron elegidas correctamente.
* Si existen estructuras mejores.
* Si se realizan búsquedas costosas innecesarias.

Indica complejidades relevantes.

---

# 8. Persistencia

Analiza completamente:

* Lectura de archivos.
* Escritura de archivos.
* Formato utilizado.
* Parseo.
* Serialización.

Determina:

* Qué archivos se generan.
* Qué información se almacena.
* Cómo se reconstruyen los objetos.

Busca:

* Corrupción posible de datos.
* Casos donde el sistema puede romperse.
* Manejo de errores.

---

# 9. Formato CSV o Similar

Si se utiliza CSV:

Analiza:

* Estrategia de serialización.
* Estrategia de parseo.

Determina:

* Si soporta comas en texto.
* Si soporta datos inválidos.
* Si puede generar registros inconsistentes.

---

# 10. Validaciones

Identifica todas las validaciones implementadas.

Por ejemplo:

* IDs.
* Correos.
* Rangos numéricos.
* Cupos.
* Nombres vacíos.
* Duplicados.

Determina:

* Qué validaciones faltan.
* Qué validaciones son redundantes.
* Qué validaciones son incorrectas.

---

# 11. Invariantes

Identifica las invariantes del sistema.

Ejemplos:

* No duplicar estudiantes.
* No exceder cupos.
* No inscribir alumnos inexistentes.
* No asignar profesores inexistentes.

Determina:

* Qué invariantes están protegidas.
* Cuáles pueden romperse.
* Cómo podrían romperse.

Este apartado es prioritario.

---

# 12. Flujo de Menús

Analiza:

* Menú principal.
* Submenús.
* Navegación.

Determina:

* Si la experiencia es consistente.
* Si existen opciones rotas.
* Si existen ciclos o bloqueos.

---

# 13. Entrada de Datos

Analiza:

* Scanner.
* Métodos auxiliares.
* Conversión de tipos.

Busca:

* Errores de entrada.
* Posibles excepciones.
* Casos no contemplados.

---

# 14. Manejo de Errores

Identifica:

* Try/catch existentes.
* Excepciones capturadas.
* Excepciones ignoradas.

Determina:

* Qué errores podrían tumbar el sistema.
* Qué errores se manejan correctamente.

---

# 15. Reutilización de Código

Busca:

* Lógica duplicada.
* Métodos repetidos.
* Bloques repetidos.

Indica:

* Dónde existe duplicación.
* Cómo podría eliminarse.

---

# 16. Calidad de Nombres

Evalúa:

* Clases.
* Métodos.
* Variables.
* Constantes.

Determina:

* Si los nombres expresan intención.
* Si existen nombres ambiguos.

---

# 17. Cohesión

Evalúa para cada clase:

* Qué tan relacionada está su funcionalidad interna.

Clasifica:

* Alta cohesión.
* Media cohesión.
* Baja cohesión.

Justifica.

---

# 18. Acoplamiento

Evalúa:

* Dependencias entre clases.
* Dependencias entre paquetes.

Indica:

* Clases excesivamente dependientes.
* Riesgos de mantenimiento.

---

# 19. Complejidad

Identifica:

* Métodos largos.
* Condicionales anidados.
* Código difícil de seguir.

Indica:

* Métodos candidatos a refactorización.

---

# 20. Escalabilidad

Determina qué tan fácil sería agregar:

* Nuevos tipos de usuarios.
* Nuevos reportes.
* Nuevos módulos.
* Nuevos formatos de almacenamiento.

Justifica técnicamente.

---

# 21. Código Muerto

Busca:

* Métodos sin uso.
* Variables sin uso.
* Clases sin uso.

Lista todo lo encontrado.

---

# 22. Posibles Bugs

Inspecciona cuidadosamente:

* Casos límite.
* Null.
* Archivos vacíos.
* Datos corruptos.
* IDs repetidos.
* Cursos eliminados.
* Inscripciones huérfanas.

Genera una lista priorizada de bugs potenciales.

---

# 23. Refactorizaciones Recomendadas

Clasifica:

## Críticas

Problemas que deberían corregirse antes de continuar.

## Importantes

Problemas que afectan mantenibilidad.

## Opcionales

Mejoras de calidad.

---

# 24. Comparación Contra Objetivos del Proyecto

Determina si el proyecto cumple:

* Modularidad.
* Separación de responsabilidades.
* Uso correcto de listas.
* Persistencia.
* Diseño de invariantes.
* Organización en múltiples archivos.
* Reutilización de código.

Califica cada punto de 0 a 10.

---

# 25. Evaluación Final

Genera:

* Puntuación técnica global (0-100).
* Fortalezas principales.
* Debilidades principales.
* Nivel aproximado del programador que desarrolló el proyecto.
* Próximos conceptos recomendados para aprender.

La evaluación debe estar basada exclusivamente en el código existente.
