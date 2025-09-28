package com.example.geonotesteaching;

// Una 'sealed interface' para la jerarquía de exportadores.
// 'non-sealed' permite que otras clases fuera de este archivo la extiendan,
// mientras que 'final' impide cualquier otra extensión.
public sealed interface Exporter permits AbstractExporter, JsonExporter, MarkdownExporter, Timeline.Render {
    String export();
}