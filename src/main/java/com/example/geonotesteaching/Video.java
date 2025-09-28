package com.example.geonotesteaching;

public record Video(String url, int width, int height, int seconds) implements Attachment {
}
