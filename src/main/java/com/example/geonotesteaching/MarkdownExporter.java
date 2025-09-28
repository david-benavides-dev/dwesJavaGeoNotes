package com.example.geonotesteaching;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class MarkdownExporter implements Exporter{
    private Note note;
    private GeoPoint location;

    public MarkdownExporter(Note note, GeoPoint location) {
        this.note = note;
        this.location = location;
    }

    @Override
    public String export() {
        return """
                # GeoNotes
                [ID]: %d - Título: %s - (%.4f, %.4f) - %s
                """.formatted(note.id(), note.title(), location.lat(), location.lon(), note.createdAt().atZone(ZoneId.of("Europe/Madrid")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}

