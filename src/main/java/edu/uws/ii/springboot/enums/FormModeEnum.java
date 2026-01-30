package edu.uws.ii.springboot.enums;

public enum FormModeEnum {
    VIEW, EDIT, CREATE;

    public static FormModeEnum from(String raw) {
        if (raw == null) return VIEW;
        return switch (raw.toLowerCase()) {
            case "edit" -> EDIT;
            case "create" -> CREATE;
            default -> VIEW;
        };
    }

    public boolean isReadOnly() {
        return this == VIEW;
    }
}