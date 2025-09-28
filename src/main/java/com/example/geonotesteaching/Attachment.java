package com.example.geonotesteaching;

// Una 'sealed interface' permite controlar qué clases o records pueden implementarla.
// Esto es útil para modelar jerarquías cerradas y seguras.
public sealed interface Attachment permits Audio, Link, Photo, Video {
}