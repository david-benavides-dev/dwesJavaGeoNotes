## Alumno(s):
- Nombre y Apellidos: David Benavides Foncubierta y Victor Gómez Tejada

## Introducción y primeros pasos

Proyecto docente con Java clásico + moderno (records, sealed, text blocks, switch expression, pattern matching).
Incluye Gradle Wrapper (scripts) para facilitar la ejecución.

### Ejecutar
- IntelliJ: Abrir carpeta y ejecutar tarea Gradle `run` o `examples`.
- Terminal:
  ```bash
  ./gradlew run
  ./gradlew examples
  ```

## Tabla de Contenidos
- [Ejercicios realizados](#ejercicios-realizados)
  - Bloque A - Fundamentos y calentamientos
     - A1. Validación y excepciones
     - A2. Equals/HashCode vs. Records (conceptual)
  - Bloque B - Jerarquiía sealed y switch moderno
     - B1. Nuevo subtipo: Video
     - B2. Formato corto vs largo en switch
  - Bloque C - Text Block y exportación
     - C1. Export JSON pretty
     - C2. Export Markdown (extra)
  - Bloque D - Coleccciones y orden
     - D1. Orden por fecha y límite
     - D2. Búsqueda con varios criterios
  - Bloque E - Pattern Matching + Record Patterns
     - E1. instanceof con patrón
     - E2. Record patterns en if o switch 
  - Bloque F - Errores y robustez
     - F1. Manejo de InputMismatch/NumberFormat
     - F2. Comprobaciones nulas
- [Notas sobre decisiones de diseño y Java vs Kotlin](#link)

## Bloque A — Fundamentos y calentamiento
## A1. Validación y excepciones
### Enunciado Original:
A1. Validación y excepciones  
Objetivo: reforzar validación clásica y mensajes claros. 

• En Note, añade validaciones adicionales:  
– title → mínimo 3 caracteres.  
– content → recorta con trim(); si queda vacío, usa "–".  
• Maneja la excepción en el menú (ya lo hace) y muestra un mensaje útil.  

Pista: usa el compact constructor del record.
### Solución:
En el record `Note` se añadieron validaciones adicionales mediante el `compact constructor`.  
Se comprueba que el título cumpla el tamaño mínimo (lanzando una excepción `IllegalArgumentException` en el caso de que no se cumpla) y que el contenido se normalice (con `trim()`, sustituyendo por "-" cuando queda vacío).  
De esta forma, se asegura la coherencia de los datos y se muestran mensajes de error claros al usuario cuando la entrada no es válida.  
```java
if (title.length() < 3) throw new IllegalArgumentException("El título debe tener un mínimo de tres caracteres.");
if (content == null) content = ""; else content = content.trim();
if (content.isEmpty()) content = "-";
```
