package edu.uws.ii.springboot.enums;

public enum DocumentTypeEnum {
    PZ("Przyjęcie zewnętrzne"),
    WZ("Wydanie zewnętrzne"),
    PM("Przesunięcie magazynowe"),
    PS("Przesunięcie sektorowe");

    private final String description;

    DocumentTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
