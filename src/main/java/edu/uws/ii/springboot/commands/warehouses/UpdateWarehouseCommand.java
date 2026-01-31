package edu.uws.ii.springboot.commands.warehouses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWarehouseCommand {

    private int id;
    private String code;
    private String name;
    private String description;

    public UpdateWarehouseCommand() {

    }
}
