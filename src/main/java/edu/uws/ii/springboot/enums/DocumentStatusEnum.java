package edu.uws.ii.springboot.enums;

public enum DocumentStatusEnum {
    DRAFT("Szkic"), POSTED("Zatwierdzony"), CANCELLED("Anulowany");

    private final String description;

    DocumentStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
