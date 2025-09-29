package com.example.geonotesteaching;

import java.util.*;
import java.util.stream.Collectors;
// La clase 'Timeline' usa un 'SequencedMap' para mantener las notas en orden de inserción.
// A diferencia de un HashMap, un 'SequencedMap' garantiza el orden y permite acceder
// al primer y último elemento de forma eficiente.
// Ajustada la indentación del text block.
// Con "\"" se le dice que es una comilla literal y con "\\\"" se le dice que es \" al compilador.
final class Timeline {
    private final Map<Long, Note> notes = new LinkedHashMap<>();

    public void addNote(Note note) { notes.put(note.id(), note); }
    public Note getNote(long id) { return notes.get(id); }
    public Map<Long, Note> getNotes() { return notes; }

    // Esta clase final genera la salida JSON usando 'text blocks'.
    public final class Render extends AbstractExporter implements Exporter {
        @Override public String export() {
            var notesList = notes.values().stream()
                // Un 'text block' es una cadena de texto multilinea que no necesita
                // concatenación ni caracteres de escape para las comillas.
                .map(note -> """
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
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.joining(",\n"));
            return """
                    { "notes": [ %s ] }
                    """.formatted(notesList);
        }
    }

    //
    public java.util.List<Note> latest(int n) {
        return getNotes()
                .values().stream()
                .sorted(java.util.Comparator.comparing(Note::createdAt).reversed())
                .limit(n)
                .toList();
    }
}