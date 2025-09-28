package com.example.geonotesteaching;

// Esta clase usa 'switch expressions' y 'pattern matching' para describir un 'Attachment'.
// Los 'switch expressions' permiten que el 'switch' sea una expresión que devuelve un valor.
// El 'pattern matching' en el 'case' permite desestructurar el objeto y
// aplicar una condición ('when') de forma concisa.
//Java exige que el switch sea exhaustivo cuando se usa como expresión, es por eso
//que todos los tipos concretos de Attachment deben estar cubiertos por un case.
//El primer case de Video se aplica solo si el video dura más de 120 segundos. El segundo
//case funciona como "fallback" que cubre todos los demás casos del tipo video que no cumplen la condición.
//Se ha cambiado el valor de la variable a a audio ya que el compilador no sabe si a se refería
// al parámetro del método o al patrón del case.
final class Describe {
    public static String describeAttachment(Attachment a) {
        return switch (a) {
            case Photo p when p.width() > 1920 -> "📷 Foto en alta definición (%d x %d)".formatted(p.width(), p.height());
            case Photo p -> "📷 Foto";
            case Audio audio when audio.duration() > 300-> {
                var mins = audio.duration() / 60;
                yield "￿ Audio (" + mins + " min)";
            }            case Audio audio -> "🎵 Audio";
            case Link l -> "🔗 %s".formatted((l.label() == null || l.label().isEmpty()) ? l.url() : l.label());
            case Video v when v.seconds() > 120 -> "Video";
            case Video v -> "Video";
        };
    }
}