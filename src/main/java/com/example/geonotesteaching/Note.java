package com.example.geonotesteaching;

import java.time.Instant;

// Un 'record' para la nota, que incluye un campo de tipo 'sealed interface'.
// El 'id' se genera automáticamente usando el hash code y el timestamp, aunque
// en un entorno real usaríamos un UUID.
public record Note(long id, String title, String content, GeoPoint location, Instant createdAt, Attachment attachment) {
    public Note {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title requerido");
        if (title.length() < 3) throw new IllegalArgumentException("El título debe tener un mínimo de tres caracteres.");
        if (content == null) content = ""; else content = content.trim();
        if (content.isEmpty()) content = "-";
        if (location == null) throw new IllegalArgumentException("location requerido");
        if (createdAt == null) createdAt = Instant.now();
    }
}