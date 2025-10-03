## Alumno(s):
Nombre y Apellidos: 
- David Benavides Foncubierta
- Vìctor Gómez Tejada

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
## A2. Equals/HashCode vs. Records (conceptual)
### Enunciado Original: 
 Objetivo: entender qué genera un record.  
 
 • Crea una clase LegacyPoint (clásica, no record) con double lat, lon, equals, hashCode y toString
 manuales.  
 • Compara su uso con GeoPoint. Entrega: breve comentario en el código o README: ¿qué ventajas /
 cuándo no usar record?  
### Solución:
Las ventajas de usar record son:  
      - Genera automaticamente los getters y los setters.  
      - Genera automaticamente el constructor.  
      - Genera automaticamente lo `equals()`, `toString()` y `hashCode()`.  
      - Los campos son final por lo que no se cambiarán accidentalmente, esto sirve para tener un código más limpio.  
      - Elimina código repetitivo de uso frecuente y facilita el mantenimiento y legibilidad.  
      - Compatible con patrones sealed o pattern matching.  
      
¿Cuando no usar record?  
- Cuando el constructor es complejo y necesitas mutabilidad de los campos una vez creado el objeto.  
- Cuando necesitas extender otras clases, ya que solo pueden implementar interfaces.  
- Cuando no te conviene usar todos los campos en `equals()` y `hasCode()`.  
```java
public class LegacyPoint{
    private double lat,lon;

    public LegacyPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "LegacyPoint{} " + "latitud = " + lat + "longitud = " + lon;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LegacyPoint)) {
            return false;
        }else{
            return this == o;
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
```

## Bloque B — Jerarquía sealed y switch moderno
## B1. Nuevo subtipo: Video  
### Enunciado original
Objetivo: ampliar jerarquía sellada.  

• Crea public record Video(String url, int width, int height, int seconds) implements
Attachment.  
• Actualiza Attachment (permits …) para incluir Video.  
• Añade soporte en Describe.describeAttachment:  
```java
case Video v when v.seconds() > 120-> "￿ Vídeo largo";
case Video v-> "￿ Vídeo";
```
• Exhaustividad: comprueba que el switch obliga a cubrir Video.  
### Solución:
Para comprobar la exhaustividad del switch la interfaz debe ser sellada y todos los subtipos enumerados explicitamente en `switch`. 
Mientras cumplas estas condiciones el compilador no se quejará.
```java
package com.example.geonotesteaching;

public record Video(String url, int width, int height, int seconds) implements Attachment {
}
```
```java
public sealed interface Attachment permits Audio, Link, Photo, Video {
}
```
```java
public static String describeAttachment(Attachment a) {
        return switch (a) {
            case Photo p when p.width() > 1920 -> "📷 Foto en alta definición (%d x %d)".formatted(p.width(), p.height());
            case Photo p -> "📷 Foto";
            case Audio audio when audio.duration() > 300-> {
                var mins = audio.duration() / 60;
                yield "￿ Audio (" + mins + " min)";
            }            case Audio audio -> "🎵 Audio";
            case Link l -> "🔗 %s".formatted(l.effectiveLabel());
            case Video v when v.seconds() > 120 -> "Video";
            case Video v -> "Video";
        };
```

## B2. Formato corto vs. largo en switch
### Enunciado original
Objetivo: usar yield con bloques.  

• Cambia alguna rama de Describe a bloque:  
case Audio a when a.duration() > 300-> {
var mins = a.duration() / 60;
yield "￿ Audio (" + mins + " min)";
}
• Asegúrate de compilar y probar.
### Solución:
Cuando el `switch` se usa como expresión para devolver un valor porque produce un resultado, se debe usar un `yield` para meter mas lógica dentro del `case` en cuestión.  
Compila porque el `switch` está devolviendo una expresión. Se usa un `yield` obligatorio para devolver una expresión del `case`. Todos los subtipos de `Attachment` están cubiertos.
```java
final class Describe {
    public static String describeAttachment(Attachment a) {
        return switch (a) {
            case Photo p when p.width() > 1920 -> "📷 Foto en alta definición (%d x %d)".formatted(p.width(), p.height());
            case Photo p -> "📷 Foto";
            case Audio audio when audio.duration() > 300-> {
                var mins = audio.duration() / 60;
                yield "￿ Audio (" + mins + " min)";
            }
            case Audio audio -> "🎵 Audio";
            case Link l -> "🔗 %s".formatted(l.effectiveLabel());
            case Video v when v.seconds() > 120 -> "Video";
            case Video v -> "Video";
        };
    }
```
## Bloque C — Text Blocks y exportación
## C1. Export JSON pretty
### Enunciado original

## C2. Export Markdown (extra)
### Enunciado original

## Bloque D — Colecciones y orden
## D1. Orden por fecha y límite
### Enunciado original

## D2. Búsqueda con varios criterios
### Enunciado original

## Bloque E — Pattern Matching + Record Patterns (Java 21)
## E1. instanceof con patrón
### Enunciado original

## E2. Record patterns en if o switch
### Enunciado original

## Bloque F — Errores y robustez
## F1. Manejo de InputMismatch/NumberFormat
### Enunciado original

## F2. Comprobaciones nulas
### Enunciado original
