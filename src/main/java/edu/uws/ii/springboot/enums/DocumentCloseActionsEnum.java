package edu.uws.ii.springboot.enums;

public enum DocumentCloseActionsEnum {
    Delete("Usuń"), Approve("zatwierdź"), Cancell("Anuluj");

    private final String description;

    DocumentCloseActionsEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
