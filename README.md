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

## Bloque A — Fundamentos y calentamiento
## A1. Validación y excepciones
### Enunciado Original:
Objetivo: reforzar validación clásica y mensajes claros. 

- En Note, añade validaciones adicionales:  
– title → mínimo 3 caracteres.  
– content → recorta con trim(); si queda vacío, usa "–".  
- Maneja la excepción en el menú (ya lo hace) y muestra un mensaje útil.  

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
 
- Crea una clase LegacyPoint (clásica, no record) con double lat, lon, equals, hashCode y toString manuales.  
- Compara su uso con GeoPoint. Entrega: breve comentario en el código o README: ¿qué ventajas / cuándo no usar record?  
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
- Crea `public record Video(String url, int width, int height, int seconds) implements Attachment`.  
- Actualiza Attachment (permits …) para incluir `Video`.  
- Añade soporte en `Describe.describeAttachment`:  
```java
case Video v when v.seconds() > 120-> "￿ Vídeo largo";
case Video v-> "￿ Vídeo";
```
- Exhaustividad: comprueba que el switch obliga a cubrir `Video`.  
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

- Cambia alguna rama de Describe a bloque:
```
case Audio a when a.duration() > 300-> {
  var mins = a.duration() / 60;
yield "￿ Audio (" + mins + " min)";
}
```
- Asegúrate de compilar y probar.
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
Objetivo: mejorar legibilidad del JSON.
- En Timeline.Render.export(), ajusta el text block para alinear y sangrar mejor.
- Escapa comillas del content si hiciera falta (p. ej., replace("\"","\\\"") antes de formatted).

### Solución
Se mejoró la exportación a JSON en `Timeline.Render.export()`, ajustando la indentación del text block para mayor legibilidad.
Además, se escaparon las comillas en title y content para garantizar un formato válido en la salida.
```java
{
  "id": %d,
  "title": "%s",
  "content": "%s",
  "location": { "lat": %f, "lon": %f },
  "createdAt": "%s"
}
""".formatted(
    note.id(), note.title().replace("\"","\\\""), note.content().replace("\"","\\\""),
    note.location().lat(), note.location().lon(),
    note.createdAt()))
```

## C2. Export Markdown (extra)
### Enunciado original
Objetivo: practicar text blocks.
- Crea MarkdownExporter (implementa Exporter) que genere:
`# GeoNotes - [ID 1] Título — (lat, lon) — YYYY-MM-DD`

### Solución
// TODO VICTOR

## Bloque D — Colecciones y orden
## D1. Orden por fecha y límite
### Enunciado original
Objetivo: practicar Streams y Comparator.
- Añade método en Timeline: 
`public java.util.List<Note> latest(int n)` que devuelva las n notas más recientes (por `createdAt` descendente).
- Añade opción en CLI: “Listar últimas N”.

### Solución
Se añadió el método `latest(int n)` en Timeline para obtener las n notas más recientes, ordenadas por fecha de creación en orden descendente.
```java
    public java.util.List<Note> latest(int n) {
        return getNotes()
                .values().stream()
                .sorted(java.util.Comparator.comparing(Note::createdAt).reversed())
                .limit(n)
                .toList();
    }
}
```
En la CLI se incorporó una nueva opción que permite al usuario listar las últimas N notas introduciendo el número deseado.
```java
   private static void getLatestNotes() {
        System.out.println("Introduce el número de notas que deseas ver: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        var latestNotes = timeline.latest(choice);
        for (Note note : latestNotes) {
            System.out.println(note.toString());
        }
    }
```

## D2. Búsqueda con varios criterios
### Enunciado original
Objetivo: filtros encadenados.
- En CLI, añade una opción “Buscar avanzada”:
  – Por rango de lat/lon (ej.: lat entre A–B).
  – Por palabra clave en `title` o `content`.
- Reutiliza `Match.isInArea` o crea un método auxiliar.

### Solución
// TODO VICTOR

## Bloque E — Pattern Matching + Record Patterns (Java 21)
## E1. instanceof con patrón
### Enunciado original
Objetivo: simplificar casting.
En Describe añade un método `static int mediaPixels(Object o)` que:
– Si es Photo p, devuelva p.width() * p.height().
– Si es Video v, devuelva v.width() * v.height().
– Si no, 0.
Implementa con if (o instanceof Photo p) { ... }.

### Solución
Se añadió el método `mediaPixels(Object o)` en `Describe`, que calcula el número total de píxeles cuando el objeto es una foto o un vídeo. En cualquier otro caso devuelve 0.
```java
    static int mediaPixels(Object o) {
        if(o instanceof Photo) {
            return ((Photo) o).width() * ((Photo) o).height();
        } else if (o instanceof Video) {
            return ((Video) o).width() * ((Video) o).height();
        } else {
            return 0;
        }
    }
```

## E2. Record patterns en if o switch
### Enunciado original
Objetivo: desestructurar con patrón.
- Crea método en Match `static String where(GeoPoint p)` que use:

```java
return switch (p) {
  case GeoPoint(double lat, double lon) when lat == 0 && lon == 0 -> "ORIGIN";
  case GeoPoint(double lat, double lon) when lat == 0 -> "Equator";
  case GeoPoint(double lat, double lon) when lon == 0 -> "Greenwich";
  case GeoPoint(double lat, double lon) -> "(" + lat + "," + lon + ")";
};
```
- Añade opción CLI para consultar where.

### Solución
En `Match` se implementó el método `where(GeoPoint p)` utilizando record patterns y condiciones `when`.  
El `switch` devuelve `ORIGIN`, `Equator`, `Greenwich` o las coordenadas en formato `(lat,lon)` según corresponda.  
Se añadió también una opción en la CLI para consultar este resultado.  
```java
    public static String where(GeoPoint p) {
        return switch (p) {
            // Caso ORIGIN: patrón record + guarda que comprueba si lat y lon son ambos 0
            case GeoPoint(double lat, double lon) when lat == 0 && lon == 0 -> "ORIGIN";

            // Caso Equator: latitud 0, cualquier longitud
            case GeoPoint(double lat, double lon) when lat == 0 -> "Equator";

            // Caso Greenwich: longitud 0, cualquier latitud
            case GeoPoint(double lat, double lon) when lon == 0 -> "Greenwich";

            // Caso general: cualquier otro punto, se devuelve como texto "(lat,lon)"
            case GeoPoint(double lat, double lon) -> "(" + lat + "," + lon + ")";
        };
    }
```

## Bloque F — Errores y robustez
## F1. Manejo de InputMismatch/NumberFormat
### Enunciado original
Objetivo: entradas seguras.
- Asegura que todas las lecturas de números usan Double.parseDouble(scanner.nextLine()) y están en try/catch con mensajes claros (ya está iniciado en GeoNotes).

### Solución
Nos aseguramos de que todas las lecturas de valores numéricos se realicen con `Double.parseDouble(scanner.nextLine())` dentro de `try/catch`'s.

## F2. Comprobaciones nulas
### Enunciado original
Objetivo: práctica “clásica” (sin null-safety de Kotlin).
- Si label en Link es nulo/vacío, muestra la url al exportar (ya implementado en Describe; revisa consistencia en exportadores).

### Solución
Se cambió label por effectiveLabel en Describe, mostrando así la url al exportar en el caso de que sea null o vacío.
```java
case Link l -> "🔗 %s".formatted(l.effectiveLabel());
```
