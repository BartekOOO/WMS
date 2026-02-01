package edu.uws.ii.springboot.enums;

public enum SectorTypeEnum {
    Normal("Sektor"),
    LoadingHub("Hub załadunkowy"),
    UnloadingHub("Hub rozładunkowy");

    private final String description;

    SectorTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
