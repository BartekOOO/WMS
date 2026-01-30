package edu.uws.ii.springboot.enums;

public enum CustomerTypeEnum {
    Supplier("Dostawca"),
    Receiver("Odbiorca"),
    SupplierReceiver("Dostawco-Odbiorca");

    private final String description;

    CustomerTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
